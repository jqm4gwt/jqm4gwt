package com.sksamuel.jqm4gwt.examples.dynotable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMSelect;
import com.sksamuel.jqm4gwt.layout.JQMTable;

public class DynTableUiBinder {

    @UiField JQMSelect select;
    @UiField JQMButton addBtn;
    @UiField JQMTable table;
    @UiField JQMButton cellBtn1;
    @UiField JQMButton cellBtn2;
    @UiField JQMButton cellBtn3;

    private final JQMPage page;

    interface DynTableUiBinderUiBinder extends UiBinder<JQMPage, DynTableUiBinder> {}

    private static DynTableUiBinderUiBinder uiBinder = GWT.create(DynTableUiBinderUiBinder.class);

    public DynTableUiBinder() {
        page = uiBinder.createAndBindUi(this);

        select.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                int size = Integer.parseInt(select.getValue());
                table.setColumns(size);
            }});

        addBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addCell();
            }});

        addCellBtnClickHandler(cellBtn1);
        addCellBtnClickHandler(cellBtn2);
        addCellBtnClickHandler(cellBtn3);
    }

    public JQMPage getPage() {
        return page;
    }

    protected void addCell() {
        JQMButton btn = new JQMButton("Remove me");
        table.add(btn);
        addCellBtnClickHandler(btn);
    }

    private void addCellBtnClickHandler(final JQMButton btn) {
        btn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Widget cell = btn.getParent();
                table.remove(cell);
            }});
    }

}
