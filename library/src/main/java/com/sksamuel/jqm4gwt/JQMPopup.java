package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMPopupEvent.PopupState;

/**
 * @author Stephen K Samuel samspade79@gmail.com 16 Sep 2012 00:26:35
 *
 * <br> See <a href="http://demos.jquerymobile.com/1.4.5/popup/">Popup</a>
 */
public class JQMPopup extends JQMContainer {

    private static int counter = 1;

    private String tolerance;

    /**
     * Creates a new {@link JQMPopup} with an automatically assigned id in the form popup-xx where XX is an incrementing number.
     */
    public JQMPopup() {
        this("popup-" + (counter++));
    }

    /**
     * Creates a new {@link JQMPopup} with explicitly defined id.
     */
    public JQMPopup(String popupId) {
        super(popupId, "popup");
    }

    public JQMPopup(Widget... widgets) {
        this();
        add(widgets);
    }

    @Override
    public void setContainerId(String containerId) {
        super.setContainerId(containerId);
        removeAttribute("data-url");
    }

    /**
     * Automatically assigns an id in the form popup-xx where XX is an incrementing number.
     */
    @Override
    public JQMContainer withContainerId() {
        setContainerId("popup-" + (counter++));
        return this;
    }

    private static native void initialize(Element elt) /*-{
        $wnd.$(elt).popup();
    }-*/;

    /**
     * Initialize dynamically added popup with static content (for example added after page is loaded).
     * <br> Also see {@link JQMPopup#waitInitOpen(String)} if you need to wait for images to be loaded.
     */
    public void initDynamic() {
        initialize(getElement());
    }

    private static native void waitInitOpen(String waitForSelector, Element elt) /*-{
        // Wait with opening the popup until the popup image has been loaded in the DOM.
        // This ensures the popup gets the correct size and position
        var pop = $wnd.$(elt);
        var saveDisp = pop.css("display");
        if (saveDisp !== "none") pop.css("display", "none");
        $wnd.$( waitForSelector, elt ).load(function() {
            pop.css("display", saveDisp);
            pop.popup();
            pop.popup( "open" );
            clearTimeout( fallback ); // Clear the fallback
        });
        // Fallback in case the browser doesn't fire a load event
        var fallback = setTimeout(function() {
            pop.css("display", saveDisp);
            pop.popup();
            pop.popup( "open" );
        }, 2000);
    }-*/;

    /**
     * First wait till some elements are loaded, then initialize and open dynamically added popup.
     * <br> See <a href="http://demos.jquerymobile.com/1.4.5/popup-dynamic/#&ui-state=dialog&ui-state=dialog&ui-state=dialog">Dynamic popup with images</a>
     */
    public void waitInitOpen(String waitForSelector) {
        if (waitForSelector == null || waitForSelector.isEmpty()) {
            initDynamic();
            open();
            return;
        }
        waitInitOpen(waitForSelector, getElement());
    }

    private static native void _close(Element elt) /*-{
        $wnd.$(elt).popup("close");
    }-*/;

    private static native void _open(Element elt) /*-{
        $wnd.$(elt).popup("open");
    }-*/;

    private static native void _open(Element popupElt, String positionToSelector) /*-{
        $wnd.$(popupElt).popup("open", { positionTo: positionToSelector });
    }-*/;

    public static native void close(String popupSelector) /*-{
        $wnd.$(popupSelector).popup("close");
    }-*/;

    public static native void open(String popupSelector) /*-{
        $wnd.$(popupSelector).popup("open");
    }-*/;

    public static native void open(String popupSelector, String positionToSelector) /*-{
        $wnd.$(popupSelector).popup("open", { positionTo: positionToSelector });
    }-*/;

    public JQMPopup close() {
        _close(getElement());
        return this;
    }

    public JQMPopup open() {
        _open(getElement());
        return this;
    }

    public JQMPopup openPosTo(String positionToSelector) {
        _open(getElement(), positionToSelector);
        return this;
    }

    @Override
    public String getRelType() {
        return "popup";
    }

