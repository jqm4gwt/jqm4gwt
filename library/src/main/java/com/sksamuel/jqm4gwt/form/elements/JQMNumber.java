package com.sksamuel.jqm4gwt.form.elements;

import com.sksamuel.jqm4gwt.JQMCommon;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 *
 *         An implementation of the HTML5 number input type. On systems that do
 *         not support this, it will be degraded into a standard text element.
 *
 *         On most mobile devices this input will result in the soft keyboard
 *         showing the number pad.
 *
 * http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-text.html
 *
 */
public class JQMNumber extends JQMText {

	public JQMNumber() {
		this(null);
	}

	public JQMNumber(String text) {
		super(text);
		setType("number");
		setStep("any");
	}

	public String getStep() {
	    return JQMCommon.getAttribute(input.getElement(), "step");
	}

    /**
     * Some browsers (for instance Firefox) are quite strict about values entered into number widget.
     * Default is any, which allows entering fractional values, though up/down buttons will increment/decrement by 1.
     *
     * @param value - any, 1, 0.1, 0.01, ...
     */
	public void setStep(String value) {
	    JQMCommon.setAttribute(input.getElement(), "step", value);
	}

}
