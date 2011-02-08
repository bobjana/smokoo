package za.co.zynafin.smokoo.util;

import java.util.ArrayList;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationEventTranslator implements ApplicationListener<ApplicationEvent> {

	private java.util.List<ComponentListener> componentListeners = new ArrayList<ComponentListener>();
	
	public void addComponentListener(ComponentListener componentListener){
		if (!componentListeners.contains(componentListener)){
			componentListeners.add(componentListener);
		}
	}
	
	public void removeComponentListener(ComponentListener componentListener){
		componentListeners.remove(componentListener);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event == null) {
			return;
		}
		if (!event.getClass().getPackage().getName().startsWith("za.co.zynafin.smokoo")){
			return;
		}
		for (ComponentListener listener : componentListeners){
			listener.onEvent(event);
		}
		
	}

}