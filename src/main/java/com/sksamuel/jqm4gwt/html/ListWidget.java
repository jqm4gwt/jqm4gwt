package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 07:24:00
 * 
 *         Implementation of an
 *         <ul>
 *         or
 *         <ol>
 *         element.
 * 
 */
public class ListWidget extends ComplexPanel {

	/**
	 * Create a new ListWidget. It will have element type UL if ordered is
	 * false, and OL if ordered is true
	 */
	public ListWidget(boolean ordered) {
		if (ordered)
			setElement(Document.get().createOLElement());
		else
			setElement(Document.get().createULElement());
	}

	@Override
	public void add(Widget w) {
		add(w, getElement());
	}

	public void insert(IsWidget w, int beforeIndex) {
		insert(asWidgetOrNull(w), beforeIndex);
	}

	public void insert(Widget w, int beforeIndex) {
		insert(w, getElement(), beforeIndex, true);
	}

}
