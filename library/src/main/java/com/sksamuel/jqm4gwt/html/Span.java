package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasText;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 13:38:38
 * 
 *         An implemenation of a <span> element exposed as a widget
 * 
 */
public class Span extends Widget implements HasText<Span> {

	public Span() {
		setElement(Document.get().createSpanElement());
	}

	@Override
	public String getText() {
		return getElement().getInnerText();
	}

	@Override
	public void setText(String text) {
		getElement().setInnerText(text);
	}

    @Override
    public Span withText(String text) {
        setText(text);
        return this;
    }
}
