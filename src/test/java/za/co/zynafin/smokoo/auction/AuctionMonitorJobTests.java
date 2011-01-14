package za.co.zynafin.smokoo.auction;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.history.BiddingRecorderJob;


public class AuctionMonitorJobTests {

	@Test
	public void run() throws Exception {
		//GIVEN
		AuctionMonitorJob monitorJob = new AuctionMonitorJob();
		monitorJob.setTimeBuffer(5000);

		List<Auction> testAuctions = new ArrayList<Auction>();
		Auction auction1 = createTestAuction("auction1",DateUtils.addSeconds(new Date(), 10000));
		testAuctions.add(auction1);
		Auction auction2 = createTestAuction("auction2",DateUtils.addSeconds(new Date(), 4000));
		testAuctions.add(auction2);
		AuctionService auctionService = mock(AuctionService.class);
		when(auctionService.listOpenAuctions()).thenReturn(testAuctions);
		monitorJob.setAuctionService(auctionService);

		BiddingRecorderJob bidHistoryExecutor = mock(BiddingRecorderJob.class);
		monitorJob.setBidHistoryRecorderJob(bidHistoryExecutor);
		//WHEN
		monitorJob.run();
		Thread.sleep(1000);
		monitorJob.cancel();
		//THEN
		verify(auctionService).listOpenAuctions();
		verify(bidHistoryExecutor).startExecution(auction2);
	}

	private Auction createTestAuction(String name,Date date) {
		Auction auction = new Auction();
		auction.setName(name);
		auction.setDate(date);
		return auction;
	}
}
