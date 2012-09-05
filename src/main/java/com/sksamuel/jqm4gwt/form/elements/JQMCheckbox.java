package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 15:42:39
 * 
 * 
 */
public class JQMCheckbox implements HasText, IsChecked, HasValue<Boolean>, HasMini {

	private final InputElement	input;

	private final FormLabel		label;

	private final String		id;

	JQMCheckbox(InputElement input, FormLabel label, String id) {
		this.input = input;
		this.label = label;
		this.id = id;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return null;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
	}

	public String getId() {
		return id;
	}

	public InputElement getInput() {
		return input;
	}

	@Override
	public String getText() {
		return label.getText();
	}

	@Override
	public Boolean getValue() {
		return isSelected();
	}

	@Override
	public boolean isMini() {
		return "true".equals(input.getAttribute("data-mini"));
	}

	@Override
	public boolean isSelected() {
		String style = label.getStyleName();
		if (style == null)
			return false;
		if (style.contains("ui-btn-down")) {
			return !style.contains("ui-checkbox-on");
		} else {
			return style.contains("ui-checkbox-on");
		}

	}

	/**
	 * If set to true then renders a smaller version of the standard-sized element.
	 */
	@Override
	public void setMini(boolean mini) {
		input.setAttribute("data-mini", String.valueOf(mini));
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public void setValue(Boolean value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Boolean value, boolean ignored) {
		input.setChecked(value);
		input.setDefaultChecked(value);
	}
}
