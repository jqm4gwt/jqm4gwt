package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

public class JQMTabsEvent extends GwtEvent<JQMTabsEvent.Handler> {

    public interface Handler extends EventHandler {
        /**
         *  If the tabs are currently collapsed, event.oldTabHeader and event.oldTabContent will be null.
         *  <p/> If the tabs are collapsing, event.newTabHeader and event.newTabContent will be null.
         */
        void onActivate(JQMTabsEvent event);

        /**
         * If any exception is thrown then tabs won't be switched and current tab remains active.
         *  <p/> If the tabs are currently collapsed, event.oldTabHeader and event.oldTabContent will be null.
         *  <p/> If the tabs are collapsing, event.newTabHeader and event.newTabContent will be null.
         */
        void onBeforeActivate(JQMTabsEvent event);
    }

    public static class DefaultHandler implements Handler {

        @Override
        public void onActivate(JQMTabsEvent event) {
        }

        @Override
        public void onBeforeActivate(JQMTabsEvent event) {
        }
    }

    static Type<JQMTabsEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMTabsEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     */
    public static <S extends HasAttachHandlers> void fire(S source, TabsState tabsState,
            Widget newTabHeader, Widget oldTabHeader, Widget newTabContent, Widget oldTabContent) {
        if (TYPE != null) {
            JQMTabsEvent event = new JQMTabsEvent(tabsState, newTabHeader, oldTabHeader,
                                                  newTabContent, oldTabContent);
            source.fireEvent(event);
        }
    }

    public static Type<JQMTabsEvent.Handler> getType() {
        if (TYPE == null) {
            TYPE = new Type<JQMTabsEvent.Handler>();
        }
        return TYPE;
    }

    public enum TabsState { ACTIVATE, BEFORE_ACTIVATE }

    private final TabsState tabsState;
    private final Widget newTabHeader;
    private final Widget oldTabHeader;
    private final Widget newTabContent;
    private final Widget oldTabContent;

    protected JQMTabsEvent(TabsState tabsState, Widget newTabHeader, Widget oldTabHeader,
            Widget newTabContent, Widget oldTabContent) {
        this.tabsState = tabsState;
        this.newTabHeader = newTabHeader;
        this.oldTabHeader = oldTabHeader;
        this.newTabContent = newTabContent;
        this.oldTabContent = oldTabContent;
    }

    public TabsState getTabsState() {
        return tabsState;
    }

    public Widget getNewTabHeader() {
        return newTabHeader;
    }

    public Widget getOldTabHeader() {
        return oldTabHeader;
    }

    public Widget getNewTabContent() {
        return newTabContent;
    }

    public Widget getOldTabContent() {
        return oldTabContent;
    }

    @Override
    public final Type<JQMTabsEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        return super.toDebugString() + " tabsState = " + tabsState;
    }

    @Override
    protected void dispatch(JQMTabsEvent.Handler handler) {
        switch (tabsState) {
            case ACTIVATE:
                handler.onActivate(this);
                break;

            case BEFORE_ACTIVATE:
                handler.onBeforeActivate(this);
                break;
        }
    }

}
