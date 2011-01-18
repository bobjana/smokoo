package za.co.zynafin.smokoo.bid;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.auction.TimeRemainingCalculator;

@Component
public class SubOneSecondBiddingStrategy {

	private List<BiddingListener> listeners = new ArrayList<BiddingListener>();
	private static final long BID_LIMIT =  800l; //600 milliseconds
	private TimeRemainingCalculator timeRemainingCalculator;
	
	private static final Logger log = Logger.getLogger(SubOneSecondBiddingStrategy.class);
	
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
					log.debug("Remainder: " + timeRemaining);
					Thread.sleep(snooze);
				}
				else {
					timeRemaining = timeRemainingCalculator.calculate(auctionId);
					if (timeRemaining <= BID_LIMIT){
						notifyListeners(new BiddingEvent(auctionId, timeRemaining));
						Thread.sleep(1000);
					}
					else{
						log.debug("Inside time limmit, but not bidding: " + timeRemaining);
					}
				}
			}
			System.out.println("Stop");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void notifyListeners(BiddingEvent biddingEvent) {
		for (BiddingListener listener : listeners){
			listener.placeBid(biddingEvent);
		}
		
	}
}
