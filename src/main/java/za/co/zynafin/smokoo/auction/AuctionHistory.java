package za.co.zynafin.smokoo.auction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.util.charts.ChartUtils;

public class AuctionHistory {

	private static final Logger log = Logger.getLogger(AuctionCloser.class);

	private double minPrice = Double.MAX_VALUE;
	private double maxPrice = 0;
	private double avgPrice = 0;
	private int minCount = 0;
	private int maxCount = Integer.MAX_VALUE;
	private int avgCount = 0;
	private Double[][] amountData;
	private Integer[][] countData;

	public AuctionHistory(Auction auction, AuctionIntervalType intervalType, List<AuctionResult> results) {
		super();
		try {
			summarizeData(auction, intervalType, results);
		} catch (AuctionDataNotAvaliableException e) {
			log.info(String.format("No auction data for interval '%s' and auction '%s' is available",
					intervalType.toString(), auction.getId()));
			return;
		}
		@SuppressWarnings("rawtypes")
		ChartUtils chartUtils = new ChartUtils<Integer>(Integer.class);
		this.minPrice = chartUtils.getMin(amountData).doubleValue();
		this.maxPrice = chartUtils.getMax(amountData).doubleValue();
		this.avgPrice = chartUtils.getAvg(amountData).doubleValue();
		this.minCount = chartUtils.getMin(countData).intValue();
		this.maxCount = chartUtils.getMax(countData).intValue();
		this.avgCount = chartUtils.getAvg(countData).intValue();
	}

	private void summarizeData(Auction auction, AuctionIntervalType intervalType, List<AuctionResult> results)
			throws AuctionDataNotAvaliableException {
		if (results == null || results.size() == 0) {
			throw new AuctionDataNotAvaliableException();
		}
		boolean weeklySummary = false;
		if (intervalType.equals(AuctionIntervalType.WEEKLY)) {
			results = summarizAuctionDays(auction, results);
			weeklySummary = true;
		}
		Date currentDate = DateUtils.truncate(results.get(0).getDate(), Calendar.HOUR_OF_DAY);
		Map<Date, List<Double>> priceMap = new HashMap<Date, List<Double>>();
		Map<Date, List<Integer>> countMap = new HashMap<Date, List<Integer>>();
		priceMap.put(currentDate, new ArrayList<Double>());
		countMap.put(currentDate, new ArrayList<Integer>());

		for (AuctionResult auctionResult : results) {
			if (weeklySummary && !DateUtils.isSameDay(currentDate, auctionResult.getDate())) {
				currentDate = auctionResult.getDate();
				priceMap.put(currentDate, new ArrayList<Double>());
				countMap.put(currentDate, new ArrayList<Integer>());
			} else if (new DateTime(currentDate.getTime()).getHourOfDay() != new DateTime(auctionResult.getDate())
					.getHourOfDay()) {
				currentDate = auctionResult.getDate();
				priceMap.put(currentDate, new ArrayList<Double>());
				countMap.put(currentDate, new ArrayList<Integer>());
			}
			priceMap.get(currentDate).add(auctionResult.getAmount());
			countMap.get(currentDate).add(auctionResult.getNumberOfBids());
		}
		amountData = new Double[priceMap.size()][];
		countData = new Integer[priceMap.size()][];
		int i = 0;
		for (Date date : priceMap.keySet()) {
			List<Double> prices = priceMap.get(date);
			List<Integer> counts = countMap.get(date);
			amountData[i] = prices.toArray(new Double[prices.size()]);
			countData[i] = counts.toArray(new Integer[counts.size()]);
			i++;
		}
	}

	private List<AuctionResult> summarizAuctionDays(Auction auction, List<AuctionResult> results)
			throws AuctionDataNotAvaliableException {
		int thisHour = new DateTime(DateUtils.truncate(auction.getDate(), Calendar.HOUR_OF_DAY)).get(DateTimeFieldType
				.hourOfDay());
		List<AuctionResult> auctionDaysResult = new ArrayList<AuctionResult>();
		for (AuctionResult auctionResult : results) {
			int auctionHour = new DateTime(DateUtils.truncate(auctionResult.getDate(), Calendar.HOUR_OF_DAY))
					.get(DateTimeFieldType.hourOfDay());
			if (thisHour == auctionHour) {
				auctionDaysResult.add(auctionResult);
			}
		}
		results = auctionDaysResult;
		if (results.size() == 0) {
			throw new AuctionDataNotAvaliableException();
		}
		return results;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public int getMinCount() {
		return minCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public int getAvgCount() {
		return avgCount;
	}

	public Double[][] getAmountData() {
		return amountData;
	}

	public Integer[][] getCountData() {
		return countData;
	}

}
