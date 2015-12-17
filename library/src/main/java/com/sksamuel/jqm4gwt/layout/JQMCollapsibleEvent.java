package com.sksamuel.jqm4gwt.layout;

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
    public static <S extends HasAttachHandlers> void fire(S source, CollapsibleState collapsibleState) {
        if (TYPE != null) {
            JQMCollapsibleEvent event = new JQMCollapsibleEvent(collapsibleState);
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

    protected JQMCollapsibleEvent(CollapsibleState collapsibleState) {
        this.collapsibleState = collapsibleState;
    }

    public CollapsibleState getCollapsibleState() {
        return collapsibleState;
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
