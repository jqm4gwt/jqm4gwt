package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Misc helper functionality for supporting development of jqm widgets.
 * @author slavap
 *
 */
public class JQMCommon {

    private JQMCommon() {} // static class, not supposed to be instantiated

    public static final String POPUP_POS_WINDOW = "window";
    public static final String POPUP_POS_ORIGIN = "origin";

    public static final String STYLE_UI_BTN = "ui-btn-";
    public static final String STYLE_UI_BTN_INLINE = "ui-btn-inline";
    public static final String STYLE_UI_BTN_ICONPOS = "ui-btn-icon-";
    public static final String STYLE_UI_BTN_ACTIVE = "ui-btn-active";

    private static final String STYLE_UI_DISABLED = "ui-state-disabled";

    // TODO: Deprecated in 1.4, quite serious refactoring is needed to switch to ui-hidden-accessible
    // See https://github.com/jquery/jquery-mobile/issues/6405
    private static final String STYLE_UI_HIDE_LABEL = "ui-hide-label";
    private static final String STYLE_UI_SHADOW = "ui-shadow";
    private static final String STYLE_UI_CORNER_ALL = "ui-corner-all";
    private static final String STYLE_UI_MINI = "ui-mini";

    public static final String STYLE_UI_ICON = "ui-icon-";
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
    private static final String DATA_TRANSITION = "data-transition";

    private static final String DATA_FILTER = "data-filter";
    private static final String DATA_INPUT = "data-input";
    private static final String DATA_FILTER_TEXT = "data-filtertext";
    private static final String DATA_FILTER_CHILDREN = "data-children";
    private static final String DATA_FILTER_REVEAL = "data-filter-reveal";

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

    /**
     * @param attrs - comma separated attr=value pairs. If specified attr= then attr will be removed.
     */
    public static void setAttributes(Element elt, String attrs) {
        if (elt == null || attrs == null || attrs.isEmpty()) return;
        String[] arr = attrs.split(",");
        for (String i : arr) {
            String s = i.trim();
            if (s.isEmpty()) continue;
            int p = s.indexOf('=');
            if (p == -1) continue;
            String attr = s.substring(0, p).trim();
            if (attr.isEmpty()) continue;
            String val = s.substring(p + 1).trim();
            if (val.isEmpty()) elt.removeAttribute(attr);
            else elt.setAttribute(attr, val);
        }
    }

