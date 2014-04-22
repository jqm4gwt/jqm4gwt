package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Misc helper functionality for supporting development of jqm widgets.
 * @author slavap
 *
 */
public class JQMCommon {

    private JQMCommon() {} // static class, not supposed to be instantiated

    public static final String STYLE_UI_BTN = "ui-btn-";
    public static final String STYLE_UI_BTN_INLINE = "ui-btn-inline";
    public static final String STYLE_UI_BTN_ICONPOS = "ui-btn-icon-";

    private static final String STYLE_UI_DISABLED = "ui-state-disabled";

    // TODO: Deprecated in 1.4, quite serious refactoring is needed to switch to ui-hidden-accessible
    // See https://github.com/jquery/jquery-mobile/issues/6405
    private static final String STYLE_UI_HIDE_LABEL = "ui-hide-label";

    private static final String STYLE_UI_BTN_ACTIVE = "ui-btn-active";
    private static final String STYLE_UI_SHADOW = "ui-shadow";
    private static final String STYLE_UI_CORNER_ALL = "ui-corner-all";
    private static final String STYLE_UI_MINI = "ui-mini";

    private static final String STYLE_UI_ICON = "ui-icon-";
    private static final String STYLE_UI_NODISC_ICON = "ui-nodisc-icon";
    private static final String STYLE_UI_ALT_ICON = "ui-alt-icon";
    private static final String STYLE_UI_SHADOW_ICON = "ui-shadow-icon";

    private static final String ROLE = "role";
    private static final String DATA_ROLE = "data-role";
    private static final String DATA_THEME = "data-theme";
    private static final String DATA_INLINE = "data-inline";
    private static final String DATA_CORNERS = "data-corners";
    private static final String DATA_ICON = "data-icon";
    private static final String DATA_ICONPOS = "data-iconpos";
    private static final String DATA_CLEAR_BTN = "data-clear-btn";
    private static final String DATA_MINI = "data-mini";
    private static final String DATA_POPUP_POSITION = "data-position-to";
    private static final String DATA_DIALOG = "data-dialog";
    private static final String DATA_WRAPPER = "data-wrapper-class";
    private static final String DATA_SHADOW = "data-shadow";

    private static final String DATA_FILTER = "data-filter";
    private static final String DATA_INPUT = "data-input";
    private static final String DATA_FILTER_TEXT = "data-filtertext";
    private static final String DATA_FILTER_CHILDREN = "data-children";

    public static boolean isVisible(Widget widget) {
        return widget != null && Mobile.isVisible(widget.getElement());
    }

    public static boolean isHidden(Widget widget) {
        return widget != null && Mobile.isHidden(widget.getElement());
    }

    /**
     * Converts aaa-bbb-ccc to aaaBbbCcc
     */
    public static String hyphenToCamelCase(String hyphenStr) {
        if (hyphenStr == null || hyphenStr.isEmpty()) return hyphenStr;
        int p = hyphenStr.indexOf('-');
        if (p == -1) return hyphenStr;
        StringBuilder sb = new StringBuilder(hyphenStr);
        do {
            int i = p + 1;
            if (i < sb.length()) {
                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
            }
            sb.deleteCharAt(p);
            p = sb.indexOf("-", p);
            if (p == -1) break;
        } while (true);

        return sb.toString();
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
        String styles = elt.getPropertyString("className");
        return styles != null && (indexOfName(styles, style) >= 0);
    }

