package za.co.zynafin.smokoo.auction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuctionDao {

	@Resource
	private DataSource dataSource;
	private SimpleJdbcTemplate template;
	private String SQL = "Select auction.date" + 
			",max(bid.ammount) as winning_amount" + 
			",count(bid.id) as bid_count" + 
			",bid.user" + 
			",name " + 
			"from auction " + 
			"left join bid " + 
			"on bid.auction = auction.id " + 
			"where closed = 1 " + 
			"and name = :auctionName " + 
			"and auction.date >= :startDate " + 
			"group by auction.id " + 
			",name " + 
			"having Count(bid.id) > 1 " + 
			"order by auction.date asc";
	
	public void init(){
		template = new SimpleJdbcTemplate(dataSource);
	}
	
	@SuppressWarnings("unchecked")
	public List<AuctionResult> getAuctionResults(final AuctionResultRequest request){
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("auctionName", request.getAuction().getName());
		parameters.put("startDate", new Timestamp(request.getInterval().getStartMillis()));
		return template.query(SQL, new RowMapper<AuctionResult>() {

			@Override
			public AuctionResult mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new AuctionResult(rs.getTimestamp(1), rs.getDouble(2), rs.getInt(3), rs.getString(4));
			}
			
		},parameters);
	}
	
}
