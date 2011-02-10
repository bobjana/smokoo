package za.co.zynafin.smokoo.web;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Spinner;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Bid;
import za.co.zynafin.smokoo.auction.AuctionService;
import za.co.zynafin.smokoo.auction.parser.AuctionActivityEvent;
import za.co.zynafin.smokoo.bid.BidDao;
import za.co.zynafin.smokoo.bid.UserBidSummary;
import za.co.zynafin.smokoo.history.RecordingSpeedChangeEvent;
import za.co.zynafin.smokoo.util.ApplicationEventPublisher;
import za.co.zynafin.smokoo.util.ApplicationEventTranslator;
import za.co.zynafin.smokoo.util.ComponentListener;

public class BiddingCtrl extends BaseCtrl implements ComponentListener{

	private static final Logger log = Logger.getLogger(BiddingCtrl.class);
	private AuctionService auctionService;
	private Auction auction;
	private Button autoBidBtn;
	private Spinner maxNumberOfBidsSpinner;
	private Grid totalBidStatsGrid;
	private Grid latestBidStatsGrid;
	private Grid bidHistoryGrid;
	private ApplicationEventTranslator applicationEventTranslator;
	private BidDao bidDao;
	private BlockingQueue<AuctionActivityEvent> events = new LinkedBlockingQueue<AuctionActivityEvent>();

	@Override
	public void afterCompose() {
		super.afterCompose();
		this.getDesktop().enableServerPush(true);

		auction = Auction.findAuction(new Long(Executions.getCurrent().getParameter("auction")));
		refreshAutoBiddingUI();
		StatsDisplayer statsDisplayer = new StatsDisplayer();
		applicationEventTranslator.addComponentListener(this);
		Executors.newSingleThreadExecutor().execute(statsDisplayer);

		bidHistoryGrid.setRowRenderer(new BidHistoryRowRenderer());
		latestBidStatsGrid.setRowRenderer(new TopBidRowRender());
		totalBidStatsGrid.setRowRenderer(new TopBidRowRender());

		displayBidHistory(bidDao.listLastestBids(auction.getAuctionId(), 10));
		displayTopUserBids(bidDao.listTopUserBids(auction.getAuctionId(), 10));
		displayLastestBids(bidDao.listLastestBidsSummary(auction.getAuctionId(), 10));

	}

	private void refreshAutoBiddingUI() {
		boolean enabled = auction.isBiddingAutomated();
		maxNumberOfBidsSpinner.setDisabled(!enabled);
		maxNumberOfBidsSpinner.setSelectionRange(0, 100);
		maxNumberOfBidsSpinner.setStep(5);
		if (enabled) {
			autoBidBtn.setLabel("Disable");
		} else {
			autoBidBtn.setLabel("Enable");
		}
	}

	public void onClick$autoBidBtn() {
		if (auction.isBiddingAutomated()) {
			auction.stopAutomatedBidding();
		} else {
			auction.startAutomatedBidding();
		}
		refreshAutoBiddingUI();
	}

	public void onClick$manualBidBtn() {
		auctionService.placeBid(auction);
	}

	private class StatsDisplayer extends Thread {
//
//		BlockingQueue<AuctionActivityEvent> events;
//		
//		public StatsDisplayer(BlockingQueue<AuctionActivityEvent> events){
//			this.events = events;
//		}

		@Override
		public void run() {
			try {
				while (true) {
					try {
	System.out.println("event removed1 before.... " + events.size());
						AuctionActivityEvent event = events.take();
	System.out.println("event removed1 after .... " + events.size());
						Thread.sleep(5000);
//						Executions.activate(BiddingCtrl.this.getDesktop());
//
//						displayBidHistory(event.getBids());
//						displayTopUserBids(bidDao.listTopUserBids(auction.getAuctionId(), 10));
//						displayLastestBids(bidDao.listLastestBidsSummary(auction.getAuctionId(), 10));
//
//						Executions.deactivate(BiddingCtrl.this.getDesktop());
					}catch(DesktopUnavailableException e){
						log.error(e);
					} catch (Throwable ex) {
						log.error(ex);
						throw UiException.Aide.wrap(ex);
					}
				}
			} finally {
				 ApplicationEventPublisher.publishEvent(new RecordingSpeedChangeEvent(this, auction, 20000));
			}
		}

	}
	
	
	@Override
	public void onEvent(ApplicationEvent event) {
		if (event instanceof AuctionActivityEvent) {
//System.out.println(event);
			if (((AuctionActivityEvent) event).getAuction().equals(auction)) {
				try {
					events.put((AuctionActivityEvent) event);
System.err.println("event recieved... " + events.size());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private void displayBidHistory(List<Bid> bids) {
		Collections.reverse(bids);
		bidHistoryGrid.setModel(new ListModelList(bids));
	}

	private void displayLastestBids(List<UserBidSummary> userBidSummaries) {
		latestBidStatsGrid.setModel(new ListModelList(userBidSummaries));
	}

	private void displayTopUserBids(List<UserBidSummary> userBidSummaries) {
		totalBidStatsGrid.setModel(new SimpleListModel(userBidSummaries));
	}

	private class TopBidRowRender implements RowRenderer {

		@Override
		public void render(Row arg0, Object arg1) throws Exception {
			UserBidSummary summary = (UserBidSummary) arg1;
			new Label(summary.getUser()).setParent(arg0);
			new Label("" + summary.getBidCount()).setParent(arg0);
		}
	}

	private class BidHistoryRowRenderer implements RowRenderer {

		private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		@Override
		public void render(Row row, Object data) throws Exception {
			Bid bid = (Bid) data;
			new Label(sdf.format(bid.getDate())).setParent(row);
			new Label("R " + bid.getAmmount()).setParent(row);
			new Label(bid.getUser()).setParent(row);
			new Label(bid.getType()).setParent(row);
		}

	}

}
