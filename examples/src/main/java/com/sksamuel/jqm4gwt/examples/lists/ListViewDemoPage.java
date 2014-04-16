package com.sksamuel.jqm4gwt.examples.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPopup;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.form.elements.JQMRadioset;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.layout.JQMTable;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.list.JQMListItem;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 01:05:56
 *
 *         A demo page for showing list views with events
 *
 */
public class ListViewDemoPage extends JQMPage {

    private static final String SOURCE_URL = "https://github.com/sksamuel/jqm4gwt/blob/master/examples/src/main/java/com/sksamuel/jqm4gwt/examples/lists/ListViewDemoPage.java";
    protected List<String> items = getCountries();

    public ListViewDemoPage() {
        JQMHeader header = new JQMHeader("List View Demo");
        header.setRightButton("View source", SOURCE_URL, DataIcon.GEAR);
        header.setBackButton(true);
        add(header);

        add(new Paragraph(
                "The first list demo shows how we can dynamically change the elements at runtime using event handlers"));

        final JQMList reversingList = new JQMList();
        reversingList.withInset(true);

        reversingList.addDivider("Countries");
        reversingList.addItems(items);
        add(reversingList);

        JQMButton button = new JQMButton("Reverse the list!");
        add(button);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {

                Collections.reverse(items);
                reversingList.clear();
                reversingList.addDivider("Countries");
                reversingList.addItems(items);
                reversingList.refresh();
            }
        });

        add(new Paragraph(
                "The next list is a demo of a formatted list and how we can adjust the formatting on the fly"));

        final JQMList formattedList = new JQMList();
        formattedList.addDivider("Favourite Albums");
        formattedList.withInset(true);
        add(formattedList);

        JQMListItem item = formattedList.addItem("Coldplay");
        item.addHeaderText(1, "Viva La Vida");
        item.addHeaderText(4,
                "Viva la Vida or Death and All His Friends, often referred to as simply Viva la Vida is the fourth studio album by English rock band Coldplay, released on 11 June 2008 on Parlophone.");
        item.addText("My rating: 5/5");
        item.setThumbnail("http://cokemachineglow.com/images/7705.jpg");

        item = formattedList.addItem("Keane");
        item.addHeaderText(1, "Under the Iron Sea");
        item.addHeaderText(3,
                "Under the Iron Sea is the second studio album by English rock band Keane, released in 2006. During its first week on sale in the UK, the album went to #1, selling 222,297 copies according to figures from the Official Chart Company.");
        item.setThumbnail("http://cdn.pitchfork.com/media/9128-under-the-iron-sea.jpg");

        item = formattedList.addItem("Starsailor");
        item.addHeaderText(1, "Silence is Easy");
        item.addText("Starsailor teamed up with Phil Spector for their second album Silence is Easy, which was recorded in Los Angeles. The collaboration came about following Spector's daughter Nicole attending one of the band's American concerts in the Winter of 2002.");
        item.setThumbnail("http://img827.imageshack.us/img827/9175/mediumcz.jpg");

        item = formattedList.addItem("California");
        item.setIcon("http://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Flag_of_California.svg/320px-Flag_of_California.svg.png");

        button = new JQMButton("Refresh");
        add(button);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                updateTimes(formattedList);
            }
        });

        button = new JQMButton("Add more text");
        add(button);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                addText(formattedList);
            }
        });

        updateTimes(formattedList);

        add(new Paragraph());
        add(new Paragraph("Secondary image demo"));
        final JQMList lst = new JQMList();
        lst.withInset(true);
        lst.getElement().addClassName("jqm4gwt-list-static-item-img-right");
        add(lst);
        item = lst.addItem("New California Republic");
        item.setThumbnail("http://www.icon100.com/icon/15902/california-flag/72/png");
        item.setSecondaryImage("http://www.icon100.com/icon/15902/california-flag/32/png");

        item = lst.addItem("Ukraine");
        item.setThumbnail("http://www.icon100.com/icon/7983/ukraine-flag-country/48/png");
        item.setSecondaryImage("http://www.icon100.com/icon/7983/ukraine-flag-country/32/png");

        add(new Paragraph());
        add(new Paragraph("The next list is a demo of control group list item usage"));

        final JQMList controlList = new JQMList();
        controlList.addDivider("Special list items");
        controlList.withInset(true);
        add(controlList);

        item = controlList.addItem("Checkbox");
        item.setCheckBox(IconPos.LEFT);
        final JQMButton cbBtn = new JQMButton("Checkbox with Button");
        cbBtn.setInline(true);
        cbBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.alert(cbBtn.getText() + " is clicked.");
            }
        });
        item.addWidget(cbBtn);

        item = new JQMListItem();
        item.setControlGroup(true);
        item.getControlGroup().setHorizontal(); // no effect yet...
        controlList.appendItem(item);

        final JQMRadioset radioSet = new JQMRadioset();
        radioSet.setHorizontal();
        radioSet.addRadio("the big bear.", "yogi@yellowsrock.net");
        radioSet.addRadio("the little bear.", "bubu@yellowsrock.net");
        radioSet.addRadio("the ranger.", "ranger@yellowsrock.net");
        radioSet.setLabelHidden(true);
        item.addWidget(radioSet);

        JQMButton send = new JQMButton("Send email");
        item.addWidget(send);
        final JQMPopup popup = new JQMPopup();
        popup.setPosition("#" + radioSet.getId());
        add(popup);
        send.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                String value = radioSet.getValue();
                if (value == null) value = "... no-one?";
                popup.clear();
                popup.add(new Label("You almost sent an email to " + value));
                popup.open();
            }
        });
        final JQMListItem thisRow = item;
        JQMButton removeUrl = new JQMButton("Make this row/band non-clickable");
        item.addWidget(removeUrl);
        removeUrl.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                if (thisRow.getHref() != null) {
                    thisRow.removeUrl();
                    //controlList.recreate();
                    controlList.refresh();
                }
            }
        });
        JQMButton addUrl = new JQMButton("Make this row/band clickable");
        item.addWidget(addUrl);
        addUrl.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                if (thisRow.getHref() == null) {
                    // semi-working code, it's not reconstructed item correctly
                    thisRow.setUrl("#");
                    //controlList.recreate();
                    controlList.refresh();
                }
            }
        });

        {
            FlowPanel p = new FlowPanel();
            JQMTable t = new JQMTable(3);
            t.add(new Label("AAA"));
            t.add(new Label("BBB"));
            JQMButton btn = new JQMButton("CCC");
            btn.setInline(true);
            btn.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Window.alert("CCC clicked");
                }
            });
            t.add(btn);
            p.add(t);
            item = new JQMListItem();
            item.setControlGroup(true);
            item.addWidget(p);
            controlList.appendItem(item);
        }

        {
            FlowPanel p = new FlowPanel();
            JQMTable t = new JQMTable(3);
            t.add(new Label("DDD"));
            t.add(new Label("EEE"));
            JQMButton btn = new JQMButton("FFF");
            btn.setInline(true);
            btn.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Window.alert("FFF clicked");
                }
            });
            t.add(btn);
            p.add(t);
            item = new JQMListItem();
            item.setControlGroup(true, false/*linkable*/);
            item.addWidget(p);
            controlList.appendItem(item);
        }
    }

    protected void addText(JQMList list) {
        for (JQMListItem item : list.getItems()) {
            if (item == null) continue;
            item.addText("Some dummy text");
        }
    }

    private List<String> getCountries() {
        final List<String> items = new ArrayList<String>();
        items.add("United Kingdom");
        items.add("Spain");
        items.add("France");
        items.add("Germany");
        items.add("Japan");
        items.add("China");
        return items;
    }

    protected void updateTimes(JQMList list) {
        String now = DateTimeFormat.getFormat(PredefinedFormat.HOUR_MINUTE_SECOND).format(new Date());
        for (JQMListItem item : list.getItems()) {
            if (item != null) {
                item.setAside(now);
                item.setCount(item.getCount() == null ? 1 : item.getCount() + 1);
            }
        }
    }
}
