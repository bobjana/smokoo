package za.co.zynafin.smokoo.bid;

import java.util.Date;
import java.util.List;

class ConsoleBiddingStatsListener implements BiddingStatsListener {

	@Override
	public void display(List<UserBidSummary> userBidSummaries) {
		if (userBidSummaries.size() > 0) {
			System.out.println(String.format("*********%s*************",new Date().toString()));
			for (UserBidSummary userBidSummary : userBidSummaries) {
				System.out.println(userBidSummary.getBidCount() + "\t" + userBidSummary.getAmount() + "\t"
						+ userBidSummary.getLastBid() + "\t" + userBidSummary.getUser());
			}
		}
	}

}