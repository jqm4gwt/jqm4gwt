package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.ScriptElement;

public interface Resources {

    class Loader {

        private static final String JQM_FIXES_CSS = "css/jqm-fixes.css";
        private static final String JQM4GWT_CSS = "css/jqm4gwt.css";
        private static final String JQUERY_ACTUAL = "js/jquery.actual.min.js";
        private static final String JQM_FIXES_JS = "js/jqm-fixes.js";

        private static HeadElement head;
        private static String moduleURL;

        private static final String HTTP = "http://";
        private static final String HTTPS = "https://";

        /**
         * @return - relative path to module from hosting root, i.e. GWT.getModuleBaseURL() minus address part.
         */
        private static String getModuleURL() {
           String s = GWT.getModuleBaseURL();
           if (s == null || s.isEmpty()) return "";
           if (s.startsWith(HTTP)) s = s.substring(HTTP.length());
           else if (s.startsWith(HTTPS)) s = s.substring(HTTPS.length());
           int p = s.indexOf('/');
           if (p == -1) return ""; // something strange
           s = s.substring(p).trim();
           return s;
        }

        private static String checkModuleURL() {
            if (moduleURL != null) return moduleURL;
            moduleURL = getModuleURL();
            return moduleURL;
        }

        private static HeadElement getHead() {
            Element elt = Document.get().getElementsByTagName("head").getItem(0);
            if (elt == null) {
                throw new RuntimeException("The host HTML page does not have a <head> element"
                                           + " which is required by StyleInjector");
            }
            return HeadElement.as(elt);
        }

        private static HeadElement checkHead() {
            if (head != null) return head;
            head = getHead();
            return head;
        }

        private static void addJs(String src) {
            ScriptElement script = Document.get().createScriptElement();
            script.setSrc(src);
            checkHead().appendChild(script);
        }

        private static void addCss(String href) {
            LinkElement link = Document.get().createLinkElement();
            link.setRel("stylesheet");
            link.setHref(href);
            checkHead().appendChild(link);
        }

        public static void injectCss(String... paths) {
            if (paths == null || paths.length == 0) return;
            checkModuleURL();
            for (String path : paths) {
                addCss(moduleURL + path);
            }
        }

        public static void injectJs(String... paths) {
            if (paths == null || paths.length == 0) return;
            checkModuleURL();
            for (String path : paths) {
                addJs(moduleURL + path);
            }
        }

        public static void injectAll() {
            injectCss(JQM_FIXES_CSS, JQM4GWT_CSS);
            injectJs(JQUERY_ACTUAL, JQM_FIXES_JS);
        }

        private static native boolean isJqmLoaded() /*-{
            var b = ($wnd.$ === undefined || $wnd.$ === null
                     || $wnd.$.mobile === undefined || $wnd.$.mobile === null);
            return !b;
        }-*/;
    }
}
