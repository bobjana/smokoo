package za.co.zynafin.smokoo.auction;

import java.util.List;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;

/**
 * A scheduled job running periodically to refresh information about auctions
 *
 */
@Component
public class AuctionRefreshJob extends TimerTask{

	private static final Logger log = Logger.getLogger(AuctionRefreshJob.class);

	private AuctionService auctionService;
	private OpenAuctionParser auctionParser;
	private static final String DEFAULT_URL = "http://www.smokoo.co.za/";
	private HttpClient httpClient = new HttpClient();
	
	@Autowired
	public void setAuctionService(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	@Autowired
	public void setAuctionParser(OpenAuctionParser auctionParser) {
		this.auctionParser = auctionParser;
	}

	@Override
	public void run() {
		log.info("Starting current active auction retrieval job...");
		String content = requestAuctionSummary();
		List<Auction> auctions = auctionParser.parse(content);
		auctionService.save(auctions);
	}
	
	private String requestAuctionSummary() {
		GetMethod getMethod = new GetMethod(DEFAULT_URL);
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
