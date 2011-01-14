package za.co.zynafin.smokoo.auction;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.parser.OpenAuctionParser;
import za.co.zynafin.smokoo.io.SmokooConnector;

/**
 * A scheduled job running periodically to refresh information about auctions
 *
 */
@Component
public class AuctionRetrievalJob extends TimerTask{

	private static final Logger log = Logger.getLogger(AuctionRetrievalJob.class);

	private AuctionService auctionService;
	private OpenAuctionParser auctionParser;
	private SmokooConnector smokooConnector;
	
	@Autowired
	public void setAuctionService(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	@Autowired
	public void setAuctionParser(OpenAuctionParser auctionParser) {
		this.auctionParser = auctionParser;
	}

	@Autowired
	public void setSmokooConnector(SmokooConnector smokooConnector) {
		this.smokooConnector = smokooConnector;
	}

	@Override
	public void run() {
		log.info("Starting auction retrieval job...");
		List<Auction> auctions = auctionParser.parse(smokooConnector.get(Constants.ACTIVE_AUCTIONS_URL));
		auctionService.save(auctions);
	}
	
	
}
