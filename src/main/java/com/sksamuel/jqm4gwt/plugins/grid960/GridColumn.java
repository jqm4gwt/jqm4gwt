package com.sksamuel.jqm4gwt.plugins.grid960;


import com.google.gwt.dom.client.Document;
import com.sksamuel.jqm4gwt.panel.JQMPanel;

public class GridColumn extends JQMPanel {

    public GridColumn(int span) {
        super(Document.get().createDivElement(), "jqm4gwt-gridcolumn", "grid_" + span);
    }
}
