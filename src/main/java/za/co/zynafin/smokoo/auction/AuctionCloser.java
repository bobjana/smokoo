package za.co.zynafin.smokoo.auction;

import java.util.List;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.parser.ClosedAuctionParser;
import za.co.zynafin.smokoo.auction.parser.OpenAuctionParser;
import za.co.zynafin.smokoo.io.SmokooConnector;

/**
 *	Closes auctions that might still be running on bid history executor and marks them closed for future runs
 */
@Component
public class AuctionCloser extends TimerTask implements ApplicationContextAware{

	private static final Logger log = Logger.getLogger(AuctionCloser.class);

	private AuctionService auctionService;
	private ClosedAuctionParser closedAuctionParser;
	private OpenAuctionParser openAuctionParser;
	private SmokooConnector smokooConnector;

	private ApplicationContext applicationContext;
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Autowired
	public void setAuctionService(AuctionService auctionService) {
		this.auctionService = auctionService;
	}
	
	@Autowired
	public void setClosedAuctionParser(ClosedAuctionParser closedAuctionParser) {
		this.closedAuctionParser = closedAuctionParser;
	}

	@Autowired
	public void setOpenAuctionParser(OpenAuctionParser openAuctionParser) {
		this.openAuctionParser = openAuctionParser;
	}

	@Autowired
	public void setSmokooConnector(SmokooConnector smokooConnector) {
		this.smokooConnector = smokooConnector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		log.info("Closing auctions...");
		List<Auction> openAuctions = auctionService.listOpenAuctions();
		List<Auction> closedAuctions = closedAuctionParser.parse(smokooConnector.get(Constants.CLOSED_AUCTIONS_URL));
		List<Auction> auctions = (List<Auction>) CollectionUtils.intersection(openAuctions, closedAuctions);
		List<Auction> currentAuctions = openAuctionParser.parse(smokooConnector.get(Constants.ACTIVE_AUCTIONS_URL));
		auctions.addAll(CollectionUtils.disjunction(openAuctions, currentAuctions));
		for (Auction auction : auctions){
			auctionService.close(auction.getAuctionTitle());
			applicationContext.publishEvent(new AuctionClosedEvent(this, auction));
		}
	}

}
