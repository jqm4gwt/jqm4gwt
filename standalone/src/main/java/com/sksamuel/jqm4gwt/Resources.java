package com.sksamuel.jqm4gwt;

import com.sksamuel.jqm4gwt.ScriptUtils.InjectCallback;

public interface Resources {

    /**
     * ScriptUtils and SequentialScriptInjector are copied from jqm4gwt-library
     * to prevent project reference.
     **/
    class Loader {

        private static final String JQM_CSS = "css/jquery.mobile-1.4.5.min.css";
        private static final String JQM_FIXES_CSS = "css/jqm-fixes.css";
        private static final String JQM4GWT_CSS = "css/jqm4gwt.css";

        private static final String JQUERY_JS = "js/jquery-2.1.1.min.js";
        private static final String JQM_JS = "js/jquery.mobile-1.4.5.min.js";
        private static final String JQUERY_ACTUAL = "js/jquery.actual.min.js";
        private static final String JQM_FIXES_JS = "js/jqm-fixes.js";

        public static void injectAll(final InjectCallback done) {
            ScriptUtils.injectCss(JQM_CSS, JQM_FIXES_CSS, JQM4GWT_CSS);
            ScriptUtils.injectJs(done, JQUERY_JS, JQM_JS, JQUERY_ACTUAL, JQM_FIXES_JS);
        }
    }
}
