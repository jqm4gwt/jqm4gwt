package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JsUtils;
import com.sksamuel.jqm4gwt.StrUtils;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMEvent;
import com.sksamuel.jqm4gwt.events.JQMEventFactory;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.JQMOrientationChangeHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.AjaxHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.CellClickHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.CellRender;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.DrawHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsAjax;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsCallback;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsColumn;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsColumns;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsEnhanceParams;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsLanguage;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsLengthMenu;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsRowCallback;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsRowDetails;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsRowSelect;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsSortItem;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsSortItems;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.RowData;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.RowDetailsRenderer;
import com.sksamuel.jqm4gwt.plugins.datatables.events.JQMDataTableEnhancedEvent;
import com.sksamuel.jqm4gwt.plugins.datatables.events.JQMDataTableRowSelChangedEvent;
import com.sksamuel.jqm4gwt.plugins.datatables.events.JQMDataTableRowSelChangedEvent.RowSelChangedData;
import com.sksamuel.jqm4gwt.table.ColumnDef;
import com.sksamuel.jqm4gwt.table.JQMTableGrid;

/**
 * Wrapper for jQuery DataTables.
 * <br> See <a href="https://www.datatables.net/examples/index">DataTables examples</a>
 *
 * @author slavap
 *
 */
public class JQMDataTable extends JQMTableGrid {

    /** Add an ID to the TR element, see <a href="https://datatables.net/examples/server_side/ids.html">
     *  Row ID attributes</a>
     **/
    public static final String DT_ROWID = "DT_RowId";
    public static final String SELECTED_ROW = "selected";

    /** Space separated classes for adding to head groups panel. */
    public static String headGroupsClasses;

    /**
     * For constructing individual column search input placeholders.
     * <br> null - language.search will be used, empty - means no prefix, just footer column title as placeholder.
     **/
    public static String individualColSearchPrefix = null;

    public static interface AjaxPrepare {
        /** Raised right before ajax call to the server. */
        void prepare(JsAjax ajax);
    }

    private AjaxPrepare ajaxPrepare;
    private AjaxHandler ajaxHandler;
    private RowData rowData;
    private CellRender cellRender;

    private boolean enhanced;
    private boolean manualEnhance;

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

    private boolean scrollX;
    private String scrollXcss;
    private String scrollY;
    private int scrollYnum;
    private boolean scrollCollapse;

    private boolean useParentHeight;
    private DrawHandler useParentHeightDrawHandler;
    private HandlerRegistration windowResizeInitialized;
    private HandlerRegistration orientationChangeInitialized;

    private Language language;
    private String languageJSON;

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
    private String lengthMenu;

    private boolean serverSide;
    private String ajax;
    private String dataSrc;

    /**
     * Defines how to make ajax call to server side processing.
     * <br> GET - pass as query params, POST and PUT - pass as form params.
     **/
    public static enum HttpMethod { GET, POST, PUT }

    private HttpMethod httpMethod;

    private boolean deferRender;
    private boolean processing;
    private boolean stateSave;
    private int stateDuration = 7200;
    private boolean autoWidth;

    public static enum RowSelectMode { SINGLE, MULTI }

    private RowSelectMode rowSelectMode;

    private Set<String> serverRowSelected;
    private Set<String> serverRowDetails;

    public static interface RowIdHelper {
        String calcRowId(JQMDataTable table, JavaScriptObject rowData);
    }

    private RowIdHelper rowIdHelper;

    private boolean individualColSearches;

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
        if (!manualEnhance) enhance();
        else JsDataTable.setDataRoleNone(getElement()); // we don't need jQuery Mobile enhancement for DataTable parts!

