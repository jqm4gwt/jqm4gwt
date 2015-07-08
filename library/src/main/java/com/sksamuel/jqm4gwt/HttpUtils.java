package com.sksamuel.jqm4gwt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class HttpUtils {

    private HttpUtils() {}

    /**
     * See <a href="http://www.gwtproject.org/doc/latest/DevGuideLogging.html">GWT Logging</a>
     */
    private static final Logger logger = Logger.getLogger("HttpUtils");

    private static void log(Level level, String msg, Throwable thrown) {
        //if (!LogConfiguration.loggingIsEnabled()) return;
        logger.log(level, msg, thrown);
    }

    private static void logError(String msg, Throwable... thrown) {
        log(Level.SEVERE, msg, thrown.length > 0 ? thrown[0] : null);
    }

    private static void logWarn(String msg, Throwable... thrown) {
        log(Level.WARNING, msg, thrown.length > 0 ? thrown[0] : null);
    }

    /**
     * @param url - must comply with SOP (see <a href="http://en.wikipedia.org/wiki/Same-origin_policy">Same-Origin Policy</a>)
     * or server must support CORS response.
     */
    public static void httpGet(final String url, final Callback<String, String> done) {
        if (Empty.is(url)) {
            if (done != null) done.onSuccess(null);
            return;
        }
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            //builder.setHeader("Origin", getAppOrigin()); - it's already populated by GWT so CORS ready
            builder.sendRequest(null/*requestData*/, new RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    int status = response.getStatusCode();
                    if (status == 200) {
                        String s = response.getText();
                        if (done != null) done.onSuccess(s);
                    } else {
                        String s = url + "  >> Response status: " + String.valueOf(status);
                        HttpUtils.logWarn(s);
                        if (done != null) done.onFailure(s);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    HttpUtils.logError(url, exception);
                    if (done != null) done.onFailure(url + "  >> " + exception.getMessage());
                }});
        } catch (RequestException e) {
            HttpUtils.logError(url, e);
            if (done != null) done.onFailure(url + "  >> " + e.getMessage());
        }
    }

    public static void httpGetList(final String url, final Callback<List<String>, String> done) {
        httpGet(url, new Callback<String, String>() {

            @Override
            public void onFailure(String reason) {
                if (done != null) done.onFailure(reason);
            }

            @Override
            public void onSuccess(String result) {
                if (done == null) return;
                if (Empty.is(result)) {
                    done.onSuccess(null);
                } else {
                    List<String> lst = new ArrayList<>();
                    String[] arr = result.split("\n");
                    for (int i = 0; i < arr.length; i++) lst.add(arr[i].trim());
                    done.onSuccess(lst);
                }
            }});
    }

    public static void httpGetJsonp(final String url, final Callback<List<String>, String> done) {
        if (Empty.is(url)) {
            if (done != null) done.onSuccess(null);
            return;
        }
        JsonpRequestBuilder builder = new JsonpRequestBuilder();

        builder.requestObject(url, new AsyncCallback<JsArrayString>() {

            @Override
            public void onFailure(Throwable caught) {
                HttpUtils.logError(url, caught);
                if (done != null) done.onFailure(url + "  >> " + caught.getMessage());
            }

            @Override
            public void onSuccess(JsArrayString result) {
                if (done != null) {
                    if (result == null || result.length() == 0) {
                        done.onSuccess(null);
                    } else {
                        List<String> lst = new ArrayList<>(result.length());
                        for (int i = 0; i < result.length(); i++) lst.add(result.get(i).trim());
                        done.onSuccess(lst);
                    }
                }
            }
        });
    }
}
