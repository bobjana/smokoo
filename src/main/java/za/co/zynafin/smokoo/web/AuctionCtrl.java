package za.co.zynafin.smokoo.web;

import java.io.IOException;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Popup;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionHistory;
import za.co.zynafin.smokoo.auction.AuctionIntervalType;
import za.co.zynafin.smokoo.auction.AuctionService;
import za.co.zynafin.smokoo.util.charts.XYChartRenderer;

public class AuctionCtrl extends BaseCtrl {

	private Auction auction;
	private XYChartRenderer xyChartRenderer = new XYChartRenderer();
	private AuctionService auctionService;
	// UI Components
	private Caption last24HoursPrices;
	private Caption last7DaysPrices;
	private Caption last24HoursBidCount;
	private Caption last7DaysBidCount;
	private Iframe auctionIFrame;

	@Override
	public void afterCompose() {
		super.afterCompose();
		doOnCreateCommon(this);
		auction = Auction.findAuction(new Long(Executions.getCurrent().getParameter("auction")));
		binder.bindBean("auction", auction);

		AuctionHistory last24HoursHistory = auctionService.getAuctionHistory(auction, AuctionIntervalType.HOURLY);
		binder.bindBean("last24Hours", last24HoursHistory);
		last24HoursPrices.setTooltip(createChartPopup(last24HoursHistory.getAmountData()));
		last24HoursBidCount.setTooltip(createChartPopup(last24HoursHistory.getCountData()));

		AuctionHistory last7DaysHistory = auctionService.getAuctionHistory(auction, AuctionIntervalType.WEEKLY);
		binder.bindBean("last7Days", last7DaysHistory);
		last7DaysPrices.setTooltip(createChartPopup(last7DaysHistory.getAmountData()));
		last7DaysBidCount.setTooltip(createChartPopup(last7DaysHistory.getCountData()));
		
		binder.loadAll();
		
		auctionIFrame.setSrc("http://www.smokoo.co.za/auctions/" + auction.getAuctionTitle());
	}

	private Popup createChartPopup(Number[][] data) {
		Popup popup = new Popup();
		popup.setHflex("min");
		popup.setVflex("min");

		byte[] chartData = xyChartRenderer.render(data);
		if (chartData != null) {
			Image image = new Image();
			try {
				image.setContent(new AImage("chart", chartData));
			} catch (IOException e) {
			}
			image.setParent(popup);
		}
		popup.setParent(this);
		return popup;
	}

}
