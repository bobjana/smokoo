package za.co.zynafin.smokoo.bid;

import java.util.Date;

public class UserBidSummary {

	private String user;
	private int bidCount;
	private Date lastBid;
	private double amount;
	
	public UserBidSummary(String user, int bidCount, Date lastBid, double amount) {
		super();
		this.user = user;
		this.bidCount = bidCount;
		this.lastBid = lastBid;
		this.amount = amount;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBidSummary other = (UserBidSummary) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	

}
