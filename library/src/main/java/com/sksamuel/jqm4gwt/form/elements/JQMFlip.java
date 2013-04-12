package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:21:09
 * 
 *         Implementation of a JQuery Model Flip element. This is like a two
 *         element {@link JQMRadioset} that is rendered like a "toggle" on an
 *         iphone.
 * 
 * @link 
 *       http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-switch
 *       .html
 * 
 */
public class JQMFlip extends JQMFieldContainer implements HasText, HasValue<String>, HasChangeHandlers,
		HasClickHandlers {

	private final FormLabel	label		= new FormLabel();

	/**
	 * The GWT widget to use as the select element
	 */
	private final ListBox	select	= new ListBox();

	private String		value2;

	private String		value1;

	/**
	 * Creates a new {@link JQMFlip} widget with the given label text and
	 * initialized with two options taking their details from the given
	 * parameters.
	 * 
	 * This constructor sets the label of each toggle to be the same as the
	 * value. If you want a seperate label and value use the JQMFlip(String,
	 * String, String, String, String) constructor.
	 * 
	 * @param text
	 *              the label text to use
	 * @param value1
	 *              the value and label of the first toggle
	 * @param value2
	 *              the value and label of the second toggle
	 * 
	 */
	public JQMFlip(String text, String value1, String value2) {
		this(text, value1, value1, value2, value2);
	}

	/**
	 * Creates a new {@link JQMFlip} widget with the given label text and
	 * initialized with two options taking their details from the given
	 * parameters.
	 * 
	 * @param text
	 *              the label text to use
	 * @param value1
	 *              the value of the first toggle
	 * @param label1
	 *              the text to display for the first toggle
	 * @param value2
	 *              the value of the second toggle
	 * @param label2
	 *              the text to display for the second toggle
	 * 
	 */
	public JQMFlip(String text, String value1, String label1, String value2, String label2) {
        this();
		setText(text);
		select.addItem(label1, value1);
		select.addItem(label2, value2);
		setValue1(value1);
		setValue2(value2);
	}

    public JQMFlip() {
        String id = Document.get().createUniqueId();
        label.setFor(id);
        select.setName(id);
        select.getElement().setId(id);
        select.getElement().setAttribute("data-role", "slider");
        add(label);
        add(select);
    }

    public String getLabel1() {
        return select.getItemText(0);
    }

    public void setLabel1(String label1) {
        if (select.getItemCount() > 1)
           select.setItemText(0, label1);
        else
            select.addItem(label1, getValue1());
    }

    public String getLabel2() {
        return select.getItemText(0);
    }

    public void setLabel2(String label2) {
        if (select.getItemCount() > 2)
           select.setItemText(1, label2);
        else
            select.addItem(label2, getValue2());
    }

    @Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return select.addChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return flow.addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return null;
	}

	public int getSelectedIndex() {
		return select.getSelectedIndex();
	}

	/**
	 * Returns the value of the legend text.
	 */
	@Override
	public String getText() {
		return label.getText();
	}

	/**
	 * Returns the currently selected value or null if there is no currently
	 * selected button
	 */
	@Override
	public String getValue() {
		switch (getSelectedIndex()) {
		case 0:
			return getValue1();
		case 1:
			return getValue2();
		default:
			return null;
		}
	}

	protected void refresh() {
		refresh(select.getElement().getId());
	}

	private native void refresh(String id) /*-{
								$wnd.$("#" + id).slider("refresh");
								}-*/;

	/**
	 * Sets the currently selected index.
	 */
	public void setSelectedIndex(int i) {
		select.setSelectedIndex(i);
		refresh();
	}

	/**
	 * Sets the value of the legend text.
	 */
	@Override
	public void setText(String text) {
		label.setText(text);
	}

	/**
	 * Sets the currently selected value
	 */
	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	/**
	 * Sets the currently selected value. Events are not fired.
	 */
	@Override
	public void setValue(String value, boolean ignored) {
		if (getValue1().equals(value))
			setSelectedIndex(0);
		else if (getValue2().equals(value))
			setSelectedIndex(0);
	}

	/**
	 * Sets the value of the first option to the given string
	 */
	public void setValue1(String newValue) {
		value1 = newValue;
	}

	/**
	 * Sets the value of the second option to the given string
	 */
	public void setValue2(String newValue) {
		value2 = newValue;
	}

    public String getValue2() {
        return value2;
    }

    public String getValue1() {
        return value1;
    }
}