    /**
     * @param attrs - comma separated attribute names.
     */
    public static void removeAttributes(Element elt, String attrs) {
        if (elt == null || attrs == null || attrs.isEmpty()) return;
        String[] arr = attrs.split(",");
        for (String i : arr) {
            String s = i.trim();
            if (s.isEmpty()) continue;
            elt.removeAttribute(s);
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

    public static boolean isShadowEx(Element elt) {
        return hasStyle(elt, STYLE_UI_SHADOW);
    }

    public static boolean isShadowEx(Widget widget) {
        return isShadowEx(widget.getElement());
    }

    public static void setShadowEx(Element elt, boolean value) {
        if (value) {
            elt.addClassName(STYLE_UI_SHADOW);
            setAttribute(elt, DATA_SHADOW, null);
        }
        else {
            elt.removeClassName(STYLE_UI_SHADOW);
            setAttribute(elt, DATA_SHADOW, "false");
        }
    }

    public static void setShadowEx(Widget widget, boolean value) {
        setShadowEx(widget.getElement(), value);
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

    public static String getThemeEx(Element elt, String prefix, String... excludes) {
        String s = getStyleStartsWith(elt, prefix, excludes);
        if (s == null || s.isEmpty()) return null;
        s = s.substring(prefix.length());
        // check length to prevent conflict with ui-btn-inline, ... in case of forgotten excludes
        return s.length() > 1 ? null : s;
    }

    public static String getThemeEx(Widget w, String prefix, String... excludes) {
        return getThemeEx(w.getElement(), prefix, excludes);
    }

    public static void setThemeEx(Element elt, String themeName, String prefix, String... excludes) {
        String old = getThemeEx(elt, prefix, excludes);
        if (old != null && !old.isEmpty()) elt.removeClassName(prefix + old);
        if (themeName != null && !themeName.isEmpty()) elt.addClassName(prefix + themeName);
        setAttribute(elt, DATA_THEME, themeName);
    }

    public static void setThemeEx(Widget w, String themeName, String prefix, String... excludes) {
        setThemeEx(w.getElement(), themeName, prefix, excludes);
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
        return hasStyle(elt, className);
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
        return hasStyle(elt, STYLE_UI_CORNER_ALL);
    }

    public static boolean isCornersEx(Widget widget) {
        return isCornersEx(widget.getElement());
    }

    public static void setCornersEx(Element elt, boolean corners) {
        if (corners) {
            elt.addClassName(STYLE_UI_CORNER_ALL);
            setAttribute(elt, DATA_CORNERS, null);
        } else {
            elt.removeClassName(STYLE_UI_CORNER_ALL);
            setAttribute(elt, DATA_CORNERS, "false");
        }
    }

    public static void setCornersEx(Widget widget, boolean corners) {
        setCornersEx(widget.getElement(), corners);
    }

    public static DataIcon getStyleIcon(Element elt) {
        String s = getStyleStartsWith(elt, STYLE_UI_ICON);
        if (s == null || s.isEmpty()) return null;
        return DataIcon.fromJqmValue(s.substring(STYLE_UI_ICON.length()));
    }

    public static void setStyleIcon(Element elt, String icon) {
        if (icon == null || icon.isEmpty()) {
            removeStylesStartsWith(elt, STYLE_UI_ICON);
            return;
        }
        String s = STYLE_UI_ICON + icon;
        if (hasStyle(elt, s)) return;
        removeStylesStartsWith(elt, STYLE_UI_ICON);
        elt.addClassName(s);
    }

    public static void setStyleIcon(Element elt, DataIcon icon) {
        if (icon == null || DataIcon.NONE.equals(icon)) {
            setStyleIcon(elt, (String) null);
            return;
        }
        setStyleIcon(elt, icon.getJqmValue());
    }

    public static DataIcon getIcon(Element elt) {
        return DataIcon.fromJqmValue(getAttribute(elt, DATA_ICON));
    }

    public static String getCustomIcon(Element elt) {
        return getAttribute(elt, DATA_ICON);
    }

    public static DataIcon getIcon(Widget widget) {
        return getIcon(widget.getElement());
    }

    public static void setIcon(Element elt, DataIcon icon) {
        setAttribute(elt, DATA_ICON, icon != null ? icon.getJqmValue() : null);
    }

    public static void setIcon(Element elt, String customIcon) {
        setAttribute(elt, DATA_ICON, customIcon != null ? customIcon : null);
    }

    public static void setIcon(Widget widget, DataIcon icon) {
        setIcon(widget.getElement(), icon);
    }

    public static String getIconExStr(Element elt) {
        String s = getStyleStartsWith(elt, STYLE_UI_ICON);
        if (s != null && !s.isEmpty()) {
            s = s.substring(STYLE_UI_ICON.length());
            return s;
        }
        s = getAttribute(elt, DATA_ICON);
        if (DataIcon.NONE.getJqmValue().equals(s)) return s; // NONE has no className, but stored in attribute
        return null;
    }

    public static DataIcon getIconEx(Element elt) {
        String s = getIconExStr(elt);
        DataIcon icon = DataIcon.fromJqmValue(s);
        return icon;
    }

    public static DataIcon getIconEx(Widget widget) {
        return getIconEx(widget.getElement());
    }

    public static void setIconEx(Element elt, String icon) {
        setStyleIcon(elt, icon);
        setAttribute(elt, DATA_ICON, icon);
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
        String s = getStyleStartsWith(elt, prefix);
        if (s != null && !s.isEmpty()) {
            s = s.substring(prefix.length());
            return IconPos.fromJqmValue(s);
        }
        // className is present only if STYLE_UI_ICON is defined,
        // so first check className and if empty then attribute
        return IconPos.fromJqmValue(getAttribute(elt, DATA_ICONPOS));
    }

    public static IconPos getIconPosEx(Widget widget, String prefix) {
        return getIconPosEx(widget.getElement(), prefix);
    }

    public static void invalidateIconPosEx(Element elt, String prefix) {
        IconPos pos = getIconPosEx(elt, prefix);
        String icon = getStyleStartsWith(elt, STYLE_UI_ICON);
        if (icon == null || icon.isEmpty()) {
            if (pos != null) {
                String clazz = prefix + pos.getJqmValue();
                elt.removeClassName(clazz);
            }
        } else {
            if (pos == null) pos = IconPos.LEFT; // if icon is defined, iconPos must be defined as well
            String clazz = prefix + pos.getJqmValue();
            elt.addClassName(clazz);
        }
        if (pos != null) {
            String v = pos.getJqmValue();
            setAttribute(elt, DATA_ICONPOS, v); // save iconPos in attribute for the possible future usage
        }
    }

    public static void setIconPosEx(Element elt, IconPos iconPos, String prefix) {
        IconPos old = getIconPosEx(elt, prefix);
        String oldClass = old != null ? prefix + old.getJqmValue() : null;
        if (iconPos != null) {
            String v = iconPos.getJqmValue();
            String icon = getStyleStartsWith(elt, STYLE_UI_ICON);
            if (icon == null || icon.isEmpty()) {
                if (oldClass != null) elt.removeClassName(oldClass);
            } else {
                String newClass = prefix + v;
                if (oldClass != null && !newClass.equals(oldClass)) elt.removeClassName(oldClass);
                elt.addClassName(newClass); // we have to be absolutely sure that newClass is added (attribute -> className case when icon added)
            }
            setAttribute(elt, DATA_ICONPOS, v);
        } else {
            if (oldClass != null) elt.removeClassName(oldClass);
            setAttribute(elt, DATA_ICONPOS, null);
        }
    }

    public static void setIconPosEx(Widget widget, IconPos iconPos, String prefix) {
        setIconPosEx(widget.getElement(), iconPos, prefix);
    }

    public static Transition getTransition(Element elt) {
        return Transition.fromJqmValue(getAttribute(elt, DATA_TRANSITION));
    }

    public static Transition getTransition(Widget widget) {
        return getTransition(widget.getElement());
    }

    public static void setTransition(Element elt, Transition value) {
        setAttribute(elt, DATA_TRANSITION, value != null ? value.getJqmValue() : null);
    }

    public static void setTransition(Widget widget, Transition value) {
        setTransition(widget.getElement(), value);
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
        return hasStyle(elt, STYLE_UI_MINI);
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

    public static boolean isFilterReveal(Element elt) {
        return "true".equals(getAttribute(elt, DATA_FILTER_REVEAL));
    }

    public static boolean isFilterReveal(Widget widget) {
        return isFilterReveal(widget.getElement());
    }

    public static void setFilterReveal(Element elt, boolean value) {
        setAttribute(elt, DATA_FILTER_REVEAL, value ? "true" : null);
    }

    public static void setFilterReveal(Widget widget, boolean value) {
        setFilterReveal(widget.getElement(), value);
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

    public static native void bindFilterEvents(HasFilterable fltr, Element fltrElt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$(fltrElt).on("filterablebeforefilter", function(event, ui) {
            var value = ui.input.val();
            fltr.@com.sksamuel.jqm4gwt.HasFilterable::doBeforeFilter(Ljava/lang/String;)(value);
        });
    }-*/;

    public static native void unbindFilterEvents(Element fltrElt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$(fltrElt).off("filterablebeforefilter");
    }-*/;

    /** Useful when overriding doFiltering() */
    public static native String getTextForFiltering(Element elt) /*-{
        return ("" + ($wnd.$.mobile.getAttribute(elt, "filtertext") || $wnd.$(elt).text()));
    }-*/;

    public static native boolean isFilterableReady(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-filterable') !== undefined) {
            return true;
        } else {
            return false;
        }
    }-*/;

    public static native void bindFilterCallback(HasFilterable fltr, Element fltrElt,
            JavaScriptObject origFilter) /*-{
        $wnd.$(fltrElt).filterable("option", "filterCallback",
            function(index, searchValue) {
                var idx = @java.lang.Integer::valueOf(I)(index);
                var rslt = fltr.@com.sksamuel.jqm4gwt.HasFilterable::doFiltering(Lcom/google/gwt/dom/client/Element;Ljava/lang/Integer;Ljava/lang/String;)(this, idx, searchValue);
                if (rslt === undefined || rslt === null) {
                    if (origFilter === undefined || origFilter === null) {
                        return false;
                    } else {
                        return origFilter.call(this, index, searchValue);
                    }
                } else {
                    var v = rslt === @java.lang.Boolean::TRUE ? true : false;
                    return v;
                }
            });
    }-*/;

    public static native void unbindFilterCallback(Element fltrElt, JavaScriptObject origFilter) /*-{
        $wnd.$(fltrElt).filterable("option", "filterCallback", origFilter);
    }-*/;

    public static native JavaScriptObject getFilterCallback(Element fltrElt) /*-{
        return $wnd.$(fltrElt).filterable("option", "filterCallback");
    }-*/;

    /**
     * @param imgCssName - custom image CSS name, may start from ui-icon- prefix, like: ui-icon-myicon
     * @return - CSS, can be injected with StyleInjector.inject()
     */
    public static String getImageCss(ImageResource img, String imgCssName) {
        if (img == null || imgCssName == null || imgCssName.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        SafeUri url = img.getSafeUri();
        sb.append('.').append(imgCssName).append(":after {");
        sb.append("background-image: url(").append(url.asString()).append(");");
        sb.append("background-size: ").append(img.getWidth()).append(Unit.PX.getType());
        sb.append(' ').append(img.getHeight()).append(Unit.PX.getType()).append(';');
        sb.append("background-color: transparent;}");
        return sb.toString();
    }

    public static native String getVal(String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return null; // jQuery is not loaded
        return $wnd.$("#" + id).val();
    }-*/;

    public static native void setVal(String id, String value) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return null; // jQuery is not loaded
        $wnd.$("#" + id).val(value);
    }-*/;

}
