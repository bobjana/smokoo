package za.co.zynafin.smokoo.bid;

public class BiddingEvent {

	private long auctionId;
	private long timeRemaining;

	public BiddingEvent(long auctionId, long timeRemaining) {
		super();
		this.auctionId = auctionId;
		this.timeRemaining = timeRemaining;
	}

	public long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(long auctionId) {
		this.auctionId = auctionId;
	}

	public long getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(long timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

}
