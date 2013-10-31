package com.sksamuel.jqm4gwt.examples.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.html.Paragraph;
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

	private static final String	SOURCE_URL	= "http://code.google.com/p/jqm4gwt/source/browse/src/com/sksamuel/jqm4gwt/examples/lists/ListViewDemoPage.java?repo=examples";
	protected List<String>		items		= getCountries();

	public ListViewDemoPage() {
		JQMHeader header = new JQMHeader("List View Demo");
		header.setRightButton("View source", SOURCE_URL, DataIcon.GEAR);
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
		item.setThumbnail("http://www.cokemachineglow.com/images/7705.jpg");

		item = formattedList.addItem("Keane");
		item.addHeaderText(1, "Under the Iron Sea");
		item.addHeaderText(3,
				"Under the Iron Sea is the second studio album by English rock band Keane, released in 2006. During its first week on sale in the UK, the album went to #1, selling 222,297 copies according to figures from the Official Chart Company.");
		item.setThumbnail("http://cdn.pitchfork.com/media/9128-under-the-iron-sea.jpg");

		item = formattedList.addItem("Starsailor");
		item.addHeaderText(1, "Silence is Easy");
		item.addText("Starsailor teamed up with Phil Spector for their second album Silence is Easy, which was recorded in Los Angeles. The collaboration came about following Spector's daughter Nicole attending one of the band's American concerts in the Winter of 2002.");
		item.setThumbnail("http://www.gazette.uwo.ca/2004/January/29/Pics/08D%20Starsailor.jpg");

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
	}

	protected void addText(JQMList list) {
		for (JQMListItem item : list.getItems()) {
			if (item == null) continue;
		    item.addText("Some dummy text");
		}
	}

	private List<String> getCountries() {
		final List<String> items = new ArrayList();
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
