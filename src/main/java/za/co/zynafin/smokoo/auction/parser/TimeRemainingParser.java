package za.co.zynafin.smokoo.auction.parser;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TimeRemainingParser {

	public long parse(String content, Long auctionId) {
		String[] tokens = StringUtils.split(content, "^!%");
		for (String token : tokens){
			String[] parts = StringUtils.split(token, "|");
			if (auctionId.toString().equals(parts[0])){
				String timePart = parts[1];
				String userPart = parts[3];
				if ("bobjana".equals(userPart)){
					return 5000;
				}
				return new Long(StringUtils.remove(timePart, "."));
			}
		}
		return -1;
	}
}
