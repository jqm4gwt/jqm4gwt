package com.sksamuel.jqm4gwt.form.validators;

import com.google.gwt.user.client.ui.HasValue;

/**
 * @author Stephen K Samuel samspade79@gmail.com 13 Jul 2011 00:45:29
 *
 * <br><br> An implementation of {@link Validator} that tests that the value from
 *          a value producing {@link HasValue} instance matches a supplied regex.
 *
 */
public class RegexValidator implements Validator {

    private final HasValue<String>  hasValue;
    private final String            regex;
    private final String            msg;

    public RegexValidator(HasValue<String> hasValue, String regex) {
        this(hasValue, regex, "This field is not in the correct format");
    }

    public RegexValidator(HasValue<String> hasValue, String regex, String msg) {
        this.hasValue = hasValue;
        this.regex = regex;
        this.msg = msg;
    }

    @Override
    public String validate() {
        if (regex == null || regex.isEmpty()) return null;
        String value = hasValue.getValue();
        if (value == null || value.trim().length() == 0) return null;
        if (value.matches(regex)) return null;
        return msg;
    }

}
