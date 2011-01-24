package za.co.zynafin.smokoo.bid;

import static org.junit.Assert.*;

import org.junit.Test;

import za.co.zynafin.smokoo.auction.TimeRemainingCalculator;


public class SubOneSecondBiddingStrategyTests {

	private int bidsPlacedCount = 0;
	
	@Test
	public void strategize() throws Exception {
		//GIVEN
		SubSecondBiddingStrategy biddingStrategy = new SubSecondBiddingStrategy();
		TimeRemainingCalculator timeRemainingCalculator = new TimeRemainingCalculatorStub(new Integer[]{8252,2310,950,650,8252,2310,950,650,8252,2310,950,650});
		biddingStrategy.setTimeRemainingCalculator(timeRemainingCalculator);
		
		biddingStrategy.addListener(new BiddingListener() {
			
			@Override
			public void placeBid(BiddingEvent event) {
				incrementBidPlaceCount();
				System.out.println("Place bid: " + event.getTimeRemaining());
			}
		});
		//WHEN
		biddingStrategy.strategize(1l);
		//THEN
		assertEquals(3, bidsPlacedCount);
	}
	
	public void incrementBidPlaceCount(){
		bidsPlacedCount++;
	}
	
	private class TimeRemainingCalculatorStub extends TimeRemainingCalculator{

		private Integer[] timeRemainingSet;
		private int index = -1;
		
		public TimeRemainingCalculatorStub(Integer[] timeRemainingSet) {
			super();
			this.timeRemainingSet = timeRemainingSet;
		}

		@Override
		public long calculate(long auctionId) {
			if (index++ < timeRemainingSet.length -1){
				return timeRemainingSet[index];
			}
			return 0;
		}
		
	}
	
	
}
