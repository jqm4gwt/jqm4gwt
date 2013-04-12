package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 22:36:48
 * 
 *         Interface for elements that accept a rel attribute for determing how a link is processed.
 * 
 */
public interface HasRel<T> {

	String getRel();


    /**
   	 * Set to dialog for a dialog page.
   	 */
    void setRel(String rel);

	/**
	 * Set to dialog for a dialog page.
	 */
    T withRel(String rel);

}
