package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a JQM tap event.
 *
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public class TapEvent extends JQMEvent<TapHandler> {

	/**
	 * Event type for tap events. Represents the meta-data associated with this event.
	 */
	private static Type<TapHandler> TYPE = null;

	/**
	 * Gets the event handler type associated with tap events.
	 */
	public static Type<TapHandler> getType() {
		if (TYPE == null) TYPE = new Type<TapHandler>();
		return TYPE;
	}

	protected TapEvent(JavaScriptObject jQueryEvent) {
		super(jQueryEvent, getType());
	}

	@Override
	protected void dispatch(TapHandler handler) {
		handler.onTap(this);
	}
}
