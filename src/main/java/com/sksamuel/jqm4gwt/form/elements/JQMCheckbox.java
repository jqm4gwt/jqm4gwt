package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 15:42:39
 */
public class JQMCheckbox implements HasText, IsChecked, HasValue<Boolean>, HasMini<JQMCheckbox> {

    private final InputElement input;

    private final FormLabel label;

    private final String id;

    private boolean isSelected;

    JQMCheckbox(InputElement input, FormLabel label, String id) {
        this.input = input;
        this.label = label;
        this.id = id;

        label.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                isSelected = !isSelected;
            }
        }, ClickEvent.getType());
    }

    ;
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
    public JQMCheckbox setMini(boolean mini) {
        input.setAttribute("data-mini", String.valueOf(mini));
        return this;
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
        isSelected = value;
    }
}
