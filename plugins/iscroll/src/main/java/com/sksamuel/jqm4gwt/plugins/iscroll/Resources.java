package com.sksamuel.jqm4gwt.plugins.iscroll;

import com.google.gwt.core.client.Callback;
import com.sksamuel.jqm4gwt.ScriptUtils;
import com.sksamuel.jqm4gwt.ScriptUtils.InjectCallback;

public interface Resources {

    class Loader {
        /**
         * Must be set to true explicitly in static constructor of your application
         * in case you are going to manage JS and CSS loading manually, e.g. combined files.
         **/
        public static boolean manualLoading;

        private static final String ISCROLL_CSS = "css/jquery.mobile.iscrollview.css";
        private static final String ISCROLL_JS = "js/iscroll.min.js";
        private static final String JQM_ISCROLL_JS = "js/jquery.mobile.iscrollview.min.js";

        public static void injectAll(final InjectCallback done) {
            ScriptUtils.waitJqmLoaded(new Callback<Void, Throwable>() {

                @Override
                public void onSuccess(Void result) {
                    if (manualLoading) {
                        if (done != null) done.onSuccess(null);
                        return;
                    }
                    ScriptUtils.injectCss(ISCROLL_CSS);
                    ScriptUtils.injectJs(done, ISCROLL_JS, JQM_ISCROLL_JS);
                }

                @Override
                public void onFailure(Throwable reason) {
                    throw new RuntimeException(reason);
                }
            });
        }
    }
}
