package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;

/**
 * @author Stephen K Samuel samspade79@gmail.com 13 May 2011 11:14:24
 * <p/>
 *         Utility methods. The static methods in this class map through to the
 *         equivilent JQM method in $.mobile
 *
 */
public class Mobile {

    public static String pleaseWaitMsg = "Please Wait...";

    private Mobile() {} // static class, should not be instantiated

    /**
     * Invokes the $.mobile.changePage method
     */
    private static native void changePage(String url, String t, boolean r, boolean ch, String rol) /*-{
        if (rol != null) {
            $wnd.$.mobile.changePage(url, { transition: t, reverse: r, changeHash: ch, role: rol });
        } else {
            $wnd.$.mobile.changePage(url, { transition: t, reverse: r, changeHash: ch });
        }
    }-*/;

    /**
     * Invokes the $.mobile.changePage method
     */
    static void changePage(String url, Transition t, boolean reverse, boolean changeHash, boolean dialog) {
        changePage(url, t.getJQMValue(), reverse, changeHash, dialog ? "dialog" : null);
    }

    static void changePage(String url, Transition t, boolean reverse, boolean changeHash) {
        changePage(url, t, reverse, changeHash, false/*dialog*/);
    }

    /**
     * Hide the page loading dialog.
     */
    public static native void hideLoadingDialog() /*-{
        $wnd.$.mobile.loading('hide');
    }-*/;

    /**
     * It's not enough for IE9, see <a href="http://stackoverflow.com/a/17852518">jQuery Mobile, disable all button when loading overlay is showed</a>
     * <p> So &lt;div class="ui-loader-background">&lt;/div> must be added to html body. </p>
     */
    public static native void disableUI() /*-{
        $wnd.$('body').addClass('ui-disabled');
    }-*/;

    public static native void enableUI() /*-{
        $wnd.$('body').removeClass('ui-disabled');
    }-*/;

    /**
     * @param value - If true then completely blocks application interaction with user
     * and shows loading dialog (needed in case of long processing).
     * <p> When false - unblocks application.
     */
    public static void busy(boolean value) {
        if (value) {
            disableUI();
            showLoadingDialog(pleaseWaitMsg);
        } else {
            hideLoadingDialog();
            enableUI();
        }
    }

    /**
     * Ask JQuery Mobile to "render" the element with the given id.
     */
    public static void render(String id) {
        JQMContext.render(id);
    }

    /**
     *
     */
    public static native void showLoadingDialog(String msg) /*-{
        $wnd.$.mobile.loading('show', {text: msg, textVisible: true});
    }-*/;

    /**
     * Scroll to a particular Y position without triggering scroll event
     * listeners.
     *
     * @param y
     *              Pass any number to scroll to that Y location.
     */
    public static native void silentScroll(int y) /*-{
        $wnd.$.mobile.silentScroll(y);
    }-*/;

    /**
     * jQuery based check if this element is visible, much more correct than GWT default implementation.
     */
    public static native boolean isVisible(Element elt) /*-{
        return $wnd.$(elt).is(':visible');
    }-*/;

    public static native boolean isHidden(Element elt) /*-{
        return $wnd.$(elt).is(':hidden');
    }-*/;

}
