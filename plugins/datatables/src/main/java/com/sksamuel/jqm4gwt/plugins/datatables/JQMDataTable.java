package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.ArrayList;
import java.util.List;

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
    // then it should be translated to:  "order": [[0, "asc"], [2, "desc"], [3, "asc"]]
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

    private String prepareEnhanceParams() {
        StringBuilder sb = null;
        if (!paging) {
            if (sb == null) sb = new StringBuilder("{");
            sb.append("\"paging\":false");
        }
        if (!info) {
            if (sb == null) sb = new StringBuilder("{"); else sb.append(',');
            sb.append("\"info\":false");
        }
        if (!ordering) {
            if (sb == null) sb = new StringBuilder("{"); else sb.append(',');
            sb.append("\"ordering\":false");
        }
        if (!searching) {
            if (sb == null) sb = new StringBuilder("{"); else sb.append(',');
            sb.append("\"searching\":false");
        }
        String orderArr = prepareOrder();
        if (sb == null) sb = new StringBuilder("{"); else sb.append(',');
        // No initial order: https://datatables.net/reference/option/order
        sb.append("\"order\":").append(Empty.nonEmpty(orderArr, "[]"));

        String cols = prepareColumns();
        if (!Empty.is(cols)) {
            sb.append(',');
            sb.append(cols);
        }

        sb.append('}');
        return sb.toString();
    }

    private String prepareOrder() {
        if (Empty.is(sorts)) return null;
        StringBuilder sb = new StringBuilder("[");
        int j = 0;
        for (ColSort sort : sorts) {
            if (j > 0) sb.append(',');
            sb.append('[');
            sb.append(sort.num).append(",\"").append(sort.kind.getJsName()).append("\"");
            sb.append(']');
            j++;
        }
        sb.append(']');
        return sb.toString();
    }

    private String prepareColumns() {
        if (Empty.is(datacols)) return null;
        boolean nothing = true;
        List<String> lst = new ArrayList<>();
        for (ColumnDefEx col : datacols) {
            if (col.isGroup()) continue;
            StringBuilder sb = null;
            if (!col.isSearchable()) {
                if (sb == null) sb = new StringBuilder("{");
                sb.append("\"searchable\":false");
            }
            if (!col.isOrderable()) {
                if (sb == null) sb = new StringBuilder("{"); else sb.append(',');
                sb.append("\"orderable\":false");
            }
            if (sb == null) lst.add(null);
            else {
                nothing = false;
                sb.append('}');
                lst.add(sb.toString());
            }
        }
        if (nothing) return null;
        StringBuilder sb = new StringBuilder("\"columns\":[");
        int i = 0;
        for (String s : lst) {
            if (i > 0) sb.append(',');
            if (Empty.is(s)) sb.append("null");
            else sb.append(s);
            i++;
        }
        sb.append(']');
        return sb.toString();
    }

    private void enhance() {
        if (enhanced) return;
        enhanced = true;
        final Element elt = getElement();
        elt.addClassName("display");
        elt.setAttribute("width", "100%");
        elt.setAttribute("cellspacing", "0"); // obsolete in HTML5, but used in DataTables examples

        enhance(elt, prepareEnhanceParams());
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

    private static native void enhance(Element elt, String jsonParams) /*-{
        if (jsonParams) $wnd.$(elt).DataTable($wnd.$.parseJSON(jsonParams));
        else $wnd.$(elt).DataTable();
    }-*/;

    private static native void setDataRoleNone(Element elt) /*-{
        var i = $wnd.$(elt);
        i.attr('data-role', 'none');
        i.find('*').attr('data-role', 'none');
    }-*/;

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
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
            if (enhanced) nativeSetOrder(getElement(), prepareOrder());
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
        if (enhanced) nativeSetOrder(getElement(), prepareOrder());
    }

    static class JsSortItem extends JsArrayMixed {
        protected JsSortItem() {}

        public final int getCol() {
            return (int) getNumber(0);
        }

        public final String getJsSortKind() {
            return getString(1);
        }
    }

    static class JsSortItems extends JsArray<JsSortItem> {
        protected JsSortItems() {}
    }

    private static native void nativeSetOrder(Element elt, String jsonOrder) /*-{
        var t = $wnd.$(elt).DataTable();
        if (jsonOrder) t.order($wnd.$.parseJSON(jsonOrder)).draw();
        else t.order.neutral().draw(); // http://datatables.net/plug-ins/api/order.neutral()
    }-*/;

    private static native JsSortItems nativeGetOrder(Element elt) /*-{
        return $wnd.$(elt).DataTable().order();
    }-*/;

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

}
