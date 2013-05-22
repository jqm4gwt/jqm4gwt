package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 * 
 *         Text element specialised for telephone number
 * 
 */
public class JQMTelephone extends JQMText {

	/**
	 * Creates a new {@link JQMTelephone} with no label text
	 */
	public JQMTelephone() {
		this(null);
	}

	/**
	 * Creates a new {@link JQMTelephone} with the given label text
	 * 
	 * @param text
	 *              the text to use for the label
	 */
	public @UiConstructor
    JQMTelephone(String text) {
		super(text);
		setType("tel");
	}

}
