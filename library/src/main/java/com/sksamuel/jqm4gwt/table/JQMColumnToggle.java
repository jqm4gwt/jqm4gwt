package com.sksamuel.jqm4gwt.table;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasFilterable;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterableEvent;

/**
 * See <a href="http://demos.jquerymobile.com/1.4.5/table-column-toggle/">Table: Column Toggle</a>
 * <br> See <a href="http://demos.jquerymobile.com/1.4.5/table-reflow/">Table: Reflow</a>
 *
 * <br> See also <a href="http://jquerymobile.com/demos/1.3.0-rc.1/docs/tables/">Responsive tables</a>
 * <br> See also <a href="http://jquerymobile.com/demos/1.3.0-beta.1/docs/demos/tables/financial-grouped-columns.html">Grouped column headers</a>
 *
 * @author slavap
 *
 */
public class JQMColumnToggle extends JQMTableGrid implements HasFilterable {

    //TODO: table-stroke and table-stripe are deprecated in 1.4, so custom CSS will be needed in 1.5
    public static final String STD_ROW_LINES = "table-stroke";
    public static final String STD_ROW_STRIPES = "table-stripe";

    public static final String STD_RESPONSIVE = "ui-responsive";

    public static final String JQM4GWT_COL_PERSISTENT = "jqm4gwt-col-persistent";

    private static final String COLUMN_BTN_TEXT = "data-column-btn-text";
    private static final String COLUMN_BTN_THEME = "data-column-btn-theme";
    private static final String COLUMN_POPUP_THEME = "data-column-popup-theme";

    private static final String TOGGLE = "columntoggle";
    private static final String REFLOW = "reflow";

    private String rowLines;
    private String rowStripes;
    private String responsive;
    private String headerTheme;

    private boolean boundFilterEvents;
    private boolean boundFilterCallback;
    private boolean boundFilterCreate;
    private JavaScriptObject origFilter;

    public JQMColumnToggle() {
        Element table = getElement();
        JQMCommon.setDataRole(table, "table");
        JQMCommon.setAttribute(table, "data-mode", TOGGLE);

        setResponsive(STD_RESPONSIVE);
        setRowLines(STD_ROW_LINES);
        setRowStripes(STD_ROW_STRIPES);
    }

    @Override
    protected void setColPriority(ComplexPanel col, String priority) {
        super.setColPriority(col, priority);
        if (col == null) return;
        if (priority != null && !priority.isEmpty()) {
            JQMCommon.setAttribute(col.getElement(), "data-priority", priority);
            col.getElement().removeClassName(JQM4GWT_COL_PERSISTENT);
        } else {
            JQMCommon.setAttribute(col.getElement(), "data-priority", null);
            col.getElement().addClassName(JQM4GWT_COL_PERSISTENT);
        }
    }

    public String getRowLines() {
        return rowLines;
    }

    public void setRowLines(String rowLines) {
        if (this.rowLines == rowLines || this.rowLines != null && this.rowLines.equals(rowLines)) return;
        if (this.rowLines != null && !this.rowLines.isEmpty()) removeStyleName(this.rowLines);
        this.rowLines = rowLines;
        if (this.rowLines != null && !this.rowLines.isEmpty()) addStyleName(this.rowLines);
    }

    public String getRowStripes() {
        return rowStripes;
    }

    public void setRowStripes(String rowStripes) {
        if (this.rowStripes == rowStripes || this.rowStripes != null && this.rowStripes.equals(rowStripes)) return;
        if (this.rowStripes != null && !this.rowStripes.isEmpty()) removeStyleName(this.rowStripes);
        this.rowStripes = rowStripes;
        if (this.rowStripes != null && !this.rowStripes.isEmpty()) addStyleName(this.rowStripes);
    }

    public String getResponsive() {
        return responsive;
    }

    public void setResponsive(String responsive) {
        if (this.responsive == responsive || this.responsive != null && this.responsive.equals(responsive)) return;
        if (this.responsive != null && !this.responsive.isEmpty()) removeStyleName(this.responsive);
        this.responsive = responsive;
        if (this.responsive != null && !this.responsive.isEmpty()) addStyleName(this.responsive);
    }

    public String getColumnBtnText() {
        return JQMCommon.getAttribute(this, COLUMN_BTN_TEXT);
    }

