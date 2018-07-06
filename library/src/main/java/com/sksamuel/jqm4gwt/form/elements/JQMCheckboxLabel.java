package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * The same as JQMCheckbox, but works right in case of dynamic setText() calls.
 */
public class JQMCheckboxLabel extends JQMCheckbox {

    @UiConstructor
    public JQMCheckboxLabel(String name, String text) {
        super(name, text);
    }

    @Override
    protected boolean isClassicInit() {
        return true;
    }
}
