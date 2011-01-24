package za.co.zynafin.smokoo.bid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import za.co.zynafin.smokoo.auction.TimeRemainingCalculator;

public class SubSecondBiddingStrategy {

	private static final Logger log = Logger.getLogger(SubSecondBiddingStrategy.class);
	private static final long BID_LIMIT =  300l; //600 milliseconds

	private List<BiddingListener> listeners = new ArrayList<BiddingListener>();
	private TimeRemainingCalculator timeRemainingCalculator;
	
	@Autowired
	public void setTimeRemainingCalculator(TimeRemainingCalculator timeRemainingCalculator) {
		this.timeRemainingCalculator = timeRemainingCalculator;
	}

	public void addListener(BiddingListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(BiddingListener listener){
		listeners.remove(listener);
	}
	
	public void strategize(long auctionId){
		try {
			long timeRemaining;
			int retryAttempt = 0;
			while (retryAttempt < 3){
				timeRemaining = timeRemainingCalculator.calculate(auctionId);
				if (timeRemaining == 0){
					Thread.sleep(500);
					retryAttempt++;
					log.info(String.format("Retry attempt (%s) to get remaining time", retryAttempt));
					continue;
				}
				retryAttempt = 0;
				if (timeRemaining > BID_LIMIT){
					long snooze = timeRemaining - BID_LIMIT;
					debug("Remainder: " + timeRemaining);
					Thread.sleep(snooze);
				}
				else {
//					if (timeRemaining > 500){
//						timeRemaining = timeRemainingCalculator.calculate(auctionId);
//					}
					if (timeRemaining <= BID_LIMIT){
						notifyListeners(new BiddingEvent(auctionId, timeRemaining));
						Thread.sleep(1000);
					}
					else{
						debug("Inside time limit, but not bidding: " + timeRemaining);
					}
				}
			}
			System.out.println("Stop");
		} catch (Exception e) {
			log.error(e);
		}
	}

	private void notifyListeners(BiddingEvent biddingEvent) {
		for (BiddingListener listener : listeners){
			listener.placeBid(biddingEvent);
		}
		
	}
	
	public void debug(String message){
		if (log.isDebugEnabled()){
			log.debug(message);
		}
	}
}
