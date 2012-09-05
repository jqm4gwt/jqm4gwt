package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 10:59:06
 * 
 *         Interface for classes that enable switching between native and jqm
 *         rendering modes.
 * 
 */
public interface HasNative {

	boolean isNative();

	void setNative(boolean b);
}
