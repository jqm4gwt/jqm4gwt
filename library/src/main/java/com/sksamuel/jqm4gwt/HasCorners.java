package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 6 Sep 2012 00:41:16
 */
public interface HasCorners<T> {

    boolean isCorners();

    void setCorners(boolean corners);

    T withCorners(boolean corners);
}
