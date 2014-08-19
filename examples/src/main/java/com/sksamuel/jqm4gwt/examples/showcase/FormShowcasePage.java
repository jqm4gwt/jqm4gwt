package com.sksamuel.jqm4gwt.examples.showcase;

import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.form.JQMForm;
import com.sksamuel.jqm4gwt.form.JQMSubmit;
import com.sksamuel.jqm4gwt.form.elements.JQMEmail;
import com.sksamuel.jqm4gwt.form.elements.JQMNumber;
import com.sksamuel.jqm4gwt.form.elements.JQMSearch;
import com.sksamuel.jqm4gwt.form.elements.JQMSlider;
import com.sksamuel.jqm4gwt.form.elements.JQMTelephone;
import com.sksamuel.jqm4gwt.form.elements.JQMText;
import com.sksamuel.jqm4gwt.form.validators.RegexValidator;
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

    private static final String textPhoneRegex = "^(\\([0-9]{3}\\) ?|[0-9]{3}-?)[0-9]{3}-?[0-9]{4}$";
    private static final String textPhoneRegexComment = "408-678-1234 or (408) 678-1234 or 4086781234";
    private static final String textEmailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
    private static final String textEmailRegexComment = "john@abc.net";

	public FormShowcasePage() {
		JQMHeader h = new JQMHeader("Form Showcase");
		h.setBackButton(true);
	    add(h);
		add(new Paragraph("This page shows all the different form elements in use."));

		JQMForm form = new JQMForm();
		add(form);

		form.add(new JQMText("A text field"));
		form.add(new JQMSearch("A search field"));
		form.add(new JQMEmail("An email field"));
		form.add(new JQMNumber("A number field"));
		form.add(new JQMSlider("A slider"));

		JQMText userName = new JQMText("User Name");
		form.add(userName);
        form.setRequired(userName, "User Name is required", true);

        JQMEmail email = new JQMEmail("Email");
        form.add(email);
        form.addValidator(new RegexValidator(email,
                textEmailRegex, "Invalid " + email.getText()
                + ". Expected: " + textEmailRegexComment), email);

        JQMTelephone phone = new JQMTelephone("Phone");
        form.add(phone);
        form.addValidator(new RegexValidator(phone,
                textPhoneRegex, "Invalid " + phone.getText()
                + ". Expected: " + textPhoneRegexComment), phone);

		add(new JQMSubmit("Submit"));
		add(new JQMFooter("jqm4gwt open source project"));
	}

}
