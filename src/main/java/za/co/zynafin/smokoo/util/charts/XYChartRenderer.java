package za.co.zynafin.smokoo.util.charts;

import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import za.co.zynafin.smokoo.auction.AuctionResult;

public class XYChartRenderer {
	
	private static final Logger log = Logger.getLogger(XYChartRenderer.class);
	
	private HttpClient httpClient;
	
	public byte[] render(Set<AuctionResult> data) {
		if (data == null || data.size() == 0){
			return null;
		}
		GoogleChartUtils chartUtils = new GoogleChartUtils();
		String priceDataString = chartUtils.generatePriceData(data);
		Double maxPrice = chartUtils.calculateMaxPrice(data);
		String countDataString = chartUtils.generateBidCountData(data);
		int maxCount = chartUtils.calculateMaxBidCount(data);
		String result = StringUtils.replace(getTemplateChartURL(),"$data",priceDataString + "|" + countDataString);
		result = StringUtils.replace(result, "$max", (maxPrice > maxCount?""+maxPrice:""+maxCount));
		result = StringUtils.replace(result, "$legends", generateLegendsString(new String[]{"price","count"}));
		result = StringUtils.replace(result, "$min-max", generateMinMaxString(maxPrice, 1) + "," + generateMinMaxString(maxCount, 1));
		result = StringUtils.replace(result, "$xaxis", chartUtils.generateXAxisLabels(data));
		result = StringUtils.replace(result, "$yaxis", chartUtils.generateYAxisLabels(maxPrice,true));
		result = StringUtils.replace(result, "$raxis", chartUtils.generateYAxisLabels(maxCount,false));
		result = StringUtils.replace(result, "|", "%7C");
		System.err.println(result);
		GetMethod getMethod = new GetMethod(result);
		try {
			getHttpClient().executeMethod(getMethod);
			return getMethod.getResponseBody();
		} catch (Exception e) {
			log.error("Unable to render xy chart",e);
			return null;
		} 
	}

	private String generateMinMaxString(Number max, int length) {
		String result = "";
		for (int i = 0; i < length; i++){
			result += "0," + max + ",";
		}
		return StringUtils.chop(result);
	}

	private String generateLegendsString(String[] legends) {
		String result = "";
		for (String legend : legends){
			result += legend + "|";
		}
		return StringUtils.chop(result);
	}
	
	private String getTemplateChartURL(){
	 return "http://chart.apis.google.com/chart" + 
	 		"?chxr=0,0,$max" +
	 		"&chxl=0:$xaxis|1:$yaxis|2:$raxis" + 
	 		"&chxt=x,y,r" + 
	 		"&chs=600x300" + 
	 		"&cht=lc" + 
	 		"&chco=3072F3,FF0000,FF9900" + 
	 		"&chds=$min-max" + 
	 		"&chd=t:$data" + 
	 		"&chdl=$legends" + 
	 		"&chdlp=b" + 
	 		"&chls=2,4,1|1|1" + 
	 		"&chma=5,5,5,25";
	}
	
	
	private HttpClient getHttpClient(){
		if (httpClient == null){
			SimpleHttpConnectionManager httpConnectionManager = new SimpleHttpConnectionManager();
			httpConnectionManager.getParams().setConnectionTimeout(2000);
			httpClient = new HttpClient(httpConnectionManager);
		}
		return httpClient;
	}
}
