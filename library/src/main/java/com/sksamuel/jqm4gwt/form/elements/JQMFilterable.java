package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.HasPlaceHolder;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.JQMCommon;

/**
 * @author SlavaP
 *
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/filterable/">Filterable</a>
 * <p/> See <a href="http://api.jquerymobile.com/filterable/">Filterable API</a>
 *
 */
public class JQMFilterable extends SimplePanel implements HasPlaceHolder<JQMFilterable>,
        HasTheme<JQMFilterable> {

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

    @Override
    public String getPlaceHolder() {
        return filter.getElement().getAttribute(HasPlaceHolder.ATTRIBUTE_PLACEHOLDER);
    }

    @Override
    public void setPlaceHolder(String placeHolderText) {
        filter.getElement().setAttribute(HasPlaceHolder.ATTRIBUTE_PLACEHOLDER, placeHolderText);
    }

    @Override
    public JQMFilterable withPlaceHolder(String placeHolderText) {
        setPlaceHolder(placeHolderText);
        return this;
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(filter);
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.applyTheme(filter, themeName);
    }

    @Override
    public JQMFilterable withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

}
