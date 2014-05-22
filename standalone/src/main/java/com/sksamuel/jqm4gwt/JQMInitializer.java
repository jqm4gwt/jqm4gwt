package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.EntryPoint;

public class JQMInitializer implements EntryPoint {

    @Override
    public void onModuleLoad() {
        Resources.Loader.injectAll(null);
    }

}
