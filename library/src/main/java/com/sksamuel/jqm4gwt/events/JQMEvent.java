package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Represents a JQM event (e.g.: tap, vclick, ...)
 *
 * @author Ovidiu Buligan
 */
public class JQMEvent<T extends EventHandler> extends JQueryBaseEvent<T> {

	/** Event handler type for this event. */
	private final Type<T> handlerType;

	protected JQMEvent(JavaScriptObject jQueryEvent, Type<T> handlerType) {
		super(jQueryEvent);
		this.handlerType = handlerType;
	}

	@Override
	public final Type<T> getAssociatedType() {
		return handlerType;
	}

	@Override
	protected void dispatch(T handler) {
		if (handler instanceof JQMEventHandler) {
		    ((JQMEventHandler) handler).onEvent(this);
		}
	}

	/**
     * Fires a {@link JQMEvent} on all registered handlers in the handler
     * manager. If no such handlers exist, this method will do nothing.
     *
     * @param source - the source of the handlers
     */
	public static void fire(HasHandlers source, String jqmEventName,
	                        JavaScriptObject jQueryEvent) {

	    JQMEvent<?> event = JQMEventFactory.createEvent(jqmEventName, jQueryEvent);
	    source.fireEvent(event);
	}

}