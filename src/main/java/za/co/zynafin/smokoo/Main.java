package za.co.zynafin.smokoo;

import java.util.concurrent.Executors;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jmx.export.MBeanExporter;

import za.co.zynafin.smokoo.bid.BiddingManager;
import za.co.zynafin.smokoo.bid.BiddingManagerFactory;

public class Main {

	public static void main(String[] args) throws Exception{
		final ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");

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
		
		
		final MBeanExporter exporter = context.getBean(MBeanExporter.class);
		exporter.setAutodetect(false);
		
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				BiddingManager biddingManager = context.getBean(BiddingManager.class);
//				biddingManager.start(10796, 15);
				
			}
		});
		

		
		
		
	}
}
