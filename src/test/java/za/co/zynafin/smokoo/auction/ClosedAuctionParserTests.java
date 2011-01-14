package za.co.zynafin.smokoo.auction;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.auction.parser.ClosedAuctionParser;

public class ClosedAuctionParserTests {

	@Test
	public void parse() throws Exception {
		//GIVEN
		ClosedAuctionParser parser = new ClosedAuctionParser();
		File file = new DefaultResourceLoader().getResource("classpath:closed_auction_summary.htm").getFile();
		String content = FileUtils.readFileToString(file);
		int expectedSize = 20;
		//WHEN
		List<Auction> result = parser.parse(content);
		//THEN
		assertEquals(expectedSize, result.size());
		Auction firstAuction = result.get(0);
		assertEquals("smokooauctioitunesg_f5ca37",firstAuction.getAuctionTitle());

		assertEquals("smokoostudio_headphones_beats_by_drdre_by__501b6e", result.get(expectedSize - 1).getAuctionTitle());
	}
	
}
