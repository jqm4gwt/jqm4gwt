package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 22:05:22
 * 
 *         Widgets implementing this interface had a user definable id
 * 
 */
public interface HasId {

	/**
	 * Returns the currently set ID
	 */
	String getId();

	/**
	 * Change the ID to the given value
	 */
	void setId(String id);
}
