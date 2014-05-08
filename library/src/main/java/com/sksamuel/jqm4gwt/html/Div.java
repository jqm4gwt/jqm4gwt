package com.sksamuel.jqm4gwt.html;

import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.HasText;

/**
 * An implemenation of a <div> element exposed as a widget. Shorter equivalent of FlowPanel.
 *
 */
public class Div extends FlowPanel implements HasText<Div> {

    public Div() {
    }

    @Override
    public String getText() {
        return getElement().getInnerText();
    }

    @Override
    public void setText(String text) {
        getElement().setInnerText(text);
    }

    @Override
    public Div withText(String text) {
        setText(text);
        return this;
    }

}
