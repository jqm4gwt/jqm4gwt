package com.sksamuel.jqm4gwt.form;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 20:26:51
 * <p>
 *         An implementation of a div based panel that has data-role set to
 *         fieldcontain.
 * </p><p>
 *         This is convenience class intended to be used by form elements, which
 *         are mostly based around a field container parent div.
 * </p>
 */
public class JQMFieldContainer extends JQMWidget {

	/**
	 * The panel to delegate this composite to
	 */
	protected final FlowPanel flow;

	protected JQMFieldContainer() {
		flow = new FlowPanel();
		initWidget(flow);
		setId();
		setDataRole("fieldcontain");
		addStyleName("jqm4gwt-fieldcontain");
	}

	protected void add(Widget widget) {
		flow.add(widget);
	}

	protected void remove(Widget widget) {
		flow.remove(widget);
	}

	/**
     * Label/Legend/Text can be hidden for form elements.
	 * <p><a href="http://view.jquerymobile.com/1.3.2/dist/demos/widgets/forms/form-hide-label.html">
	 * Field containers, hide label/legend</a></p>
	 */
	public void setLabelHidden(boolean value) {
        if (value) addStyleName("ui-hide-label");
        else removeStyleName("ui-hide-label");
    }
}
