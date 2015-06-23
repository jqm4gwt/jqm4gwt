package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.StrUtils;
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
    private boolean info = true; // for paging and searching, like: Showing 1 to 10 of 51 entries (filtered from 57 total entries)
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
                jsCol.setDataIdx(col.getDataIdx());
            } else if (col.getData() != null) {
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

        enhance(elt, prepareJsEnhanceParams());
        String wrapId = elt.getId() + "_wrapper";
        Element p = elt.getParentElement();
        while (p != null) {
            if (wrapId.equals(p.getId())) {
                /*  slow! working only when mobileinit sets $.mobile.ignoreContentEnabled = true
                    p.setAttribute("data-enhance", "false");
                 */
                setDataRoleNone(p); // we don't need jQuery Mobile enhancement for DataTable parts!
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
            JsSortItems jsSort = nativeGetOrder(getElement());
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
            if (enhanced) nativeSetOrder(getElement(), null);
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
        if (enhanced) nativeSetOrder(getElement(), prepareJsOrder());
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

    static class JsSortItem extends JsArrayMixed {

        protected JsSortItem() {}

        public static native JsSortItem create(int col, String jsSortKind) /*-{
            return [col, jsSortKind];
        }-*/;

        public final int getCol() {
            return (int) getNumber(0);
        }

        public final void setCol(int value) {
            set(0, value);
        }

        public final String getJsSortKind() {
            return getString(1);
        }

        public final void setJsSortKind(String value) {
            set(1, value);
        }
    }

    static class JsSortItems extends JsArray<JsSortItem> {
        protected JsSortItems() {}

        /** @param item - if null returns empty array */
        public static native JsSortItems create(JsSortItem item) /*-{
            if(item) return [item];
            else return [];
        }-*/;
    }

    private static native void nativeSetOrder(Element elt, JsSortItems sorts) /*-{
        var t = $wnd.$(elt).DataTable();
        if (sorts) t.order(sorts).draw();
        else t.order.neutral().draw(); // http://datatables.net/plug-ins/api/order.neutral()
    }-*/;

    private static native JsSortItems nativeGetOrder(Element elt) /*-{
        return $wnd.$(elt).DataTable().order();
    }-*/;

    static class JsColumn extends JavaScriptObject {

        protected JsColumn() {}

        public static native JsColumn create() /*-{
            return {};
        }-*/;

        public final native String getName() /*-{
            return this.name;
        }-*/;

        public final native void setName(String value) /*-{
            this.name = value;
        }-*/;

        public final native boolean getVisible() /*-{
            return this.visible;
        }-*/;

        public final native void setVisible(boolean value) /*-{
            this.visible = value;
        }-*/;

        public final native boolean getOrderable() /*-{
            return this.orderable;
        }-*/;

        public final native void setOrderable(boolean value) /*-{
            this.orderable = value;
        }-*/;

        public final native boolean getSearchable() /*-{
            return this.searchable;
        }-*/;

        public final native void setSearchable(boolean value) /*-{
            this.searchable = value;
        }-*/;

        public final native String getClassName() /*-{
            return this.className;
        }-*/;

        public final native void setClassName(String value) /*-{
            this.className = value;
        }-*/;

        public final native String getCellType() /*-{
            return this.cellType;
        }-*/;

        public final native void setCellType(String value) /*-{
            this.cellType = value;
        }-*/;

        public final native String getDefaultContent() /*-{
            return this.defaultContent;
        }-*/;

        public final native void setDefaultContent(String value) /*-{
            this.defaultContent = value;
        }-*/;

        public final native String getWidth() /*-{
            return this.width;
        }-*/;

        public final native void setWidth(String value) /*-{
            this.width = value;
        }-*/;

        public final native String getData() /*-{
            return this.data;
        }-*/;

        public final native void setData(String value) /*-{
            this.data = value;
        }-*/;

        public final native int getDataIdx() /*-{
            if (typeof this.data === 'number') return this.data;
            else return -1;
        }-*/;

        public final native void setDataIdx(int value) /*-{
            this.data = value;
        }-*/;
    }

    static class JsColumns extends JsArray<JsColumn> {
        protected JsColumns() {}

        /** @param item - if null returns empty array */
        public static native JsColumns create(JsColumn item) /*-{
            if(item) return [item];
            else return [];
        }-*/;
    }

    /** See <a href="https://datatables.net/reference/option/#Internationalisation">Internationalisation</a> */
    static class JsLanguage extends JavaScriptObject implements Language {

        protected JsLanguage() {}

        public static native JsLanguage create() /*-{
            return {};
        }-*/;

        @Override
        public final native String getDecimal() /*-{
            return this.decimal;
        }-*/;

        @Override
        public final native void setDecimal(String value) /*-{
            this.decimal = value;
        }-*/;

        @Override
        public final native String getThousands() /*-{
            return this.thousands;
        }-*/;

        @Override
        public final native void setThousands(String value) /*-{
            this.thousands = value;
        }-*/;

        @Override
        public final native String getLengthMenu() /*-{
            return this.lengthMenu;
        }-*/;

        @Override
        public final native void setLengthMenu(String value) /*-{
            this.lengthMenu = value;
        }-*/;

        @Override
        public final native String getZeroRecords() /*-{
            return this.zeroRecords;
        }-*/;

        @Override
        public final native void setZeroRecords(String value) /*-{
            this.zeroRecords = value;
        }-*/;

        @Override
        public final native String getInfo() /*-{
            return this.info;
        }-*/;

        @Override
        public final native void setInfo(String value) /*-{
            this.info = value;
        }-*/;

        @Override
        public final native String getInfoEmpty() /*-{
            return this.infoEmpty;
        }-*/;

        @Override
        public final native void setInfoEmpty(String value) /*-{
            this.infoEmpty = value;
        }-*/;

        @Override
        public final native String getInfoFiltered() /*-{
            return this.infoFiltered;
        }-*/;

        @Override
        public final native void setInfoFiltered(String value) /*-{
            this.infoFiltered = value;
        }-*/;

        @Override
        public final native String getLoadingRecords() /*-{
            return this.loadingRecords;
        }-*/;

        @Override
        public final native void setLoadingRecords(String value) /*-{
            this.loadingRecords = value;
        }-*/;

        @Override
        public final native String getProcessing() /*-{
            return this.processing;
        }-*/;

        @Override
        public final native void setProcessing(String value) /*-{
            this.processing = value;
        }-*/;

        @Override
        public final native String getSearch() /*-{
            return this.search;
        }-*/;

        @Override
        public final native void setSearch(String value) /*-{
            this.search = value;
        }-*/;

        @Override
        public final native String getSearchPlaceholder() /*-{
            return this.searchPlaceholder;
        }-*/;

        @Override
        public final native void setSearchPlaceholder(String value) /*-{
            this.searchPlaceholder = value;
        }-*/;

        @Override
        public final native String getUrl() /*-{
            return this.url;
        }-*/;

        @Override
        public final native void setUrl(String value) /*-{
            this.url = value;
        }-*/;

        @Override
        public final String getPaginate() {
            return getPaginateStr();
        }

        @Override
        public final void setPaginate(String value) {
            setPaginateStr(value);
            if (!Empty.is(value)) {
                List<String> lst = StrUtils.commaSplit(value);
                String[] arr = new String[] { null, null, null, null };
                if (lst != null) {
                    for (int i = 0; i < lst.size(); i++) {
                        if (i >= arr.length) break;
                        arr[i] = StrUtils.replaceAllBackslashCommas(lst.get(i).trim());
                    }
                }
                nativeSetPaginate(arr[0], arr[1], arr[2], arr[3]);
            } else {
                nativeSetPaginate(null, null, null, null);
            }
        }

        private final native String getPaginateStr() /*-{
            return this.paginateStr;
        }-*/;

        private final native void setPaginateStr(String value) /*-{
            this.paginateStr = value;
        }-*/;

        private final native void nativeSetPaginate(String sPrev, String sNext,
                                                    String sFirst, String sLast) /*-{
            var empty = true;
            var p = {};
            if (sPrev) {
                empty = false;
                p.previous = sPrev;
            }
            if (sNext) {
                empty = false;
                p.next = sNext;
            }
            if (sFirst) {
                empty = false;
                p.first = sFirst;
            }
            if (sLast) {
                empty = false;
                p.last = sLast;
            }
            if (!empty) this.paginate = p;
        }-*/;
    }

    static class JsEnhanceParams extends JavaScriptObject {

        protected JsEnhanceParams() {}

        public static native JsEnhanceParams create() /*-{
            return {};
        }-*/;

        public final native boolean getPaging() /*-{
            return this.paging;
        }-*/;

        public final native void setPaging(boolean value) /*-{
            this.paging = value;
        }-*/;

        public final native boolean getInfo() /*-{
            return this.info;
        }-*/;

        public final native void setInfo(boolean value) /*-{
            this.info = value;
        }-*/;

        public final native boolean getOrdering() /*-{
            return this.ordering;
        }-*/;

        public final native void setOrdering(boolean value) /*-{
            this.ordering = value;
        }-*/;

        public final native boolean getSearching() /*-{
            return this.searching;
        }-*/;

        public final native void setSearching(boolean value) /*-{
            this.searching = value;
        }-*/;

        public final native JsSortItems getOrder() /*-{
            return this.order;
        }-*/;

        public final native void setOrder(JsSortItems value) /*-{
            this.order = value;
        }-*/;

        public final native JsColumns getColumns() /*-{
            return this.columns;
        }-*/;

        public final native void setColumns(JsColumns value) /*-{
            this.columns = value;
        }-*/;

        public final native String getScrollY() /*-{
            return this.scrollY;
        }-*/;

        public final native void setScrollY(String value) /*-{
            this.scrollY = value;
        }-*/;

        public final native boolean getScrollCollapse() /*-{
            return this.scrollCollapse;
        }-*/;

        public final native void setScrollCollapse(boolean value) /*-{
            this.scrollCollapse = value;
        }-*/;

        public final native JsLanguage getLanguage() /*-{
            return this.language;
        }-*/;

        public final native void setLanguage(JsLanguage value) /*-{
            this.language = value;
        }-*/;

        public final native String getPagingType() /*-{
            return this.pagingType;
        }-*/;

        public final native void setPagingType(String value) /*-{
            this.pagingType = value;
        }-*/;

    }

    private static native void enhance(Element elt, JsEnhanceParams params) /*-{
        if (params) $wnd.$(elt).DataTable(params); // $wnd.$.parseJSON(jsonParams)
        else $wnd.$(elt).DataTable();
    }-*/;

    private static native void setDataRoleNone(Element elt) /*-{
        var i = $wnd.$(elt);
        i.attr('data-role', 'none');
        i.find('*').attr('data-role', 'none');
    }-*/;

}
