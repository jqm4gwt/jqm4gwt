package com.sksamuel.jqm4gwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;

/**
 * Dynamically injects JS scripts into DOM, strictly one after one (sequentially).
 */
public class SequentialScriptInjector {

    private HeadElement head;

    private String urlPrefix;
    private Callback<Collection<String>, Throwable> injectCallback;
    private List<InjectTask> injectList;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    private void injectDone() {
        if (injectCallback == null) return;
        List<String> lst = injectList != null ? new ArrayList<String>(injectList.size()) : null;
        if (lst != null) {
            for (InjectTask t : injectList) lst.add(t.url);
        }
        injectCallback.onSuccess(lst);
    }

    private void success(InjectTask task) {
        int i = injectList.indexOf(task);
        if (i == -1) throw new RuntimeException("Invalid Task: " + task.url);
        if (i == injectList.size() - 1) {
            injectDone();
            return;
        }
        injectList.get(i + 1).exec();
    }

    private void failure(InjectTask task, Throwable throwable) {
        if (injectCallback != null) injectCallback.onFailure(throwable);
        else throw new RuntimeException(task.url, throwable);
    }

    private static class InjectTask {

        final HeadElement head;
        final String url;
        final SequentialScriptInjector manager;

        public InjectTask(HeadElement head, String url, SequentialScriptInjector manager) {
            this.head = head;
            this.url = url;
            this.manager = manager;
        }

        public void exec() {
            // ScriptInjector.fromUrl(url) CANNOT be used here, because it injects scripts into $wnd namespace
            ScriptElement script = Document.get().createScriptElement();
            script.setSrc(url);
            head.appendChild(script);
            SequentialScriptInjector.attachListeners(script, new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception reason) {
                    manager.failure(InjectTask.this, reason);
                }

                @Override
                public void onSuccess(Void result) {
                    manager.success(InjectTask.this);
                }
            }, false/*removeTag*/);
        }
    }

    public void inject(Callback<Collection<String>, Throwable> done, String... urls) {
        injectList = null;
        injectCallback = done;
        if (urls == null || urls.length == 0) {
            injectDone();
            return;
        }
        boolean urlPrefixDefined = urlPrefix != null && !urlPrefix.isEmpty();
        List<InjectTask> lst = null;
        for (String url : urls) {
            if (url == null || url.isEmpty()) continue;
            if (lst == null) lst = new ArrayList<InjectTask>();
            InjectTask t = new InjectTask(checkHead(), urlPrefixDefined ? urlPrefix + url : url, this);
            lst.add(t);
        }
        if (lst == null) {
            injectDone();
            return;
        }
        injectList = lst;
        injectList.get(0).exec();
    }

    private static HeadElement getHead() {
        Element elt = Document.get().getElementsByTagName("head").getItem(0);
        if (elt == null) {
            throw new RuntimeException("The host HTML page does not have a <head> element"
                                       + " which is required by StyleInjector");
        }
        return HeadElement.as(elt);
    }

    private HeadElement checkHead() {
        if (head != null) return head;
        head = getHead();
        return head;
    }

    /** Exact copy from ScriptInjector */
    private static native void attachListeners(JavaScriptObject scriptElement,
            Callback<Void, Exception> callback, boolean removeTag) /*-{
        function clearCallbacks() {
          scriptElement.onerror = scriptElement.onreadystatechange = scriptElement.onload = function() {
          };
          if (removeTag) {
            @com.google.gwt.core.client.ScriptInjector::nativeRemove(Lcom/google/gwt/core/client/JavaScriptObject;)(scriptElement);
          }
        }
        scriptElement.onload = $entry(function() {
          clearCallbacks();
          if (callback) {
            callback.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(null);
          }
        });
        // or possibly more portable script_tag.addEventListener('error', function(){...}, true);
        scriptElement.onerror = $entry(function() {
          clearCallbacks();
          if (callback) {
            var ex = @com.google.gwt.core.client.CodeDownloadException::new(Ljava/lang/String;)("onerror() called.");
            callback.@com.google.gwt.core.client.Callback::onFailure(Ljava/lang/Object;)(ex);
          }
        });
        scriptElement.onreadystatechange = $entry(function() {
          if (scriptElement.readyState == 'complete' || scriptElement.readyState == 'loaded') {
            scriptElement.onload();
          }
        });
    }-*/;

}
