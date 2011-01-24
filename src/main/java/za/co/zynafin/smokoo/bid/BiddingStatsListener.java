package za.co.zynafin.smokoo.bid;

import java.util.List;

public interface BiddingStatsListener {

	void display(List<UserBidSummary> userBidSummaries);
}
