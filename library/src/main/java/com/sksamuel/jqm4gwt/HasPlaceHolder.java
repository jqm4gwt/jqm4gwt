package com.sksamuel.jqm4gwt;

/**
 * @author jblok 29 Jun 2013
 * 
 *         This interface is implemented by widgets that have place holder text.
 */
public interface HasPlaceHolder<T> 
{
	String ATTRIBUTE_PLACEHOLDER = "placeholder";

	String getPlaceHolder();

   	void setPlaceHolder(String placeHolderText);
   	
    T withPlaceHolder(String placeHolderText);
}
