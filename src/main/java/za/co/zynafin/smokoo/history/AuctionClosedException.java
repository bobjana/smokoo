package za.co.zynafin.smokoo.history;

public class AuctionClosedException extends Exception{

	public AuctionClosedException() {
		super();
	}

	public AuctionClosedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuctionClosedException(String message) {
		super(message);
	}

	public AuctionClosedException(Throwable cause) {
		super(cause);
	}

	
}
