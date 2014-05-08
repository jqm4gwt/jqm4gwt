package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasHTML;
import com.sksamuel.jqm4gwt.HasText;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 13:38:38
 *
 * <p/> An implemenation of a &lt;label> element
 *
 */
public class FormLabel extends Widget implements HasText<FormLabel>, HasHTML<FormLabel> {

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

    @Override
    public FormLabel withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public String getHTML() {
        return getElement().getInnerHTML();
    }

    @Override
    public void setHTML(String html) {
        getElement().setInnerHTML(html);
    }

    @Override
    public FormLabel withHTML(String html) {
        setHTML(html);
        return this;
    }
}
