package za.co.zynafin.smokoo;

public interface Constants {

	String DEFAULT_URL = "http://www.smokoo.co.za";
	String CLOSED_AUCTIONS_URL = DEFAULT_URL + "/closed_auctions.php";
	String ACTIVE_AUCTIONS_URL = DEFAULT_URL;
	String AUCTION_TIME_REMAING_URL = DEFAULT_URL + "/ajax_get_auction_timer_data.php";
	String AUCTION_BID_HISTORY_URL = DEFAULT_URL + "/ajax_get_auction_bids.php?title=";
	String AUCTION_PLACE_A_BID_URL = DEFAULT_URL + "/make_bid.php";

	String LOGIN_URL = DEFAULT_URL + "/login.php";
	String LOGOUT_URL = DEFAULT_URL + "/logout.php";

}
