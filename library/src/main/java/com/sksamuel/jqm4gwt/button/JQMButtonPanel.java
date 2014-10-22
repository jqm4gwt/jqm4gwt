package com.sksamuel.jqm4gwt.button;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;

public class JQMButtonPanel extends JQMButton {

    private CustomFlowPanel stage;
    private FlowPanel flow;

    public JQMButtonPanel() {
        super("");
    }

    @UiChild(tagname = "widget")
    public void add(Widget w) {
        if (w == null) return;
        if (flow == null) {
            stage = new CustomFlowPanel(getElement());
            flow = new FlowPanel();
            stage.add(flow);
        }
        flow.add(w);
    }

    public void remove(Widget w) {
        if (flow != null && w != null) flow.remove(w);
    }

    public int getWidgetCount() {
        return flow != null ? flow.getWidgetCount() : 0;
    }

    public Widget[] getWidgets() {
        Widget[] widgets = new Widget[flow != null ? flow.getWidgetCount() : 0];
        if (flow != null) {
            for (int k = 0; k < flow.getWidgetCount(); k++) {
                widgets[k] = flow.getWidget(k);
            }
        }
        return widgets;
    }

}