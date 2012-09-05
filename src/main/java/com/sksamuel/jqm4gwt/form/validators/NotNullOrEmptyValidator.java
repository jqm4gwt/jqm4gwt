package com.sksamuel.jqm4gwt.form.validators;

import com.google.gwt.user.client.ui.HasValue;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 22:16:40
 * 
 *         An implementation of {@link Validator} that will test if the value
 *         produced from the {@link HasValue} instance is not null and not
 *         composed only of white space (ie, has length bigger than 0 after
 *         being trimmed).
 * 
 */
public class NotNullOrEmptyValidator implements Validator {

	private final String			msg;
	private final HasValue<String>	hasValue;

	public NotNullOrEmptyValidator(HasValue<String> hasValue, String msg) {
		this.hasValue = hasValue;
		this.msg = msg;
	}

	@Override
	public String validate() {
		String value = hasValue.getValue();
		return value == null || value.trim().length() == 0 ? msg : null;
	}

}
