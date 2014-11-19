package com.sksamuel.jqm4gwt.examples;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPopup;
import com.sksamuel.jqm4gwt.JQMPopupEvent;
import com.sksamuel.jqm4gwt.LoadingIndicator;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.button.JQMButtonGroup;
import com.sksamuel.jqm4gwt.html.Div;

/**
 * @author Stephen K Samuel samspade79@gmail.com 16 Sep 2012 00:52:48
 *
 */
public class PopupExamplePage extends JQMPage {

    public PopupExamplePage() {
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

            popup = new JQMPopup(new Label("No transition")).withPadding(true);
            add(popup);
            add(new JQMButton("No transition", popup).withInline(true));

            for (Transition t : Transition.values()) {

                popup = new JQMPopup(new Label("I am a " + t.getJqmValue() + " transition")).withPadding(true);
                add(popup.withTransition(t));
                add(new JQMButton(t.getJqmValue(), popup).withInline(true));

            }
        }

        {
            final JQMPopup popOpen = new JQMPopup();
            popOpen.add(new Label("popup.open() works"));
            popOpen.setPosition("#btnTestOpen");
            JQMButton btnTestOpen = new JQMButton("Test popup.open()");
            btnTestOpen.setId("btnTestOpen");
            btnTestOpen.setInline(true);
            btnTestOpen.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    popOpen.open();
                }
            });
            add(popOpen);
            add(new Div());
            add(btnTestOpen);
        }

        {
            final LoadingIndicator loading = new LoadingIndicator();
            loading.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
            loading.setVisible(false);
            JQMButton btnDynamicPop = new JQMButton("Test dynamic popup");
            btnDynamicPop.setId("btnDynamicPop");
            btnDynamicPop.setInline(true);
            btnDynamicPop.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    JQMPopup pop = new JQMPopup();
                    pop.add(new Label("Dynamic popup works"));
                    pop.setPosition("#btnDynamicPop");
                    Image img = new Image("http://www.hq.nasa.gov/alsj/a11/AS11-40-5903.jpg");
                    img.addStyleName("dynamicPopImg");
                    pop.add(img);
                    add(pop);
                    loading.setVisible(true);
                    pop.addPopupHandler(new JQMPopupEvent.DefaultHandler() {
                        @Override
                        public void onAfterOpen(JQMPopupEvent event) {
                            loading.setVisible(false);
                        }
                    });
                    pop.waitInitOpen(".dynamicPopImg");
                    // In case of static content (there are no dynamically loaded images),
                    // simpler code can be used to initialize popup:
                    // pop.initDynamic();
                    // pop.open();
                }
            });
            add(btnDynamicPop);
            add(loading);
        }

        {
            final JQMPopup pop = new JQMPopup();
            pop.add(new Label("penguin clicked"));
            pop.setPosition("#penguin");
            Image penguin = new Image("http://upload.wikimedia.org/wikipedia/commons/0/07/Emperor_Penguin_Manchot_empereur.jpg");
            penguin.setWidth("320px");
            penguin.addClickHandler( new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    pop.open();
                }
            });
            penguin.getElement().setId("penguin");
            add(pop);
            add(new Div());
            add(penguin);
        }
    }
}
