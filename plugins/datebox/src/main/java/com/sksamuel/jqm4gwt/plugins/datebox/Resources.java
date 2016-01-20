package com.sksamuel.jqm4gwt.plugins.datebox;

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

        private static final String JQM_DATEBOX_CSS = "css/jqm-datebox-1.4.5.min.css";
        private static final String CALBOX_JS = "js/jqm-datebox-1.4.5.comp.calbox.min.js";
        private static final String DATEBOX_I18N_JS = "js/jquery.mobile.datebox.i18n.en.utf8.min.js";
        //private static final String CALBOX_FIXES_JS = "js/jqm-datebox-fixes.js";

        public static void injectAll(final InjectCallback done) {
            ScriptUtils.waitJqmLoaded(new Callback<Void, Throwable>() {

                @Override
                public void onSuccess(Void result) {
                    if (manualLoading) {
                        if (done != null) done.onSuccess(null);
                        return;
                    }
                    ScriptUtils.injectCss(JQM_DATEBOX_CSS);
                    ScriptUtils.injectJs(done, CALBOX_JS, /*CALBOX_FIXES_JS,*/ DATEBOX_I18N_JS);
                }

                @Override
                public void onFailure(Throwable reason) {
                    throw new RuntimeException(reason);
                }
            });
        }
    }

}
