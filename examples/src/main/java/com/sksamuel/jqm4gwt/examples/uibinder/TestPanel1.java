package com.sksamuel.jqm4gwt.examples.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.toolbar.JQMPanel;

public class TestPanel1 {

    @UiField
    JQMButton infoBtn;

    private static TestPanel1UiBinder uiBinder = GWT.create(TestPanel1UiBinder.class);

    interface TestPanel1UiBinder extends UiBinder<Widget, TestPanel1> {
    }

    private final JQMPanel panel = (JQMPanel) uiBinder.createAndBindUi(this);

    public TestPanel1() {
        infoBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.alert("Info button pressed.");
            }
        });
    }

    public JQMPanel getPanel() {
        return panel;
    }

}
