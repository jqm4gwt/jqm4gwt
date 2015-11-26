package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * Standalone radio box, very similar to JQMCheckbox, but user cannot uncheck it.
 * <br> If some radio boxes have the same name then they will automatically work as radio set.
 **/
public class JQMRadiobox extends JQMCheckbox {

    @UiConstructor
    public JQMRadiobox(String name, String text) {
        super(name, text);
    }

    @Override
    protected String getInputType() {
        return "radio";
    }
}
