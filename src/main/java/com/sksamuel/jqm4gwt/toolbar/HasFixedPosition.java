package com.sksamuel.jqm4gwt.toolbar;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 06:55:34
 * 
 *         Interface for widgets that can be fixed in place.
 * 
 */
public interface HasFixedPosition {

	/**
	 * Returns true if this widget is set fixed position
	 */
	boolean isFixed();

	/**
	 * Set the positioning type of this widget
	 */
	void setFixed(boolean fixed);

}
