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

    public static native JsArrayString getObjKeys(JavaScriptObject jsObj) /*-{
        var keys = [];
        for (var key in jsObj) {
            keys.push(key);
        }
        return keys;
    }-*/;

    public static native String getObjValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return '' + v; // prevents: JS value of type number cannot be converted to String
    }-*/;

    public static native void setObjValue(JavaScriptObject jsObj, String key, String value) /*-{
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


}
