package za.co.zynafin.smokoo.auction;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.parser.TimeRemainingParser;
import za.co.zynafin.smokoo.io.SmokooConnector;

@Component
public class TimeRemainingCalculator {

	private SmokooConnector smokooConnector;
	private TimeRemainingParser timeRemainingParser;
	private Randomizer randomizer;
	
	public TimeRemainingCalculator() {
		super();
		randomizer = new MathRandomizer();
	}

	//Testing purposes only
	public TimeRemainingCalculator(Randomizer randomizer) {
		super();
		this.randomizer = randomizer;
	}

	@Autowired
	public void setSmokooConnector(SmokooConnector connector) {
		this.smokooConnector = connector;
	}
	
	@Autowired
	public void setTimeRemainingParser(TimeRemainingParser timeRemainingParser) {
		this.timeRemainingParser = timeRemainingParser;
	}

	public long calculate(long auctionId){
		long startTime = new Date().getTime();
		String url = Constants.AUCTION_TIME_REMAING_URL + String.format("?auction_ids=%s&unique_string=%s",Long.toString(auctionId),
				randomizer.randomize());
		String content = smokooConnector.get(url);
		long remaining = timeRemainingParser.parse(content, auctionId);
		long endTime = new Date().getTime();
		long duration = endTime - startTime;
System.out.println("Calc duration: " + duration);
		long result = remaining - duration;
		if (result <= 0){
			return 0;
		}
		return result;
	}

	public interface Randomizer{
		
		public double randomize();
	}
	
	private class MathRandomizer implements Randomizer{

		@Override
		public double randomize() {
			return Math.random();
		}
		
	}

}
