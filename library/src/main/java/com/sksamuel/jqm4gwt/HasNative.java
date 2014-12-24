package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 10:59:06
 * <br>
 * Interface for classes that enable switching between native and jqm rendering modes.
 */
public interface HasNative<T> {

    boolean isNative();

    void setNative(boolean b);

    T withNative(boolean b);
}
