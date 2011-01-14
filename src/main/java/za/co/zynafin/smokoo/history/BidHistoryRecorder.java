package za.co.zynafin.smokoo.history;

import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Bid;

@Component
public class BidHistoryRecorder {

	private static final Logger log = Logger.getLogger(BidHistoryRecorder.class);

	private BidHistoryParser bidHistoryParser;
	private BidHistoryService bidHistoryService;
	private HttpClient httpClient;
	private static final String BASE_URL = "http://www.smokoo.co.za/ajax_get_auction_bids.php?title=";

	@Autowired
	public void setBidHistoryParser(BidHistoryParser bidHistoryParser) {
		this.bidHistoryParser = bidHistoryParser;
	}

	@Autowired
	public void setBidHistoryService(BidHistoryService bidHistoryService) {
		this.bidHistoryService = bidHistoryService;
	}
	
	//NOTE: Used for testing purposes
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public void record(Auction auction) throws AuctionClosedException {
		try {
			log.debug("Start recording bids for auction - " + auction.getAuctionTitle());
			String content = requestBidHistory(auction);
			List<Bid> bids = bidHistoryParser.parse(content);
			bidHistoryService.save(auction, bids);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to record auction",e);
		}
	}

	private String requestBidHistory(Auction auction) {
		GetMethod getMethod = new GetMethod(BASE_URL + auction.getAuctionTitle());
		int status;
		try {
			status = getHttpClient().executeMethod(getMethod);
			if (HttpStatus.SC_OK != status) {
				throw new RuntimeException("Unable to request bid history - " + status);
			}
			return getMethod.getResponseBodyAsString();
		} catch (NoRouteToHostException e){
			return null;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private HttpClient getHttpClient() {
		if (this.httpClient == null){
			this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		}
		return httpClient;
	}
}
