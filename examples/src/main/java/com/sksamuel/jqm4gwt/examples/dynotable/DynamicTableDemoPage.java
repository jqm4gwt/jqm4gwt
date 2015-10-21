package com.sksamuel.jqm4gwt.examples.dynotable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMSelect;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.layout.JQMTable;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 23:23:36
 *
 */
public class DynamicTableDemoPage extends JQMPage {

    private JQMTable table;

    public DynamicTableDemoPage() {
        JQMHeader h = new JQMHeader("Dynamic Table");
        h.setBackButton(true);
        add(h);
        add(new Paragraph(
                "This example shows how a JQMTable can be combined with event handlers to "
                + "dynamically adjust the table and cells."));

        Paragraph p = new Paragraph();
        p.setHTML("Click 'Add new cell' and a new cell will be added to the end of the table. "
                + "<br> Click the button inside a cell and that cell will be removed. "
                + "<br> Choose the table size and the table will be dynamically resized.");
        add(p);

        table = new JQMTable(2);

        final JQMSelect select = new JQMSelect();
        select.withText("Choose table size").addOptions("1", "2", "3", "4", "5");
        select.setValue("2");
        select.setSelectInline(true);
        select.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                int size = Integer.parseInt(select.getValue());
                table.setColumns(size);
            }});

        JQMButton addBtn = new JQMButton("Add new cell");
        addBtn.setInline(true);
        addBtn.getElement().getStyle().setColor("blue");
        addBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addCell();
            }});
        JQMTable t = new JQMTable(2);
        t.add(select);
        t.add(addBtn);
        add(t);
        add(table);

        // add three buttons to start with
        addCell();
        addCell();
        addCell();
    }

    protected void addCell() {
        JQMButton btn = new JQMButton("Remove me");
        final Widget cell = table.add(btn);
        btn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                table.remove(cell);
            }});
    }
}
