package com.sksamuel.jqm4gwt.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;

/**
 * See <a href="http://view.jquerymobile.com/1.3.2/dist/demos/widgets/table-column-toggle/">Table: Column Toggle</a>
 * <p/> See also <a href="http://jquerymobile.com/demos/1.3.0-rc.1/docs/tables/">Responsive tables</a>
 * <p/><b>WARNING!</b> You'd better use fixed jQuery Mobile 1.3.2 from "jqm4gwt\standalone\misc\fixed jquery mobile",
 * because critical "columns stay hidden when shrinking and resizing the browser window"
 * bug is not fixed in official 1.3.2 version.
 * @author slavap
 *
 */
public class JQMColumnToggle extends CustomFlowPanel {

    public static final String STD_ROW_LINES = "table-stroke";
    public static final String STD_ROW_STRIPES = "table-stripe";
    public static final String STD_RESPONSIVE = "ui-responsive";

    private static final String COLUMN_BTN_TEXT = "data-column-btn-text";
    private static final String COLUMN_BTN_THEME = "data-column-btn-theme";
    private static final String COLUMN_POPUP_THEME = "data-column-popup-theme";

    private final ComplexPanel tHead;
    private final ComplexPanel tBody;

    private boolean loaded;

    private String rowLines;
    private String rowStripes;
    private String responsive;

    private String colNames;
    private String cells;

    private static class ColumnDef {
        public final String title;
        public final String priority;

        public ColumnDef(String title, String priority) {
            this.title = title;
            this.priority = priority;
        }
    }

    /** populated based on colNames parsing */
    private final Set<ColumnDef> columns = new LinkedHashSet<ColumnDef>();

    /** populated directly by addColTitleWidget(), probably from UiBinder template */
    private final Map<Widget, ColumnDef> colTitleWidgets = new LinkedHashMap<Widget, ColumnDef>();

    private List<String> dataStr;
    private List<Widget> dataObj;

