package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.user.client.ui.HasText;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 12:46:07
 */
public class JQMRadio implements HasText, HasMini<JQMRadio>, HasTheme<JQMRadio> {

    private final FormLabel label;

    JQMRadio(FormLabel label) {
        this.label = label;
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
        return label.getElement().getAttribute("theme");
    }

    JQMRadio getValue() {
        return null;
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

    @Override
    public void setTheme(String theme) {
        label.getElement().setAttribute("theme", String.valueOf(theme));
    }

    @Override
    public JQMRadio withTheme(String theme) {
        setTheme(theme);
        return this;
    }

}
