package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;

/**
 * @author Stephen K Samuel samspade79@gmail.com 13 May 2011 11:14:24
 * <br>
 *         Utility methods. The static methods in this class map through to the
 *         equivalent JQM method in $.mobile
 *
 */
public class Mobile {

    /**
     * See <a href="http://api.jquerymobile.com/pagecontainer/#method-change">Pagecontainer.change -&gt; options.role</a>
     * <br> The data-role value to be used when displaying the page.
     * By default this is undefined which means rely on the value of the data-role attribute defined on the element.
     * <br> For example: pagecontainer.change() with dialog option is not deprecated, and as result of
     * such call page's data-role will be set to "dialog" and cause conflict with explicitly
     * defined data-dialog property.
     */
    public static final String DATA_ROLE_DIALOG = "dialog";

    public static String pleaseWaitMsg = "Please Wait...";

    /** Standard loading indicator could be replaced by custom one,
     *  see <a href="http://demos.jquerymobile.com/1.4.5/loader/">Loader</a>
     **/
    public static String busyCustomHtml;

    public static String busyTheme;
    public static boolean busyTextOnly;

    private Mobile() {} // static class, should not be instantiated

    public static native void back() /*-{
        $wnd.$.mobile.back();
    }-*/;

    public static native Element getActivePage() /*-{
        var pageContainer = $wnd.$.mobile.pageContainer;
        var pg = pageContainer.pagecontainer("getActivePage");
        if (pg) return pg[0];
        else return null;
    }-*/;

    /**
     * Invokes the $.mobile.changePage method.
     *
     * @param _role - could be {@link Mobile#DATA_ROLE_DIALOG}
     */
    private static native void changePage(String url, String _transition, boolean _reverse,
                                          boolean _changeHash, String _role) /*-{
        var pageContainer = $wnd.$.mobile.pageContainer;
        if (_role !== null) {
            pageContainer.pagecontainer("change", url,
                    { transition: _transition,
                      reverse: _reverse,
                      changeHash: _changeHash,
                      role: _role
                    });
        } else {
            pageContainer.pagecontainer("change", url,
                    { transition: _transition,
                      reverse: _reverse,
                      changeHash: _changeHash
                    });
        }
    }-*/;

    /**
     * Invokes the $.mobile.changePage method
     */
    static void changePage(String url, TransitionIntf<?> t, boolean reverse, boolean changeHash,
                           boolean dialog) {
        changePage(url, t.getJqmValue(), reverse, changeHash, dialog ? DATA_ROLE_DIALOG : null);
    }

    static void changePage(String url, TransitionIntf<?> t, boolean reverse, boolean changeHash) {
        changePage(url, t, reverse, changeHash, false/*dialog*/);
    }

    /**
     * Hide the page loading dialog.
     */
    public static native void hideLoadingDialog() /*-{
        $wnd.$.mobile.loading('hide');
    }-*/;

    public static native void showLoadingDialog(String msg) /*-{
        $wnd.$.mobile.loading('show', { text: msg, textVisible: true });
    }-*/;

    public static native void showLoadingDialog(String msg, String customHtml, String theme, boolean textOnly) /*-{
        var opt = { text: msg, textVisible: true };
        if (customHtml) opt.html = customHtml;
        if (theme) opt.theme = theme;
        if (textOnly) opt.textonly = textOnly;

        $wnd.$.mobile.loading('show', opt);
    }-*/;

    /**
     * It's not enough for IE9, see <a href="http://stackoverflow.com/a/17852518">jQuery Mobile, disable all button when loading overlay is showed</a>
     * <p> So &lt;div class="ui-loader-background"&gt;&lt;/div&gt; must be added to html body. </p>
     */
    public static native void disableUI() /*-{
        $wnd.$('body').addClass('ui-state-disabled');
    }-*/;

    public static native void enableUI() /*-{
        $wnd.$('body').removeClass('ui-state-disabled');
    }-*/;

    /**
     * @param value - If true then completely blocks application interaction with user
     * and shows loading dialog (needed in case of long processing).
     * <p> When false - unblocks application.
     */
    public static void busy(boolean value) {
        if (value) {
            disableUI();
            showLoadingDialog(pleaseWaitMsg, busyCustomHtml, busyTheme, busyTextOnly);
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
     * Scroll to a particular Y position without triggering scroll event listeners.
     *
     * @param y - Pass any number to scroll to that Y location.
     */
    public static native void silentScroll(int y) /*-{
        $wnd.$.mobile.silentScroll(y);
    }-*/;

    /**
     * jQuery based check if this element is visible, much more correct than GWT default implementation.
     * <br> Expensive, but gives realistic visibility (CSS rules, parent chain considered,
     * width and height are explicitly set to 0, ...)
     */
    public static native boolean isVisible(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return false; // jQuery is not loaded
        return $wnd.$(elt).is(':visible');
    }-*/;

    public static native boolean isHidden(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return true; // jQuery is not loaded
        return $wnd.$(elt).is(':hidden');
    }-*/;

    /**
     * For some widgets it could be beneficial to prevent jqm's active state removal on each click,
     * i.e. $.mobile.activeClickedLink.removeClass($.mobile.activeBtnClass) should not be executed.
     */
    public static native void clearActiveClickedLink() /*-{
        $wnd.$.mobile.activeClickedLink = null;
    }-*/;

}
