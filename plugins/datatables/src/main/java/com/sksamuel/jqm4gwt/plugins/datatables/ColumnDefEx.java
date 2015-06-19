package com.sksamuel.jqm4gwt.plugins.datatables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.table.ColumnDef;

public class ColumnDefEx extends ColumnDef {

    private List<Widget> widgets;

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

}
