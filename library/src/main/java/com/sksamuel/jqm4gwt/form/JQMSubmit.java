package com.sksamuel.jqm4gwt.form;

import com.google.gwt.user.client.ui.SubmitButton;
import com.sksamuel.jqm4gwt.button.JQMButton;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 *
 *         An implementation of a submit button. Submit buttons are tightly
 *         integrated with forms and have special semantics when added to a
 *         {@link JQMForm}
 *
 */
public class JQMSubmit extends JQMButton {

    /** Nullary constructor. */
    public JQMSubmit() {
        this(null);
    }

	/**
	 * Create a {@link JQMSubmit} with the given label
	 */
	public JQMSubmit(String text) {
		super(new SubmitButton(text));
		addStyleName("jqm4gwt-submit");
	}
}
