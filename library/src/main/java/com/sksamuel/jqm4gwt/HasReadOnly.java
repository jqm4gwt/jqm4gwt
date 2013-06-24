package com.sksamuel.jqm4gwt;

/**
 * @author jblok 23 Jun 2013
 * 
 *         This interface is implemented by widgets that have the readOnly property.
 */
public interface HasReadOnly {

	/**
	 * Returns true if this widget is currently readOnly
	 */
	boolean isReadOnly();

    /**
   	 * Sets whether this widget should be readOnly.
   	 *
   	 * @param readOnly
   	 */
   	void setReadOnly(boolean readOnly);
}
