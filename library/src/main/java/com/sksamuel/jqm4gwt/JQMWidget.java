package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.events.HasJQMEventHandlers;
import com.sksamuel.jqm4gwt.events.JQMEventFactory;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterable;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterableEvent;
import com.sksamuel.jqm4gwt.form.elements.JQMSelect;
import com.sksamuel.jqm4gwt.layout.JQMCollapsibleSet;
import com.sksamuel.jqm4gwt.list.JQMList;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 17:02:40
 *         <p/>
 *         An extension of the standard GWT {@link Widget} that adds
 *         functionality common to all JQM elements, as well as convenience
 *         methods used by subclasses.
 *         <p/>
 *         The {@link JQMWidget} is an extension of composite because
 *         {@link JQMWidget}s do not typically add new functionality (in terms
 *         of new elements), they are mostly compositions of existing HTML
 *         elements.
 *         <p/>
 *         This abstract superclass does not define the nature of the
 *         composition in use. Implementating subclasses must decide how to
 *         compose and thus call initWidget() themselves.
 */
public abstract class JQMWidget extends Composite implements HasTheme<JQMWidget>, HasId<JQMWidget>,
        HasDataRole, HasEnabled, HasJQMEventHandlers, HasFilterable {

    private boolean boundFilterEvents;
    private boolean boundFilterCallback;
    private JavaScriptObject origFilter;

    /**
     * Returns the value of the attribute with the given name
     */
    public String getAttribute(String name) {
        return JQMCommon.getAttribute(this, name);
    }

    public boolean getAttributeBoolean(String name) {
        return "true".equalsIgnoreCase(getAttribute(name));
    }

    @Override
    public String getDataRole() {
        return JQMCommon.getDataRole(this);
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(this);
    }

    /**
     * Removes the attribute with the given name
     *
     * @param name the name of the attribute to remove
     */
    protected void removeAttribute(String name) {
        getElement().removeAttribute(name);
    }

    public void removeDataRole() {
        JQMCommon.removeDataRole(this);
    }

    /**
     * Sets the value of the attribute with the given name to the given value.
     */
    public void setAttribute(String name, String value) {
        JQMCommon.setAttribute(this, name, value);
    }

    /**
     * Sets the data-role attribute to the given value.
     *
     * @param value the value to set the data-role attribute to
     */
    @Override
    public void setDataRole(String value) {
        JQMCommon.setDataRole(this, value);
    }

    /** Returns this widget's ID (set on the main element) */
    @Override
    public final String getId() {
        return getElement().getId();
    }

    /** The same as {@link JQMWidget#getId()}, but needed for UiBinder templates */
    public final String getWidgetId() {
        return getId();
    }

    /**
     * Assigns an automatically generated ID to this widget. This method uses
     * the default GWT id creation methods in Document.get().createUniqueId()
     */
    protected final void setId() {
        setId(Document.get().createUniqueId());
    }

    @Override
    public final void setId(String id) {
        getElement().setId(id);
    }

    /**
     * The same as {@link JQMWidget#setId(String)}, but needed for UiBinder templates
     * where setId() cannot be used, because id="xxx" has different predefined meaning.
     */
    public final void setWidgetId(String id) {
        setId(id);
    }

    @Override
    public final JQMWidget withId(String id) {
        setId(id);
        return this;
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setTheme(this, themeName);
    }

    @Override
    public JQMWidget withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    @Override
    public void setEnabled(boolean b) {
        JQMCommon.setEnabled(this, b);
    }

    @Override
    public boolean isEnabled() {
        return JQMCommon.isEnabled(this);
    }

    /**
     * Gives realistic visibility (parent chain considered, ...)
     * If you need logical visibility of this particular widget,
     * use {@link UIObject#isVisible(Element elem)}
     */
    @Override
    public boolean isVisible() {
        return super.isVisible() && JQMCommon.isVisible(this);
    }

    @Override
    public HandlerRegistration addJQMEventHandler(String jqmEventName, EventHandler handler) {

        Type<EventHandler> t = JQMEventFactory.getType(jqmEventName, EventHandler.class);

        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, jqmEventName, t);
    }

    public String getFilterText() {
        return JQMCommon.getFilterText(getDataFilterWidget());
    }

    /**
     * {@link JQMFilterable} will use this text when searching through this widget.
     * <p/><b>Detail description:</b> By default, the filter simply searches against
     * the contents of each list item.
     * If you want the filter to search against different content, add the data-filtertext
     * attribute to the item and populate it with one or many keywords and phrases that
     * should be used to match against. Note that if this attribute is added,
     * the contents of the list item are ignored.
     * <p> This attribute is useful for dealing with allowing for ticker symbols
     * and full company names to be searched, or for covering common spellings
     * and abbreviations for countries.
     */
    public void setFilterText(String filterText) {
        JQMCommon.setFilterText(getDataFilterWidget(), filterText);
    }

    /** Can be overridden in descendants to provide proper data filter widget */
    protected Widget getDataFilterWidget() {
        return this;
    }

    /** @return true if this list is set to filterable, false otherwise. */
    public boolean isFilterable() {
        return JQMCommon.isFilterable(getDataFilterWidget());
    }

    public void setFilterable(boolean value) {
        JQMCommon.setFilterable(getDataFilterWidget(), value);
        checkFilterEvents();
    }

    public String getDataFilter() {
        return JQMCommon.getDataFilter(getDataFilterWidget());
    }

    /**
     * To be used in conjunction with {@link JQMFilterable}.
     * <p/> May not work for any widget or require {@link JQMWidget#getDataFilterWidget()} override
     * for composite widgets like {@link JQMSelect}.
     * <p/> But {@link JQMList}, {@link JQMCollapsibleSet}, and others with children collection are supported.
     *
     * @param filterSelector - a jQuery selector that will be used to retrieve the element
     * that will serve as the input source, UiBinder example: dataFilter="#{fltr1.getFilterId}"
     */
    public void setDataFilter(String filterSelector) {
        JQMCommon.setDataFilter(getDataFilterWidget(), filterSelector);
        checkFilterEvents();
    }

    /**
     * @return - associated JQMFilterable.filter.getElement()
     */
    protected Element getFilterSearchElt() {
        String s = getDataFilter();
        if (s == null || s.isEmpty()) return null;
        return findElt(s);
    }

    protected static native Element findElt(String selector) /*-{
        return $wnd.$( selector ).first()[0];
    }-*/;

    public String getFilterSearchText() {
        Element elt = getFilterSearchElt();
        if (elt == null) return null;
        return JQMCommon.getAttribute(elt, "data-lastval");
    }

    /**
     * Complimentary to setDataFilter(). Sets filter search text of associated JQMFilterable widget.
     */
    public void setFilterSearchText(String value) {
        Element elt = getFilterSearchElt();
        if (elt == null) return;
        refreshFilterSearch(elt, value);
    }

    protected static native void refreshFilterSearch(Element elt, String text) /*-{
        $wnd.$(elt).val(text).attr('data-lastval', '').trigger('change');
    }-*/;

    public String getFilterChildren() {
        return JQMCommon.getFilterChildren(getDataFilterWidget());
    }

    /**
     * See <a href="http://api.jquerymobile.com/filterable/#option-children">Filterable Children</a>
     */
    public void setFilterChildren(String filterChildren) {
        JQMCommon.setFilterChildren(getDataFilterWidget(), filterChildren);
    }

    public boolean isFilterReveal() {
        return JQMCommon.isFilterReveal(getDataFilterWidget());
    }

    public void setFilterReveal(boolean value) {
        JQMCommon.setFilterReveal(getDataFilterWidget(), value);
    }

    @Override
    public void refreshFilter() {
        if (isFilterable()) JQMCommon.refreshFilter(getDataFilterWidget());
    }

    /** @param filter - currently entered filter text */
    protected void onBeforeFilter(String filter) {
    }

    @Override
    public void doBeforeFilter(String filter) {
        onBeforeFilter(filter);
        JQMFilterableEvent.fire(this, JQMFilterableEvent.FilterableState.BEFORE_FILTER, filter);
    }

    public HandlerRegistration addFilterableHandler(JQMFilterableEvent.Handler handler) {
        return addHandler(handler, JQMFilterableEvent.getType());
    }

    private void bindFilterEvents() {
        if (boundFilterEvents) return;
        JQMCommon.bindFilterEvents(this, getDataFilterWidget().getElement());
        boundFilterEvents = true;
    }

    private void unbindFilterEvents() {
        if (!boundFilterEvents) return;
        JQMCommon.unbindFilterEvents(getDataFilterWidget().getElement());
        boundFilterEvents = false;
    }

    /**
     * @param elt - current filtering element, JQMCommon.getTextForFiltering(elt) can be used to get filtering text
     * @param index - filtering element's index
     * @param searchValue - filtering text
     *
     * @return - must return <b>true</b> if the element is to be <b>filtered out</b>.
     * <p/> - must return <b>false</b> if the element is to be <b>shown</b>.
     * <p/> - null means default filtering should be used.
     * <p/> JQMCommon.getTextForFiltering(elt) can be used to get filtering element's text
     */
    protected Boolean onFiltering(Element elt, Integer index, String searchValue) {
        //String s = JQMCommon.getTextForFiltering(elt);
        return null;
    }

    @Override
    public Boolean doFiltering(Element elt, Integer index, String searchValue) {
        Boolean rslt = onFiltering(elt, index, searchValue);
        Boolean eventRslt = JQMFilterableEvent.fire(this, JQMFilterableEvent.FilterableState.FILTERING,
                searchValue, elt, index);
        // return the worst (from "filter out" to "default filtering") result
        if (rslt != null && rslt || eventRslt != null && eventRslt) return true;
        if (rslt != null) return rslt;
        if (eventRslt != null) return eventRslt;
        return null;
    }

    private void bindFilterCallback() {
        if (boundFilterCallback) return;
        Element elt = getDataFilterWidget().getElement();
        origFilter = JQMCommon.getFilterCallback(elt);
        JQMCommon.bindFilterCallback(this, elt, origFilter);
        boundFilterCallback = true;
    }

    private void unbindFilterCallback() {
        if (!boundFilterCallback) return;
        JQMCommon.unbindFilterCallback(getDataFilterWidget().getElement(), origFilter);
        origFilter = null;
        boundFilterCallback = false;
    }

    private void checkFilterEvents() {
        if (isAttached()) {
            boolean b = isFilterable();
            if (!b) {
                unbindFilterEvents();
                unbindFilterCallback();
            } else {
                bindFilterEvents();
                Scheduler.get().scheduleFinally(new RepeatingCommand() {
                    @Override
                    public boolean execute() {
                        if (!isFilterable()) return false;
                        Element elt = getDataFilterWidget().getElement();
                        if (!JQMCommon.isFilterableReady(elt)) return true;
                        bindFilterCallback();
                        return false;
                    }
                });
            }
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        checkFilterEvents();
    }

    @Override
    protected void onUnload() {
        unbindFilterEvents();
        super.onUnload();
    }

}
