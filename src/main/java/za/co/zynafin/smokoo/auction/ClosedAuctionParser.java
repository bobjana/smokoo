package za.co.zynafin.smokoo.auction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;

@Component
public class ClosedAuctionParser extends AuctionParser {
	
	private static final Logger log = Logger.getLogger(ClosedAuctionParser.class);
	
	@Override
	protected List<Auction> parseTagNodes(TagNode node) throws XPatherException {
		List<Auction> auctions = new ArrayList<Auction>();
		Object[] auctionNodes = node.evaluateXPath("//div[@class='div_box2_item_name']/a");
		for (Object auctionNode : auctionNodes){
			Auction auction = new Auction();
			TagNode auctionTagNode = (TagNode)auctionNode;
			auction.setAuctionTitle(parseAuctionTitle(auctionTagNode.getAttributeByName("href")));	
			auctions.add(auction);
		}
		return auctions;
	}

}
