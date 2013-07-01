package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMWidget;

/**
 * @author Gabi Boros gabi.boros@gmail.com 7 June 2013 15:24:13
 * 
 *         This class models a Jquery Mobile panel element.
 * 
 * @link 
 *       http://jquerymobile.com/demos/1.3.0/docs/widgets/panels/
 * 
 */
public class JQMPanel extends JQMWidget {

	public static final String DISPLAY_REVEAL = "reveal";
	public static final String DISPLAY_PUSH = "push";
	public static final String DISPLAY_OVERLAY = "overlay";

	public static final String POSITION_LEFT = "left";
	public static final String POSITION_RIGHT = "right";

	private FlowPanel flowPanel;
	
	public JQMPanel()
	{
		this(Document.get().createUniqueId());
	}

	public JQMPanel(String id) {
		flowPanel = new FlowPanel();
		initWidget(flowPanel);
		setId(id);
		setDataRole("panel");
	}
	
	public void add(Widget w) {
		flowPanel.add(w);
	}

	public void remove(Widget w) {
		flowPanel.remove(w);
	}
	
	public Widget[] getWidgets() {
		Widget[] widgets = new Widget[flowPanel.getWidgetCount()];
		for (int k = 0; k < flowPanel.getWidgetCount(); k++) {
			widgets[k] = flowPanel.getWidget(k);
		}
		return widgets;
	}
	
	public void setAnimate(boolean animate) {
		setAttribute("data-animate", String.valueOf(animate));
	}
	
	public boolean isAnimate() {
		return "true".equals(getAttribute("data-animate"));
	}
	
	public void setDisplay(String display) {
		setAttribute("data-display", display);
	}
	
	public String getDisplay() {
		return getAttribute("data-display");
	}
	
	public void setPosition(String position) {
		setAttribute("data-position", position);
	}
	
	public String getPosition() {
		return getAttribute("data-position");
	}	
	
	public void setPositionFixed(boolean positionFixed) {
		setAttribute("data-position-fixed", String.valueOf(positionFixed));
	}
	
	public boolean isPositionFixed() {
		return "true".equals(getAttribute("data-position-fixed"));
	}
	
	public void setSwipeClose(boolean swipeClose) {
		setAttribute("data-swipe-close", String.valueOf(swipeClose));
	}
	
	public boolean isSwipeClose() {
		return "true".equals(getAttribute("data-swipe-close"));
	}	
	
	public void setDismissible(boolean dismissible) {
		setAttribute("data-dismissible", String.valueOf(dismissible));
	}
	
	public boolean isDismissible() {
		return "true".equals(getAttribute("data-dismissible"));
	}	
	
	public void open() {
		_open(getId());
	}
	
	public void close() {
		_close(getId());
	}
	
	public void toggle() {
		_toggle(getId());
	}

	protected void onPanelBeforeClose() {		
	}
	
	protected void onPanelBeforeOpen() {		
	}
	
	protected void onPanelClose() {		
	}
	
	protected void onPanelCreate() {		
	}
	
	protected void onPanelOpen() {		
	}

    public void bindLifecycleEvents() {
        bindLifecycleEvents(this, getId());
    }
    
    public void unbindLifecycleEvents() {
    	unbindLifecycleEvents(getId());
    }	
	
    private native void bindLifecycleEvents(JQMPanel p, String id) /*-{
	$wnd.$('#' + id).bind("panelbeforeclose",
	function(event, ui) {
	p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::onPanelBeforeClose()();
	});
	
	$wnd.$('#' + id).bind("panelbeforeopen",
	function(event, ui) {
	p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::onPanelBeforeOpen()();
	});
	
	$wnd.$('#' + id).bind("panelclose",
	function(event, ui) {
	p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::onPanelClose()();
	});
	
	$wnd.$('#' + id).bind("panelcreate",
	function(event, ui) {
	p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::onPanelCreate()();
	});
	
	$wnd.$('#' + id).bind("panelopen",
	function(event, ui) {
	p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::onPanelOpen()();
	});	
	
	}-*/;

    private native void unbindLifecycleEvents(String id) /*-{
	$wnd.$('#' + id).unbind("panelbeforeclose");	
	$wnd.$('#' + id).unbind("panelbeforeopen");
	$wnd.$('#' + id).unbind("panelclose");
	$wnd.$('#' + id).unbind("panelcreate");
	$wnd.$('#' + id).unbind("panelopen");
	}-*/;
	
	
	private native void _open(String id) /*-{
    $wnd.$('#' + id).panel("open")
							}-*/;

	private native void _close(String id) /*-{
    $wnd.$('#' + id).panel("close")
							}-*/;	
	
	private native void _toggle(String id) /*-{
    $wnd.$('#' + id).panel("toggle")
							}-*/;		
}
