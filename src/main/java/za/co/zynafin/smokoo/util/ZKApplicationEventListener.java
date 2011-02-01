package za.co.zynafin.smokoo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ZKApplicationEventListener implements ApplicationListener<ApplicationEvent> {

//	private JmsTemplate jmsTemplate;
//	
//	@Autowired
//	public void setJmsTemplate(JmsTemplate jmsTemplate) {
//		this.jmsTemplate = jmsTemplate;
//	}
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event == null) {
			return;
		}
		//todo: implements jms sender
//		if (event.getClass().getName().startsWith("za.co.zynafin.smokoo")) {
//			jmsTemplate.send(new MessageCreator() {
//
//				@Override
//				public Message createMessage(Session session) throws JMSException {
//					// TODO Auto-generated method stub
//					return null;
//				}
//				
//				
//			});
//			EventQueue eventQueue = null;
//			try {
//				eventQueue = EventQueues.lookup("spring-to-zk-eventqueue", EventQueues.APPLICATION, true);
//			} catch (IllegalStateException e) {
//				System.out.println(e.getMessage());
//				return;
//			}
//			eventQueue.publish(new Event("zkSpringEvent", null, event));
//		}
	}

}