        if (isAdjustSizesNeeded()) {
            initWindowResize();
            initOrientationChange();
        }
    }

    @Override
    protected void onUnload() {
        if (windowResizeInitialized != null) {
            windowResizeInitialized.removeHandler();
            windowResizeInitialized = null;
        }
        if (orientationChangeInitialized != null) {
            orientationChangeInitialized.removeHandler();
            orientationChangeInitialized = null;
        }

        super.onUnload();
    }

    private JsEnhanceParams prepareJsEnhanceParams() {
        JsEnhanceParams p = JsEnhanceParams.create();
        if (!paging) p.setPaging(false);
        if (!lengthChange) p.setLengthChange(false);
        if (!Empty.is(lengthMenu)) {
            List<String> lst = StrUtils.commaSplit(lengthMenu);
            Map<String, String> keyVal = StrUtils.splitKeyValue(lst);
            if (!Empty.is(keyVal)) {
                boolean allNulls = true;
                for (String v : keyVal.values()) {
                    if (v != null) {
                        allNulls = false;
                        break;
                    }
                }
                if (allNulls) {
                    JsArrayInteger values = JavaScriptObject.createArray(keyVal.size()).cast();
                    int j = 0;
                    for (Entry<String, String> i : keyVal.entrySet()) {
                        values.set(j, Integer.parseInt(i.getKey()));
                        j++;
                    }
                    p.setLengtMenu(JsLengthMenu.create(values));
                } else {
                    JsArrayInteger values = JavaScriptObject.createArray(keyVal.size()).cast();
                    JsArrayString names = JavaScriptObject.createArray(keyVal.size()).cast();
                    int j = 0;
                    for (Entry<String, String> i : keyVal.entrySet()) {
                        values.set(j, Integer.parseInt(i.getKey()));
                        names.set(j, i.getValue() != null ? i.getValue() : i.getKey());
                        j++;
                    }
                    p.setLengtMenu(JsLengthMenu.create(values, names));
                }
            }
        }
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
        if (!Empty.is(scrollXcss)) p.setScrollXcss(scrollXcss);
        else if (scrollX) p.setScrollX(true);
        if (!Empty.is(scrollY)) p.setScrollY(scrollY);
        if (scrollCollapse) p.setScrollCollapse(true);

        JsLanguage l = null;
        if (!Empty.is(languageJSON)) {
            l = JsLanguage.create(languageJSON);
        }
        if (language != null) {
            if (l == null) l = JsLanguage.create();
            Language.Builder.copy(language, l, true/*nonEmpty*/);
        }
        if (l != null) p.setLanguage(l);

        if (ajaxHandler != null) {
            p.setAjaxHandler(getElement(), ajaxHandler);
        } else if (!Empty.is(ajax)) {
            if (dataSrc == null && httpMethod == null && ajaxPrepare == null) {
                p.setAjax(ajax);
            } else {
                JsAjax aj = JsAjax.create();
                aj.setUrl(ajax);
                if (dataSrc != null) aj.setDataSrc(dataSrc);
                if (httpMethod != null) aj.setMethod(httpMethod.name());
                if (ajaxPrepare != null) ajaxPrepare.prepare(aj);
                p.setAjaxObj(aj);
            }
        }
        if (serverSide) {
            p.setServerSide(true);
            p.setRowCallback(new JsRowCallback() {
                @Override
                public void onRow(Element row, JavaScriptObject rowData) {
                    if (!Empty.is(serverRowSelected)) {
                        String rowId = getRowId(rowData);
                        if (!Empty.is(rowId) && serverRowSelected.contains(rowId)) {
                            JsDataTable.initRow(row, true);
                        }
                    }
                }});
            JsDataTable.setRowSelChanged(getElement(), new JsRowSelect() {
                @Override
                public void onRowSelChanged(Element row, boolean selected, JavaScriptObject rowData) {
                    String rowId = getRowId(rowData);
                    if (!Empty.is(rowId)) {
                        if (selected) {
                            if (serverRowSelected == null) serverRowSelected = new HashSet<>();
                            serverRowSelected.add(rowId);
                        } else {
                            if (!Empty.is(serverRowSelected)) serverRowSelected.remove(rowId);
                        }
                    }
                    fireRowSelChanged(row, selected, rowData);
                }});
        } else {
            JsDataTable.setRowSelChanged(getElement(), new JsRowSelect() {
                @Override
                public void onRowSelChanged(Element row, boolean selected, JavaScriptObject rowData) {
                    fireRowSelChanged(row, selected, rowData);
                }});
        }
        if (deferRender) p.setDeferRender(true);
        if (processing) p.setProcessing(true);
        if (stateSave) {
            p.setStateSave(true);
            p.setStateDuration(stateDuration);
        }
        if (!autoWidth) p.setAutoWidth(false);
        return p;
    }

    public HandlerRegistration addRowSelChangedHandler(JQMDataTableRowSelChangedEvent.Handler handler) {
        if (handler == null) return null;
        return addHandler(handler, JQMDataTableRowSelChangedEvent.getType());
    }

    private void fireRowSelChanged(Element row, boolean selected, JavaScriptObject rowData) {
        int cnt = getHandlerCount(JQMDataTableRowSelChangedEvent.getType());
        if (cnt == 0) return;
        JQMDataTableRowSelChangedEvent.fire(this, new RowSelChangedData(row, selected, rowData));
    }

    public HandlerRegistration addEnhancedHandler(JQMDataTableEnhancedEvent.Handler handler) {
        if (handler == null) return null;
        return addHandler(handler, JQMDataTableEnhancedEvent.getType());
    }

    private void fireEnhanced() {
        int cnt = getHandlerCount(JQMDataTableEnhancedEvent.getType());
        if (cnt == 0) return;
        JQMDataTableEnhancedEvent.fire(this, false/*before*/);
    }

    private void fireBeforeEnhance() {
        int cnt = getHandlerCount(JQMDataTableEnhancedEvent.getType());
        if (cnt == 0) return;
        JQMDataTableEnhancedEvent.fire(this, true/*before*/);
    }

    private String getRowId(JavaScriptObject rowData) {
        String rowId = JsUtils.getObjValue(rowData, DT_ROWID);
        if (Empty.is(rowId) && rowIdHelper != null) {
            rowId = rowIdHelper.calcRowId(this, rowData);
        }
        return rowId;
    }

    public AjaxPrepare getAjaxPrepare() {
        return ajaxPrepare;
    }

    /**
     *  Additional options could be specified before making ajax call to the server.
     *  <br> See <a href="http://api.jquery.com/jQuery.ajax/">jQuery.ajax()</a>
     **/
    public void setAjaxPrepare(AjaxPrepare value) {
        ajaxPrepare = value;
    }


    public AjaxHandler getAjaxHandler() {
        return ajaxHandler;
    }

    /**
     * Custom handler for getting data from the server, could be useful in case of client side
     * caching, specific data exchange protocol, ...
     **/
    public void setAjaxHandler(AjaxHandler handler) {
        ajaxHandler = handler;
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

    private JsColumns prepareJsColumns() {
        if (Empty.is(datacols)) {
            if (Empty.is(columns)) return null;
            boolean nothing = true;
            JsColumns rslt = JsColumns.create(null);
            for (ColumnDef col : columns) {
                if (col.isGroup()) continue;
                JsColumn jsCol = null;
                if (!Empty.is(col.getName())) {
                    if (jsCol == null) jsCol = JsColumn.create();
                    jsCol.setName(col.getName());
                }
                if (rowData != null) {
                    if (jsCol == null) jsCol = JsColumn.create();
                    jsCol.setDataFunc(getElement(), rowData);
                }
                if (cellRender != null) {
                    if (jsCol == null) jsCol = JsColumn.create();
                    jsCol.setRenderFunc(getElement(), col, cellRender);
                }
                if (jsCol != null) nothing = false;
                rslt.push(jsCol);
            }
            if (nothing) return null;
            return rslt;
        }
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
            if (col.calcDefaultContent() != null) { // empty is valid value for content
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setDefaultContent(col.calcDefaultContent());
            }
            if (!Empty.is(col.getWidth())) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setWidth(col.getWidth());
            }
            if (rowData != null) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setDataFunc(getElement(), rowData);
            } else if (col.getDataIdx() != null) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setDataIdx(col.getDataIdx());
            } else if (col.getData() != null) { // empty is valid value and means that data is null
                if (jsCol == null) jsCol = JsColumn.create();
                String s = col.getData();
                jsCol.setData(s.isEmpty() || "null".equals(s) ? null : s);
            }
            if (col.isCustomCellRender() && cellRender != null) {
                if (jsCol == null) jsCol = JsColumn.create();
                jsCol.setRenderFunc(getElement(), col, cellRender);
            }

            if (jsCol != null) nothing = false;
            rslt.push(jsCol);
        }
        if (nothing) return null;
        return rslt;
    }

    public boolean isManualEnhance() {
        return manualEnhance;
    }

    public void setManualEnhance(boolean manualEnhance) {
        this.manualEnhance = manualEnhance;
    }

    public void enhance() {
        if (enhanced) return;
        enhanced = true;
        final Element elt = getElement();
        elt.addClassName("display");
        elt.setAttribute("width", "100%");
        elt.setAttribute("cellspacing", "0"); // obsolete in HTML5, but used in DataTables examples

        fireBeforeEnhance(); // some properties can be preset for using in prepareJsEnhanceParams()
        JsEnhanceParams jsParams = prepareJsEnhanceParams();
        jsParams.setInitComplete(new JsCallback() {
            @Override
            public void onSuccess() {
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
                afterEnhance();
                onInitComplete();
                fireEnhanced();
            }
        });
        JsDataTable.enhance(elt, jsParams);
    }

    private void afterEnhance() {
        initRowSelectMode();
        initIndividualColSearches();
    }

    /** Called when DataTable's asynchronous initialization is complete/finished. */
    protected void onInitComplete() {
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

    public boolean isScrollX() {
        return scrollX;
    }

    /** See <a href="https://datatables.net/reference/option/scrollX">scrollX</a> */
    public void setScrollX(boolean scrollX) {
        this.scrollX = scrollX;
    }

    public String getScrollXcss() {
        return scrollXcss;
    }

    /** Value in CSS units.
     * <br> See <a href="https://datatables.net/reference/option/scrollX">scrollX</a>
     **/
    public void setScrollXcss(String scrollXcss) {
        this.scrollXcss = scrollXcss;
    }

    public String getScrollY() {
        return scrollY;
    }

    /**
     * Max table's scrolling area vertical height, so actual table's height will be higher:
     * header + scrolling area + footer.
     * <br> Any CSS measurement is acceptable, or just a number which is treated as pixels.
     **/
    public void setScrollY(String scrollY) {
        this.scrollY = scrollY;
        if (Empty.is(this.scrollY)) this.scrollYnum = 0;
        else {
            String s = StrUtils.getDigitsOnly(this.scrollY);
            if (!Empty.is(s)) this.scrollYnum = Integer.parseInt(s);
            else this.scrollYnum = 0;
        }
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

    public boolean isUseParentHeight() {
        return useParentHeight;
    }

    /** Takes all parent height. Only works when scrollY is set to some value, for example: 1px
     *  <br> And scrollY value will be used as min-height for scrolling area.
     **/
    public void setUseParentHeight(boolean useParentHeight) {
        this.useParentHeight = useParentHeight;
        if (this.useParentHeight) {
            if (useParentHeightDrawHandler == null) {
                useParentHeightDrawHandler = new DrawHandler() {

                    @Override
                    public void afterDraw(Element tableElt, JavaScriptObject settings) {
                        if (JQMDataTable.this.useParentHeight) adjustToParentHeight();
                    }

                    @Override
                    public boolean beforeDraw(Element tableElt, JavaScriptObject settings) {
                        return true;
                    }};
                JsDataTable.addDrawHandler(getElement(), useParentHeightDrawHandler);
            }
            if (loaded) {
                initWindowResize();
                initOrientationChange();
            }
        }
    }

    private void initWindowResize() {
        if (windowResizeInitialized != null) return;
        windowResizeInitialized = Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                if (isAdjustSizesNeeded()) {
                    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            adjustAllSizes();
                        }});
                }
            }});
    }

    private void initOrientationChange() {
        if (orientationChangeInitialized != null) return;
        orientationChangeInitialized = addJQMEventHandler(JQMComponentEvents.ORIENTATIONCHANGE,
                new JQMOrientationChangeHandler() {
                    @Override
                    public void onEvent(JQMEvent<?> event) {
                        if (isAdjustSizesNeeded()) {
                            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                                @Override
                                public void execute() {
                                    adjustAllSizes();
                                }});
                        }
                    }});
    }

    /** Some properties (for example: scrollX, useParentHeight) may require to call final adjustments
     *  when page is completely shown, i.e. it cannot be done automatically by JQMDataTable and
     *  should be resolved manually. */
    public boolean isAdjustSizesNeeded() {
        return isUseParentHeight() || isScrollX() || !Empty.is(scrollXcss);
    }

    public void adjustAllSizes() {
        boolean isScrollX = isScrollX() || !Empty.is(scrollXcss);
        boolean isParentHeight = isUseParentHeight();
        if (isParentHeight) adjustToParentHeight();
        if (isParentHeight || isScrollX) adjustColumnSizing();
    }

    private HandlerRegistration addJQMEventHandler(String jqmEventName, EventHandler handler) {

        Type<EventHandler> t = JQMEventFactory.getType(jqmEventName, EventHandler.class);

        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, jqmEventName, t);
    }

    private static final String SCROLL_BODY = "dataTables_scrollBody";
    private static final String WRAPPER = "dataTables_wrapper";

    /**
     * Adjusts height to parent's height.
     * <br> It's one time action, and works regardless of useParentHeight current value.
     * <br> Only works when scrollY is set to some value, for example: 1px
     * <br> And scrollY value will be used as min-height for scrolling area.
     **/
    public void adjustToParentHeight() {
        Element tableElt = getElement();
        Element wrapper = null;
        Element scrollBody = null;
        Element elt = tableElt.getParentElement();
        while (elt != null) {
            if (scrollBody == null) {
                if (JQMCommon.hasStyle(elt, SCROLL_BODY)) {
                    scrollBody = elt;
                }
            } else if (wrapper == null) {
                if (JQMCommon.hasStyle(elt, WRAPPER)) {
                    wrapper = elt;
                    break;
                }
            }
            elt = elt.getParentElement();
        }
        if (wrapper != null && scrollBody != null) {
            Element wrapParent = wrapper.getParentElement();
            if (wrapParent != null) {
                int h = wrapParent.getClientHeight();
                int wrapH = wrapper.getOffsetHeight();
                String s = scrollBody.getStyle().getHeight();
                s = StrUtils.getDigitsOnly(s);
                if (!Empty.is(s)) {
                    int scrollBodyH = Integer.parseInt(s);
                    int newH = (h - wrapH) + scrollBodyH - 1;
                    if (newH < 0) newH = 0;
                    if (scrollYnum > 0 && newH < scrollYnum) newH = scrollYnum;
                    scrollBody.getStyle().setHeight(newH, Unit.PX);
                }
            }
        }
    }

    /** Aligns header to match the columns, useful after resize or orientation changes. */
    public void adjustColumnSizing() {
        JsDataTable.adjustColumnSizing(getElement());
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

    public String getLanguageJSON() {
        return languageJSON;
    }

    /** If defined will be used as starting point for language initialization.
     *  <br> I.e. languageJSON is parsed first, then language applies/overrides on top of it.
     **/
    public void setLanguageJSON(String languageJSON) {
        this.languageJSON = languageJSON;
    }

    @UiChild(tagname = "column")
    public void addColumn(ColumnDefEx col) {
        if (col == null) return;
        clearHead();
        datacols.add(col); // head will be created later in onLoad() or by refreshColumns()
    }

    /** You have to call refreshColumns() to update head and body (if widget is already loaded). */
    public void setColumns(List<ColumnDefEx> cols) {
        clearHead();
        datacols.clear();
        if (!Empty.is(cols)) datacols.addAll(cols); // head will be created later in onLoad() or by refreshColumns()
    }

    public ColumnDefEx findColumn(String colName) {
        if (Empty.is(colName) || Empty.is(datacols)) return null;
        for (ColumnDefEx col : datacols) {
            if (!col.isGroup()) {
                if (colName.equals(col.getName())) return col;
            }
        }
        return null;
    }

    /** Works dynamically after dataTable is initialized */
    public void setColumnVisible(String colName, boolean visible) {
        ColumnDefEx col = findColumn(colName);
        if (col == null) return;
        col.setVisible(visible);
        JsDataTable.setColVisible(getElement(), colName, visible);
    }

    public ColumnDefEx getColumn(int index) {
        if (index < 0) return null;
        if (!Empty.is(datacols)) {
            if (index >= datacols.size()) return null;
            int i = 0;
            for (ColumnDefEx col : datacols) {
                if (!col.isGroup()) {
                    if (i == index) return col;
                    i++;
                }
            }
        }
        return null;
    }

    public int getColumnCount() {
        if (!Empty.is(datacols)) {
            int i = 0;
            for (ColumnDefEx col : datacols) {
                if (!col.isGroup()) i++;
            }
            return i;
        }
        return 0;
    }

    public String getCellData(int rowIndex, int colIndex) {
        JavaScriptObject cellData = JsDataTable.getCellData(getElement(), rowIndex, colIndex);
        return cellData != null ? cellData.toString() : null;
    }

    public String getCellData(int rowIndex, String colName) {
        JavaScriptObject cellData = JsDataTable.getCellData(getElement(), rowIndex, colName);
        return cellData != null ? cellData.toString() : null;
    }

    public String getColumnsAsTableHtml(int rowIndex, String addAttrsToResult) {
        if (Empty.is(datacols)) return null;
        int colIdx = -1;
        String rslt = "";
        for (ColumnDefEx col : datacols) {
            if (col.isGroup()) continue;
            colIdx++;
            if (Empty.is(col.getData())) continue;
            String v = Empty.nonNull(getCellData(rowIndex, colIdx), "");
            String k = Empty.nonNull(col.getTitle(), "");
            if (Empty.is(k) && Empty.is(v)) continue;
            rslt += "<tr><td>" + k + "</td><td>" + v + "</td></tr>";
        }
        if (Empty.is(rslt)) return null;
        String s = Empty.is(addAttrsToResult) ? "<table>" : "<table " + addAttrsToResult + ">";
        return s + rslt + "</table>";
    }

    @Override
    protected int getNumOfCols() {
        if (loaded && !Empty.is(datacols)) {
            return getColumnCount();
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

    /** Refreshes head and body, needed for example after addColumn(). */
    public void refreshColumns() {
        clearHead();
        tBody.clear();
        populateAll();
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

    private void clearServerRowStructs() {
        if (!Empty.is(serverRowSelected)) serverRowSelected.clear();
        if (!Empty.is(serverRowDetails)) serverRowDetails.clear();
    }

    /**
     * Reload the table data from the ajax data source.
     * <br> It makes sense when we are receiving all data at once from the server side,
     * so calling draw() is not enough in such case, because it will just reuse previously
     * loaded client side data.
     **/
    public void ajaxReload(boolean resetPaging) {
        clearServerRowStructs();
        JsDataTable.ajaxReload(getElement(), resetPaging);
    }

    public void ajaxReload(String newUrl, boolean resetPaging) {
        clearServerRowStructs();
        JsDataTable.ajaxReload(getElement(), newUrl, resetPaging);
    }

    /**
     * Softer than {@link JQMDataTable#ajaxReload(boolean) }, uses already loaded client side data.
     * <br> In case of (serverSide == true) it's practically the same as ajaxReload().
     **/
    public void refreshDraw(boolean resetPaging) {
        JsDataTable.draw(getElement(), resetPaging);
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

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
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

    public String getLengthMenu() {
        return lengthMenu;
    }

    /** Customizes the options shown in the length menu. Example: 10, 25, 50, -1=All */
    public void setLengthMenu(String lengthMenu) {
        this.lengthMenu = lengthMenu;
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

    /** Server-side processing - where filtering, paging and sorting calculations are all performed
     *  by a server. Parameters are passed to server as query params (GET) or form params (POST, PUT).
     *  <br> See <a href="https://datatables.net/manual/server-side">Parameters</a>
     **/
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

    public boolean isAutoWidth() {
        return autoWidth;
    }

    /** See <a href="https://datatables.net/reference/option/autoWidth">autoWidth</a> */
    public void setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    public void addCellBtnClickHandler(CellClickHandler handler) {
        if (handler == null) return;
        JsDataTable.addCellClickHandler(getElement(), ButtonElement.TAG, handler, true);
    }

    public void removeCellBtnClickHandlers() {
        JsDataTable.removeCellClickHandler(getElement(), ButtonElement.TAG);
    }

    /**
     * @param eltSelector - some selector to specific widgets, for example: a.ui-btn.special-btn
     * @param handler - in custom case callback of handler.onClick() may occur
     *                  with undefined rowIndex and colIndex, i.e. -1 values.
     */
    public void addCellCustomClickHandler(CellClickHandler handler, String eltSelector) {
        if (handler == null || Empty.is(eltSelector)) return;
        JsDataTable.addCellClickHandler(getElement(), eltSelector, handler, false);
    }

    public void removeCellCustomClickHandlers(String eltSelector) {
        JsDataTable.removeCellClickHandler(getElement(), eltSelector);
    }

    public void addCellCheckboxClickHandler(CellClickHandler handler) {
        if (handler == null) return;
        JsDataTable.addCellClickHandler(getElement(), InputElement.TAG + "[type='checkbox']",
                                        handler, true);
    }

    public void removeCellCheckboxClickHandlers() {
        JsDataTable.removeCellClickHandler(getElement(), InputElement.TAG + "[type='checkbox']");
    }

    public RowSelectMode getRowSelectMode() {
        return rowSelectMode;
    }

    public void setRowSelectMode(RowSelectMode value) {
        if (rowSelectMode == value) return;
        doneRowSelectMode();
        rowSelectMode = value;
        initRowSelectMode();
    }

    protected void doneRowSelectMode() {
        if (!enhanced || rowSelectMode == null) return;
        if (RowSelectMode.SINGLE.equals(rowSelectMode)) {
            JsDataTable.switchOffSingleRowSelect(getElement());
        } else if (RowSelectMode.MULTI.equals(rowSelectMode)) {
            JsDataTable.switchOffMultiRowSelect(getElement());
        }
    }

    protected void initRowSelectMode() {
        if (!enhanced || rowSelectMode == null) return;
        if (RowSelectMode.SINGLE.equals(rowSelectMode)) {
            JsDataTable.switchOnSingleRowSelect(getElement());
        } else if (RowSelectMode.MULTI.equals(rowSelectMode)) {
            JsDataTable.switchOnMultiRowSelect(getElement());
        }
    }

    public void addRowDetailsRenderer(RowDetailsRenderer renderer) {
        if (renderer == null) return;
        JsDataTable.addRowDetailsRenderer(getElement(), renderer);
        if (serverSide) {
            JsDataTable.setRowDetailsChanged(getElement(), new JsRowDetails() {
                @Override
                public void onChanged(Element row, boolean opened, JavaScriptObject rowData) {
                    String rowId = getRowId(rowData);
                    if (Empty.is(rowId)) return;
                    if (opened) {
                        if (serverRowDetails == null) serverRowDetails = new HashSet<>();
                        serverRowDetails.add(rowId);
                    } else {
                        if (!Empty.is(serverRowDetails)) serverRowDetails.remove(rowId);
                    }
                }
            });
            JsDataTable.addDrawHandler(getElement(), new DrawHandler() {
                @Override
                public void afterDraw(Element tableElt, JavaScriptObject settings) {
                    if (!Empty.is(serverRowDetails)) {
                        JsArrayString rowIds = JavaScriptObject.createArray(serverRowDetails.size()).cast();
                        int i = 0;
                        for (String s : serverRowDetails) {
                            rowIds.set(i, s);
                            i++;
                        }
                        JsDataTable.openRowDetails(tableElt, rowIds);
                    }
                }

                @Override
                public boolean beforeDraw(Element tableElt, JavaScriptObject settings) {
                    return true;
                }
            });
        }
    }

    public void closeRowDetails(Element rowDetailElt) {
        JsDataTable.closeRowDetails(getElement(), rowDetailElt);
    }

    /** See <a href="https://datatables.net/reference/event/draw">Draw event</a> */
    public void addDrawHandler(DrawHandler handler) {
        if (handler == null) return;
        JsDataTable.addDrawHandler(getElement(), handler);
    }

    /** Makes no much sense when serverSide is true, use getSelRowIds() in that case. */
    public JsArrayInteger getSelRowIndexes() {
        return JsDataTable.getSelRowIndexes(getElement());
    }

    /** Makes no much sense when serverSide is true, use getSelRowIds() in that case. */
    public JsArray<JavaScriptObject> getSelRowDatas() {
        return JsDataTable.getSelRowDatas(getElement());
    }

    /** Works when serverSide is true. */
    public Set<String> getSelRowIds() {
        return serverRowSelected;
    }

    public void unselectAllRows() {
        JsDataTable.unselectAllRows(getElement());
    }

    /** @param cellOrRowElt - could be cellElt or rowElt */
    public void changeRow(Element cellOrRowElt, boolean selected) {
        JsDataTable.changeRow(cellOrRowElt, selected);
    }

    /** @param cellOrRowElt - could be cellElt or rowElt */
    public void selectOneRowOnly(Element cellOrRowElt) {
        JsDataTable.selectOneRowOnly(cellOrRowElt);
    }

    public JavaScriptObject getData() {
        return JsDataTable.getData(getElement());
    }

    public void clearData() {
        JsDataTable.clearData(getElement());
    }

    public void addRow(JavaScriptObject newRow) {
        JsDataTable.addRow(getElement(), newRow);
    }

    public void removeRow(int rowIndex) {
        JsDataTable.removeRow(getElement(), rowIndex);
    }

    public void removeSelRows() {
        JsArrayInteger sel = JsDataTable.getSelRowIndexes(getElement());
        if (sel.length() == 0) return;
        int[] idxs = new int[sel.length()];
        for (int i = 0; i < idxs.length; i++) idxs[i] = sel.get(i);
        Arrays.sort(idxs);
        for (int i = idxs.length - 1; i >= 0; i--) {
            removeRow(idxs[i]);
        }
        rowsInvalidate(true);
    }

    public void rowsInvalidate(boolean resetPaging) {
        JsDataTable.rowsInvalidate(getElement(), resetPaging);
    }

    public RowIdHelper getRowIdHelper() {
        return rowIdHelper;
    }

    /** Could be useful when we need selection support for server side mode, but server doesn't
     *  provide DT_RowId in data for some reason.
     **/
    public void setRowIdHelper(RowIdHelper rowIdHelper) {
        this.rowIdHelper = rowIdHelper;
    }

    public boolean isIndividualColSearches() {
        return individualColSearches;
    }

    /** You must define footer column titles to get this property working, i.e. for each non-empty
     *  title search input widget will be auto-generated.
     **/
    public void setIndividualColSearches(boolean individualColSearches) {
        this.individualColSearches = individualColSearches;
    }

    protected void initIndividualColSearches() {
        if (!enhanced || !individualColSearches || tFoot == null) return;
        JsDataTable.createFooterIndividualColumnSearches(getElement(), individualColSearchPrefix);
    }

    public RowData getRowData() {
        return rowData;
    }

    /** Custom data accessor, useful in case non-JavaScriptObject data structure, i.e. DTO/POJO. */
    public void setRowData(RowData rowData) {
        this.rowData = rowData;
    }

    public CellRender getCellRender() {
        return cellRender;
    }

    /**
     * Custom widget can be inserted into any cell.
     */
    public void setCellRender(CellRender cellRender) {
        this.cellRender = cellRender;
    }

    public void clearSearch() {
        JsDataTable.clearSearch(getElement());
    }

}
