package com.sksamuel.jqm4gwt.html;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * An implemenation of a &lt;div> element exposed as a widget. Shorter equivalent of FlowPanel.
 *
 */
public class Div extends FlowPanel {
    // implements HasText<Div> or HasHTML<Div> cannot be declared!
    // Otherwise UiBinder <h:Div><b:JQMButton/></h:Div> will be broken.

    public Div() {
    }

    public String getText() {
        return getElement().getInnerText();
    }

    public void setText(String text) {
        getElement().setInnerText(text);
    }

    public Div withText(String text) {
        setText(text);
        return this;
    }

    public String getHTML() {
        return getElement().getInnerHTML();
    }

    public void setHTML(String html) {
        getElement().setInnerHTML(html);
    }

    public Div withHTML(String html) {
        setHTML(html);
        return this;
    }

}
