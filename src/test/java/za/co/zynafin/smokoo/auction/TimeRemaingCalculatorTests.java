package za.co.zynafin.smokoo.auction;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import za.co.zynafin.smokoo.Constants;
import za.co.zynafin.smokoo.auction.TimeRemainingCalculator.Randomizer;
import za.co.zynafin.smokoo.auction.parser.TimeRemainingParser;
import za.co.zynafin.smokoo.io.SmokooConnector;


public class TimeRemaingCalculatorTests {
	
	private Randomizer randomizer;
	private TimeRemainingCalculator calculator;
	private TimeRemainingParser timeRemainingParser;
	private SmokooConnector connector;

	@Before
	public void setup(){
		randomizer = Mockito.mock(Randomizer.class);
		Mockito.when(randomizer.randomize()).thenReturn(0.123d);
		
		calculator = new TimeRemainingCalculator(randomizer);
		connector = Mockito.mock(SmokooConnector.class);
		Mockito.when(connector.get(Constants.AUCTION_TIME_REMAING_URL + "?auction_ids=1&unique_string=0.123")).thenReturn("piped|content");
		calculator.setSmokooConnector(connector);
		
		timeRemainingParser = Mockito.mock(TimeRemainingParser.class);
		calculator.setTimeRemainingParser(timeRemainingParser );
	}
	
	@Test
	public void calculate() throws Exception {
		//GIVEN
		Mockito.when(timeRemainingParser.parse("piped|content", 1l)).thenReturn(3000l);
		//WHEN
		Date startDate = new Date();
		long result = calculator.calculate(1l);
		Date endDate = new Date();
		long processPeriod = endDate.getTime() - startDate.getTime();
		long estimatedTime = 3000l - processPeriod;
		//THEN
		assertEquals(estimatedTime,result);
	}
	
	@Test
	public void calculate_TimeUp() throws Exception {
		Mockito.when(timeRemainingParser.parse("piped|content", 1l)).thenReturn(-1l);
		//WHEN
		long result = calculator.calculate(1l);
		//THEN
		assertEquals(0l,result);
	}
}
