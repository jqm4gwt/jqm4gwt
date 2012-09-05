package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.user.client.ui.HasText;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 12:46:07
 * 
 */
public class JQMRadio implements HasText {

	private final FormLabel	label;

	JQMRadio(FormLabel label) {
		this.label = label;
	}

	/**
	 * Returns the current display text for this radio button
	 * 
	 * @return the buttons display text
	 */
	@Override
	public String getText() {
		return label.getText();
	}

	JQMRadio getValue() {
		return null;
	}

	/**
	 * Sets the display text for this radio button
	 */
	@Override
	public void setText(String text) {
		label.setText(text);
	}

}
