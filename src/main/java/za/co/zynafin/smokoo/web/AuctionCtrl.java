package za.co.zynafin.smokoo.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Popup;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionBidCountHistory;
import za.co.zynafin.smokoo.auction.AuctionHistory;
import za.co.zynafin.smokoo.auction.AuctionIntervalType;
import za.co.zynafin.smokoo.auction.AuctionService;
import za.co.zynafin.smokoo.bid.BiddingManager;
import za.co.zynafin.smokoo.bid.BiddingManagerFactory;
import za.co.zynafin.smokoo.util.charts.XYChartRenderer;

public class AuctionCtrl extends BaseCtrl {

	private Auction auction;
	private XYChartRenderer xyChartRenderer = new XYChartRenderer();
	private AuctionService auctionService;
	private BiddingManagerFactory biddingManagerFactory;
	// UI Components
	private Caption last24HoursPriceGroup;
	private Caption last7DaysBidCountGroup;
	private Button startBiddingBtn;
//	private Groupbox gb;
	

	@Override
	public void afterCompose() {
		super.afterCompose();
		doOnCreateCommon(this);
		auction = Auction.findAuction(new Long(Executions.getCurrent().getParameter("auction")));
		binder.bindBean("auction", auction);
		AuctionHistory last24HoursHistory = auctionService.getAuctionHistory(auction, AuctionIntervalType.HOURLY);
		binder.bindBean("last24Hours", last24HoursHistory);
//		last24HoursPriceGroup.setTooltip(createChartPopup(last24HoursHistory.getData()));
		
		AuctionHistory last7DaysHistory = auctionService.getAuctionHistory(auction, AuctionIntervalType.WEEKLY);
		binder.bindBean("last7Days", last7DaysHistory);

		binder.loadAll();
		initBiddingManager();
	}

private void initBiddingManager() {
	if (biddingManagerFactory.exists(auction.getId())){
		startBiddingBtn.setVisible(false);
		onClick$startBiddingBtn(null);
	}
}
	
	public void onClick$startBiddingBtn(Event event){
		BiddingManager biddingManager = biddingManagerFactory.getInstance(auction.getId());
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("biddingManager", biddingManager);
		parameters.put("auction", auction);
		Borderlayout bl = (Borderlayout) Path.getComponent("/auctionView/mainLayout");
		bl.getCenter().getChildren().clear();
		Executions.createComponents("bidding.zul", bl.getCenter(), parameters);
	}

	private Popup createChartPopup(Number[][] data) {
		Popup popup = new Popup();
		popup.setHflex("min");
		popup.setVflex("min");
		
		byte[] chartData = xyChartRenderer.render(data);
		if (chartData!=null){
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
