package com.sksamuel.jqm4gwt.form.validators;

import com.google.gwt.user.client.ui.HasValue;

public class NotEqualValueValidator extends CompareStringValueValidator {

    public NotEqualValueValidator(HasValue<String> hasValue1, HasValue<String> hasValue2, String msg) {
        super(hasValue1, hasValue2, msg);
    }

    @Override
    protected boolean areValuesValid(String val1, String val2) {
        return !val1.equals(val2);
    }
}