    public static void removeStylesStartsWith(Element elt, String startsWith) {
        if (elt == null || startsWith == null || startsWith.isEmpty()) return;
        String styles = elt.getPropertyString("className");
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
            elt.setPropertyString("className", styles);
        }
    }

    public static String getStyleStartsWith(Element elt, String startsWith, String... excludes) {
        if (elt == null || startsWith == null || startsWith.isEmpty()) return null;
        String styles = elt.getPropertyString("className");
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
                String s = styles.substring(p, j);
                if (excludes.length > 0) {
                    boolean excluded = false;
                    for (String excl : excludes) {
                        if (s.equals(excl)) {
                            p = j + 1;
                            excluded = true;
                            break;
                        }
                    }
                    if (!excluded) return s;
                } else {
                    return s;
                }

            } else {
                p += startsWith.length();
            }
        } while (true);

        return null;
    }

    /**
     * @param styles - space separated style names
     */
    public static void addStyleNames(UIObject o, String styles) {
        if (o == null || styles == null || styles.isEmpty()) return;
        String[] arr = styles.split(" ");
        for (String i : arr) {
            String s = i.trim();
            if (!s.isEmpty()) o.addStyleName(s);
        }
    }

    /**
     * @param styles - space separated style names
     */
    public static void removeStyleNames(UIObject o, String styles) {
        if (o == null || styles == null || styles.isEmpty()) return;
        String[] arr = styles.split(" ");
        for (String i : arr) {
            String s = i.trim();
            if (!s.isEmpty()) o.removeStyleName(s);
        }
    }

    public static boolean isEnabled(Widget widget) {
        return !hasStyle(widget, STYLE_UI_DISABLED);
    }

    public static void setEnabled(Widget widget, boolean value) {
        if (value) widget.removeStyleName(STYLE_UI_DISABLED);
        else widget.addStyleName(STYLE_UI_DISABLED);
    }

    public static boolean isLabelHidden(Widget widget) {
        return hasStyle(widget, STYLE_UI_HIDE_LABEL);
    }

    public static void setLabelHidden(Widget widget, boolean value) {
        if (value) widget.addStyleName(STYLE_UI_HIDE_LABEL);
        else widget.removeStyleName(STYLE_UI_HIDE_LABEL);
    }

    public static boolean isIconAlt(Widget widget) {
        return hasStyle(widget, STYLE_UI_ALT_ICON);
    }

    public static void setIconAlt(Widget widget, boolean value) {
        if (value) widget.addStyleName(STYLE_UI_ALT_ICON);
        else widget.removeStyleName(STYLE_UI_ALT_ICON);
    }

    public static boolean isIconNoDisc(Widget widget) {
        return hasStyle(widget, STYLE_UI_NODISC_ICON);
    }

    public static void setIconNoDisc(Widget widget, boolean value) {
        if (value) widget.addStyleName(STYLE_UI_NODISC_ICON);
        else widget.removeStyleName(STYLE_UI_NODISC_ICON);
    }

    public static boolean isIconShadow(Widget widget) {
        return hasStyle(widget, STYLE_UI_SHADOW_ICON);
    }

    public static void setIconShadow(Widget widget, boolean value) {
        if (value) widget.addStyleName(STYLE_UI_SHADOW_ICON);
        else widget.removeStyleName(STYLE_UI_SHADOW_ICON);
    }

    public static boolean isShadow(Widget widget) {
        Element elt = widget.getElement();
        String s = getAttribute(elt, DATA_SHADOW);
        if (s == null || s.isEmpty()) return true; // shadow is ON by default
        return "true".equals(s);
        // TODO switch to className: hasStyle(widget, STYLE_UI_SHADOW);
    }

    public static void setShadow(Widget widget, boolean value) {
        if (value) {
            widget.addStyleName(STYLE_UI_SHADOW);
            setAttribute(widget.getElement(), DATA_SHADOW, null);
        }
        else {
            widget.removeStyleName(STYLE_UI_SHADOW);
            setAttribute(widget.getElement(), DATA_SHADOW, "false");
        }
    }

    public static boolean isBtnActive(Element elt) {
        return hasStyle(elt, STYLE_UI_BTN_ACTIVE);
    }

    public static boolean isBtnActive(Widget widget) {
        return hasStyle(widget, STYLE_UI_BTN_ACTIVE);
    }

    public static void setBtnActive(Widget widget, boolean value) {
        if (value) widget.addStyleName(STYLE_UI_BTN_ACTIVE);
        else widget.removeStyleName(STYLE_UI_BTN_ACTIVE);
    }

    public static void setBtnActive(Element elt, boolean value) {
        if (value) elt.addClassName(STYLE_UI_BTN_ACTIVE);
        else elt.removeClassName(STYLE_UI_BTN_ACTIVE);
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

    public static String getRole(Element elt) {
        return getAttribute(elt, ROLE);
    }

    public static String getRole(Widget widget) {
        return getRole(widget.getElement());
    }

    public static void setRole(Element elt, String value) {
        setAttribute(elt, ROLE, value);
    }

    public static void setRole(Widget widget, String value) {
        setRole(widget.getElement(), value);
    }

    public static void removeRole(Widget widget) {
        removeAttribute(widget, ROLE);
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

    public static String getThemeEx(Element elt, String prefix) {
        return getAttribute(elt, DATA_THEME); // TODO switch to className
    }

    public static String getThemeEx(Widget w, String prefix) {
        return getThemeEx(w.getElement(), prefix);
    }

    public static void setThemeEx(Element elt, String themeName, String prefix) {
        String old = getThemeEx(elt, prefix);
        if (old != null && !old.isEmpty()) elt.removeClassName(prefix + old);
        if (themeName != null && !themeName.isEmpty()) elt.addClassName(prefix + themeName);
        setAttribute(elt, DATA_THEME, themeName);
    }

    public static void setThemeEx(Widget w, String themeName, String prefix) {
        setThemeEx(w.getElement(), themeName, prefix);
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

    public static boolean isInlineEx(Element elt, String className) {
        return "true".equals(getAttribute(elt, DATA_INLINE)); // TODO switch to className
    }

    public static boolean isInlineEx(Widget widget, String className) {
        return isInlineEx(widget.getElement(), className);
    }

    public static void setInlineEx(Element elt, boolean inline, String className) {
        if (inline) {
            elt.addClassName(className);
            setAttribute(elt, DATA_INLINE, "true");
        }
        else {
            elt.removeClassName(className);
            removeAttribute(elt, DATA_INLINE);
        }
    }

    public static void setInlineEx(Widget widget, boolean inline, String className) {
        setInlineEx(widget.getElement(), inline, className);
    }

    public static boolean isCorners(Element elt) {
        String s = getAttribute(elt, DATA_CORNERS);
        if (s == null || s.isEmpty()) return true; // corners are ON by default
        return "true".equals(s);
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

    public static boolean isCornersEx(Element elt) {
        String s = getAttribute(elt, DATA_CORNERS); // TODO switch to className
        if (s == null || s.isEmpty()) return true; // corners are ON by default
        return "true".equals(s);
    }

    public static boolean isCornersEx(Widget widget) {
        return isCornersEx(widget.getElement());
    }

    public static void setCornersEx(Element elt, boolean corners) {
        if (corners) elt.addClassName(STYLE_UI_CORNER_ALL);
        else elt.removeClassName(STYLE_UI_CORNER_ALL);
        setAttribute(elt, DATA_CORNERS, String.valueOf(corners));
    }

    public static void setCornersEx(Widget widget, boolean corners) {
        setCornersEx(widget.getElement(), corners);
    }

    public static DataIcon getStyleIcon(Element elt) {
        String s = getStyleStartsWith(elt, STYLE_UI_ICON);
        if (s == null || s.isEmpty()) return null;
        return DataIcon.fromJqmValue(s.substring(STYLE_UI_ICON.length()));
    }

    public static void setStyleIcon(Element elt, DataIcon icon) {
        if (icon == null) {
            removeStylesStartsWith(elt, STYLE_UI_ICON);
            return;
        }
        String s = STYLE_UI_ICON + icon.getJqmValue();
        if (hasStyle(elt, s)) return;
        removeStylesStartsWith(elt, STYLE_UI_ICON);
        elt.addClassName(s);
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

    public static DataIcon getIconEx(Element elt) {
        return DataIcon.fromJqmValue(getAttribute(elt, DATA_ICON)); // TODO switch to className
    }

    public static DataIcon getIconEx(Widget widget) {
        return getIconEx(widget.getElement());
    }

    public static void setIconEx(Element elt, DataIcon icon) {
        setStyleIcon(elt, icon);
        setAttribute(elt, DATA_ICON, icon != null ? icon.getJqmValue() : null);
    }

    public static void setIconEx(Widget widget, DataIcon icon) {
        setIconEx(widget.getElement(), icon);
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

    public static IconPos getIconPosEx(Element elt, String prefix) {
        return IconPos.fromJqmValue(getAttribute(elt, DATA_ICONPOS)); // TODO switch to className
    }

    public static IconPos getIconPosEx(Widget widget, String prefix) {
        return getIconPosEx(widget.getElement(), prefix);
    }

    public static void setIconPosEx(Element elt, IconPos iconPos, String prefix) {
        IconPos old = getIconPosEx(elt, prefix);
        String oldClass = old != null ? prefix + old.getJqmValue() : null;
        if (iconPos != null) {
            String v = iconPos.getJqmValue();
            String newClass = prefix + v;
            if (oldClass != null && !newClass.equals(oldClass)) elt.removeClassName(oldClass);
            elt.addClassName(newClass); // TODO move under if (!newClass.equals(oldClass)) when getIconPosEx() switched to className
            setAttribute(elt, DATA_ICONPOS, v);
        } else {
            if (oldClass != null) elt.removeClassName(oldClass);
            setAttribute(elt, DATA_ICONPOS, null);
        }
    }

    public static void setIconPosEx(Widget widget, IconPos iconPos, String prefix) {
        setIconPosEx(widget.getElement(), iconPos, prefix);
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

    public static boolean isMiniEx(Element elt) {
        return "true".equals(getAttribute(elt, DATA_MINI)); // TODO switch to className
    }

    public static boolean isMiniEx(Widget widget) {
        return isMiniEx(widget.getElement());
    }

    public static void setMiniEx(Element elt, boolean mini) {
        if (mini) {
            elt.addClassName(STYLE_UI_MINI);
            setAttribute(elt, DATA_MINI, "true");
        } else {
            elt.removeClassName(STYLE_UI_MINI);
            removeAttribute(elt, DATA_MINI);
        }
    }

    public static void setMiniEx(Widget widget, boolean mini) {
        setMiniEx(widget.getElement(), mini);
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

    public static boolean isDataDialog(Element elt) {
        return "true".equals(getAttribute(elt, DATA_DIALOG));
    }

    public static boolean isDataDialog(Widget widget) {
        return isDataDialog(widget.getElement());
    }

    public static void setDataDialog(Element elt, boolean value) {
        if (value) setAttribute(elt, DATA_DIALOG, "true");
        else removeAttribute(elt, DATA_DIALOG);
    }

    public static void setDataDialog(Widget widget, boolean value) {
        setDataDialog(widget.getElement(), value);
    }

    public static String getDataWrapper(Element elt) {
        return getAttribute(elt, DATA_WRAPPER);
    }

    public static String getDataWrapper(Widget widget) {
        return getDataWrapper(widget.getElement());
    }

    public static void setDataWrapper(Element elt, String wrapper) {
        setAttribute(elt, DATA_WRAPPER, wrapper);
    }

    public static void setDataWrapper(Widget widget, String wrapper) {
        setDataWrapper(widget.getElement(), wrapper);
    }

    public static boolean isFilterable(Element elt) {
        return "true".equals(getAttribute(elt, DATA_FILTER));
    }

    public static boolean isFilterable(Widget widget) {
        return isFilterable(widget.getElement());
    }

    public static void setFilterable(Element elt, boolean value) {
        setAttribute(elt, DATA_FILTER, value ? "true" : null);
    }

    public static void setFilterable(Widget widget, boolean value) {
        setFilterable(widget.getElement(), value);
    }

    public static String getDataFilter(Element elt) {
        return getAttribute(elt, DATA_INPUT);
    }

    public static String getDataFilter(Widget widget) {
        return getDataFilter(widget.getElement());
    }

    public static void setDataFilter(Element elt, String filterSelector) {
        if (filterSelector == null || filterSelector.isEmpty()) {
            setAttribute(elt, DATA_FILTER, null);
            setAttribute(elt, DATA_INPUT, null);
        } else {
            setAttribute(elt, DATA_FILTER, "true");
            setAttribute(elt, DATA_INPUT, filterSelector);
        }
    }

    public static void setDataFilter(Widget widget, String filterSelector) {
        setDataFilter(widget.getElement(), filterSelector);
    }

    public static String getFilterText(Element elt) {
        return getAttribute(elt, DATA_FILTER_TEXT);
    }

    public static String getFilterText(Widget widget) {
        return getFilterText(widget.getElement());
    }

    public static void setFilterText(Element elt, String filterText) {
        setAttribute(elt, DATA_FILTER_TEXT, filterText);
    }

    public static void setFilterText(Widget widget, String filterText) {
        setFilterText(widget.getElement(), filterText);
    }

    public static String getFilterChildren(Element elt) {
        return getAttribute(elt, DATA_FILTER_CHILDREN);
    }

    public static String getFilterChildren(Widget widget) {
        return getFilterChildren(widget.getElement());
    }

    public static void setFilterChildren(Element elt, String filterChildren) {
        setAttribute(elt, DATA_FILTER_CHILDREN, filterChildren);
    }

    public static void setFilterChildren(Widget widget, String filterChildren) {
        setFilterChildren(widget.getElement(), filterChildren);
    }

    public static native void refreshFilter(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-filterable') !== undefined) {
            w.filterable('refresh');
        }
    }-*/;

    /**
     * Updates the filterable widget.
     * If you manipulate a filterable widget programmatically (e.g. by adding new children
     * or removing old ones), you must call the refreshFilter() method on it to update the visuals.
     */
    public static void refreshFilter(Widget widget) {
        refreshFilter(widget.getElement());
    }

}
