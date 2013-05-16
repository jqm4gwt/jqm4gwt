package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 10:36:07
 *         <p/>
 *         Interface for elements that are themeable.
 * @link http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/api/themes.html
 */
public interface HasTheme<T> {

    /**
     * Returns the value of the data-theme attribute
     */
    String getTheme();

    /**
     * Sets the value of the data-theme attribute. Should be a value definined
     * by the accompanying CSS stylesheet.
     * <p/>
     * JQM by default defines styles A-E. User styles will typically be
     * defined as F onwards.
     */
    void setTheme(String themeName);

    /**
     * Sets the value of the data-theme attribute. Should be a value definined
     * by the accompanying CSS stylesheet.
     * <p/>
     * JQM by default defines styles A-E. User styles will typically be
     * defined as F onwards.
     */
    T withTheme(String themeName);
}
