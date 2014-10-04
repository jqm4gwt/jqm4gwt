package com.sksamuel.jqm4gwt.events;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public class JQMEventFactory {

    private JQMEventFactory() {} // static class

    private interface EventInstantiator<T extends EventHandler> {
        JQMEvent<T> create(JavaScriptObject jQueryEvent);
        GwtEvent.Type<T> getType();
    }

    private static final Map<String, EventInstantiator<?>> instantiators =
            new HashMap<String, EventInstantiator<?>>();

    static {
        instantiators.put(JQMComponentEvents.CHANGE, new EventInstantiator<JQMChangeHandler>() {

            private final Type<JQMChangeHandler> TYPE = new Type<JQMChangeHandler>();

            @Override
            public JQMEvent<JQMChangeHandler> create(JavaScriptObject jQueryEvent) {
                return new JQMEvent<JQMChangeHandler>(jQueryEvent, getType());
            }

            @Override
            public Type<JQMChangeHandler> getType() {
                return TYPE;
            }});

        instantiators.put(JQMComponentEvents.INPUT, new EventInstantiator<JQMInputHandler>() {

            private final Type<JQMInputHandler> TYPE = new Type<JQMInputHandler>();

            @Override
            public JQMEvent<JQMInputHandler> create(JavaScriptObject jQueryEvent) {
                return new JQMEvent<JQMInputHandler>(jQueryEvent, getType());
            }

            @Override
            public Type<JQMInputHandler> getType() {
                return TYPE;
            }});

        instantiators.put(JQMComponentEvents.VCLICK, new EventInstantiator<JQMVClickHandler>() {

            private final Type<JQMVClickHandler> TYPE = new Type<JQMVClickHandler>();

            @Override
            public JQMEvent<JQMVClickHandler> create(JavaScriptObject jQueryEvent) {
                return new JQMEvent<JQMVClickHandler>(jQueryEvent, getType());
            }

            @Override
            public Type<JQMVClickHandler> getType() {
                return TYPE;
            }});

        instantiators.put(JQMComponentEvents.TAP_EVENT, new EventInstantiator<TapHandler>() {

            @Override
            public JQMEvent<TapHandler> create(JavaScriptObject jQueryEvent) {
                return new TapEvent(jQueryEvent);
            }

            @Override
            public Type<TapHandler> getType() {
                return TapEvent.getType();
            }});

        instantiators.put(JQMComponentEvents.TAP_HOLD_EVENT, new EventInstantiator<TapHoldHandler>() {

            @Override
            public JQMEvent<TapHoldHandler> create(JavaScriptObject jQueryEvent) {
                return new TapHoldEvent(jQueryEvent);
            }

            @Override
            public Type<TapHoldHandler> getType() {
                return TapHoldEvent.getType();
            }});

        instantiators.put(JQMComponentEvents.ORIENTATIONCHANGE, new EventInstantiator<JQMOrientationChangeHandler>() {

            private final Type<JQMOrientationChangeHandler> TYPE = new Type<JQMOrientationChangeHandler>();

            @Override
            public JQMEvent<JQMOrientationChangeHandler> create(JavaScriptObject jQueryEvent) {
                return new JQMEvent<JQMOrientationChangeHandler>(jQueryEvent, getType());
            }

            @Override
            public Type<JQMOrientationChangeHandler> getType() {
                return TYPE;
            }});
    }

    public static JQMEvent<?> createEvent(String jqmEventName, JavaScriptObject jQueryEvent) {
        EventInstantiator<?> i = instantiators.get(jqmEventName);
        if (i != null) return i.create(jQueryEvent);
        throw new RuntimeException("Cannot create event: " + jqmEventName);
    }

    public static Type<?> getType(String jqmEventName) {
        EventInstantiator<?> i = instantiators.get(jqmEventName);
        if (i != null) return i.getType();
        throw new RuntimeException("Unknown event: " + jqmEventName);
    }

    /**
     * @param handlerClass - needed for getting typed result
     */
    public static <T extends EventHandler> Type<T> getType(String jqmEventName, Class<T> handlerClass) {
        Type<?> t = getType(jqmEventName);
        @SuppressWarnings("unchecked")
        Type<T> rslt = (Type<T>) t;
        return rslt;
    }

}
