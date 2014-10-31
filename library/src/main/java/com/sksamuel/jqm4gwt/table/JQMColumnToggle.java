package com.sksamuel.jqm4gwt.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasFilterable;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterableEvent;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;

/**
 * See <a href="http://demos.jquerymobile.com/1.4.5/table-column-toggle/">Table: Column Toggle</a>
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/table-reflow/">Table: Reflow</a>
 *
 * <p/> See also <a href="http://jquerymobile.com/demos/1.3.0-rc.1/docs/tables/">Responsive tables</a>
 * <p/> See also <a href="http://jquerymobile.com/demos/1.3.0-beta.1/docs/demos/tables/financial-grouped-columns.html">Grouped column headers</a>
 *
 * @author slavap
 *
 */
public class JQMColumnToggle extends CustomFlowPanel implements HasFilterable,
        HasValue<Collection<String>> {

    //TODO: table-stroke and table-stripe are deprecated in 1.4, so custom CSS will be needed in 1.5
    public static final String STD_ROW_LINES = "table-stroke";
    public static final String STD_ROW_STRIPES = "table-stripe";

    public static final String STD_RESPONSIVE = "ui-responsive";

    public static final String JQM4GWT_COL_PERSISTENT = "jqm4gwt-col-persistent";
    public static final String JQM4GWT_THEAD_GROUPS = "jqm4gwt-thead-groups";

    private static final String COLUMN_BTN_TEXT = "data-column-btn-text";
    private static final String COLUMN_BTN_THEME = "data-column-btn-theme";
    private static final String COLUMN_POPUP_THEME = "data-column-popup-theme";

    private static final String TOGGLE = "columntoggle";
    private static final String REFLOW = "reflow";

    private static final String IMG_ONLY = "img-only";

    // See http://stackoverflow.com/a/2709855
    @SuppressWarnings("unused")
    private static final String COMMA_SPLIT = "(?<!\\\\),";

    @SuppressWarnings("unused")
    private static final String BACKSLASH_COMMA = "\\\\,";

    private final ComplexPanel tHead;
    private final ComplexPanel tBody;

    private boolean loaded;

    private String rowLines;
    private String rowStripes;
    private String responsive;
    private String headerTheme;

    private String colNames;
    private String cells;
    private String colGroups;

    private static class ColumnDef {
        public String title;
        public String priority;
        public int colspan; // needed for 'Grouped column headers' mode

        public ColumnDef() {
        }

        public ColumnDef(String title, String priority) {
            this();
            this.title = title;
            this.priority = priority;
        }
    }

    /** populated based on colNames parsing */
    private final Set<ColumnDef> columns = new LinkedHashSet<ColumnDef>();

    /** populated directly by addColTitleWidget(), probably from UiBinder template */
    private final Map<Widget, ColumnDef> colTitleWidgets = new LinkedHashMap<Widget, ColumnDef>();

    /** populated based on colGroups parsing */
    private final Set<ColumnDef> headGroups = new LinkedHashSet<ColumnDef>();

    /** populated directly by addColGroupWidget(), probably from UiBinder template */
    private final Map<Widget, ColumnDef> colGroupWidgets = new LinkedHashMap<Widget, ColumnDef>();

    private Collection<String> dataStr;
    private Map<Widget, Boolean> dataObj;

    private boolean boundFilterEvents;
    private boolean boundFilterCallback;
    private JavaScriptObject origFilter;

    public JQMColumnToggle() {
        super(Document.get().createTableElement());
        Element table = getElement();
        table.setId(Document.get().createUniqueId());
        JQMCommon.setDataRole(table, "table");
        JQMCommon.setAttribute(table, "data-mode", TOGGLE);

        setResponsive(STD_RESPONSIVE);
        setRowLines(STD_ROW_LINES);
        setRowStripes(STD_ROW_STRIPES);

        tHead = new CustomFlowPanel(Document.get().createTHeadElement());
        tBody = new CustomFlowPanel(Document.get().createTBodyElement());
        add(tHead);
        add(tBody);
    }

    public String getColNames() {
        return colNames;
    }

    private static String[] commaSplit(String s) {
        if (s == null) return null;
        //return s.split(COMMA_SPLIT); - NOT WORKING when compiled to JS

        if (s.isEmpty()) return new String[] { s };
        List<String> rslt = null;
        int start = 0;
        int p = start;
        do {
            int j = s.indexOf(',', p);
            if (j == -1) {
                if (rslt == null) return new String[] { s };
                rslt.add(s.substring(start));
                break;
            }
            if (j > 0 && s.charAt(j - 1) == '\\') {
                p = j + 1;
                continue;
            } else {
                if (rslt == null) rslt = new ArrayList<String>();
                rslt.add(s.substring(start, j));
                start = j + 1;
                p = start;
            }

        } while (true);

        return rslt != null ? rslt.toArray(new String[0]) : null;
    }

    private static String replaceAllBackslashCommas(String s) {
        if (s == null) return null;
        //return s.replaceAll(BACKSLASH_COMMA, ","); - NOT WORKING when compiled to JS

        if (s.isEmpty()) return s;
        int p = s.lastIndexOf("\\,");
        if (p == -1) return s;

        StringBuilder sb = new StringBuilder(s);
        sb.deleteCharAt(p);
        int i = p - 1;
        while (i >= 1) {
            if (sb.charAt(i) == ',' && sb.charAt(i - 1) == '\\') {
                sb.deleteCharAt(i - 1);
                i = i - 2;
            } else {
                i--;
            }
        }
        return sb.toString();
    }

    /**
     * @param colNames - comma separated column names with optional priority (1 = highest, 6 = lowest).
     * If you need comma in name use \, to preserve it.
     * <p/> Column name can be valid HTML, i.e. &lt;abbr title="Rotten Tomato Rating">Rating&lt;/abbr>=1
     * <p/> Example: Rank,Movie Title,Year=3,Reviews=5
     * <p/> To make a column persistent so it's not available for hiding, just omit priority.
     * This will make the column visible at all widths and won't be available in the column chooser menu.
     */
    public void setColNames(String colNames) {
        if (this.colNames == colNames || this.colNames != null && this.colNames.equals(colNames)) return;
        this.colNames = colNames;
        colTitleWidgets.clear();

        if (this.colNames == null || this.colNames.isEmpty()) {
            setColumns(null);
            return;
        }
        String[] arr = commaSplit(this.colNames);
        Set<ColumnDef> cols = new LinkedHashSet<ColumnDef>();
        for (int i = 0; i < arr.length; i++) {
            String s = replaceAllBackslashCommas(arr[i].trim());
            cols.add(parseColumnDef(s, false/*colspanExpected*/));
        }
        setColumns(cols);
    }

    private static ColumnDef parseColumnDef(String str, boolean colspanExpected) {
        if (str == null) return null;
        ColumnDef col = new ColumnDef();
        int j = str.lastIndexOf('=');
        if (j >= 0) {
            String s = str.substring(j + 1).trim();
            if (s != null && !s.isEmpty()) col.priority = s;
            col.title = str.substring(0, j);
        } else {
            col.title = str;
        }
        if (colspanExpected) {
            j = col.title.indexOf('=');
            if (j >= 0) {
                String s = col.title.substring(0, j).trim();
                if (s != null && !s.isEmpty()) col.colspan = Integer.parseInt(s);
                col.title = col.title.substring(j + 1);
            }
        }
        return col;
    }

    public String getColGroups() {
        return colGroups;
    }

    /**
     * @param colGroups - comma separated grouped column headers with colspan and priority (1 = highest, 6 = lowest).
     * If you need comma in name use \, to preserve it.
     * <p/> Expected format: colspan=GroupName=priority
     * <p/> Group name can be valid HTML, i.e. 4=&lt;abbr title="Previous Year Results">2012&lt;/abbr>=1
     * <p/> Example: 3=Q1 2012=5, 3=Q2 2012=4, 3=Q3 2012=3, 3=Q4 2012=2, 3=2012 Totals=1
     */
    public void setColGroups(String colGroups) {
        if (this.colGroups == colGroups || this.colGroups != null && this.colGroups.equals(colGroups)) return;
        this.colGroups = colGroups;
        colGroupWidgets.clear();

        if (this.colGroups == null || this.colGroups.isEmpty()) {
            setHeadGroups(null);
            return;
        }
        String[] arr = commaSplit(this.colGroups);
        Set<ColumnDef> groups = new LinkedHashSet<ColumnDef>();
        for (int i = 0; i < arr.length; i++) {
            String s = replaceAllBackslashCommas(arr[i].trim());
            groups.add(parseColumnDef(s, true/*colspanExpected*/));
        }
        setHeadGroups(groups);
    }

    private void setHeadGroups(Set<ColumnDef> groups) {
        headGroups.clear();
        if (groups != null) headGroups.addAll(groups);
        populateHeadGroups();
    }

    public String getCells() {
        return cells;
    }

    /**
     * @param cells - comma separated table cells, each string/cell can be valid HTML.
     * If you need comma in name use \, to preserve it.
     * <p/> Example: &lt;th>1&lt;/th>, The Matrix, 1999, 8.7, &lt;th>2&lt;/th>, Falling Down, 1993, 7.5
     */
    public void setCells(String cells) {
        if (this.cells == cells || this.cells != null && this.cells.equals(cells)) return;
        this.cells = cells;
        if (this.cells == null || this.cells.isEmpty()) {
            setDataStr(null);
            return;
        }
        String[] arr = commaSplit(this.cells);
        List<String> lst = new ArrayList<String>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            String s = replaceAllBackslashCommas(arr[i].trim());
            lst.add(s);
        }
        setDataStr(lst);
    }

    public Collection<String> getBodyData() {
        return dataStr;
    }

    /**
     * Set and refresh table cells/body. Each string/cell in collection can be valid HTML.
     */
    public void setBodyData(Collection<String> data) {
        this.cells = null;
        setDataStr(data);
    }

    public void refreshBody() {
        tBody.clear();
        populateBody();
    }

    private static void setColPriority(Widget col, String priority) {
        if (col == null) return;
        if (priority != null && !priority.isEmpty()) {
            JQMCommon.setAttribute(col.getElement(), "data-priority", priority);
            col.getElement().removeClassName(JQM4GWT_COL_PERSISTENT);
        } else {
            JQMCommon.setAttribute(col.getElement(), "data-priority", null);
            col.getElement().addClassName(JQM4GWT_COL_PERSISTENT);
        }
    }

    private void setColumns(Set<ColumnDef> cols) {
        int cnt = columns.size();
        int newCnt = cols != null ? cols.size() : 0;
        columns.clear();
        if (cols != null) columns.addAll(cols);
        removeHeadRow();
        populateHead();
        if (cnt == newCnt) {
            if (tBody.getWidgetCount() == 0) populateBody();
            return;
        }
        refreshBody();
    }

    private void populateHead() {
        if (!columns.isEmpty()) {
            int i = 0;
            for (ColumnDef col : columns) {
                addToHead(col.title, col.priority, i++);
            }
            return;
        }
        if (!colTitleWidgets.isEmpty()) {
            int i = 0;
            for (Entry<Widget, ColumnDef> j : colTitleWidgets.entrySet()) {
                addToHead(j.getKey(), j.getValue().title, j.getValue().priority, i++);
            }
            return;
        }
    }

    private ComplexPanel addToHead(String title, String priority, int index) {
        if (index < 0) return null;
        ComplexPanel col = getCol(getHeadRow(), index, true/*addTh*/);
        if (col == null) return null;
        setColPriority(col, priority);
        col.getElement().setInnerHTML(title);
        applyImgOnly(col);
        return col;
    }

    private void addToHead(Widget w, String title, String priority, int index) {
        ComplexPanel col = addToHead(title, priority, index);
        if (col == null) return;
        col.clear();
        if (w != null) col.add(w);
    }

    private void populateBody() {
        if (dataStr != null) {
            int i = 0;
            for (String s : dataStr) {
                addToBody(s, i++);
            }
            return;
        }
        if (dataObj != null) {
            int i = 0;
            for (Entry<Widget, Boolean> j : dataObj.entrySet()) {
                addToBody(j.getKey(), i++, j.getValue() != null ? j.getValue() : false);
            }
            return;
        }
    }

    private int getNumOfCols() {
        if (!columns.isEmpty()) return columns.size();
        if (!colTitleWidgets.isEmpty() && loaded) return colTitleWidgets.size();
        return 0;
    }

    private void populateHeadGroups() {
        if (!headGroups.isEmpty()) {
            int i = 0;
            for (ColumnDef grp : headGroups) {
                addToHeadGroups(grp, i++);
            }
            return;
        }
        if (!colGroupWidgets.isEmpty()) {
            int i = 0;
            for (Entry<Widget, ColumnDef> j : colGroupWidgets.entrySet()) {
                addToHeadGroups(j.getKey(), j.getValue(), i++);
            }
            return;
        }
        removeHeadGroupsRow();
    }

    private ComplexPanel addToHeadGroups(ColumnDef grp, int index) {
        if (grp == null || index < 0) return null;
        boolean addTh = grp.colspan > 1 || isTh(grp.title);
        ComplexPanel col = getCol(getHeadGroupsRow(), index, addTh);
        if (col == null) return null;
        setColPriority(col, grp.priority);
        col.getElement().setInnerHTML(addTh ? removeTh(grp.title) : grp.title);
        if (grp.colspan > 1) JQMCommon.setAttribute(col, "colspan", String.valueOf(grp.colspan));
        applyImgOnly(col);
        return col;
    }

    private void addToHeadGroups(Widget w, ColumnDef grp, int index) {
        ComplexPanel col = addToHeadGroups(grp, index);
        if (col == null) return;
        col.clear();
        if (w != null) col.add(w);
    }

    private static boolean isTh(String s) {
        return s != null && !s.isEmpty() && (s.startsWith("<th>") || s.startsWith("<TH>"));
    }

    private static String removeTh(String s) {
        if (s == null || s.isEmpty() || !isTh(s)) return s;
        return s.substring("<th>".length(), s.length() - "</th>".length()).trim();
    }

    private static boolean isImgOnly(Element elt) {
        if (elt == null) return false;
        String s = elt.getInnerHTML();
        if (s == null || s.isEmpty()) return false;
        int p = s.indexOf('<');
        if (p == -1) return false;
        int endP = p + 1 + ImageElement.TAG.length();
        String t = s.substring(p + 1, endP);
        if (!ImageElement.TAG.equalsIgnoreCase(t)) return false;
        for (int i = endP; i < s.length(); i++) {
            if (s.charAt(i) == '>') {
                p = s.indexOf('<', i + 1);
                return p == -1;
            }
        }
        return false;
    }

    private static void applyImgOnly(Widget w) {
        if (w == null) return;
        Element elt = w.getElement();
        if (isImgOnly(elt)) elt.addClassName(IMG_ONLY);
        else elt.removeClassName(IMG_ONLY);
    }

    private void addToBody(String cell, int index) {
        if (cell == null || index < 0 || getNumOfCols() <= 0) return;
        int row = index / getNumOfCols();
        int col = index % getNumOfCols();
        ComplexPanel r = getRow(row);
        if (r == null) return;
        boolean addTh = isTh(cell);
        ComplexPanel c = getCol(r, col, addTh);
        if (c == null) return;
        if (addTh) {
            String s = removeTh(cell);
            c.getElement().setInnerHTML(s);
        } else {
            c.getElement().setInnerHTML(cell);
        }
        applyImgOnly(c);
    }

    private void addToBody(Widget w, int index, boolean addTh) {
        if (index < 0 || getNumOfCols() <= 0) return;
        int row = index / getNumOfCols();
        int col = index % getNumOfCols();
        ComplexPanel r = getRow(row);
        if (r == null) return;
        ComplexPanel c = getCol(r, col, addTh);
        if (c == null) return;
        c.clear();
        if (w != null) c.add(w);
        applyImgOnly(c);
    }

    private static boolean isTag(String tag, Element elt) {
        if (tag == null || elt == null) return false;
        return tag.equalsIgnoreCase(elt.getTagName());
    }

    private ComplexPanel getRow(int row) {
        int cnt = -1;
        for (int i = 0; i < tBody.getWidgetCount(); i++) {
            Widget child = tBody.getWidget(i);
            if (child instanceof ComplexPanel && isTag(TableRowElement.TAG, child.getElement())) {
                cnt++;
                if (cnt == row) return (ComplexPanel) child;
            }
        }
        ComplexPanel r = null;
        for (int i = cnt; i < row; i++) {
            r = new CustomFlowPanel(Document.get().createTRElement());
            tBody.add(r);
        }
        return r;
    }

    private static ComplexPanel getCol(ComplexPanel r, int col, boolean addTh) {
        if (r == null || col < 0) return null;
        int cnt = -1;
        for (int i = 0; i < r.getWidgetCount(); i++) {
            Widget child = r.getWidget(i);
            if (child instanceof ComplexPanel
                    && (isTag(TableCellElement.TAG_TH, child.getElement())
                            || isTag(TableCellElement.TAG_TD, child.getElement()))) {
                cnt++;
                if (cnt == col) return (ComplexPanel) child;
            }
        }
        ComplexPanel c = null;
        for (int i = cnt; i < col; i++) {
            c = addTh ? new CustomFlowPanel(Document.get().createTHElement())
                      : new CustomFlowPanel(Document.get().createTDElement());
            r.add(c);
        }
        return c;
    }

    private static class HeadGroupsPanel extends CustomFlowPanel {

        public HeadGroupsPanel(com.google.gwt.dom.client.Element e) {
            super(e);
            getElement().addClassName(JQM4GWT_THEAD_GROUPS);
        }
    }

    private ComplexPanel findHeadRow() {
        for (int i = 0; i < tHead.getWidgetCount(); i++) {
            Widget child = tHead.getWidget(i);
            if (child instanceof HeadGroupsPanel) continue;
            if (child instanceof ComplexPanel && isTag(TableRowElement.TAG, child.getElement())) {
                return (ComplexPanel) child;
            }
        }
        return null;
    }

    private ComplexPanel getHeadRow() {
        ComplexPanel r = findHeadRow();
        if (r != null) return r;
        r = new CustomFlowPanel(Document.get().createTRElement());
        tHead.add(r);
        setHeaderTheme(headerTheme);
        return r;
    }

    private void removeHeadRow() {
        ComplexPanel r = findHeadRow();
        if (r != null) tHead.remove(r);
    }

    private ComplexPanel findHeadGroupsRow() {
        for (int i = 0; i < tHead.getWidgetCount(); i++) {
            Widget child = tHead.getWidget(i);
            if (child instanceof HeadGroupsPanel) return (ComplexPanel) child;
        }
        return null;
    }

    private ComplexPanel getHeadGroupsRow() {
        ComplexPanel r = findHeadGroupsRow();
        if (r != null) return r;
        ComplexPanel headRow = getHeadRow();
        r = new HeadGroupsPanel(Document.get().createTRElement());
        if (headRow == null) {
            tHead.add(r);
        } else {
            tHead.remove(headRow);
            tHead.add(r);
            tHead.add(headRow);
        }
        setHeaderTheme(headerTheme);
        return r;
    }

    private void removeHeadGroupsRow() {
        ComplexPanel r = findHeadGroupsRow();
        if (r != null) tHead.remove(r);
    }

    private void setDataStr(Collection<String> lst) {
        dataObj = null;
        dataStr = lst;
        refreshBody();
    }

    @SuppressWarnings("unused")
    private void setDataObj(Map<Widget, Boolean> lst) {
        dataObj = lst;
        dataStr = null;
        refreshBody();
    }

    /**
     * @param asTh - &lt;th> will be used for creating cell instead of &lt;td>,
     * so such cell will be styled differently, like columnNames/header cells.
     */
    @UiChild(tagname = "cell")
    public void addCellWidget(Widget w, Boolean asTh) {
        if (dataStr != null) {
            tBody.clear();
            dataStr = null;
        }
        if (dataObj == null) dataObj = new LinkedHashMap<Widget, Boolean>();
        dataObj.put(w, asTh);
        addToBody(w, dataObj.size() - 1, asTh != null ? asTh : false);
    }

    @UiChild(tagname = "colTitle")
    public void addColTitleWidget(Widget w, String priority, String text) {
        if (colNames != null) {
            removeHeadRow();
            columns.clear();
            colNames = null;
        }
        ColumnDef colDef = new ColumnDef(text, priority);
        colTitleWidgets.put(w, colDef);
        addToHead(w, colDef.title, colDef.priority, colTitleWidgets.size() - 1);
    }

    @UiChild(tagname = "colGroup")
    public void addColGroupWidget(Widget w, String priority, String text, Integer colspan) {
        if (colGroups != null) {
            removeHeadGroupsRow();
            headGroups.clear();
            colGroups = null;
        }
        ColumnDef colDef = new ColumnDef(text, priority);
        if (colspan != null && colspan > 0) colDef.colspan = colspan;
        colGroupWidgets.put(w, colDef);
        addToHeadGroups(w, colDef, colGroupWidgets.size() - 1);
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
        return JQMCommon.getStyleStartsWith(this.getElement(), "ui-body-");
    }

    public void setBackgroundTheme(String value) {
        String s = getBackgroundTheme();
        String newTheme = value != null && !value.isEmpty() ? "ui-body-" + value : null;
        if (s == newTheme || s != null && s.equals(newTheme)) return;
        JQMCommon.removeStylesStartsWith(this.getElement(), "ui-body-");
        if (newTheme != null) this.getElement().addClassName(newTheme);
    }

    private static String getEltHeaderTheme(Element elt) {
        if (elt == null) return null;
        return JQMCommon.getStyleStartsWith(elt, "ui-bar-");
    }

    private static void setEltHeaderTheme(Element elt, String value) {
        if (elt == null) return;
        String s = getEltHeaderTheme(elt);
        String newTheme = value != null && !value.isEmpty() ? "ui-bar-" + value : null;
        if (s == newTheme || s != null && s.equals(newTheme)) return;
        JQMCommon.removeStylesStartsWith(elt, "ui-bar-");
        if (newTheme != null) elt.addClassName(newTheme);
    }

    public String getHeaderTheme() {
        ComplexPanel r = findHeadRow();
        if (r == null) return headerTheme;
        String s = getEltHeaderTheme(r.getElement());
        if (s != null && !s.isEmpty()) {
            s = s.substring("ui-bar-".length());
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
        loaded = true;
        checkFilterEvents();
        if (tBody.getWidgetCount() == 0 && !colTitleWidgets.isEmpty()) populateBody();
    }

    @Override
    protected void onUnload() {
        unbindFilterEvents();
        super.onUnload();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Collection<String>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public Collection<String> getValue() {
        return getBodyData();
    }

    @Override
    public void setValue(Collection<String> value) {
        setValue(value, false/*fireEvents*/);
    }

    @Override
    public void setValue(Collection<String> value, boolean fireEvents) {
        Collection<String> oldValue = fireEvents ? getValue() : null;
        setBodyData(value);
        if (fireEvents) {
            Collection<String> newValue = getValue();
            ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
        }
    }

}
