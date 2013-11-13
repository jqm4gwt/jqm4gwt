package com.sksamuel.jqm4gwt.html;

import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.JQMCommon;

/**
 * Image with look and feel as button, to clearly indicate users on touch devices that it's clickable.
 * <p> See <a href="http://stackoverflow.com/a/16212448">jQuery Mobile: How to insert an image into button</a></p>
 * @author slavap
 *
 */
public class ImageLinkButton extends ImageLink implements HasInline<ImageLink>,
        HasCorners<ImageLinkButton>, HasTheme<ImageLinkButton> {

    @Override
    protected void initA() {
        super.initA();
        JQMCommon.setDataRole(a, "button");
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
}
