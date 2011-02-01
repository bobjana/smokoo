package za.co.zynafin.smokoo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventPublisher implements ApplicationContextAware{

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationEventPublisher.applicationContext = applicationContext;
	}
	
	public static void publishEvent(ApplicationEvent applicationEvent){
		ApplicationEventPublisher.applicationContext.publishEvent(applicationEvent);
	}
	
}
