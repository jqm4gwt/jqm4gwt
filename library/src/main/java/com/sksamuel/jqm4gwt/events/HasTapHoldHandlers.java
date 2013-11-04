package com.sksamuel.jqm4gwt.events;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A widget that implements this interface provides registration for
 * {@link TapHoldHandler} instances.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public interface HasTapHoldHandlers extends HasJQMHandlers {
	/**
	 * Adds a {@link TapHoldEvent} handler.
	 * 
	 * @param handler
	 *            the tap hold handler
	 * @return {@link HandlerRegistration} used to remove this handler.
	 */
	HandlerRegistration addTapHandler(TapHoldHandler handler);
}