package za.co.zynafin.smokoo.history;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import za.co.zynafin.smokoo.Auction;

public class BiddingRecorderJobTests {

	private BiddingRecorder recorder = Mockito.mock(BiddingRecorder.class);
	BiddingRecorderJob job;
	
	@Before
	public void setup(){
		job = new BiddingRecorderJob();
		job.setBidHistoryRecorder(recorder); 
	}
	
	@Test
	public void startRecording() throws Exception {
		//GIVEN
		Auction auction = new Auction();
		auction.setId(1l);
		auction.setAuctionTitle("abc");
		//WHEN
		job.startRecording(auction);
		//THEN
		assertEquals(1,job.numberOfJobsRunning());
	}
	
	@Test
	public void startRecording_DifferentJob() throws Exception {
		//GIVEN
		Auction auction = new Auction();
		auction.setId(1l);
		auction.setAuctionTitle("abc");

		Auction auction2 = new Auction();
		auction2.setId(2l);
		auction2.setAuctionTitle("def");
		//WHEN
		job.startRecording(auction,2000);
		job.startRecording(auction2,4000);
		//THEN
		assertEquals(2,job.numberOfJobsRunning());
	}
	
	@Test
	public void startRecording_SameJob() throws Exception {
		//GIVEN
		Auction auction = new Auction();
		auction.setAuctionTitle("abc");

		Auction auction2 = new Auction();
		auction2.setAuctionTitle("abc");
		//WHEN
		job.startRecording(auction,2000);
		job.startRecording(auction2,2000);
		//THEN
		assertEquals(1,job.numberOfJobsRunning());
	}
}
