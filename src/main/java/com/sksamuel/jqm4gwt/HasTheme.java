package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 10:36:07
 * 
 *         Interface for elements that are themeable.
 * 
 * @link http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/api/themes.html
 * 
 */
public interface HasTheme {

	/**
	 * Returns the value of the data-theme attribute
	 */
	String getTheme();

	/**
	 * Sets the value of the data-theme attribute. Should be a value definined
	 * by the accompanying CSS stylesheet.
	 * 
	 * JQM by default defines styles A-E. User styles will typically be
	 * defined as F onwards.
	 * 
	 */
	void setTheme(String themeName);
}
