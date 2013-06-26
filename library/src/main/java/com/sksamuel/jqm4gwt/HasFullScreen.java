package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 23:20:01
 */
public interface HasFullScreen<T> {

    /**
     * @return true if this page is set to render in full screen mode
     */
    boolean isFullScreen();

    /**
     * Sets this page to render in full screen.
     *
     * @param fs if true then the page will render in full screen, if false
     *           then in normal mode
     */
    void setFullScreen(boolean fs);


    /**
     * Sets this page to render in full screen.
     *
     * @param fs if true then the page will render in full screen, if false
     *           then in normal mode
     */
    T withFullScreen(boolean fs);
}
