package com.sksamuel.jqm4gwt.form.validators;

import com.google.gwt.user.client.ui.HasValue;

public abstract class CompareStringValueValidator implements Validator {

    private final HasValue<String>  hasValue1;
    private final HasValue<String>  hasValue2;
    private final String            msg;

    public CompareStringValueValidator(HasValue<String> hasValue1, HasValue<String> hasValue2,
                                       String validationMsg) {
        this.hasValue1 = hasValue1;
        this.hasValue2 = hasValue2;
        this.msg = validationMsg;
    }

    /**
     * @param val1 - already trim()
     * @param val2 - already trim()
     * @return - true if validation successful, i.e. no validation message should be shown.
     */
    protected abstract boolean areValuesValid(String val1, String val2);

    @Override
    public String validate() {
        String value1 = hasValue1.getValue();
        String value2 = hasValue2.getValue();
        if (value1 == null) return null;
        if (value2 == null) return null;
        value1 = value1.trim();
        if (value1.length() == 0) return null;
        value2 = value2.trim();
        if (value2.length() == 0) return null;
        if (areValuesValid(value1, value2)) return null;
        return msg;
    }
}
