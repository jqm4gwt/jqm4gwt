package com.sksamuel.jqm4gwt.examples.datatables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.HttpUtils;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JsUtils;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.html.Span;
import com.sksamuel.jqm4gwt.plugins.datatables.ColumnDefEx;
import com.sksamuel.jqm4gwt.plugins.datatables.JQMDataTable;
import com.sksamuel.jqm4gwt.plugins.datatables.JQMDataTable.RowIdHelper;
import com.sksamuel.jqm4gwt.plugins.datatables.JQMDataTable.RowSelectMode;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.AjaxHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.CellClickHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.CellRender;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.DrawHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsAjaxRequest;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsAjaxResponse;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsColItem;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsColItems;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsOrderItems;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.JsRowDataMetaInfo;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.RowData;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.RowDetailsRenderer;
import com.sksamuel.jqm4gwt.plugins.datatables.events.JQMDataTableRowSelChangedEvent;
import com.sksamuel.jqm4gwt.table.ColumnDef;

public class DataTableExamplesPage {

    private static DataTableExamplesPageUiBinder uiBinder = GWT
            .create(DataTableExamplesPageUiBinder.class);

    interface DataTableExamplesPageUiBinder extends UiBinder<JQMPage, DataTableExamplesPage> {
    }

    private JQMPage page;

    @UiField
    JQMDataTable dataTable1;

    @UiField
    JQMDataTable dataTable2;

    @UiField
    JQMDataTable dataTable3;

    @UiField
    JQMDataTable dataTable4;

    @UiField
    JQMDataTable dataTable5;

    @UiField
    JQMDataTable dataTable6;

    @UiField
    JQMButton btnUnselectAll;

    @UiField
    JQMButton btnGetSelected;

    @UiField
    JQMButton btnReplaceData;

    @UiField
    JQMButton btnDeleteSelRow;

    @UiField
    JQMButton btnClearSearch;

    @UiField
    Span selRowsInfo;

    private static JsArray<JsArrayMixed> dataArray = null;
    private static JsArray<JavaScriptObject> dataObjs = null;

    private static class TestDataItem {
        public final int id;
        public final String code;
        public final String name;

        public TestDataItem(int id, String code, String name) {
            this.id = id;
            this.code = code;
            this.name = name;
        }

        public String getValStr(String fieldName) {
            if (Empty.is(fieldName)) return null;
            switch (fieldName) {
            case "id":
                return String.valueOf(id);
            case "code":
                return code;
            case "name":
                return name;
            default:
                return null;
            }
        }
    }

    private static final List<TestDataItem> testDataItems = new ArrayList<>();

    static {
        testDataItems.add(new TestDataItem( 1, "aaa", "Alpha"));
        testDataItems.add(new TestDataItem( 2, "bbb", "Beta"));
        testDataItems.add(new TestDataItem( 3, "ccc", "Claw"));
        testDataItems.add(new TestDataItem( 4, "ddd", "Draw"));
        testDataItems.add(new TestDataItem( 5, "eee", "Effel"));
        testDataItems.add(new TestDataItem( 6, "fff", "Fork"));
        testDataItems.add(new TestDataItem( 7, "ggg", "Glow"));
        testDataItems.add(new TestDataItem( 8, "hhh", "Halo"));
        testDataItems.add(new TestDataItem( 9, "iii", "Irish"));
        testDataItems.add(new TestDataItem(10, "jjj", "Jerk"));
        testDataItems.add(new TestDataItem(11, "kkk", "Key"));
        testDataItems.add(new TestDataItem(12, "lll", "Load"));
        testDataItems.add(new TestDataItem(13, "mmm", "Mars"));
        testDataItems.add(new TestDataItem(14, "nnn", "Night"));
        testDataItems.add(new TestDataItem(15, "ooo", "Ork"));
        testDataItems.add(new TestDataItem(16, "ppp", "Park"));
        testDataItems.add(new TestDataItem(17, "qqq", "Quick"));
        testDataItems.add(new TestDataItem(18, "rrr", "Road"));
        testDataItems.add(new TestDataItem(19, "sss", "Salt"));
        testDataItems.add(new TestDataItem(20, "ttt", "Toad"));
        testDataItems.add(new TestDataItem(21, "uuu", "Uranus"));
        testDataItems.add(new TestDataItem(22, "vvv", "Vortex"));
        testDataItems.add(new TestDataItem(23, "www", "Work"));
        testDataItems.add(new TestDataItem(24, "xxx", "Xonix"));
        testDataItems.add(new TestDataItem(25, "yyy", "York"));
        testDataItems.add(new TestDataItem(26, "zzz", "Zerg"));
    }

