package za.co.zynafin.smokoo.auction;

import java.util.Date;

public class AuctionResult {

	private Date date;
	private double amount;
	private int numberOfBids;
	private String winner;

	
	public AuctionResult(Date date, double amount, int numberOfBids, String winner) {
		super();
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

}
