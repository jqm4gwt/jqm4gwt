package com.sksamuel.jqm4gwt.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMContext.WidgetDefaults;
import com.sksamuel.jqm4gwt.StrUtils;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;

/**
 * Advanced wrapper for Html Table element. Not supposed to be used "per se", see descendants.
 *
 * @author slavap
 */
public class JQMTableGrid extends CustomFlowPanel implements HasValue<Collection<String>> {

    // both are used in jqm4gwt.css
    public static final String THEAD_GROUPS = "jqm4gwt-thead-groups";
    public static final String IMG_ONLY = "img-only";

    protected boolean loaded;

    protected final ComplexPanel tHead;
    protected final ComplexPanel tBody;
    protected ComplexPanel tFoot;

    /** populated based on colNames parsing */
    protected final List<ColumnDef> columns = new ArrayList<ColumnDef>();

    /** populated directly by addColTitleWidget(), probably from UiBinder template */
    protected final Map<Widget, ColumnDef> colTitleWidgets = new LinkedHashMap<Widget, ColumnDef>();

    /** populated based on colGroups parsing */
    protected final List<ColumnDef> headGroups = new ArrayList<ColumnDef>();

    /** populated directly by addColGroupWidget(), probably from UiBinder template */
    protected final Map<Widget, ColumnDef> colGroupWidgets = new LinkedHashMap<Widget, ColumnDef>();

    protected String colNames;
    protected String colGroups;

    protected String footColTitles;

    private String colClassNames;
    private Map<Integer, String> colClsNames;
    private Map<Integer, String> colClsNamesHead;
    private Map<Integer, String> colClsNamesBody;

    private String cells;

    private Collection<String> dataStr;
    /** Boolean - true means add as &lt;th&gt; */
    private Map<Widget, Boolean> dataObj;

    protected JQMTableGrid() {
        super(Document.get().createTableElement());
        Element table = getElement();
        table.setId(Document.get().createUniqueId());
        tHead = new CustomFlowPanel(Document.get().createTHeadElement());
        tBody = new CustomFlowPanel(Document.get().createTBodyElement());
        add(tHead);
        add(tBody);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        loaded = true;
        WidgetDefaults dflt = JQMContext.getWidgetDefaults();
        if (dflt != null) dflt.loaded(this);
        applyColClassNames(true/*add*/);
        if (tBody.getWidgetCount() == 0 && !colTitleWidgets.isEmpty()) populateBody();
    }

    @Override
    protected void onUnload() {
        loaded = false;
        super.onUnload();
    }

    protected void checkFooterCreated() {
        if (tFoot != null) return;
        tFoot = new CustomFlowPanel(Document.get().createTFootElement());
        int i = getWidgetIndex(tBody);
        if (i >= 0) insert(tFoot, i);
        else add(tFoot);
    }

    public String getColNames() {
        return colNames;
    }

    /**
     * @param colNames - comma separated column titles with optional priority (1 = highest, 6 = lowest),
     * formally:<b> title~name=priority </b>
     * <br> If you need comma in title use \, to preserve it.
     * <br> Column title can be valid HTML, i.e. &lt;abbr title="Rotten Tomato Rating">Rating&lt;/abbr&gt;=1
     * <br> Example: Rank,Movie Title,Year=3,Reviews=5
     * <br> To make a column persistent so it's not available for hiding, just omit priority.
     * This will make the column visible at all widths and won't be available in the column chooser menu.
     */
    public void setColNames(String colNames) {
        if (this.colNames == colNames || this.colNames != null && this.colNames.equals(colNames)) return;
        this.colNames = colNames;

        if (Empty.is(this.colNames)) {
            populateColumns(null);
            return;
        }
        List<String> lst = StrUtils.commaSplit(this.colNames);
        List<ColumnDef> cols = new ArrayList<ColumnDef>();
        for (String i : lst) {
            String s = StrUtils.replaceAllBackslashCommas(i.trim());
            cols.add(ColumnDef.create(s, false/*headGroup*/));
        }
        populateColumns(cols);
    }

