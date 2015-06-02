package com.sksamuel.jqm4gwt;

/**
 * Needed to simplify custom transitions support.
 * <br> See <a href="http://demos.jquerymobile.com/1.2.1/docs/pages/page-customtransitions.html">
 *      Creating custom CSS-based transitions</a>
 */
public interface TransitionIntf<T extends TransitionIntf<T>> {

    /** Returns the string value that JQM expects */
    String getJqmValue();

    /** Converts from JQM string value to actual class value */
    T parseJqmValue(String jqmValue);
}
