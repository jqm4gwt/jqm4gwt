package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
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

    private boolean paging = true;
    private boolean info = true; // for paging and searching, like: Showing 1 to 10 of 51 entries (filtered from 57 total entries)
    private boolean ordering = true;
    private boolean searching = true;

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
            if (jsName == null || jsName.isEmpty()) return null;
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
            if (s == null || s.isEmpty()) return null;
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
        enhance();
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
        if (sorts != null && !sorts.isEmpty()) {
            if (sb == null) sb = new StringBuilder("{"); else sb.append(',');
            sb.append("\"order\":[");
            int j = 0;
            for (ColSort sort : sorts) {
                if (j > 0) sb.append(',');
                sb.append('[');
                sb.append(sort.num).append(",\"").append(sort.kind.getJsName()).append("\"");
                sb.append(']');
                j++;
            }
            sb.append(']');
        }
        if (sb == null) return null;
        sb.append('}');
        return sb.toString();
    }

    private void enhance() {
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
        return colSorts;
    }

    /**
     * @param colSorts - expected: 0, 2=desc, 3
     **/
    public void setColSorts(String colSorts) {
        if (this.colSorts == colSorts || this.colSorts != null && this.colSorts.equals(colSorts)) return;
        this.colSorts = colSorts;

        if (sorts != null) sorts.clear();
        if (this.colSorts == null || this.colSorts.isEmpty()) return;
        String[] arr = StrUtils.commaSplit(this.colSorts);
        for (int i = 0; i < arr.length; i++) {
            String s = StrUtils.replaceAllBackslashCommas(arr[i].trim());
            ColSort colSort = ColSort.create(s);
            if (colSort != null) {
                if (sorts == null) sorts = new ArrayList<>();
                sorts.add(colSort);
            }
        }
    }

}
