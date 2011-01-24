package za.co.zynafin.smokoo.util.charts;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class XYChartRenderer {
	
	private static final Logger log = Logger.getLogger(XYChartRenderer.class);
	
	private static final ChartUtils<Double> dChartUtils = new ChartUtils<Double>(Double.class);
	private static final ChartUtils<Integer> iChartUtils = new ChartUtils<Integer>(Integer.class);
	private HttpClient httpClient = new HttpClient();
	
	public byte[] render(Number[][] data) {
		if (data == null || data[0][0] == null) {
			return null;
		}
		String chartURL = buildGoogleChartURL(data);
		GetMethod getMethod = new GetMethod(chartURL);
		try {
			httpClient.executeMethod(getMethod);
			return getMethod.getResponseBody();
		} catch (Exception e) {
			log.error("Unable to render xy chart",e);
			return null;
		} 
	}

	private String buildGoogleChartURL(Number[][] data) {
		String minSet = "";
		String maxSet = "";
		String avgSet = "";
		Number max = 0;
		for (int i = 0; i < data.length; i++) {
			Number[] values = getValues(data[i]);
			if (values == null){
				continue;
			}
			if (values[1].doubleValue() > max.doubleValue()) {
				max = values[1].doubleValue();
			}
			minSet += values[0] + ",";
			maxSet += values[1] + ",";
			avgSet += values[2] + ",";
		}
		String dataString = StringUtils.chop(minSet) + "|" + StringUtils.chop(maxSet) + "|" + StringUtils.chop(avgSet);
		String result = StringUtils.replace(getTemplateChartURL(),"$data",dataString);
		result = StringUtils.replace(result, "$max", max.toString());
		result = StringUtils.replace(result, "|", "%7C");
		return result;
	}

	private Number[] getValues(Number[] data) {
		Number[] result = new Number[3];
		if (data.length == 0){
			return null;
		}
		if (data[0] instanceof Double){
			Double[][] input = new Double[][]{(Double[])data};
			result[0] = dChartUtils.getMin(input);
			result[1] = dChartUtils.getMax(input);
			result[2] = dChartUtils.getAvg(input);
		}
		else{
			Integer[][] input = new Integer[][]{(Integer[])data};
			result[0] = iChartUtils.getMin(input);
			result[1] = iChartUtils.getMax(input);
			result[2] = iChartUtils.getAvg(input);
		}
		return result;
	}
	
	private String getTemplateChartURL(){
	 return "http://chart.apis.google.com/chart" + 
	 		"?chxr=0,0,$max" + 
	 		"&chxt=y" + 
	 		"&chs=440x220" + 
	 		"&cht=lc" + 
	 		"&chco=3072F3,FF0000,FF9900" + 
	 		"&chds=0,$max,0,$max,0,$max" + 
	 		"&chd=t:$data" + 
	 		"&chdl=Min|Max|Avg" + 
	 		"&chdlp=b" + 
	 		"&chls=2,4,1|1|1" + 
	 		"&chma=5,5,5,25";
	}
	
}
