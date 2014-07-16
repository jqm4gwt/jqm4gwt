package com.sksamuel.jqm4gwt.examples.transition;

import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 18:27:12
 *
 *         A demo page showing the different transitions available. Works by
 *         reshowing the same page over and over !
 *
 */
public class TransitionDemoPage extends JQMPage {

	public TransitionDemoPage(String id) {
		super(id);

		JQMHeader h = new JQMHeader("Transitions Demo");
		add(h);
		h.setBackButton(true);
		add(new Paragraph("This page shows how different transitions can be used on links."));
		add(new Paragraph("NOTE: The transitions don't seem to work properly on some Firefox browsers."));
		add(new Paragraph("This is a jQuery Mobile issue, not an issue with the JQM4GWT wrapper."));

		for (final Transition transition : Transition.values()) {
			JQMButton button = new JQMButton(transition.name(), new JQMPage() {
				{
					JQMHeader hdr = new JQMHeader(transition.name());
					hdr.setBackButton(true);
				    add(hdr);
					add(new Paragraph("I was transitioned in by " + transition.getJqmValue() + "- isn't that great!"));
				}
			}, transition);
			add(button);
		}

	}
}
