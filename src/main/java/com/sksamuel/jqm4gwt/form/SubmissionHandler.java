package com.sksamuel.jqm4gwt.form;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 21:54:36
 * 
 *         A submission handler is invoked when a form is submitted after
 *         validation has succeeded.
 * 
 * @param T
 *              the type of the form that this handler will handle
 * 
 */
public interface SubmissionHandler<T extends JQMForm> {

	/**
	 * This method is invoked when a form has been submitted and validated.
	 * The parameter of type T will be the source form meaning a single
	 * {@link SubmissionHandler} can be used for multiple instances of a form
	 */
	void onSubmit(T form);
}