    public void setColumnBtnText(String value) {
        JQMCommon.setAttribute(this, COLUMN_BTN_TEXT, value);
    }

    public String getColumnBtnTheme() {
        return JQMCommon.getAttribute(this, COLUMN_BTN_THEME);
    }

    public void setColumnBtnTheme(String value) {
        JQMCommon.setAttribute(this, COLUMN_BTN_THEME, value);
    }

    public String getColumnPopupTheme() {
        return JQMCommon.getAttribute(this, COLUMN_POPUP_THEME);
    }

    public void setColumnPopupTheme(String value) {
        JQMCommon.setAttribute(this, COLUMN_POPUP_THEME, value);
    }

    public String getBackgroundTheme() {
        return JQMCommon.getStyleStartsWith(this.getElement(), JQMCommon.STYLE_UI_BODY);
    }

    public void setBackgroundTheme(String value) {
        String s = getBackgroundTheme();
        String newTheme = value != null && !value.isEmpty() ? JQMCommon.STYLE_UI_BODY + value : null;
        if (s == newTheme || s != null && s.equals(newTheme)) return;
        JQMCommon.removeStylesStartsWith(this.getElement(), JQMCommon.STYLE_UI_BODY);
        if (newTheme != null) this.getElement().addClassName(newTheme);
    }

    private static String getEltHeaderTheme(Element elt) {
        if (elt == null) return null;
        return JQMCommon.getStyleStartsWith(elt, JQMCommon.STYLE_UI_BAR);
    }

    private static void setEltHeaderTheme(Element elt, String value) {
        if (elt == null) return;
        String s = getEltHeaderTheme(elt);
        String newTheme = value != null && !value.isEmpty() ? JQMCommon.STYLE_UI_BAR + value : null;
        if (s == newTheme || s != null && s.equals(newTheme)) return;
        JQMCommon.removeStylesStartsWith(elt, JQMCommon.STYLE_UI_BAR);
        if (newTheme != null) elt.addClassName(newTheme);
    }

    @Override
    protected void addedToHead(ComplexPanel... rows) {
        super.addedToHead(rows);
        setHeaderTheme(headerTheme);
    }

    public String getHeaderTheme() {
        ComplexPanel r = findHeadRow();
        if (r == null) return headerTheme;
        String s = getEltHeaderTheme(r.getElement());
        if (s != null && !s.isEmpty()) {
            s = s.substring(JQMCommon.STYLE_UI_BAR.length());
            return s;
        }
        return headerTheme;
    }

    public void setHeaderTheme(String value) {
        headerTheme = value;
        ComplexPanel r = findHeadRow();
        if (r != null) setEltHeaderTheme(r.getElement(), value);
        r = findHeadGroupsRow();
        if (r != null) setEltHeaderTheme(r.getElement(), value);
    }

    public boolean isReflow() {
        return REFLOW.equals(JQMCommon.getAttribute(this, "data-mode"));
    }

    public void setReflow(boolean value) {
        if (isReflow() == value) return;
        if (value) JQMCommon.setAttribute(this, "data-mode", REFLOW);
        else JQMCommon.setAttribute(this, "data-mode", TOGGLE);
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
                bindFilterableCreated();
            }
        }
    }

    private void bindFilterableCreated() {
        if (boundFilterCreate) return;
        bindFilterableCreated(getDataFilterWidget().getElement(), this);
        boundFilterCreate = true;
    }

    private void unbindFilterableCreated() {
        if (!boundFilterCreate) return;
        unbindFilterableCreated(getDataFilterWidget().getElement());
        boundFilterCreate = false;
    }

    private static native void bindFilterableCreated(Element elt, JQMColumnToggle ct) /*-{
        $wnd.$(elt).on( 'filterablecreate', function( event, ui ) {
            ct.@com.sksamuel.jqm4gwt.table.JQMColumnToggle::filterableCreated()();
        });
    }-*/;

    private static native void unbindFilterableCreated(Element elt) /*-{
        $wnd.$(elt).off( 'filterablecreate' );
    }-*/;

    private void filterableCreated() {
        bindFilterCallback();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        checkFilterEvents();
    }

    @Override
    protected void onUnload() {
        unbindFilterEvents();
        unbindFilterCallback();
        unbindFilterableCreated();
        super.onUnload();
    }

}
