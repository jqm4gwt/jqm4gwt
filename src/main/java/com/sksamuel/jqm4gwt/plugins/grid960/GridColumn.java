package com.sksamuel.jqm4gwt.plugins.grid960;


import com.google.gwt.dom.client.Document;
import com.sksamuel.jqm4gwt.panel.JQMPanel;

public class GridColumn extends JQMPanel {

    private static final String GRID_STYLE_PREFIX = "grid_";

    /**
     * Nullary constructor used by UiBinder - must be followed setSpan for proper styling.
     */
    public GridColumn() {
        super(Document.get().createDivElement(), "jqm4gwt-gridcolumn", null);
    }

    public GridColumn(int span) {
        super(Document.get().createDivElement(), "jqm4gwt-gridcolumn", GRID_STYLE_PREFIX + span);
    }

    public int getSpan() {
        return Integer.parseInt(getStylePrimaryName().substring(GRID_STYLE_PREFIX.length()));
    }

    public void setSpan(int span) {
        setStyleName(GRID_STYLE_PREFIX + span);
    }
}
