package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.button.JQMButton;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 23:09:12
 * 
 *         jQuery Mobile has a very basic navbar widget that is useful for
 *         providing up to 5 buttons with optional icons in a bar , typically
 *         within a header or footer.
 * 
 * 
 * @link 
 *       http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/toolbars/docs-navbar
 *       .html
 * 
 */
public class JQMNavBar extends JQMWidget implements HasFixedPosition {

	private final Element	ul;

	/**
	 * Create a new {@link JQMNavBar} with no content
	 */
	public JQMNavBar() {

		Label label = new Label();
		initWidget(label);

		ul = Document.get().createULElement();
		label.getElement().appendChild(ul);
	}

	public void add(JQMButton button) {
		LIElement e = Document.get().createLIElement();
		e.appendChild(button.getElement());
		ul.appendChild(e);
	}

    @UiChild (limit = 5)
    public void addButton(JQMButton button) {
        add(button);
    }

	@Override
	public boolean isFixed() {
		return "fixed".equals(getAttribute("data-position"));
	}

	public void remove(JQMButton button) { // todo

	}

	@Override
	public void setFixed(boolean fixed) {
		if (fixed)
			setAttribute("data-position", "fixed");
		else
			removeAttribute("data-position");
	}
}
