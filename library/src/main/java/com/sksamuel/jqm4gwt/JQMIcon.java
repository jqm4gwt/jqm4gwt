package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.sksamuel.jqm4gwt.html.Div;

/** Simple jQuery Mobile icon, non-clickable - use JQMButton if you need any interaction. */
public class JQMIcon extends Div implements HasIcon<JQMIcon>, HasIconShadow<JQMIcon> {

    public JQMIcon() {
        addStyleName("jqm4gwt-icon");
        // See http://stackoverflow.com/a/20849113/714136
        getElement().getStyle().setPosition(Position.RELATIVE);
        setIconPos(IconPos.NOTEXT);
    }

    @Override
    public IconPos getIconPos() {
        return JQMCommon.getIconPosEx(this, JQMCommon.STYLE_UI_BTN_ICONPOS);
    }

    /**
     * Sets the position of the icon. Default is IconPos.NOTEXT
     */
    @Override
    public void setIconPos(IconPos pos) {
        JQMCommon.setIconPosEx(this, pos, JQMCommon.STYLE_UI_BTN_ICONPOS);
    }

    @Override
    public JQMIcon withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

    @Override
    public boolean isIconShadow() {
        return JQMCommon.isIconShadow(this);
    }

    @Override
    public void setIconShadow(boolean shadow) {
        JQMCommon.setIconShadow(this, shadow);
    }

    @Override
    public JQMIcon withIconShadow(boolean shadow) {
        setIconShadow(shadow);
        return this;
    }

    @Override
    public JQMIcon removeIcon() {
        JQMCommon.setIconEx(this, null);
        return this;
    }

    public boolean isIconNoDisc() {
        return JQMCommon.isIconNoDisc(this);
    }

    public void setIconNoDisc(boolean value) {
        JQMCommon.setIconNoDisc(this, value);
    }

    public boolean isIconAlt() {
        return JQMCommon.isIconAlt(this);
    }

    /**
     * @param value - if true "white vs. black" icon style will be used
     */
    public void setIconAlt(boolean value) {
        JQMCommon.setIconAlt(this, value);
    }

    @Override
    public void setBuiltInIcon(DataIcon icon) {
        JQMCommon.setIconEx(this, icon);
        JQMCommon.invalidateIconPosEx(getElement(), JQMCommon.STYLE_UI_BTN_ICONPOS);
    }

    @Override
    public void setIconURL(String src) {
        Element elt = getElement();
        JQMCommon.setIconEx(elt, src);
        JQMCommon.invalidateIconPosEx(elt, JQMCommon.STYLE_UI_BTN_ICONPOS);
    }

    public String getIconURL() {
        return JQMCommon.getIconExStr(getElement());
    }

    public void setCustomIcon(String icon) {
        setIconURL(icon);
    }

    public String getCustomIcon() {
        return getIconURL();
    }

    @Override
    public JQMIcon withBuiltInIcon(DataIcon icon) {
        setBuiltInIcon(icon);
        return this;
    }

    @Override
    public JQMIcon withIconURL(String src) {
        setIconURL(src);
        return this;
    }

}
