package za.co.zynafin.smokoo.web;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Spinner;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.parser.AuctionActivityEvent;
import za.co.zynafin.smokoo.bid.BiddingManager;
import za.co.zynafin.smokoo.bid.BiddingStatsListener;
import za.co.zynafin.smokoo.bid.UserBidSummary;

public class BiddingCtrl extends BaseCtrl implements BiddingStatsListener, ApplicationListener<AuctionActivityEvent>{

	private BiddingManager biddingManager;
	private Auction auction;
	private Button autoBidBtn;
	private Spinner maxNumberOfBidsSpinner;
	private Grid totalBidStats;
	private Html summaryHtml;

	@Override
	public void afterCompose() {
		super.afterCompose();
System.out.println("after....");
		doOnCreateCommon(this);
		this.biddingManager = (BiddingManager) Executions.getCurrent().getArg().get("biddingManager");
		this.auction = (Auction) Executions.getCurrent().getArg().get("auction");
		this.biddingManager.start(auction);
		biddingManager.addStatsListeners(this);
		refreshAutoBiddingUI();
	}

	private void refreshAutoBiddingUI() {
		boolean enabled = biddingManager.isEnabled();
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
		boolean enabled = biddingManager.isEnabled();
		if (enabled) {
			biddingManager.disable();
		} else {
			biddingManager.enable();
		}
		refreshAutoBiddingUI();
	}

	public void onClick$manualBidBtn() {
		biddingManager.placeManualBid();
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

	@Override
	public void onApplicationEvent(AuctionActivityEvent event) {
		summaryHtml.setContent(event.getRawContent());
	}
	
}
