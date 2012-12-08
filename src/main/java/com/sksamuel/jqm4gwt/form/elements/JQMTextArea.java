package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
 *         <p/>
 *         An implementation of a standard HTML Textarea
 */
public class JQMTextArea extends JQMFieldContainer implements HasGridDimensions<JQMTextArea>, HasText, HasValue<String>, HasMini<JQMTextArea>, HasKeyDownHandlers, HasKeyUpHandlers, HasFocusHandlers, HasBlurHandlers, Focusable {

    private final FormLabel label = new FormLabel();

    private final TextArea input = new TextArea();

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
    public JQMTextArea setColumns(int cols) {
        input.getElement().setAttribute("cols", String.valueOf(cols));
        return this;
    }

    @Override
    public void setFocus(boolean focused) {
        input.setFocus(focused);
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
    public JQMTextArea setRows(int rows) {
        input.getElement().setAttribute("rows", String.valueOf(rows));
        return this;
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMTextArea setMini(boolean mini) {
        setAttribute("data-mini", String.valueOf(mini));
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


}
