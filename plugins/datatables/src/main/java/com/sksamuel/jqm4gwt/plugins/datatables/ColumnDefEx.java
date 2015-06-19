package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.table.ColumnDef;

/**
 * Additional column properties supported by DataTable.
 * <br> See <a href="https://datatables.net/reference/option/columns">DataTables Columns</a>
 *
 * @author slavap
 *
 */
public class ColumnDefEx extends ColumnDef {

    private List<Widget> widgets;

    private boolean orderable = true;
    private boolean searchable = true;

    @UiChild(tagname = "widget")
    public void addWidget(Widget w) {
        if (w != null) {
            if (widgets == null) widgets = new ArrayList<>();
            widgets.add(w);
        }
    }

    public boolean hasWidgets() {
        return !Empty.is(widgets);
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

}
