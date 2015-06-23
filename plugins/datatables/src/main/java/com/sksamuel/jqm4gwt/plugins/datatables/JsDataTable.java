package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.dom.client.Element;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.StrUtils;

public class JsDataTable {

    private JsDataTable() {} // static class

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

    static native void setOrder(Element elt, JsSortItems sorts) /*-{
        var t = $wnd.$(elt).DataTable();
        if (sorts) t.order(sorts).draw();
        else t.order.neutral().draw(); // http://datatables.net/plug-ins/api/order.neutral()
    }-*/;

    static native JsSortItems getOrder(Element elt) /*-{
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

    static native void enhance(Element elt, JsEnhanceParams params) /*-{
        if (params) $wnd.$(elt).DataTable(params); // $wnd.$.parseJSON(jsonParams)
        else $wnd.$(elt).DataTable();
    }-*/;

    static native void setDataRoleNone(Element elt) /*-{
        var i = $wnd.$(elt);
        i.attr('data-role', 'none');
        i.find('*').attr('data-role', 'none');
    }-*/;

}
