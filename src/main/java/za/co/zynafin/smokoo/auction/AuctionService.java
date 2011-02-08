package za.co.zynafin.smokoo.auction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.parser.TimeRemainingParser;
import za.co.zynafin.smokoo.io.ApplicationMessageSender;
import za.co.zynafin.smokoo.io.SmokooConnector;

@Service
public class AuctionService implements ApplicationListener<AutomateAuctionEvent> {

	private static final Logger log = Logger.getLogger(AuctionService.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AuctionDao auctionDao;
	@Autowired
	private SmokooConnector smokooConnector;
	@Autowired
	private TimeRemainingParser timeRemainingParser;
	@Autowired
	private ApplicationMessageSender applicationMessageSender;

	private AuctionIdRandomizer randomizer;

	@SuppressWarnings("rawtypes")
	private Map<Auction, Future> automatedAuctions = new HashMap<Auction, Future>();

	public AuctionService() {
		super();
		randomizer = new MathRandomizer();
	}

	public AuctionService(AuctionIdRandomizer randomizer) {
		super();
		this.randomizer = randomizer;
	}

	public void setAuctionDao(AuctionDao auctionDao) {
		this.auctionDao = auctionDao;
	}

	public void setSmokooConnector(SmokooConnector smokooConnector) {
		this.smokooConnector = smokooConnector;
	}

	public void setTimeRemainingParser(TimeRemainingParser timeRemainingParser) {
		this.timeRemainingParser = timeRemainingParser;
	}

	public void setRandomizer(AuctionIdRandomizer randomizer) {
		this.randomizer = randomizer;
	}

	@Transactional
	public void save(Auction auction) {
		try {
			Auction existingAuction = Auction.findAuctionsByAuctionId(auction.getAuctionId()).getSingleResult();
			if (existingAuction != null){
				if (existingAuction.isClosed()){
					existingAuction.setClosed(false);
				}
				auction = existingAuction;
			}
		} catch (EmptyResultDataAccessException e) {
			// DO NOTHING
		}
		entityManager.merge(auction);
	}

	public void save(List<Auction> auctions) {
		for (Auction auction : auctions) {
			save(auction);
		}
	}

	@Transactional
	public void close(String auctionTitle) {
		try {
			Auction auction = Auction.findAuctionsByAuctionTitleEquals(auctionTitle).getSingleResult();
			auction.setClosed(true);
			entityManager.merge(auction);
		} catch (EmptyResultDataAccessException e) {
			// DO NOTHING
		}
	}

	public List<Auction> listOpenAuctions() {
		try {
			return Auction.findAuctionsByClosedAndDateNotNull(false).getResultList();
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Auction>();
		}
	}

	public AuctionHistory getAuctionHistory(Auction auction, AuctionIntervalType type) {
		List<AuctionResult> results = auctionDao.getAuctionResults(new AuctionResultRequest(auction, type));
		return new AuctionHistory(auction, type, results);
	}

	public Long getTimeRemaining(Auction auction) {
		StopWatch w = new StopWatch();
		w.start();
		String url = Constants.AUCTION_TIME_REMAING_URL
				+ String.format("?auction_ids=%s&unique_string=%s", Long.toString(auction.getAuctionId()),
						randomizer.randomize());
		String content = smokooConnector.get(url);
		if (StringUtils.isEmpty(content)) {
			return 0l;
		}
		long remaining = timeRemainingParser.parse(content, auction.getAuctionId());
		w.stop();
		long result = remaining - w.getTime();
		if (result <= 0) {
			return 0l;
		}
		return result;
	}

	public void placeBid(Auction auction) {
		placeBid(auction.getAuctionId());
	}
	
	@Override
	public void onApplicationEvent(AutomateAuctionEvent event) {
			Auction auction = (Auction)event.getSource();
			entityManager.merge(auction);
			if (auction.isBiddingAutomated()){
				automatedAuctions.put(auction, Executors.newCachedThreadPool().submit(new SubSecondBiddingStrategy(auction)));
			}
			else{
				Future future = automatedAuctions.get(auction);
				future.cancel(true);
				automatedAuctions.remove(auction);
			}
		}


	private void placeAutomatedBid(Auction auction) {
		if (5/* TODO: Implement max Ammount bids placed */<= auction.getMaxNumberOfBids()) {
			placeBid(auction.getAuctionId());
		}
	}

	private void placeBid(long auctionId) {
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("auction_id", "" + auctionId) };
		smokooConnector.post(Constants.AUCTION_PLACE_A_BID_URL, parameters);
	}
	

	private class MathRandomizer implements AuctionIdRandomizer {

		@Override
		public double randomize() {
			return Math.random();
		}

	}

	private class SubSecondBiddingStrategy implements Runnable {

		private static final long BID_LIMIT = 3000l; // 600 milliseconds

		private Auction auction;

		public SubSecondBiddingStrategy(Auction auction) {
			this.auction = auction;
		}

		@Override
		public void run() {
			try {
				long timeRemaining;
				int retryAttempt = 0;
				while (retryAttempt < 3) {
					timeRemaining = getTimeRemaining(auction);
					if (timeRemaining == 0) {
						Thread.sleep(500);
						retryAttempt++;
						log.info(String.format("Retry attempt (%s) to get remaining time", retryAttempt));
						continue;
					}
					retryAttempt = 0;
					if (timeRemaining > BID_LIMIT) {
						long snooze = timeRemaining - BID_LIMIT;
						debug("Remainder: " + timeRemaining);
						Thread.sleep(snooze);
					} else {
						// if (timeRemaining > 500){
						// timeRemaining = timeRemainingCalculatorImpl.calculate(auctionId);
						// }
						if (timeRemaining <= BID_LIMIT) {
							placeAutomatedBid(auction);
							Thread.sleep(1000);
						} else {
							debug("Inside time limit, but not bidding: " + timeRemaining);
						}
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
		}

		public void debug(String message) {
			if (log.isDebugEnabled()) {
				log.debug(message);
			}
		}
	}

}
