package za.co.zynafin.smokoo;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import za.co.zynafin.smokoo.auction.AutomateAuctionEvent;
import za.co.zynafin.smokoo.util.ApplicationEventPublisher;

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

	private boolean fastAndFurious;
	
	private boolean biddingAutomated;
	
	private int maxNumberOfBids;
	
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

	public static TypedQuery<Auction> findAuctionsByClosedAndDateNotNull(boolean closed) {
		EntityManager em = Auction.entityManager();
		TypedQuery<Auction> q = em
				.createQuery("SELECT Auction FROM Auction AS auction WHERE auction.closed = :closed and auction.date != null",
						Auction.class);
		q.setParameter("closed", closed);
		return q;
	}

	public void startAutomatedBidding(){
		if (biddingAutomated){
			return;
		}
		biddingAutomated = true;
		ApplicationEventPublisher.publishEvent(new AutomateAuctionEvent(this));
	}
	
	public void stopAutomatedBidding(){
		if (!biddingAutomated){
			return;
		}
		biddingAutomated = false;
		ApplicationEventPublisher.publishEvent(new AutomateAuctionEvent(this));
	}
	

}
