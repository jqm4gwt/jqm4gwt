package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.Orientation;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;

/**
 * @author Stephen K Samuel samspade79@gmail.com 17 Jul 2011 15:38:47
 * <p/>
 *         An implementation of an anchor tag that wraps an image tag.
 *         <pre> &lt;a href='mylink'>&lt;img src='myimage'/>&lt;/a> </pre>
 *
 */
public class ImageLink extends Widget implements HasClickHandlers, HasTapHandlers, HasEnabled {

    protected static final String JQM4GWT_IMAGE_LINK_A = "jqm4gwt-image-link-a";
    protected static final String JQM4GWT_IMAGE_LINK_IMG = "jqm4gwt-image-link-img";
    protected static final String DATA_RESIZE_PRIORITY = "data-resize-priority";

    protected ImageElement img;
    protected AnchorElement a;

    public ImageLink() {
        a = Document.get().createAnchorElement();
        setElement(a);
        initA();
        img = Document.get().createImageElement();
        initImg();
        a.appendChild(img);
    }

    protected void initA() {
        JQMCommon.setDataRole(a, "none");
        a.setAttribute("rel", "external");
        a.setAttribute("data-rel", "external");
        setStyleName(a, JQM4GWT_IMAGE_LINK_A);
    }

    protected void initImg() {
        setStyleName(img, JQM4GWT_IMAGE_LINK_IMG);
        setImageResizePriority(Orientation.HORIZONTAL);
    }

    public ImageLink(String href, String src) {
        this();
        setHref(href);
        setSrc(src);
    }

    public String getSrc() {
        return img.getAttribute("src");
    }

    public String getHref() {
        return a.getAttribute("href");
    }

    /**
     * The URL of the source image
     *
     * @param src
     */
    public void setSrc(String src) {
        img.setAttribute("src", src);
    }

    /**
     * The destination URL of the link
     *
     * @param href
     */
    public void setHref(String href) {
        a.setAttribute("href", href);
    }

    /**
     * Set the width of the image
     */
    @Override
    public void setWidth(String width) {
        img.getStyle().setProperty("width", width);
    }

    /**
     * Set the height of the image
     */
    @Override
    public void setHeight(String height) {
        img.getStyle().setProperty("height", height);
    }

    public Orientation getImageResizePriority() {
        String s = img.getAttribute(DATA_RESIZE_PRIORITY);
        if (s == null || s.isEmpty()) return null;
        if (Orientation.HORIZONTAL.getJqmValue().equals(s)) return Orientation.HORIZONTAL;
        if (Orientation.VERTICAL.getJqmValue().equals(s)) return Orientation.VERTICAL;
        return null;
    }

    /**
     * Defines how to resize image in case of limited/explicit button size.
     * <p> HORIZONTAL - width is 100% and height is auto (default mode). </p>
     * <p> VERTICAL - height is 100% and width is auto. </p>
     * <p> See <a href="http://stackoverflow.com/a/16217391">Flexible images</a></p>
     */
    public void setImageResizePriority(Orientation value) {
        if (value == null) img.removeAttribute(DATA_RESIZE_PRIORITY);
        else img.setAttribute(DATA_RESIZE_PRIORITY, value.getJqmValue());
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addTapHandler(TapHandler handler) {
        // this is not a native browser event so we will have to manage it via JS
        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, JQMComponentEvents.TAP_EVENT, TapEvent.getType());
    }

    @Override
    public boolean isEnabled() {
        return JQMCommon.isEnabled(this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        JQMCommon.setEnabled(this, enabled);
    }
}
