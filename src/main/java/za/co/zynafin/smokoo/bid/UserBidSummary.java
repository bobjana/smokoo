package za.co.zynafin.smokoo.bid;

import java.util.Date;

public class UserBidSummary {

	private String user;
	private int bidCount;
	private Date lastBid;
	
	public UserBidSummary(String user, int bidCount, Date lastBid) {
		super();
		this.user = user;
		this.bidCount = bidCount;
		this.lastBid = lastBid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getBidCount() {
		return bidCount;
	}

	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}

	public Date getLastBid() {
		return lastBid;
	}

	public void setLastBid(Date lastBid) {
		this.lastBid = lastBid;
	}

}
