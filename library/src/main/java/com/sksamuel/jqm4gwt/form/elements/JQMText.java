package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
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
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.HasClearButton;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasPlaceHolder;
import com.sksamuel.jqm4gwt.HasPreventFocusZoom;
import com.sksamuel.jqm4gwt.HasReadOnly;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 May 2011 13:49:09
 *         <p/>
 *         An implementation of a standard HTML text input.
 */
public class JQMText extends JQMFieldContainer implements HasText<JQMText>, HasFocusHandlers,
        HasClickHandlers, HasTapHandlers, HasChangeHandlers, HasValue<String>, HasReadOnly<JQMText>,
        JQMFormWidget, HasKeyDownHandlers, HasKeyUpHandlers, HasMouseOverHandlers,
        HasMouseOutHandlers, HasPreventFocusZoom, HasMini<JQMText>,
        HasPlaceHolder<JQMText>, Focusable, HasClearButton<JQMText>, HasCorners<JQMText> {

    /**
     * The widget used for the label
     */
    protected final FormLabel label;

    /**
     * The widget used for the input element
     */
    protected final TextBox input;

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

        label = new FormLabel();
        label.setFor(id);

        input = new TextBox();
        input.getElement().setId(id);
        input.setName(id);

        add(label);
        add(input);

        setText(text);
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
    public HandlerRegistration addTapHandler(TapHandler handler) {
        // this is not a native browser event so we will have to manage it via JS
        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, JQMComponentEvents.TAP_EVENT, TapEvent.getType());
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
        disable(input.getElement());
    }

    private static native void disable(Element elt)/*-{
        $wnd.$(elt).textinput('disable');
    }-*/;

    public void enable() {
        enable(input.getElement());
    }

    private static native void enable(Element elt) /*-{
        $wnd.$(elt).textinput('enable');
    }-*/;

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
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMText withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    /**
     * This option disables page zoom temporarily when a custom select is focused, which prevents iOS devices from zooming the page into the select.
     * By default, iOS often zooms into form controls, and the behavior is often unnecessary and intrusive in mobile-optimized layouts.
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

    @Override
    public JQMText withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public boolean isReadOnly() {
        return input.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly)
    {
        input.setReadOnly(readOnly);
    }

    @Override
    public JQMText withReadOnly(boolean readOnly)
    {
        setReadOnly(readOnly);
        return this;
    }

    @Override
    public String getPlaceHolder()
    {
        return input.getElement().getAttribute(HasPlaceHolder.ATTRIBUTE_PLACEHOLDER);
    }

    @Override
    public void setPlaceHolder(String placeHolderText) {
        input.getElement().setAttribute(HasPlaceHolder.ATTRIBUTE_PLACEHOLDER,placeHolderText);
    }

    @Override
    public JQMText withPlaceHolder(String placeHolderText) {
        setPlaceHolder(placeHolderText);
        return this;
    }

    public String getInputId() {
        return input.getElement().getId();
    }

    public void setInputId(String id) {
        input.getElement().setId(id);
        input.setName(id);
        label.setFor(id);
    }

    /**
     * Could be useful when submitting form to external service with pre-required element names.
     * See <a href="http://jquerymobile.com/demos/1.2.1/docs/forms/forms-sample.html">Submitting Forms</a>
     */
    public void setInputName(String id) {
        input.setName(id);
    }

    public String getInputName() {
        return input.getName();
    }

    public void setInputAttrs(String attrs) {
        JQMCommon.setAttributes(input.getElement(), attrs);
    }

    public void removeInputAttrs(String attrs) {
        JQMCommon.removeAttributes(input.getElement(), attrs);
    }

    @Override
    public boolean isClearButton() {
        return JQMCommon.isClearButton(input);
    }

    @Override
    public void setClearButton(boolean value) {
        JQMCommon.setClearButton(input, value);
    }

    @Override
    public JQMText withClearButton(boolean value) {
        setClearButton(value);
        return this;
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCorners(input);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCorners(input, corners);
    }

    @Override
    public JQMText withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }
}
