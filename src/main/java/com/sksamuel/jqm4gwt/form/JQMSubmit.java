package com.sksamuel.jqm4gwt.form;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SubmitButton;
import com.sksamuel.jqm4gwt.JQMWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 * 
 *         An implementation of a submit button. Submit buttons are tightly
 *         integrated with forms and have special semantics when added to a
 *         {@link JQMForm}
 * 
 */
public class JQMSubmit extends JQMWidget implements HasText, HasClickHandlers {

	private final SubmitButton	submit;

	/**
	 * Create a {@link JQMSubmit} with the given label
	 */
	public JQMSubmit(String text) {

		submit = new SubmitButton(text);
		initWidget(submit);

		setStyleName("jqm4gwt-submit");
		setDataRole("button");
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return submit.addClickHandler(handler);
	}

	@Override
	public String getText() {
		return submit.getText();
	}

	@Override
	public void setText(String text) {
		submit.setText(text);
	}
}
