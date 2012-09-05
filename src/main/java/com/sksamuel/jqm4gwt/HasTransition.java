package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 00:58:28
 * 
 *         Interface for elements that accept a {@link Transition} parameter.
 */
public interface HasTransition {

	/**
	 * Returns the currently set {@link Transition} or null if no transition
	 * is set.
	 */
	Transition getTransition();

	/**
	 * Sets the {@link Transition} replacing any existing value.
	 */
	void setTransition(Transition transition);
}
