package za.co.zynafin.smokoo;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import za.co.zynafin.smokoo.auction.TimeRemainingCalculator;
import za.co.zynafin.smokoo.bid.BiddingManager;

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
		
		
//		TimeRemainingCalculator calc = context.getBean(TimeRemainingCalculator.class);
//		
//		long gap = 500;
//		while (true){
//			long start = new Date().getTime();
//			long time = calc.calculate(9724l);
//			long dur = new Date().getTime() - start;
//			System.out.println(time + " " + dur);
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		BiddingManager biddingManager = context.getBean(BiddingManager.class);
		biddingManager.bid(10769, 5);
	}
}
