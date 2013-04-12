package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 6 Sep 2012 00:04:52
 *
 */
public interface HasIconPos<T> {

	/**
	 * Returns the current position of the icon
	 * 
	 * 
	 * @return the IconPos enum representing the location of the icon
	 */
	IconPos getIconPos();

	/**
	 * Sets the position of the icon
	 */
	void setIconPos(IconPos pos);

    /**
   	 * Sets the position of the icon
   	 */
   	T withIconPos(IconPos pos);
}
