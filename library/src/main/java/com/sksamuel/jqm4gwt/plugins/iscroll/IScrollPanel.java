package com.sksamuel.jqm4gwt.plugins.iscroll;

import com.google.gwt.dom.client.Document;
import com.sksamuel.jqm4gwt.panel.JQMPanel;

/**
 * A wrapper for the iScroll plugin for JQuery Mobile. In order to use this
 * include the following into your html file:
 *
 *
 * @author jraymond
 *         Date: 4/15/13
 *         Time: 2:25 PM
 */
public class IScrollPanel extends JQMPanel {

    public IScrollPanel() {
        super(Document.get().createDivElement(), "content");
        setAttribute("data-iscroll", null);
    }
}
