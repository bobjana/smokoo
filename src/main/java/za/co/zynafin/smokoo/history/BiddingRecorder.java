package za.co.zynafin.smokoo.history;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Bid;
import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.AuctionClosedException;
import za.co.zynafin.smokoo.bid.BidParser;
import za.co.zynafin.smokoo.bid.BidService;
import za.co.zynafin.smokoo.io.SmokooConnector;

@Component
public class BiddingRecorder {

	private static final Logger log = Logger.getLogger(BiddingRecorder.class);

	private BidParser bidParser;
	private BidService bidHistoryService;
	private SmokooConnector smokooConnector;

	@Autowired
	public void setBidParser(BidParser bidParser) {
		this.bidParser = bidParser;
	}

	@Autowired
	public void setBidHistoryService(BidService bidHistoryService) {
		this.bidHistoryService = bidHistoryService;
	}
	
	@Autowired
	public void setSmokooConnector(SmokooConnector smokooConnector) {
		this.smokooConnector = smokooConnector;
	}

	public void record(Auction auction) throws AuctionClosedException {
		try {
			log.debug("Start recording bids for auction - " + auction.getAuctionTitle());
			String content = requestBidHistory(auction);
			List<Bid> bids = bidParser.parse(content);
			bidHistoryService.save(auction, bids);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to record auction",e);
		}
	}

	private String requestBidHistory(Auction auction) {
		return smokooConnector.get(Constants.AUCTION_BID_HISTORY_URL + auction.getAuctionTitle());
	}

}
