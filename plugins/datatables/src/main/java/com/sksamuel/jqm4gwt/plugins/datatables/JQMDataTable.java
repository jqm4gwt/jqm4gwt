package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.StrUtils;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsAjax;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsColumn;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsColumns;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsEnhanceParams;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsLanguage;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsSortItem;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsSortItems;
import com.sksamuel.jqm4gwt.table.JQMTableGrid;

/**
 * Wrapper for jQuery DataTables.
 * <br> See <a href="https://www.datatables.net/examples/index">DataTables examples</a>
 *
 * @author slavap
 *
 */
public class JQMDataTable extends JQMTableGrid {

    /** Space separated classes for adding to head groups panel. */
    public static String headGroupsClasses;

    private boolean enhanced;

    private boolean paging = true;
    private boolean lengthChange = true;
    private boolean info = true;
    private boolean ordering = true;
    private boolean searching = true;

    private final List<ColumnDefEx> datacols = new ArrayList<>();

    // expected: 0, 2=desc, 3
    // then it should be translated to: "order": [[0, "asc"], [2, "desc"], [3, "asc"]]
    private String colSorts;

    protected static enum SortKind {
        DESC("desc"), ASC("asc");

        private final String jsName;

        SortKind(String jsName) {
            this.jsName = jsName;
        }

        public String getJsName() {
            return jsName;
        }

        public static SortKind fromJsName(String jsName) {
            if (Empty.is(jsName)) return null;
            for (SortKind v : values()) {
                if (jsName.equalsIgnoreCase(v.getJsName())) return v;
            }
            return null;
        }
    }

    protected static class ColSort {
        public final int num;
        public final SortKind kind;

        public ColSort(int num, SortKind kind) {
            this.num = num;
            this.kind = kind;
        }

        public static ColSort create(String s) {
            if (Empty.is(s)) return null;
            int j = s.lastIndexOf('=');
            SortKind k = SortKind.ASC;
            if (j >= 0) {
                String ss = s.substring(j + 1).trim();
                s = s.substring(0, j).trim();
                if (s.isEmpty()) return null;
                k = SortKind.fromJsName(ss);
                if (k == null) k = SortKind.ASC;
            }
            return new ColSort(Integer.parseInt(s), k);
        }
    }

    private List<ColSort> sorts;

    private String scrollY;
    private boolean scrollCollapse;

    private Language language;

    public static enum PagingType {

        /** 'Previous' and 'Next' buttons only */
        SIMPLE("simple"),
        /** 'Previous' and 'Next' buttons, plus page numbers */
        SIMPLE_NUMBERS("simple_numbers"),
        /** 'First', 'Previous', 'Next' and 'Last' buttons */
        FULL("full"),
        /** 'First', 'Previous', 'Next' and 'Last' buttons, plus page numbers */
        FULL_NUMBERS("full_numbers");

        private final String jsName;

        PagingType(String jsName) {
            this.jsName = jsName;
        }

        public String getJsName() {
            return jsName;
        }

        public static PagingType fromJsName(String jsName) {
            if (Empty.is(jsName)) return null;
            for (PagingType v : values()) {
                if (jsName.equalsIgnoreCase(v.getJsName())) return v;
            }
            return null;
        }
    }

    private PagingType pagingType;

    private String ajax;
    private String dataSrc;
    private boolean serverSide;
    private boolean deferRender;
    private boolean processing;
    private boolean stateSave;
    private int stateDuration = 7200;

    public JQMDataTable() {
    }

    @Override
    protected String getHeadGroupsClasses() {
        return headGroupsClasses;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        populateAll();
        enhance();
    }

    @Override
    protected void onUnload() {
        enhanced = false;
        super.onUnload();
    }

    private JsEnhanceParams prepareJsEnhanceParams() {
        JsEnhanceParams p = JsEnhanceParams.create();
        if (!paging) p.setPaging(false);
        if (!lengthChange) p.setLengthChange(false);
        if (pagingType != null) p.setPagingType(pagingType.getJsName());
        if (!info) p.setInfo(false);
        if (!ordering) p.setOrdering(false);
        if (!searching) p.setSearching(false);
        JsSortItems order = prepareJsOrder();
        if (order == null) { // No initial order: https://datatables.net/reference/option/order
            order = JsSortItems.create(null);
        }
        p.setOrder(order);
        JsColumns cols = prepareJsColumns();
        if (cols != null) p.setColumns(cols);
        if (!Empty.is(scrollY)) p.setScrollY(scrollY);
        if (scrollCollapse) p.setScrollCollapse(true);
        if (language != null) {
            JsLanguage l = JsLanguage.create();
            Language.Builder.copy(language, l, true/*nonEmpty*/);
            p.setLanguage(l);
        }
        if (!Empty.is(ajax)) {
            if (dataSrc == null) p.setAjax(ajax);
            else {
                JsAjax aj = JsAjax.create();
                aj.setUrl(ajax);
                aj.setDataSrc(dataSrc);
                p.setAjaxObj(aj);
            }
        }
        if (serverSide) p.setServerSide(true);
        if (deferRender) p.setDeferRender(true);
        if (processing) p.setProcessing(true);
        if (stateSave) {
            p.setStateSave(true);
            p.setStateDuration(stateDuration);
        }
        return p;
    }

