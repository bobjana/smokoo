package za.co.zynafin.smokoo.bid;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import za.co.zynafin.smokoo.Bid;

@Repository
public class BidDao {

	@Resource
	private DataSource dataSource;
	private SimpleJdbcTemplate template;
	private static String TOP_USER_BIDS = "select count(b.id) as hits,b.user,max(b.ammount),max(b.date) from bid b, auction a where a.auction_id = ? and b.auction = a.id group by user order by hits desc limit ?";
	private static String LATEST_BIDS = "select a.date,ammount,user,type from bid b, auction a where a.auction_id = ? and b.auction = a.id order by date asc limit ?";
	
	public void init(){
		template = new SimpleJdbcTemplate(dataSource);
	}
	
	public List<UserBidSummary> listTopUserBids(long auctionId, int topUserCount){
		return template.query(TOP_USER_BIDS, new RowMapper<UserBidSummary>() {

			@Override
			public UserBidSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new UserBidSummary(rs.getString(2), rs.getInt(1), rs.getTimestamp(4), rs.getDouble(3));
			}
			
		}, auctionId,topUserCount);
	}
	
	public List<Bid> listLastestBids(long auctionId, int numberOfBids){
		return template.query(LATEST_BIDS, new RowMapper<Bid>() {

			@Override
			public Bid mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bid bid = new Bid();
				bid.setDate(new Date(rs.getTimestamp(1).getTime()));
				bid.setAmmount(rs.getDouble(2));
				bid.setUser(rs.getString(3));
				bid.setType(rs.getString(4));
				return bid;
			}
			
		}, auctionId,numberOfBids);

	}
	
	public List<UserBidSummary> listLastestBidsSummary(long auctionId, int numberOfBids){
		List<Bid> bids = listLastestBids(auctionId, numberOfBids);
		List<UserBidSummary> result = new ArrayList<UserBidSummary>();
		Map<String,Integer> userCount = new HashMap<String, Integer>();
		for (Bid bid : bids){
			String user = bid.getUser();
			if (!userCount.containsKey(user)){
				userCount.put(user, 0);
			}
			userCount.put(user, userCount.get(user) + 1);
		}
		for (String user : userCount.keySet()){
			result.add(new UserBidSummary(user, userCount.get(user),null,0d));
		}
		return result;
	}
}
