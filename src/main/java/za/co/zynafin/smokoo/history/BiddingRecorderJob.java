package za.co.zynafin.smokoo.history;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionClosedEvent;
import za.co.zynafin.smokoo.auction.AuctionClosedException;

@Component
public class BiddingRecorderJob implements ApplicationListener<ApplicationEvent> {

	private static final Logger log = Logger.getLogger(BiddingRecorderJob.class);

	private Map<Auction, RecordingTimerTask> executions = new HashMap<Auction, RecordingTimerTask>();
	private BiddingRecorder bidHistoryRecorder;
	private Timer timer = new Timer();
	private static final long DEFAULT_DELAY = 20000; // 20 seconds

	@Autowired
	public void setBidHistoryRecorder(BiddingRecorder bidHistoryRecorder) {
		this.bidHistoryRecorder = bidHistoryRecorder;
	}

	public void startRecording(Auction auction) {
		if (!executions.containsKey(auction)){
if (executions.size() > 0){
System.out.println("--start record " + auction.getAuctionTitle());
for (Auction execution : executions.keySet()){
	System.out.println("-" + execution.getAuctionTitle());
}
}
			startRecording(auction, DEFAULT_DELAY);
		}
	}

	public void startRecording(Auction auction, long period) {
		if (executions.containsKey(auction)){
			executions.get(auction).cancel();
			executions.remove(auction);
		}
		RecordingTimerTask recordingTimerTask = new RecordingTimerTask(auction);
		executions.put(auction, recordingTimerTask);
		timer.schedule(recordingTimerTask, 0, period);
System.out.println("start record " + period + "   " + executions.size());
	}

	public void stopRecording(Auction auction) {
		if (executions.containsKey(auction)) {
			executions.get(auction).cancel();
			executions.remove(auction);
		}
	}

	public boolean isExecuting(Auction auction) {
		return executions.containsKey(auction);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof AuctionClosedEvent) {
			stopRecording(((AuctionClosedEvent) event).getAuction());
		} else if (event instanceof RecordingSpeedChangeEvent) {
			RecordingSpeedChangeEvent recordingSpeedChangeEvent = (RecordingSpeedChangeEvent) event;
			log.info(String.format("Change recording speed for auction (%s) to %s ms", recordingSpeedChangeEvent.getAuction()
					.getAuctionId(), recordingSpeedChangeEvent.getSpeed()));
			Auction auction = recordingSpeedChangeEvent.getAuction();
			stopRecording(auction);
			startRecording(auction, recordingSpeedChangeEvent.getSpeed());
		}
	}

	private class RecordingTimerTask extends TimerTask {

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
				log.info(String.format("Auction '%s' has been closed, removing bidding recorder", auction.getAuctionTitle()));
				stopRecording(auction);
			}
		}

	}
}
