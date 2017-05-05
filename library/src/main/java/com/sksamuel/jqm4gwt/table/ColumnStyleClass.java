package com.sksamuel.jqm4gwt.table;

/**
 * Options for the styling of cells/columns.
 * <br> See <a href="https://datatables.net/manual/styling/classes#Cell-classes">Cell classes</a>
 * <br> Ported for basic JQMTableGrid as well.
 */
public enum ColumnStyleClass {

    /** Right align text in the header and body. */
    RIGHT("dt-right"),
    /** Right align text in the header only. */
    RIGHT_HEAD("dt-head-right"),
    /** Right align text in the body only. */
    RIGHT_BODY("dt-body-right"),
    /** Left align text in the header and body. */
    LEFT("dt-left"),
    LEFT_HEAD("dt-head-left"),
    LEFT_BODY("dt-body-left"),
    /** Center align text in the header and body. */
    CENTER("dt-center"),
    CENTER_HEAD("dt-head-center"),
    CENTER_BODY("dt-body-center"),
    /** Justify text in the header and body. See <a href="https://www.w3schools.com/cssref/playit.asp?filename=playcss_text-align&preval=justify">Justify</a> */
    JUSTIFY("dt-justify"),
    JUSTIFY_HEAD("dt-head-justify"),
    JUSTIFY_BODY("dt-body-justify"),
    /** Nowrap text in the header and body. */
    NOWRAP("dt-nowrap"),
    NOWRAP_HEAD("dt-head-nowrap"),
    NOWRAP_BODY("dt-body-nowrap");

    public static final String JQM_BODY_ONLY_PREFIX = "dt-body-";
    public static final String JQM_HEAD_ONLY_PREFIX = "dt-head-";

    private final String jqmVal;

    private ColumnStyleClass(String jqmVal) {
        this.jqmVal = jqmVal;
    }

    /**
     * Returns the string value that DataTable expects.
     */
    public String getJqmValue() {
        return jqmVal;
    }

    public static ColumnStyleClass fromJqmValue(String jqmValue) {
        if (jqmValue == null || jqmValue.isEmpty()) return null;
        for (ColumnStyleClass i : ColumnStyleClass.values()) {
            if (i.getJqmValue().equals(jqmValue)) return i;
        }
        return null;
    }

}
