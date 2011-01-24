package za.co.zynafin.smokoo.bid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BidDao {

	@Resource
	private DataSource dataSource;
	private SimpleJdbcTemplate template;
	private static String TOP_USER_BIDS = "select count(b.id) as hits,b.user,max(b.ammount),max(b.date) from bid b, auction a where a.auction_id = ? and b.auction = a.id group by user order by hits desc limit ?";
	
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
}
