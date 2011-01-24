package za.co.zynafin.smokoo.auction;

import za.co.zynafin.smokoo.util.charts.ChartUtils;


public class AuctionBidCountHistory {

	private int minCount = 10000000;
	private int maxCount = 0;
	private int avgCount = 0;
	private Integer[][] data;

	public AuctionBidCountHistory(Integer[][] data) {
		super();
		this.data = data;
		ChartUtils chartUtils = new ChartUtils<Integer>(Integer.class);
		this.minCount = chartUtils.getMin(data).intValue();
		this.maxCount = chartUtils.getMax(data).intValue();
		this.avgCount = chartUtils.getAvg(data).intValue();
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getAvgCount() {
		return avgCount;
	}

	public void setAvgCount(int avgCount) {
		this.avgCount = avgCount;
	}

	public Integer[][] getData() {
		return data;
	}

	public void setData(Integer[][] data) {
		this.data = data;
	}
	
	

}
