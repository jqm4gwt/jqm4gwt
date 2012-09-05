package com.sksamuel.jqm4gwt;

import com.sksamuel.jqm4gwt.form.elements.JQMTextArea;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 May 2011 13:54:02
 * 
 *         This interface is for widgets that have two dimensions, eg a
 *         {@link JQMTextArea}
 * 
 */
public interface HasGridDimensions {

	/**
	 * Returns the number of columns
	 */
	int getColumns();

	/**
	 * Returns the number of rows
	 */
	int getRows();

	void setColumns(int columns);

	void setRows(int rows);
}
