package com.sksamuel.jqm4gwt.form.elements;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 *
 *         Text element specialised for email
 * http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-text.html
 */
public class JQMEmail extends JQMText {

    /**
     * Creates a new {@link JQMEmail} with no label text
     */
    public JQMEmail() {
        this(null);
    }

    /**
     * Creates a new {@link JQMEmail} with the given label text
     *
     * @param text the text to use for the label
     */
    public JQMEmail(String text) {
        super(text);
        setType("email");
    }

}
