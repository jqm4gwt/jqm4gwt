package com.sksamuel.jqm4gwt.html;

import com.sksamuel.jqm4gwt.HasHTML;
import com.sksamuel.jqm4gwt.HasText;

/**
 * An implementation of a &lt;div&gt; element exposed as a widget. Shorter equivalent of FlowPanel.
 * <br> Cannot have child widgets, this class is supposed to work with text/html content only.
 **/
public class DivText extends Div implements HasText<DivText>, HasHTML<DivText> {

    @Override
    public DivText withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public DivText withHTML(String html) {
        setHTML(html);
        return this;
    }
}