    public DataTableExamplesPage() {
        page = uiBinder.createAndBindUi(this);
        dataTable2.addCellBtnClickHandler(new CellClickHandler() {
            @Override
            public boolean onClick(Element cellElt, JavaScriptObject rowData, int rowIndex) {
                String s = dataTable2.getCellData(rowIndex, "name");
                //Element tableElt = JsDataTable.findTableElt(cellElt);
                //s += "\n\n" + tableElt.getInnerHTML();
                Window.alert(s);
                if (RowSelectMode.SINGLE.equals(dataTable2.getRowSelectMode())) {
                    dataTable2.selectOneRowOnly(cellElt);
                } else if (RowSelectMode.MULTI.equals(dataTable2.getRowSelectMode())) {
                    dataTable2.changeRow(cellElt, true);
                }
                return true;
            }
        });
        final CellClickHandler checkBoxClickHandler = new CellClickHandler() {
            @Override
            public boolean onClick(Element cellElt, JavaScriptObject rowData, int rowIndex) {
                Element tableElt = JsDataTable.findTableElt(cellElt);
                JQMDataTable dataTable = (JQMDataTable) JQMCommon.findWidget(tableElt);
                InputElement cb = cellElt.cast();
                if (cb.isChecked()) {
                    if (RowSelectMode.SINGLE.equals(dataTable.getRowSelectMode())) {
                        dataTable.selectOneRowOnly(cb);
                    } else if (RowSelectMode.MULTI.equals(dataTable.getRowSelectMode())) {
                        dataTable.changeRow(cb, true);
                    }
                } else if (dataTable.getRowSelectMode() != null) {
                    dataTable.changeRow(cb, false);
                }
                return true;
            }
        };
        dataTable2.addCellCheckboxClickHandler(checkBoxClickHandler);
        dataTable2.addRowDetailsRenderer(new RowDetailsRenderer() {
            @Override
            public String getHtml(Element tableElt, JavaScriptObject rowData, int rowIndex) {
                return dataTable2.getColumnsAsTableHtml(rowIndex, "border='0' style='padding-left:50px;'");
            }
        });
        dataTable2.addRowSelChangedHandler(new JQMDataTableRowSelChangedEvent.Handler() {
            @Override
            public void onRowSelChanged(JQMDataTableRowSelChangedEvent event) {
                JsArrayInteger sel = dataTable2.getSelRowIndexes();
                if (sel.length() == 0) selRowsInfo.setText("No rows selected");
                else {
                    if (sel.length() > 7) {
                        dataTable2.changeRow(event.getData().row, false/*selected*/);
                        sel = dataTable2.getSelRowIndexes();
                        String s = JsUtils.getObjValue(event.getData().rowData, "name");
                        Window.alert("Cannot select more than 7 records. Ignoring selection of: " + s);
                    }
                    selRowsInfo.setText("Selected: " + sel.length() + " rows");
                }
            }
        });
        btnUnselectAll.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (dataTable2.getRowSelectMode() != null) {
                    dataTable2.unselectAllRows();
                }
            }
        });
        btnGetSelected.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                JsArrayInteger sel = dataTable2.getSelRowIndexes();
                if (sel.length() == 0) Window.alert("No rows selected");
                else {
                    String msg = "";
                    for (int i = 0; i < sel.length(); i++) {
                        String s = dataTable2.getCellData(sel.get(i), "name");
                        msg += s + "\n";
                    }
                    Window.alert(msg);
                }
            }
        });
        btnClearSearch.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dataTable2.clearSearch();
            }
        });
        btnReplaceData.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                JsArrayMixed d = dataTable3.getData().cast();
                int len = d.length();
                /*if (len > 0) {
                    JsArrayMixed r0 = d.getObject(0);
                    Window.alert("Total: " + String.valueOf(len) + " rows.\n" + r0.getString(0));
                } else {
                    Window.alert("Empty table.");
                }*/

                dataTable3.clearData();
                for (int i = 0; i <= len; i++) {
                    JsArrayMixed r = createDataRow(i);
                    dataTable3.addRow(r);
                }
                dataTable3.rowsInvalidate(true);
            }
        });
        btnDeleteSelRow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dataTable3.removeSelRows();
            }
        });

        dataTable4.setRowIdHelper(new RowIdHelper() {
            @Override
            public String calcRowId(JQMDataTable table, JavaScriptObject rowData) {
                JsArrayMixed r = rowData.cast();
                String s = "";
                for (int i = 0; i < r.length(); i++) {
                    s += r.getString(i);
                }
                return s;
            }
        });
        dataTable4.setAjaxHandler(new AjaxHandler() {
            @Override
            public void getData(final Element tableElt, final JavaScriptObject request,
                                final JavaScriptObject drawCallback) {
                if (dataArray == null) {
                    HttpUtils.httpGet("data/array.json", new Callback<String, String>() {

                        @Override
                        public void onSuccess(String result) {
                            JavaScriptObject obj = JsUtils.jsonParse(result);
                            dataArray = JsUtils.getNestedObjValue(obj, "data").cast();
                            getArrayServerData((JsAjaxRequest) request, drawCallback);
                        }

                        @Override
                        public void onFailure(String reason) {
                            Window.alert(reason);
                        }
                    });
                } else {
                    getArrayServerData((JsAjaxRequest) request, drawCallback);
                }
            }
        });
        dataTable4.enhance();

        dataTable5.addCellBtnClickHandler(new CellClickHandler() {
            @Override
            public boolean onClick(Element cellElt, JavaScriptObject rowData, int rowIndex) {
                String s = dataTable5.getCellData(rowIndex, "name");
                Window.alert(s);
                if (RowSelectMode.SINGLE.equals(dataTable5.getRowSelectMode())) {
                    dataTable5.selectOneRowOnly(cellElt);
                } else if (RowSelectMode.MULTI.equals(dataTable5.getRowSelectMode())) {
                    dataTable5.changeRow(cellElt, true);
                }
                return true;
            }
        });
        dataTable5.addCellCheckboxClickHandler(checkBoxClickHandler);
        dataTable5.addRowDetailsRenderer(new RowDetailsRenderer() {
            @Override
            public String getHtml(Element tableElt, JavaScriptObject rowData, int rowIndex) {
                return dataTable5.getColumnsAsTableHtml(rowIndex, "border='0' style='padding-left:50px;'");
            }
        });
        dataTable5.setAjaxHandler(new AjaxHandler() {
            @Override
            public void getData(final Element tableElt, final JavaScriptObject request,
                                final JavaScriptObject drawCallback) {
                if (dataObjs == null) {
                    HttpUtils.httpGet("data/nested-objects.json", new Callback<String, String>() {

                        @Override
                        public void onSuccess(String result) {
                            JavaScriptObject obj = JsUtils.jsonParse(result);
                            dataObjs = JsUtils.getNestedObjValue(obj, "data").cast();
                            JsAjaxRequest req = (JsAjaxRequest) request;
                            generateRowIds(req.getColumns());
                            getObjsServerData(req, drawCallback);
                        }

                        @Override
                        public void onFailure(String reason) {
                            Window.alert(reason);
                        }
                    });
                } else {
                    getObjsServerData((JsAjaxRequest) request, drawCallback);
                }
            }
        });
        dataTable5.addDrawHandler(new DrawHandler() {
            @Override
            public void afterDraw(Element tableElt, JavaScriptObject settings) {
                JsArray<Element> rows = JsDataTable.doGrouping(settings, 4);
                for (int i = 0; i < rows.length(); i++) {
                    Element r = rows.get(i);
                    r.getStyle().setFontWeight(FontWeight.BOLD);
                }
            }

            @Override
            public boolean beforeDraw(Element tableElt, JavaScriptObject settings) {
                return true;
            }
        });
        dataTable5.enhance();

        dataTable6.addCellCheckboxClickHandler(checkBoxClickHandler);
        dataTable6.addRowDetailsRenderer(new RowDetailsRenderer() {
            @Override
            public String getHtml(Element tableElt, JavaScriptObject rowData, int rowIndex) {
                return dataTable6.getColumnsAsTableHtml(rowIndex, "border='0' style='padding-left:50px;'");
            }
        });
        dataTable6.setAjaxHandler(new AjaxHandler() {
            @Override
            public void getData(final Element tableElt, final JavaScriptObject request,
                                final JavaScriptObject drawCallback) {
                getJavaServerData((JQMDataTable) JQMCommon.findWidget(tableElt),
                        (JsAjaxRequest) request, drawCallback);
            }
        });
        dataTable6.setRowData(new RowData() {
            private final JsArrayMixed holder = JavaScriptObject.createArray(1).cast();

            @Override
            public JsArrayMixed onData(Element tableElt, JavaScriptObject rowData, String opType,
                                       JavaScriptObject setVal, JavaScriptObject metaInfo) {
                JsRowDataMetaInfo meta = metaInfo.cast();
                Widget w = JQMCommon.findWidget(tableElt);
                ColumnDefEx col = ((JQMDataTable) w).getColumn(meta.getCol());
                if (Empty.is(col.getData())) {
                    holder.set(0, (JavaScriptObject) null);
                    return holder;
                }
                TestDataItem item = (TestDataItem) JsUtils.getNestedObjJavaValue(rowData, "dataItem");
                switch (col.getData()) {
                case "id":
                    holder.set(0, item.id);
                    break;
                case "code":
                    holder.set(0, item.code);
                    break;
                case "name":
                    holder.set(0, item.name);
                    break;
                default:
                    holder.set(0, (JavaScriptObject) null);
                    break;
                }
                return holder;
            }
        });
        dataTable6.setCellRender(new CellRender() {
            @Override
            public String onRender(Element tableElt, ColumnDef col, JsArrayMixed cellData,
                    JavaScriptObject rowData, String opType, JavaScriptObject metaInfo) {
                if (!(col instanceof ColumnDefEx)) return null;
                ColumnDefEx colEx = (ColumnDefEx) col;
                if ("code".equals(colEx.getData())) {
                    TestDataItem item = (TestDataItem) JsUtils.getNestedObjJavaValue(rowData, "dataItem");
                    String s = "<input type='text' value='" + cellData.getString(0)
                        + "' data-rowid='" + item.id
                        + "' data-column='" + colEx.getData()
                        + "' data-initval='" + item.code + "'>";
                    return s;
                }
                return null;
            }});
        dataTable6.addDrawHandler(new DrawHandler() {
            @Override
            public void afterDraw(Element tableElt, JavaScriptObject settings) {
            }

            @Override
            public boolean beforeDraw(Element tableElt, JavaScriptObject settings) {
                JsArray<Element> arr = getTableElts(tableElt, "input[type=text]");
                String s = "";
                for (int i = 0; i < arr.length(); i++) {
                    Element elt = arr.get(i);
                    String oldVal = elt.getAttribute("data-initval");
                    String newVal = JQMCommon.getVal(elt);
                    if (oldVal == newVal || oldVal != null && oldVal.equals(newVal)) {
                        // not changed
                    } else {
                        s += "rowId: " + elt.getAttribute("data-rowid") + "; ";
                        s += "column: " + elt.getAttribute("data-column") + "\n";
                        s += oldVal + " -> " + newVal + "\n\n";
                    }
                }
                if (!s.isEmpty()) Window.alert(s);
                return true;
            }
        });
        dataTable6.enhance();
    }

    private static native JsArray<Element> getTableElts(Element tableElt, String selector) /*-{
        var rslt = $wnd.$(tableElt).DataTable().$(selector);
        return $wnd.$.makeArray(rslt);
    }-*/;

    private static void getArrayServerData(JsAjaxRequest req, JavaScriptObject drawCallback) {
        final JsOrderItems order = req.getOrder();
        String s = "";
        if (order.length() > 0) {
            for (int i = 0; i < order.length(); i++) {
                s += "col=" + String.valueOf(order.get(i).getCol())
                     + ", dir=" + order.get(i).getDir() + "; ";
            }
        }
        if (!s.isEmpty()) s = "order: " + s + "\n\n";

        JsColItems cols = req.getColumns();
        String colStr = "";
        if (cols.length() > 0) {
            for (int i = 0; i < cols.length(); i++) {
                colStr += "data=" + String.valueOf(cols.get(i).getData())
                     + ", name=" + cols.get(i).getName() + "; ";
            }
        }
        if (!colStr.isEmpty()) colStr = "columns: " + colStr + "\n\n";

        //Window.alert(s + colStr + JsUtils.stringify(req));

        String search = req.getSearchValue();
        search = search != null ? search.trim() : "";
        String searchLo = search.toLowerCase();
        final int total = dataArray.length();
        List<JsArrayMixed> lst = new ArrayList<>();
        cols = req.getColumns();
        for (int i = 0; i < total; i++) {
            JsArrayMixed row = dataArray.get(i);
            boolean okRow = search.isEmpty();
            if (!search.isEmpty()) {
                for (int j = 0; j < cols.length(); j++) {
                    String v = row.getString(j);
                    if (Empty.is(v)) continue;
                    if (v.contains(search)) {
                        okRow = true;
                        break;
                    }
                    String vLo = v.toLowerCase();
                    if (vLo.contains(searchLo)) {
                        okRow = true;
                        break;
                    }
                }
            }
            if (!okRow) continue;
            for (int j = 0; j < cols.length(); j++) {
                JsColItem col = cols.get(j);
                if (!Empty.is(col.getSearchValue())) {
                    String colSearch = col.getSearchValue().trim();
                    String colSearchLo = colSearch.toLowerCase();
                    if (!colSearch.isEmpty()) {
                        String v = row.getString(j);
                        if (Empty.is(v)) {
                            okRow = false;
                            break;
                        }
                        if (!v.contains(colSearch) && !v.toLowerCase().contains(colSearchLo)) {
                            okRow = false;
                            break;
                        }
                    }
                }
            }
            if (okRow) lst.add(row);
        }
        JsArrayMixed[] arr = lst.toArray(new JsArrayMixed[0]);
        final int filtered = arr.length;

        if (order.length() > 0) {
            Arrays.sort(arr, new Comparator<JsArrayMixed>() {
                @Override
                public int compare(JsArrayMixed o1, JsArrayMixed o2) {
                    for (int i = 0; i < order.length(); i++) {
                        int colIdx = order.get(i).getCol();
                        String v1 = o1.getString(colIdx);
                        String v2 = o2.getString(colIdx);
                        int cmp = v1.compareTo(v2);
                        if (cmp != 0) {
                            String dir = order.get(i).getDir();
                            if ("asc".equals(dir)) return cmp;
                            else return -cmp;
                        }
                    }
                    return 0;
                }});
        }

        JsAjaxResponse resp = JsAjaxResponse.create();
        resp.setDraw(req.getDraw());
        resp.setRecordsTotal(total);
        resp.setRecordsFiltered(filtered);
        int cnt = Math.min(filtered - req.getStart(), req.getLength());
        JsArray<JsArrayMixed> d = JavaScriptObject.createArray(cnt).cast();
        for (int i = 0; i < cnt; i++) {
            d.set(i, arr[req.getStart() + i]);
        }
        resp.setData(d);
        //s = JsUtils.stringify(resp);
        //Window.alert(s);
        JsUtils.callFunc(drawCallback, resp);
    }

    private static void getObjsServerData(JsAjaxRequest req, JavaScriptObject drawCallback) {
        String search = req.getSearchValue();
        search = search != null ? search.trim() : "";
        String searchLo = search.toLowerCase();
        final int total = dataObjs.length();

        List<JavaScriptObject> lst = new ArrayList<>();
        final JsColItems cols = req.getColumns();
        for (int i = 0; i < total; i++) {
            JavaScriptObject row = dataObjs.get(i);
            boolean okRow = search.isEmpty();
            if (!search.isEmpty()) {
                for (int j = 0; j < cols.length(); j++) {
                    JsColItem col = cols.get(j);
                    if (Empty.is(col.getData())) continue;
                    String v = JsUtils.getChainValStr(row, col.getData());
                    if (Empty.is(v)) continue;
                    if (v.contains(search)) {
                        okRow = true;
                        break;
                    }
                    String vLo = v.toLowerCase();
                    if (vLo.contains(searchLo)) {
                        okRow = true;
                        break;
                    }
                }
            }
            if (!okRow) continue;
            for (int j = 0; j < cols.length(); j++) {
                JsColItem col = cols.get(j);
                if (!Empty.is(col.getData()) && !Empty.is(col.getSearchValue())) {
                    String colSearch = col.getSearchValue().trim();
                    String colSearchLo = colSearch.toLowerCase();
                    if (!colSearch.isEmpty()) {
                        String v = JsUtils.getChainValStr(row, col.getData());
                        if (Empty.is(v)) {
                            okRow = false;
                            break;
                        }
                        if (!v.contains(colSearch) && !v.toLowerCase().contains(colSearchLo)) {
                            okRow = false;
                            break;
                        }
                    }
                }
            }
            if (okRow) lst.add(row);
        }
        JavaScriptObject[] arr = lst.toArray(new JavaScriptObject[0]);
        final int filtered = arr.length;

        final JsOrderItems order = req.getOrder();
        if (order.length() > 0) {
            Arrays.sort(arr, new Comparator<JavaScriptObject>() {
                @Override
                public int compare(JavaScriptObject o1, JavaScriptObject o2) {
                    for (int i = 0; i < order.length(); i++) {
                        int colIdx = order.get(i).getCol();
                        JsColItem col = cols.get(colIdx);
                        String dat = col.getData();
                        if (Empty.is(dat)) continue;
                        String v1 = JsUtils.getChainValStr(o1, dat);
                        String v2 = JsUtils.getChainValStr(o2, dat);
                        int cmp = v1.compareTo(v2);
                        if (cmp != 0) {
                            String dir = order.get(i).getDir();
                            if ("asc".equals(dir)) return cmp;
                            else return -cmp;
                        }
                    }
                    return 0;
                }});
        }

        JsAjaxResponse resp = JsAjaxResponse.create();
        resp.setDraw(req.getDraw());
        resp.setRecordsTotal(total);
        resp.setRecordsFiltered(filtered);
        int cnt = Math.min(filtered - req.getStart(), req.getLength());
        JsArray<JavaScriptObject> d = JavaScriptObject.createArray(cnt).cast();
        for (int i = 0; i < cnt; i++) {
            d.set(i, arr[req.getStart() + i]);
        }
        resp.setData(d);
        //s = JsUtils.stringify(resp);
        //Window.alert(s);
        JsUtils.callFunc(drawCallback, resp);
    }

    private static void getJavaServerData(final JQMDataTable dataTable, JsAjaxRequest req,
                                          JavaScriptObject drawCallback) {
        // We have to work with dataTable.getColumn(), because jsColItems have getData() empty
        // as result of custom RowData assigned.

        String search = req.getSearchValue();
        search = search != null ? search.trim() : "";
        String searchLo = search.toLowerCase();
        final int total = testDataItems.size();

        List<TestDataItem> lst = new ArrayList<>();
        final JsColItems cols = req.getColumns();
        for (int i = 0; i < total; i++) {
            TestDataItem row = testDataItems.get(i);
            boolean okRow = search.isEmpty();
            if (!search.isEmpty()) {
                for (int j = 0; j < cols.length(); j++) {
                    ColumnDefEx col = dataTable.getColumn(j);
                    if (Empty.is(col.getData())) continue;
                    String v = row.getValStr(col.getData());
                    if (Empty.is(v)) continue;
                    if (v.contains(search)) {
                        okRow = true;
                        break;
                    }
                    String vLo = v.toLowerCase();
                    if (vLo.contains(searchLo)) {
                        okRow = true;
                        break;
                    }
                }
            }
            if (!okRow) continue;
            for (int j = 0; j < cols.length(); j++) {
                JsColItem jsCol = cols.get(j);
                ColumnDefEx col = dataTable.getColumn(j);
                if (!Empty.is(col.getData()) && !Empty.is(jsCol.getSearchValue())) {
                    String colSearch = jsCol.getSearchValue().trim();
                    String colSearchLo = colSearch.toLowerCase();
                    if (!colSearch.isEmpty()) {
                        String v = row.getValStr(col.getData());
                        if (Empty.is(v)) {
                            okRow = false;
                            break;
                        }
                        if (!v.contains(colSearch) && !v.toLowerCase().contains(colSearchLo)) {
                            okRow = false;
                            break;
                        }
                    }
                }
            }
            if (okRow) lst.add(row);
        }

        final JsOrderItems order = req.getOrder();
        if (order.length() > 0) {
            Collections.sort(lst, new Comparator<TestDataItem>() {
                @Override
                public int compare(TestDataItem o1, TestDataItem o2) {
                    for (int i = 0; i < order.length(); i++) {
                        int colIdx = order.get(i).getCol();
                        ColumnDefEx col = dataTable.getColumn(colIdx);
                        final int cmp;
                        if ("id".equals(col.getData())) {
                            cmp = o1.id - o2.id;
                        } else {
                            String v1 = o1.getValStr(col.getData());
                            String v2 = o2.getValStr(col.getData());
                            cmp = v1.compareTo(v2);
                        }
                        if (cmp != 0) {
                            String dir = order.get(i).getDir();
                            if ("asc".equals(dir)) return cmp;
                            else return -cmp;
                        }
                    }
                    return 0;
                }});
        }

        final int filtered = lst.size();
        JsAjaxResponse resp = JsAjaxResponse.create();
        resp.setDraw(req.getDraw());
        resp.setRecordsTotal(total);
        resp.setRecordsFiltered(filtered);
        int cnt = Math.min(filtered - req.getStart(), req.getLength());
        JsArray<JavaScriptObject> d = JavaScriptObject.createArray(cnt).cast();
        for (int i = 0; i < cnt; i++) {
            TestDataItem item = lst.get(req.getStart() + i);
            JavaScriptObject jsObj = JavaScriptObject.createObject();
            JsUtils.setObjValue(jsObj, JQMDataTable.DT_ROWID, String.valueOf(item.id));
            JsUtils.setNestedObjJavaValue(jsObj, "dataItem", item);
            d.set(i, jsObj);
        }
        resp.setData(d);
        //s = JsUtils.stringify(resp);
        //Window.alert(s);
        JsUtils.callFunc(drawCallback, resp);
    }

    private static void generateRowIds(JsColItems cols) {
        for (int i = 0; i < dataObjs.length(); i++) {
            JavaScriptObject row = dataObjs.get(i);
            String s = "";
            for (int j = 0; j < cols.length(); j++) {
                JsColItem col = cols.get(j);
                String d = col.getData();
                if (Empty.is(d)) continue;
                String v = JsUtils.getChainValStr(row, d);
                s += v;
            }
            JsUtils.setObjValue(row, JQMDataTable.DT_ROWID, JsUtils.hashFnv32a(s));
        }
    }

    private static JsArrayMixed createDataRow(int rowIdx) {
        JsArrayMixed r = JavaScriptObject.createArray().cast();
        r.push("Tiger Nixon");
        r.push("System Architect");
        r.push("Edinburgh");
        r.push(String.valueOf(rowIdx));
        r.push("2011/04/25");
        r.push("$123,456");
        return r;
    }

    public JQMPage getPage() {
        return page;
    }

}
