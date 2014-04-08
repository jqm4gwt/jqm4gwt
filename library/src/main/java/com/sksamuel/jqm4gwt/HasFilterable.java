package com.sksamuel.jqm4gwt;

/**
 * <p/> Filterable functionality support.
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.2/filterable/">Filterable</a>
 * <p/> See <a href="http://api.jquerymobile.com/filterable/">Filterable API</a>
 *
 * @author SlavaP
 *
 */
public interface HasFilterable {
    void refreshFilter();
    void doBeforeFilter(String filter);
}
