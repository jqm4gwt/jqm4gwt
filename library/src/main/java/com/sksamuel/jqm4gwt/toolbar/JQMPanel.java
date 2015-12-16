package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.toolbar.JQMPanelEvent.PanelState;

/**
 * @author Gabi Boros gabi.boros@gmail.com 7 June 2013 15:24:13
 *
 * <br> This class models a Jquery Mobile
 *
 * <a href="http://demos.jquerymobile.com/1.4.5/panel/">Panel Widget</a>
 * <br> See also <a href="http://api.jquerymobile.com/panel/">API for Panel Widget</a>
 *
 *
 */
public class JQMPanel extends JQMWidget {

    public static enum Display {

        REVEAL("reveal"), PUSH("push"), OVERLAY("overlay");

        private final String jqmDisplay;

        private Display(String jqmDisplay) {
            this.jqmDisplay = jqmDisplay;
        }

        /** Returns the string value that JQM expects */
        public String getJqmValue() {
            return jqmDisplay;
        }

        public static Display fromJqmValue(String jqmValue) {
            if (jqmValue == null || jqmValue.isEmpty()) return null;
            for (Display i : Display.values()) {
                if (i.getJqmValue().equals(jqmValue)) return i;
            }
            return null;
        }
    }

    public static enum Position {

        LEFT("left"), RIGHT("right");

        private final String jqmPos;

        private Position(String jqmPos) {
            this.jqmPos = jqmPos;
        }

        /** Returns the string value that JQM expects */
        public String getJqmValue() {
            return jqmPos;
        }

        public static Position fromJqmValue(String jqmValue) {
            if (jqmValue == null || jqmValue.isEmpty()) return null;
            for (Position i : Position.values()) {
                if (i.getJqmValue().equals(jqmValue)) return i;
            }
            return null;
        }
    }

    private SimplePanel flowPanelContainer;
    private FlowPanel flowPanel;

    private boolean open;

    public JQMPanel() {
        this(Document.get().createUniqueId());
    }

    public @UiConstructor JQMPanel(String panelId) {
        flowPanel = new FlowPanel();
        flowPanel.setStylePrimaryName("ui-panel-inner");
        flowPanelContainer = new SimplePanel();
        flowPanelContainer.add(flowPanel);
        initWidget(flowPanelContainer);
        if (panelId == null || panelId.isEmpty()) setId(Document.get().createUniqueId());
        else setId(panelId);
        setDataRole("panel");
    }

    @UiChild(tagname="widget")
    public void add(Widget w) {
        flowPanel.add(w);
    }

    public void remove(Widget w) {
        flowPanel.remove(w);
    }

    public Widget[] getWidgets() {
        Widget[] widgets = new Widget[flowPanel.getWidgetCount()];
        for (int k = 0; k < flowPanel.getWidgetCount(); k++) {
            widgets[k] = flowPanel.getWidget(k);
        }
        return widgets;
    }

    /**
     * When you dynamically add content to a panel or make hidden content visible
     * while the panel is open, you have to call refresh().
     */
    public void refresh() {
        updateLayout(getElement());
    }

    private static native void updateLayout(Element elt) /*-{
        $wnd.$(elt).trigger("updatelayout");
    }-*/;

    /**
     *  Default: true
     *  <br> Sets whether the panel will animate when opening and closing.
     *  If set to false, the panel will just appear and disappear without animation.
     *  This is recommended for fastest performance.
     */
    public void setAnimate(boolean animate) {
        setAttribute("data-animate", String.valueOf(animate));
    }

    public boolean isAnimate() {
        String v = getAttribute("data-animate");
        return (v == null || v.isEmpty() || "true".equals(v));
    }

    /**
     * @param display - reveal, overlay, push
     * <br> Default: reveal
     * <br> The relationship of the panel to the page contents.
     * <br> This option accepts one of three values:
     * <br> reveal - Push the page over
     * <br> push -  Re-flow the content to fit the panel content as a column
     * <br> overlay - Sit over the content
     */
    public void setDisplay(Display display) {
        setAttribute("data-display", display != null ? display.getJqmValue() : null);
    }

    public Display getDisplay() {
        return Display.fromJqmValue(getAttribute("data-display"));
    }

    /**
     * @param position - left or right (default: left)
     * */
    public void setPosition(Position position) {
        setAttribute("data-position", position != null ? position.getJqmValue() : null);
    }

    public Position getPosition() {
        return Position.fromJqmValue(getAttribute("data-position"));
    }

    /**
     *  Default: false
     *  <br> Sets whether the panel has fixed positioning so the contents are in view
     *  even if the page is scrolled down. This also allows the page to scroll while
     *  the panel stays fixed.
     *  <br> We recommend not to enable this feature when panels are used within
     *  Android apps because of poor performance and display issues.
     */
    public void setPositionFixed(boolean positionFixed) {
        setAttribute("data-position-fixed", String.valueOf(positionFixed));
    }

    public boolean isPositionFixed() {
        return "true".equals(getAttribute("data-position-fixed"));
    }

    /**
     * Default: true
     * <br> Sets whether the panel can be closed by swiping left or right over the panel.
     */
    public void setSwipeClose(boolean swipeClose) {
        setAttribute("data-swipe-close", String.valueOf(swipeClose));
    }

    public boolean isSwipeClose() {
        String v = getAttribute("data-swipe-close");
        return (v == null || v.isEmpty() || "true".equals(v));
    }

    /**
     * Default: true
     * <br> Sets whether the panel can be closed by clicking outside onto the page.
     */
    public void setDismissible(boolean dismissible) {
        setAttribute("data-dismissible", String.valueOf(dismissible));
    }

