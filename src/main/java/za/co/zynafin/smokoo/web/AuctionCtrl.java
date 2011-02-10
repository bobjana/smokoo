package za.co.zynafin.smokoo.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Timer;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionHistory;
import za.co.zynafin.smokoo.auction.AuctionIntervalType;
import za.co.zynafin.smokoo.auction.AuctionResult;
import za.co.zynafin.smokoo.auction.AuctionService;
import za.co.zynafin.smokoo.history.RecordingSpeedChangeEvent;
import za.co.zynafin.smokoo.util.ApplicationEventPublisher;
import za.co.zynafin.smokoo.util.charts.XYChartRenderer;

public class AuctionCtrl extends BaseCtrl {

	private Auction auction;
	private XYChartRenderer xyChartRenderer = new XYChartRenderer();
	private AuctionService auctionService;
	// UI Components
	private Grid last24HoursGrid;
	private Grid last7DaysGrid;
	private Iframe auctionIFrame;
	private Label timeRemainingLabel;
	private long timeRemaining;
	private int retryTimeRemainingCount = 0;
	private Timer timeRemainingTimer;
	private Semaphore semaphore = new Semaphore(1);
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"MM/dd HH:mm");

	@Override
	public void afterCompose() {
		super.afterCompose();
		doOnCreateCommon(this);
		auction = Auction.findAuction(new Long(Executions.getCurrent()
				.getParameter("auction")));
		binder.bindBean("auction", auction);

		AuctionHistory last24HoursHistory = auctionService.getAuctionHistory(
				auction, AuctionIntervalType.HOURLY);
		last24HoursGrid.setModel(new SimpleListModel(last24HoursHistory
				.getData().toArray()));
		last24HoursGrid.setRowRenderer(new AuctionHistoryRowRenderer(
				last24HoursHistory));
		last24HoursGrid.setHeight("250px");
		last24HoursGrid
				.setPopup(createChartPopup(last24HoursHistory.getData()));
		AuctionHistory last7DaysHistory = auctionService.getAuctionHistory(
				auction, AuctionIntervalType.WEEKLY);
		last7DaysGrid.setModel(new SimpleListModel(last7DaysHistory.getData()
				.toArray()));
		last7DaysGrid.setRowRenderer(new AuctionHistoryRowRenderer(
				last7DaysHistory));
		last7DaysGrid.setHeight("250px");
		last7DaysGrid.setPopup(createChartPopup(last7DaysHistory.getData()));

		binder.loadAll();

		auctionIFrame.setSrc("http://www.smokoo.co.za/auctions/"
				+ auction.getAuctionTitle());
		timeRemaining = auctionService.getTimeRemaining(auction);
System.err.println(auction.getAuctionTitle());
		 ApplicationEventPublisher.publishEvent(new RecordingSpeedChangeEvent(this, auction, 4000));
	}

	public void onTimer$timeRemainingTimer(Event event) {
		try {
			semaphore.acquire();
			timeRemaining -= 100;
			if (timeRemaining <= 0) {
				timeRemaining = auctionService.getTimeRemaining(auction);
			}
			semaphore.release();
			if (timeRemaining <= 0) {
				if (retryTimeRemainingCount++ < 20) {
					timeRemainingLabel.setValue("");
					return;
				}
				timeRemainingLabel.setValue("Finished");
				timeRemainingLabel
						.setStyle("font-size:50px;text-align:center; color:red");
				timeRemainingTimer.setRepeats(false);
			} else {
				timeRemainingLabel.setValue(toHumanReadableTime(timeRemaining));
				retryTimeRemainingCount = 0;
			}
		} catch (InterruptedException e) {
		}
	}

	public void onTimer$statsRetrievalTimer(Event event) {
		try {
			semaphore.acquire();
			if (timeRemaining < 60*1000){
				timeRemaining = auctionService.getTimeRemaining(auction);
			}
			semaphore.release();
		} catch (InterruptedException e) {
		}
	}

	private String toHumanReadableTime(long time) {
		if (time / (60 * 60 * 1000) > 0) {
			return DurationFormatUtils.formatDuration(time, "HHh:mm:ss");
		}
		if (time / (60 * 1000) > 0) {
			return DurationFormatUtils.formatDuration(time, "mm:ss");
		}
		return DurationFormatUtils.formatDuration(time, "mm:ss");
	}

	private Popup createChartPopup(Set<AuctionResult> data) {
		Popup popup = new Popup();
		popup.setHflex("min");
		popup.setVflex("min");

		Image image = new Image();
		image.setParent(popup);
		image.setContent(generateChartImage(data));
		popup.setParent(this);
		return popup;
	}

	private AImage generateChartImage(Set<AuctionResult> data) {
		byte[] chartData = xyChartRenderer.render(data);
		if (chartData == null) {
			return null;
		}
		try {
			return new AImage("chart", chartData);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private class AuctionHistoryRowRenderer implements RowRenderer {

		private AuctionHistory auctionHistory;

		public AuctionHistoryRowRenderer(AuctionHistory auctionHistory) {
			this.auctionHistory = auctionHistory;
		}

		@Override
		public void render(Row row, Object data) throws Exception {
			AuctionResult result = (AuctionResult) data;
			new Label(sdf.format(result.getDate())).setParent(row);
			Label priceLabel = new Label("R " + result.getAmount());
			priceLabel.setParent(row);
			Label countLabel = new Label("" + result.getTotalNumberOfBidsPlaced());
			countLabel.setParent(row);
			if (result.getAmount() == auctionHistory.getMaxPrice()) {
				priceLabel.setStyle("color:red;font-weight:bold;");
			} else if (result.getAmount() == auctionHistory.getMinPrice()) {
				priceLabel.setStyle("color:green;font-weight:bold;");
			}
			
			new Label(result.getWinner()).setParent(row);
			Label bindsSpentLabel = new Label("" + result.getNumberOfBids());
			bindsSpentLabel.setParent(row);
			if (result.getNumberOfBids() == auctionHistory.getMaxCount()) {
				bindsSpentLabel.setStyle("color:red;font-weight:bold;");
			} else if (result.getNumberOfBids() == auctionHistory.getMinCount()) {
				bindsSpentLabel.setStyle("color:green;font-weight:bold;");
			}
		}

	}

}
