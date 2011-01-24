package za.co.zynafin.smokoo.auction;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.history.BiddingRecorderJob;

/**
 * Starts the bids history recording for auctions that are about to become
 * active.
 */
@Component
@ManagedResource(objectName = "bean:name=auctionMonitorJob", description = "My Managed Bean", log = true, logFile = "jmx.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "foo", persistName = "bar")
public class AuctionMonitorJob extends TimerTask {

	private static final Logger log = Logger.getLogger(AuctionMonitorJob.class);

	private AuctionService auctionService;
	private BiddingRecorderJob biddingRecorderJob;
	private int timeBuffer = 5 * 1000 * 60; // 5 minutes default

	@ManagedAttribute(description = "The Time Buffer", currencyTimeLimit = 20, persistPolicy = "OnUpdate")
	public void setTimeBuffer(int timeBuffer) {
		this.timeBuffer = timeBuffer;
	}

	@ManagedAttribute(description = "The Time Buffer", currencyTimeLimit = 15)
	public int getTimeBuffer() {
		return timeBuffer;
	}

	@Autowired
	public void setAuctionService(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	@Autowired
	public void setBiddingRecorderJob(BiddingRecorderJob biddingRecorderJob) {
		this.biddingRecorderJob = biddingRecorderJob;
	}

	@Override
	public void run() {
		log.info("Starting auction monitor job...");
		List<Auction> openAuctions = auctionService.listOpenAuctions();
		Date nextStartTime = determineNextStartTime();
		for (Auction auction : openAuctions) {
			if (auction.getDate() != null && auction.getDate().before(nextStartTime)
					&& !biddingRecorderJob.isExecuting(auction)) {
				log.info(String.format("Getting ready to record bids for auction '%s'...", auction.getAuctionTitle()));
				biddingRecorderJob.startRecording(auction);
			}
		}
	}

	private Date determineNextStartTime() {
		return DateUtils.addMilliseconds(new Date(), timeBuffer);
	}

}
