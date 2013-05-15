package com.sksamuel.jqm4gwt.list;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasInset;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.html.ListWidget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 21:21:13
 *         <p/>
 *         An implementation of a jquery mobile list view as seen here:
 * @link http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/lists/index.html
 * <p/>
 * This list can be ordered or unordered (which must be set at constructor time). The list can be dynamically
 * modified with random access.
 *
 *       * <h3>Use in UiBinder Templates</h3>
 *
 * When working with JQMList in
 * {@link com.google.gwt.uibinder.client.UiBinder UiBinder} templates, you
 * can set the List Items and Separators via child elements. For example:
 * <pre>
 * &lt;jqm:list.JQMList>
 *    &lt;jqm:item text="Item text here..."/>
 *    &lt;jqm:divider text="-- Divider Text Here --"/>
 *    &lt;jqm:item text="...more item text Here"/>
 * &lt;/jqm:list.JQMList>
 * </pre>
 */
public class JQMList extends JQMWidget implements HasClickHandlers, HasInset<JQMList>, HasFilter<JQMList> {

    /**
     * An ordered JQMList
     */
    public static class Ordered extends JQMList {
        public Ordered() {
           super(true);
        }
    }

    /**
     * An unordered JQMList
     */
    public static class Unordered extends JQMList {
        public Unordered() {
           super(false);
        }
    }

    /**
     * The underlying <li>or
     * <ul>
     * widget
     */
    private final ListWidget list;

    /**
     * The index of the last click
     */
    private int clickIndex;

    /**
     * The collection of items added to this list
     */
    private final List<JQMListItem> items = new ArrayList<JQMListItem>();

    /**
     * Create a new unordered {@link JQMList}
     */
    public JQMList() {
        this(false);
    }

    /**
     * Create a new {@link JQMList} that is unordered or ordered depending on the given boolean.
     *
     * @param ordered true if you want an ordered list, false otherwise.
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
     * <p/>
     * When a click event has been fired, you can get a reference to the position that was clicked by getClickIndex()
     * and a reference to the item that was clicked with getClickItem()
     */
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return list.addDomHandler(handler, ClickEvent.getType());
    }

    protected void addDivider(JQMListDivider d) {
        list.add(d);
        items.add(null);//to keep the list and items in sync
    }

    /**
     * Add a new divider with the given text and an automatically assigned id.
     *
     * @return the created divider which can be used to change the text dynamically. Changes to that
     *         instance write back to this list.
     */
    public HasText addDivider(String text) {
        JQMListDivider d = new JQMListDivider(text);
        addDivider(d);
        return d;
    }

    /**
     * This is simply addDivider(String text) but returning void to work with UiBinder.
     * @see #addDivider(String)
     * @param text
     */
    @UiChild(tagname = "divider")
    public void appendDivider(String text) {
        addDivider(text);
    }

    public void addItem(int index, final JQMListItem item) {
        list.insert(item, index);
        items.add(index,item);
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
     * This is simply addItem(String text) but returning void to work with UiBinder.
     * @see #addItem(String)
     * @param text
     */
    @UiChild(tagname = "item")
    public void appendItem(String text) {
        addItem(text);
    }


    /**
     * Adds a new {@link JQMListItem} that contains the given @param text as the heading element.
     * <p/>
     * The list item is made linkable to the given page
     *
     * @param text the text to use as the content of the header element
     * @param page the page to make the list item link to
     */
    public JQMListItem addItem(String text, JQMPage page) {
        return addItem(text, "#" + page.getId());
    }

    /**
     * Adds a new {@link JQMListItem} that contains the given @param text as the content. Note that if you want to
     * navigate to an internal url (ie, another JQM Page) then you must prefix the url with a hash. IE, the hash is
     * not added automatically. This allows you to navigate to external urls as well.
     * <p/>
     * If you add an item after the page has been created then you must call .refresh() to update the layout.
     * <p/>
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
    public JQMList addItems(Collection<String> items) {
        for (String item : items)
            addItem(item);
        return this;
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
     * @param item the {@link JQMListItem} to check for
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
        return getAttribute("data-divider-theme");
    }

    public String getFilterPlaceholder() {
        return getAttribute("data-filter-placeholder");
    }

    public String getFilterTheme() {
        return getAttribute("data-filter-theme");
    }

    /**
     * Returns the {@link JQMListItem} at the given position
     */
    public JQMListItem getItem(int pos) {
        return items.get(pos);
    }

    /**
     * Returns a List of {@link JQMListItem}s currently set on this list.
     * @return the items, null values for divider locations!
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
     * @return true if a divider with the given text was found and removed, otherwise false.
     */
    public boolean removeDivider(String text) {
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if ("list-divider".equals(w.getElement().getAttribute("data-role"))) {
                list.remove(k);
                items.remove(k);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the item at the given position.
     *
     * @param pos the integer position, 0 indexed
     */
    public void removeItem(int pos) {
        items.remove(pos);
        list.remove(pos);
    }

    /**
     * Removes the given item from the list
     *
     * @param item the item to remove
     */
    public void removeItem(JQMListItem item) {
    	if (item != null) {
	        items.remove(item);
	        list.remove(item);
    	}
    }

    /**
     * Removes all the given items. Conveniece method for calling removeItem(JQMListItem)multiple times.
     */
    public void removeItems(List<JQMListItem> items) {
        for (JQMListItem item : items)
            removeItem(item);
    }

    public JQMList setAutoDividers(boolean auto) {
        getElement().setAttribute("data-autodividers", String.valueOf(auto));
        return this;
    }

    protected JQMList setClickIndex(int clickIndex) {
        this.clickIndex = clickIndex;
        return this;
    }

    JQMList setClickItem(JQMListItem item) {
        setClickIndex(list.getWidgetIndex(item));
        return this;
    }

    /**
     * Sets the color scheme (swatch) for list item count bubbles. It accepts a single letter from a-z that maps to
     * the swatches included in your theme.
     */
    public JQMList setCountTheme(String theme) {
        setAttribute("data-count-theme", theme);
        return this;
    }

    /**
     * Sets the theme attribute for the data dividers
     */
    public JQMList setDividerTheme(String theme) {
        setAttribute("data-divider-theme", theme);
        return this;
    }

    @Override
    public JQMList setFilterable(boolean filterable) {
        if (filterable)
            setAttribute("data-filter", "true");
        else
            removeAttribute("data-filter");
        return this;
    }

    public JQMList setFilterPlaceholder(String placeholderText) {
        setAttribute("data-filter-placeholder", placeholderText);
        return this;
    }

    /**
     * Sets the color scheme (swatch) for the search filter bar. It accepts a single letter from a-z that maps to the
     * swatches included in your theme.
     */
    public JQMList setFilterTheme(String theme) {
        setAttribute("data-filter-theme", theme);
        return this;
    }

    public JQMList setHeaderTheme(String theme) {
        setAttribute("data-header-theme", theme);
        return this;
    }

    @Override
    public JQMList setInset(boolean inset) {
        if (inset)
            getElement().setAttribute("data-inset", "true");
        else
            getElement().removeAttribute("data-inset");
        return this;
    }

    public JQMList setSplitTheme(String theme) {
        setAttribute("data-split-theme", theme);
        return this;
    }

}
