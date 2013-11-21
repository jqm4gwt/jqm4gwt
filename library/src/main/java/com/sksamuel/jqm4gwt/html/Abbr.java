package com.sksamuel.jqm4gwt.html;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasText;

/**
 * An implemenation of a <abbr> element exposed as a widget.
 * <p/> The <abbr> tag indicates an abbreviation or an acronym, like "WWW" or "NATO".
 *
 * @author slavap
 *
 */
public class Abbr extends Widget implements HasText<Abbr> {

    public Abbr() {
        setElement(DOM.createElement("abbr"));
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
    public Abbr withText(String text) {
        setText(text);
        return this;
    }

}
