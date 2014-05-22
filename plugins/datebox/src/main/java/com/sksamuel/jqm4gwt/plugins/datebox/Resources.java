package com.sksamuel.jqm4gwt.plugins.datebox;

import com.google.gwt.core.client.Callback;
import com.sksamuel.jqm4gwt.ScriptUtils;
import com.sksamuel.jqm4gwt.ScriptUtils.InjectCallback;

public interface Resources {

    class Loader {

        private static final String JQM_DATEBOX_CSS = "css/jqm-datebox-1.4.0.min.css";
        private static final String CALBOX_JS = "js/jqm-datebox-1.4.0.comp.calbox.min.js";
        private static final String DATEBOX_I18N_JS = "js/jquery.mobile.datebox.i18n.en_US.utf8.js";

        public static void injectAll(final InjectCallback done) {
            ScriptUtils.waitJqmLoaded(new Callback<Void, Void>() {

                @Override
                public void onSuccess(Void result) {
                    ScriptUtils.injectCss(JQM_DATEBOX_CSS);
                    ScriptUtils.injectJs(done, CALBOX_JS, DATEBOX_I18N_JS);
                }

                @Override
                public void onFailure(Void reason) {
                }
            });
        }
    }

}
