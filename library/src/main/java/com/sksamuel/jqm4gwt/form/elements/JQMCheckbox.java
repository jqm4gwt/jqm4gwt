package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 15:42:39
 */
public class JQMCheckbox implements HasText<JQMCheckbox>, IsChecked, HasValue<Boolean>, HasMini<JQMCheckbox> {

    private InputElement input;

    private FormLabel label;

    private String id;

    private boolean isSelected;

    JQMCheckbox(InputElement input, FormLabel label, String id) {
        setInput(input);
        setLabel(label);
        setId(id);
    }

    /**
     * Constructor used by UiBinder. Should be followed by calls to set InputElement, Label and Id)
     */
    public JQMCheckbox() {

    }

    public FormLabel getLabel() {
        return label;
    }

    public void setLabel(FormLabel label) {
        this.label = label;

        label.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                isSelected = !isSelected;
            }
        }, ClickEvent.getType());
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
        return isSelected;
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        input.setAttribute("data-mini", String.valueOf(mini));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMCheckbox withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public JQMCheckbox withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public void setValue(Boolean value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Boolean value, boolean ignored) {
        input.setChecked(value);
        input.setDefaultChecked(value);
        isSelected = value;
    }

    public void setInput(InputElement input) {
        this.input = input;
    }

    public void setId(String id) {
        this.id = id;
    }
}
