package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a JQM tap hold event.
 *
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public class TapHoldEvent extends JQMEvent<TapHoldHandler> {

	/**
	 * Event type for tap hold events. Represents the meta-data associated with this event.
	 */
	private static Type<TapHoldHandler> TYPE = null;

	/**
	 * Gets the event handler type associated with tap hold events.
	 */
	public static Type<TapHoldHandler> getType() {
		if (TYPE == null) TYPE = new Type<TapHoldHandler>();
		return TYPE;
	}

	protected TapHoldEvent(JavaScriptObject jQueryEvent) {
		super(jQueryEvent, getType());
	}

	@Override
	protected void dispatch(TapHoldHandler handler) {
		handler.onTapHold(this);
	}
}
