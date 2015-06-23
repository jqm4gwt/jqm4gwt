package com.sksamuel.jqm4gwt.plugins.datatables;


public class LanguageImpl implements Language {

    private String decimal;
    private String thousands;
    private String lengthMenu;
    private String zeroRecords;
    private String info;
    private String infoEmpty;
    private String infoFiltered;
    private String loadingRecords;
    private String processing;
    private String search;
    private String searchPlaceholder;
    private String url;
    private String paginate;

    @Override
    public String getDecimal() {
        return decimal;
    }

    @Override
    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    @Override
    public String getThousands() {
        return thousands;
    }

    @Override
    public void setThousands(String thousands) {
        this.thousands = thousands;
    }

    @Override
    public String getLengthMenu() {
        return lengthMenu;
    }

    @Override
    public void setLengthMenu(String lengthMenu) {
        this.lengthMenu = lengthMenu;
    }

    @Override
    public String getZeroRecords() {
        return zeroRecords;
    }

    @Override
    public void setZeroRecords(String zeroRecords) {
        this.zeroRecords = zeroRecords;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String getInfoEmpty() {
        return infoEmpty;
    }

    @Override
    public void setInfoEmpty(String infoEmpty) {
        this.infoEmpty = infoEmpty;
    }

    @Override
    public String getInfoFiltered() {
        return infoFiltered;
    }

    @Override
    public void setInfoFiltered(String infoFiltered) {
        this.infoFiltered = infoFiltered;
    }

    @Override
    public String getLoadingRecords() {
        return loadingRecords;
    }

    @Override
    public void setLoadingRecords(String loadingRecords) {
        this.loadingRecords = loadingRecords;
    }

    @Override
    public String getProcessing() {
        return processing;
    }

    @Override
    public void setProcessing(String processing) {
        this.processing = processing;
    }

    @Override
    public String getSearch() {
        return search;
    }

    @Override
    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public String getSearchPlaceholder() {
        return searchPlaceholder;
    }

    @Override
    public void setSearchPlaceholder(String searchPlaceholder) {
        this.searchPlaceholder = searchPlaceholder;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getPaginate() {
        return paginate;
    }

    @Override
    public void setPaginate(String paginate) {
        this.paginate = paginate;
    }

}