    @Override
    public String getTheme() {
        String rslt = JQMCommon.getThemeEx(this, JQMCommon.STYLE_UI_BODY);
        if (rslt == null || rslt.isEmpty()) rslt = super.getTheme();
        return rslt;
    }

    @Override
    public void setTheme(String theme) {
        JQMCommon.setThemeEx(this, theme, JQMCommon.STYLE_UI_BODY);
    }

    public String getOverlayTheme() {
        return getAttribute("data-overlay-theme");
    }

    public void setOverlayTheme(String theme) {
        setAttribute("data-overlay-theme", theme);
    }

    public JQMPopup withOverlayTheme(String theme) {
        setOverlayTheme(theme);
        return this;
    }

    public boolean isPadding() {
        return JQMCommon.hasStyle(this, "ui-content");
    }

    public void setPadding(boolean padding) {
        if (padding) {
            addStyleName("ui-content");
        } else {
            removeStyleName("ui-content");
        }
    }

    public JQMPopup withPadding(boolean padding) {
        setPadding(padding);
        return this;
    }

    public String getPosition() {
        return JQMCommon.getPopupPos(this);
    }

    /**
     * @param pos - origin, window, or jQuery selector.
     * <br> Element is searched by selector, and then popup is centered over it.
     * The selector is filtered for elements that are visible with ":visible".
     * If the result is empty, the popup will be centered in the window.
     */
    public void setPosition(String pos) {
        JQMCommon.setPopupPos(this, pos);
    }

    @Override
    public String toString() {
        return "JQMPopup [id=" + id + "]";
    }

    public boolean isDialog() {
        String s = JQMCommon.getAttribute(this, "data-dismissible");
        return "false".equals(s);
    }

    /**
     * @param value - if true creates a modal style dialog, to prevent the click-outside-to-close
     * behavior so people need to interact with popup buttons to close it.
     */
    public void setDialog(boolean value) {
        JQMCommon.setAttribute(this, "data-dismissible", value ? "false" : null);
    }

    public boolean isDismissible() {
        return !isDialog();
    }

    /**
     * Opposite to setDialog()
     * <br> Sets whether clicking outside the popup or pressing Escape while the popup is
     * open will close the popup.
     * <br> Note: When history support is turned on, pressing the browser's "Back" button
     * will dismiss the popup even if this option is set to false.
     */
    public void setDismissible(boolean value) {
        setDialog(!value);
    }

    public boolean isShadow() {
        String s = JQMCommon.getAttribute(this, "data-shadow");
        boolean noShadow = "false".equals(s);
        return !noShadow;
    }

    public void setShadow(boolean value) {
        JQMCommon.setAttribute(this, "data-shadow", value ? null : "false");
    }

    public String getArrows() {
        return JQMCommon.getAttribute(this, "data-arrow");
    }

    /**
     * @param arrows - possible values: true, false.
     * Also comma-separated list of edge abbreviations: l t r b
     * <br> "l" for left, "t" for top, "r" for right, and "b" for bottom.
     *
     * <br> <b>The order in which the edges are given matters, see explanation below.</b>
     *
     * <br> For each edge given in the list, the framework calculates:
     * <br> 1. the distance between the tip of the arrow and the center of the origin
     * <br> 2. and whether minimizing the above distance would cause the arrow to appear
     * too close to one of the corners of the popup along the given edge.
     *
     * <br> If the second condition applies, the edge is discarded as a possible solution
     * for placing the arrow. Otherwise, the calculated distance is examined.
     * If it is 0, meaning that the popup can be placed exactly such that the tip of
     * the arrow points at the center of the origin, no further edges are examined,
     * and the popup is positioned along the last examined edge.
     *
     */
    public void setArrows(String arrows) {
        JQMCommon.setAttribute(this, "data-arrow", arrows);
        Element elt = getElement();
        if (isInitialized(elt)) _setArrows(elt, arrows);
    }

    private static native void _setArrows(Element elt, String arrows) /*-{
        $wnd.$(elt).popup("option", "arrow", arrows);
    }-*/;

    /** Triggered when a popup has completely closed */
    protected void onAfterClose() {
    }

    protected void doAfterClose() {
        onAfterClose();
        JQMPopupEvent.fire(this, PopupState.AFTER_CLOSE);
    }

