package com.sksamuel.jqm4gwt.plugins.datatables;

import com.sksamuel.jqm4gwt.Empty;

interface Language {

    class Builder {

        public static void copy(Language src, Language dst, boolean nonEmpty) {
            if (!nonEmpty || !Empty.is(src.getDecimal())) dst.setDecimal(src.getDecimal());
            if (!nonEmpty || !Empty.is(src.getThousands())) dst.setThousands(src.getThousands());
            if (!nonEmpty || !Empty.is(src.getLengthMenu())) dst.setLengthMenu(src.getLengthMenu());
            if (!nonEmpty || !Empty.is(src.getZeroRecords())) dst.setZeroRecords(src.getZeroRecords());
            if (!nonEmpty || !Empty.is(src.getInfo())) dst.setInfo(src.getInfo());
            if (!nonEmpty || !Empty.is(src.getInfoEmpty())) dst.setInfoEmpty(src.getInfoEmpty());
            if (!nonEmpty || !Empty.is(src.getInfoFiltered())) dst.setInfoFiltered(src.getInfoFiltered());
            if (!nonEmpty || !Empty.is(src.getLoadingRecords())) dst.setLoadingRecords(src.getLoadingRecords());
            if (!nonEmpty || !Empty.is(src.getProcessing())) dst.setProcessing(src.getProcessing());
            if (!nonEmpty || !Empty.is(src.getSearch())) dst.setSearch(src.getSearch());
            if (!nonEmpty || !Empty.is(src.getSearchPlaceholder())) dst.setSearchPlaceholder(src.getSearchPlaceholder());
            if (!nonEmpty || !Empty.is(src.getUrl())) dst.setUrl(src.getUrl());
            if (!nonEmpty || !Empty.is(src.getPaginate())) dst.setPaginate(src.getPaginate());
        }
    }

    String getDecimal();
    void setDecimal(String value);

    String getThousands();
    void setThousands(String value);

    String getLengthMenu();
    /** Example: Display _MENU_ records per page */
    void setLengthMenu(String value);

    String getZeroRecords();
    /** Table empty as a result of filtering string. Example: Nothing found - sorry */
    void setZeroRecords(String value);

    String getInfo();
    /** Example: Showing page _PAGE_ of _PAGES_ */
    void setInfo(String value);

    String getInfoEmpty();
    /** Customization for: Showing 0 to 0 of 0 entries. Example: No records available */
    void setInfoEmpty(String value);

    String getInfoFiltered();
    /** Example: (filtered from _MAX_ total records) */
    void setInfoFiltered(String value);

    String getLoadingRecords();
    /** Shown when Ajax loading data. Example: Please wait - loading... */
    void setLoadingRecords(String value);

    String getProcessing();
    /**
     * Text that is displayed when the table is processing a user action (usually a sort command or similar).
     * <br> Example: DataTables is currently busy
     **/
    void setProcessing(String value);

    String getSearch();
    /** Sets the string that is used for DataTables filtering input control. */
    void setSearch(String value);

    String getSearchPlaceholder();
    /**
     * Sets placeholder attribute for search input.
     * <br> Search input with no label - just the placeholder:
     * <br> search: "_INPUT_", searchPlaceholder: "Search..."
     **/
    void setSearchPlaceholder(String value);

    String getUrl();
    /** See <a href="https://datatables.net/reference/option/language.url">language.url</a> */
    void setUrl(String value);

    String getPaginate();
    /** Comma separated: previous, next, first, last */
    void setPaginate(String value);

}