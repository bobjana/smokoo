package za.co.zynafin.smokoo.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.zkoss.lang.SystemException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import za.co.zynafin.smokoo.auction.parser.AuctionActivityEvent;
import za.co.zynafin.smokoo.util.ComponentListener;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the base controller for all zul-files that will extends <br>
 * from the window component.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 * 
 */
public abstract class BaseCtrl extends Window implements AfterCompose, Serializable {

	private static final long serialVersionUID = -2179229704315045689L;

	protected transient AnnotateDataBinder binder;
	protected transient Map<String, Object> args;

	public void doOnCreateCommon(Window w) {
		try {
			binder = new AnnotateDataBinder(w);
			binder.loadAll();
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	public void doOnCreateCommon(Window w, Event fe) throws Exception {
		doOnCreateCommon(w);
		CreateEvent ce = (CreateEvent) ((ForwardEvent) fe).getOrigin();
		args = (Map<String, Object>) ce.getArg();
	}

	/**
	 * default constructor.<br>
	 */
	public BaseCtrl() {
		super();
	}

	@Override
	public void afterCompose() {
		processRecursive(this, this);

		Components.wireVariables(this, this); // auto wire variables
		Components.addForwards(this, this); // auto forward
	}

	/*
	 * Are there inner window components than wire these too.
	 */
	private void processRecursive(Window main, Window child) {
		Components.wireVariables(main, child);
		Components.addForwards(main, this);
		List<Component> winList = (List<Component>) child.getChildren();
		for (Component window : winList) {
			if (window instanceof Window) {
				processRecursive(main, (Window) window);
			}
		}
	}

	protected West getContextMenuLayoutRegion() {
		Borderlayout bl = getBorderLayout();
		return bl.getWest();
	}

	protected Center getCenterLayoutRegion() {
		Borderlayout bl = getBorderLayout();
		return bl.getCenter();
	}

	protected Borderlayout getBorderLayout() {
		Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
		return bl;
	}

}
