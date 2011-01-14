package za.co.zynafin.smokoo.history;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionClosedException;

@Component
public class BiddingRecorderJob {

	private static final Logger log = Logger.getLogger(BiddingRecorderJob.class);
	
	private Map<Auction,TimerTask> executions = new HashMap<Auction, TimerTask>();
	private BiddingRecorder bidHistoryRecorder;
	private Timer timer = new Timer();
	private static final long DEFAULT_DELAY = 20000; //20 seconds
	
	@Autowired
	public void setBidHistoryRecorder(BiddingRecorder bidHistoryRecorder) {
		this.bidHistoryRecorder = bidHistoryRecorder;
	}

	public void startExecution(Auction auction){
		RecordingTimerTask recordingTimerTask = new RecordingTimerTask(auction);
		executions.put(auction, recordingTimerTask);
		timer.schedule(recordingTimerTask, 0, DEFAULT_DELAY);
	}

	public void stopExecution(Auction auction){
		if (executions.containsKey(auction)){
			executions.get(auction).cancel();
			executions.remove(auction);
		}
	}
	
	public boolean isExecuting(Auction auction){
		return executions.containsKey(auction);
	}
	
	private class RecordingTimerTask extends TimerTask{

		private Auction auction;
		
		public RecordingTimerTask(Auction auction) {
			super();
			this.auction = auction;
		}

		@Override
		public void run() {
			try {
				bidHistoryRecorder.record(auction);
			} catch (AuctionClosedException e) {
				log.info(String.format("Auction '%s' has been closed, removing frp, bidding recorder",auction.getAuctionTitle()));
				stopExecution(auction);
			}
		}
		
	}
}
