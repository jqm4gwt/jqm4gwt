package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasText;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 05:36:02
 * 
 *         Implementation of a
 *         <p>
 *         element
 * 
 */
public class Paragraph extends Widget implements HasText<Paragraph> {

	public Paragraph() {
		setElement(Document.get().createPElement());
	}

	public Paragraph(String text) {
		this();
		getElement().setInnerText(text);
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
    public Paragraph withText(String text) {
        setText(text);
        return this;
    }
}
