package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JQMFilterableEvent extends GwtEvent<JQMFilterableEvent.Handler> {

    public interface Handler extends EventHandler {
        void onBeforeFilter(JQMFilterableEvent event);
    }

    public static class DefaultHandler implements Handler {

        @Override
        public void onBeforeFilter(JQMFilterableEvent event) {
        }
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

    public static Type<JQMFilterableEvent.Handler> getType() {
        if (TYPE == null) {
            TYPE = new Type<JQMFilterableEvent.Handler>();
        }
        return TYPE;
    }

    public enum FilterableState { BEFORE_FILTER }

    private final FilterableState filterableState;
    private final String filterText;

    protected JQMFilterableEvent(FilterableState filterableState, String filterText) {
        this.filterableState = filterableState;
        this.filterText = filterText;
    }

    public FilterableState getFilterableState() {
        return filterableState;
    }

    public String getFilterText() {
        return filterText;
    }

    @Override
    public final Type<JQMFilterableEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        return super.toDebugString() + " filterableState = " + filterableState
                + "; filterText = " + filterText;
    }

    @Override
    protected void dispatch(JQMFilterableEvent.Handler handler) {
        switch (filterableState) {
            case BEFORE_FILTER:
                handler.onBeforeFilter(this);
                break;
        }
    }

}
