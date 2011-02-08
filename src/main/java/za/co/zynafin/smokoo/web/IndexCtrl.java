package za.co.zynafin.smokoo.web;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionDateComparator;
import za.co.zynafin.smokoo.auction.AuctionService;
import za.co.zynafin.smokoo.util.ApplicationEventTranslator;

public class IndexCtrl extends BaseCtrl{

	private AuctionService auctionService;
	private Grid openAuctionsGrid;
	private ApplicationEventTranslator applicationEventTranslator;
	
	@Override
	public void afterCompose() {
		super.afterCompose();
		List<Auction> auctions = auctionService.listOpenAuctions();
		Collections.sort(auctions, new AuctionDateComparator());
		openAuctionsGrid.setAutopaging(true);
		openAuctionsGrid.setModel(new SimpleListModel(auctions.toArray()));
		openAuctionsGrid.setRowRenderer(new AuctionRowRenderer());
	}
	
	private class AuctionRowRenderer implements RowRenderer{

		private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		@Override
		public void render(Row row, Object data) throws Exception {
			final Auction auction = (Auction) data;
			Hbox iconsBox = new Hbox();
			iconsBox.setParent(row);
			if (auction.isFastAndFurious()){
				new Image("images/fast_furious.png").setParent(iconsBox);
			}
			new Label(auction.getName()).setParent(row);
			new Label(sdf.format(auction.getDate())).setParent(row);
			new Label("R " + auction.getRetailPrice()).setParent(row);
			Image gotoAuctionImg = new Image("images/update.png");
			gotoAuctionImg.addEventListener("onClick", new EventListener() {
				
				@Override
				public void onEvent(Event event) throws Exception {
//					Executions.getCurrent().setAttribute("auction", auction.getId());
					Executions.sendRedirect("auction.zul?auction=" + auction.getId());
				}
			});
			gotoAuctionImg.setParent(row);
		}
		
	}
	
}
