package com.sksamuel.jqm4gwt.examples.collapsible;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.layout.JQMCollapsible;
import com.sksamuel.jqm4gwt.layout.JQMCollapsibleSet;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 23:23:36
 * 
 */
public class DynamicCollapsibleDemoPage extends JQMPage {

	private JQMCollapsibleSet	set;
    private static int count = 0;

	public DynamicCollapsibleDemoPage() {
        withContainerId();
		add(new JQMHeader("Dynamic Collapsible"));
		add(new Paragraph(
				"This page shows how a JQM Collapsible can be combined with an event handler to dynamically add new collapsibles. "
						+ "Each collapsible here is placed inside a collapsible set."));

		JQMButton add = new JQMButton("Add new collapsible");
		add.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addCollapsible();
			}
		});
		add(add);

		set = new JQMCollapsibleSet();
		add(set);
		addCollapsible();
		addCollapsible();
	}

	protected void addCollapsible() {
		JQMCollapsible c = new JQMCollapsible("Expand me!");
        c.setId(""+count++);
		c.add(new Paragraph("some content in the collapsible"));

		JQMCollapsible nested = new JQMCollapsible("A nested collapsible");
		nested.add(new Paragraph("some content in the nested collapsible"));
        c.setId(""+count++);
		c.add(nested);

		set.add(c);
		Mobile.render(c.getId());
	}
}
