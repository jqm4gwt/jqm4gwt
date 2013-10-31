package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A widget that implements this interface provides registration for
 * {@link TapHandler} instances.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public interface HasTapHandlers extends HasJQMHandlers {
	/**
	 * Adds a {@link TapEvent} handler.
	 * 
	 * @param handler
	 *            the tap handler
	 * @return {@link HandlerRegistration} used to remove this handler.
	 */
	HandlerRegistration addTapHandler(TapHandler handler);
}