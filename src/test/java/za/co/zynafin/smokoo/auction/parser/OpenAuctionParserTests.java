package za.co.zynafin.smokoo.auction.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.parser.OpenAuctionParser;

public class OpenAuctionParserTests {

	@Test
	public void parse() throws Exception {
		//GIVEN
		OpenAuctionParser parser = new OpenAuctionParser();
		File file = new DefaultResourceLoader().getResource("classpath:open_auction_summary.htm").getFile();
		String content = FileUtils.readFileToString(file);
		int expectedSize = 16;
		//WHEN
		List<Auction> result = parser.parse(content);
		//THEN
		assertEquals(expectedSize, result.size());
		Auction firstAuction = result.get(0);
		assertEquals("BRAVIA LED KDL EX600",firstAuction.getName());
		assertEquals("smokooauction_sonyledbraviakdl_40ex600_2_041301",firstAuction.getAuctionTitle());
		assertEquals(9685, firstAuction.getAuctionId());
		assertEquals(DateUtils.truncate(DateUtils.addSeconds(new Date(), 7),Calendar.SECOND), DateUtils.truncate(firstAuction.getDate(),Calendar.SECOND));
		assertEquals(new Double(14999.90), new Double(firstAuction.getRetailPrice()));
		assertEquals(false, firstAuction.isFastAndFurious());
		assertEquals(new Double(0.05d), new Double(firstAuction.getBidIncrementAmount()));
		
		assertEquals("ARCHOS 10.1\" tablet", result.get(expectedSize - 1).getName());
	}
	
}
