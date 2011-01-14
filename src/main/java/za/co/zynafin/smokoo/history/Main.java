package za.co.zynafin.smokoo.history;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionRefreshJob;
import za.co.zynafin.smokoo.auction.AuctionService;
import za.co.zynafin.smokoo.auction.CloseAuctionJob;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");

//		Auction auction = new Auction();
//		String auctionTitle = "smokooauction_sonyledbraviakdl_40ex600_2_041301";
//		auction.setAuctionTitle(auctionTitle);
//		
//		AuctionService auctionService = context.getBean(AuctionService.class);
//		Auction persistedAuction = null;
//		try {
//			persistedAuction = Auction.findAuctionsByAuctionTitleEquals(auctionTitle).getSingleResult();
//		} catch (Exception e) {
//		}
//		if (persistedAuction == null){
//			auctionService.save(auction);
//			auction = Auction.findAuctionsByAuctionTitleEquals(auctionTitle).getSingleResult();
//		}
//		else{
//			auction = persistedAuction;
//		}
		
//		BidHistoryExecutor executor = context.getBean(BidHistoryExecutor.class);
//		executor.startExecution(auction);
		
//		AuctionRefreshJob refreshJob = context.getBean(AuctionRefreshJob.class);
//		refreshJob.run();
		
//	ClosedAuctionMonitorJob janitorJob = context.getBean(ClosedAuctionMonitorJob.class);
//	janitorJob.run();
		
	}
}
