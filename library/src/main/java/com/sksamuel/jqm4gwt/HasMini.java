package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 6 Sep 2012 00:44:56
 * 
 * For widgets that can be rendered in normal or mini mode
 *
 */
public interface HasMini<T> {

	boolean isMini();

    void setMini(boolean mini);

    T withMini(boolean mini);
}
