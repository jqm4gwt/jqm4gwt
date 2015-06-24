package com.sksamuel.jqm4gwt.examples.datatables;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.sksamuel.jqm4gwt.JQMPage;

public class DataTableExamplesPage {

    private static DataTableExamplesPageUiBinder uiBinder = GWT
            .create(DataTableExamplesPageUiBinder.class);

    interface DataTableExamplesPageUiBinder extends UiBinder<JQMPage, DataTableExamplesPage> {
    }

    private JQMPage page;

    public DataTableExamplesPage() {
        page = uiBinder.createAndBindUi(this);
    }

    public JQMPage getPage() {
        return page;
    }

}
