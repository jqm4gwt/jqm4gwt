package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;

/**
 * <p/> Filterable functionality support.
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/filterable/">Filterable</a>
 * <p/> See <a href="http://api.jquerymobile.com/filterable/">Filterable API</a>
 *
 * @author SlavaP
 *
 */
public interface HasFilterable {
    void refreshFilter();
    void doBeforeFilter(String filter);

    /**
     * @return - must return true if the element is to be filtered,
     * and it must return false if the element is to be shown.
     * null - means default filtering should be used.
     */
    Boolean doFiltering(Element elt, Integer index, String searchValue);
}