    public boolean isDismissible() {
        String v = getAttribute("data-dismissible");
        return (v == null || v.isEmpty() || "true".equals(v));
    }

    public void open() {
        _open(getElement());
    }

    public void close() {
        _close(getElement());
    }

    public void toggle() {
        _toggle(getElement());
    }

    public boolean isOpen() {
        return open;
    }

    protected void onPanelBeforeClose() {
    }

    protected void onPanelBeforeOpen() {
    }

    protected void onPanelClose() {
    }

    protected void onPanelCreate() {
    }

    protected void onPanelOpen() {
    }

    protected void doPanelBeforeClose() {
        onPanelBeforeClose();
        JQMPanelEvent.fire(this, PanelState.BEFORE_CLOSE);
    }

    protected void doPanelBeforeOpen() {
        onPanelBeforeOpen();
        JQMPanelEvent.fire(this, PanelState.BEFORE_OPEN);
    }

    protected void doPanelClose() {
        open = false;
        onPanelClose();
        JQMPanelEvent.fire(this, PanelState.CLOSE);
    }

    protected void doPanelCreate() {
        onPanelCreate();
        JQMPanelEvent.fire(this, PanelState.CREATE);
    }

    protected void doPanelOpen() {
        open = true;
        if (getParent() == null) {
            RootPanel.get().add(this); // probably global panel, needed to make GWT events working
        }
        onPanelOpen();
        JQMPanelEvent.fire(this, PanelState.OPEN);
    }

    public HandlerRegistration addPanelHandler(JQMPanelEvent.Handler handler) {
        return addHandler(handler, JQMPanelEvent.getType());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        Widget parent = getParent();
        if (parent instanceof JQMPage) bindLifecycleEvents(this, ((JQMPage) parent).getId());
    }

    @Override
    protected void onUnload() {
        Widget parent = getParent();
        if (parent instanceof JQMPage) unbindLifecycleEvents(((JQMPage) parent).getId());
        super.onUnload();
    }

    private static native void bindLifecycleEvents(JQMPanel p, String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$('div[data-url="' + id + '"]').on("panelbeforeclose",
            function(event, ui) { p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelBeforeClose()(); });

        $wnd.$('div[data-url="' + id + '"]').on("panelbeforeopen",
            function(event, ui) { p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelBeforeOpen()(); });

        $wnd.$('div[data-url="' + id + '"]').on("panelclose",
            function(event, ui) { p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelClose()(); });

        $wnd.$('div[data-url="' + id + '"]').on("panelcreate",
            function(event, ui) { p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelCreate()(); });

        $wnd.$('div[data-url="' + id + '"]').on("panelopen",
            function(event, ui) { p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelOpen()(); });
    }-*/;

    private static native void bindLifecycleEventsExternal(JQMPanel p, String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$("body").on("panelbeforeclose",
            function(event, ui) {
                if (event.target.id === id) {
                    p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelBeforeClose()();
                }
            });

        $wnd.$("body").on("panelbeforeopen",
            function(event, ui) {
                if (event.target.id === id) {
                    p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelBeforeOpen()();
                }
            });

        $wnd.$("body").on("panelclose",
            function(event, ui) {
                if (event.target.id === id) {
                    p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelClose()();
                }
            });

        $wnd.$("body").on("panelcreate",
            function(event, ui) {
                if (event.target.id === id) {
                    p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelCreate()();
                }
            });

        $wnd.$("body").on("panelopen",
            function(event, ui) {
                if (event.target.id === id) {
                    p.@com.sksamuel.jqm4gwt.toolbar.JQMPanel::doPanelOpen()();
                }
            });
    }-*/;

    private static native void unbindLifecycleEvents(String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var p = $wnd.$("#" + id);
        p.off("panelbeforeclose");
        p.off("panelbeforeopen");
        p.off("panelclose");
        p.off("panelcreate");
        p.off("panelopen");
    }-*/;

    private static native void _open(Element elt) /*-{
        $wnd.$(elt).panel("open")
    }-*/;

    private static native void _close(Element elt) /*-{
        $wnd.$(elt).panel("close")
    }-*/;

    private static native void _toggle(Element elt) /*-{
        $wnd.$(elt).panel("toggle")
    }-*/;

    public boolean isExternal() {
        NodeList<Node> children = Document.get().getBody().getChildNodes();
        if (children == null || children.getLength() == 0) return false;
        for (int i = 0; i < children.getLength(); i++) {
            if (children.getItem(i) == getElement()) return true;
        }
        return false;
    }

    /**
     * See <a href="http://demos.jquerymobile.com/1.4.5/panel-external/">External Panels</a>
     */
    public void setExternal(boolean value) {
        if (value == isExternal()) return;
        if (value) {
            Document.get().getBody().appendChild(getElement());
            Scheduler.get().scheduleFinally(new ScheduledCommand() {
                @Override
                public void execute() {
                    manualInitialize(getElement());
                    bindLifecycleEventsExternal(JQMPanel.this, getId());
                }
            });
        } else {
            // TODO (not important): implement unbindLifecycleEventsExternal() through static field & method
            Document.get().getBody().removeChild(getElement());
        }
    }

    private static native void manualInitialize(Element elt) /*-{
        $wnd.$(elt).panel().enhanceWithin();
    }-*/;

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setThemeEx(getElement(), themeName, JQMCommon.STYLE_UI_BODY);
    }

    @Override
    public String getTheme() {
        return JQMCommon.getThemeEx(getElement(), JQMCommon.STYLE_UI_BODY);
    }

}
