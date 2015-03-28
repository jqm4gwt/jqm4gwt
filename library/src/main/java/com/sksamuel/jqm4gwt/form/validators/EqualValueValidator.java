package com.sksamuel.jqm4gwt.form.validators;

import com.google.gwt.user.client.ui.HasValue;

/**
 * @author Stephen K Samuel samspade79@gmail.com 14 Jul 2011 13:49:01
 *
 * <br> A implementation of {@link Validator} that checks that two fields are
 *      either both null or that they have the same value.
 *
 */
public class EqualValueValidator extends CompareStringValueValidator {

    public EqualValueValidator(HasValue<String> hasValue1, HasValue<String> hasValue2, String msg) {
        super(hasValue1, hasValue2, msg);
	}

    @Override
    protected boolean areValuesValid(String val1, String val2) {
        return val1.equals(val2);
    }

}
