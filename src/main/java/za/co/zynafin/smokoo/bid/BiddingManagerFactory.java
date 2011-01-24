package za.co.zynafin.smokoo.bid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BiddingManagerFactory implements ApplicationContextAware{

	private static Map<Long,BiddingManager> managers = new HashMap<Long, BiddingManager>();
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public boolean exists(long id){
		return managers.containsKey(id);
	}
	
	public BiddingManager getInstance(long id){
			if (!managers.containsKey(id)){
				BiddingManager manager = applicationContext.getBean(BiddingManager.class);
				managers.put(id, manager);
			}
			return managers.get(id);
	}
	
	public void destroy(long id){
		if (managers.containsKey(id)){
			managers.remove(id);
		}
	}
		
}
