package za.co.zynafin.smokoo.io;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMessageSender {

	private JmsTemplate jmsTemplate;

	@Autowired
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void sendMessage(final Serializable message){
		
		jmsTemplate.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				System.err.println("before message create...");
				Message msg = session.createObjectMessage(message);
				System.err.println("after message create...");
				return msg;
			}
			
		});
	}
	
}
