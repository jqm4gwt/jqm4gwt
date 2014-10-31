package com.sksamuel.jqm4gwt.form;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMCommon;
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
        addStyleName("jqm4gwt-fieldcontain");
        addStyleName("ui-field-contain");
    }

    protected JQMFieldContainer(FlowPanel externFlow) {
        flow = externFlow;
    }

    protected void add(Widget widget) {
        flow.add(widget);
    }

    protected void remove(Widget widget) {
        flow.remove(widget);
    }

    public boolean isLabelHidden() {
        return JQMCommon.isLabelHidden(this);
    }

    /**
     * Label/Legend/Text can be hidden for form elements.
     * <p><a href="http://demos.jquerymobile.com/1.4.5/forms-label-hidden-accessible/">
     * Field containers, hide label/legend</a></p>
     */
    public void setLabelHidden(boolean value) {
        JQMCommon.setLabelHidden(this, value);
    }
}
