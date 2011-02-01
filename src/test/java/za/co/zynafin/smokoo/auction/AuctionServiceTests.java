package za.co.zynafin.smokoo.auction;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.parser.TimeRemainingParser;
import za.co.zynafin.smokoo.io.SmokooConnector;

public class AuctionServiceTests {
	
	private AuctionIdRandomizer randomizer;
	private AuctionService auctionService;
	private TimeRemainingParser timeRemainingParser;
	private SmokooConnector connector;

	@Before
	public void setup(){
		randomizer = Mockito.mock(AuctionIdRandomizer.class);
		Mockito.when(randomizer.randomize()).thenReturn(0.123d);
		
		auctionService = new AuctionService(randomizer);
		connector = Mockito.mock(SmokooConnector.class);
		Mockito.when(connector.get(Constants.AUCTION_TIME_REMAING_URL + "?auction_ids=1&unique_string=0.123")).thenReturn("piped|content");
		auctionService.setSmokooConnector(connector);
		
		timeRemainingParser = Mockito.mock(TimeRemainingParser.class);
		auctionService.setTimeRemainingParser(timeRemainingParser );
	}
	
	@Test
	public void getTimeRemaining() throws Exception {
		//GIVEN
		Mockito.when(timeRemainingParser.parse("piped|content", 1l)).thenReturn(3000l);
		Auction auction = new Auction();
		auction.setAuctionId(1l);
		//WHEN
		Date startDate = new Date();
		long result = auctionService.getTimeRemaining(auction);
		Date endDate = new Date();
		long processPeriod = endDate.getTime() - startDate.getTime();
		long estimatedTime = 3000l - processPeriod;
		//THEN
		assertEquals(estimatedTime,result);
	}
	
	@Test
	public void getTimeRemaining_TimeUp() throws Exception {
		Mockito.when(timeRemainingParser.parse("piped|content", 1l)).thenReturn(-1l);
		Auction auction = new Auction();
		auction.setAuctionId(1l);
		//WHEN
		long result = auctionService.getTimeRemaining(auction);
		//THEN
		assertEquals(0l,result);
	}
}
