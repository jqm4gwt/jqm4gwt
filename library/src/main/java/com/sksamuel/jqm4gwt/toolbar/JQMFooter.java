package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasJqmFooter;
import com.sksamuel.jqm4gwt.HasText;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 21:21:13
 *
 *         This class models a JQM Footer element. It can contain an arbitary
 *         number of other widgets.
 *
 * @link
 *       http://jquerymobile.com/demos/1.2.0/docs/toolbars/docs-footers.html
 *
 */
public class JQMFooter extends JQMToolbar implements HasText<JQMFooter>, HasJqmFooter {

	/**
	 * Create a new empty {@link JQMFooter}. Use this when you want a blank
	 * footer that you will add widgets to later.
	 */
	public JQMFooter() {
		this((String) null);
	}

	/**
	 * Creates a new footer with the given text as a h1 element
	 *
	 * @param text
	 *              the text to use in the h1 element
	 */
	public JQMFooter(String text) {
		super("footer", "jpm4gwt-footer", text);
	}

	/**
	 * Create a new footer containing the given array of widgets.
	 *
	 * @param widgets
	 *              the array of widgets to add to the footer bar
	 */
	public JQMFooter(Widget... widgets) {
		this((String) null);
		for (Widget widget : widgets) {
			add(widget);
		}
	}

    @Override
    public JQMFooter withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public JQMFooter getJqmFooter() {
        return this;
    }

    @Override
    public Composite getFooterStage() {
        return this;
    }
}
