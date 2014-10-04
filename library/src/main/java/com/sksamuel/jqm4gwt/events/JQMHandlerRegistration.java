package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is responsible for registering and unregistering JQM events.
 *
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public class JQMHandlerRegistration<X extends EventHandler> implements HandlerRegistration {

    private String jqmEventName;
    private HandlerRegistration defaultRegistration;
    private WidgetHandlerCounter counter;
    private Type<X> type;
    private Widget widget;
    private X handler;
    private HandlerRegistration attachHandlerRegistration;

    public static interface WidgetHandlerCounter {
        int getHandlerCountForWidget(GwtEvent.Type<?> type);
    }

    protected JQMHandlerRegistration() {
    }

    public static <X extends EventHandler> JQMHandlerRegistration<X> registerJQueryHandler(
            WidgetHandlerCounter counter, Widget widget, X handler, String jqmEventName, Type<X> type) {

        JQMHandlerRegistration<X> reg = new JQMHandlerRegistration<X>();
        reg.addHandler(counter, widget, handler, jqmEventName, type);
        return reg;
    }

    protected void addHandler(WidgetHandlerCounter counter, Widget widget,
            X handler, String jqmEventName, Type<X> type) {

        this.widget = widget;
        this.counter = counter;
        this.jqmEventName = jqmEventName;
        this.type = type;
        this.handler = handler;

        if (widget.isAttached()) {
            attachIfNeeded();
        }

        attachHandlerRegistration = widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached())
                    attachIfNeeded();
                else
                    detachIfNeeded();
            }
        });
    }

    private void attachIfNeeded() {
        boolean addInJS = counter.getHandlerCountForWidget(type) == 0;
        this.defaultRegistration = widget.addHandler(handler, type);
        if (addInJS) {
            if (JQMComponentEvents.ORIENTATIONCHANGE.equals(jqmEventName)) {
                int wHash = System.identityHashCode(widget);
                addJQueryEventToWindow(widget.getElement(), jqmEventName, String.valueOf(wHash));
            } else {
                addJQueryEvent(widget.getElement(), jqmEventName);
            }
        }
    }

    private void detachIfNeeded() {
        if (defaultRegistration != null) {
            defaultRegistration.removeHandler();
            defaultRegistration = null;
            if (counter.getHandlerCountForWidget(type) == 0) {
                if (JQMComponentEvents.ORIENTATIONCHANGE.equals(jqmEventName)) {
                    int wHash = System.identityHashCode(widget);
                    removeJQueryEventHandlerFromWindow(jqmEventName, String.valueOf(wHash));
                } else {
                    removeJQueryEventHandler(widget.getElement(), jqmEventName);
                }
            }
        }
    }

    @Override
    public void removeHandler() {
        detachIfNeeded();
        attachHandlerRegistration.removeHandler();
    }

    // Each widget needs to have a single "root" element.
    // Whenever the widget becomes attached, it create exactly one "back reference" from the element
    // to the widget that is, element.__listener = widget, performed in DOM.setEventListener()
    // This is set whenever the widget is attached, and cleared whenever it is detached.
    //
    private static native JavaScriptObject addJQueryEvent(Element element, String jqmEventName) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return null; // jQuery is not loaded
        $wnd.$(element).on(jqmEventName + ".jqm4gwt.s", $entry(function(event) {
                @com.sksamuel.jqm4gwt.events.JQMHandlerRegistration::dispatchJQMEvent(Ljava/lang/String;Lcom/google/gwt/user/client/EventListener;Lcom/google/gwt/core/client/JavaScriptObject;)(jqmEventName, element.__listener, event);
        }));
    }-*/;

    private static native JavaScriptObject addJQueryEventToWindow(Element element,
            String jqmEventName, String eltGuid) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return null; // jQuery is not loaded
        $wnd.$($wnd.window).on(jqmEventName + ".jqm4gwt.s." + eltGuid, $entry(function(event) {
                @com.sksamuel.jqm4gwt.events.JQMHandlerRegistration::dispatchJQMEvent(Ljava/lang/String;Lcom/google/gwt/user/client/EventListener;Lcom/google/gwt/core/client/JavaScriptObject;)(jqmEventName, element.__listener, event);
        }));
    }-*/;

    private static final void dispatchJQMEvent(String jqmEventName,
            EventListener listener, JavaScriptObject jQueryEvent) {
        if (listener != null && listener instanceof HasHandlers) {
            JQMEvent.fire((HasHandlers) listener, jqmEventName, jQueryEvent);
        }
    }

    private static native void removeJQueryEventHandler(Element element, String jqmEventName) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$(element).off(jqmEventName + ".jqm4gwt.s");
    }-*/;

    private static native void removeJQueryEventHandlerFromWindow(String jqmEventName, String eltGuid) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$($wnd.window).off(jqmEventName + ".jqm4gwt.s." + eltGuid);
    }-*/;

}