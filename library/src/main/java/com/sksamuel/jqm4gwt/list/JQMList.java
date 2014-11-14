package com.sksamuel.jqm4gwt.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasInset;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.html.ListWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 21:21:13
 * <p/>
 * An implementation of a jquery mobile list view as seen here:
 * <a href="http://jquerymobile.com/demos/1.2.1/docs/lists/index.html">Listviews</a>
 * <p/>
 * This list can be ordered or unordered (which must be set at constructor time).
 * The list can be dynamically modified with random access.
 *
 * <h3>Use in UiBinder Templates</h3>
 *
 * When working with JQMList in
 * {@link com.google.gwt.uibinder.client.UiBinder UiBinder} templates, you
 * can set the List Items and Separators via child elements. For example:
 * <pre>
 * &lt;jqm:list.JQMList>
 *     &lt;jqm:item>&lt;jqm:list.JQMListItem text="List Item #1 Text Here"/>&lt;/jqm:item>
 *     &lt;jqm:divider>&lt;jqm:list.JQMListDivider text="List divider text here"/>&lt;/jqm:divider>
 *     &lt;jqm:item>&lt;jqm:list.JQMListItem text="List Item #2 Text Here"/>&lt;/jqm:item>
 * &lt;/jqm:list.JQMList>
 * </pre>
 */
