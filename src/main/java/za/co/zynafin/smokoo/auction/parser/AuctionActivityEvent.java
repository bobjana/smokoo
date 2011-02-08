package za.co.zynafin.smokoo.auction.parser;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Bid;

public class AuctionActivityEvent extends ApplicationEvent{

	private Auction auction;
	private String rawContent;
	private List<Bid> bids;

	public AuctionActivityEvent(Object source, Auction auction, String rawContent, List<Bid> bids) {
		super(source);
		this.rawContent = rawContent;
		this.bids = bids;
		this.auction = auction;
	}

	public String getRawContent() {
		return rawContent;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public Auction getAuction() {
		return auction;
	}
	
}
