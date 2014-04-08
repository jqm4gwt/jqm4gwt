package com.sksamuel.jqm4gwt.list;

/**
 * @author Stephen K Samuel samspade79@gmail.com 25 Jul 2011 01:03:13
 *
 */
public interface HasFilter<T> {

	boolean isFilterable();

    void setFilterable(boolean filterable);

	T withFilterable(boolean filterable);

}
