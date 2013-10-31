package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.dom.client.DomEvent;

/**
 * Represents a JQM tap hold event.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public class TapHoldEvent extends TapBaseEvent<TapHoldHandler> {

	/**
	 * Event type for tap hold events. Represents the meta-data associated with this
	 * event.
	 */
	private static Type<TapHoldHandler> TYPE = null;

	/**
	 * Gets the event type associated with tap hold events.
	 * 
	 * @return the handler type
	 */
	public static Type<TapHoldHandler> getType() {
		if (TYPE == null) TYPE = new Type<TapHoldHandler>();
		return TYPE;
	}

	/**
	 * Protected constructor, use
	 * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers)}
	 * or
	 * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers, com.google.gwt.dom.client.Element)}
	 * to fire tap hold.
	 */
	protected TapHoldEvent() {
	}

	@Override
	public final Type<TapHoldHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TapHoldHandler handler) {
		handler.onTapHold(this);
	}

	/**
	 * Fires a {@link TapHoldEvent} on all registered handlers in the handler
	 * manager. If no such handlers exist, this method will do nothing.
	 * 
	 * @param source
	 *            the source of the handlers
	 */
	public static void fire(HasTapHoldHandlers source) {
		if (TYPE != null) {
			TapHoldEvent event = new TapHoldEvent();
			source.fireEvent(event);
		}
	}

}