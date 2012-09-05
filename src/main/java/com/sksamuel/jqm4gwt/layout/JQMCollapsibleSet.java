package com.sksamuel.jqm4gwt.layout;

import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.JQMWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 10:44:29
 * 
 *         An implementation of a group or set of collapsibles. When
 *         {@link JQMCollapsible} widgets are placed inside a
 *         {@link JQMCollapsibleSet} they behave like an accordian widget - that
 *         is only one can be open at any time. If a user opens another
 *         collapsible panel, then any others will be closed automatically.
 * 
 * @link http://jquerymobile.com/demos/1.1.1/docs/content/content-collapsible-set.html
 * 
 */
public class JQMCollapsibleSet extends JQMWidget {

	private final FlowPanel	flow;

	public JQMCollapsibleSet() {
		flow = new FlowPanel();
		initWidget(flow);
		setDataRole("collapsible-set");
		setId();
	}

	/**
	 * Adds the given collapsible to the end of this set
	 */
	public void add(JQMCollapsible c) {
		flow.add(c);
	}

	/**
	 * Removes the given collapsible from this set
	 */
	public void remove(JQMCollapsible c) {
		flow.remove(c);
	}

}
