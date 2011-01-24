package za.co.zynafin.smokoo.auction;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
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
		StopWatch w = new StopWatch();
		w.start();
		String url = Constants.AUCTION_TIME_REMAING_URL + String.format("?auction_ids=%s&unique_string=%s",Long.toString(auctionId),
				randomizer.randomize());
		String content = smokooConnector.get(url);
		if (StringUtils.isEmpty(content)){
			return 0;
		}
		long remaining = timeRemainingParser.parse(content, auctionId);
		w.stop();
		//TODO: post to queue for further analysis
//System.out.println("Calc duration: " + duration);
		long result = remaining - w.getTime();
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
