package com.sksamuel.jqm4gwt.examples.events;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMFlip;
import com.sksamuel.jqm4gwt.form.elements.JQMRadioset;
import com.sksamuel.jqm4gwt.form.elements.JQMSelect;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 18:00:50
 * 
 * 
 */
public class EventsDemoPage extends JQMPage {

	public EventsDemoPage() {
		super("eventspage");

		final JQMHeader header = new JQMHeader("Events demo");
		JQMButton right = new JQMButton("View source",
				"https://github.com/sksamuel/jqm4gwt-examples/blob/master/src/main/java/com/sksamuel/jqm4gwt/examples/events/EventsDemoPage.java");
		right.withBuiltInIcon(DataIcon.GEAR);
		right.setExternal();
		header.setRightButton(right);

		final JQMButton left = new JQMButton("ClickMe");
		left.withBuiltInIcon(DataIcon.STAR);
		left.setExternal();
		left.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				Window.alert("You clicked me!");
			}
		});
		header.setLeftButton(left);
		add(header);

		add(new Paragraph("Events are some of the most convenient part of GWT. "
				+ "The jqm4gwt project provides the usual DOM events on the JQuery Mobile widgetset."));

		add(new Paragraph("The follow button has an onClick handler registered. "
				+ "Click the button and you will see the handler call Window.alert()"));

		JQMButton button = new JQMButton("Click me!");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Hi there!");
			}
		});
		add(button);

		final JQMFlip flip = new JQMFlip("Flip me", "Shiraz", "Merlot");
		flip.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.alert("You seem to like " + flip.getValue());
			}
		});
		add(flip);

		final JQMButton button2 = new JQMButton("Change the flip");
		button2.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				flip.setSelectedIndex(flip.getSelectedIndex() + 1 % 2);
			}
		});
		add(button2);

		add(new Paragraph("We aren't just limited to static methods. The following is an example of combining events with JQM widgets."));

		final JQMRadioset set = new JQMRadioset();
		set.setText("Select your composer");
		set.addRadio("Morricone");
		set.addRadio("Newman");
		set.addRadio("Mansell");
		set.addRadio("Zimmer");
		set.setSelectedValue("Zimmer");
		add(set);

		final JQMButton button3 = new JQMButton("Change the radioset");
		button3.addClickHandler(new ClickHandler() {

			int	counter	= 0;

			@Override
			public void onClick(ClickEvent event) {
				String value = set.getValue(counter++ % set.size());
				set.setValue(value);

			}
		});
		add(button3);

		// select "change" event
		add(new Paragraph("Selects fire the usual change event. Try changing the selection below"));

		final JQMSelect select = new JQMSelect("A select box with onChange event");
		select.withNative(false);
		for (Transition t : Transition.values()) {
			select.addOption(t.name());
		}
		select.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Window.alert("You selected: " + select.getSelectedValue());
			}
		});
		add(select);

		final JQMFooter footer = new JQMFooter();
		button = new JQMButton("Footer Button 1");
		button.withBuiltInIcon(DataIcon.REFRESH);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.alert("Welcome to the jungle");
			}
		});
		footer.add(button);
		button = new JQMButton("Footer Button 2");
		button.withBuiltInIcon(DataIcon.GRID);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.alert("We've got fun and games");
			}
		});
		footer.add(button);

		add(footer);
	}
}
