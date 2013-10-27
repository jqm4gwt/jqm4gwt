package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Misc helper functionality for supporting development of jqm widgets.
 * @author slavap
 *
 */
public class JQMCommon {

    private JQMCommon() {} // static class, not supposed to be instantiated

    private static final String STYLE_UI_DISABLED = "ui-disabled";
    private static final String STYLE_UI_HIDE_LABEL = "ui-hide-label";

    private static final String DATA_ROLE = "data-role";
    private static final String DATA_THEME = "data-theme";
    private static final String DATA_INLINE = "data-inline";
    private static final String DATA_CORNERS = "data-corners";
    private static final String DATA_ICONPOS = "data-iconpos";

    public static boolean isVisible(Widget widget) {
        return widget != null && Mobile.isVisible(widget.getElement());
    }

    public static boolean isHidden(Widget widget) {
        return widget != null && Mobile.isHidden(widget.getElement());
    }

    public static boolean hasStyle(Widget widget, String style) {
        String styles = widget.getStyleName();
        return styles != null && styles.contains(style);
    }

    public static boolean isEnabled(Widget widget) {
        return !hasStyle(widget, STYLE_UI_DISABLED);
    }

    public static void setEnabled(Widget widget, boolean value) {
        if (isEnabled(widget) != value) {
            if (value) widget.removeStyleName(STYLE_UI_DISABLED);
            else widget.addStyleName(STYLE_UI_DISABLED);
        }
    }

    public static boolean isLabelHidden(Widget widget) {
        return hasStyle(widget, STYLE_UI_HIDE_LABEL);
    }

    public static void setLabelHidden(Widget widget, boolean value) {
        if (isLabelHidden(widget) != value) {
            if (value) widget.addStyleName(STYLE_UI_HIDE_LABEL);
            else widget.removeStyleName(STYLE_UI_HIDE_LABEL);
        }
    }

    public static String getAttribute(Element elt, String name) {
        return elt.getAttribute(name);
    }

    public static String getAttribute(Widget widget, String name) {
        return getAttribute(widget.getElement(), name);
    }

    public static void setAttribute(Element elt, String name, String value) {
        if (value == null) elt.removeAttribute(name);
        else elt.setAttribute(name, value);
    }

    public static void setAttribute(Widget widget, String name, String value) {
        setAttribute(widget.getElement(), name, value);
    }

    public static void removeAttribute(Element elt, String name) {
        setAttribute(elt, name, null);
    }

    public static void removeAttribute(Widget widget, String name) {
        setAttribute(widget.getElement(), name, null);
    }

    public static String getDataRole(Element elt) {
        return getAttribute(elt, DATA_ROLE);
    }

    public static String getDataRole(Widget widget) {
        return getDataRole(widget.getElement());
    }

    public static void setDataRole(Element elt, String value) {
        setAttribute(elt, DATA_ROLE, value);
    }

    public static void setDataRole(Widget widget, String value) {
        setDataRole(widget.getElement(), value);
    }

    public static void removeDataRole(Widget widget) {
        removeAttribute(widget, DATA_ROLE);
    }

    public static String getTheme(Element elt) {
        return getAttribute(elt, DATA_THEME);
    }

    public static String getTheme(Widget widget) {
        return getTheme(widget.getElement());
    }

    public static void setTheme(Element elt, String themeName) {
        setAttribute(elt, DATA_THEME, themeName);
    }

    public static void setTheme(Widget widget, String themeName) {
        setTheme(widget.getElement(), themeName);
    }

    public static void applyTheme(Widget widget, String themeName) {
        setTheme(widget, themeName);
    }

    public static boolean isInline(Element elt) {
        return "true".equals(getAttribute(elt, DATA_INLINE));
    }

    public static boolean isInline(Widget widget) {
        return isInline(widget.getElement());
    }

    public static void setInline(Element elt, boolean inline) {
        if (inline) setAttribute(elt, DATA_INLINE, "true");
        else removeAttribute(elt, DATA_INLINE);
    }

    public static void setInline(Widget widget, boolean inline) {
        setInline(widget.getElement(), inline);
    }

    public static boolean isCorners(Element elt) {
        return "true".equals(getAttribute(elt, DATA_CORNERS));
    }

    public static boolean isCorners(Widget widget) {
        return isCorners(widget.getElement());
    }

    public static void setCorners(Element elt, boolean corners) {
        setAttribute(elt, DATA_CORNERS, String.valueOf(corners));
    }

    public static void setCorners(Widget widget, boolean corners) {
        setCorners(widget.getElement(), corners);
    }

    public static IconPos getIconPos(Element elt) {
        return IconPos.fromJqmValue(getAttribute(elt, DATA_ICONPOS));
    }

    public static IconPos getIconPos(Widget widget) {
        return getIconPos(widget.getElement());
    }

    public static void setIconPos(Element elt, IconPos iconPos) {
        setAttribute(elt, DATA_ICONPOS, iconPos.getJqmValue());
    }

    public static void setIconPos(Widget widget, IconPos iconPos) {
        setIconPos(widget.getElement(), iconPos);
    }

}
