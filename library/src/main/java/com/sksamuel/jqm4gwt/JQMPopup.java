package com.sksamuel.jqm4gwt;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 16 Sep 2012 00:26:35
 */
public class JQMPopup extends JQMContainer {

    private static int counter = 1;

    /**
     * Creates a new {@link JQMPopup} with an automatically assigned id in the form popup-xx where XX is an incrementing number.
     */
    public JQMPopup() {
        this("popup-" + (counter++));
    }

    /**
     * Creates a new {@link JQMPopup} with explicitly defined id.
     */
    public JQMPopup(String popupId) {
        super(popupId, "popup");
    }

    public JQMPopup(Widget... widgets) {
        this();
        add(widgets);
    }

    @Override
    public void setContainerId(String containerId) {
        super.setContainerId(containerId);
        removeAttribute("data-url");
    }

    /**
     * Automatically assigns an id in the form popup-xx where XX is an incrementing number.
     */
    @Override
    public JQMContainer withContainerId() {
        setContainerId("popup-" + (counter++));
        return this;
    }

    private native void _close(String id) /*-{
        $wnd.$('#' + id).popup("close")
    }-*/;

    private native void _open(String id) /*-{
        $wnd.$('#' + id).popup("open")
    }-*/;

    public JQMPopup close() {
        _close(id);
        return this;
    }

    @Override
    public String getRelType() {
        return "popup";
    }

    public JQMPopup open() {
        _open(id);
        return this;
    }

    public String getOverlayTheme() {
        return getAttribute("data-overlay-theme");
    }

    public void setOverlayTheme(String theme) {
        setAttribute("data-overlay-theme", theme);
    }

    public JQMPopup withOverlayTheme(String theme) {
        setOverlayTheme(theme);
        return this;
    }

    public boolean isPadding() {
        return JQMCommon.hasStyle(this, "ui-content");
    }

    public void setPadding(boolean padding) {
        if (padding) {
            addStyleName("ui-content");
        } else {
            removeStyleName("ui-content");
        }
    }

    public JQMPopup withPadding(boolean padding) {
        setPadding(padding);
        return this;
    }

    public String getPosition() {
        return JQMCommon.getPopupPos(this);
    }

    public void setPosition(String pos) {
        JQMCommon.setPopupPos(this, pos);
    }

    @Override
    public String toString() {
        return "JQMPopup [id=" + id + "]";
    }

    public boolean isDialog() {
        String s = JQMCommon.getAttribute(this, "data-dismissible");
        return "false".equals(s);
    }

    /**
     * @param value - if true creates a modal style dialog, to prevent the click-outside-to-close
     * behavior so people need to interact with popup buttons to close it.
     */
    public void setDialog(boolean value) {
        JQMCommon.setAttribute(this, "data-dismissible", value ? "false" : null);
    }
}
