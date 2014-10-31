package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.html.Heading;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 13:27:36
 *
 * <p/> Superclass for toolbars - {@link JQMHeader} and {@link JQMFooter}
 *
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/toolbar/">Toolbar</a>
 * <p/> See also <a href="http://api.jquerymobile.com/toolbar/">Toolbar API</a>
 */
public abstract class JQMToolbar extends JQMWidget implements HasText, HasFixedPosition {

    private final FlowPanel flow;

    /** The header contains the text, it can be null */
    private Heading header;

    /**
     * Creates a new toolbar with a header element for the given text
     */
    protected JQMToolbar(String dataRole, String styleName, String text) {

        flow = new FlowPanel();
        initWidget(flow);

        setDataRole(dataRole);
        setStyleName(styleName);

        setText(text);
        setTapToggle(false);
    }

    /** Adds the given widget to the toolbar */
    @UiChild(tagname="widget")
    public void add(Widget w) {
        flow.add(w);
    }

    /** Returns the text of the Hn element */
    @Override
    public String getText() {
        return header == null ? null : header.getText();
    }

    /**
     * Sets the value of the Hn element
     */
    @Override
    public void setText(String text) {
        if (text == null) {
            if (header != null) flow.remove(header);
        } else {
            if (header == null) {
                header = new Heading(1);
                flow.add(header);
            }
            header.setText(text);
        }
    }

    public Widget[] getWidgets() {
        Widget[] widgets = new Widget[flow.getWidgetCount()];
        for (int k = 0; k < flow.getWidgetCount(); k++) {
            widgets[k] = flow.getWidget(k);
        }
        return widgets;
    }

    public void insert(Widget left, int i) {
        flow.insert(left, i);
    }

    @Override
    public final boolean isFixed() {
        return "fixed".equals(getAttribute("data-position"));
    }

    @Override
    public final void setFixed(boolean fixed) {
        if (fixed) setAttribute("data-position", "fixed");
        else removeAttribute("data-position");
    }

    /** Removes the given widget from the toolbar */
    public void remove(Widget w) {
        flow.remove(w);
    }

    /** Removes the Hn text if any is set. */
    public void removeText() {
        if (header != null) {
            flow.remove(header);
            header = null;
        }
    }

    public boolean isTapToggle() {
        String s = getAttribute("data-tap-toggle");
        if (s == null || s.isEmpty()) return true;
        return !("false".equals(s));
    }

    /** Sets whether the fixed toolbar's visibility can be toggled by tapping on the page. */
    public void setTapToggle(boolean value) {
        if (value) removeAttribute("data-tap-toggle");
        else setAttribute("data-tap-toggle", "false");
    }

    public boolean isUpdatePagePadding() {
        String s = getAttribute("data-update-page-padding");
        if (s == null || s.isEmpty()) return true;
        return !("false".equals(s));
    }

    /**
     *  Have the page top and bottom padding updated on resize, transition, "updatelayout" events
     *  (the framework always updates the padding on the "pageshow" event).
     */
    public void setUpdatePagePadding(boolean value) {
        if (value) removeAttribute("data-update-page-padding");
        else setAttribute("data-update-page-padding", "false");
    }

    private static native void updatePagePadding(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-toolbar') !== undefined) {
            w.toolbar('updatePagePadding');
        }
    }-*/;

    public void updatePagePadding() {
        updatePagePadding(getElement());
    }

    /**
     * Call to refresh the list after a programmatic change is made.
     */
    public void refresh() {
        refresh(getElement());
    }

    private static native void refresh(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-toolbar') !== undefined) {
            w.toolbar('refresh');
        }
    }-*/;

    private static native void hide(Element elt) /*-{
        $wnd.$(elt).toolbar('hide');
    }-*/;

    private static native void show(Element elt) /*-{
        $wnd.$(elt).toolbar('show');
    }-*/;

    private static native void toggle(Element elt) /*-{
        $wnd.$(elt).toolbar('toggle');
    }-*/;

    public void hide() {
        hide(getElement());
    }

    public void show() {
        show(getElement());
    }

    public void toggle() {
        toggle(getElement());
    }
}
