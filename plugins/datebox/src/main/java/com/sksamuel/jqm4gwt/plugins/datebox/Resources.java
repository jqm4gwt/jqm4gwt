package com.sksamuel.jqm4gwt.plugins.datebox;

public interface Resources {

    class Loader {

        private static final String JQM_DATEBOX_CSS = "css/jqm-datebox-1.4.0.min.css";
        private static final String CALBOX_JS = "js/jqm-datebox-1.4.0.comp.calbox.min.js";
        private static final String DATEBOX_I18N_JS = "js/jquery.mobile.datebox.i18n.en_US.utf8.js";

        public static void injectAll() {
            com.sksamuel.jqm4gwt.Resources.Loader.injectCss(JQM_DATEBOX_CSS);
            com.sksamuel.jqm4gwt.Resources.Loader.injectJs(CALBOX_JS, DATEBOX_I18N_JS);
        }
    }

}
