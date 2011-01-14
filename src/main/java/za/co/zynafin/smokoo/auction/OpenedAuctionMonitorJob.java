package za.co.zynafin.smokoo.auction;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.history.BidHistoryExecutor;

/**
 *	Starts the bids history recording for auctions that are about to become active. 
 */
@Component
public class OpenedAuctionMonitorJob extends TimerTask{

	private static final Logger log = Logger.getLogger(OpenedAuctionMonitorJob.class);

	private AuctionService auctionService;
	private BidHistoryExecutor bidHistoryExecutor;
	private int timeBuffer = 10*1000*60; //10 minutes default
	
	public void setTimeBuffer(int timeBuffer) {
		this.timeBuffer = timeBuffer;
	}

	@Autowired
	public void setAuctionService(AuctionService auctionService) {
		this.auctionService = auctionService;
	}
	
	@Autowired
	public void setBidHistoryExecutor(BidHistoryExecutor bidHistoryExecutor) {
		this.bidHistoryExecutor = bidHistoryExecutor;
	}

	@Override
	public void run() {
		log.info("Starting open auction monitor job...");
		List<Auction> openAuctions = auctionService.listOpenAuctions();
		Date nextStartTime = determineNextStartTime(); 
		for (Auction auction : openAuctions){
			if (auction.getDate() != null && auction.getDate().before(nextStartTime) && !bidHistoryExecutor.isExecuting(auction)){
					log.info(String.format("Auction '%s' ready for bid history gathering",auction.getAuctionTitle()));
					bidHistoryExecutor.startExecution(auction);
			}
		}
	}

	private Date determineNextStartTime() {
		return DateUtils.addMilliseconds(new Date(), timeBuffer);
	}
	

}
