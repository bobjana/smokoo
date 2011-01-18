package za.co.zynafin.smokoo.bid;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.AuctionMonitorJob;
import za.co.zynafin.smokoo.io.SmokooConnector;

@Component
public class BiddingManager implements BiddingListener{

	private static final Logger log = Logger.getLogger(BiddingManager.class);
	
	private SubOneSecondBiddingStrategy subOneSecondBiddingStrategy;
	private SmokooConnector smokooConnector;
	
	@Autowired
	public void setSubZeroBiddingStrategy(SubOneSecondBiddingStrategy subOneSecondBiddingStrategy) {
		this.subOneSecondBiddingStrategy = subOneSecondBiddingStrategy;
	}
	
	@Autowired
	public void setSmokooConnector(SmokooConnector smokooConnector) {
		this.smokooConnector = smokooConnector;
	}
	
	public void bid(long auctionId, int numberOfBids){
		subOneSecondBiddingStrategy.addListener(this);
		subOneSecondBiddingStrategy.strategize(auctionId);
//		this.numberOfBids = numberOfBids;
	}


	@Override
	public void placeBid(BiddingEvent event) {
//System.out.println("Number of bids remaining: " + numberOfBids);
//		if (numberOfBids-- <= 0){
//			return;
//		}
		NameValuePair[] parameters = new NameValuePair[]{new NameValuePair("auction_id",Long.toString(event.getAuctionId()))};
		smokooConnector.post(Constants.AUCTION_PLACE_A_BID_URL, parameters);
		log.info(String.format("Bidding for auction '%s', time remaining: %s",event.getAuctionId(),event.getTimeRemaining()));
	}
	
}
