package za.co.zynafin.smokoo.auction;

import java.util.Comparator;

import za.co.zynafin.smokoo.Auction;

public class AuctionDateComparator implements Comparator<Auction>{

	@Override
	public int compare(Auction o1, Auction o2) {
		if (o1.getDate() == null && o2.getDate() != null){
			return -1;
		}else if(o2.getDate() == null && o1.getDate() != null){
			return 1;
		}
		return o1.getDate().compareTo(o2.getDate());
	}

	
}
