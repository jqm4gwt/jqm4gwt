package com.sksamuel.jqm4gwt.form.validators;

import com.google.gwt.user.client.ui.HasValue;

/**
 * @author Stephen K Samuel samspade79@gmail.com 14 Jul 2011 13:49:01
 * 
 *         A implementation of {@link Validator} that checks that two fields are
 *         either both null or that they have the same value.
 * 
 */
public class EqualValueValidator implements Validator {

	private final HasValue<String>	hasValue1;
	private final HasValue<String>	hasValue2;
	private final String			msg;

	public EqualValueValidator(HasValue<String> hasValue1, HasValue<String> hasValue2, String msg) {
		this.hasValue1 = hasValue1;
		this.hasValue2 = hasValue2;
		this.msg = msg;
	}

	@Override
	public String validate() {
		String value1 = hasValue1.getValue();
		String value2 = hasValue2.getValue();
		if (value1 == null || value1.trim().length() == 0)
			return null;
		if (value2 == null || value2.trim().length() == 0)
			return null;
		if (value1.equals(value2))
			return null;

		return msg;
	}

}
