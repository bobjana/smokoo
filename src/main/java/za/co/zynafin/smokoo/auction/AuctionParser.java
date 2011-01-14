package za.co.zynafin.smokoo.auction;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import za.co.zynafin.smokoo.Auction;

public abstract class AuctionParser {

	public final List<Auction> parse(String content){
		try {
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode node = cleaner.clean(content);
			return parseTagNodes(node);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse auction summary", e);
		}
	}

	protected abstract List<Auction> parseTagNodes(TagNode node) throws XPatherException;
	
	protected String parseAuctionTitle(String rawData) {
		return StringUtils.remove(rawData, "/auctions/");
	}

}