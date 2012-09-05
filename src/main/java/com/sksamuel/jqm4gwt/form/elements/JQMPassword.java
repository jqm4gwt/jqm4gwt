package com.sksamuel.jqm4gwt.form.elements;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:14:05
 * 
 *         An implementation of a standard HTML password input.
 * 
 */
public class JQMPassword extends JQMText {

	/**
	 * Creates a new {@link JQMPassword} with no label
	 */
	public JQMPassword() {
		this(null);
	}

	/**
	 * Creates a new {@link JQMPassword} with the given label text
	 */
	public JQMPassword(String text) {
		super(text);
		setType("password");
	}
}