    /** Triggered after a popup has completely opened */
    protected void onAfterOpen() {
    }

    protected void doAfterOpen() {
        onAfterOpen();
        JQMPopupEvent.fire(this, PopupState.AFTER_OPEN);
    }

    /**
     * Triggered before a popup computes the coordinates where it will appear.
     * <br> Handling this event gives an opportunity to modify the content of
     * the popup before it appears on the screen. For example, the content can be
     * scaled or parts of it can be hidden or removed if it is too wide or too tall.
     * You can also modify the options parameter to affect the popup's placement.
     *
     * @param openOptions
     **/
    protected void onBeforePosition(PopupOptions openOptions) {
    }

    public static class PopupOptions {

        private Double x;
        private Double y;
        private String positionTo;

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public String getPositionTo() {
            return positionTo;
        }

        public void setPositionTo(String positionTo) {
            this.positionTo = positionTo;
        }

        @Override
        public String toString() {
            return "PopupOptions [x=" + JQMContext.round(x, 2)
                    + ", y=" + JQMContext.round(y, 2) + ", positionTo=" + positionTo + "]";
        }
    }

    /**
     * @param openOptions = { x: 123, y: 456, positionTo: #xxx }
     */
    protected void doBeforePosition(JavaScriptObject openOptions) {
        String p = JsUtils.getObjValue(openOptions, "positionTo");
        Double x = JsUtils.getObjDoubleValue(openOptions, "x");
        Double y = JsUtils.getObjDoubleValue(openOptions, "y");

        PopupOptions opts = new PopupOptions();
        opts.setPositionTo(p);
        opts.setX(x);
        opts.setY(y);

        onBeforePosition(opts);
        JQMPopupEvent.fire(this, PopupState.BEFORE_POSITION, opts);

        String newP = opts.getPositionTo();
        if (newP == null) {
            if (p != null && !p.isEmpty()) JsUtils.deleteObjProperty(openOptions, "positionTo");
        } else {
            JsUtils.setObjValue(openOptions, "positionTo", newP);
        }
        Double newX = opts.getX();
        if (newX == null) {
            if (x != null) JsUtils.deleteObjProperty(openOptions, "x");
        } else {
            JsUtils.setObjDoubleValue(openOptions, "x", newX);
        }
        Double newY = opts.getY();
        if (newY == null) {
            if (y != null) JsUtils.deleteObjProperty(openOptions, "y");
        } else {
            JsUtils.setObjDoubleValue(openOptions, "y", newY);
        }
    }

    private static native void setTolerance(Element elt, String tolerance) /*-{
        $wnd.$(elt).popup("option", "tolerance", tolerance);
    }-*/;

    private static native String getTolerance(Element elt) /*-{
        return $wnd.$(elt).popup("option", "tolerance");
    }-*/;

    protected static native boolean isInitialized(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return false; // jQuery is not loaded
        var w = $wnd.$(elt);
        return w.data('mobile-popup') !== undefined;
    }-*/;

    public String getTolerance() {
        Element elt = getElement();
        if (isInitialized(elt)) return getTolerance(elt);
        else return tolerance;
    }

