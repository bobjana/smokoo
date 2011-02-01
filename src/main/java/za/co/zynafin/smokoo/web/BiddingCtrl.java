package za.co.zynafin.smokoo.web;

import java.text.SimpleDateFormat;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Spinner;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionService;
import za.co.zynafin.smokoo.auction.parser.AuctionActivityEvent;
import za.co.zynafin.smokoo.bid.BiddingStatsListener;
import za.co.zynafin.smokoo.bid.UserBidSummary;

public class BiddingCtrl extends BaseCtrl implements BiddingStatsListener {

	private AuctionService auctionService;
	private Auction auction;
	private Button autoBidBtn;
	private Spinner maxNumberOfBidsSpinner;
	private Grid totalBidStats;
	private Html summaryHtml;

	@Override
	public void afterCompose() {
		super.afterCompose();
		auction = Auction.findAuction(new Long(Executions.getCurrent().getParameter("auction")));
		refreshAutoBiddingUI();
		subcribeToEventQueue();
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
		if (auction.isBiddingAutomated()){
			auction.stopAutomatedBidding();
		}
		else{
			auction.startAutomatedBidding();
		}
		refreshAutoBiddingUI();
	}

	public void onClick$manualBidBtn() {
		auctionService.placeBid(auction);
	}

	@Override
	public void display(List<UserBidSummary> userBidSummaries) {
		totalBidStats.setModel(new SimpleListModel(userBidSummaries));
		totalBidStats.setRowRenderer(new RowRenderer() {

			@Override
			public void render(Row arg0, Object arg1) throws Exception {
				UserBidSummary summary = (UserBidSummary) arg1;
				new Label(summary.getUser()).setParent(arg0);
				new Label("" + summary.getBidCount()).setParent(arg0);
				new Label(new SimpleDateFormat("hh:mm").format(summary.getLastBid())).setParent(arg0);
			}
		});
	}

	private void subcribeToEventQueue() {
		EventQueues.lookup("spring-to-zk-eventqueue", EventQueues.APPLICATION, true).subscribe(new EventListener() {
			public void onEvent(Event event) {
				if (event.getData() instanceof AuctionActivityEvent) {
					summaryHtml.setContent(((AuctionActivityEvent) event.getData()).getRawContent());
				}
			}
		});
	}
}
