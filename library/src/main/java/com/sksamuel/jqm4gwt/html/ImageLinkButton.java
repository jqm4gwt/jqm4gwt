package com.sksamuel.jqm4gwt.html;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;

/**
 * Image with look and feel as button, to clearly indicate users on touch devices that it's clickable.
 * <p> See <a href="http://stackoverflow.com/a/16212448">jQuery Mobile: How to insert an image into button</a></p>
 * @author slavap
 *
 */
public class ImageLinkButton extends ImageLink implements HasCorners<ImageLinkButton>,
        HasInline<ImageLinkButton>, HasClickHandlers, HasTapHandlers, HasTheme<ImageLinkButton>, HasEnabled {

    @Override
    protected void initA() {
        super.initA();
        JQMCommon.setDataRole(a, "button");
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCorners(this);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCorners(this, corners);
    }

    @Override
    public ImageLinkButton withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    @Override
    public boolean isInline() {
        return JQMCommon.isInline(this);
    }

    @Override
    public void setInline(boolean inline) {
        JQMCommon.setInline(this, inline);
    }

    @Override
    public ImageLinkButton withInline(boolean inline) {
        setInline(inline);
        return this;
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
    public String getTheme() {
        return JQMCommon.getTheme(this);
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setTheme(this, themeName);
    }

    @Override
    public ImageLinkButton withTheme(String themeName) {
        setTheme(themeName);
        return this;
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