public class JQMList extends JQMWidget implements HasClickHandlers, HasTapHandlers,
        HasInset<JQMList>, HasFilter<JQMList>, HasCorners<JQMList> {

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

    /** The underlying &lt;li> or &lt;ul> widget */
    private final ListWidget list;

    /** The index of the last click */
    private int clickIndex;
    private boolean clickIsSplit;

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

    @Override
    public HandlerRegistration addTapHandler(TapHandler handler) {
        // this is not a native browser event so we will have to manage it via JS
        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, JQMComponentEvents.TAP_EVENT, TapEvent.getType());
    }

    public void addDivider(int index, JQMListDivider d) {
        list.insert(d, index);
        items.add(index, null); // to keep the list and items in sync
        d.setList(this);
    }

    /**
     * Add a new divider with the given text and an automatically assigned id.
     *
     * @return the created divider which can be used to change the text dynamically. Changes to that
     *         instance write back to this list.
     */
    public HasText addDivider(String text) {
        JQMListDivider d = new JQMListDivider(text);
        appendDivider(d);
        return d;
    }

    /**
     * For UiBinder.
     */
    @UiChild(tagname = "divider")
    public void appendDivider(JQMListDivider divider) {
        addDivider(items.size(), divider);
    }

    public void addItem(int index, final JQMListItem item) {
        list.insert(item, index);
        items.add(index, item);
        item.setList(this);
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

    public static enum ListItemImageKind { NONE, THUMBNAIL, ICON }

    /**
     * This method is needed as good enough workaround for the following issue:
     * <a href="https://github.com/sksamuel/jqm4gwt/issues/18">List item with icon/thumbnail</a>
     *
     * @param url - could be null in case of non-clickable/readonly item. Empty string means
     * it will be clickable!
     *
     * @param imageUrl - could be null/empty initially, and then set later manually
     * (but imageKind must not be NONE if you are planning to set images for this item).
     */
    public JQMListItem addItem(String text, String url,
                               ListItemImageKind imageKind, String imageUrl) {
        return addItem(items.size(), text, url, imageKind, imageUrl);
    }

    public JQMListItem addItem(int index, String text, String url,
                               ListItemImageKind imageKind, String imageUrl) {
        // In case if icon/thumbnail is present there is severe rendering problem,
        // for details see https://github.com/sksamuel/jqm4gwt/issues/18

        JQMListItem item = null;
        boolean hasImage = imageKind != ListItemImageKind.NONE;
        boolean clickable = url != null;

        if (hasImage) { // workaround is needed for proper rendering
            if (clickable) {
                item = addItem(index, text, url);
            } else {
                item = addItem(index, text);
                // Empty url cannot be used, because we don't need clickable and right arrow icon
                item.addHeaderText(1, "");
            }
            switch (imageKind) {
            case ICON:
                if (imageUrl != null && !imageUrl.isEmpty()) item.setIcon(imageUrl);
                break;
            case THUMBNAIL:
                if (imageUrl != null && !imageUrl.isEmpty()) item.setThumbnail(imageUrl);
                break;
            default:
                break;
            }
        } else {
            item = addItem(index, text, url);
        }
        return item;
    }

    /**
     * For UiBinder.
     */
    @UiChild(tagname = "item")
    public void appendItem(JQMListItem item) {
        addItem(items.size(), item);
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

    public boolean getClickIsSplit() {
        return clickIsSplit;
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

    @Override
    public JQMList withFilterable(boolean filterable) {
        setFilterable(filterable);
        return this;
    }

    public String getFilterPlaceholder() {
        return getAttribute("data-filter-placeholder");
    }

    public void setFilterPlaceholder(String placeholderText) {
        setAttribute("data-filter-placeholder", placeholderText);
    }

    public String getFilterTheme() {
        return getAttribute("data-filter-theme");
    }

    /**
     * Sets the color scheme (swatch) for the search filter bar.
     * It accepts a single letter from a-z that maps to the swatches included in your theme.
     */
    public void setFilterTheme(String theme) {
        setAttribute("data-filter-theme", theme);
    }

    public JQMList withFilterTheme(String theme) {
        setFilterTheme(theme);
        return this;
    }

    @Override
    public boolean isFilterReveal() {
        return "true".equals(JQMCommon.getAttribute(this, "data-filter-reveal"));
    }

    @Override
    public void setFilterReveal(boolean value) {
        JQMCommon.setAttribute(this, "data-filter-reveal", value ? "true" : null);
    }

    @Override
    protected Element getFilterSearchElt() {
       Element elt = super.getFilterSearchElt();
       if (elt != null) return elt;
       elt = getAutoFilterSearchElt(getElement());
       return elt;
    }

    /**
     * @return - autogenerated filterable widget (result of searching, may be unsuccessful)
     */
    private static native Element getAutoFilterSearchElt(Element list) /*-{
        return $wnd.$(list).prev('.ui-filterable').find('.ui-input-search > input').first()[0];
    }-*/;

    /**
     * Returns the {@link JQMListItem} at the given position
     */
    public JQMListItem getItem(int pos) {
        return items.get(pos);
    }

    /**
     * Returns a List of {@link JQMListItem}s currently set on this list.
     * @return the items, has null values for divider locations!
     */
    public List<JQMListItem> getItems() {
        return items;
    }

    /**
     * @return - true if there is not null {@link JQMListItem} in items.
     */
    public boolean isAnyListItem() {
        for (JQMListItem li : items) {
            if (li != null) return true;
        }
        return false;
    }

    public interface LiCallback {
        void on(JQMListItem item);
    }

    public void forEach(LiCallback callback) {
        if (callback == null) return;
        for (JQMListItem li : items) {
            if (li == null) continue;
            callback.on(li);
        }
    }

    public void setAllChecked(boolean checked) {
        for (JQMListItem li : items) {
            if (li == null || li.isChecked() == checked) continue;
            li.setChecked(checked);
        }
    }

    public static enum ListChecked {
        NONE, ANY, ALL
    }

    public ListChecked getCheckedInfo() {
        boolean anyChecked = false;
        boolean allChecked = true;
        int cnt = 0;
        for (JQMListItem li : items) {
            if (li == null || !li.isCheckBox()) continue;
            cnt++;
            if (!li.isChecked()) {
                allChecked = false;
                if (anyChecked) break;
            } else {
                anyChecked = true;
                if (!allChecked) break;
            }
        }
        if (cnt == 0) allChecked = false;
        if (allChecked) return ListChecked.ALL;
        else if (anyChecked) return ListChecked.ANY;
        else return ListChecked.NONE;
    }

    public boolean isAnyChecked() {
        for (JQMListItem li : items) {
            if (li == null || !li.isCheckBox()) continue;
            if (li.isChecked()) return true;
        }
        return false;
    }

    @Override
    public boolean isInset() {
        return "true".equals(getElement().getAttribute("inset"));
    }

    /**
     * Call to refresh the list after a programmatic change is made.
     * <p/> In some cases you have to call recreate() first, and then refresh(), for example
     * when adding complex list items.
     */
    public void refresh() {
        refresh(getElement());
    }

    protected static native void refresh(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-listview') !== undefined) {
            w.listview('refresh');
        }
    }-*/;

    /**
     * Needed in some cases (when refresh() is not enough/working) for dynamic list views.
     * <p>For example after adding complex list items you have to call recreate() and then refresh().</p>
     */
    public void recreate() {
        JQMContext.render(getElement());
    }

    protected boolean isDivider(Widget w) {
        if (w == null) return false;
        return JQMListDivider.ATTR_VALUE.equals(w.getElement().getAttribute(JQMListDivider.ATTR_NAME));
    }

    public void removeDivider(JQMListDivider divider) {
        if (divider == null) return;
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if (isDivider(w) && divider.equals(w)) {
                list.remove(k);
                items.remove(k);
                return;
            }
        }
    }

    /**
     * Remove the divider with the given text. This method will search all the dividers and remove the first divider
     * found with the given text.
     *
     * @return true if a divider with the given text was found and removed, otherwise false.
     */
    public boolean removeDivider(String text) {
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if (isDivider(w) && w.getElement().getInnerText().equals(text)) {
                list.remove(k);
                items.remove(k);
                return true;
            }
        }
        return false;
    }

    /**
     * Remove the divider with the given tag. This method will search all the dividers and remove the first divider
     * found with the given tag.
     *
     * @return true if a divider with the given tag was found and removed, otherwise false.
     */
    public boolean removeDividerByTag(Object tag) {
        if (tag == null) return false;
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if (isDivider(w) && tag.equals(((JQMListDivider) w).getTag())) {
                list.remove(k);
                items.remove(k);
                return true;
            }
        }
        return false;
    }

    /**
     * Find the divider with the given text. This method will search all the dividers and return the first divider
     * found with the given text.
     *
     * @return the divider with the given text (if found, or null otherwise).
     */
    public JQMListDivider findDivider(String text) {
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if (isDivider(w) && w.getElement().getInnerText().equals(text)) {
                return (JQMListDivider) w;
            }
        }
        return null;
    }

    /**
     * Find the divider with the given tag. This method will search all the dividers and return the first divider
     * found with the given tag.
     *
     * @return the divider with the given tag (if found, or null otherwise).
     */
    public JQMListDivider findDividerByTag(Object tag) {
        if (tag == null) return null;
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if (isDivider(w) && tag.equals(((JQMListDivider) w).getTag())) {
                return (JQMListDivider) w;
            }
        }
        return null;
    }

    public List<JQMListDivider> getDividers() {
        List<JQMListDivider> rslt = null;
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if (isDivider(w)) {
                if (rslt == null) rslt = new ArrayList<JQMListDivider>();
                rslt.add((JQMListDivider) w);
            }
        }
        return rslt;
    }

    public int findDividerIdx(JQMListDivider d) {
        if (d == null) return -1;
        for (int k = 0; k < list.getWidgetCount(); k++) {
            Widget w = list.getWidget(k);
            if (d.equals(w)) return k;
        }
        return -1;
    }

    /**
     * For given divider finds the index at which new item should be inserted to become a part of
     * this divider's group.
     *
     * @return - proper index for new item insertion, or -1 in case of error
     *
     */
    public int findInsertIdxByDivider(JQMListDivider d) {
        if (d == null) return -1;
        int i = findDividerIdx(d);
        if (i == -1) return -1;
        List<JQMListItem> lst = getItems();
        int rslt = i + 1;
        while (rslt < lst.size()) {
            if (lst.get(rslt) == null) { // next divider
                return rslt;
            }
            rslt++;
        }
        return rslt;
    }

    public JQMListItem findItem(String text) {
        if (text == null) return null;
        List<JQMListItem> lst = getItems();
        for (JQMListItem i : lst) {
            if (i == null) continue;
            if (text.equals(i.getText())) return i;
        }
        return null;
    }

    /**
     * Ignores dividers, only counts JQMListItem bands.
     *
     *  @return - logical index of this item among other items (can be useful for matching/sync
     *  between underlying data structure and UI in case of dynamic/multiple dividers
     *  and JQMListItem's click handlers).
     **/
    public int findItemOnlyIdx(JQMListItem item) {
        if (item == null) return -1;
        List<JQMListItem> items = getItems();
        if (items == null) return -1;
        int i = items.indexOf(item);
        if (i == -1) return -1;
        int j = 0;
        for (JQMListItem k : items) {
            if (k == null) continue;
            if (k == item) return j;
            j++;
        }
        return -1;
    }

    /**
     * Find the list item with the given tag. This method will search all the items and return
     * the first item found with the given tag.
     *
     * @return the list item with the given tag (if found, or null otherwise).
     */
    public JQMListItem findItemByTag(Object tag) {
        if (tag == null) return null;
        List<JQMListItem> lst = getItems();
        for (JQMListItem i : lst) {
            if (i == null) continue;
            if (tag.equals(i.getTag())) return i;
        }
        return null;
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

    public void setAutoDividers(boolean auto) {
        getElement().setAttribute("data-autodividers", String.valueOf(auto));
    }

    public JQMList withAutoDividers(boolean auto) {
        setAutoDividers(auto);
        return this;
    }

    protected JQMList setClickIndex(int clickIndex, boolean isSplit) {
        this.clickIndex = clickIndex;
        this.clickIsSplit = isSplit;
        return this;
    }

    JQMList setClickItem(JQMListItem item, boolean isSplit) {
        setClickIndex(list.getWidgetIndex(item), isSplit);
        return this;
    }

    /**
     * Sets the color scheme (swatch) for list item count bubbles. It accepts a single letter from a-z that maps to
     * the swatches included in your theme.
     */
    public void setCountTheme(String theme) {
        setAttribute("data-count-theme", theme);
    }

    public JQMList withCountTheme(String theme) {
        setCountTheme(theme);
        return this;
    }

    /**
     * Sets the theme attribute for the data dividers
     */
    public void setDividerTheme(String theme) {
        setAttribute("data-divider-theme", theme);
    }

    public JQMList withDividerTheme(String theme) {
        setDividerTheme(theme);
        return this;
    }

    public void setHeaderTheme(String theme) {
        setAttribute("data-header-theme", theme);
    }

    public JQMList withHeaderTheme(String theme) {
        setHeaderTheme(theme);
        return this;
    }

    @Override
    public void setInset(boolean inset) {
        if (inset)
            getElement().setAttribute("data-inset", "true");
        else
            getElement().removeAttribute("data-inset");
    }

    @Override
    public JQMList withInset(boolean inset) {
        setInset(inset);
        return this;
    }

    public void setSplitTheme(String theme) {
        setAttribute("data-split-theme", theme);
    }

    public JQMList withSplitTheme(String theme) {
        setSplitTheme(theme);
        return this;
    }

    public String getSplitTheme() {
        return getAttribute("data-split-theme");
    }

    public void setSplitIcon(DataIcon icon) {
        setAttribute("data-split-icon", icon != null ? icon.getJqmValue() : null);
    }

    public DataIcon getSplitIcon() {
        String s = getAttribute("data-split-icon");
        return DataIcon.fromJqmValue(s);
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCorners(this);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCorners(this, corners);
    }

    @Override
    public JQMList withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    public boolean isIconNoDisc() {
        return JQMCommon.isIconNoDisc(this);
    }

    public void setIconNoDisc(boolean value) {
        JQMCommon.setIconNoDisc(this, value);
    }

    public boolean isIconAlt() {
        return JQMCommon.isIconAlt(this);
    }

    /**
     * @param value - if true "white vs. black" icon style will be used
     */
    public void setIconAlt(boolean value) {
        JQMCommon.setIconAlt(this, value);
    }

}
