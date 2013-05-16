package com.sksamuel.jqm4gwt.form.elements;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 * 
 *         Text element specialised for URLs
 * 
 */
public class JQMUrl extends JQMText {

	/**
	 * Create a new {@link JQMUrl} with no label text
	 * 
	 */
	public JQMUrl() {
		this(null);
	}

	/**
	 * Create a new {@link JQMUrl} with the given label text
	 * 
	 * @param text
	 *              the text to use as the label
	 */
	public JQMUrl(String text) {
		super(text);
		setType("url");
	}
}