    public String getFootColTitles() {
        return footColTitles;
    }

    /**
     * @param footColTitles - comma separated footer column titles.
     * <br> If you need comma in title use \, to preserve it.
     * <br> Column title can be valid HTML, i.e. &lt;abbr title="Rotten Tomato Rating">Rating&lt;/abbr&gt;
     * <br> Example: Rank,Movie Title,Year,Reviews
     */
    public void setFootColTitles(String footColTitles) {
        if (this.footColTitles == footColTitles
                || this.footColTitles != null && this.footColTitles.equals(footColTitles)) return;
        this.footColTitles = footColTitles;
        if (Empty.is(this.footColTitles)) {
            if (tFoot != null) {
                tFoot.clear();
                tFoot.getElement().setInnerText(null);
            }
            return;
        }
        if (tFoot != null) {
            tFoot.clear();
            tFoot.getElement().setInnerText(null);
        } else {
            checkFooterCreated();
        }
        TableRowElement tr = Document.get().createTRElement();
        List<String> lst = StrUtils.commaSplit(this.footColTitles);
        for (String i : lst) {
            String s = StrUtils.replaceAllBackslashCommas(i.trim());
            TableCellElement th = Document.get().createTHElement();
            th.setInnerHTML(s);
            tr.appendChild(th);
        }
        tFoot.getElement().insertFirst(tr);
    }

    public String getColGroups() {
        return colGroups;
    }

