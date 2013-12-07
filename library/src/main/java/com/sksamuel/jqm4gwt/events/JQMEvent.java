package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.DomEvent;

/**
 * Represents a JQM event (ex: tap, vclick etc)
 * 
 * @author Ovidiu Buligan
 */
public class JQMEvent extends JQueryBaseEvent<JQMEventHandler> {

	/**
	 * Event type for tap events. Represents the meta-data associated with this
	 * event.
	 */
	private static Type<JQMEventHandler> TYPE = null;

	/**
	 * Gets the event type associated with tap events.
	 * 
	 * @return the handler type
	 */
	public static Type<JQMEventHandler> getType() {
		if (TYPE == null) TYPE = new Type<JQMEventHandler>();
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
	protected JQMEvent(JavaScriptObject jQueryEvent) {
		super(jQueryEvent);
	}

	@Override
	public final Type<JQMEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(JQMEventHandler handler) {
		handler.onEvent(this);
	}

	/**
	 * Fires a {@link JQMEvent} on all registered handlers in the handler
	 * manager. If no such handlers exist, this method will do nothing.
	 * 
	 * @param source
	 *            the source of the handlers
	 * @param jQueryEvent 
	 */
	public static void fire(HasJQMEventHandlers source, JavaScriptObject jQueryEvent) {
		if (TYPE != null) {
			JQMEvent event = new JQMEvent(jQueryEvent);
			source.fireEvent(event);
		}
	}

}