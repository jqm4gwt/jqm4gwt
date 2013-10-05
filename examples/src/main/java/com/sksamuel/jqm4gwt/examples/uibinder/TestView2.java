package com.sksamuel.jqm4gwt.examples.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiField;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.form.JQMForm;
import com.sksamuel.jqm4gwt.form.SubmissionHandler;
import com.sksamuel.jqm4gwt.form.elements.JQMCheckbox;

/**
 * @author jraymond
 *         Date: 4/18/13
 *         Time: 12:19 PM
 */
public class TestView2 {

    interface UiBinder extends com.google.gwt.uibinder.client.UiBinder<JQMPage, TestView2> { }

    public static final UiBinder BINDER = GWT.create(TestView2.UiBinder.class);

    private JQMPage page = BINDER.createAndBindUi(this);

    @UiField
    JQMForm form;

    @UiField
    JQMPage testPage2;

    @UiField
    JQMCheckbox cbContentCentered;

    {
        form.setSubmissionHandler(new SubmissionHandler<JQMForm>() {
            @Override
            public void onSubmit(JQMForm form) {
                // Do something here.
            }
        });
        cbContentCentered.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                testPage2.setContentCentered(event.getValue());
            }
        });
    }

    public void show() {
        JQMContext.changePage(page);
    }
}
