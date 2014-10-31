package com.sksamuel.jqm4gwt;

import com.google.gwt.user.client.ui.Composite;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;

/**
 * Composite based on JQMFooter (UiBinder template) may implement this interface by getWidget() call.
 */
public interface HasJqmFooter {
    JQMFooter getJqmFooter();
    Composite getFooterStage();
}
