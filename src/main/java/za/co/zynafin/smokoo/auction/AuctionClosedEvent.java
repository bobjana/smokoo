package za.co.zynafin.smokoo.auction;

import org.springframework.context.ApplicationEvent;

import za.co.zynafin.smokoo.Auction;

public class AuctionClosedEvent extends ApplicationEvent{

	private Auction auction;

	public AuctionClosedEvent(Object source, Auction auction) {
		super(source);
		this.auction = auction;
	}

	public Auction getAuction() {
		return auction;
	}

}
