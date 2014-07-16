package com.sksamuel.jqm4gwt;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sksamuel.jqm4gwt.JQMPopup.PopupOptions;

public class JQMPopupEvent extends GwtEvent<JQMPopupEvent.Handler> {

    public interface Handler extends EventHandler {

        /** Triggered when a popup has completely closed */
        void onAfterClose(JQMPopupEvent event);

        /** Triggered after a popup has completely opened */
        void onAfterOpen(JQMPopupEvent event);

        /** Triggered before a popup computes the coordinates where it will appear */
        void onBeforePosition(JQMPopupEvent event);
    }

    public static class DefaultHandler implements Handler {

        @Override
        public void onAfterClose(JQMPopupEvent event) {
        }

        @Override
        public void onAfterOpen(JQMPopupEvent event) {
        }

        @Override
        public void onBeforePosition(JQMPopupEvent event) {
        }
    }

    static Type<JQMPopupEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMPopupEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     */
    public static <S extends HasAttachHandlers> void fire(S source, PopupState popupState,
                                                          PopupOptions popupOptions) {
      if (TYPE != null) {
        JQMPopupEvent event = new JQMPopupEvent(popupState);
        if (popupOptions != null) event.setPopupOptions(popupOptions);
        source.fireEvent(event);
      }
    }

    public static <S extends HasAttachHandlers> void fire(S source, PopupState popupState) {
        fire(source, popupState, null/*popupOptions*/);
    }

    public static Type<JQMPopupEvent.Handler> getType() {
      if (TYPE == null) {
        TYPE = new Type<JQMPopupEvent.Handler>();
      }
      return TYPE;
    }

    public enum PopupState { AFTER_CLOSE, AFTER_OPEN, BEFORE_POSITION }

    private final PopupState popupState;

    private PopupOptions popupOptions;

    protected JQMPopupEvent(PopupState popupState) {
        this.popupState = popupState;
    }

    public PopupState getPopupState() {
        return popupState;
    }

    public PopupOptions getPopupOptions() {
        return popupOptions;
    }

    public void setPopupOptions(PopupOptions popupOptions) {
        this.popupOptions = popupOptions;
    }

    @Override
    public final Type<JQMPopupEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        return super.toDebugString() + " popupState = " + popupState;
    }

    @Override
    protected void dispatch(JQMPopupEvent.Handler handler) {
        switch (popupState) {
        case AFTER_CLOSE:
            handler.onAfterClose(this);
            break;
        case AFTER_OPEN:
            handler.onAfterOpen(this);
            break;
        case BEFORE_POSITION:
            handler.onBeforePosition(this);
            break;
        }
    }

}
