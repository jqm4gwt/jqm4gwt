package com.sksamuel.jqm4gwt.plugins.iscroll;

public interface Resources {

    class Loader {

        private static final String ISCROLL_CSS = "css/jquery.mobile.iscrollview.css";
        private static final String ISCROLL_JS = "js/iscroll.min.js";
        private static final String JQM_ISCROLL_JS = "js/jquery.mobile.iscrollview.min.js";

        public static void injectAll() {
            com.sksamuel.jqm4gwt.Resources.Loader.injectCss(ISCROLL_CSS);
            com.sksamuel.jqm4gwt.Resources.Loader.injectJs(ISCROLL_JS, JQM_ISCROLL_JS);
        }
    }
}
