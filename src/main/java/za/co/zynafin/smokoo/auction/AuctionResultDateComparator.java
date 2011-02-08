package za.co.zynafin.smokoo.auction;

import java.util.Comparator;

public class AuctionResultDateComparator implements Comparator<AuctionResult>{

	@Override
	public int compare(AuctionResult o1, AuctionResult o2) {
		return o1.getDate().compareTo(o2.getDate());
	}

	
}
