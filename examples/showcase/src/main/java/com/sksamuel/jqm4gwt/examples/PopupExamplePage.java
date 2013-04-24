package com.sksamuel.jqm4gwt.examples;

import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPopup;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.button.JQMButtonGroup;

/**
 * @author Stephen K Samuel samspade79@gmail.com 16 Sep 2012 00:52:48
 *
 */
public class PopupExamplePage extends JQMPage {

	public PopupExamplePage() {
        withContainerId();
		setHeader("Popups");

		JQMPopup popup = new JQMPopup();
		popup.add(new Label("This is a popup with the ui-content class added to the popup container."));
		popup.setPadding(true);
		JQMButton button = new JQMButton("Popup with padding", popup);
		add(button);
		add(popup);

		{
			popup = new JQMPopup();
			popup.add(new Label("I am positioned to the window."));
			popup.setPosition("window");
			JQMButton button1 = new JQMButton("Position to window", popup);
			add(popup);

			popup = new JQMPopup();
			popup.add(new Label("I am positioned over the origin."));
			JQMButton button2 = new JQMButton("Position to origin", popup);

			add(new JQMButtonGroup(button1, button2).withHorizontal());
			add(popup);

		}

		{

			popup = new JQMPopup(new Label("No transition")).setPadding(true);
			add(popup);
			add(new JQMButton("No transition", popup).withInline(true));

			for (Transition t : Transition.values()) {

				popup = new JQMPopup(new Label("I am a " + t.getJQMValue() + " transition")).setPadding(true);
				add(popup.withTransition(t));
				add(new JQMButton(t.getJQMValue(), popup).withInline(true));

			}
		}
	}
}
