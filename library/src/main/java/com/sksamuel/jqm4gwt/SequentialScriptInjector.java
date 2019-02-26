package com.sksamuel.jqm4gwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;

/**
 * Dynamically injects JS scripts into DOM, strictly one after one (sequentially).
 */
public class SequentialScriptInjector {

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

        final String url;
        final SequentialScriptInjector manager;

        public InjectTask(String url, SequentialScriptInjector manager) {
            this.url = url;
            this.manager = manager;
        }

        public void exec() {
            ScriptInjector.fromUrl(url).setWindow(ScriptInjector.TOP_WINDOW).setCallback(
                    new Callback<Void, Exception>() {
                        @Override
                        public void onFailure(Exception reason) {
                            manager.failure(InjectTask.this, reason);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            manager.success(InjectTask.this);
                        }
                    }).inject();
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
            InjectTask t = new InjectTask(urlPrefixDefined ? urlPrefix + url : url, this);
            lst.add(t);
        }
        if (lst == null) {
            injectDone();
            return;
        }
        injectList = lst;
        injectList.get(0).exec();
    }

}
