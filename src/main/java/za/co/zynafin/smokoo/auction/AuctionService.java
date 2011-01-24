package za.co.zynafin.smokoo.auction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.zynafin.smokoo.Auction;

@Service
public class AuctionService {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AuctionDao auctionDao;

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
			return Auction.findAuctionsByClosedAndDateNotNull(false).getResultList();
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Auction>();
		}
	}
	
	public AuctionHistory getAuctionHistory(Auction auction, AuctionIntervalType type){
		List<AuctionResult> results = auctionDao.getAuctionResults(new AuctionResultRequest(auction,type));
		return new AuctionHistory(auction, type, results);
	}
	
	public AuctionBidCountHistory getAuctionBidCountHistory(String auction,Period period){
		
		AuctionBidCountHistory auctionBidCountHistory = new AuctionBidCountHistory(new Integer[][]{
      {19,34,56},//1
      {5,5,2},//2
      {8,6,7},//3
      {34,4,5,8,2},//4
      {1},//5
      {34,5,2,1},//6
      {3,2,65},//7
		});
		
		return auctionBidCountHistory;
		
	}
}
