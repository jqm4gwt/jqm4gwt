package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.AttachDetachException;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

/**
 * Flow panel based on the given HTML element sent as constructor argument.
 * <p/> A panel that formats its child widgets using the default HTML layout behavior.
 */
public class CustomFlowPanel extends ComplexPanel {

	/**
	 * Creates an empty flow panel based on the given element.
	 */
	public CustomFlowPanel(Element e) {
		setElement(e); // no excessive FlowPanel() -> DOM.createDiv()
	}

	// THE FOLLOWING CODE is exact copy from FlowPanel and ComplexPanel

	/** The command used to orphan children. */
    private AttachDetachException.Command orphanCommand;

	private WidgetCollection children = null;

	@Override
    protected WidgetCollection getChildren() {
	    return children != null ? children : super.getChildren();
	}

	// ComplexPanel.doLogicalClear() is not available, that's why we need this stupid copy
	private void _doLogicalClear() {
	    // Only use one orphan command per panel to avoid object creation.
	    if (orphanCommand == null) {
	        orphanCommand = new AttachDetachException.Command() {
    	        @Override
                public void execute(Widget w) {
    	            orphan(w);
    	        }
    	    };
	    }
	    try {
	        AttachDetachException.tryCommand(this, orphanCommand);
	    } finally {
	        children = new WidgetCollection(this);
	    }
	}

    /**
     * Adds a new child widget to the panel.
     *
     * @param w the widget to be added
     */
    @Override
    public void add(Widget w) {
        Element elt = getElement();
        add(w, elt);
    }

    @Override
    public void clear() {
        try {
            _doLogicalClear();
        } finally {
            // Remove all existing child nodes.
            Node child = getElement().getFirstChild();
            while (child != null) {
                getElement().removeChild(child);
                child = getElement().getFirstChild();
            }
        }
    }

    public void insert(IsWidget w, int beforeIndex) {
        insert(asWidgetOrNull(w), beforeIndex);
    }

    /**
     * Inserts a widget before the specified index.
     *
     * @param w the widget to be inserted
     * @param beforeIndex the index before which it will be inserted
     * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range
     */
    public void insert(Widget w, int beforeIndex) {
        Element elt = getElement();
        insert(w, elt, beforeIndex, true);
    }

}
