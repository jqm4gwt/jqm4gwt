package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.Callback;
import com.sksamuel.jqm4gwt.ScriptUtils.InjectCallback;

public interface Resources {

    class Loader {

        private static final String JQM_FIXES_CSS = "css/jqm-fixes.css";
        private static final String JQM4GWT_CSS = "css/jqm4gwt.css";
        private static final String JQUERY_ACTUAL = "js/jquery.actual.min.js";
        private static final String JQM_FIXES_JS = "js/jqm-fixes.js";

        public static void injectAll(final InjectCallback done) {
            ScriptUtils.waitJqmLoaded(new Callback<Void, Throwable>() {

                @Override
                public void onSuccess(Void result) {
                    ScriptUtils.injectCss(JQM_FIXES_CSS, JQM4GWT_CSS);
                    ScriptUtils.injectJs(done, JQUERY_ACTUAL, JQM_FIXES_JS);
                }

                @Override
                public void onFailure(Throwable reason) {
                    throw new RuntimeException(reason);
                }
            });
        }
    }
}
