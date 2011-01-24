package za.co.zynafin.smokoo.auction;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.Interval;

import za.co.zynafin.smokoo.Auction;

public class AuctionResultRequest {

	private Auction auction;
	private Interval interval;

	public AuctionResultRequest(Auction auction, AuctionIntervalType type) {
		super();
		this.auction = auction;
		Date date = DateUtils.setMinutes(auction.getDate(), 59);
		long startInstant;
		if (type.equals(AuctionIntervalType.HOURLY)) {
			startInstant = DateUtils.truncate(DateUtils.addDays(date, -1),Calendar.HOUR_OF_DAY).getTime();
		} else {
			startInstant = DateUtils.addDays(date, -7).getTime();
		}
		interval = new Interval(startInstant, date.getTime());
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

	public void setInterval(Interval interval) {
		this.interval = interval;
	}


}
