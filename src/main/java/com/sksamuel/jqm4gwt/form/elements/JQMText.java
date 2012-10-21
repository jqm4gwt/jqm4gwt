package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasPreventFocusZoom;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 May 2011 13:49:09
 * 
 *         An implementation of a standard HTML text input.
 * 
 */
public class JQMText extends JQMWidget implements HasText, HasFocusHandlers, HasClickHandlers, HasChangeHandlers, HasValue<String>,
		JQMFormWidget, HasKeyDownHandlers, HasKeyUpHandlers, HasMouseOverHandlers, HasMouseOutHandlers, HasPreventFocusZoom, HasMini,
		Focusable {

	/**
	 * The widget used for the label
	 */
	private final FormLabel	label;

	/**
	 * The widget used for the input element
	 */
	private final TextBox	input;

	/**
	 * The main container used as the basis for this widget
	 */
	private final FlowPanel	flow;

	/**
	 * Create a new {@link JQMText} element with no label
	 */
	public JQMText() {
		this(null);
	}

	/**
	 * Create a new {@link JQMText} element with the given label text
	 */
	public JQMText(String text) {
		String id = Document.get().createUniqueId();

		flow = new FlowPanel();
		initWidget(flow);

		label = new FormLabel();
		label.setFor(id);

		input = new TextBox();
		input.getElement().setId(id);
		input.setName(id);

		flow.add(label);
		flow.add(input);

		setText(text);
		setDataRole("fieldcontain");
		addStyleName("jqm4gwt-fieldcontain");

		setId();
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return input.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return input.addChangeHandler(handler);
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
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return input.addFocusHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return input.addKeyDownHandler(handler);
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return input.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return flow.addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return flow.addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return input.addValueChangeHandler(handler);
	}

	public void disable() {
		disable(input.getElement().getId());
	}

	private native void disable(String id)/*-{
								$("#" + id).textinput('disable');
								}-*/;

	public void enable() {
		enable(input.getElement().getId());
	}

	private native void enable(String id) /*-{
								$("#" + id).textinput('enable');
								}-*/;

	@Override
	public String getId() {
		return input.getElement().getId();
	}

	@Override
	public int getTabIndex() {
		return input.getTabIndex();
	}

	/**
	 * Returns the text of the label
	 */
	@Override
	public String getText() {
		return label == null ? null : label.getText();
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
	public boolean isPreventFocusZoom() {
		return "true".equals(input.getElement().getAttribute("data-prevent-focus-zoom"));
	}

	@Override
	public void setAccessKey(char key) {
		input.setAccessKey(key);
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

	/**
	*This option disables page zoom temporarily when a custom select is focused, which prevents iOS devices from zooming the page into the select. 
	*By default, iOS often zooms into form controls, and the behavior is often unnecessary and intrusive in mobile-optimized layouts.
	*/
	@Override
	public void setPreventFocusZoom(boolean b) {
		setAttribute("data-prevent-focus-zoom", String.valueOf(b));
	}

	@Override
	public void setTabIndex(int index) {
		input.setTabIndex(index);
	}

	/**
	 * Set the text of the label to the given @param text
	 */
	@Override
	public void setText(String text) {
		label.setText(text);
	}

	protected void setType(String type) {
		input.getElement().setAttribute("type", type);
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