    public JQMColumnToggle() {
        super(Document.get().createTableElement());
        Element table = getElement();
        table.setId(Document.get().createUniqueId());
        JQMCommon.setDataRole(table, "table");
        JQMCommon.setAttribute(table, "data-mode", "columntoggle");

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

    /**
     * @param colNames - comma separated column names with optional priority (1 = highest, 6 = lowest).
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
        String[] arr = this.colNames.split(",");
        Set<ColumnDef> cols = new LinkedHashSet<ColumnDef>();
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].trim();
            int j = s.lastIndexOf('=');
            String prio = null;
            if (j >= 0) {
                prio = s.substring(j + 1);
                s = s.substring(0, j);
            }
            cols.add(new ColumnDef(s, prio));
        }
        setColumns(cols);
    }

    public String getCells() {
        return cells;
    }

    /**
     * @param cells - comma separated table cells, each string/cell can be valid HTML
     * <p/> Example: &lt;th>1&lt;/th>, The Matrix, 1999, 8.7, &lt;th>2&lt;/th>, Falling Down, 1993, 7.5
     */
    public void setCells(String cells) {
        if (this.cells == cells || this.cells != null && this.cells.equals(cells)) return;
        this.cells = cells;
        if (this.cells == null || this.cells.isEmpty()) {
            setDataStr(null);
            return;
        }
        String[] arr = this.cells.split(",");
        List<String> lst = new ArrayList<String>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].trim();
            lst.add(s);
        }
        setDataStr(lst);
    }

    public void refreshBody() {
        tBody.clear();
        populateBody();
    }

    private void setColumns(Set<ColumnDef> cols) {
        int cnt = columns.size();
        int newCnt = cols != null ? cols.size() : 0;
        columns.clear();
        if (cols != null) columns.addAll(cols);
        tHead.clear();
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

    private static void setColPriority(Widget col, String priority) {
        if (col == null) return;
        if (priority != null && !priority.isEmpty()) {
            JQMCommon.setAttribute(col.getElement(), "data-priority", priority);
        } else {
            JQMCommon.setAttribute(col.getElement(), "data-priority", null);
        }
    }

    private void addToHead(String title, String priority, int index) {
        if (index < 0) return;
        ComplexPanel col = getHeadCol(index);
        if (col == null) return;
        setColPriority(col, priority);
        col.getElement().setInnerHTML(title);
    }

    private void addToHead(Widget w, String title, String priority, int index) {
        if (index < 0) return;
        ComplexPanel col = getHeadCol(index);
        if (col == null) return;
        col.clear();
        setColPriority(col, priority);
        col.getElement().setInnerHTML(title);
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
            for (Widget w : dataObj) {
                addToBody(w, i++);
            }
            return;
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        loaded = true;
        if (tBody.getWidgetCount() == 0 && !colTitleWidgets.isEmpty()) populateBody();
    }

    private int getNumOfCols() {
        if (!columns.isEmpty()) return columns.size();
        if (!colTitleWidgets.isEmpty() && loaded) return colTitleWidgets.size();
        return 0;
    }

    private void addToBody(String cell, int index) {
        if (cell == null || index < 0 || getNumOfCols() <= 0) return;
        int row = index / getNumOfCols();
        int col = index % getNumOfCols();
        ComplexPanel r = getRow(row);
        if (r == null) return;
        boolean addTh = cell.startsWith("<th>") || cell.startsWith("<TH>");
        ComplexPanel c = getCol(r, col, addTh);
        if (c == null) return;
        if (addTh) {
            String s = cell.substring("<th>".length(), cell.length() - "</th>".length()).trim();
            c.getElement().setInnerHTML(s);
        } else {
            c.getElement().setInnerHTML(cell);
        }
    }

    private void addToBody(Widget w, int index) {
        if (index < 0 || getNumOfCols() <= 0) return;
        int row = index / getNumOfCols();
        int col = index % getNumOfCols();
        ComplexPanel r = getRow(row);
        if (r == null) return;
        ComplexPanel c = getCol(r, col, false/*addTh*/);
        if (c == null) return;
        c.clear();
        if (w != null) c.add(w);
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

    private ComplexPanel getHeadRow() {
        for (int i = 0; i < tHead.getWidgetCount(); i++) {
            Widget child = tHead.getWidget(i);
            if (child instanceof ComplexPanel && isTag(TableRowElement.TAG, child.getElement())) {
                return (ComplexPanel) child;
            }
        }
        ComplexPanel r = new CustomFlowPanel(Document.get().createTRElement());
        tHead.add(r);
        return r;
    }

    private ComplexPanel getHeadCol(int col) {
        ComplexPanel r = getHeadRow();
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
            c = new CustomFlowPanel(Document.get().createTHElement());
            r.add(c);
        }
        return c;
    }

    private void setDataStr(List<String> lst) {
        dataObj = null;
        dataStr = lst;
        refreshBody();
    }

    @SuppressWarnings("unused")
    private void setDataObj(List<Widget> lst) {
        dataObj = lst;
        dataStr = null;
        refreshBody();
    }

    @UiChild(tagname = "cell")
    public void addCellWidget(Widget w) {
        if (dataStr != null) {
            tBody.clear();
            dataStr = null;
        }
        if (dataObj == null) dataObj = new ArrayList<Widget>();
        dataObj.add(w);
        addToBody(w, dataObj.size() - 1);
    }

    @UiChild(tagname = "colTitle")
    public void addColTitleWidget(Widget w, String priority, String text) {
        if (colNames != null) {
            tHead.clear();
            columns.clear();
            colNames = null;
        }
        ColumnDef colDef = new ColumnDef(text, priority);
        colTitleWidgets.put(w, colDef);
        addToHead(w, colDef.title, colDef.priority, colTitleWidgets.size() - 1);
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

    public String getHeaderTheme() {
        ComplexPanel r = getHeadRow();
        if (r == null) return null;
        return JQMCommon.getStyleStartsWith(r.getElement(), "ui-bar-");
    }

    public void setHeaderTheme(String value) {
        ComplexPanel r = getHeadRow();
        if (r == null) return;
        String s = getHeaderTheme();
        String newTheme = value != null && !value.isEmpty() ? "ui-bar-" + value : null;
        if (s == newTheme || s != null && s.equals(newTheme)) return;
        JQMCommon.removeStylesStartsWith(r.getElement(), "ui-bar-");
        if (newTheme != null) r.getElement().addClassName(newTheme);
    }

}
