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
 * <p/>    The {@link JQMContext} provides methods that facilitate interaction
 *         between GWT, JQM and the DOM.
 *
 */
public class JQMContext {

    private static boolean defaultTransistionDirection = false;
    private static boolean defaultChangeHash = true;

    public static native void disableHashListening() /*-{
        $wnd.$.mobile.hashListeningEnabled = false;
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
        RootPanel p = RootPanel.get();
        if (p == null) return;
        p.add(container);
        enhance(container);
        container.setEnhanced(true);
    }

    /**
     * Programatically change the displayed page to the given {@link JQMPage}
     * instance. This uses the default transition which is Transition.POP
     */
    public static void changePage(JQMContainer container) {
        changePage(container, false/*dialog*/);
    }

    public static void changePage(JQMContainer container, boolean dialog) {
        changePage(container, dialog, getDefaultTransition());
    }

    /**
     * Change the displayed page to the given {@link JQMPage} instance using
     * the supplied transition.
     */
    public static void changePage(JQMContainer container, Transition transition) {
        changePage(container, false/*dialog*/, transition);
    }

    public static void changePage(JQMContainer container, boolean dialog, Transition transition) {
        changePage(container, dialog, transition, defaultTransistionDirection);
    }

    /**
     * Change the displayed page to the given {@link JQMPage} instance using
     * the supplied transition and reverse setting.
     */
    public static void changePage(JQMContainer container, Transition transition, boolean reverse) {
        changePage(container, false/*dialog*/, transition, reverse);
    }

    public static void changePage(JQMContainer container, boolean dialog, Transition transition,
                                  boolean reverse) {
        Mobile.changePage("#" + container.getId(), transition, reverse, defaultChangeHash, dialog);
    }

    private static void enhance(JQMContainer c) {
        render(c.getElement());
    }

    public static Transition getDefaultTransition() {
        String val = getDefaultTransitionImpl();
        Transition t = getTransitionForJQMString(val);
        return t != null ? t : Transition.FADE;
    }

    private static Transition getTransitionForJQMString(String val) {
        if (val != null) {
            for (Transition t : Transition.values()) {
                if (val.equalsIgnoreCase(t.getJqmValue())) {
                    return t;
                }
            }
        }
        return null;
    }

    private static native String getDefaultTransitionImpl() /*-{
        return $wnd.$.mobile.defaultPageTransition;
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

    public static void setDefaultTransition(Transition defaultTransition) {
        setDefaultTransitionImpl(defaultTransition.getJqmValue());
    }

    public static void setDefaultTransition(Transition defaultTransition,
            boolean direction) {
        setDefaultTransitionImpl(defaultTransition.getJqmValue());
        defaultTransistionDirection = direction;
    }

    private static native void setDefaultTransitionImpl(String transition) /*-{
        $wnd.$.mobile.defaultPageTransition = transition;
    }-*/;

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

    public static Date jsDateToDate(JsDate jsDate) {
        if (jsDate == null) return null;
        double msec = jsDate.getTime();
        return new Date((long) msec);
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
     * <p/>
     * @param rule - substring for css rule search
     * @param props - requested property names
     * @param regexProps - regular expressions for requested properties, see the following links:
     *
     * <p/> <a href="http://www.w3schools.com/jsref/jsref_obj_regexp.asp">JavaScript RegExp Object</a>
     * <p/> <a href="http://www.regular-expressions.info/javascriptexample.html">Regex Tester</a>
     * <p/> <a href="http://www.regular-expressions.info/anchors.html">Start of String and End of String Anchors</a>
     * <p/>
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

    /**
     * Example: jsObjToString(css, ":", ";") returns <b> color:red;background-color:yellow; </b>
     *
     * @param jsObj - property/value javascript object
     * @param propValueSeparator - will be used to separate property and value, i.e. aaa:33
     * @param propsSeparator - will be used to separate property/value pairs, i.e. aaa:33;bbb:44;
     * @return - string representation of property/value javascript object, i.e. aaa:33;bbb:44;
     */
    public static native String jsObjToString(JavaScriptObject jsObj, String propValueSeparator,
                                              String propsSeparator) /*-{
        var rslt = '';
        for (var prop in jsObj) {
            rslt += prop + propValueSeparator + jsObj[prop] + propsSeparator;
        }
        return rslt;
    }-*/;

    public static native JsArrayString getJsObjKeys(JavaScriptObject jsObj) /*-{
        var keys = [];
        for (var key in jsObj) {
            keys.push(key);
        }
        return keys;
    }-*/;

    public static native String getJsObjValue(JavaScriptObject jsObj, String key) /*-{
        return jsObj[key];
    }-*/;

    public static native void setJsObjValue(JavaScriptObject jsObj, String key, String value) /*-{
        jsObj[key] = value;
    }-*/;

    public static native void deleteJsObjProperty(JavaScriptObject jsObj, String key) /*-{
        delete jsObj[key];
    }-*/;

    public static native Integer getJsObjIntValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return v === undefined || v === null ? null : @java.lang.Integer::valueOf(I)(v);
    }-*/;

    public static native Double getJsObjDoubleValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return v === undefined || v === null ? null : @java.lang.Double::valueOf(D)(v);
    }-*/;

    public static native void setJsObjIntValue(JavaScriptObject jsObj, String key, int value) /*-{
        jsObj[key] = value;
    }-*/;

    public static native void setJsObjDoubleValue(JavaScriptObject jsObj, String key, double value) /*-{
        jsObj[key] = value;
    }-*/;

    public static double round(double valueToRound, int numberOfDecimalPlaces) {
        double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return Math.round(interestedInZeroDPs) / multipicationFactor;
    }

}
