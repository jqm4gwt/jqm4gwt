package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 13:38:38
 * 
 *         An implementation of a H{n} element.
 * 
 */
public class Heading extends Widget implements HasText {

	public Heading(int n) {
		setElement(Document.get().createHElement(n));
	}

	public Heading(int i, String text) {
		this(i);
		setText(text);
	}

	@Override
	public String getText() {
		return getElement().getInnerText();
	}

	@Override
	public void setText(String text) {
		getElement().setInnerText(text);
	}
}
