package com.sksamuel.jqm4gwt.plugins.grid960;


import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.panel.JQMPanel;

public class Grid960Panel extends JQMPanel {

    protected Grid960Panel() {
        super(Document.get().createDivElement(), "grid960", "container_12");
    }

    @UiChild
    public void add(GridColumn col) {
        super.add(col);
    }

    @Override
    public void add(Widget w) {
        throw new RuntimeException("Grid960Panels only accept GridColumns. Add your content to a grid column and " +
                "then add the column to this panel.");
    }
}
