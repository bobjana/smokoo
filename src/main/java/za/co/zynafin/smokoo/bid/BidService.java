package za.co.zynafin.smokoo.bid;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.zynafin.smokoo.Auction;
import za.co.zynafin.smokoo.Bid;
import za.co.zynafin.smokoo.auction.AuctionClosedException;

@Service
public class BidService {

	@PersistenceContext
	private EntityManager entityManager;
	
	private BidDao bidDao;
	
	@Autowired
	public void setBidDao(BidDao bidDao) {
		this.bidDao = bidDao;
	}

	@Transactional
	public void save(Auction auction, List<Bid> bids) throws AuctionClosedException{
		boolean newBidReceived = false;
		for (Bid bid : bids) {
			bid.setAuction(auction);
			if (!exists(auction, bid)) {
				entityManager.persist(bid);
				newBidReceived = true;
			}
		}
		if (!newBidReceived && new Date().after(auction.getDate())){
			throw new AuctionClosedException();
		}
	}

	private boolean exists(Auction auction, Bid bid) {
		try {
			return Bid.findBidsByAuctionAndUserAndDate(auction, bid.getUser(), bid.getDate()).getSingleResult() != null;
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	public List<UserBidSummary> listTopBidders(Auction auction){
		return bidDao.listTopUserBids(auction.getAuctionId(), 5);
	}

}
