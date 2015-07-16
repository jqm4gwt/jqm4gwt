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

    public static native Object getNestedObjJavaValue(JavaScriptObject jsObj, String key) /*-{
        var v = jsObj[key];
        return v === undefined || v === null ? null : v;
    }-*/;

    /** JavaScriptObject can hold reference to real Java object and even call its methods and fields. */
    public static native void setNestedObjJavaValue(JavaScriptObject jsObj, String key,
                                                    Object value) /*-{
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

    /**
     * See <a href="http://stackoverflow.com/a/22429679">Generate a Hash from string in JavaScript</a>
     * <br>
     * <br> Calculate a 32 bit FNV-1a hash
     * <br> Found here: https://gist.github.com/vaiorabbit/5657561
     * <br> Ref.: http://isthe.com/chongo/tech/comp/fnv/
     *
     * @param seed - actually must be in range of uint32.
     */
    public static native String hashFnv32a(String str, double seed) /*-{
        var i, l, hval = seed;

        for (i = 0, l = str.length; i < l; i++) {
            hval ^= str.charCodeAt(i);
            hval += (hval << 1) + (hval << 4) + (hval << 7) + (hval << 8) + (hval << 24);
        }

        // Convert to 8 digit hex string, if uint32 is needed you can use: return hval >>> 0;
        return ("0000000" + (hval >>> 0).toString(16)).substr(-8);
    }-*/;

    public static native String hashFnv32a(String str) /*-{
        return @com.sksamuel.jqm4gwt.JsUtils::hashFnv32a(Ljava/lang/String;D)(str, 0x811c9dc5);
    }-*/;

}
