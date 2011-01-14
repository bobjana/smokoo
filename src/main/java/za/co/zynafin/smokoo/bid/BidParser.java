package za.co.zynafin.smokoo.bid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Bid;

@Component
public class BidParser {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	
	/*
	 * time	amount	user type
	 */
	public List<Bid> parse(String content) throws ParseException{
		if (StringUtils.isEmpty(content)){
			return new ArrayList<Bid>();
		}
		String todaysDate = sdf.format(new Date()).substring(0,11);
		List<Bid> result = new ArrayList<Bid>();
		List<List<String>> rawData = parseRawData(content);
		for (List<String> columns : rawData){
			if (columns.size() != 4){
				continue;
			}
			Bid bid = new Bid();
			bid.setDate(sdf.parse(todaysDate + columns.get(0)));
			bid.setAmmount(new Double(StringUtils.remove(columns.get(1), 'R')));
			bid.setUser(columns.get(2));
			bid.setType(columns.get(3));
			result.add(bid);
		}
		return result;
	}

	private List<List<String>> parseRawData(String content) {
		final List<List<String>> data = new LinkedList<List<String>>();
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(content);

		node.traverse(new TagNodeVisitor() {
		    public boolean visit(TagNode tagNode, HtmlNode htmlNode) {
		    	int index = -1;
		        if (htmlNode instanceof TagNode) {
		            TagNode tag = (TagNode) htmlNode;
		            String tagName = tag.getName();
		            if ("tr".equals(tagName)) {
		            	index++;
		            	data.add(index,new ArrayList<String>());
		            }
		            else if ("td".equals(tagName)){
		            	data.get(0).add(tag.getText().toString());
		            }
		        } 
		        return true;
		    }
		});
		return data;
	}
}
