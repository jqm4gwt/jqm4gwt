package com.sksamuel.jqm4gwt.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasInset;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.html.ListWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 21:21:13
 * 
 *         An implementation of a jquery mobile list view as seen here:
 * @link http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/lists/index.html
 * 
 *       This list can be ordered or unordered (which must be set at constructor time). The list can be dynamically
 *       modified with random access.
 * 
 * 
 * 
 */
public class JQMList extends JQMWidget implements HasClickHandlers, HasInset, HasFilter {

	/**
	 * The underlying <li>or
	 * <ul>
	 * widget
	 */
	private final ListWidget		list;

	/**
	 * The index of the last click
	 */
	private int					clickIndex;

	/**
	 * The collection of items added to this list
	 */
	private final List<JQMListItem>	items	= new ArrayList();

	/**
	 * Create a new unordered {@link JQMList}
	 */
	public JQMList() {
		this(false);
	}

	/**
	 * Create a new {@link JQMList} that is unordered or ordered depending on the given boolean.
	 * 
	 * @param ordered
	 *              true if you want an ordered list, false otherwise.
	 */
	public JQMList(boolean ordered) {

		list = new ListWidget(ordered);
		initWidget(list);

		setStyleName("jqm4gwt-list");
		setDataRole("listview");

		setId();
	}

