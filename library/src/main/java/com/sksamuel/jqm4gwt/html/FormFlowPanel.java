package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;

/** The same as FlowPanel, but based on &lt;form&gt; element. */
public class FormFlowPanel extends CustomFlowPanel {

    public FormFlowPanel() {
        super(Document.get().createFormElement());
    }

}
