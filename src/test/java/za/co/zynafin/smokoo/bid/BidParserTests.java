package za.co.zynafin.smokoo.bid;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import za.co.zynafin.smokoo.Bid;
import za.co.zynafin.smokoo.bid.BidParser;

public class BidParserTests {
	//http://www.smokoo.co.za/ajax_get_auction_bids.php?title=smokooauctionipad64gb_3gwifi_2_f91cc1
	@Test
	public void parse() throws Exception {
		//GIVEN
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		String todaysDate = sdf.format(new Date()).substring(0,11);
		BidParser parser = new BidParser();
		File file = new DefaultResourceLoader().getResource("classpath:bid_history_output.txt").getFile();
		String content = FileUtils.readFileToString(file);
		//WHEN
		List<Bid> result = parser.parse(content);
		//THEN
		assertEquals(10, result.size());
		Bid firstBid = result.get(0);
		assertEquals("Mave123",firstBid.getUser());
		assertEquals("Auto Bid",firstBid.getType());
		assertEquals(new Double(407.30d),new Double(firstBid.getAmmount()));
		assertEquals(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(todaysDate + "19:58:01"),firstBid.getDate());
		
		Bid lastBid = result.get(9);
		assertEquals("boerie", lastBid.getUser());
	}
}
