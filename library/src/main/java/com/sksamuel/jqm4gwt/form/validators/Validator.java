package com.sksamuel.jqm4gwt.form.validators;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 16:25:54
 * 
 *         Implementations of this interface provide a means to validate a field
 *         and return an error.
 * 
 */
public interface Validator {

	/**
	 * Perform validator and return an error message if there was a failure
	 */
	String validate();
}
