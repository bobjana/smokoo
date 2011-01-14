package za.co.zynafin.smokoo.auction;

import java.util.List;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.history.BidHistoryExecutor;

/**
 *	Closes auctions that might still be running on bid history executor and marks them as closed
 */
@Component
public class CloseAuctionJob extends TimerTask{

	private static final Logger log = Logger.getLogger(CloseAuctionJob.class);

	private AuctionService auctionService;
	private BidHistoryExecutor bidHistoryExecutor;
	private ClosedAuctionParser closedAuctionParser;
	private OpenAuctionParser openAuctionParser;
	private HttpClient httpClient = new HttpClient();
	private static final String DEFAULT_CLOSED_URL = "http://www.smokoo.co.za/closed_auctions.php";
	private static final String DEFAULT_ACTIVE_URL = "http://www.smokoo.co.za";
	
	@Autowired
	public void setAuctionService(AuctionService auctionService) {
		this.auctionService = auctionService;
	}
	
	@Autowired
	public void setBidHistoryExecutor(BidHistoryExecutor bidHistoryExecutor) {
		this.bidHistoryExecutor = bidHistoryExecutor;
	}
	
	@Autowired
	public void setClosedAuctionParser(ClosedAuctionParser closedAuctionParser) {
		this.closedAuctionParser = closedAuctionParser;
	}

	@Autowired
	public void setOpenAuctionParser(OpenAuctionParser openAuctionParser) {
		this.openAuctionParser = openAuctionParser;
	}

	@Override
	public void run() {
		log.info("Starting Closed auction monitor job...");
		List<Auction> openAuctions = auctionService.listOpenAuctions();
		List<Auction> closedAuctions = closedAuctionParser.parse(requestAuctions(DEFAULT_CLOSED_URL));
		List<Auction> auctions = (List<Auction>) CollectionUtils.intersection(openAuctions, closedAuctions);
		List<Auction> currentAuctions = openAuctionParser.parse(requestAuctions(DEFAULT_ACTIVE_URL));
		auctions.addAll(CollectionUtils.disjunction(openAuctions, currentAuctions));
		for (Auction auction : auctions){
			auctionService.close(auction.getAuctionTitle());
			bidHistoryExecutor.stopExecution(auction);
		}
	}
	
	private String requestAuctions(String url) {
		GetMethod getMethod = new GetMethod(url);
		int status;
		try {
			status = httpClient.executeMethod(getMethod);
			if (HttpStatus.SC_OK != status) {
				throw new RuntimeException("Unable to request auction summary - " + status);
			}
			return getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
