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

    public JQMPopup(String id) {
        super(id, "popup");
        getElement().setId(getId());
        removeAttribute("data-url");
    }

    public JQMPopup(Widget... widgets) {
        this();
        add(widgets);
    }

    /**
     * Automatically assigns an id in the form popup-xx where XX is an incrementing number.
     */
    @Override
    public JQMContainer withContainerId() {
        super.setContainerId("popup-" + (counter++));
        return this;
    }

    private native void _close(String id) /*-{
                                $('#' + id).popup("close")
								}-*/;

    private native void _open(String id) /*-{
                                $('#' + id).popup("open")
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

    public JQMPopup setOverlayTheme(String theme) {
        setAttribute("data-overlay-theme", theme);
        return this;
    }

    public JQMPopup setPadding(boolean padding) {
        if (padding) {
            addStyleName("ui-content");
        } else {
            removeStyleName("ui-content");
        }
        return this;
    }

    public void setPosition(String pos) {
        if (!pos.startsWith("#") && !pos.equals("window") && !pos.equals("origin"))
            throw new IllegalArgumentException("Position must be origin, window, or an id selector");
        setAttribute("data-position-to", pos);
    }

    @Override
    public String toString() {
        return "JQMPopup [id=" + id + "]";
    }

}
