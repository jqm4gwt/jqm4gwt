package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 22:22:08
 * 
 *         This interface is implemented by widgets that can be rendered inline.
 * 
 */
public interface HasInline {

	/**
	 * Returns true if this widget is currently rendering inline
	 */
	boolean isInline();

	/**
	 * Sets whether this widget should render inline.
	 * 
	 * @param inline
	 *              if true then this set widget will render inline or if
	 *              false then it will expand to full
	 */
	void setInline(boolean inline);

}
