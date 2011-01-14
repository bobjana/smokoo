package za.co.zynafin.smokoo;

import java.util.Date;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findAuctionsByAuctionTitleEquals", "findAuctionsByAuctionId", "findAuctionsByClosed" })
public class Auction {

    private String name;

    private String auctionTitle;

    private long auctionId;

    private Date date;

    private boolean closed;

    private double retailPrice;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((auctionTitle == null) ? 0 : auctionTitle.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Auction other = (Auction) obj;
			if (auctionTitle == null) {
				if (other.auctionTitle != null)
					return false;
			} else if (!auctionTitle.equals(other.auctionTitle))
				return false;
			return true;
		}
    
    
    
}
