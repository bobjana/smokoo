package za.co.zynafin.smokoo.history;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.junit.Test;
import org.mockito.Mockito;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Bid;
import za.co.zynafin.smokoo.bid.BidParser;
import za.co.zynafin.smokoo.bid.BidService;
import za.co.zynafin.smokoo.history.BiddingRecorder;
import za.co.zynafin.smokoo.io.SmokooConnector;

public class BidHistoryRecorderTests {

	@Test
	public void record() throws Exception {
		// GIVEN
		BiddingRecorder recorder = new BiddingRecorder();
		SmokooConnector connector = Mockito.mock(SmokooConnector.class);
		recorder.setSmokooConnector(connector);
		Auction auction = new Auction();
		BidService bidHistoryService = Mockito.mock(BidService.class);
		recorder.setBidHistoryService(bidHistoryService);
		BidParser parser = Mockito.mock(BidParser.class);
		List<Bid> mockBids = new ArrayList<Bid>();
		Mockito.when(parser.parse(null)).thenReturn(mockBids);
		recorder.setBidParser(parser);
		// WHEN
		recorder.record(auction);
		// THEN
		Mockito.verify(parser).parse(Mockito.anyString());
		Mockito.verify(bidHistoryService).save(auction, mockBids);
	}

	


}
