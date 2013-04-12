package com.sksamuel.jqm4gwt.examples.helloworld;

import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 18:00:50
 * 
 *         A starter example that shows header, footer and some text saying
 *         "hello world"
 * 
 */
public class HelloWorldPage extends JQMPage {

	public HelloWorldPage() {
		super("helloworld");

		JQMHeader h = new JQMHeader("Hello world header");
		add(h);

		add(new Paragraph("Hello world. Boy am I original!"));

		JQMFooter f = new JQMFooter("Hello world footer");
		add(f);
	}

}
