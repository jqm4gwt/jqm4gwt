package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link TapEvent} events.<br><br>
 *
 * When this event's handler will make a transition to the next page, please make sure to use
 * {@link JQueryBaseEvent#getJQueryEvent()} and {@link WrappedJQueryEvent#stopImmediatePropagation()}
 * and {@link WrappedJQueryEvent#preventDefault()}.
 * Otherwise you will risk an unwanted tap on the widget at that same location on the next page
 * on some browsers/devices.<br><br>
 *
 * You can also use {@link TapHandlerForPageSwitch} for page transitions.
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