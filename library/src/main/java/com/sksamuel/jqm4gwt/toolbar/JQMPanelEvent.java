package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JQMPanelEvent extends GwtEvent<JQMPanelEvent.Handler> {

        public interface Handler extends EventHandler {
            void onCreate(JQMPanelEvent event);
            void onBeforeOpen(JQMPanelEvent event);
            void onBeforeClose(JQMPanelEvent event);
            void onOpen(JQMPanelEvent event);
            void onClose(JQMPanelEvent event);
        }

        public static class DefaultHandler implements Handler {
            @Override
            public void onCreate(JQMPanelEvent event) {
            }

            @Override
            public void onBeforeOpen(JQMPanelEvent event) {
            }

            @Override
            public void onBeforeClose(JQMPanelEvent event) {
            }

            @Override
            public void onOpen(JQMPanelEvent event) {
            }

            @Override
            public void onClose(JQMPanelEvent event) {
            }
        }

        static Type<JQMPanelEvent.Handler> TYPE;

        /**
         * Fires an {@link JQMPanelEvent} on all registered handlers in the handler source.
         *
         * @param <S> The handler source type
         * @param source - the source of the handlers
         */
        public static <S extends HasAttachHandlers> void fire(S source, PanelState panelState) {
          if (TYPE != null) {
            JQMPanelEvent event = new JQMPanelEvent(panelState);
            source.fireEvent(event);
          }
        }

        public static Type<JQMPanelEvent.Handler> getType() {
          if (TYPE == null) {
            TYPE = new Type<JQMPanelEvent.Handler>();
          }
          return TYPE;
        }

        public enum PanelState { CREATE, BEFORE_OPEN, OPEN, BEFORE_CLOSE, CLOSE }

        private final PanelState panelState;

        protected JQMPanelEvent(PanelState panelState) {
            this.panelState = panelState;
        }

        public PanelState getPanelState() {
            return panelState;
        }

        @Override
        public final Type<JQMPanelEvent.Handler> getAssociatedType() {
            return TYPE;
        }

        @Override
        public String toDebugString() {
            assertLive();
            return super.toDebugString() + " panelState = " + panelState;
        }

        @Override
        protected void dispatch(JQMPanelEvent.Handler handler) {
            switch (panelState) {
                case CREATE:
                    handler.onCreate(this);
                    break;

                case BEFORE_CLOSE:
                    handler.onBeforeClose(this);
                    break;

                case CLOSE:
                    handler.onClose(this);
                    break;

                case BEFORE_OPEN:
                    handler.onBeforeOpen(this);
                    break;

                case OPEN:
                    handler.onOpen(this);
                    break;
            }
        }

    }
