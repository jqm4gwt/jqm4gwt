package com.sksamuel.jqm4gwt.plugins.datatables;

/**
 * Class names available to control the different styling features of a DataTable.
 *
 * <br> See <a href="https://datatables.net/manual/styling/classes#Table-classes">Table classes</a>
 */
public enum DataTableStyleClass {

    /** Added by default. Short-hand for the: stripe, hover, row-border and order-column classes. */
    STD_DISPLAY("display"),
    /** Border around all four sides of each cell. Note cell-border and row-border are mutually exclusive and cannot be used together. */
    CELL_BORDER("cell-border"),
    /** Reduce the amount of white-space the default styling for the DataTable uses, increasing the information density on screen. */
    COMPACT("compact"),
    /** Disable wrapping of content in the table, so all text in the cells is on a single line. */
    NOWRAP("nowrap"),
    /** Highlight the column that the table data is currently ordered on. */
    ORDER_COL_HIGHLIGHT("order-column"),
    /** Row highlighting on mouse over. */
    ROW_HOVER("hover"),
    /** Border around only the top an bottom of each each (i.e. for the rows). Note cell-border and row-border are mutually exclusive and cannot be used together. */
    ROW_BORDER("row-border"),
    /** Row striping. */
    ROW_STRIPE("stripe");

    private final String jqmVal;

    private DataTableStyleClass(String jqmVal) {
        this.jqmVal = jqmVal;
    }

    /**
     * Returns the string value that DataTable expects.
     */
    public String getJqmValue() {
        return jqmVal;
    }

    public static DataTableStyleClass fromJqmValue(String jqmValue) {
        if (jqmValue == null || jqmValue.isEmpty()) return null;
        for (DataTableStyleClass i : DataTableStyleClass.values()) {
            if (i.getJqmValue().equals(jqmValue)) return i;
        }
        return null;
    }

}
