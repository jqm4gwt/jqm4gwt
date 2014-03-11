package com.sksamuel.jqm4gwt.examples.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.toolbar.JQMPanel;

public class TestPanel1 {

    private static TestPanel1UiBinder uiBinder = GWT.create(TestPanel1UiBinder.class);

    interface TestPanel1UiBinder extends UiBinder<Widget, TestPanel1> {
    }

    private final JQMPanel panel = (JQMPanel) uiBinder.createAndBindUi(this);

    public TestPanel1() {
    }

    public JQMPanel getPanel() {
        return panel;
    }

}
