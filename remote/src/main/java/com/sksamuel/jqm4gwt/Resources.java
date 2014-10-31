package com.sksamuel.jqm4gwt;

import java.util.Collection;

import com.sksamuel.jqm4gwt.ScriptUtils.InjectCallback;

public interface Resources {

    /**
     * ScriptUtils and SequentialScriptInjector are copied from jqm4gwt-library
     * to prevent project reference.
     **/
    class Loader {

        private static final String JQM_CSS = "http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css";
        private static final String JQM_FIXES_CSS = "css/jqm-fixes.css";
        private static final String JQM4GWT_CSS = "css/jqm4gwt.css";

        private static final String JQUERY_JS = "http://code.jquery.com/jquery-2.1.1.min.js";
        private static final String JQM_JS = "http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js";
        private static final String JQUERY_ACTUAL = "https://raw.githubusercontent.com/dreamerslab/jquery.actual/v1.0.16/jquery.actual.min.js";
        private static final String JQM_FIXES_JS = "js/jqm-fixes.js";

        public static void injectAll(final InjectCallback done) {
            ScriptUtils.injectCss(true, JQM_CSS);
            ScriptUtils.injectCss(JQM_FIXES_CSS, JQM4GWT_CSS);
            ScriptUtils.injectJs(true, new InjectCallback() {

                @Override
                public void onSuccess(Collection<String> result) {
                    ScriptUtils.injectJs(done, JQM_FIXES_JS);
                }

                @Override
                public void onFailure(Throwable reason) {
                    if (done != null) done.onFailure(reason);
                    else super.onFailure(reason);
                }

            }, JQUERY_JS, JQM_JS, JQUERY_ACTUAL);
        }
    }
}
