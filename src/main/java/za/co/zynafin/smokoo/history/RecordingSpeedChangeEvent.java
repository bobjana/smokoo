package za.co.zynafin.smokoo.history;

import org.springframework.context.ApplicationEvent;

import za.co.zynafin.smokoo.Auction;

public class RecordingSpeedChangeEvent extends ApplicationEvent{

	private long speed;
	private Auction auction;
	
	public RecordingSpeedChangeEvent(Object source, Auction auction, long speedInMillis) {
		super(source);
		this.auction = auction;
		this.speed = speedInMillis;
	}

	public long getSpeed() {
		return speed;
	}

	public Auction getAuction() {
		return auction;
	}

	
}
