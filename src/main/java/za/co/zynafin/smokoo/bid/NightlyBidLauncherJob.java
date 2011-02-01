package za.co.zynafin.smokoo.bid;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.AuctionClosedEvent;
import za.co.zynafin.smokoo.auction.AuctionService;

@Component
public class NightlyBidLauncherJob extends TimerTask implements ApplicationContextAware,
		ApplicationListener<AuctionClosedEvent> {

	private AuctionService auctionService;
	private ApplicationContext applicationContext;
//	private Map<Auction, BiddingManager> processedAuctions = new HashMap<Auction, BiddingManager>();

	@Autowired
	public void setAuctionService(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void run() {
//		Calendar cal = new GregorianCalendar();
//		cal.setTime(new Date());
//		if (cal.get(Calendar.HOUR_OF_DAY) > 1 && cal.get(Calendar.HOUR_OF_DAY) < 5) {
//			List<Auction> auctions = auctionService.listOpenAuctions();
//			Date cuttoff = DateUtils.addMinutes(new Date(), 30);
//			for (Auction auction : auctions) {
//				if (processedAuctions.containsKey(auction)) {
//					continue;
//				}
//				if (auction.getName().endsWith("Smokoo Credits") && auction.getDate().before(cuttoff)) {
//					BiddingManager biddingManager = applicationContext.getBean(BiddingManager.class);
//					biddingManager.start(auction);
//					processedAuctions.put(auction, biddingManager);
//					
//
//					ObjectName objectName = null;
//					try {
//						objectName = new ObjectName("bean:name=test123");
//					} catch (MalformedObjectNameException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (NullPointerException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
////
////					objectName = ObjectNameManager.getInstance(objName);
////					exporter.setAutodetect(false);
//					MBeanExporter exporter = applicationContext.getBean(MBeanExporter.class);
//					exporter.setAutodetect(false);
//					
//					exporter.registerManagedResource(biddingManager, objectName);
//					
//					
//					
//					break;
//				}
//			}
//		}
	}

	@Override
	public void onApplicationEvent(AuctionClosedEvent event) {
//		BiddingManager manager = processedAuctions.get(event.getAuction());
//		if (manager == null) {
//			return;
//		}
////		manager.stop();
//		processedAuctions.remove(event.getAuction());
//		manager = null;
	}

}
