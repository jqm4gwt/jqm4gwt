package com.sksamuel.jqm4gwt.examples.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPopup;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.JQMForm;
import com.sksamuel.jqm4gwt.form.SubmissionHandler;
import com.sksamuel.jqm4gwt.form.elements.JQMSlider;
import com.sksamuel.jqm4gwt.form.elements.JQMText;

/**
 * @author jraymond
 *         Date: 4/4/13
 *         Time: 11:02 AM
 */
public class TestView1 {

    interface UiBinder extends com.google.gwt.uibinder.client.UiBinder<JQMPage, TestView1> { }

    public static final UiBinder BINDER = GWT.create(TestView1.UiBinder.class);

    public static JQMPage createPage() { return new TestView1().page; }

    private JQMPage page = TestView1.BINDER.createAndBindUi(this);

    private TestView2 nextView = new TestView2();

    @UiField
    JQMPopup popup;

    @UiField
    JQMButton popupOpenButton;

    @UiField
    JQMButton popupCloseButton;

    @UiField
    JQMText text;

    @UiField
    JQMButton disableTextButton;

    @UiField
    JQMButton enableTextButton;

    @UiField
    JQMSlider slider;

    @UiField
    JQMButton disableSliderButton;

    @UiField
    JQMButton enableSliderButton;

    @UiField
    JQMForm form;

    @UiHandler("popupOpenButton")
    void handlePopupOpenButtonClick(ClickEvent e) {
        popup.open();
    }

    @UiHandler("popupCloseButton")
    void handlePopupCloseButtonClick(ClickEvent e) {
        popup.close();
    }

    @UiHandler("disableTextButton")
    void handleDisableTextButtonClick(ClickEvent e) {
        text.disable();
    }

    @UiHandler("enableTextButton")
    void handleEnableTextButtonClick(ClickEvent e) {
        text.enable();
    }

    @UiHandler("disableSliderButton")
    void handleDisableSliderButtonClick(ClickEvent e) {
        slider.disable();
    }

    @UiHandler("enableSliderButton")
    void handleEnableSliderButtonClick(ClickEvent e) {
        slider.enable();
    }
    
    @UiHandler("busyButton")
    void handleBusyButtonClick(ClickEvent e) {
        Mobile.busy(true);
        Timer timer = new Timer() {
            @Override
            public void run() {
                Mobile.busy(false);
            }};
        timer.schedule(3000);    
    }

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
