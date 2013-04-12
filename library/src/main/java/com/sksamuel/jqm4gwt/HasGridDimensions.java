package com.sksamuel.jqm4gwt;

import com.sksamuel.jqm4gwt.form.elements.JQMTextArea;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 May 2011 13:54:02
 *         <p/>
 *         This interface is for widgets that have two dimensions, eg a
 *         {@link JQMTextArea}
 */
public interface HasGridDimensions<T> {

    /**
     * Returns the number of columns
     */
    int getColumns();

    /**
     * Returns the number of rows
     */
    int getRows();

    T setColumns(int columns);

    T setRows(int rows);
}
