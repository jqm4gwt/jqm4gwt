package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link TapEvent} events.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public interface TapHandler extends EventHandler {

	/**
	 * Called when a {@link TapEvent} is fired.
	 * 
	 * @param event
	 *            the {@link TapEvent} that was fired
	 */
	void onTap(TapEvent event);

}