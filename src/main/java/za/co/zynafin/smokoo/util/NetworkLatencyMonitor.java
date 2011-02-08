package za.co.zynafin.smokoo.util;

import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.NetworkLatency;

@Component
public class NetworkLatencyMonitor {

	public void onNetworkLatency(NetworkLatency networkLatency){
		networkLatency.persist();
	}
	
}
