package com.sksamuel.jqm4gwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.ScriptElement;

public class ScriptUtils {

    private ScriptUtils() {} // static class

    private static final List<SequentialScriptInjector> injectors = new ArrayList<SequentialScriptInjector>();
    private static Throwable injectFailed;

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
        injectCss(false, paths);
    }

    public static void injectCss(boolean noModulePrefix, String... paths) {
        if (paths == null || paths.length == 0) return;
        if (noModulePrefix) {
            for (String path : paths) {
                addCss(path);
            }
        } else {
            checkModuleURL();
            for (String path : paths) {
                addCss(moduleURL + path);
            }
        }
    }

    public static abstract class InjectCallback implements Callback<Collection<String>, Throwable> {
        @Override
        public void onFailure(Throwable reason) {
            throw new RuntimeException(reason);
        }
    }

    public static void injectJs(InjectCallback done, String... paths) {
        injectJs(false, done, paths);
    }

    public static void injectJs(boolean noModulePrefix, final InjectCallback done, String... paths) {
        if (paths == null || paths.length == 0) return;
        final SequentialScriptInjector injector = new SequentialScriptInjector();
        injectors.add(injector);
        if (!noModulePrefix) {
            checkModuleURL();
            injector.setUrlPrefix(moduleURL);
        }
        injector.inject(new InjectCallback() {

            @Override
            public void onSuccess(Collection<String> result) {
                injectors.remove(injector);
                if (done != null) done.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable reason) {
                injectors.remove(injector);
                injectFailed = reason;
                if (done != null) done.onFailure(reason);
                else super.onFailure(reason);
            }

        }, paths);

    }

    private static native boolean isJqmLoaded() /*-{
        var b = ($wnd.$ === undefined || $wnd.$ === null
                 || $wnd.$.mobile === undefined || $wnd.$.mobile === null);
        return !b;
    }-*/;

    public static void waitJqmLoaded(final Callback<Void, Throwable> done) {
        if (done == null) return;
        Scheduler.get().scheduleFinally(new RepeatingCommand() {
            @Override
            public boolean execute() {
                if (!isJqmLoaded() || !injectors.isEmpty()) return true; // continue waiting
                if (injectFailed != null) done.onFailure(injectFailed);
                else done.onSuccess(null);
                return false; // done waiting
            }
        });
    }
}
