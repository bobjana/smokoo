package za.co.zynafin.smokoo.bid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.io.SmokooConnector;

//Prototype - Holds state & single bidding strategy that is not shared
public class BiddingManager implements BiddingListener {

	private static final Logger log = Logger.getLogger(BiddingManager.class);

	private SmokooConnector smokooConnector;
	private SubSecondBiddingStrategy subSecondBiddingStrategy;
	private BidService bidService;
	private List<BiddingStatsListener> statsListeners = new ArrayList<BiddingStatsListener>();
	private int maxNumberOfBids = 5;
	private boolean enabled;
	private boolean started;
	private Auction auction;
	
	@Autowired
	public void setSubSecondBiddingStrategy(SubSecondBiddingStrategy subSecondBiddingStrategy) {
		this.subSecondBiddingStrategy = subSecondBiddingStrategy;
	}

	@Autowired
	public void setSmokooConnector(SmokooConnector smokooConnector) {
		this.smokooConnector = smokooConnector;
	}

	@Autowired
	public void setBidService(BidService bidService) {
		this.bidService = bidService;
	}

	public void setMaxNumberOfBids(int maxNumberOfBids) {
		this.maxNumberOfBids = maxNumberOfBids;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public SubSecondBiddingStrategy getSubSecondBiddingStrategy() {
		return subSecondBiddingStrategy;
	}

	public int getMaxNumberOfBids() {
		return maxNumberOfBids;
	}

	public void addStatsListeners(BiddingStatsListener listener) {
		if (!statsListeners.contains(listener)) {
			statsListeners.add(listener);
		}
	}

	public void removeStatsListeners(BiddingStatsListener listener) {
		statsListeners.remove(listener);
	}

	public void start(final Auction auction) {
		if (started) {
			return;
		}
		log.info("Start bidding manager for auction: " + auction.getAuctionId());
		subSecondBiddingStrategy.addListener(this);
		this.enabled = true;
		this.started = true;
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				subSecondBiddingStrategy.strategize(auction.getAuctionId());
			}
		});
		
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new GrabAuctionStatsTask(), 0, 2, TimeUnit.SECONDS);
	}

	@ManagedOperation(description = "Enable Bidding Manager", currencyTimeLimit = 15)
	public void enable() {
		enabled = true;
	}

	public void disable() {
		enabled = false;
	}

	@ManagedAttribute(description = "Enabled", currencyTimeLimit = 15)
	public boolean isEnabled() {
		return enabled;
	}

	public void stop() {
		log.info("Stopping " + toString());
	}

	@Override
	public void placeBid(BiddingEvent event) {
		if (maxNumberOfBids-- <= 0) {
			log.info("Max number of bids exceeded");
			return;
		}
		if (!enabled) {
			log.info("Currently disabled bidding, auctionId= " + event.getAuctionId());
			return;
		}
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("auction_id", Long.toString(event
				.getAuctionId())) };
		smokooConnector.post(Constants.AUCTION_PLACE_A_BID_URL, parameters);
		log.info(String.format("Bidding for auction '%s', time remaining: %s", event.getAuctionId(),
				event.getTimeRemaining()));
	}

	public void placeManualBid() {
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("auction_id", Long.toString(auction
				.getAuctionId())) };
		smokooConnector.post(Constants.AUCTION_PLACE_A_BID_URL, parameters);
		log.info(String.format("Place manual bid for auction '%s'", getAuction().getAuctionId()));
	}

	@Override
	public String toString() {
		return "Bidding Manager: " + auction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auction == null) ? 0 : auction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BiddingManager other = (BiddingManager) obj;
		if (auction == null) {
			if (other.auction != null)
				return false;
		} else if (!auction.equals(other.auction))
			return false;
		return true;
	}

	//TODO: Task not running
	private class GrabAuctionStatsTask implements Runnable {

		@Override
		public void run() {
			List<UserBidSummary> stats = bidService.listTopBidders(auction);
System.out.println("Running....." + stats.size());
			if (stats == null) {
				return;
			}
			for (BiddingStatsListener listener : statsListeners) {
				listener.display(stats);
			}
		}

	}

}
