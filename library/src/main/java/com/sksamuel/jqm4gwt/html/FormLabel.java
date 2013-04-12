package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 13:38:38
 * 
 *         An implemenation of a <label> element
 * 
 */
public class FormLabel extends Widget implements HasText {

	public FormLabel() {
		setElement(Document.get().createLabelElement());
	}

	public String getFor() {
		return getElement().getAttribute("for");
	}

	@Override
	public String getText() {
		return getElement().getInnerText();
	}

	public void setFor(String id) {
		getElement().setAttribute("for", id);
	}

	@Override
	public void setText(String text) {
		getElement().setInnerText(text);
	}
}
