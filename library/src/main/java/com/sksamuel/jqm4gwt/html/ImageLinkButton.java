package com.sksamuel.jqm4gwt.html;

import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.button.JQMButton;

/**
 * Image with look and feel as button, to clearly indicate users on touch devices that it's clickable.
 * <p> See <a href="http://stackoverflow.com/a/16212448">jQuery Mobile: How to insert an image into button</a></p>
 * @author slavap
 *
 */
public class ImageLinkButton extends ImageLink implements HasInline<ImageLink>,
        HasCorners<ImageLinkButton>, HasTheme<ImageLinkButton>, HasMini<ImageLinkButton> {

    private static final String COMPACT_BTN = "jqm4gwt-compact-btn";

    @Override
    protected void initA() {
        super.initA();
        JQMButton.initEltAsButton(a);
        a.addClassName(COMPACT_BTN);
    }

    @Override
    public boolean isInline() {
        return JQMCommon.isInlineEx(this, JQMCommon.STYLE_UI_BTN_INLINE);
    }

    @Override
    public void setInline(boolean inline) {
        JQMCommon.setInlineEx(this, inline, JQMCommon.STYLE_UI_BTN_INLINE);
    }

    @Override
    public ImageLinkButton withInline(boolean inline) {
        setInline(inline);
        return this;
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCornersEx(this);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCornersEx(this, corners);
    }

    @Override
    public ImageLinkButton withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    @Override
    public String getTheme() {
        return JQMCommon.getThemeEx(this, JQMCommon.STYLE_UI_BTN,
                /*excludes:*/ JQMCommon.STYLE_UI_BTN_INLINE, JQMCommon.STYLE_UI_BTN_ICONPOS,
                JQMCommon.STYLE_UI_BTN_ACTIVE);
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setThemeEx(this, themeName, JQMCommon.STYLE_UI_BTN,
                /*excludes:*/ JQMCommon.STYLE_UI_BTN_INLINE, JQMCommon.STYLE_UI_BTN_ICONPOS,
                JQMCommon.STYLE_UI_BTN_ACTIVE);
    }

    @Override
    public ImageLinkButton withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMiniEx(this);
    }

    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMiniEx(this, mini);
    }

    @Override
    public ImageLinkButton withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    public boolean isCompact() {
        return JQMCommon.hasStyle(this, COMPACT_BTN);
    }

    /**
     * Button's size is very close to image + text size, i.e. minimal paddings around them are used.
     * <b>True</b> by default.
     */
    public void setCompact(boolean value) {
        if (value) getElement().addClassName(COMPACT_BTN);
        else getElement().removeClassName(COMPACT_BTN);
    }

    public boolean isShadow() {
        return JQMCommon.isShadowEx(this);
    }

    /** Button will have shadow if true */
    public void setShadow(boolean shadow) {
        JQMCommon.setShadowEx(this, shadow);
    }
}
