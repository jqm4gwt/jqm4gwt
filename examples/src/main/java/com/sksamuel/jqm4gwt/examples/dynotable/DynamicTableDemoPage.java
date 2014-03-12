package com.sksamuel.jqm4gwt.examples.dynotable;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

	private JQMTable	table;

	public DynamicTableDemoPage() {
		JQMHeader h = new JQMHeader("Dynamic Table");
		h.setBackButton(true);
	    add(h);
		add(new Paragraph(
				"This example shows how a JQMTable can be combined with event handlers to dynamically adjust the table and cells."));
		add(new Paragraph("Click 'add new cell' and a new cell will be added to the end of the table. "
				+ "Click the button inside a cell and that cell will be removed. "
				+ "Choose the table size and the table will be dynamically resized"));

		table = new JQMTable(2);

		final JQMSelect select = new JQMSelect();
		select.setText("Choose table size");
		select.addOption("1");
		select.addOption("2");
		select.addOption("3");
		select.addOption("4");
		select.addOption("5");
		select.setValue("2");
		select.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int size = Integer.parseInt(select.getValue());
				table.setColumns(size);
			}
		});
		add(select);

		JQMButton add = new JQMButton("Add new cell");
		add.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addCell();
			}

		});
		add(add);

		add(table);

		// add three buttons to start with
		addCell();
		addCell();
		addCell();
	}

	protected void addCell() {
		final JQMButton b = new JQMButton("Remove me");
		final Widget container = table.add(b);
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				table.remove(container);
			}

		});

	}
}