    /**
     * @param tolerance - Default: "30,15,30,15"
     * <br> Sets the minimum distance from the edge of the window for the corresponding edge
     * of the popup. By default, the values above will be used for the distance from
     * the top, right, bottom, and left edge of the window, respectively.
     *
     * <br> You can specify a value for this option in one of four ways:
     * <br> 1. Empty string, null, or some other falsy value. This will cause the popup to revert
     * to the above default values.
     *
     * <br> 2. A single number. This number will be used for all four edge tolerances.
     *
     * <br> 3. Two numbers separated by a comma. The first number will be used for tolerances
     * from the top and bottom edge of the window, and the second number will be used for tolerances
     * from the left and right edge of the window.
     *
     * <br> 4. Four comma-separated numbers. The first will be used for tolerance from the top edge,
     * the second for tolerance from the right edge, the third for tolerance from the bottom edge,
     * and the fourth for tolerance from the left edge.
     *
     */
    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
        Element elt = getElement();
        if (isInitialized(elt)) setTolerance(elt, tolerance);
    }

    /**
     * Based on mobile.popup _desiredCoords()
     *
     * @param positionTo - jQuery selector for finding element (must be visible)
     * @return - { left: 111, top: 222, width: 333, height: 444 }
     */
    private static native JavaScriptObject jsCalcEltCoords(String positionTo) /*-{
        var dst = null;
        try {
            dst = $wnd.$(positionTo);
        } catch(err) {
            dst = null;
        }
        if (dst) {
            dst.filter(":visible");
            if (dst.length === 0) {
                dst = null;
            }
        }

        // If an element was found, preparing result
        if (dst) {
            var offset = dst.offset();
            var rslt = { left: offset.left, top: offset.top, width: dst.outerWidth(), height: dst.outerHeight() };
            return rslt;
        } else {
            return null;
        }
    }-*/;

    public static class EltCoords {

        public final double left;
        public final double top;
        public final double width;
        public final double height;

        public EltCoords(double left, double top, double width, double height) {
            super();
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "EltCoords [left=" + JQMContext.round(left, 2)
                    + ", top=" + JQMContext.round(top, 2)
                    + ", width=" + JQMContext.round(width, 2)
                    + ", height=" + JQMContext.round(height, 2) + "]";
        }
    }

    /**
     * @param positionTo - jQuery selector for finding element (must be visible)
     */
    public static EltCoords calcEltCoords(String positionTo) {
       JavaScriptObject o = jsCalcEltCoords(positionTo);
       if (o == null) return null;
       Double left = JsUtils.getObjDoubleValue(o, "left");
       Double top = JsUtils.getObjDoubleValue(o, "top");
       Double width = JsUtils.getObjDoubleValue(o, "width");
       Double height = JsUtils.getObjDoubleValue(o, "height");
       return new EltCoords(left, top, width, height);
    }

    private static native void bindLifecycleEvents(JQMPopup p, Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var popup = $wnd.$(elt);
        popup.on("popupafterclose", function(event, ui) {
            p.@com.sksamuel.jqm4gwt.JQMPopup::doAfterClose()();
        });
        popup.on("popupafteropen", function(event, ui) {
            p.@com.sksamuel.jqm4gwt.JQMPopup::doAfterOpen()();
        });
        popup.on("popupbeforeposition", function(event, ui) {
            //alert(JSON.stringify(ui));
            p.@com.sksamuel.jqm4gwt.JQMPopup::doBeforePosition(Lcom/google/gwt/core/client/JavaScriptObject;)(ui);
        });
    }-*/;

    private static native void unbindLifecycleEvents(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var popup = $wnd.$(elt);
        popup.off("popupafterclose");
        popup.off("popupafteropen");
        popup.off("popupbeforeposition");
        popup.off("popupcreate");
    }-*/;

    private static native void bindCreated(Element elt, JQMPopup pop) /*-{
        $wnd.$(elt).on( 'popupcreate', function( event, ui ) {
            pop.@com.sksamuel.jqm4gwt.JQMPopup::created()();
        });
    }-*/;

    private void created() {
        setTolerance(getElement(), tolerance);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        Element elt = getElement();
        bindLifecycleEvents(this, elt);
        if (tolerance != null) {
            bindCreated(elt, this);
        }
    }

    @Override
    protected void onUnload() {
        unbindLifecycleEvents(getElement());
        super.onUnload();
    }

    public HandlerRegistration addPopupHandler(JQMPopupEvent.Handler handler) {
        return addHandler(handler, JQMPopupEvent.getType());
    }

    public boolean isCloseThenClick() {
        String s = JQMCommon.getAttribute(getElement(), "data-closeThenClick");
        return "true".equals(s);
    }

    /**
     * Works in conjunction with dismissible, will pass/re-route outside click to proper widget
     * after popup is closed, so you don't have to tap widget one more time (i.e. prevents click event
     * eating by popup).
     */
    public void setCloseThenClick(boolean value) {
        JQMCommon.setAttribute(getElement(), "data-closeThenClick", value ? "true" : null);
    }

}

