package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 22:24:12
 * 
 */
public interface JQMFormWidget extends IsWidget, HasValue<String>, HasBlurHandlers {

	/**
	 * Create and return a new Label for displaying errors and attach to the
	 * form widget.
	 */
	Label addErrorLabel();
}
