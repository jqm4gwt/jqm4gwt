package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasGridDimensions;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasPlaceHolder;
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
 * <br>
 * An implementation of a standard HTML Textarea
 */
public class JQMTextArea extends JQMFieldContainer implements HasGridDimensions<JQMTextArea>,
        HasText<JQMTextArea>, HasValue<String>,
        HasReadOnly<JQMTextArea>, HasMini<JQMTextArea>, HasCorners<JQMTextArea>,
        HasPlaceHolder<JQMTextArea>, JQMFormWidget, HasBlurHandlers, Focusable,
        HasFocusHandlers, HasClickHandlers, HasTapHandlers, HasChangeHandlers,
        HasKeyDownHandlers, HasKeyUpHandlers, HasKeyPressHandlers {

    private final FormLabel label = new FormLabel();
    private final TextArea input = new TextArea();

    private Integer savedTabIndex = null;

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
     * @param text the display text for the label
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
     * @param text the display text for the label
     * @param cols the number of cols to display
     * @param rows the number of rows to display.
     */
    public JQMTextArea(String text, int cols, int rows) {
        this(text);
        withColumns(cols);
        withRows(rows);
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
        return JQMCommon.isMini(input);
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
    public JQMTextArea withColumns(int cols) {
        setColumns(cols);
        return this;
    }

    @Override
    public void setFocus(boolean focused) {
        input.setFocus(focused);
    }

    @Override
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public void setRows(int rows) {
        input.getElement().setAttribute("rows", String.valueOf(rows));
    }

    @Override
    public JQMTextArea withRows(int rows) {
        setRows(rows);
        return this;
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(input, mini);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMTextArea withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        input.setValue(value, fireEvents);
    }

    @Override
    public void setValue(String value) {
        setValue(value, false);
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return input.addBlurHandler(handler);
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return input.addFocusHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return input.addKeyUpHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return input.addKeyDownHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return input.addKeyPressHandler(handler);
    }

    @Override
    public JQMTextArea withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public boolean isReadOnly() {
        return input.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        input.setReadOnly(readOnly);
    }

    @Override
    public JQMTextArea withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }

    @Override
    public String getPlaceHolder() {
        return input.getElement().getAttribute(HasPlaceHolder.ATTRIBUTE_PLACEHOLDER);
    }

    @Override
    public void setPlaceHolder(String placeHolderText) {
        input.getElement().setAttribute(HasPlaceHolder.ATTRIBUTE_PLACEHOLDER,placeHolderText);
    }

    @Override
    public JQMTextArea withPlaceHolder(String placeHolderText) {
        setPlaceHolder(placeHolderText);
        return this;
    }

    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return input.addChangeHandler(handler);
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
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public Label addErrorLabel() {
        return null;
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
    public JQMTextArea withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    public Element getInputElt() {
        return input.getElement();
    }

    public String getInputId() {
        return input.getElement().getId();
    }

    public void setInputId(String id) {
        input.getElement().setId(id);
        input.setName(id);
        label.setFor(id);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (!isEnabled()) {
            Element inputElt = getInputElt();
            if (savedTabIndex == null) savedTabIndex = inputElt.getTabIndex();
            inputElt.setTabIndex(-1);
        }
    }

    @Override
    public void setEnabled(boolean value) {
        boolean prevEnabled = isEnabled();
        super.setEnabled(value);
        if (prevEnabled == value) return;

        if (isAttached()) {
            Element inputElt = getInputElt();
            if (value) {
                if (savedTabIndex != null) {
                    inputElt.setTabIndex(savedTabIndex);
                    savedTabIndex = null;
                }
            } else {
                savedTabIndex = inputElt.getTabIndex();
                inputElt.setTabIndex(-1);
            }
        }
    }

    @Override
    public void setTabIndex(int value) {
        if (isAttached()) {
            if (isEnabled()) {
                getInputElt().setTabIndex(value);
                savedTabIndex = null;
            } else {
                savedTabIndex = value;
            }
        } else {
            input.setTabIndex(value);
            JQMCommon.setAttribute(getInputElt(), "tabindex", String.valueOf(value));
        }
    }

    @Override
    public void setTheme(String themeName) {
        super.setTheme(themeName);
        JQMCommon.setThemeEx(input.getElement(), themeName, JQMCommon.STYLE_UI_BODY);
    }
}
