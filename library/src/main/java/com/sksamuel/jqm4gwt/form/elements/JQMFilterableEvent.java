package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sksamuel.jqm4gwt.JQMCommon;

public class JQMFilterableEvent extends GwtEvent<JQMFilterableEvent.Handler> {

    public interface Handler extends EventHandler {
        void onBeforeFilter(JQMFilterableEvent event);

        /**
         * @return - must return <b>true</b> if the element is to be <b>filtered out</b>.
         * <p/> - must return <b>false</b> if the element is to be <b>shown</b>.
         * <p/> - null means default filtering should be used.
         * <p/> JQMCommon.getTextForFiltering(elt) can be used to get filtering element's text
         */
        Boolean onFiltering(JQMFilterableEvent event);
    }

    public static class DefaultHandler implements Handler {

        @Override
        public void onBeforeFilter(JQMFilterableEvent event) {
        }

        @Override
        public Boolean onFiltering(JQMFilterableEvent event) {
            return null;
        }
    }

    /**
     * @param splitTextForFiltering - regex for splitting item's text (optional, could be null).
     * @return - false: show, true: filter out.
     */
    public Boolean filterStartWithIgnoreCase(String splitTextForFiltering) {
        String search = getFilterText();
        if (search == null || search.isEmpty()) return null;
        String s = JQMCommon.getTextForFiltering(getFilteringElt());
        if (s == null || s.isEmpty()) return null;
        if (splitTextForFiltering == null || splitTextForFiltering.isEmpty()) {
            return !(s.startsWith(search) || s.toLowerCase().startsWith(search.toLowerCase()));
        } else {
            String[] arr = s.split(splitTextForFiltering);
            for (String i : arr) {
                i = i.trim();
                boolean match = i.startsWith(search) || i.toLowerCase().startsWith(search.toLowerCase());
                if (match) return false;
            }
            return true;
        }
    }

    /**
     * @return - false: show, true: filter out.
     */
    public Boolean filterStartWithIgnoreCase() {
        return filterStartWithIgnoreCase(null/*splitTextForFiltering*/);
    }

    static Type<JQMFilterableEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMFilterableEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     */
    public static <S extends HasAttachHandlers> void fire(S source, FilterableState filterableState,
                                                          String filterText) {
        if (TYPE != null) {
            JQMFilterableEvent event = new JQMFilterableEvent(filterableState, filterText);
            source.fireEvent(event);
        }
    }

    public static <S extends HasAttachHandlers> Boolean fire(S source, FilterableState filterableState,
            String filterText, Element filteringElt, Integer filteringEltIdx) {
        if (TYPE != null) {
            JQMFilterableEvent event = new JQMFilterableEvent(filterableState, filterText,
                                                              filteringElt, filteringEltIdx);
            source.fireEvent(event);
            return event.filteringResult;
        }
        return null;
    }

    public static Type<JQMFilterableEvent.Handler> getType() {
        if (TYPE == null) {
            TYPE = new Type<JQMFilterableEvent.Handler>();
        }
        return TYPE;
    }

    public enum FilterableState { BEFORE_FILTER, FILTERING }

    private final FilterableState filterableState;
    private final String filterText;

    private final Element filteringElt;
    private final Integer filteringEltIdx;

    private Boolean filteringResult;

    protected JQMFilterableEvent(FilterableState filterableState, String filterText,
            Element filteringElt, Integer filteringEltIdx) {
        this.filterableState = filterableState;
        this.filterText = filterText;
        this.filteringElt = filteringElt;
        this.filteringEltIdx = filteringEltIdx;
    }

    protected JQMFilterableEvent(FilterableState filterableState, String filterText) {
        this(filterableState, filterText, null, null);
    }

    public FilterableState getFilterableState() {
        return filterableState;
    }

    public String getFilterText() {
        return filterText;
    }

    public Element getFilteringElt() {
        return filteringElt;
    }

    public Integer getFilteringEltIdx() {
        return filteringEltIdx;
    }

    @Override
    public final Type<JQMFilterableEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        String s = super.toDebugString() + " filterableState = " + filterableState
                + "; filterText = " + filterText;
        if (filteringElt != null) {
            s += "; filteringElt = " + filteringElt.toString()
                    + "; filteringEltIdx = " + filteringEltIdx;
        }
        return s;
    }

    @Override
    protected void dispatch(JQMFilterableEvent.Handler handler) {
        switch (filterableState) {
            case BEFORE_FILTER:
                handler.onBeforeFilter(this);
                break;

            case FILTERING:
                filteringResult = handler.onFiltering(this);
                break;
        }
    }

}
