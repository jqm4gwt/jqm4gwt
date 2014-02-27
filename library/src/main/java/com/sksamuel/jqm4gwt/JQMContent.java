package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Document;
import com.sksamuel.jqm4gwt.panel.JQMPanel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 23:55:27
 *
 * <p/> A panel that is used for all the main content widgets.
 * <p/> This maps to the &lt;div class="ui-content" role="main" /> element in the page.
 *
 */
public class JQMContent extends JQMPanel {

	JQMContent() {
		super(Document.get().createDivElement(), null/*dataRole*/, "ui-content");
		JQMCommon.setRole(getElement(), "main");
	}

}
