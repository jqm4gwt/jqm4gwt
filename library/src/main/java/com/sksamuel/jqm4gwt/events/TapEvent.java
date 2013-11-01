package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.DomEvent;

/**
 * Represents a JQM tap event.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public class TapEvent extends JQueryBaseEvent<TapHandler> {

	/**
	 * Event type for tap events. Represents the meta-data associated with this
	 * event.
	 */
	private static Type<TapHandler> TYPE = null;

	/**
	 * Gets the event type associated with tap events.
	 * 
	 * @return the handler type
	 */
	public static Type<TapHandler> getType() {
		if (TYPE == null) TYPE = new Type<TapHandler>();
		return TYPE;
	}

	/**
	 * Protected constructor, use
	 * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers)}
	 * or
	 * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers, com.google.gwt.dom.client.Element)}
	 * to fire tap.
	 * @param jQueryEvent 
	 */
	protected TapEvent(JavaScriptObject jQueryEvent) {
		super(jQueryEvent);
	}

	@Override
	public final Type<TapHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TapHandler handler) {
		handler.onTap(this);
	}

	/**
	 * Fires a {@link TapEvent} on all registered handlers in the handler
	 * manager. If no such handlers exist, this method will do nothing.
	 * 
	 * @param source
	 *            the source of the handlers
	 * @param jQueryEvent 
	 */
	public static void fire(HasTapHandlers source, JavaScriptObject jQueryEvent) {
		if (TYPE != null) {
			TapEvent event = new TapEvent(jQueryEvent);
			source.fireEvent(event);
		}
	}

}