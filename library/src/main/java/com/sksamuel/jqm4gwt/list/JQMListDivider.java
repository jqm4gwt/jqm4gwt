package com.sksamuel.jqm4gwt.list;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasText;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 14:36:20
 * 
 *         An implementation of a list divider.
 * 
 */
class JQMListDivider extends Widget implements HasText<JQMListDivider> {

	JQMListDivider(String text) {
		setElement(Document.get().createLIElement());
		getElement().setAttribute("data-role", "list-divider");
		getElement().setId(Document.get().createUniqueId());
		setText(text);
	}

	/**
	 * Returns the text currently set on the divider.
	 * 
	 * @return the divider text
	 */
	@Override
	public String getText() {
		return getElement().getInnerText();
	}

	/**
	 * Sets the text of this divider. Changes by this method will create
	 * through to the underlying list.
	 */
	@Override
	public void setText(String text) {
		getElement().setInnerText(text);
	}

    @Override
    public JQMListDivider withText(String text) {
        setText(text);
        return this;
    }
}
