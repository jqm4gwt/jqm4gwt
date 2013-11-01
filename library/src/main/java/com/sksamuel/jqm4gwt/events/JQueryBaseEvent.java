package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * A wrapper around a JS jQuery event.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public abstract class JQueryBaseEvent<H extends EventHandler> extends GwtEvent<H> {
	
	private WrappedJQueryEvent jQueryEvent;

	public JQueryBaseEvent(JavaScriptObject jQueryEvent) {
		this.jQueryEvent = new WrappedJQueryEvent(jQueryEvent);
	}

	/**
	 * Returns the actual JQuery event.
	 * @return the underlying jQuery event.
	 */
	public WrappedJQueryEvent getJQueryEvent() {
		return jQueryEvent;
	}

}