package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 12:46:07
 */
public class JQMRadio extends Widget implements HasText<JQMRadio>, HasMini<JQMRadio>, HasTheme<JQMRadio> {

    private FormLabel label;

    // we must use a text box as the standard GWT radio/check inputs
    // already have an associated label (but a div not a <label>) so we
    // can't use them. Textboxes do not have such a label so will work
    // for us, as long as we coerce the type attribute to radio.
    private final TextBox input = new TextBox();

    JQMRadio() {
        Element element = input.getElement();
        element.setId(Document.get().createUniqueId());
        element.setAttribute("type", "radio");
        setElement(element);
        setLabel(new FormLabel());
    }

    JQMRadio(String value, String text) {
        this();
        setValue(value);
        setText(text);
    }

    void setName(String name) {
        input.setName(name);
    }

    TextBox getInput() {
        return input;
    }

    FormLabel getLabel() {
       return label;
    }

    void setLabel(FormLabel label) {
        this.label = label;
        label.setFor(input.getElement().getId());
    }

    /**
     * Returns the current display text for this radio button
     *
     * @return the buttons display text
     */
    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public String getTheme() {
        return label.getElement().getAttribute("data-theme");
    }

    public String getValue() {
        return input.getValue();
    }

    @Override
    public boolean isMini() {
        return "true".equals(label.getElement().getAttribute("data-mini"));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        label.getElement().setAttribute("data-mini", String.valueOf(mini));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMRadio withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    /**
     * Sets the display text for this radio button
     */
    @Override
    public void setText(String text) {
        label.setText(text);
    }

    /**
     * Sets the value for this radio button
     */
    public void setValue(String value) {
        input.setValue(value);
    }

    @Override
    public void setTheme(String themeName) {
        if (themeName == null)
            label.getElement().removeAttribute("data-theme");
        else
            label.getElement().setAttribute("data-theme", themeName);
    }

    @Override
    public JQMRadio withTheme(String theme) {
        setTheme(theme);
        return this;
    }

    @Override
    public JQMRadio withText(String text) {
        setText(text);
        return this;
    }
}