    /**
     * @param colGroups - comma separated grouped column headers(titles) with colspan, rowspan,
     * and priority (1 = highest, 6 = lowest),
     * formally:<b> colspan;rowspan=GroupTitle~ColumnName=priority </b>
     * <br> If you need comma in title use \, to preserve it.
     * <br> Group title can be valid HTML, i.e. 4=&lt;abbr title="Previous Year Results">2012&lt;/abbr&gt;=1
     * <br> Example: 3=Q1 2012=5, 3=Q2 2012=4, 3=Q3 2012=3, 3=Q4 2012=2, 3=2012 Totals=1
     */
    public void setColGroups(String colGroups) {
        if (this.colGroups == colGroups || this.colGroups != null && this.colGroups.equals(colGroups)) return;
        this.colGroups = colGroups;
        colGroupWidgets.clear();

        if (this.colGroups == null || this.colGroups.isEmpty()) {
            setHeadGroups(null);
            return;
        }
        List<String> lst = StrUtils.commaSplit(this.colGroups);
        Set<ColumnDef> groups = new LinkedHashSet<ColumnDef>();
        for (String i : lst) {
            String s = StrUtils.replaceAllBackslashCommas(i.trim());
            groups.add(ColumnDef.create(s, true/*headGroup*/));
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
     * If you need comma in cell value use \, to preserve it.
     * <br> Example: &lt;th&gt;1&lt;/th&gt;, The Matrix, 1999, 8.7, &lt;th&gt;2&lt;/th&gt;, Falling Down, 1993, 7.5
     */
    public void setCells(String cells) {
        if (this.cells == cells || this.cells != null && this.cells.equals(cells)) return;
        this.cells = cells;
        if (this.cells == null || this.cells.isEmpty()) {
            setDataStr(null);
            return;
        }
        List<String> lst = StrUtils.commaSplit(this.cells);
        ListIterator<String> iter = lst.listIterator();
        while (iter.hasNext()) {
            String s = iter.next();
            s = StrUtils.replaceAllBackslashCommas(s.trim());
            iter.set(s);
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

    /** Adjusts body to match current columns */
    public void refreshBody() {
        tBody.clear();
        populateBody();
    }

    private void populateColumns(List<ColumnDef> cols) {
        int cnt = columns.size();
        int newCnt = cols != null ? cols.size() : 0;
        columns.clear();
        if (cols != null) columns.addAll(cols);
        colTitleWidgets.clear();
        removeHeadRow();
        populateHead();
        if (cnt == newCnt) {
            if (tBody.getWidgetCount() == 0) populateBody();
            return;
        }
        refreshBody();
    }

    private static class HeadGroupsPanel extends CustomFlowPanel {

        public HeadGroupsPanel(Element e, String addnlClasses) {
            super(e);
            if (addnlClasses != null && !addnlClasses.isEmpty()) {
                JQMCommon.addStyleNames(this, addnlClasses);
            }
        }
    }

    /** Space separated classes for adding to head groups panel. */
    protected String getHeadGroupsClasses() {
        return THEAD_GROUPS;
    }

    protected ComplexPanel findHeadRow() {
        for (int i = 0; i < tHead.getWidgetCount(); i++) {
            Widget child = tHead.getWidget(i);
            if (child instanceof HeadGroupsPanel) continue;
            if (child instanceof ComplexPanel && isTag(TableRowElement.TAG, child.getElement())) {
                return (ComplexPanel) child;
            }
        }
        return null;
    }

    /**
     * @param rows - collection of rows which are just added to head.
     */
    protected void addedToHead(ComplexPanel... rows) {
    }

    private ComplexPanel getHeadRow() {
        ComplexPanel r = findHeadRow();
        if (r != null) return r;
        r = new CustomFlowPanel(Document.get().createTRElement());
        tHead.add(r);
        addedToHead(r);
        return r;
    }

    private void removeHeadRow() {
        ComplexPanel r = findHeadRow();
        if (r != null) tHead.remove(r);
    }

    protected ComplexPanel findHeadGroupsRow() {
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
        r = new HeadGroupsPanel(Document.get().createTRElement(), getHeadGroupsClasses());
        if (headRow == null) {
            tHead.add(r);
            addedToHead(r);
        } else {
            tHead.remove(headRow);
            tHead.add(r);
            tHead.add(headRow);
            addedToHead(r, headRow);
        }
        return r;
    }

    private void removeHeadGroupsRow() {
        ComplexPanel r = findHeadGroupsRow();
        if (r != null) tHead.remove(r);
    }

    protected void populateHead() {
        if (!columns.isEmpty()) {
            int i = 0;
            for (ColumnDef col : columns) {
                addToHead(col.getTitle(), col.getPriority(), i++);
            }
            return;
        }
        if (!colTitleWidgets.isEmpty()) {
            int i = 0;
            for (Entry<Widget, ColumnDef> j : colTitleWidgets.entrySet()) {
                addToHead(j.getKey(), j.getValue().getTitle(), j.getValue().getPriority(), i++);
            }
            return;
        }
    }

    /**
     * @param col
     * @param priority
     */
    protected void setColPriority(ComplexPanel col, String priority) {
    }

    protected ComplexPanel addToHead(String title, String priority, int index) {
        if (index < 0) return null;
        ComplexPanel col = getCol(getHeadRow(), index, true/*addTh*/);
        if (col == null) return null;
        setColPriority(col, priority);
        col.getElement().setInnerHTML(title);
        String classes = calcColClassNames(index, true/*head*/);
        if (!Empty.is(classes)) JQMCommon.addStyleNames(col, classes);
        applyImgOnly(col);
        return col;
    }

    protected ComplexPanel addToHead(Widget w, String title, String priority, int index) {
        ComplexPanel col = addToHead(title, priority, index);
        if (col == null) return null;
        col.clear();
        if (w != null) col.add(w);
        return col;
    }

    protected void populateBody() {
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

    /** important for body, because exact number of columns should be known when populating */
    protected int getNumOfCols() {
        if (!columns.isEmpty()) return getHeadRegularColCnt() + columns.size();
        if (loaded && !colTitleWidgets.isEmpty()) return getHeadRegularColCnt() + colTitleWidgets.size();
        return getHeadRegularColCnt();
    }

    private int getHeadRegularColCnt() {
        int headColumnCnt = 0;
        if (!headGroups.isEmpty()) {
            for (ColumnDef i : headGroups) {
                if (!i.isGroup()) headColumnCnt++;
            }
        }
        return headColumnCnt;
    }

    protected void populateHeadGroups() {
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

    protected ComplexPanel addToHeadGroups(ColumnDef grp, int index) {
        if (grp == null || index < 0) return null;
        final int colspan = grp.getColspan();
        final int rowspan = grp.getRowspan();
        final boolean isTitleTh = isTh(grp.getTitle());
        boolean addTh = colspan > 1 || rowspan > 1 || isTitleTh;
        ComplexPanel col = getCol(getHeadGroupsRow(), index, addTh);
        if (col == null) return null;
        setColPriority(col, grp.getPriority());
        col.getElement().setInnerHTML(isTitleTh ? removeTh(grp.getTitle()) : grp.getTitle());
        if (colspan > 1) JQMCommon.setAttribute(col, "colspan", String.valueOf(colspan));
        if (rowspan > 1) JQMCommon.setAttribute(col, "rowspan", String.valueOf(rowspan));
        String classes = calcColClassNames(index, true/*head*/);
        if (!Empty.is(classes)) JQMCommon.addStyleNames(col, classes);
        applyImgOnly(col);
        return col;
    }

    protected ComplexPanel addToHeadGroups(Widget w, ColumnDef grp, int index) {
        ComplexPanel col = addToHeadGroups(grp, index);
        if (col == null) return null;
        col.clear();
        if (w != null) col.add(w);
        return col;
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
        p = s.indexOf('>', endP);
        if (p == -1) return false;
        p = s.indexOf('<', p + 1);
        return p == -1;
    }

    private static void applyImgOnly(Widget w) {
        if (w == null) return;
        Element elt = w.getElement();
        if (isImgOnly(elt)) elt.addClassName(IMG_ONLY);
        else elt.removeClassName(IMG_ONLY);
    }

    private ComplexPanel getRowByAbsIdx(int index) {
        if (index < 0 || getNumOfCols() <= 0) return null;
        int row = index / getNumOfCols();
        ComplexPanel r = getRow(row);
        return r;
    }

    /**
     * @param colIdx - in range of 0..getNumOfCols-1
     */
    protected boolean isColCellTypeTh(int colIdx) {
        return false;
    }

    private void addBodyCell(ComplexPanel row, int colIdx, String cell, boolean addTh, Widget... widgets) {
        if (!addTh) addTh = isColCellTypeTh(colIdx);
        ComplexPanel c = getCol(row, colIdx, addTh);
        if (c == null) return;
        c.clear();
        if (widgets != null && widgets.length > 0) {
            for (Widget w : widgets) c.add(w);
        } else {
            c.getElement().setInnerHTML(cell);
        }
        String classes = calcColClassNames(colIdx, false/*head*/);
        if (!Empty.is(classes)) JQMCommon.addStyleNames(c, classes);
        applyImgOnly(c);
    }

    protected void addToBody(String cell, int index) {
        if (cell == null) return;
        ComplexPanel r = getRowByAbsIdx(index);
        if (r == null) return;
        int col = index % getNumOfCols();
        boolean addTh = isTh(cell);
        if (addTh) cell = removeTh(cell);
        addBodyCell(r, col, cell, addTh);
    }

    protected void addToBody(Widget w, int index, boolean addTh) {
        ComplexPanel r = getRowByAbsIdx(index);
        if (r == null) return;
        int col = index % getNumOfCols();
        addBodyCell(r, col, null/*cell*/, addTh, w);
    }

    private static boolean isTag(String tag, Element elt) {
        if (tag == null || elt == null) return false;
        return tag.equalsIgnoreCase(elt.getTagName());
    }

    @SuppressWarnings("unused")
    private int getNumOfRows() {
        return tBody.getWidgetCount();
    }

    private void forEachRow(ForEachCallback callback) {
        if (callback == null) return;
        int cnt = -1;
        for (int i = 0; i < tBody.getWidgetCount(); i++) {
            Widget child = tBody.getWidget(i);
            if (child instanceof ComplexPanel && isTag(TableRowElement.TAG, child.getElement())) {
                cnt++;
                callback.process((ComplexPanel) child, cnt);
            }
        }
    }

    @SuppressWarnings("unused")
    private ComplexPanel findRow(int row) {
        int cnt = -1;
        for (int i = 0; i < tBody.getWidgetCount(); i++) {
            Widget child = tBody.getWidget(i);
            if (child instanceof ComplexPanel && isTag(TableRowElement.TAG, child.getElement())) {
                cnt++;
                if (cnt == row) return (ComplexPanel) child;
            }
        }
        return null;
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

    private interface ForEachCallback {
        void process(ComplexPanel item, int index);
    }

    private static void forEachCol(ComplexPanel row, ForEachCallback callback) {
        if (row == null || callback == null) return;
        int cnt = -1;
        for (int i = 0; i < row.getWidgetCount(); i++) {
            Widget child = row.getWidget(i);
            if (child instanceof ComplexPanel
                    && (isTag(TableCellElement.TAG_TH, child.getElement())
                            || isTag(TableCellElement.TAG_TD, child.getElement()))) {
                cnt++;
                callback.process((ComplexPanel) child, cnt);
            }
        }
    }

    @SuppressWarnings("unused")
    private static ComplexPanel findCol(ComplexPanel row, int col) {
        if (row == null || col < 0) return null;
        int cnt = -1;
        for (int i = 0; i < row.getWidgetCount(); i++) {
            Widget child = row.getWidget(i);
            if (child instanceof ComplexPanel
                    && (isTag(TableCellElement.TAG_TH, child.getElement())
                            || isTag(TableCellElement.TAG_TD, child.getElement()))) {
                cnt++;
                if (cnt == col) return (ComplexPanel) child;
            }
        }
        return null;
    }

    private static ComplexPanel getCol(ComplexPanel row, int col, boolean addTh) {
        if (row == null || col < 0) return null;
        int cnt = -1;
        for (int i = 0; i < row.getWidgetCount(); i++) {
            Widget child = row.getWidget(i);
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
            row.add(c);
        }
        return c;
    }

    /**
     * @param asTh - &lt;th&gt; will be used for creating cell instead of &lt;td&gt;,
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

    protected void clearColNames() {
        if (colNames != null) {
            removeHeadRow();
            columns.clear();
            colNames = null;
        }
    }

    protected void clearHead() {
        clearColNames();
        colTitleWidgets.clear();
        clearColGroups();
        colGroupWidgets.clear();
    }

    @UiChild(tagname = "colTitle")
    public void addColTitleWidget(Widget w, String priority, String text, String colName) {
        clearColNames();
        ColumnDef colDef = new ColumnDef(text, priority);
        if (!Empty.is(colName)) colDef.setName(colName);
        colTitleWidgets.put(w, colDef);
        addToHead(w, colDef.getTitle(), colDef.getPriority(), colTitleWidgets.size() - 1);
    }

    protected void clearColGroups() {
        if (colGroups != null) {
            removeHeadGroupsRow();
            headGroups.clear();
            colGroups = null;
        }
    }

    @UiChild(tagname = "colGroup")
    public void addColGroupWidget(Widget w, String priority, String text, String colName,
                                  Integer colspan, Integer rowspan) {
        clearColGroups();
        ColumnDef colDef = new ColumnDef(text, priority);
        if (!Empty.is(colName)) colDef.setName(colName);
        if (colspan != null && colspan > 0) colDef.setColspan(colspan);
        if (rowspan != null && rowspan > 0) colDef.setRowspan(rowspan);
        colGroupWidgets.put(w, colDef);
        addToHeadGroups(w, colDef, colGroupWidgets.size() - 1);
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

    public String getColClassNames() {
        return colClassNames;
    }

    /**
     * @param colClassNames - expected: 2=RIGHT NOWRAP abc, 3=LEFT
     **/
    public void setColClassNames(String colClassNames) {
        String old = getColClassNames();
        if (StrUtils.equals(old, colClassNames)) return;
        if (loaded) applyColClassNames(false/*add*/);
        this.colClassNames = colClassNames;
        if (colClsNames != null) {
            colClsNames.clear();
            if (colClsNamesHead != null) colClsNamesHead.clear();
            if (colClsNamesBody != null) colClsNamesBody.clear();
        }
        if (!Empty.is(this.colClassNames)) {
            List<String> lst = StrUtils.commaSplit(this.colClassNames);
            for (String i : lst) {
                int j = i.indexOf("=");
                if (j > 0 && j < i.length() - 1) {
                    String s = i.substring(0, j).trim();
                    int n;
                    try {
                        n = Integer.parseInt(s);
                        if (n < 0) continue;
                    } catch (Exception ex) {
                        continue;
                    }
                    s = i.substring(j + 1).trim();
                    if (s.isEmpty()) continue;
                    s = ColumnDef.processClassNames(s);
                    if (colClsNames == null) colClsNames = new HashMap<>();
                    colClsNames.put(n, s);
                }
            }
            if (loaded) applyColClassNames(true/*add*/);
        }
    }

    protected String getColClassNames(int colIdx) {
        return colClsNames != null ? colClsNames.get(colIdx) : null;
    }

    private String getColClassNamesHead(int colIdx) {
        return colClsNamesHead != null ? colClsNamesHead.get(colIdx) : null;
    }

    private String getColClassNamesBody(int colIdx) {
        return colClsNamesBody != null ? colClsNamesBody.get(colIdx) : null;
    }

    private void checkColClsNamesDependants() {
        if (colClsNames == null || colClsNames.isEmpty()) {
            if (colClsNamesHead != null) colClsNamesHead.clear();
            if (colClsNamesBody != null) colClsNamesBody.clear();
            return;
        }
        if (colClsNamesHead == null || colClsNamesHead.isEmpty()) {
            if (colClsNamesHead == null) colClsNamesHead = new HashMap<>(colClsNames.size());
            for (Entry<Integer, String> i : colClsNames.entrySet()) {
                String s = i.getValue();
                s = JQMCommon.removeStylesStartsWith(s, ColumnStyleClass.JQM_BODY_ONLY_PREFIX);
                colClsNamesHead.put(i.getKey(), s);
            }
        }
        if (colClsNamesBody == null || colClsNamesBody.isEmpty()) {
            if (colClsNamesBody == null) colClsNamesBody = new HashMap<>(colClsNames.size());
            for (Entry<Integer, String> i : colClsNames.entrySet()) {
                String s = i.getValue();
                s = JQMCommon.removeStylesStartsWith(s, ColumnStyleClass.JQM_HEAD_ONLY_PREFIX);
                colClsNamesBody.put(i.getKey(), s);
            }
        }
    }

    private String calcColClassNames(int colIdx, boolean head) {
        checkColClsNamesDependants();
        String classes = head ? getColClassNamesHead(colIdx) : getColClassNamesBody(colIdx);
        return classes;
    }

    /**
     * @param add - if false then colClassNames will be removed.
     */
    protected void applyColClassNames(boolean add) {
        if (colClsNames == null || colClsNames.isEmpty()) return;

        ComplexPanel head = getHeadRow();
        if (head != null) {
            forEachCol(head, (col, i) -> {
                String classes = calcColClassNames(i, true/*head*/);
                if (!Empty.is(classes)) {
                    if (add) JQMCommon.addStyleNames(col, classes);
                    else JQMCommon.removeStyleNames(col, classes);
                }
            });
        }

        forEachRow((row, j) -> {
            forEachCol(row, (col, i) -> {
                String classes = calcColClassNames(i, false/*head*/);
                if (!Empty.is(classes)) {
                    if (add) JQMCommon.addStyleNames(col, classes);
                    else JQMCommon.removeStyleNames(col, classes);
                }
            });
        });
    }

}
