package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextArea;
import com.sksamuel.jqm4gwt.HasGridDimensions;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 May 2011 13:49:09
 * 
 *         An implementation of a standard HTML Textarea
 * 
 */
public class JQMTextArea extends JQMFieldContainer implements HasGridDimensions, HasText, HasValue<String>, HasMini, Focusable {

	private final FormLabel	label	= new FormLabel();

	private final TextArea	input	= new TextArea();

	/**
	 * Create a new {@link JQMTextArea} with no label text
	 */
	public JQMTextArea() {
		this(null);
	}

	/**
	 * Create a new {@link JQMTextArea} with the given label text and with the
	 * default size
	 * 
	 * @param text
	 *              the display text for the label
	 */
	public JQMTextArea(String text) {
		String id = Document.get().createUniqueId();

		setText(text);
		label.setFor(id);

		input.getElement().setId(id);
		input.setName(id);

		add(label);
		add(input);
	}

	/**
	 * Create a new {@link JQMTextArea} with the given label text and with the
	 * specified number of columns and rows.
	 * 
	 * @param text
	 *              the display text for the label
	 * 
	 * @param cols
	 *              the number of cols to display
	 * @param rows
	 *              the number of rows to display.
	 */
	public JQMTextArea(String text, int cols, int rows) {
		this(text);
		setColumns(cols);
		setRows(rows);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return input.addValueChangeHandler(handler);
	}

	@Override
	public int getColumns() {
		return Integer.parseInt(input.getElement().getAttribute("cols"));
	}

	@Override
	public int getRows() {
		return Integer.parseInt(input.getElement().getAttribute("rows"));
	}

	@Override
	public int getTabIndex() {
		return input.getTabIndex();
	}

	@Override
	public String getText() {
		return label.getText();
	}

	@Override
	public String getValue() {
		return input.getValue();
	}

	@Override
	public boolean isMini() {
		return "true".equals(getAttribute("data-mini"));
	}

	@Override
	public void setAccessKey(char key) {
		input.setAccessKey(key);
	}

	@Override
	public void setColumns(int cols) {
		input.getElement().setAttribute("cols", String.valueOf(cols));
	}

	@Override
	public void setFocus(boolean focused) {
		input.setFocus(focused);
	}

	/**
	 * If set to true then renders a smaller version of the standard-sized element.
	 */
	@Override
	public void setMini(boolean mini) {
		setAttribute("data-mini", String.valueOf(mini));
	}

	@Override
	public void setRows(int rows) {
		input.getElement().setAttribute("rows", String.valueOf(rows));
	}

	@Override
	public void setTabIndex(int index) {
		input.setTabIndex(index);
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		input.setValue(value, fireEvents);
	}
}
