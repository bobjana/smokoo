package za.co.zynafin.smokoo.auction;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.zynafin.smokoo.Auction;

@Service
public class AuctionService {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void save(Auction auction) {
		try {
			Auction existingAuction = Auction.findAuctionsByAuctionId(auction.getAuctionId()).getSingleResult();
			existingAuction.setDate(auction.getDate());
			auction = existingAuction;
		} catch (EmptyResultDataAccessException e) {
			// DO NOTHING
		}
		entityManager.merge(auction);
	}

	public void save(List<Auction> auctions) {
		for (Auction auction : auctions) {
			save(auction);
		}
	}
	
	@Transactional
	public void close(String auctionTitle){
		try{
			Auction auction = Auction.findAuctionsByAuctionTitleEquals(auctionTitle).getSingleResult();
			auction.setClosed(true);
			entityManager.merge(auction);
		}
		catch(EmptyResultDataAccessException e){
			//DO NOTHING
		}
	}

	public List<Auction> listOpenAuctions() {
		try {
			return Auction.findAuctionsByClosed(false).getResultList();
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Auction>();
		}
	}
}
