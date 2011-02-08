package za.co.zynafin.smokoo.util;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import za.co.zynafin.smokoo.NetworkLatency;
import za.co.zynafin.smokoo.io.ApplicationMessageSender;

@Aspect
public class NetworkLatencyTracker {

	@Autowired
	private ApplicationMessageSender applicationMessageSender;
	
	@Around(value="execution(Long za.co.zynafin.smokoo.auction.AuctionService+.getTimeRemaining(..))")
	public Object track(ProceedingJoinPoint point) throws Throwable{
		StopWatch w = new StopWatch();
		w.start();
		Object value = point.proceed();
		w.stop();
System.err.println(w.getTime());
//		applicationMessageSender.sendMessage(new NetworkLatency(w.getTime()));
		return value;
	}
}
