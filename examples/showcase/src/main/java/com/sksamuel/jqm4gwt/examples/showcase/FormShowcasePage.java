package com.sksamuel.jqm4gwt.examples.showcase;

import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.form.JQMSubmit;
import com.sksamuel.jqm4gwt.form.elements.JQMEmail;
import com.sksamuel.jqm4gwt.form.elements.JQMNumber;
import com.sksamuel.jqm4gwt.form.elements.JQMSearch;
import com.sksamuel.jqm4gwt.form.elements.JQMSlider;
import com.sksamuel.jqm4gwt.form.elements.JQMText;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 18:43:06
 * 
 *         The showcase page is a single page with every element/event/etc under
 *         the sun all crammed in !
 * 
 */
public class FormShowcasePage extends JQMPage {

	public FormShowcasePage() {

		add(new JQMHeader("Showcase"));
		add(new Paragraph("This page shows all the different form elements in use."));

		add(new JQMText("A text field"));
		add(new JQMSearch("A search field"));
		add(new JQMEmail("An email field"));
		add(new JQMNumber("A number field"));
		add(new JQMSlider("A slider"));

		add(new JQMSubmit("Submit"));

		add(new JQMFooter("jqm4gwt open source project"));
	}

}
