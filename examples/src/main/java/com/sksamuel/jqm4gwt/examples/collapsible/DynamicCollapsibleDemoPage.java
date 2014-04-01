package com.sksamuel.jqm4gwt.examples.collapsible;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sksamuel.jqm4gwt.JQMPage;
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

	private JQMCollapsibleSet set;
    private static int count = 0;

	public DynamicCollapsibleDemoPage() {
		JQMHeader h = new JQMHeader("Dynamic Collapsible");
		h.setBackButton(true);
	    add(h);
		add(new Paragraph(
				"This page shows how a JQM Collapsible can be combined with an event handler to dynamically add new collapsibles. "
						+ "Each collapsible here is placed inside a collapsible set."));

		JQMButton addBtn = new JQMButton("Add new collapsible");
		addBtn.setInline(true);
		addBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addCollapsible();
			}
		});
		add(addBtn);

		JQMButton expandBtn = new JQMButton("Expand last collapsible");
		expandBtn.setInline(true);
        expandBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int cnt = set.getCollapsibleCount();
                if (cnt > 0) set.getCollapsible(cnt - 1).expand();
            }
        });
        add(expandBtn);

        JQMButton collapseBtn = new JQMButton("Collapse last collapsible");
        collapseBtn.setInline(true);
        collapseBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int cnt = set.getCollapsibleCount();
                if (cnt > 0) set.getCollapsible(cnt - 1).collapse();
            }
        });
        add(collapseBtn);

		set = new JQMCollapsibleSet();
		add(set);
		addCollapsible();
		addCollapsible();
	}

	protected void addCollapsible() {
		JQMCollapsible c = new JQMCollapsible("Expand me!");
        c.setId(String.valueOf(count++));
		c.add(new Paragraph("some content in the collapsible"));

		JQMCollapsible nested = new JQMCollapsible("A nested collapsible");
		nested.add(new Paragraph("some content in the nested collapsible"));
        c.setId(String.valueOf(count++));
		c.add(nested);

		set.add(c);
		set.refresh();
	}
}
