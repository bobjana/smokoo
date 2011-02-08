package za.co.zynafin.smokoo.auction;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import za.co.zynafin.smokoo.Auction;

public class AuctionResultRequest {

	private Auction auction;
	private Interval interval;

	public AuctionResultRequest(Auction auction, AuctionIntervalType type) {
		super();
		this.auction = auction;
		if (type.equals(AuctionIntervalType.HOURLY)) {
			interval = Duration.standardHours(24).toIntervalTo(new DateTime());
		} else {
			interval = Duration.standardDays(7).toIntervalTo(new DateTime());
		}
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public Interval getInterval() {
		return interval;
	}

}
