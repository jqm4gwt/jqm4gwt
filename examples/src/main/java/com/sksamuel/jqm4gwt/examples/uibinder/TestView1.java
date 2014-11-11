package com.sksamuel.jqm4gwt.examples.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.ImageResources;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMDialog;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPage.DlgCloseBtn;
import com.sksamuel.jqm4gwt.JQMPageEvent;
import com.sksamuel.jqm4gwt.JQMPopup;
import com.sksamuel.jqm4gwt.JQMPopup.EltCoords;
import com.sksamuel.jqm4gwt.JQMPopup.PopupOptions;
import com.sksamuel.jqm4gwt.JQMPopupEvent;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.button.JQMButtonGroup;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMEvent;
import com.sksamuel.jqm4gwt.events.JQMOrientationChangeHandler;
import com.sksamuel.jqm4gwt.form.JQMForm;
import com.sksamuel.jqm4gwt.form.SubmissionHandler;
import com.sksamuel.jqm4gwt.form.elements.JQMCheckbox;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterable;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterableEvent;
import com.sksamuel.jqm4gwt.form.elements.JQMFlip;
import com.sksamuel.jqm4gwt.form.elements.JQMNumber;
import com.sksamuel.jqm4gwt.form.elements.JQMPassword;
import com.sksamuel.jqm4gwt.form.elements.JQMRadioset;
import com.sksamuel.jqm4gwt.form.elements.JQMRangeSlider;
import com.sksamuel.jqm4gwt.form.elements.JQMSearch;
import com.sksamuel.jqm4gwt.form.elements.JQMSelect;
import com.sksamuel.jqm4gwt.form.elements.JQMSelect.Option;
import com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable;
import com.sksamuel.jqm4gwt.form.elements.JQMSelectWithIcons;
import com.sksamuel.jqm4gwt.form.elements.JQMSlider;
import com.sksamuel.jqm4gwt.form.elements.JQMTelephone;
import com.sksamuel.jqm4gwt.form.elements.JQMText;
import com.sksamuel.jqm4gwt.form.elements.JQMUrl;
import com.sksamuel.jqm4gwt.html.Heading;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.html.Span;
import com.sksamuel.jqm4gwt.layout.JQMTable;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.list.JQMListDivider;
import com.sksamuel.jqm4gwt.list.JQMListItem;
import com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.sksamuel.jqm4gwt.toolbar.JQMTabs;
import com.sksamuel.jqm4gwt.toolbar.JQMTabsEvent;

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

    private static final JQMDialog dlg = new JQMDialog(new JQMHeader("Dialog Test"));
    private static JQMDialog dlgMsg = null;

    private static final TestDialog1 dlg1 = new TestDialog1();

    private static final String CITY_SEARCH_URL = "http://gd.geobytes.com/AutoCompleteCity?q=";
    private String cityFilter;

    @UiField
    JQMPopup popup;

    @UiField
    JQMButton popupOpenButton;

    @UiField
    JQMButton popupCloseButton;

    @UiField
    JQMButton page2AsDialog;

    @UiField
    JQMButton page2RestoreRolePage;

    @UiField
    JQMButton page2RestoreRoleDialog;

    @UiField
    JQMButton dlgButton;

    @UiField
    JQMButton dlgButton1;

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
    JQMButton getSliderValueBtn;

    @UiField
    JQMButton setSliderNullBtn;

    @UiField
    JQMButton setSliderMinBtn;

    @UiField
    JQMButton setSliderMinTo6Btn;

    @UiField
    JQMButton setSliderStepBtn;

    @UiField
    JQMRangeSlider rangeSlider;

    @UiField
    JQMButton disableRangeSliderBtn;

    @UiField
    JQMButton enableRangeSliderBtn;

    @UiField
    JQMButton rangeSliderGetValuesBtn;

    @UiField
    JQMButton setRangeSliderNullBtn;

    @UiField
    JQMForm form;

    @UiField
    JQMButton navBarBtn1;

    @UiField
    JQMCheckbox cb1;

    @UiField
    JQMCheckbox cb2;

    @UiField
    JQMCheckbox cb3;

    @UiField
    JQMCheckbox cb4;

    @UiField
    JQMButton checkboxInfoBtn;

    @UiField
    JQMRadioset radio1;

    @UiField
    JQMRadioset gender;

    @UiField
    JQMButton radioInfoBtn;

    @UiField
    JQMButton setRadioValBtn;

    @UiField
    JQMButton setRadioNullBtn;

    @UiField
    JQMFlip flip;

    @UiField
    JQMButton flipInfoBtn;

    @UiField
    JQMButton setFlipValBtn;

    @UiField
    JQMButton setFlipNullBtn;

    @UiField
    JQMText flipTrackTheme;

    @UiField
    JQMButton setFlipTrackTheme;

    @UiField
    JQMButton headerTestBtn1;

    @UiField
    JQMButton headerTestBtn2;

    @UiField
    FlowPanel headerPanel1;

    @UiField
    JQMCalBox datePicker;

    @UiField
    JQMButton datePickerGetValueBtn;

    @UiField
    JQMButton datePickerSetNullBtn;

    @UiField
    Span datePickerValueChanged;

    @UiField
    JQMListItem listItem4;

    @UiField
    JQMButton setListItemTextBtn;

    @UiField
    JQMListItem liCbA;

    @UiField
    JQMListItem liCbC;

    @UiField
    JQMListItem liCbD;

    @UiField
    JQMButton listItemCheckedBtn;

    @UiField
    JQMButton listItemSwitchCheckedBtn;

    @UiField
    JQMList listWithChecked;

    @UiField
    JQMList unorderedList;

    @UiField
    JQMButton ulBtn;

    @UiField
    JQMButton movieYearBtn;

    @UiField
    JQMButton movieTitleBtn;

    @UiField
    JQMButton alwaysHoverBtn;

    @UiField
    JQMButton toggleAlwaysHoverBtn;

    @UiField
    JQMTabs horzTabs;

    @UiField
    JQMCheckbox cbAllowLeavingTab1;

    @UiField
    FlowPanel horzTabsTab1;

    @UiField
    JQMSelect select1;

    @UiField
    JQMButton getSelectValBtn;

    @UiField
    JQMButton setSelectValBtn;

    @UiField
    JQMButton setSelectValNullBtn;

    @UiField
    JQMButton addSelectItemsBtn;

    @UiField
    JQMButton addFruitsBtn;

    @UiField
    JQMList fruitsList;

    @UiField
    JQMButton addNumsBtn;

    @UiField
    JQMSelect numsList;

    @UiField
    JQMFilterable numsFltr;

    @UiField
    JQMList cityList;

    @UiField
    JQMTable percentCols;

    @UiField
    JQMButton hidePctColBtn;

    @UiField
    JQMButton showPctColBtn;

    @UiField
    JQMButtonGroup btnGroup1;

    @UiField
    JQMSelectWithIcons ddIcons;

    @UiField
    JQMButton dividerShowBtn;

    @UiField
    JQMButton dividerBuyBtn;

    @UiField
    JQMButton dividerShareBtn;

    @UiField
    JQMList orderList1;

    @UiField
    JQMPopup listPopup;

    @UiField
    Heading listPopupInfo;

    @UiField
    JQMSearch searchEd;

    @UiField
    JQMNumber numberEd;

    @UiField
    JQMTelephone phoneEd;

    @UiField
    JQMPassword passwordEd;

    @UiField
    JQMUrl urlEd;

    @UiField
    JQMButton fltr2GetSearchTextBtn;

    @UiField
    JQMText fltr2SetSearchTextEd;

    @UiField
    JQMButton fltr2SetSearchTextBtn;

    @UiField
    JQMButton fruitsGetSearchTextBtn;

    @UiField
    JQMText fruitsSetSearchTextEd;

    @UiField
    JQMButton fruitsSetSearchTextBtn;

    @UiField
    JQMSelectFilterable selectFilterable;

    public TestView1() {
        page.addPageHandler(new JQMPageEvent.DefaultHandler() {
            @Override
            public void onShow(JQMPageEvent event) {
                //Window.alert("TestView1.page.onShow() is called");
            }
        });
    }

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

    @UiHandler("getSliderValueBtn")
    void getSliderValueBtnClick(ClickEvent e) {
        Window.alert(String.valueOf(slider.getValue()));
    }

    @UiHandler("setSliderNullBtn")
    void setSliderNullBtnClick(ClickEvent e) {
        slider.setValue(null);
    }

    @UiHandler("setSliderMinBtn")
    void setSliderMinBtnClick(ClickEvent e) {
        Double min = slider.getMin();
        if (min == null) {
            min = slider.getMax();
            if (min == null) min = 0d;
            else min = min - 100;
        }
        slider.setValue(min);
    }

    @UiHandler("setSliderMinTo6Btn")
    void setSliderMinTo6BtnClick(ClickEvent e) {
        slider.setMin(6d);
    }

    @UiHandler("setSliderStepBtn")
    void setSliderStepBtnClick(ClickEvent e) {
        slider.setStep(0.5d);
    }

    @UiHandler("disableRangeSliderBtn")
    void handleDisableRangeSliderBtnClick(ClickEvent e) {
        rangeSlider.disable();
    }

    @UiHandler("enableRangeSliderBtn")
    void handleEnableRangeSliderBtnClick(ClickEvent e) {
        rangeSlider.enable();
    }

    @UiHandler("rangeSliderGetValuesBtn")
    void rangeSliderGetValuesBtnClick(ClickEvent e) {
        Window.alert(String.valueOf(rangeSlider.getLoValue()) + " - "
                + String.valueOf(rangeSlider.getHiValue()));
    }

    @UiHandler("setRangeSliderNullBtn")
    void setRangeSliderNullBtnClick(ClickEvent e) {
        rangeSlider.setLoValue(null);
        rangeSlider.setHiValue(null);
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

    @UiHandler("navBarBtn1")
    void handleNavBarBtn1Click(ClickEvent e) {
        Window.alert("'" + navBarBtn1.getText() + "' is clicked!");
    }

    @UiHandler("checkboxInfoBtn")
    void checkboxInfoBtnClick(ClickEvent e) {
        String cb1Str = "'" + cb1.getText() + "' is " + (cb1.isChecked() ? "checked" : "unchecked");
        String cb2Str = "'" + cb2.getText() + "' is " + (cb2.isChecked() ? "checked" : "unchecked");
        String cb3Str = "'" + cb3.getText() + "' is " + (cb3.isChecked() ? "checked" : "unchecked");
        String cb4Str = "'" + cb4.getText() + "' is " + (cb4.isChecked() ? "checked" : "unchecked");
        Window.alert(cb1Str + "\n" + cb2Str + "\n" + cb3Str + "\n" + cb4Str);
    }

    @UiHandler("radioInfoBtn")
    void radioInfoBtnClick(ClickEvent e) {
        Window.alert("'" + radio1.getText() + "' value is " + radio1.getValue());
    }

    @UiHandler("setRadioValBtn")
    void setRadioValBtnClick(ClickEvent e) {
        radio1.setValue("c");
    }

    @UiHandler("setRadioNullBtn")
    void setRadioNullBtnClick(ClickEvent e) {
        radio1.setValue(null);
    }

    @UiHandler("flipInfoBtn")
    void flipInfoBtnClick(ClickEvent e) {
        Window.alert("'" + flip.getText() + "' value is " + flip.getValue());
    }

    @UiHandler("setFlipValBtn")
    void setFlipValBtnClick(ClickEvent e) {
        flip.setValue("flip2Value");
    }

    @UiHandler("setFlipNullBtn")
    void setFlipNullBtnClick(ClickEvent e) {
        flip.setValue(null);
    }

    @UiHandler("setFlipTrackTheme")
    void setFlipTrackThemeClick(ClickEvent e) {
        flip.setTrackTheme(flipTrackTheme.getValue());
    }

    @UiHandler("datePickerGetValueBtn")
    void datePickerGetValueBtnClick(ClickEvent e) {
        Window.alert("'" + datePicker.getText() + "' value is " + datePicker.getValue());
    }

    @UiHandler("datePickerSetNullBtn")
    void datePickerSetNullBtnClick(ClickEvent e) {
        datePicker.setValue(null);
        datePicker.refresh();
    }

    @UiHandler("headerTestBtn1")
    void headerTestBtn1Click(ClickEvent e) {
        boolean visible = Mobile.isVisible(nextView.testPage2.getElement());
        boolean hidden = Mobile.isHidden(nextView.testPage2.getElement());
        String s2 = "nextView.testPage2: visible=" + String.valueOf(visible) + "; hidden="
                    + String.valueOf(hidden) + "; HasVisibility.isVisible()=" + nextView.testPage2.isVisible();

        visible = Mobile.isVisible(page.getElement());
        hidden = Mobile.isHidden(page.getElement());
        String s1 = "This page: visible=" + String.valueOf(visible) + "; hidden="
                    + String.valueOf(hidden) + "; HasVisibility.isVisible()=" + page.isVisible();

        Window.alert(headerTestBtn1.getText() + " is clicked!\n\n" + s1 + "\n\n" + s2);
    }

    @UiHandler("headerTestBtn2")
    void headerTestBtn2Click(ClickEvent e) {
        Window.alert(headerTestBtn2.getText() + " is clicked!");
    }

    @UiHandler("page2AsDialog")
    void page2AsDialogClick(ClickEvent e) {
        nextView.testPage2.setDlgTransparent(true);
        nextView.testPage2.openDialog();
    }

    @UiHandler("page2RestoreRolePage")
    void page2RestoreRolePageClick(ClickEvent e) {
        nextView.testPage2.restoreRolePage();
    }

    @UiHandler("page2RestoreRoleDialog")
    void page2RestoreRoleDialogClick(ClickEvent e) {
        nextView.testPage2.restoreRoleDialog();
    }

    @UiHandler("dlgButton")
    void dlgButtonClick(ClickEvent e) {
        dlg.setDlgTransparent(true);
        dlg.setCorners(false);
        dlg.setDlgCloseBtn(DlgCloseBtn.RIGHT);
        dlg.setDlgCloseBtnText("Close me");
        dlg.openDialog();
    }

    @UiHandler("dlgButton1")
    void dlgButton1Click(ClickEvent e) {
        if (dlgMsg == null) {
            dlgMsg = new JQMDialog();
            dlgMsg.setCorners(false);
            dlgMsg.setHeader(new JQMHeader("Message"));
            dlgMsg.setDlgTransparent(true);
            dlgMsg.setDlgCloseBtn(DlgCloseBtn.RIGHT);
            FlowPanel p = new FlowPanel();
            Paragraph messageText = new Paragraph("Some text message here.");
            p.add(messageText);
            messageText.getElement().getStyle().setPadding(1, Unit.EM);
            dlgMsg.add(p);
            p = new FlowPanel();
            JQMButton b = new JQMButton("Close");
            b.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    dlgMsg.closeDialog();
                }
            });
            p.add(b);
            dlgMsg.add(p);
        }
        dlgMsg.openDialog();
    }

    @UiHandler("setListItemTextBtn")
    void setListItemTextBtnClick(ClickEvent e) {
        listItem4.setText(null);
    }

    @UiHandler("fltr2GetSearchTextBtn")
    void fltr2GetSearchTextBtnClick(ClickEvent e) {
        Window.alert("Search text: " + orderList1.getFilterSearchText());
    }

    @UiHandler("fltr2SetSearchTextBtn")
    void fltr2SetSearchTextBtnClick(ClickEvent e) {
        orderList1.setFilterSearchText(fltr2SetSearchTextEd.getValue());
    }

    @UiHandler("fruitsGetSearchTextBtn")
    void fruitsGetSearchTextBtnClick(ClickEvent e) {
        Window.alert("Search text: " + fruitsList.getFilterSearchText());
    }

    @UiHandler("fruitsSetSearchTextBtn")
    void fruitsSetSearchTextBtnClick(ClickEvent e) {
        fruitsList.setFilterSearchText(fruitsSetSearchTextEd.getValue());
    }

    @UiHandler("listItemCheckedBtn")
    void listItemCheckedBtnClick(ClickEvent e) {
        Window.alert(liCbA.getText() + " isChecked(): " + liCbA.isChecked() + "; getCheckBox(): " + liCbA.getCheckBox() + "\n"
                   + liCbC.getText() + " isChecked(): " + liCbC.isChecked() + "\n"
                   + liCbD.getText() + " isChecked(): " + liCbD.isChecked());
        //liCbA.setCheckBox(IconPos.LEFT);
        //listWithChecked.refresh();
    }

    @UiHandler("listItemSwitchCheckedBtn")
    void listItemSwitchCheckedBtnClick(ClickEvent e) {
        boolean v = liCbD.isChecked();
        liCbD.setChecked(!v);
    }

    @UiHandler("ulBtn")
    void ulBtnClick(ClickEvent e) {
        unorderedList.clear();

        JQMListItem li = new JQMListItem("test1", "");
        li.setSplitHref("");
        unorderedList.appendItem(li);

        li = new JQMListItem("test2", "");
        li.setSplitHref("");
        unorderedList.appendItem(li);

        li = new JQMListItem();
        li.setControlGroup(true);
        Label l = new Label("Input1");
        li.addWidget(l);
        TextBox t = new TextBox();
        li.addWidget(t);
        l = new Label("Input2");
        li.addWidget(l);
        t = new TextBox();
        li.addWidget(t);
        li.setSplitHref("");
        unorderedList.appendItem(li);

        //unorderedList.recreate();
        unorderedList.refresh();
    }

    @UiHandler("movieYearBtn")
    void movieYearBtnClick(ClickEvent e) {
        Window.alert(movieYearBtn.getText());
    }

    @UiHandler("movieTitleBtn")
    void movieTitleBtnClick(ClickEvent e) {
        Window.alert("'" + movieTitleBtn.getText() + "' button is clicked.");
    }

    @UiHandler("toggleAlwaysHoverBtn")
    void handleToggleAlwaysHoverBtnClick(ClickEvent e) {
        alwaysHoverBtn.setAlwaysHover(!alwaysHoverBtn.isAlwaysHover());
    }

    @UiHandler("getSelectValBtn")
    void getSelectValBtnClick(ClickEvent e) {
        Window.alert("Selected index: " + select1.getSelectedIndex() + "  Value: " + select1.getValue());
    }

    @UiHandler("setSelectValBtn")
    void setSelectValBtnClick(ClickEvent e) {
        select1.setSelectedIndex(1);
    }

    @UiHandler("setSelectValNullBtn")
    void setSelectValNullBtnClick(ClickEvent e) {
        select1.setValue(null);
    }

    @UiHandler("addSelectItemsBtn")
    void addSelectItemsBtnClick(ClickEvent e) {
        if (select1.indexOf("option5value") >= 0) return;
        select1.addOption("option5Text", "option5value");
        select1.addOption("option6Text", "option6value");
        select1.addOption("option7Text", "option7value");
        Option opt = new JQMSelect.Option();
        opt.setText("option8Text");
        opt.setValue("option8value");
        opt.setDisabled(true);
        select1.addOption(opt);
        select1.refresh();
    }

    @UiHandler("addFruitsBtn")
    void addFruitsBtnClick(ClickEvent e) {
        if (fruitsList.findItem("Apricot") != null) return;
        fruitsList.appendItem(new JQMListItem("Apricot", IconPos.LEFT));
        fruitsList.appendItem(new JQMListItem("Kiwi", IconPos.LEFT));
        fruitsList.appendItem(new JQMListItem("Mango", IconPos.LEFT));
        fruitsList.recreate();
        fruitsList.refresh();
        fruitsList.refreshFilter();
    }

    @UiHandler("addNumsBtn")
    void addNumsBtnClick(ClickEvent e) {
        if (numsList.indexOf("11") >= 0) return;
        numsList.addOption("11", "Eleven");
        numsList.addOption("12", "Twelve");
        numsList.addOption("13", "Thirteen");
        numsList.refresh();
        numsList.refreshFilter();
    }

    @UiHandler("hidePctColBtn")
    void hidePctColBtnClick(ClickEvent e) {
        percentCols.hidePercentageColumns(1, 2);
    }

    @UiHandler("showPctColBtn")
    void showPctColBtnClick(ClickEvent e) {
        percentCols.hidePercentageColumns();
    }


    {
        form.setSubmissionHandler(new SubmissionHandler<JQMForm>() {
            @Override
            public void onSubmit(JQMForm form) {
                // Do something here.
            }
        });
        /*radio1.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                Window.alert("'" + radio1.getText() + "' value changed: " +event.getValue());
            }});

        radio1.addSelectionHandler(new SelectionHandler<String>() {
            @Override
            public void onSelection(SelectionEvent<String> event) {
                Window.alert("'" + radio1.getText() + "' selected item: " + event.getSelectedItem());
            }});*/

        gender.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                Window.alert("'" + gender.getText() + "' value changed: " +event.getValue());
            }});

        /*gender.addSelectionHandler(new SelectionHandler<String>() {
            @Override
            public void onSelection(SelectionEvent<String> event) {
                Window.alert("'" + gender.getText() + "' selected item: " + event.getSelectedItem());
            }});*/

        headerPanel1.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //Window.alert("headerPanel1 is clicked!");
            }}, ClickEvent.getType());

        unorderedList.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                JQMListItem item = unorderedList.getClickItem();
                boolean isSplit = unorderedList.getClickIsSplit();
                Window.alert("Clicked: " + item.getText() + "; Split button: " + isSplit);
            }
        });

        horzTabs.addTabsHandler(new JQMTabsEvent.DefaultHandler() {
            @Override
            public void onBeforeActivate(JQMTabsEvent event) {
                Widget old = event.getOldTabContent();
                if (horzTabsTab1.equals(old)
                        && (cbAllowLeavingTab1.getValue() == null
                            || cbAllowLeavingTab1.getValue() == false)) {
                    Window.alert("Cannot leave this tab, checkbox must be checked first!");
                    throw new RuntimeException();
                }
            }
        });

        select1.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                //Window.alert(event.getValue());
            }
        });

        cityList.addFilterableHandler(new JQMFilterableEvent.DefaultHandler() {
            @Override
            public void onBeforeFilter(JQMFilterableEvent event) {
                String s = event.getFilterText();
                if (s != null) s = s.trim();
                boolean empty1 = cityFilter == null || cityFilter.isEmpty();
                boolean empty2 = s == null || s.isEmpty();
                if (empty1 && empty2) return;
                if (empty2) {
                    cityFilter = null;
                    cityList.clear();
                    cityList.refresh();
                    return;
                }
                if (s != null && s.equals(cityFilter)) return;

                cityFilter = s;
                if (cityFilter.length() <= 2) {
                    cityList.clear();
                    cityList.refresh();
                    return;
                }
                // See http://www.gwtproject.org/doc/latest/tutorial/Xsite.html
                String url = URL.encode(CITY_SEARCH_URL + cityFilter);
                JsonpRequestBuilder builder = new JsonpRequestBuilder();
                builder.requestObject(url, new AsyncCallback<JsArrayString>() {
                  @Override
                  public void onFailure(Throwable caught) {
                      Window.alert(caught.getMessage());
                  }

                  @Override
                  public void onSuccess(JsArrayString data) {
                      cityList.clear();
                      if (data != null && data.length() > 0) {
                          for (int i = 0; i < data.length(); i++) {
                              cityList.appendItem(new JQMListItem(data.get(i), IconPos.LEFT));
                          }
                          cityList.recreate();
                      }
                      cityList.refresh();
                  }
                });
            }
        });

        btnGroup1.addFilterableHandler(new JQMFilterableEvent.DefaultHandler() {
            @Override
            public Boolean onFiltering(JQMFilterableEvent event) {
                String s = event.getFilterText();
                if (s == null || s.isEmpty()) return null;
                String[] arr = s.split(",");
                String txt = JQMCommon.getTextForFiltering(event.getFilteringElt());
                if (txt == null || txt.isEmpty()) return null;
                txt = txt.toLowerCase();
                for (int i = 0; i < arr.length; i++) {
                    String v = arr[i].trim();
                    if (v.isEmpty()) continue;
                    v = v.toLowerCase();
                    if (txt.contains(v)) return false;
                }
                return true; // filter out
            }
        });

        String css = JQMCommon.getImageCss(ImageResources.INSTANCE.ajaxLoader(),
                                           JQMCommon.STYLE_UI_ICON + "loader");
        StyleInjector.inject(css);

        ddIcons.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                String s = event.getValue();
                String icon = ddIcons.getOptionIcon(s);
                ddIcons.setIconURL(icon);
            }
        });

        /*popup.addPopupHandler(new JQMPopupEvent.DefaultHandler() {
            @Override
            public void onAfterClose(JQMPopupEvent event) {
                Window.alert("Popup after close");
            }

            @Override
            public void onAfterOpen(JQMPopupEvent event) {
                Window.alert("Popup after open");
            }

            @Override
            public void onBeforePosition(JQMPopupEvent event) {
                PopupOptions opts = event.getPopupOptions();
                Window.alert("Popup before position: " + opts.toString());
            }
        });*/

        listPopup.addPopupHandler(new JQMPopupEvent.DefaultHandler() {
            @Override
            public void onBeforePosition(JQMPopupEvent event) {
                PopupOptions opts = event.getPopupOptions();
                JQMListItem li = orderList1.getClickItem();
                if (li == null) {
                    listPopupInfo.setText("before position: " + opts.toString());
                } else {
                    EltCoords coords = JQMPopup.calcEltCoords('#' + li.getId());
                    listPopupInfo.setText("before position: " + opts.toString()
                            + " " + coords.toString());
                }
            }
        });

        ClickHandler dividerBtnClick = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                JQMButton btn = (JQMButton) event.getSource();
                JQMListDivider di = orderList1.findDividerByTag("dividerWithButtons");
                String s = di != null ? di.getTagStr() + " found" : "dividerWithButtons NOT found";
                Window.alert("Clicked button: '" + btn.getText() + "'\n\n" + s);
            }};

        dividerShowBtn.addClickHandler(dividerBtnClick);
        dividerBuyBtn.addClickHandler(dividerBtnClick);
        dividerShareBtn.addClickHandler(dividerBtnClick);

        //searchEd.setValue("abcd", false/*fireEvents*/);
        /*searchEd.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                Window.alert(searchEd.getValue());
            }
        });
        searchEd.addInputHandler(new JQMInputHandler() {
            @Override
            public void onEvent(JQMEvent<?> event) {
                Window.alert(searchEd.getValue());
            }
        });
        phoneEd.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                Window.alert(phoneEd.getValue());
            }
        });
        numberEd.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                Window.alert(numberEd.getValue());
            }
        });
        passwordEd.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                Window.alert(passwordEd.getValue());
            }
        });
        urlEd.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                Window.alert(urlEd.getValue());
            }
        });*/

        datePicker.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                datePickerValueChanged.setText(datePicker.getValue());
            }
        });
        /*datePicker.setGridDateFormatter(new GridDateFormatterEx() {
            @Override
            public String format(int yyyy, int mm, int dd, String iso8601) {
                return '~' + String.valueOf(dd) + '~';
            }

            @Override
            public String getStyleNames(int yyyy, int mm, int dd, String iso8601) {
                return dd % 2 == 0 ? "aaa bbb" : "ccc ddd";
            }
        });*/

        selectFilterable.addFilterableHandler(new JQMFilterableEvent.DefaultHandler() {
            @Override
            public Boolean onFiltering(JQMFilterableEvent event) {
                return event.filterStartWithIgnoreCase(",");
            }});

        page.addJQMEventHandler(JQMComponentEvents.ORIENTATIONCHANGE, new JQMOrientationChangeHandler() {
            @Override
            public void onEvent(JQMEvent<?> event) {
                //String s = JQMOrientationChangeHandler.Process.getOrientation(event);
                //Window.alert(s);
            }
        });

    }

    public void show() {
        JQMContext.changePage(page);
    }

}
