package com.sksamuel.jqm4gwt;

/**
 * See <a href="http://www.whatwg.org/specs/web-apps/current-work/multipage/elements.html#phrasing-content">
 * Phrasing content</a>
 */
public interface HasHTML<T> extends com.google.gwt.user.client.ui.HasHTML {
    T withHTML(String html);
}
