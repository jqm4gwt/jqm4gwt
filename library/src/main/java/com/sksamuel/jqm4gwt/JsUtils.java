package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class JsUtils {

    private JsUtils() {} // static class

    /**
     * Example: jsObjToString(css, ":", ";") returns <b> color:red;background-color:yellow; </b>
     *
     * @param jsObj - property/value javascript object
     * @param propValueSeparator - will be used to separate property and value, i.e. aaa:33
     * @param propsSeparator - will be used to separate property/value pairs, i.e. aaa:33;bbb:44;
     * @return - string representation of property/value javascript object, i.e. aaa:33;bbb:44;
     */
    public static native String objToString(JavaScriptObject jsObj, String propValueSeparator,
                                            String propsSeparator) /*-{
        var rslt = '';
        for (var prop in jsObj) {
            rslt += prop + propValueSeparator + jsObj[prop] + propsSeparator;
        }
        return rslt;
    }-*/;

    public static native String stringify(JavaScriptObject jsObj) /*-{
        return JSON.stringify(jsObj);
    }-*/;

    public static native JavaScriptObject jsonParse(String json) /*-{
        return JSON.parse(json);
    }-*/;

    public static native JsArrayString getObjKeys(JavaScriptObject jsObj) /*-{
        var keys = [];
        for (var key in jsObj) {
            keys.push(key);
        }
        return keys;
    }-*/;

    public static native String getObjValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return v === undefined || v === null
               ? null : '' + v; // prevents: JS value of type number cannot be converted to String
    }-*/;

    public static native void setObjValue(JavaScriptObject jsObj, String key, String value) /*-{
        jsObj[key] = value;
    }-*/;

    public static native JavaScriptObject getNestedObjValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return v === undefined || v === null ? null : v;
    }-*/;

    public static native void setNestedObjValue(JavaScriptObject jsObj, String key,
                                                JavaScriptObject value) /*-{
        jsObj[key] = value;
    }-*/;

    public static native void deleteObjProperty(JavaScriptObject jsObj, String key) /*-{
        delete jsObj[key];
    }-*/;

    public static native Integer getObjIntValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return v === undefined || v === null ? null : @java.lang.Integer::valueOf(I)(v);
    }-*/;

    public static native Double getObjDoubleValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return v === undefined || v === null ? null : @java.lang.Double::valueOf(D)(v);
    }-*/;

    public static native void setObjIntValue(JavaScriptObject jsObj, String key, int value) /*-{
        jsObj[key] = value;
    }-*/;

    public static native void setObjDoubleValue(JavaScriptObject jsObj, String key, double value) /*-{
        jsObj[key] = value;
    }-*/;

    public static native void callFunc(JavaScriptObject jsFunc, JavaScriptObject arg0) /*-{
        jsFunc(arg0);
    }-*/;

    /**
     * See <a href="http://stackoverflow.com/a/22129960">Accessing nested JavaScript objects with string key</a>
     * <p>
     * <b> resolveChain('document.body.style.width') </b>
     * <br> or <b> resolveChain('style.width', document.body) </b>
     * <br> or even use array indexes (someObject has been defined in the question):
     * <br> <b> resolveChain('part3.0.size', someObject) </b>
     * <br> a safe flag makes Object.resolve return undefined when intermediate
     *      properties are undefined, rather than throwing a TypeError:
     * <br> <b> resolveChain('properties.that.do.not.exist', {hello:'world'}, true) </b>
     * </p>
     */
    public static native String getChainValStr(JavaScriptObject jsObj, String chainPath) /*-{
        if (!$wnd.resolveChain) {
            $wnd.resolveChain = function (path, obj, safe) {
                return path.split('.').reduce(function(prev, curr) {
                    return !safe ? prev[curr] : (prev ? prev[curr] : undefined)
                }, obj || self)
            }
        }
        var v = $wnd.resolveChain(chainPath, jsObj, true);
        return v === undefined || v === null
               ? null : '' + v; // prevents: JS value of type number cannot be converted to String
    }-*/;

}
