package za.co.zynafin.smokoo;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class NetworkLatency implements Serializable{
	
	public NetworkLatency(){}
	
	public NetworkLatency(long delay) {
		super();
		this.time = new DateTime();
		this.delay = delay;
	}


	private DateTime time;
	private long delay;
}
