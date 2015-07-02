package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.sksamuel.jqm4gwt.JsUtils;

public interface JQMOrientationChangeHandler extends JQMEventHandler {

    String PORTRAIT = "portrait";
    String LANDSCAPE = "landscape";

    static class Process {
        public static String getOrientation(JQMEvent<?> event) {
            if (event == null) return null;
            JavaScriptObject nativeEvent = event.getJQueryEvent().getNative();
            String s = JsUtils.getObjValue(nativeEvent, "orientation");
            return s;
        }
    }
}
