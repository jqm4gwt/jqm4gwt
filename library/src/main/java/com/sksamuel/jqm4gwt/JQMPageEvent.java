package com.sksamuel.jqm4gwt;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JQMPageEvent extends GwtEvent<JQMPageEvent.Handler> {

    public interface Handler extends EventHandler {
        void onInit(JQMPageEvent event);
        void onBeforeShow(JQMPageEvent event);
        void onBeforeHide(JQMPageEvent event);
        void onShow(JQMPageEvent event);
        void onHide(JQMPageEvent event);
    }

    public static class DefaultHandler implements Handler {
        @Override
        public void onInit(JQMPageEvent event) {
        }

        @Override
        public void onBeforeShow(JQMPageEvent event) {
        }

        @Override
        public void onBeforeHide(JQMPageEvent event) {
        }

        @Override
        public void onShow(JQMPageEvent event) {
        }

        @Override
        public void onHide(JQMPageEvent event) {
        }
    }

    static Type<JQMPageEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMPageEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     */
    public static <S extends HasAttachHandlers> void fire(S source, PageState pageState,
            JQMPage prevPage, JQMPage nextPage) {
      if (TYPE != null) {
        JQMPageEvent event = new JQMPageEvent(pageState, prevPage, nextPage);
        source.fireEvent(event);
      }
    }

    public static Type<JQMPageEvent.Handler> getType() {
      if (TYPE == null) {
        TYPE = new Type<JQMPageEvent.Handler>();
      }
      return TYPE;
    }

    public enum PageState { INIT, BEFORE_SHOW, SHOW, BEFORE_HIDE, HIDE }

    private final PageState pageState;

    private final JQMPage prevPage;
    private final JQMPage nextPage;

    protected JQMPageEvent(PageState pageState, JQMPage prevPage, JQMPage nextPage) {
        this.pageState = pageState;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
    }

    public PageState getPageState() {
        return pageState;
    }

    /**
     * When HIDE - current page to be hidden. <br>
     * When SHOW - previous page we are transitioning from.
     **/
    public JQMPage getPrevPage() {
        return prevPage;
    }

    /**
     * When HIDE - next page to be shown. <br>
     * When SHOW - current page we are transitioning to.
     **/
    public JQMPage getNextPage() {
        return nextPage;
    }

    @Override
    public final Type<JQMPageEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        String s = super.toDebugString() + " pageState = " + pageState;
        s += "; prevPage = " + (prevPage != null ? prevPage.getId() : "null");
        s += "; nextPage = " + (nextPage != null ? nextPage.getId() : "null");
        return s;
    }

    @Override
    protected void dispatch(JQMPageEvent.Handler handler) {
        switch (pageState) {
            case INIT:
                handler.onInit(this);
                break;

            case BEFORE_HIDE:
                handler.onBeforeHide(this);
                break;

            case HIDE:
                handler.onHide(this);
                break;

            case BEFORE_SHOW:
                handler.onBeforeShow(this);
                break;

            case SHOW:
                handler.onShow(this);
                break;
        }
    }
}
