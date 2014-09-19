package com.sksamuel.jqm4gwt.panel;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasFilterable;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasOrientation;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.Orientation;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterableEvent;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 22:09:41
 *         <p/>
 *         An implementation of {@link JQMPanel} for control groups.
 */
public class JQMControlGroup extends JQMPanel implements HasOrientation<JQMControlGroup>,
        HasMini<JQMControlGroup>, HasCorners<JQMControlGroup>, HasFilterable {

    private boolean boundFilterEvents;
    private boolean boundFilterCallback;
    private JavaScriptObject origFilter;

    protected JQMControlGroup(Element element, String styleName) {
        super(element, "controlgroup", styleName);
    }

    public JQMControlGroup() {
        this(Document.get().createDivElement(), "jqm4gwt-controlgroup");
    }

    public JQMControlGroup(Widget... widgets) {
        this();
        for (Widget w : widgets) {
            add(w);
        }
    }

    public Orientation getOrientation() {
        return HasOrientation.Support.getOrientation(this);
    }

    public void setOrientation(Orientation value) {
        HasOrientation.Support.setOrientation(this, value);
    }

    @Override
    public boolean isHorizontal() {
        return "horizontal".equals(getAttribute("data-type"));
    }

    @Override
    public void setHorizontal() {
        setAttribute("data-type", "horizontal");
    }

    @Override
    public JQMControlGroup withHorizontal() {
        setHorizontal();
        return this;
    }

    @Override
    public boolean isVertical() {
        return !isHorizontal();
    }

    @Override
    public void setVertical() {
        removeAttribute("data-type");
    }

    @Override
    public JQMControlGroup withVertical() {
        setVertical();
        return this;
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(this);
    }

    /** If set to true then renders a smaller version of the standard-sized element. */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(this, mini);
    }

    /** If set to true then renders a smaller version of the standard-sized element. */
    @Override
    public JQMControlGroup withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCorners(this);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCorners(this, corners);
    }

    @Override
    public JQMControlGroup withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    protected static native void refresh(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-controlgroup') !== undefined) {
            w.controlgroup('refresh');
        }
    }-*/;

    /**
     * After dynamic changes to widgets, can be called in conjunction with JQMContext.render().
     * <p/> Example:
     * <p/> JQMContext.render(grp.getElement().getId());
     * <p/> grp.refresh();
     */
    public void refresh() {
        refresh(getElement());
    }

    // Filterable support copied from JQMWidget

    private Widget getDataFilterWidget() {
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
     * @param filterSelector - a jQuery selector that will be used to retrieve the element
     * that will serve as the input source, UiBinder example: dataFilter="#{fltr1.getFilterId}"
     */
    public void setDataFilter(String filterSelector) {
        JQMCommon.setDataFilter(getDataFilterWidget(), filterSelector);
        checkFilterEvents();
    }

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
     * @param elt - current filtering element
     * @param index - filtering element's index
     * @param searchValue - filtering text
     * @return - must return true if the element is to be filtered,
     * and it must return false if the element is to be shown.
     * null - means default filtering should be used.
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
