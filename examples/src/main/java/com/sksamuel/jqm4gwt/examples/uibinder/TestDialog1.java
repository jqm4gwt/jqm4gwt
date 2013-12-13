package com.sksamuel.jqm4gwt.examples.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.sksamuel.jqm4gwt.JQMDialog;

public class TestDialog1 {

    interface MyBinder extends UiBinder<JQMDialog, TestDialog1> {}

    public static final MyBinder binder = GWT.create(MyBinder.class);

    private JQMDialog dlg = binder.createAndBindUi(this);

    public JQMDialog getDialog() {
        return dlg;
    }

}
