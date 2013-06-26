package com.sksamuel.jqm4gwt.form.elements;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 * 
 *         An implementation of the HTML5 number input type. On systems that do
 *         not support this, it will be degraded into a standard text element.
 * 
 *         On most mobile devices this input will result in the soft keyboard
 *         showing the number pad.
 * 
 * @link 
 *       http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-text.
 *       html
 * 
 */
public class JQMNumber extends JQMText {

	public JQMNumber() {
		this(null);
	}

	public JQMNumber(String text) {
		super(text);
		setType("number");
	}

}
