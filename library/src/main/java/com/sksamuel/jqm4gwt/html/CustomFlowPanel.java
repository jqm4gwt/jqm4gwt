package com.sksamuel.jqm4gwt.html;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Flow panel based on the given HTML element sent as constructor argument.
 * <p/>A panel that formats its child widgets using the default HTML layout behavior.
 */
public class CustomFlowPanel extends FlowPanel {

	private Element e;

	/**
	 * Creates an empty flow panel based on the given element.
	 */
	public CustomFlowPanel(Element e) {
		super();
		this.e = e;
		setElement(e);
	}

	public CustomFlowPanel(com.google.gwt.dom.client.Element e) {
	    this((Element) e.cast());
	}

	@Override
	protected void setElement(Element elem) {
		if (e != null) {
			super.setElement(e);
			e = null;
		}
	}

}
