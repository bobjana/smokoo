package za.co.zynafin.smokoo.util;

import org.springframework.context.ApplicationEvent;

public interface ComponentListener {

	void onEvent(ApplicationEvent event);
}
