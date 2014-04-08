package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author SlavaP
 *
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.2/filterable/">Filterable</a>
 * <p/> See <a href="http://api.jquerymobile.com/filterable/">Filterable API</a>
 *
 */
public class JQMFilterable extends SimplePanel {

    protected final TextBox filter;

    public JQMFilterable() {
        super(Document.get().createFormElement());
        getElement().addClassName("ui-filterable");
        filter = new TextBox();
        filter.getElement().setAttribute("data-type", "search");
        setFilterId(Document.get().createUniqueId());
        add(filter);
    }

    public String getFilterId() {
        return filter.getElement().getId();
    }

    public void setFilterId(String id) {
        filter.getElement().setId(id);
    }

}
