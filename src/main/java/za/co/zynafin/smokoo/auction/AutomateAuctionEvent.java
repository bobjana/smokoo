package za.co.zynafin.smokoo.auction;

import org.springframework.context.ApplicationEvent;

public class AutomateAuctionEvent extends ApplicationEvent {

	public AutomateAuctionEvent(Object source) {
		super(source);
	}

}
