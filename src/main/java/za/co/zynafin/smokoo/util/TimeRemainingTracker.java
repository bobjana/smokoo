package za.co.zynafin.smokoo.util;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TimeRemainingTracker {

	@Around(value="execution(Long za.co.zynafin.smokoo.auction.TimeRemainingCalculator+.calculate(..))")
	public Object track(ProceedingJoinPoint point) throws Throwable{
		StopWatch w = new StopWatch();
		w.start();
		Object value = point.proceed();
		w.stop();
		//TODO: Dump on queue.....
		return value;
	}
}
