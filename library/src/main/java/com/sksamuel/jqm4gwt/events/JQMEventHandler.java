package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link JQMEvent} events.
 *
 * @author Ovidiu Buligan
 */
public interface JQMEventHandler extends EventHandler {

	/**
	 * Called when a {@link JQMEvent} is fired.
	 *
	 * @param event - the {@link JQMEvent} that was fired
	 */
	void onEvent(JQMEvent<?> event);
}
