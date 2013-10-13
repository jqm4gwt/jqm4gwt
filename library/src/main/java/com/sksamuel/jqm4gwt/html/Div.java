package com.sksamuel.jqm4gwt.html;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * An implemenation of a <div> element exposed as a widget. Shorter equivalent of FlowPanel.
 *
 */
public class Div extends FlowPanel {

    public Div() {
    }

    public String getText() {
        return getElement().getInnerText();
    }

    public void setText(String text) {
        getElement().setInnerText(text);
    }

}
