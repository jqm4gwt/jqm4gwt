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

    private boolean visible = true;
    private boolean orderable = true;
    private boolean searchable = true;

    private String classNames;
    private boolean cellTypeTh;
    private String defaultContent;
    private String width;

    private Integer dataIdx;
    private String  data;

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getClassNames() {
        return classNames;
    }

    /** Space separated list of additional styling classes for table body cells. */
    public void setClassNames(String classNames) {
        this.classNames = classNames;
    }

    public boolean isCellTypeTh() {
        return cellTypeTh;
    }

    /**
     * This can be useful as TH cells have semantic meaning in the table body,
     * allowing them to act as a header for a row.
     */
    public void setCellTypeTh(boolean cellTypeTh) {
        this.cellTypeTh = cellTypeTh;
    }

    public String getDefaultContent() {
        return defaultContent;
    }

    /** Set default, static, content for a column, for example simple edit and/or delete buttons. */
    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }

    public String getWidth() {
        return width;
    }

    /** Defines the width of a column, and may take any CSS value (3em, 20px, 10%, etc). */
    public void setWidth(String width) {
        this.width = width;
    }

    public String getData() {
        return data;
    }

    /**
     * Set the data source for the column from the rows data object/array.
     * <br> data and dataIdx are mutually exclusive, so you cannot use them simultaneously.
     * <br> See <a href="https://datatables.net/reference/option/columns.data">columns.data</a>
     **/
    public void setData(String data) {
        this.dataIdx = null;
        this.data = data;
    }

    public Integer getDataIdx() {
        return dataIdx;
    }

    /** Treated as an array index for the data source. Also see data property. */
    public void setDataIdx(Integer dataIdx) {
        this.data = null;
        this.dataIdx = dataIdx;
    }

}
