package za.co.zynafin.smokoo.auction;

import java.util.Comparator;

public class AuctionResultBidCountComparator implements Comparator<AuctionResult>{

	@Override
	public int compare(AuctionResult o1, AuctionResult o2) {
		if (o1.getNumberOfBids() > o2.getNumberOfBids()){
			return 1;
		}else if (o1.getNumberOfBids() < o2.getNumberOfBids()){
			return -1;
		}
		return 0;
	}

	
}
