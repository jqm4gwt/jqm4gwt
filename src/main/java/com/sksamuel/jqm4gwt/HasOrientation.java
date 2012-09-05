package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 22:07:28
 * 
 *         Interface for elements that are able to be rendered either
 *         horizontally or vertically.
 * 
 */
public interface HasOrientation {

	/**
	 * Returns true if this widget is currently set to be rendered in
	 * horizontal mode
	 */
	boolean isHorizontal();

	/**
	 * Returns true if this widget is currently set to be rendered in vertical
	 * mode
	 */
	boolean isVertical();

	/**
	 * Set this widget to be rendered horizontally. If already set to
	 * horizontal then this has no effect.
	 */
	void setHorizontal();

	/**
	 * Set this widget to be rendered vertically. If already set to vertical
	 * then this has no effect.
	 */
	void setVertical();
}