	/**
	 * Registers a new {@link ClickHandler} on this list.
	 * 
	 * When a click event has been fired, you can get a reference to the position that was clicked by getClickIndex()
	 * and a reference to the item that was clicked with getClickItem()
	 */
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return list.addDomHandler(handler, ClickEvent.getType());
	}

	protected void addDivider(JQMListDivider d) {
		list.add(d);
	}

	/**
	 * Add a new divider with the given text and an automatically assigned id.
	 * 
	 * @return the created {@link JQMListDivider} which can be used to change the text dynamically. Changes to that
	 *         instance write back to this list.
	 */
	public JQMListDivider addDivider(String text) {
		JQMListDivider d = new JQMListDivider(text);
		addDivider(d);
		return d;
	}

	protected void addItem(int index, final JQMListItem item) {
		list.insert(item, index);
		items.add(item);
		item.setList(this);
		item.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setClickIndex(list.getWidgetIndex(item));
			}
		}, ClickEvent.getType());
	}

	public JQMListItem addItem(int index, String text) {
		return addItem(index, text, (String) null);
	}

	public JQMListItem addItem(int index, String text, String url) {
		JQMListItem item = new JQMListItem(text, url);
		addItem(index, item);
		return item;
	}

	/**
	 * Add a new read only item
	 */
	public JQMListItem addItem(String text) {
		return addItem(text, (String) null);
	}

	/**
	 * Adds a new {@link JQMListItem} that contains the given @param text as the heading element.
	 * 
	 * The list item is made linkable to the given page
	 * 
	 * @param text
	 *              the text to use as the content of the header element
	 * @param page
	 *              the page to make the list item link to
	 */
	public JQMListItem addItem(String text, JQMPage page) {
		return addItem(text, "#" + page.getId());
	}

	public <T extends Place> JQMListItem addItem(String text, T place, PlaceTokenizer<T> tokenizer) {
		String url = GWT.getTypeName(place) + ":" + tokenizer.getToken(place);
		return addItem(text, url);
	}

	/**
	 * Adds a new {@link JQMListItem} that contains the given @param text as the content. Note that if you want to
	 * navigate to an internal url (ie, another JQM Page) then you must prefix the url with a hash. IE, the hash is
	 * not added automatically. This allows you to navigate to external urls as well.
	 * 
	 * If you add an item after the page has been created then you must call .refresh() to update the layout.
	 * 
	 * The list item is made linkable to the @param url
	 */
	public JQMListItem addItem(String text, String url) {
		JQMListItem item = new JQMListItem(text, url);
		addItem(items.size(), item);
		return item;
	}

	/**
	 * Add a collection of read only items.
	 */
	public void addItems(Collection<String> items) {
		for (String item : items)
			addItem(item);
	}

	/**
	 * Remove all items from this list.
	 */
	public void clear() {
		list.clear();
		items.clear();
	}

	/**
	 * Returns true if this list contains the given item
	 * 
	 * @param item
	 *              the {@link JQMListItem} to check for
	 */
	public boolean contains(JQMListItem item) {
		return items.contains(item);
	}

	/**
	 * Returns the index of the last click. This is useful in event handlers when one wants to get a reference to
	 * which item was clicked.
	 * 
	 * @return the index of the last clicked item
	 */
	public int getClickIndex() {
		return clickIndex;
	}

	/**
	 * Returns the JQMListItem that was last clicked on. This is useful in event handlers when one wants to get a
	 * reference to which item was clicked.
	 * 
	 * @return the last clicked item
	 */
	public JQMListItem getClickItem() {
		return items.get(getClickIndex());
	}

	public String getCountTheme() {
		return getAttribute("data-count-theme");
	}

	/**
	 * Returns the currently set theme for dividers
	 * 
	 * @return the data divider theme string, eg "b"
	 */
	public String getDividerTheme() {
		return getAttribute("data-dividertheme");
	}

	public String getFilterPlaceholder() {
		return getAttribute("data-filter-placeholder");
	}

	public String getFilterTheme() {
		return getAttribute("data-filter-theme");
	}

	/**
	 * Returns the {@link JQMListItem} at the given position
	 * 
	 * @return
	 */
	public JQMListItem getItem(int pos) {
		return items.get(pos);
	}

	/**
	 * Returns a List of {@link JQMListItem}s currently set on this list.
	 */
	public List<JQMListItem> getItems() {
		return items;
	}

	/**
	 * Returns the value of the filterable option on this list.
	 * 
	 * @return true if this list is set to filterable, false otherwise.
	 */
	@Override
	public String isFilterable() {
		return getAttribute("data-filter");
	}

	@Override
	public boolean isInset() {
		return "true".equals(getElement().getAttribute("inset"));
	}

	protected void onTap(String id) {
		Window.alert("I  was tapped!");
	}

	/**
	 * Call to refresh the list after a programatic change is made.
	 */
	public void refresh() {
		refresh(getId());
	}

	protected native void refresh(String id) /*-{
								$wnd.$("#" + id).listview('refresh');
								}-*/;

	/**
	 * Remove the divider with the given text. This method will search all the dividers and remove the first divider
	 * found with the given text.
	 * 
	 * 
	 * @return true if a divider with the given text was found and removed, otherwise false.
	 */
	public boolean removeDivider(String text) {
		for (int k = 0; k < list.getWidgetCount(); k++) {
			Widget w = list.getWidget(k);
			if ("list-divider".equals(w.getElement().getAttribute("data-role"))) {
				list.remove(k);
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the item at the given position.
	 * 
	 * @param pos
	 *              the integer position, 0 indexed
	 */
	public void removeItem(int pos) {
		JQMListItem item = items.remove(pos);
		list.remove(item);
	}

	/**
	 * Removes the given item from the list
	 * 
	 * @param item
	 *              the item to remove
	 */
	public void removeItem(JQMListItem item) {
		items.remove(item);
		list.remove(item);
	}

	/**
	 * Removes all the given items. Conveniece method for calling removeItem(JQMListItem)multiple times.
	 */
	public void removeItems(List<JQMListItem> items) {
		for (JQMListItem item : items)
			removeItem(item);
	}

	protected void setClickIndex(int clickIndex) {
		this.clickIndex = clickIndex;
	}

	void setClickItem(JQMListItem item) {
		setClickIndex(list.getWidgetIndex(item));
	}

	/**
	 * Sets the color scheme (swatch) for list item count bubbles. It accepts a single letter from a-z that maps to
	 * the swatches included in your theme.
	 */
	public void setCountTheme(String theme) {
		setAttribute("data-count-theme", theme);
	}

	/**
	 * Sets the theme attribute for the data dividers
	 */
	public void setDividerTheme(String theme) {
		setAttribute("data-dividertheme", theme);
	}

	@Override
	public void setFilterable(boolean filterable) {
		if (filterable)
			setAttribute("data-filter", "true");
		else
			removeAttribute("data-filter");
	}

	public void setFilterPlaceholder(String placeholderText) {
		setAttribute("data-filter-placeholder", placeholderText);
	}

	/**
	 * Sets the color scheme (swatch) for the search filter bar. It accepts a single letter from a-z that maps to the
	 * swatches included in your theme.
	 */
	public void setFilterTheme(String theme) {
		setAttribute("data-filter-theme", theme);
	}

	@Override
	public void setInset(boolean inset) {
		if (inset)
			getElement().setAttribute("data-inset", "true");
		else
			getElement().removeAttribute("data-inset");
	}
}
