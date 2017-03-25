package com.sksamuel.jqm4gwt.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JQMCollapsibleEvent extends GwtEvent<JQMCollapsibleEvent.Handler> {

    public interface Handler extends EventHandler {
        void onCollapse(JQMCollapsibleEvent event);
        void onExpand(JQMCollapsibleEvent event);
    }

    public static class DefaultHandler implements Handler {

        @Override
        public void onCollapse(JQMCollapsibleEvent event) {
        }

        @Override
        public void onExpand(JQMCollapsibleEvent event) {
        }
    }

    static Type<JQMCollapsibleEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMCollapsibleEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     */
    public static <S extends HasAttachHandlers> void fire(S source, CollapsibleState collapsibleState,
                                                          Element eventTarget) {
        if (TYPE != null) {
            JQMCollapsibleEvent event = new JQMCollapsibleEvent(collapsibleState, eventTarget);
            source.fireEvent(event);
        }
    }

    public static Type<JQMCollapsibleEvent.Handler> getType() {
        if (TYPE == null) {
            TYPE = new Type<JQMCollapsibleEvent.Handler>();
        }
        return TYPE;
    }

    public enum CollapsibleState { COLLAPSED, EXPANDED }

    private final CollapsibleState collapsibleState;

    private final Element initiatedBy;

    protected JQMCollapsibleEvent(CollapsibleState collapsibleState, Element eventTarget) {
        this.initiatedBy = eventTarget;
        this.collapsibleState = collapsibleState;
    }

    public CollapsibleState getCollapsibleState() {
        return collapsibleState;
    }

    /**
     * @return - useful in case of nested collapsibles, because event is bubbling up to parent collapsibles
     */
    public Element getInitiatedBy() {
        return initiatedBy;
    }

    /**
     * @return - true if event was initiated by passed collapsible (useful in case of nested collapsibles,
     *           because event is bubbling up to parent collapsibles).
     */
    public boolean isInitiatedBy(JQMCollapsible collapsible) {
        if (collapsible == null) return false;
        return collapsible.getElement().equals(initiatedBy);
    }

    @Override
    public final Type<JQMCollapsibleEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        return super.toDebugString() + " collapsibleState = " + collapsibleState;
    }

    @Override
    protected void dispatch(JQMCollapsibleEvent.Handler handler) {
        switch (collapsibleState) {
            case COLLAPSED:
                handler.onCollapse(this);
                break;

            case EXPANDED:
                handler.onExpand(this);
                break;
        }
    }

}
