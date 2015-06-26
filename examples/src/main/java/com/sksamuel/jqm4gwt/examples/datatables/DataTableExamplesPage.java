package com.sksamuel.jqm4gwt.examples.datatables;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.plugins.datatables.ColumnDefEx;
import com.sksamuel.jqm4gwt.plugins.datatables.JQMDataTable;
import com.sksamuel.jqm4gwt.plugins.datatables.JQMDataTable.RowSelectMode;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable;
import com.sksamuel.jqm4gwt.plugins.datatables.JsDataTable.CellClickHandler;

public class DataTableExamplesPage {

    private static DataTableExamplesPageUiBinder uiBinder = GWT
            .create(DataTableExamplesPageUiBinder.class);

    interface DataTableExamplesPageUiBinder extends UiBinder<JQMPage, DataTableExamplesPage> {
    }

    private JQMPage page;

    @UiField
    JQMDataTable dataTable2;

    @UiField
    JQMButton btnUnselectAll;

    public DataTableExamplesPage() {
        page = uiBinder.createAndBindUi(this);
        dataTable2.addCellBtnClickHandler(new CellClickHandler() {
            @Override
            public boolean onClick(Element elt, JavaScriptObject rowData) {
                ColumnDefEx col = dataTable2.findColumn("name");
                if (col != null) {
                    String s = JQMContext.getJsObjValue(rowData, col.getData());
                    Window.alert(s);
                    JsDataTable.selectOneRowOnly(elt);
                    return true;
                }
                return false;
            }
        });
        dataTable2.addCellCheckboxClickHandler(new CellClickHandler() {
            @Override
            public boolean onClick(Element elt, JavaScriptObject rowData) {
                InputElement cb = elt.cast();
                if (cb.isChecked()) {
                    if (RowSelectMode.SINGLE.equals(dataTable2.getRowSelectMode())) {
                        JsDataTable.selectOneRowOnly(cb);
                    } else {
                        JsDataTable.changeRow(cb, true);
                    }
                } else {
                    JsDataTable.changeRow(cb, false);
                }
                return true;
            }
        });
        btnUnselectAll.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                JsDataTable.unselectAllRows(dataTable2.getElement());
            }
        });
    }

    public JQMPage getPage() {
        return page;
    }

}
