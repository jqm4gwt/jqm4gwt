package com.sksamuel.jqm4gwt.form.elements;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.HasOrientation;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.form.JQMFieldset;
import com.sksamuel.jqm4gwt.html.FormLabel;
import com.sksamuel.jqm4gwt.html.Legend;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 May 2011 08:17:31
 * 
 *         A widget that is composed of 1 or more checkboxes.
 * 
 *         The child checkboxes are grouped together and can be set to be
 *         vertical or horizontal.
 * 
 * @link http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-
 *       checkboxes.html
 * 
 */
public class JQMCheckset extends JQMFieldContainer implements HasText, HasSelectionHandlers<String>, HasOrientation<JQMCheckset>,
		HasClickHandlers, JQMFormWidget {

	private JQMFieldset		fieldset;

	private Legend			legend;

	private final List<TextBox>		inputs	= new ArrayList<TextBox>();
	private final List<FormLabel>		labels	= new ArrayList<FormLabel>();
	private final List<JQMCheckbox>	checks	= new ArrayList<JQMCheckbox>();

	/**
	 * Creates a new {@link JQMCheckset} with no label text
	 */
	public JQMCheckset() {
		this(null);
	}

	/**
	 * Creates a new {@link JQMCheckset} with the label set to the given text.
	 * 
	 * @param text
	 *              the display text for the label
	 */
	public JQMCheckset(String text) {
		setupFieldset(text);
	}

	private void setupFieldset(String labelText) {
		if(fieldset != null) remove(fieldset);
		fieldset = new JQMFieldset();
		add(fieldset);

		legend = new Legend();
		fieldset.add(legend);

		setText(labelText);
	}
	
	@Override
	public HandlerRegistration addBlurHandler(final BlurHandler handler) {
		for (FormLabel label : labels)
			label.addDomHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					handler.onBlur(null);
				}
			}, ClickEvent.getType());
		return null;
	}

	/**
	 * Add a new check option to the checkset.
	 * 
	 * @param id
	 *              the name of the checkbox
	 * @param text
	 *              the text to display for the checkbox
	 * 
	 * @return the {@link JQMCheckbox} instance used to control the added
	 *         checkbox
	 */
	public JQMCheckbox addCheck(String id, String text) {

		TextBox input = new TextBox();
		input.setName(id);
		input.getElement().setId(id);
		input.getElement().setAttribute("type", "checkbox");
		inputs.add(input);

		FormLabel label = new FormLabel();
		label.setFor(id);
		label.setText(text);
		labels.add(label);

		fieldset.add(input);
		fieldset.add(label);

		InputElement e = input.getElement().cast();
		final JQMCheckbox check = new JQMCheckbox(e, label, id);
		checks.add(check);
		return check;
	}

	public void clear() {
		inputs.clear();
		labels.clear();
		checks.clear();
		setupFieldset(getText());
	}
	
    @Override
    public JQMWidget setTheme(String themeName) {
    	super.setTheme(themeName);
    	for(TextBox checkInput : inputs) applyTheme(checkInput, themeName);
        return this;
    }
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public Label addErrorLabel() {
		return null;
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
		return null;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return null;
	}

	/**
	 * Returns the value of the legend text
	 */
	@Override
	public String getText() {
		return legend.getText();
	}

	/**
	 * Returns the id of any checkbox that is checked or null if no checkbox
	 * in this checkset has been selected
	 * 
	 * @return the first selected value
	 */
	@Override
	public String getValue() {
		for (JQMCheckbox box : checks) {
			if (box.isSelected())
				return box.getId();
		}
		return null;
	}

	private String getValue(Element element) {
		while (element != null) {
			if ("label".equalsIgnoreCase(element.getTagName())
					&& element.getAttribute("class") != null
					&& (element.getAttribute("class").contains("ui-btn-active") || element.getAttribute("class")
							.contains("ui-btn-down")))
				return element.getAttribute("for");
			String value = getValue(element.getFirstChildElement());
			if (value != null)
				return value;
			element = element.getNextSiblingElement();
		}
		return null;
	}

	private native void getValueC(String id) /*-{
								alert($wnd.$('#' + id).is(':checked'));
								}-*/;

	/**
	 * Returns true if at least one checkbox in this checkset is selected.
	 */
	public boolean hasSelection() {
		return getValue() != null;
	}

	@Override
	public boolean isHorizontal() {
		return fieldset.isHorizontal();
	}

	/**
	 * Returns true if the checkbox with the given id is selected.
	 * 
	 * @param id
	 *              the id of the checkbox to test
	 */
	public boolean isSelected(String id) {
		for (JQMCheckbox box : checks) {
			if (id.equals(box.getId()))
				return box.getValue();
		}
		return false;
	}

	@Override
	public boolean isVertical() {
		return fieldset.isVertical();
	}

	public void removeCheck(String id, String label) {
		// TODO traverse all elements removing anything that has a "for" for
		// this id or actually has this id
	}

	@Override
	public JQMCheckset setHorizontal() {
		fieldset.setHorizontal();
		return this;
	}

	/**
	 * Sets the value of the legend text.
	 */
	@Override
	public void setText(String text) {
		legend.setText(text);
	}

	/**
	 * Sets the checkbox with the given value to be selected
	 */
	@Override
	public void setValue(String id) {
		for (JQMCheckbox box : checks) {
			if (id.equals(box.getId()))
				box.setValue(true);
		}
	}

	@Override
	public void setValue(String id, boolean ignored) {
		// for (TextBox check : checks) {
		// if (id.equals(check.getValue())) {
		// check.getElement().setAttribute("defaultChecked", "true");
		// check.getElement().setAttribute("checked", "true");
		// return;
		// }
		// }
	}

	@Override
	public JQMCheckset setVertical() {
		fieldset.setVertical();
		return this;
	}
}
