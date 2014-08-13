package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A widget that implements this interface provides registration for {@link JQMEventHandler} instances.
 * These are generic jquery events they can be any custom jquery event.
 *
 * @author Ovidiu Buligan
 */
public interface HasJQMEventHandlers extends HasJQMHandlers {
	/**
	 * Adds a {@link JQMEvent} handler.
	 *
	 * @param handler - the JQM generic event handler
	 *
	 * @return {@link HandlerRegistration} used to remove this handler.
	 */
	HandlerRegistration addJQMEventHandler(String jqmEventName, EventHandler handler);
}