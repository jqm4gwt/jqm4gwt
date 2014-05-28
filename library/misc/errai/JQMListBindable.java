package com.vx.sw.client.jqm4gwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.errai.databinding.client.BindableListChangeHandler;
import org.jboss.errai.databinding.client.BindableListWrapper;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.list.JQMListDivider;
import com.sksamuel.jqm4gwt.list.JQMListItem;

/**
 * The same idea as {@link org.jboss.errai.ui.client.widget.ListWidget} but adopted for JQM4GWT.
 */
public class JQMListBindable<M> extends JQMList
        implements HasValue<List<M>>, BindableListChangeHandler<M> {

    public interface Renderer<M> {

        JQMListItem addItem(JQMListBindable<M> list, M item);

        /**
         * Makes sense for unordered list only, otherwise should be equal to addItem().
         */
        JQMListItem insertItem(JQMListBindable<M> list, int index, M item);

        /**
         * @param uiItem - the same item which has been returned earlier by addItem() / insertItem()
         * for this particular model item.
         */
        void removeItem(JQMListBindable<M> list, M item, JQMListItem uiItem);

        /**
         * Called after all add/remove operations are finished, right before list.refresh() call.
         */
        void onBeforeRefresh(JQMListBindable<M> list);

        /**
         * Called after all add/remove operations and list.refresh() are finished.
         */
        void onAfterRefresh(JQMListBindable<M> list);
    }

    public static abstract class BaseRenderer<M> implements Renderer<M> {

        @Override
        public JQMListItem insertItem(JQMListBindable<M> list, int index, M item) {
            return addItem(list, item);
        }

        @Override
        public void onBeforeRefresh(JQMListBindable<M> list) {
        }

        @Override
        public void onAfterRefresh(JQMListBindable<M> list) {
        }
    }

    public static abstract class ListRenderer<M> extends BaseRenderer<M> {

        private final boolean showEmptyMsg;

        private JQMListItem emptyMsg = null;

        public ListRenderer(boolean showEmptyMsg) {
            this.showEmptyMsg = showEmptyMsg;
        }

        public ListRenderer() {
            this(true/*showEmptyMsg*/);
        }

        protected abstract JQMListItem createListItem(M item);

        protected String getEmptyText() {
            return "-----";
        }

        private void addEmptyMsg(JQMListBindable<M> list) {
            if (!showEmptyMsg || emptyMsg != null) return;
            List<JQMListItem> items = list.getItems();
            if (items == null || items.isEmpty()) {
                JQMListDivider d = (JQMListDivider) list.addDivider("");
                emptyMsg = list.addItem(getEmptyText());
                d.setTag(emptyMsg);
            }
        }

        private void removeEmptyMsg(JQMListBindable<M> list) {
            if (!showEmptyMsg || emptyMsg == null) return;
            list.removeItem(emptyMsg);
            list.removeDivider(emptyMsg);
            emptyMsg = null;
        }

        @Override
        public JQMListItem addItem(JQMListBindable<M> list, M item) {
            removeEmptyMsg(list);

            JQMListItem li = createListItem(item);
            if (li != null) list.appendItem(li);
            return li;
        }

        @Override
        public void removeItem(JQMListBindable<M> list, M item, JQMListItem uiItem) {
            list.removeItem(uiItem);
            list.removeDivider(uiItem);
            addEmptyMsg(list);
        }

        @Override
        public void onBeforeRefresh(JQMListBindable<M> list) {
            addEmptyMsg(list);
            list.recreate();
        }
    }

    /**
     * An ordered JQMListBindable
     */
    public static class Ordered<M> extends JQMListBindable<M> {
        public Ordered() {
           super(true);
        }
    }

    /**
     * An unordered JQMListBindable
     */
    public static class Unordered<M> extends JQMListBindable<M> {
        public Unordered() {
           super(false);
        }
    }

    private Renderer<M> renderer;

    private BindableListWrapper<M> dataItems;

    private final Map<M, JQMListItem> dataToUI = new HashMap<M, JQMListItem>();

    private boolean valueChangeHandlerInitialized;

    public JQMListBindable() {
        this(false/*ordered*/);
    }

    public JQMListBindable(boolean ordered) {
        super(ordered);
    }

    @Override
    public void clear() {
        super.clear();
        dataToUI.clear();
    }

    /**
     * Sets the list of model objects.
     * The list will be wrapped in an {@link BindableListWrapper} to make direct changes
     * to the list observable.
     *
     * @param items - The list of model objects. If null or empty all existing items will be removed.
     */
    public void setDataItems(List<M> items) {
        boolean changed = this.dataItems != items;
        this.dataItems = items instanceof BindableListWrapper ? (BindableListWrapper<M>) items
                                                              : new BindableListWrapper<M>(items);
        if (changed) this.dataItems.addChangeHandler(this);
        addDataItems();
    }

    public List<M> getDataItems() {
        return dataItems;
    }

    private void addDataItems() {
        clear();
        if (dataItems != null && !dataItems.isEmpty()) {
            for (final M item : dataItems) {
                addDataItem(item);
            }
        }
        doRefresh();
    }

    private void addDataItem(final M item) {
        JQMListItem ui = renderer.addItem(this, item);
        dataToUI.put(item, ui);
    }

    private void insertDataItem(final int index, final M item) {
        JQMListItem ui = renderer.insertItem(this, index, item);
        dataToUI.put(item, ui);
    }

    private void removeDataItem(final M item) {
        JQMListItem ui = dataToUI.get(item);
        renderer.removeItem(this, item, ui);
        dataToUI.remove(item);
    }

    private void doRefresh() {
        renderer.onBeforeRefresh(this);
        refresh();
        renderer.onAfterRefresh(this);
    }

    public M getDataItem(JQMListItem uiItem) {
        if (uiItem == null) return null;
        for (Entry<M, JQMListItem> i : dataToUI.entrySet()) {
            if (uiItem.equals(i.getValue())) return i.getKey();
        }
        return null;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<M>> handler) {
        if (!valueChangeHandlerInitialized) {
            valueChangeHandlerInitialized = true;
            addDomHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    ValueChangeEvent.fire(JQMListBindable.this, getValue());
                }
            }, ChangeEvent.getType());
        }
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public List<M> getValue() {
        if (dataItems == null) {
            dataItems = new BindableListWrapper<M>(new ArrayList<M>());
            dataItems.addChangeHandler(this);
        }
        return dataItems;
    }

    @Override
    public void setValue(List<M> value) {
        setValue(value, false);
    }

    @Override
    public void setValue(List<M> value, boolean fireEvents) {
        List<M> oldValue = getValue();
        setDataItems(value);
        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
        }
    }

    @Override
    public void onItemAdded(List<M> oldList, M item) {
        addDataItem(item);
        doRefresh();
    }

    @Override
    public void onItemAddedAt(List<M> oldList, int index, M item) {
        insertDataItem(index, item);
        doRefresh();
    }

    @Override
    public void onItemsAdded(List<M> oldList, Collection<? extends M> items) {
        for (M m : items) {
            addDataItem(m);
        }
        doRefresh();
    }

    @Override
    public void onItemsAddedAt(List<M> oldList, int index, Collection<? extends M> item) {
        Iterator<? extends M> iter = item.iterator();
        int pos = index;
        while (iter.hasNext()) {
            M i = iter.next();
            insertDataItem(pos, i);
            pos++;
        }
        doRefresh();
    }

    @Override
    public void onItemsCleared(List<M> oldList) {
        clear();
        doRefresh();
    }

    @Override
    public void onItemRemovedAt(List<M> oldList, int index) {
        removeDataItem(oldList.get(index));
        doRefresh();
    }

    @Override
    public void onItemsRemovedAt(List<M> oldList, List<Integer> indexes) {
        for (Integer index : indexes) {
            removeDataItem(oldList.get(index));
        }
        doRefresh();
    }

    @Override
    public void onItemChanged(List<M> oldList, int index, M item) {
        removeDataItem(oldList.get(index));
        insertDataItem(index, item);
        doRefresh();
    }

    /**
     * Needed in case of renderer changed, so list has to be visually reconstructed by new renderer.
     */
    public void rerender() {
        clear();
        if (dataItems != null) {
            for (M m : dataItems) {
                addDataItem(m);
            }
        }
        doRefresh();
    }

    public Renderer<M> getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer<M> renderer) {
        this.renderer = renderer;
    }

}
