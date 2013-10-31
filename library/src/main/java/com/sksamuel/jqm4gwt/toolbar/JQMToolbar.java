package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.html.Heading;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 13:27:36
 *
 *         Superclass for toolbars - {@link JQMHeader} and {@link JQMFooter}
 *
 * @link
 *         http://jquerymobile.com/demos/1.2.0/docs/toolbars/docs-footers.html
 *
 */
public abstract class JQMToolbar extends JQMWidget implements HasText, HasFixedPosition {

	private final FlowPanel	flow;

	/**
	 * The header contains the text, it can be null
	 */
	private Heading		header;

	/**
	 * Creates a new toolbar with a header element for the given text
	 */
	protected JQMToolbar(String dataRole, String styleName, String text) {

		flow = new FlowPanel();
		initWidget(flow);

		setDataRole(dataRole);
		setStyleName(styleName);

		setText(text);
	}

	/**
	 * Adds the given widget to the toolbar
	 */
    @UiChild(tagname="widget")
	public void add(Widget w) {
		flow.add(w);
	}

	/**
	 * Returns the text of the Hn element
	 */
	@Override
	public String getText() {
		return header == null ? null : header.getText();
	}

	public Widget[] getWidgets() {
		Widget[] widgets = new Widget[flow.getWidgetCount()];
		for (int k = 0; k < flow.getWidgetCount(); k++) {
			widgets[k] = flow.getWidget(k);
		}
		return widgets;
	}

	public void insert(Widget left, int i) {
		flow.insert(left, i);
	}

	@Override
	public final boolean isFixed() {
		return "fixed".equals(getAttribute("data-position"));
	}

	/**
	 * Removes the given widget from the toolbar
	 */
	public void remove(Widget w) {
		flow.remove(w);
	}

	/**
	 * Removes the Hn text if any is set.
	 */
	public void removeText() {
		if (header != null) {
			flow.remove(header);
			header = null;
		}
	}

	@Override
	public final void setFixed(boolean fixed) {
		if (fixed)
			setAttribute("data-position", "fixed");
		else
			removeAttribute("data-position");
	}

	/**
	 *  Ability to toggle toolbar-visibility on user tap/click
	 */
	public void setTapToggle(boolean toggle) {
		setAttribute("data-tap-toggle", String.valueOf(toggle));
	}

	/**
	 * Sets the value of the Hn element
	 */
	@Override
	public void setText(String text) {
		if (text == null) {
			if (header != null)
				flow.remove(header);
		} else {
			if (header == null) {
				header = new Heading(1);
				flow.add(header);
			}
			header.setText(text);
		}
	}

	/**
	 *  Have the page top and bottom padding updated on resize, transition, "updatelayout" events
	 *  (the framework always updates the padding on the "pageshow" event).
	 */
	public void setUpdatePagePadding(boolean toggle) {
		setAttribute("data-update-page-padding", String.valueOf(toggle));
	}
}
