package za.co.zynafin.smokoo.util.charts;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import za.co.zynafin.smokoo.auction.AuctionResult;

public class GoogleChartUtilsTests {

	@Test
	public void calculateMaxPrice() throws Exception {
		//GIVEN
		Set<AuctionResult> results = getTestResults();
		GoogleChartUtils chartUtils = new GoogleChartUtils();
		//WHEN
		Double result = chartUtils.calculateMaxPrice(results);
		//THEN
		assertEquals(new Double("238.0"),result);
	}
	
	@Test
	public void getXAxisLabels() throws Exception {
		//GIVEN
		Set<AuctionResult> results = getTestResults();
		GoogleChartUtils chartUtils = new GoogleChartUtils();
		//WHEN
		String result = chartUtils.generateXAxisLabels(results);
		//THEN
		assertEquals("|05|20|23|",result);
	}
	
	@Test
	public void getXAxisLabels_SpanDays() throws Exception {
		//GIVEN
		Set<AuctionResult> results = getMultipleDaysTestResults();
		GoogleChartUtils chartUtils = new GoogleChartUtils();
		//WHEN
		String result = chartUtils.generateXAxisLabels(results);
		//THEN
		assertEquals("|30Jan 20%3A00|31Jan 20%3A00|2Feb 20%3A00|",result);
	}

	@Test
	public void generateYAxisLabels_price() throws Exception {
		//GIVEN
		Set<AuctionResult> results = getTestResults();
		GoogleChartUtils chartUtils = new GoogleChartUtils();
		//WHEN
		String result = chartUtils.generateYAxisLabels(results,true);
		//THEN
		assertEquals("%3A3.15%3A23.0%3A238.0%3A",result);
	}
	
	@Test
	public void generatePriceData() throws Exception {
		//GIVEN
		Set<AuctionResult> results = getTestResults();
		GoogleChartUtils chartUtils = new GoogleChartUtils();
		//WHEN
		String result = chartUtils.generatePriceData(results);
		//THEN
		assertEquals("23.0,3.15,238.0",result);
	}
	
	@Test
	public void generateBidCountData() throws Exception {
		//GIVEN
		Set<AuctionResult> results = getTestResults();
		GoogleChartUtils chartUtils = new GoogleChartUtils();
		//WHEN
		String result = chartUtils.generateBidCountData(results);
		//THEN
		assertEquals("10,24,45",result);
	}
	
	private Set<AuctionResult> getTestResults() throws Exception{
		Set<AuctionResult> results = new TreeSet<AuctionResult>();
		Date date = new SimpleDateFormat("yyyyMMdd HH:mm").parse("20110202 05:23");
		results.add(new AuctionResult(1l, date, 23.00, 10,null,0.05));
		results.add(new AuctionResult(1l, date, 15.00, 7, null,0.05));
		results.add(new AuctionResult(1l, date,12.00, 3, null,0.05));
		date = new SimpleDateFormat("yyyyMMdd HH:mm").parse("20110202 20:23");
		results.add(new AuctionResult(2l, date,3.15, 24, null,0.05));
		results.add(new AuctionResult(2l, date, 2.75, 3, null,0.05));
		date = new SimpleDateFormat("yyyyMMdd HH:mm").parse("20110202 23:16");
		results.add(new AuctionResult(3l, date, 238.00, 45, null,0.05));
		return results;
	}
	
	private Set<AuctionResult> getMultipleDaysTestResults() throws Exception{
		Set<AuctionResult> results = new TreeSet<AuctionResult>();
		Date date = new SimpleDateFormat("yyyyMMdd HH:mm").parse("20110130 20:23");
		results.add(new AuctionResult(1l, date, 08.00, 10, null,0.05));
		date = new SimpleDateFormat("yyyyMMdd HH:mm").parse("20110131 20:00");
		results.add(new AuctionResult(2l, date, 3.00, 5, null,0.05));
		date = new SimpleDateFormat("yyyyMMdd HH:mm").parse("20110202 20:00");
		results.add(new AuctionResult(3l, date, 238.00, 6,null,0.05));
		return results;
	}
}
