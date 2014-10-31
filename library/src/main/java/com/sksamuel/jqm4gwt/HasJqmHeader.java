package com.sksamuel.jqm4gwt;

import com.google.gwt.user.client.ui.Composite;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * Composite based on JQMHeader (UiBinder template) may implement this interface by getWidget() call.
 */
public interface HasJqmHeader {
    JQMHeader getJqmHeader();
    Composite getHeaderStage();
}
