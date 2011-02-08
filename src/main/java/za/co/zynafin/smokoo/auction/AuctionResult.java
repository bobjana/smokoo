package za.co.zynafin.smokoo.auction;

import java.util.Date;

public class AuctionResult implements Comparable<AuctionResult>{

	private Date date;
	private double amount;
	private int numberOfBids;
	private String winner;
	private Long id;
	
	public AuctionResult(Long id, Date date, double amount, int numberOfBids, String winner) {
		super();
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.numberOfBids = numberOfBids;
		this.winner = winner;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getNumberOfBids() {
		return numberOfBids;
	}

	public void setNumberOfBids(int numberOfBids) {
		this.numberOfBids = numberOfBids;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AuctionResult other = (AuctionResult) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(AuctionResult o) {
		return date.compareTo(o.getDate());
	}
	
	
	

}
