package com.sksamuel.jqm4gwt;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 12:57:43
 *
 * <br>    The {@link JQMContext} provides methods that facilitate interaction
 *         between GWT, JQM and the DOM.
 *
 */
public class JQMContext {

    public static interface WidgetDefaults {
        /**
         * Called after a widget becomes attached to the browser's document.
         */
        void loaded(Widget w);
    }

    private static WidgetDefaults widgetDefaults;

    private static TransitionIntf<?> defaultTransition = Transition.FADE;
    private static TransitionIntf<?> defaultDialogTransition = Transition.POP;
    private static boolean defaultTransistionDirection = false;

    private static boolean defaultChangeHash = true;

    // FIXME: it can work only if called from mobileinit event handler (i.e. before jqm is loaded)
    /*
      <script type="text/javascript">
        // See http://www.gajotres.net/prevent-jquery-mobile-from-using-hash-navigation/
        $(document).on("mobileinit", function () {
          $.mobile.hashListeningEnabled = false;
          $.mobile.pushStateEnabled = false;
          $.mobile.changePage.defaults.changeHash = false;
        });
      </script>
    */
    public static native void disableHashListening() /*-{
        $wnd.$.mobile.hashListeningEnabled = false;
    }-*/;

    // FIXME: it can work only if called from mobileinit event handler (i.e. before jqm is loaded)
    public static native void disablePushStateEnabled() /*-{
        $wnd.$.mobile.pushStateEnabled = false;
    }-*/;

    /**
     * Appends the given {@link JQMPage} to the DOM so that JQueryMobile is
     * able to manipulate it and render it. This should only be done once per
     * page, otherwise duplicate HTML would be added to the DOM and this would
     * result in elements with overlapping IDs.
     *
     */
    public static void attachAndEnhance(JQMContainer container) {
        if (container == null) return;
        RootPanel p = getRootPanel();
        if (p == null) return;
        p.add(container);
        enhance(container);
        container.setEnhanced(true);
    }

    public static RootPanel getRootPanel() {
        String id = Mobile.getPageContainer().getId();
        if (id != null && id.isEmpty()) id = null;
        return RootPanel.get(id);
    }

    /**
     * Programatically change the displayed page to the given {@link JQMPage}
     * instance. This uses the default transition which is Transition.FADE
     */
    public static void changePage(JQMContainer container) {
        changePage(container, false/*dialog*/);
    }

    public static void changePage(JQMContainer container, boolean dialog) {
        TransitionIntf<?> t = container.getTransition();
        if (t == null) {
            t = dialog || JQMCommon.isDataDialog(container.getElement())
                    ? getDefaultDialogTransition() : getDefaultTransition();
        }
        changePage(container, dialog, t);
    }

    /**
     * Change the displayed page to the given {@link JQMPage} instance using
     * the supplied transition.
     */
    public static void changePage(JQMContainer container, TransitionIntf<?> transition) {
        changePage(container, false/*dialog*/, transition);
    }

    public static void changePage(JQMContainer container, boolean dialog, TransitionIntf<?> transition) {
        changePage(container, dialog, transition, defaultTransistionDirection);
    }

    /**
     * Change the displayed page to the given {@link JQMPage} instance using
     * the supplied transition and reverse setting.
     */
    public static void changePage(JQMContainer container, TransitionIntf<?> transition, boolean reverse) {
        changePage(container, false/*dialog*/, transition, reverse);
    }

    public static void changePage(JQMContainer container, boolean dialog, TransitionIntf<?> transition,
                                  boolean reverse) {
        Mobile.changePage("#" + container.getId(), transition, reverse, defaultChangeHash, dialog);
    }

    private static void enhance(JQMContainer c) {
        render(c.getElement());
    }

    public static TransitionIntf<?> getDefaultTransition() {
        String val = getDefaultTransitionImpl();
        TransitionIntf<?> t = defaultTransition.parseJqmValue(val);
        return t != null ? t : Transition.FADE;
    }

    private static native String getDefaultTransitionImpl() /*-{
        return $wnd.$.mobile.defaultPageTransition;
    }-*/;

