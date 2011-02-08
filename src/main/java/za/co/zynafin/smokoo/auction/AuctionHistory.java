package za.co.zynafin.smokoo.auction;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

import za.co.zynafin.smokoo.Auction;

public class AuctionHistory {

	private static final Logger log = Logger.getLogger(AuctionCloser.class);

	SummaryStatistics priceStatistics = new SummaryStatistics();
	SummaryStatistics countStatistics = new SummaryStatistics();

	private Set<AuctionResult> data = new TreeSet<AuctionResult>();

	public AuctionHistory(Auction auction, AuctionIntervalType intervalType, List<AuctionResult> results) {
		if (results == null || results.size() == 0) {
			log.info(String.format("No auction data for interval '%s' and auction '%s' is available",
					intervalType.toString(), auction.getId()));
			return;
		}
		Set<AuctionResult> uniqueResults = new TreeSet<AuctionResult>();
		uniqueResults.addAll(results);
		for (AuctionResult auctionResult : uniqueResults) {
			priceStatistics.addValue(auctionResult.getAmount());
			countStatistics.addValue(auctionResult.getNumberOfBids());
		}
		this.data = uniqueResults;
	}

	public Set<AuctionResult> getData() {
		return data;
	}

	public double getMaxPrice() {
		return roundMoney(priceStatistics.getMax());
	}

	public double getMinPrice() {
		return roundMoney(priceStatistics.getMin());
	}

	public double getMeanPrice() {
		return roundMoney(priceStatistics.getMean());
	}

	public int getMinCount() {
		return (int) countStatistics.getMin();
	}

	public int getMaxCount() {
		return (int) countStatistics.getMax();
	}

	public int getMeanCount() {
		return (int) countStatistics.getMean();
	}

	public double getCountVariance() {
		return countStatistics.getVariance();
	}

	public double getCountStandardDeviation() {
		return countStatistics.getStandardDeviation();
	}

	private double roundMoney(Double number) {
		return new Double(new DecimalFormat("#.##").format(number));
	}

}
