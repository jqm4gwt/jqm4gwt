package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
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
    private static final String STYLE_UI_BTN_ACTIVE = "ui-btn-active";

    private static final String STYLE_UI_ICON_NODISC = "ui-icon-nodisc";
    private static final String STYLE_UI_ICON_ALT = "ui-icon-alt";

    private static final String DATA_ROLE = "data-role";
    private static final String DATA_THEME = "data-theme";
    private static final String DATA_INLINE = "data-inline";
    private static final String DATA_CORNERS = "data-corners";
    private static final String DATA_ICON = "data-icon";
    private static final String DATA_ICONPOS = "data-iconpos";
    private static final String DATA_ICONSHADOW = "data-iconshadow";
    private static final String DATA_CLEAR_BTN = "data-clear-btn";
    private static final String DATA_MINI = "data-mini";
    private static final String DATA_POPUP_POSITION = "data-position-to";

    public static boolean isVisible(Widget widget) {
        return widget != null && Mobile.isVisible(widget.getElement());
    }

    public static boolean isHidden(Widget widget) {
        return widget != null && Mobile.isHidden(widget.getElement());
    }

    /**
     * Exact copy of com.google.gwt.dom.client.Element.indexOfName()
     * <p> Returns the index of the first occurrence of name in a space-separated list of names,
     * or -1 if not found. </p>
     *
     * @param nameList list of space delimited names
     * @param name a non-empty string.  Should be already trimmed.
     */
    public static int indexOfName(String nameList, String name) {
        int idx = nameList.indexOf(name);
        // Calculate matching index.
        while (idx != -1) {
            if (idx == 0 || nameList.charAt(idx - 1) == ' ') {
                int last = idx + name.length();
                int lastPos = nameList.length();
                if ((last == lastPos) || ((last < lastPos) && (nameList.charAt(last) == ' '))) {
                    break;
                }
            }
            idx = nameList.indexOf(name, idx + 1);
        }
        return idx;
    }

    /**
     * @return - first child Element with specified className
     */
    public static Element findChild(Element elt, String className) {
        if (elt == null) return null;
        if (JQMCommon.hasStyle(elt, className)) return elt;
        NodeList<Node> children = elt.getChildNodes();
        if (children != null && children.getLength() > 0) {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.getItem(i);
                if (child instanceof Element) {
                    Element e = (Element) child;
                    e = findChild(e, className);
                    if (e != null) return e;
                }
            }
        }
        return null;
    }

    /**
     * Moves all children of "from" element onto "to" element.
     */
    public static void moveChildren(Element from, Element to) {
        if (from == null || to == null || from == to) return;
        for (int k = from.getChildCount() - 1; k >= 0; k--) {
            Node node = from.getChild(k);
            from.removeChild(node);
            to.insertFirst(node);
        }
    }

    public static boolean hasStyle(Widget widget, String style) {
        if (widget == null) return false;
        String styles = widget.getStyleName();
        return styles != null && (indexOfName(styles, style) >= 0);
    }

    public static boolean hasStyle(Element elt, String style) {
        if (elt == null) return false;
        String styles = DOM.getElementProperty(elt.<com.google.gwt.user.client.Element> cast(),
                                               "className");
        return styles != null && (indexOfName(styles, style) >= 0);
    }

    public static void removeStylesStartsWith(Element elt, String startsWith) {
        if (elt == null || startsWith == null || startsWith.isEmpty()) return;
        String styles = DOM.getElementProperty(elt.<com.google.gwt.user.client.Element> cast(),
                                               "className");
        if (styles == null || styles.isEmpty()) return;
        boolean changed = false;
        int p = 0;
        do {
            p = styles.indexOf(startsWith, p);
            if (p == -1) break;
            if (p == 0 || p > 0 && styles.charAt(p - 1) == ' ') {
                int j = p + startsWith.length();
                while (j < styles.length()) {
                    if (styles.charAt(j) == ' ') break;
                    j++;
                }
                if (p > 0) styles = styles.substring(0, p - 1) + styles.substring(j); // skip leading space
                else styles = styles.substring(j).trim();
                changed = true;
            } else {
                p += startsWith.length();
            }
        } while (true);

        if (changed) {
            DOM.setElementProperty(elt.<com.google.gwt.user.client.Element> cast(),
                                   "className", styles);
        }
    }

    public static String getStyleStartsWith(Element elt, String startsWith) {
        if (elt == null || startsWith == null || startsWith.isEmpty()) return null;
        String styles = DOM.getElementProperty(elt.<com.google.gwt.user.client.Element> cast(),
                                               "className");
        if (styles == null || styles.isEmpty()) return null;
        int p = 0;
        do {
            p = styles.indexOf(startsWith, p);
            if (p == -1) break;
            if (p == 0 || p > 0 && styles.charAt(p - 1) == ' ') {
                int j = p + startsWith.length();
                while (j < styles.length()) {
                    if (styles.charAt(j) == ' ') break;
                    j++;
                }
                return styles.substring(p, j);
            } else {
                p += startsWith.length();
            }
        } while (true);

        return null;
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

    public static boolean isIconAlt(Widget widget) {
        return hasStyle(widget, STYLE_UI_ICON_ALT);
    }

    public static void setIconAlt(Widget widget, boolean value) {
        if (isIconAlt(widget) != value) {
            if (value) widget.addStyleName(STYLE_UI_ICON_ALT);
            else widget.removeStyleName(STYLE_UI_ICON_ALT);
        }
    }

    public static boolean isIconNoDisc(Widget widget) {
        return hasStyle(widget, STYLE_UI_ICON_NODISC);
    }

    public static void setIconNoDisc(Widget widget, boolean value) {
        if (isIconNoDisc(widget) != value) {
            if (value) widget.addStyleName(STYLE_UI_ICON_NODISC);
            else widget.removeStyleName(STYLE_UI_ICON_NODISC);
        }
    }

    public static boolean isBtnActive(Widget widget) {
        return hasStyle(widget, STYLE_UI_BTN_ACTIVE);
    }

    public static void setBtnActive(Widget widget, boolean value) {
        if (isBtnActive(widget) != value) {
            if (value) widget.addStyleName(STYLE_UI_BTN_ACTIVE);
            else widget.removeStyleName(STYLE_UI_BTN_ACTIVE);
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

    public static DataIcon getIcon(Element elt) {
        return DataIcon.fromJqmValue(getAttribute(elt, DATA_ICON));
    }

    public static DataIcon getIcon(Widget widget) {
        return getIcon(widget.getElement());
    }

    public static void setIcon(Element elt, DataIcon icon) {
        setAttribute(elt, DATA_ICON, icon != null ? icon.getJqmValue() : null);
    }

    public static void setIcon(Widget widget, DataIcon icon) {
        setIcon(widget.getElement(), icon);
    }

    public static IconPos getIconPos(Element elt) {
        return IconPos.fromJqmValue(getAttribute(elt, DATA_ICONPOS));
    }

    public static IconPos getIconPos(Widget widget) {
        return getIconPos(widget.getElement());
    }

    public static void setIconPos(Element elt, IconPos iconPos) {
        setAttribute(elt, DATA_ICONPOS, iconPos != null ? iconPos.getJqmValue() : null);
    }

    public static void setIconPos(Widget widget, IconPos iconPos) {
        setIconPos(widget.getElement(), iconPos);
    }

    public static boolean isIconShadow(Element elt) {
        return !"false".equals(getAttribute(elt, DATA_ICONSHADOW));
    }

    public static boolean isIconShadow(Widget widget) {
        return isIconShadow(widget.getElement());
    }

    public static void setIconShadow(Element elt, boolean value) {
        setAttribute(elt, DATA_ICONSHADOW, value ? null : "false");
    }

    public static void setIconShadow(Widget widget, boolean value) {
        setIconShadow(widget.getElement(), value);
    }

    public static boolean isClearButton(Element elt) {
        return "true".equals(getAttribute(elt, DATA_CLEAR_BTN));
    }

    public static boolean isClearButton(Widget widget) {
        return isClearButton(widget.getElement());
    }

    public static void setClearButton(Element elt, boolean value) {
        if (value) setAttribute(elt, DATA_CLEAR_BTN, "true");
        else removeAttribute(elt, DATA_CLEAR_BTN);
    }

    public static void setClearButton(Widget widget, boolean value) {
        setClearButton(widget.getElement(), value);
    }

    public static boolean isMini(Element elt) {
        return "true".equals(getAttribute(elt, DATA_MINI));
    }

    public static boolean isMini(Widget widget) {
        return isMini(widget.getElement());
    }

    public static void setMini(Element elt, boolean mini) {
        if (mini) setAttribute(elt, DATA_MINI, "true");
        else removeAttribute(elt, DATA_MINI);
    }

    public static void setMini(Widget widget, boolean mini) {
        setMini(widget.getElement(), mini);
    }

    public static String getPopupPos(Element elt) {
        return getAttribute(elt, DATA_POPUP_POSITION);
    }

    public static String getPopupPos(Widget widget) {
        return getPopupPos(widget.getElement());
    }

    public static void setPopupPos(Element elt, String value) {
        if (value == null) {
            setAttribute(elt, DATA_POPUP_POSITION, null);
            return;
        }
        if (!value.startsWith("#") && !value.equals("window") && !value.equals("origin")) {
            throw new IllegalArgumentException("Popup position must be origin, window, or an id selector");
        }
        setAttribute(elt, DATA_POPUP_POSITION, value);
    }

    public static void setPopupPos(Widget widget, String value) {
        setPopupPos(widget.getElement(), value);
    }

}