    private JsSortItems prepareJsOrder() {
        if (Empty.is(sorts)) return null;
        JsSortItems rslt = null;
        for (ColSort sort : sorts) {
            JsSortItem jsSort = JsSortItem.create(sort.num, sort.kind.getJsName());
            if (rslt == null) rslt = JsSortItems.create(jsSort);
            else rslt.push(jsSort);
        }
        return rslt;
    }

    @SuppressWarnings("null")
    private JsColumns prepareJsColumns() {
        if (Empty.is(datacols)) return null;
        boolean nothing = true;
        JsColumns rslt = JsColumns.create(null);
        for (ColumnDefEx col : datacols) {
            if (col.isGroup()) continue;
            JsColumn jsCol = null;
            if (!Empty.is(col.getName())) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setName(col.getName());
            }
            if (!col.isVisible()) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setVisible(false);
            }
            if (!col.isSearchable()) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setSearchable(false);
            }
            if (!col.isOrderable()) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setOrderable(false);
            }
            if (!Empty.is(col.getClassNames())) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setClassName(col.getClassNames());
            }
            if (col.isCellTypeTh()) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setCellType("th");
            }
            if (!Empty.is(col.getDefaultContent())) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setDefaultContent(col.getDefaultContent());
            }
            if (!Empty.is(col.getWidth())) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setWidth(col.getWidth());
            }
            if (col.getDataIdx() != null) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setDataIdx(col.getDataIdx());
            } else if (col.getData() != null) {
                if (jsCol == null) jsCol = JsColumn.create();
                String s = col.getData();
                jsCol.setData(s.isEmpty() || "null".equals(s) ? null : s);
            }

            if (jsCol != null) nothing = false;
            rslt.push(jsCol);
        }
        if (nothing) return null;
        return rslt;
    }

    private void enhance() {
        if (enhanced) return;
        enhanced = true;
        final Element elt = getElement();
        elt.addClassName("display");
        elt.setAttribute("width", "100%");
        elt.setAttribute("cellspacing", "0"); // obsolete in HTML5, but used in DataTables examples

        JsDataTable.enhance(elt, prepareJsEnhanceParams());
        String wrapId = elt.getId() + "_wrapper";
        Element p = elt.getParentElement();
        while (p != null) {
            if (wrapId.equals(p.getId())) {
                /*  slow! working only when mobileinit sets $.mobile.ignoreContentEnabled = true
                    p.setAttribute("data-enhance", "false");
                 */
                JsDataTable.setDataRoleNone(p); // we don't need jQuery Mobile enhancement for DataTable parts!
                break;
            }
            p = p.getParentElement();
        }
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public PagingType getPagingType() {
        return pagingType;
    }

    public void setPagingType(PagingType pagingType) {
        this.pagingType = pagingType;
    }

    public boolean isInfo() {
        return info;
    }

    /** For paging and searching, like: Showing 1 to 10 of 51 entries (filtered from 57 total entries) */
    public void setInfo(boolean info) {
        this.info = info;
    }

    public boolean isOrdering() {
        return ordering;
    }

    public void setOrdering(boolean ordering) {
        this.ordering = ordering;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public String getScrollY() {
        return scrollY;
    }

    /**
     * Max table's vertical height.
     * <br> Any CSS measurement is acceptable, or just a number which is treated as pixels.
     **/
    public void setScrollY(String scrollY) {
        this.scrollY = scrollY;
    }

    public boolean isScrollCollapse() {
        return scrollCollapse;
    }

    /**
     * @param scrollCollapse - if true then table will match the height of the rows shown
     * if that height is smaller than that given height by the scrollY.
     */
    public void setScrollCollapse(boolean scrollCollapse) {
        this.scrollCollapse = scrollCollapse;
    }

    public String getColSorts() {
        if (enhanced) {
            JsSortItems jsSort = JsDataTable.getOrder(getElement());
            if (jsSort == null || jsSort.length() == 0) {
                colSorts = null;
                return colSorts;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jsSort.length(); i++) {
                JsSortItem item = jsSort.get(i);
                if (i > 0) sb.append(", ");
                sb.append(item.getCol());
                SortKind sk = SortKind.fromJsName(item.getJsSortKind());
                if (SortKind.DESC.equals(sk)) sb.append('=').append(sk.getJsName());
            }
            colSorts = sb.toString();
            return colSorts;
        } else {
            return colSorts;
        }
    }

    /**
     * @param colSorts - expected: 0, 2=desc, 3
     **/
    public void setColSorts(String colSorts) {
        String old = getColSorts();
        if (old == colSorts || old != null && old.equals(colSorts)) return;
        this.colSorts = colSorts;

        if (sorts != null) sorts.clear();
        if (Empty.is(this.colSorts)) {
            if (enhanced) JsDataTable.setOrder(getElement(), null);
            return;
        }
        List<String> lst = StrUtils.commaSplit(this.colSorts);
        for (String i : lst) {
            String s = StrUtils.replaceAllBackslashCommas(i.trim());
            ColSort colSort = ColSort.create(s);
            if (colSort != null) {
                if (sorts == null) sorts = new ArrayList<>();
                sorts.add(colSort);
            }
        }
        if (enhanced) JsDataTable.setOrder(getElement(), prepareJsOrder());
    }

    @UiChild(tagname = "language", limit = 1)
    public void setLanguage(Language value) {
        language = value;
    }

    public Language getLanguage() {
        return language;
    }

    @UiChild(tagname = "column")
    public void addColumn(ColumnDefEx col) {
        if (col == null) return;
        clearHead();
        datacols.add(col); // head will be created later in onLoad()
    }

    @Override
    protected int getNumOfCols() {
        if (loaded && !Empty.is(datacols)) {
            int i = 0;
            for (ColumnDefEx col : datacols) {
                if (!col.isGroup()) i++;
            }
            return i;
        }
        return super.getNumOfCols();
    }

    @Override
    protected boolean isColCellTypeTh(int colIdx) {
        if (loaded && !Empty.is(datacols)) {
            int i = 0;
            for (ColumnDefEx col : datacols) {
                if (!col.isGroup()) {
                    if (i == colIdx) return col.isCellTypeTh();
                    i++;
                }
            }
            return false;
        }
        return super.isColCellTypeTh(colIdx);
    }

    private void populateAll() {
        if (Empty.is(datacols)) return;
        List<ColumnDefEx> row0 = null;
        List<ColumnDefEx> row1 = null;
        for (ColumnDefEx col : datacols) {
            if (col.isRegularInGroupRow() || col.isGroup()) {
                if (row0 == null) row0 = new ArrayList<>();
                row0.add(col);
            } else {
                if (row1 == null) row1 = new ArrayList<>();
                row1.add(col);
            }
        }
        if (row0 != null && !Empty.is(row0)) {
            int i = 0;
            for (ColumnDefEx col : row0) {
                ComplexPanel pa = addToHeadGroups(col, i++);
                if (pa != null && col.hasWidgets()) {
                    List<Widget> lst = col.getWidgets();
                    for (Widget w : lst) pa.add(w);
                }
            }
        }
        if (row1 != null && !Empty.is(row1)) {
            int i = 0;
            for (ColumnDefEx col : row1) {
                ComplexPanel pa = addToHead(col.getTitle(), col.getPriority(), i++);
                if (pa != null && col.hasWidgets()) {
                    List<Widget> lst = col.getWidgets();
                    for (Widget w : lst) pa.add(w);
                }
            }
        }
        refreshBody();
    }

    public String getAjax() {
        return ajax;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }

    public String getDataSrc() {
        return dataSrc;
    }

    /**
     * Defines the property from the data source object (i.e. that returned by the Ajax request) to read.
     * <br> empty string means read data from a plain array rather than an array in an object.
     * <br> See <a href="https://datatables.net/reference/option/ajax.dataSrc">ajax dataSrc</a>
     **/
    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

    public boolean isDeferRender() {
        return deferRender;
    }

    /**
     * @param deferRender - if true then DataTables will create the nodes (rows and cells
     * in the table body) only when they are needed for a draw.
     * <br> See <a href="https://datatables.net/reference/option/deferRender">deferRender</a>
     */
    public void setDeferRender(boolean deferRender) {
        this.deferRender = deferRender;
    }

    public boolean isLengthChange() {
        return lengthChange;
    }

    /** Feature control the end user's ability to change the paging display length of the table. */
    public void setLengthChange(boolean lengthChange) {
        this.lengthChange = lengthChange;
    }

    public boolean isProcessing() {
        return processing;
    }

    /**
     * Enable or disable the display of a 'processing' indicator when the table is being processed
     * (e.g. a sort). This is particularly useful for tables with large amounts of data
     * where it can take a noticeable amount of time to sort the entries.
     */
    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    /** Server-side processing - where filtering, paging and sorting calculations are all performed by a server. */
    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }

    public boolean isStateSave() {
        return stateSave;
    }

    /**
     * When enabled a DataTables will storage state information such as pagination position,
     * display length, filtering and sorting. When the end user reloads the page the table's
     * state will be altered to match what they had previously set up.
     *
     * <br><br> See <a href="https://datatables.net/reference/option/stateSave">stateSave</a>
     */
    public void setStateSave(boolean stateSave) {
        this.stateSave = stateSave;
    }

    public int getStateDuration() {
        return stateDuration;
    }

    /**
     * Value is given in seconds. The value 0 is a special value as it indicates that the state
     * can be stored and retrieved indefinitely with no time limit.
     * <br> When set to -1 sessionStorage will be used, while for 0 or greater localStorage will be used.
     */
    public void setStateDuration(int stateDuration) {
        this.stateDuration = stateDuration;
    }

}
