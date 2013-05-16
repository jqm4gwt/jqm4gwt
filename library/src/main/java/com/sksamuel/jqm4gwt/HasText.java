package com.sksamuel.jqm4gwt;

/**
 * @author jraymond
 *         Date: 5/3/13
 *         Time: 10:28 AM
 */
public interface HasText<T> extends com.google.gwt.user.client.ui.HasText {
    T withText(String text);
}
