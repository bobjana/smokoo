package za.co.zynafin.smokoo.util.charts;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.Duration;
import org.joda.time.Interval;

import za.co.zynafin.smokoo.auction.AuctionResult;
import za.co.zynafin.smokoo.auction.AuctionResultBidCountComparator;
import za.co.zynafin.smokoo.auction.AuctionResultPriceComparator;

public class GoogleChartUtils {

	public Double calculateMaxPrice(Set<AuctionResult> results){
		double max = -1;
		for (AuctionResult result : results){
			if (result.getAmount() > max){
				max = result.getAmount();
			}
		}
		return max;
	}
	
	public Integer calculateMaxBidCount(Set<AuctionResult> results){
		int max = -1;
		for (AuctionResult result : results){
			if (result.getNumberOfBids() > max){
				max = result.getNumberOfBids();
			}
		}
		return max;
	}
	
	public String generateXAxisLabels(Set<AuctionResult> results){
		TreeSet<AuctionResult> sortedResults = getUniqueResults(results);
		String axis = "|";
		boolean spansDays = determineIfResultsSpanDays(sortedResults);
		for (AuctionResult sortedResult : sortedResults){
			axis += extractXLabel(sortedResult,spansDays) + "|";
		}
		axis = StringUtils.replace(axis, ":", "%3A");
		return StringUtils.chop(axis);
	}
	
	public String generateYAxisLabels(Set<AuctionResult> data, boolean showPrice) {
		TreeSet<AuctionResult> uniqueResults = getUniqueResults(data);
		Comparator comp = null;
		if (showPrice){
			comp = new AuctionResultPriceComparator();
		}
		else{
			comp = new AuctionResultBidCountComparator();
		}
		TreeSet<AuctionResult> sortedResults = new TreeSet<AuctionResult>(comp);
		
//		List<AuctionResult> sortedResults = new ArrayList<AuctionResult>();
		sortedResults.addAll(uniqueResults);
//		Collections.sort(sortedResults, new AuctionResultPriceComparator());
		String axis = "|";
		for (AuctionResult sortedResult : sortedResults){
			axis += extractYLabel(sortedResult,showPrice) + "|";
		}
		return StringUtils.chop(axis);
	}
	
	public String generateYAxisLabels(Number max, boolean isPrice) {
		String axis = "|";
		if (max == null || max.doubleValue() == 0d){
			return axis;
		}
		Float interval = max.floatValue() / 10;
		Float marker = 0f;
		for (float i = 0; i <=10; i++){
			if (!isPrice){
				axis =  axis + Math.round(marker) + "|";			
			}
			else{
				axis =  axis + new Double(new DecimalFormat("#.##").format(marker)) + "|";			
			}
			marker = marker + interval;
		}
		return StringUtils.chop(axis);
	}

	public String generatePriceData(Set<AuctionResult> results) {
		TreeSet<AuctionResult> sortedResults = getUniqueResults(results);
		String result = "";
		for (AuctionResult sortedResult : sortedResults){
			result += sortedResult.getAmount() + ",";
		}
		return StringUtils.chop(result);
	}
	
	public String generateBidCountData(Set<AuctionResult> results) {
		return generateBidCountData(results, 1);
	}
	
	public String generateBidCountData(Set<AuctionResult> results, int positions) {
		String result = "";
		for (AuctionResult sortedResult : results){
			result += sortedResult.getNumberOfBids() + ",";
		}
		return StringUtils.chop(result);
	}

	private TreeSet<AuctionResult> getUniqueResults(Set<AuctionResult> results) {
		TreeSet<AuctionResult> sortedResults = new TreeSet<AuctionResult>();
		for (AuctionResult result : results){
			if (sortedResults.contains(result)){
				continue;
			}
			sortedResults.add(result);
		}
		return sortedResults;
	}
	
	private String extractXLabel(AuctionResult sortedResult, boolean spansDays) {
		Date date = DateUtils.truncate(sortedResult.getDate(), Calendar.HOUR_OF_DAY);
		if (!spansDays){
			return new SimpleDateFormat("HH").format(date);
		}
		return new SimpleDateFormat("dMMM").format(date);
	}
	
	private String extractYLabel(AuctionResult sortedResult, boolean extractPrice) {
		if (extractPrice){
			return "" + sortedResult.getAmount();
		}
		return "" + sortedResult.getNumberOfBids();
	}
	
	private boolean determineIfResultsSpanDays(TreeSet<AuctionResult> sortedResults) {
		Date first = sortedResults.first().getDate();
		Date last = sortedResults.last().getDate();
		Interval interval = new Interval(first.getTime(), last.getTime());
		return interval.toDuration().isLongerThan(Duration.standardHours(24));
	}

}
