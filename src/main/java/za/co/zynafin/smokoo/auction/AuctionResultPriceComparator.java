package za.co.zynafin.smokoo.auction;

import java.util.Comparator;

public class AuctionResultPriceComparator implements Comparator<AuctionResult>{

	@Override
	public int compare(AuctionResult o1, AuctionResult o2) {
		if (o1.getAmount() > o2.getAmount()){
			return 1;
		}else if (o1.getAmount() < o2.getAmount()){
			return -1;
		}
		return 0;
	}

	
}
