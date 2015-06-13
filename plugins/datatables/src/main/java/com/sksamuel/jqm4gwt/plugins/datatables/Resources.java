package com.sksamuel.jqm4gwt.plugins.datatables;

import com.google.gwt.core.client.Callback;
import com.sksamuel.jqm4gwt.ScriptUtils;
import com.sksamuel.jqm4gwt.ScriptUtils.InjectCallback;

public interface Resources {

    class Loader {

        private static final String DATATABLES_CSS = "css/jquery.dataTables.min.css";
        private static final String DATATABLES_JS = "js/jquery.dataTables.min.js";

        public static void injectAll(final InjectCallback done) {
            ScriptUtils.waitJqmLoaded(new Callback<Void, Throwable>() {

                @Override
                public void onSuccess(Void result) {
                    ScriptUtils.injectCss(DATATABLES_CSS);
                    ScriptUtils.injectJs(done, DATATABLES_JS);
                }

                @Override
                public void onFailure(Throwable reason) {
                    throw new RuntimeException(reason);
                }
            });
        }
    }

}
