package com.sksamuel.jqm4gwt.uibinder.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiField;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.form.JQMForm;
import com.sksamuel.jqm4gwt.form.SubmissionHandler;

/**
 * @author jraymond
 *         Date: 4/4/13
 *         Time: 11:02 AM
 */
public class TestView {

    interface UiBinder extends com.google.gwt.uibinder.client.UiBinder<JQMPage, TestView> { }

    public static final UiBinder BINDER = GWT.create(TestView.UiBinder.class);

    private JQMPage page = TestView.BINDER.createAndBindUi(this);

    @UiField
    JQMForm form;

    {
        form.setSubmissionHandler(new SubmissionHandler() {
            @Override
            public void onSubmit(JQMForm form) {
                // Do something here.
            }
        });
    }

    public void show() {
        JQMContext.changePage(page);
    }

}
