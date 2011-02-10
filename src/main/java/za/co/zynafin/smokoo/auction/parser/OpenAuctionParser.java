package za.co.zynafin.smokoo.auction.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Auction;

@Component
public class OpenAuctionParser extends AuctionParser{

	@Override
	protected List<Auction> parseTagNodes(TagNode node) throws XPatherException {
		List<Auction> auctions = new ArrayList<Auction>();
		Object[] auctionNodes = node.evaluateXPath("//div[@class='div_box2_item']");
		for (Object auctionNode : auctionNodes){
			Auction auction = new Auction();
			TagNode auctionTagNode = (TagNode)auctionNode;
			
			Object[] nameNodeObject = auctionTagNode.evaluateXPath("/div/a");
			TagNode nameNode = (TagNode)nameNodeObject[0];
			auction.setAuctionTitle(parseAuctionTitle(nameNode.getAttributeByName("href")));
			auction.setName(nameNode.getText().toString());
			
			Object[] incrementNodeObject = auctionTagNode.evaluateXPath("//div[@title='Variable Price increments limits']/img");
			TagNode incrementNode = (TagNode)incrementNodeObject[0];
			auction.setBidIncrementAmount(parseBidIncrementAmmount(incrementNode.getAttributeByName("src")));
			
			Object[] priceNodeObject = auctionTagNode.evaluateXPath("//div[@class='div_box2_item_price']");
			TagNode priceNode = (TagNode)priceNodeObject[0];
			auction.setRetailPrice(parseRetailPrice(priceNode.getText().toString()));
			
			Object[] timeNodeObject = auctionTagNode.evaluateXPath("//div[@class='div_box2_item_time']");
			TagNode timeNode = (TagNode)timeNodeObject[0];
			auction.setDate(parseDate(timeNode.getText().toString()));
			auction.setAuctionId(parseAuctionId(timeNode.getAttributeByName("id")));

			Object[] attributeNodes = auctionTagNode.evaluateXPath("//div[@class='div_line_ico']");
			auction.setFastAndFurious(parseFastAndFurious(attributeNodes));
			
			auctions.add(auction);
		}
		return auctions;

	}
	

	private double parseBidIncrementAmmount(String attribute) {
		String result = StringUtils.remove(attribute, "http://static.smokoo.co.za//data/images/prices/");
		result = StringUtils.remove(result, "c.gif");
		Double increment = new Double(result);
		return increment / 100;
	}


	private Date parseDate(String rawData) {
		if (StringUtils.isAlpha(rawData)){
			return null;
		}
		Integer secondsLeft = new Integer(rawData);
		return DateUtils.addSeconds(new Date(), secondsLeft);
	}

	private double parseRetailPrice(String rawData) {
		String prefix = "Retail Price  R";
		if (rawData == null || !rawData.startsWith(prefix)){
			throw new RuntimeException("Unable to parse retail price - " + rawData);
		}
		rawData = StringUtils.remove(rawData, ',');
		return new Double(StringUtils.remove(rawData, prefix));
	}

	private long parseAuctionId(String rawData) {
		String prefix = "timeleft_";
		if (rawData == null || !rawData.startsWith(prefix)){
			throw new RuntimeException("Unable to parse action ID");
		}
		String id = StringUtils.remove(rawData, prefix);
		return new Long(id);
	}
	
	private boolean parseFastAndFurious(Object[] attributeNodes) {
		for (Object attr : attributeNodes){
			TagNode tagNode = (TagNode) attr;
			String title = tagNode.getAttributeByName("title");
			if (title != null && title.equals("Fast and Furious Auction")){
				return true;
			}
		}
		return false;
	}


}