    public static TransitionIntf<?> getDefaultDialogTransition() {
        String val = getDefaultDialogTransitionImpl();
        TransitionIntf<?> t = defaultDialogTransition.parseJqmValue(val);
        return t != null ? t : Transition.POP;
    }

    private static native String getDefaultDialogTransitionImpl() /*-{
        return $wnd.$.mobile.defaultDialogTransition;
    }-*/;

    /**
     * Return the pixel offset of an element from the left of the document.
     * This can also be thought of as the y coordinate of the top of the
     * elements bounding box.
     *
     * @param id
     *            the id of the element to find the offset
     */
    public static native int getLeft(String id) /*-{
        return $wnd.$("#" + id).offset().left;
    }-*/;

    /**
     * Return the pixel offset of an element from the top of the document.
     * This can also be thought of as the x coordinate of the left of the
     * elements bounding box.
     *
     * @param id
     *            the id of the element to find the offset
     */
    public static native int getTop(String id) /*-{
        return $wnd.$("#" + id).offset().top;
    }-*/;

    public static native int getTop(Element elt) /*-{
        return $wnd.$(elt).offset().top;
    }-*/;

    public static native void initializePage() /*-{
        $wnd.$.mobile.initializePage();
    }-*/;

    /**
     * Ask JQuery Mobile to "render" the child elements of the element with the given id.
     */
    public static void render(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("render() for empty id is not possible");
        }
        renderImpl(id);
    }

    public static void render(Element elt) {
        if (elt == null) {
            throw new IllegalArgumentException("render() for null element is not possible");
        }
        renderImpl(elt);
    }

    // page() was replaced by trigger("create"), see http://stackoverflow.com/a/6848969
    // trigger("create") was replaced by enhanceWithin(), see http://jquerymobile.com/upgrade-guide/1.4/#enhancewithin
    private static native void renderImpl(String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$("#" + id).enhanceWithin();
    }-*/;

    private static native void renderImpl(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$(elt).enhanceWithin();
    }-*/;

    public static void setDefaultTransition(TransitionIntf<?> dfltTransition) {
        defaultTransition = dfltTransition;
        setDefaultTransitionImpl(defaultTransition.getJqmValue());
    }

    /**
     * @param direction - if true then "reverse" effect will be used,
     *   see <a href="http://api.jquerymobile.com/pagecontainer/#method-change">Pagecontainer.change()</a>
     */
    public static void setDefaultTransition(TransitionIntf<?> dfltTransition, boolean direction) {
        setDefaultTransition(dfltTransition);
        defaultTransistionDirection = direction;
    }

    private static native void setDefaultTransitionImpl(String transition) /*-{
        $wnd.$.mobile.defaultPageTransition = transition;
    }-*/;

    public static void setDefaultDialogTransition(TransitionIntf<?> dfltDlgTransition) {
        defaultDialogTransition = dfltDlgTransition;
        setDefaultDialogTransitionImpl(defaultDialogTransition.getJqmValue());
    }

    private static native void setDefaultDialogTransitionImpl(String transition) /*-{
        $wnd.$.mobile.defaultDialogTransition = transition;
    }-*/;

    /**
     * @param maxWidth - Transitions can be disabled (set to "none") when the window
     * width is greater than a certain pixel width. This feature is useful because transitions
     * can be distracting or perform poorly on larger screens. This value is configurable via
     * the global option $.mobile.maxTransitionWidth, which defaults to false. The option accepts
     * any number representing a pixel width or false value. If it's not false, the handler will
     * use a "none" transition when the window is wider than the specified value.
     * <br> See <a href="http://view.jquerymobile.com/1.2.1/docs/pages/page-transitions.html">
     * Page transitions</a>
     */
    private static native void setMaxTransitionWidthImpl(String maxWidth) /*-{
        $wnd.$.mobile.maxTransitionWidth = maxWidth;
    }-*/;

    /**
     * @param maxWidth - (null or &lt;= 0) means transitions will be used regardless of current
     * window width, otherwise they will be disabled when window width &gt; maxWidth.
     */
    public static void setMaxTransitionWidth(Integer maxWidth) {
        if (maxWidth == null || maxWidth.intValue() <= 0) setMaxTransitionWidthImpl("false");
        else setMaxTransitionWidthImpl(maxWidth.toString());
    }

    public static void setDefaultChangeHash(boolean defaultChangeHash) {
        JQMContext.defaultChangeHash = defaultChangeHash;
    }

    /**
     * Scroll the page to the y-position of the given element. The element must
     * be attached to the DOM (obviously!).
     *
     * This method will not fire jquery mobile scroll events.
     */
    public static void silentScroll(Element e) {
        if (e.getId() != null)
            Mobile.silentScroll(getTop(e.getId()));
    }

    /**
     * Scroll the page to the y-position of the given widget. The widget must be
     * attached to the DOM (obviously!)
     *
     * This method will not fire jquery mobile scroll events.
     */
    public static void silentScroll(Widget widget) {
        silentScroll(widget.getElement());
    }

    public static final double MSEC_IN_MINUTE = 60d * 1000d;

    public static Date jsDateToDate(JsDate jsDate) {
        if (jsDate == null) return null;
        double msec = jsDate.getTime();
        Date d = new Date((long) msec);
        int o1 = jsDate.getTimezoneOffset();
        @SuppressWarnings("deprecation")
        int o2 = d.getTimezoneOffset();
        // For 1951 PST -> PDT was changed on Sun, Apr 29, 2:00 AM
        // But 04/04/1951 is PDT in JsDate (which is wrong) and PST in Java Date (which is right)
        if (o1 != o2) {
            msec += MSEC_IN_MINUTE * (o2 - o1);
            d = new Date((long) msec);
        }
        return d;
    }

    public static JsArrayString getJsArrayString(String... strings) {
        JsArrayString jsStrs = (JsArrayString) JavaScriptObject.createArray();
        for (String s : strings) {
            jsStrs.push(s);
        }
        return jsStrs;
    }

    /**
     * See <a href="http://stackoverflow.com/a/12629050">Read :hover pseudo class with javascript</a>
     * <br>
     * @param rule - substring for css rule search
     * @param props - requested property names
     * @param regexProps - regular expressions for requested properties, see the following links:
     *
     * <br> <a href="http://www.w3schools.com/jsref/jsref_obj_regexp.asp">JavaScript RegExp Object</a>
     * <br> <a href="http://www.regular-expressions.info/javascriptexample.html">Regex Tester</a>
     * <br> <a href="http://www.regular-expressions.info/anchors.html">Start of String and End of String Anchors</a>
     * <br>
     *
     * @return - property/value javascript object
     */
    public static native JavaScriptObject getCssForRule(String rule, JsArrayString props, JsArrayString regexProps) /*-{
        var sheets = $wnd.document.styleSheets;
        var slen = sheets.length;
        for (var i = 0; i < slen; i++) {
            var rules = $wnd.document.styleSheets[i].cssRules;
            if (rules === undefined || rules === null) { continue; }
            var rlen = rules.length;
            for (var j = 0; j < rlen; j++) {
                var selectorText = rules[j].selectorText;
                if (selectorText === undefined || selectorText === null) { continue; }
                if (selectorText.indexOf(rule) >= 0) {
                    var ret = {};
                    var styleDefs = rules[j].style;
                    for (var k = 0, dlen = styleDefs.length; k < dlen; k++) {
                        var def = styleDefs[k];
                        var found = props === null && regexProps === null;
                        if (props !== null) {
                            for (n = 0, plen = props.length; n < plen; n++) {
                                if (def === props[n]) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found && regexProps !== null) {
                            for (n = 0, plen = regexProps.length; n < plen; n++) {
                                if (def.match(regexProps[n])) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) { continue; }
                        ret[def] = styleDefs.getPropertyValue(def);
                    }
                    return ret;
                }
            }
        }
        return null;
    }-*/;

    public static double round(double valueToRound, int numberOfDecimalPlaces) {
        double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return Math.round(interestedInZeroDPs) / multipicationFactor;
    }

    public static WidgetDefaults getWidgetDefaults() {
        return widgetDefaults;
    }

    public static void setWidgetDefaults(WidgetDefaults widgetDefaults) {
        JQMContext.widgetDefaults = widgetDefaults;
    }

    public static void widgetDefaultsNotifyLoaded(Widget w) {
        if (w != null && widgetDefaults != null) widgetDefaults.loaded(w);
    }

}
