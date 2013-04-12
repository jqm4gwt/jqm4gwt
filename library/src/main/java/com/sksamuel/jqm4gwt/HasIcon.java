package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 13 Jul 2011 23:08:58
 * 
 */
public interface HasIcon<T> extends HasIconPos<T> {

	/**
	 * Removes any icon set on the implementing class. If no icon has been set
	 * then this has no effect.
	 */
	T removeIcon();

	/**
	 * Sets the data icon to use
	 * 
	 * @param icon
	 *              of the standard built in icon types
	 */
	T setIcon(DataIcon icon);

	/**
	 * Sets the icon to be a custom URL.
	 * 
	 * @param src
	 *              the src of the custom icon
	 */
	T setIcon(String src);

}
