package za.co.zynafin.smokoo.auction.parser;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import za.co.zynafin.smokoo.Bid;

public class AuctionActivityEvent extends ApplicationEvent{

	private String rawContent;
	private List<Bid> bids;

	public AuctionActivityEvent(Object source, String rawContent, List<Bid> bids) {
		super(source);
		this.rawContent = rawContent;
		this.bids = bids;
	}

	public String getRawContent() {
		return rawContent;
	}

	public List<Bid> getBids() {
		return bids;
	}


}
