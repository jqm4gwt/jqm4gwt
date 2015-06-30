package com.sksamuel.jqm4gwt.examples.datatables;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.plugins.datatables.JQMDataTable;
import com.sksamuel.jqm4gwt.plugins.datatables.JQMDataTable.RowSelectMode;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.CellClickHandler;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.RowDetailsRenderer;

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
    JQMButton btnUnselectAll;

    @UiField
    JQMButton btnGetSelected;

    @UiField
    JQMButton btnReplaceData;

    @UiField
    JQMButton btnDeleteSelRow;

    public DataTableExamplesPage() {
        page = uiBinder.createAndBindUi(this);
        dataTable2.addCellBtnClickHandler(new CellClickHandler() {
            @Override
            public boolean onClick(Element elt, JavaScriptObject rowData, int rowIndex) {
                String s = dataTable2.getCellData(rowIndex, "name");
                Window.alert(s);
                if (RowSelectMode.SINGLE.equals(dataTable2.getRowSelectMode())) {
                    dataTable2.selectOneRowOnly(elt);
                } else if (RowSelectMode.MULTI.equals(dataTable2.getRowSelectMode())) {
                    dataTable2.changeRow(elt, true);
                }
                return true;
            }
        });
        dataTable2.addCellCheckboxClickHandler(new CellClickHandler() {
            @Override
            public boolean onClick(Element elt, JavaScriptObject rowData, int rowIndex) {
                InputElement cb = elt.cast();
                if (cb.isChecked()) {
                    if (RowSelectMode.SINGLE.equals(dataTable2.getRowSelectMode())) {
                        dataTable2.selectOneRowOnly(cb);
                    } else if (RowSelectMode.MULTI.equals(dataTable2.getRowSelectMode())) {
                        dataTable2.changeRow(cb, true);
                    }
                } else if (dataTable2.getRowSelectMode() != null) {
                    dataTable2.changeRow(cb, false);
                }
                return true;
            }
        });
        dataTable2.addRowDetailsRenderer(new RowDetailsRenderer() {
            @Override
            public String getHtml(JavaScriptObject rowData, int rowIndex) {
                return dataTable2.getColumnsAsTableHtml(rowIndex, "border='0' style='padding-left:50px;'");
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
                    JsArrayMixed r = JavaScriptObject.createArray().cast();
                    r.push("Tiger Nixon");
                    r.push("System Architect");
                    r.push("Edinburgh");
                    r.push(String.valueOf(i));
                    r.push("2011/04/25");
                    r.push("$123,456");
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
    }

    public JQMPage getPage() {
        return page;
    }

}
