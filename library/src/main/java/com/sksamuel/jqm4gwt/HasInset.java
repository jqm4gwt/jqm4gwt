package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 13:29:34
 *         <br>
 *         Interface for elements or groups that can be set to inset mode. What
 *         inset mode does is dependant on the actual implementing element.
 */
public interface HasInset<T> {

    /**
     * Returns true if this widget is set to inset mode
     */
    boolean isInset();

    /**
     * Sets the widget to inset mode or not.
     *
     * @param inset if true then this widget is set to inset, if false then
     *              inset mode is disabled
     */
    void setInset(boolean inset);

    /**
     * Sets the widget to inset mode or not.
     *
     * @param inset if true then this widget is set to inset, if false then
     *              inset mode is disabled
     */
    T withInset(boolean inset);

}
