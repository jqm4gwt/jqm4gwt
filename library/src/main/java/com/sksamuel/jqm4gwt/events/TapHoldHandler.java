package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link TapHoldEvent} events.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public interface TapHoldHandler extends EventHandler {

	/**
	 * Called when a {@link TapHoldEvent} is fired.
	 * 
	 * @param event
	 *            the {@link TapHoldEvent} that was fired
	 */
	void onTapHold(TapHoldEvent event);

}