package com.sksamuel.jqm4gwt.button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIcon;
import com.sksamuel.jqm4gwt.HasIconShadow;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasRel;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.HasTransition;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMContainer;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPageEvent;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 14:02:24
 * <p/>
 * An implementation of a Jquery mobile button.
 * <p/>See <a href="http://demos.jquerymobile.com/1.4.5/button-markup/">Buttons</a>
 * <p/>See also <a href="http://jquerymobile.com/demos/1.2.1/docs/buttons/buttons-types.html">Button basics</a>
 */
public class JQMButton extends JQMWidget implements HasText<JQMButton>, HasRel<JQMButton>,
        HasTransition<JQMButton>, HasClickHandlers, HasInline<JQMButton>,
        HasIcon<JQMButton>, HasCorners<JQMButton>, HasIconShadow<JQMButton>, HasMini<JQMButton>,
        HasTapHandlers {

    private static final String[] HOVER_PROPS = { "background-color", "color", "border-color", "text-shadow" };

    private static final String[] HOVER_REGEX = { "^border-\\S*color$", "^border-\\S*color-value$" };

    /** FindRegex/Replacement pairs */
    private static final String[] HOVER_REPLACE = { "color-value", "color" };

    /** Heuristics based on jquery.mobile.css definitions */
    private static final Map<String, String> currentThemeSearch = new LinkedHashMap<String, String>();

    private static final Map<String, JavaScriptObject> cachedCssRules = new HashMap<String, JavaScriptObject>();

    private boolean alwaysActive;
    private boolean alwaysHover;
    private JavaScriptObject hoverStyle = null;

    private class StyleItem {
        public final String property;
        public final String oldValue;
        public final String newValue;

        public StyleItem(String property, String oldValue, String newValue) {
            this.property = property;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }
    }

    private List<StyleItem> hoverStyleApplied = null;

    static {
        currentThemeSearch.put("ui-group-theme-", null);
        currentThemeSearch.put("ui-body-", "ui-body-inherit");
        currentThemeSearch.put("ui-bar-", "ui-bar-inherit");
        currentThemeSearch.put("ui-page-theme-", null);
    }

    private static String getCurrentTheme(Element elt) {
        if (elt == null) return null;
        for (Entry<String, String> i : currentThemeSearch.entrySet()) {
            String v = i.getValue();
            String s = v == null ? JQMCommon.getStyleStartsWith(elt, i.getKey())
                                 : JQMCommon.getStyleStartsWith(elt, i.getKey(), v);
            if (s != null && !s.isEmpty()) {
                return s.substring(i.getKey().length());
            }
        }
        return null;
    }

    private String getCurrentTheme() {
        String s = getTheme();
        if (s != null && !s.isEmpty()) return s;
        Element elt = getElement().getParentElement();
        while (elt != null) {
            s = getCurrentTheme(elt);
            if (s != null) return s;
            elt = elt.getParentElement();
        }
        return "a"; // just meaningful default value
    }

    /**
     * Create a {@link JQMButton} with the given text that does not link to
     * anything. This button would only react to events if a link is added or
     * a click handler is attached.
     *
     * @param text the text to display on the button
     */
    public @UiConstructor JQMButton(String text) {
        this(new Anchor(text));
    }

    /**
     * Convenience constructor that creates a button that shows the given
     * {@link JQMPage} when clicked. The link will use a Transition.POP type.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param c    the {@link JQMContainer} to create a link to
     */
    public JQMButton(String text, final JQMContainer c) {
        this(text, c, null);
    }

    /**
     * Convenience constructor that creates a button that shows the given
     * {@link JQMPage} when clicked.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param c    the {@link JQMContainer} to create a link to
     * @param t    the transition type to use
     */
    public JQMButton(String text, final JQMContainer c, final Transition t) {
        this(text, "#" + c.getId(), t);
        withRel(c.getRelType());
    }

    /**
     * Convenience constructor that creates a button that shows the given url
     * when clicked. The link will use a Transition.POP type.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param url  the HTTP url to create a link to
     */
    public JQMButton(String text, String url) {
        this(text, url, null);
    }

    /**
     * Convenience constructor that creates a button that shows the given url
     * when clicked.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param url  the HTTP url to create a link to
     * @param t    the transition type to use
     */
    public JQMButton(String text, String url, final Transition t) {
        this(text);
        if (url != null)
            setHref(url);
        if (t != null)
            withTransition(t);
    }

    public static void initEltAsButton(Element elt) {
        //JQMCommon.setDataRole("button"); - performance and buttonMarkup() is deprecated as of 1.4 and will be removed in 1.5

        JQMCommon.setDataRole(elt, null);
        elt.addClassName("ui-btn");
        String tag = elt.getTagName();
        if (tag == null || !tag.equals("BUTTON")) JQMCommon.setRole(elt, "button");
        // TODO - defaults should be set based on $.fn.buttonMarkup.defaults
        JQMCommon.setShadowEx(elt, true);
        JQMCommon.setCornersEx(elt, true);
    }

    protected JQMButton(Widget widget) {
        initWidget(widget);
        setStyleName("jqm4gwt-button");
        initEltAsButton(getElement());
        setId();
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
    public IconPos getIconPos() {
        return JQMCommon.getIconPosEx(this, JQMCommon.STYLE_UI_BTN_ICONPOS);
    }

	/**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    @Override
    public void setIconPos(IconPos pos) {
        JQMCommon.setIconPosEx(this, pos, JQMCommon.STYLE_UI_BTN_ICONPOS);
    }

    /**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    @Override
    public JQMButton withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

    @Override
    public String getText() {
        Element e = getElement();
        while (e.getFirstChildElement() != null) {
            e = e.getFirstChildElement();
        }
        return e.getInnerText();
    }

    /**
     * Returns true if this button is set to load the linked page as a dialog page
     *
     * @return true if this link will show as a dialog
     */
    public boolean isDialog() {
        return Mobile.DATA_ROLE_DIALOG.equals(getRel());
    }

    /**
     * Sets this button to be a dialog button. This changes the look and feel
     * of the page that is loaded as a consequence of clicking on this button.
     */
    public void setDialog(boolean dialog) {
        setRel(dialog ? Mobile.DATA_ROLE_DIALOG : null);
    }

    public JQMButton withDialog(boolean dialog) {
        setDialog(dialog);
        return this;
    }

    /**
     * Returns true if this button is set to load a popup
     */
    public boolean isPopup() {
        return "popup".equals(getRel());
    }

    /**
     * @param popup - true if this button is set to load a popup
     */
    public void setPopup(boolean popup) {
        setRel(popup ? "popup" : null);
    }

    public JQMButton withPopup(boolean popup) {
        setPopup(popup);
        return this;
    }

    public String getPopupPos() {
        return JQMCommon.getPopupPos(this);
    }

    /**
     * @param pos - possible values: window, origin, jQuery selector to get positioning element.
     */
    public void setPopupPos(String pos) {
        JQMCommon.setPopupPos(this, pos);
    }

    @Override
    public boolean isIconShadow() {
        return JQMCommon.isIconShadow(this);
    }

    /** Applies the drop shadow icon style to the select button if set to true. */
    @Override
    public void setIconShadow(boolean shadow) {
        JQMCommon.setIconShadow(this, shadow);
    }

    /** Applies the drop shadow icon style to the select button if set to true. */
    @Override
    public JQMButton withIconShadow(boolean shadow) {
        setIconShadow(shadow);
        return this;
    }

    public boolean isShadow() {
        return JQMCommon.isShadowEx(this);
    }

    /** Button will have shadow if true */
    public void setShadow(boolean shadow) {
        JQMCommon.setShadowEx(this, shadow);
    }

    public String getHref() {
        return getAttribute("href");
    }

    public void setHref(String url) {
         setAttribute("href", url);
    }

    public JQMButton withHref(String url) {
        setHref(url);
        return this;
    }

    /**
     * Sets this button to be a back button. This will override any URL set on
     * the button.
     */
    public void setBack(boolean back) {
        setRel(back ? "back" : null);
    }

    public JQMButton withBack(boolean back) {
        setBack(back);
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
    public JQMButton withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    /**
     * Short cut for withRel("external");
     */
    public void setExternal(boolean external) {
        setRel(external ? "external" : null);
    }

    public JQMButton withExternal(boolean external) {
        setExternal(external);
        return this;
    }

    public DataIcon getBuiltInIcon() {
        return JQMCommon.getIconEx(this);
    }

    /**
     * Sets the icon used by this button. See {@link DataIcon}.
     */
    @Override
    public void setBuiltInIcon(DataIcon icon) {
        JQMCommon.setIconEx(this, icon);
        JQMCommon.invalidateIconPosEx(getElement(), JQMCommon.STYLE_UI_BTN_ICONPOS);
    }

    @Override
    public JQMButton removeIcon() {
        JQMCommon.setIconEx(this, null);
        return this;
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

    /**
     * Sets the icon used by this button. See {@link DataIcon}.
     */
    @Override
    public JQMButton withBuiltInIcon(DataIcon icon) {
        setBuiltInIcon(icon);
        return this;
    }

    @Override
    public JQMButton withIconURL(String src) {
        setIconURL(src);
        return this;
    }

    /**
     * @return true if this button is set to inline
     */
    @Override
    public boolean isInline() {
        return JQMCommon.isInlineEx(this, JQMCommon.STYLE_UI_BTN_INLINE);
    }

    /**
     * Sets this button to be inline.
     * <p/>
     * NOTE: If this button is inside a {@link JQMButtonGroup} then you must
     * call withInline(boolean) on the button group itself and not each button
     * individually.
     *
     * @param inline true to change to line or false to switch to full width
     */
    @Override
    public void setInline(boolean inline) {
        JQMCommon.setInlineEx(this, inline, JQMCommon.STYLE_UI_BTN_INLINE);
    }

    /**
     * Sets this button to be inline.
     * <p/>
     * NOTE: If this button is inside a {@link JQMButtonGroup} then you must
     * call withInline(boolean) on the button group itself and not each button
     * individually.
     *
     * @param inline true to change to line or false to switch to full width
     */
    @Override
    public JQMButton withInline(boolean inline) {
        setInline(inline);
        return this;
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMiniEx(this);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMiniEx(this, mini);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMButton withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public String getRel() {
        return JQMCommon.getAttribute(getElement(), "data-rel");
    }

    @Override
    public void setRel(String rel) {
        JQMCommon.setAttribute(getElement(), "data-rel", rel);
    }

    @Override
    public JQMButton withRel(String rel) {
        setRel(rel);
        return this;
    }

    @Override
    public void setText(String text) {
        // if the button has already been rendered then we need to go down
        // deep until we get the final span.
        // it's not a case in 1.4.x anymore, because buttons have clean/simple mark-up now,
        // but could be useful for complex buttons (see setHtml() method).
        Element e = getElement();
        while (e.getFirstChildElement() != null) {
            e = e.getFirstChildElement();
        }
        e.setInnerText(text);
    }

    /**
     * Useful for complex buttons, for example buttons with vertically centered text.
     **/
    public void setHtml(String html) {
        Element e = getElement();
        e.setInnerHTML(html);
    }

    public String getHtml() {
        Element e = getElement();
        return e.getInnerHTML();
    }

    @Override
    public JQMButton withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public Transition getTransition() {
        return JQMCommon.getTransition(getElement());
    }

    /**
     * Sets the transition to be used by this button when loading the URL.
     */
    @Override
    public void setTransition(Transition transition) {
        JQMCommon.setTransition(getElement(), transition);
    }

    /**
     * Sets the transition to be used by this button when loading the URL.
     */
    @Override
    public JQMButton withTransition(Transition transition) {
        setTransition(transition);
        return this;
    }

    public void setTransitionReverse(boolean reverse) {
        if (reverse) setAttribute("data-direction", "reverse");
        else removeAttribute("data-direction");
    }

    public JQMButton withTransitionReverse(boolean reverse) {
        setTransitionReverse(reverse);
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

    public boolean isAlwaysActive() {
        return alwaysActive;
    }

    /**
     * @param value - if true button always be highlighted as active.
     */
    public void setAlwaysActive(boolean value) {
        if (alwaysActive == value) return;
        alwaysActive = value;
        JQMCommon.setBtnActive(this, alwaysActive);
        if (alwaysActive) Scheduler.get().scheduleFinally(createAlwaysActiveCmd());
    }

    private Scheduler.RepeatingCommand createAlwaysActiveCmd() {
        return new Scheduler.RepeatingCommand() {
            @Override
            public boolean execute() {
                if (alwaysActive) JQMCommon.setBtnActive(JQMButton.this, true);
                return alwaysActive; // stops when alwaysActive == false
            }};
    }

    public boolean isAlwaysHover() {
        return alwaysHover;
    }

    /**
     * @param value - if true button always be highlighted as hover.
     */
    public void setAlwaysHover(boolean value) {
        if (alwaysHover == value) return;
        alwaysHover = value;
        checkAlwaysHover();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (alwaysHover) {
            Widget p = getParent();
            while (p != null) {
                if (p instanceof JQMPage) {
                    ((JQMPage) p).addPageHandler(new JQMPageEvent.DefaultHandler() {
                        @Override
                        public void onShow(JQMPageEvent event) {
                            super.onShow(event);
                            checkAlwaysHover();
                        }
                    });
                    break;
                }
                p = p.getParent();
            }
            if (!(p instanceof JQMPage)) checkAlwaysHover();
        }
    }

    private void checkAlwaysHover() {
        if (isAttached()) {
            if (alwaysHover) {
                prepareHoverStyle();
                applyHoverStyle();
            } else {
                removeHoverStyle();
            }
        }
    }

    private void applyHoverStyle() {
        if (hoverStyle == null) return;
        Style st = getElement().getStyle();
        if (hoverStyleApplied == null) {
            hoverStyleApplied = new ArrayList<StyleItem>();
            JsArrayString keys = JQMContext.getJsObjKeys(hoverStyle);
            if (keys == null) return;
            for (int i = 0; i < keys.length(); i++) {
                String val = JQMContext.getJsObjValue(hoverStyle, keys.get(i));
                if (val == null) continue;
                String prop = keys.get(i);
                for (int j = 0; j < HOVER_REPLACE.length - 1; j = j + 2) {
                    prop = prop.replaceAll(HOVER_REPLACE[j], HOVER_REPLACE[j + 1]);
                }
                prop = JQMCommon.hyphenToCamelCase(prop);
                String oldVal = st.getProperty(prop);
                String newVal = val.toString();
                if (newVal.equals(oldVal)) continue;
                hoverStyleApplied.add(new StyleItem(prop, oldVal, newVal));
            }
        }
        for (StyleItem item : hoverStyleApplied) {
            st.setProperty(item.property, item.newValue);
        }
    }

    private void removeHoverStyle() {
        if (hoverStyleApplied != null) {
            Style st = getElement().getStyle();
            for (StyleItem item : hoverStyleApplied) {
                st.setProperty(item.property, item.oldValue);
            }
        }
        hoverStyleApplied = null;
        hoverStyle = null;
    }

    private void prepareHoverStyle() {
        if (hoverStyle != null) return;

        String theme = getCurrentTheme();
        String rule = ".ui-btn.ui-btn-" + theme + ":hover";
        hoverStyle = cachedCssRules.get(rule);
        if (hoverStyle != null) return;

        String ruleIE = ".ui-btn-" + theme + ".ui-btn:hover"; // IE9 changes/rearranges css rules!
        hoverStyle = cachedCssRules.get(ruleIE);
        if (hoverStyle != null) return;

        JsArrayString jsStrs = JQMContext.getJsArrayString(HOVER_PROPS);
        JsArrayString jsRegex = JQMContext.getJsArrayString(HOVER_REGEX);
        hoverStyle = JQMContext.getCssForRule(rule, jsStrs, jsRegex);
        if (hoverStyle != null) {
            cachedCssRules.put(rule, hoverStyle);
        } else {
            hoverStyle = JQMContext.getCssForRule(ruleIE, jsStrs, jsRegex);
            if (hoverStyle != null) cachedCssRules.put(ruleIE, hoverStyle);
        }
    }

    @Override
    public String getTheme() {
        return JQMCommon.getThemeEx(this, JQMCommon.STYLE_UI_BTN,
                /*excludes:*/ JQMCommon.STYLE_UI_BTN_INLINE, JQMCommon.STYLE_UI_BTN_ICONPOS,
                JQMCommon.STYLE_UI_BTN_ACTIVE);
    }

    @Override
    public void setTheme(String themeName) {
        setTheme(this.getElement(), themeName);
    }

    public static void setTheme(Element elt, String themeName) {
        JQMCommon.setThemeEx(elt, themeName, JQMCommon.STYLE_UI_BTN,
                /*excludes:*/ JQMCommon.STYLE_UI_BTN_INLINE, JQMCommon.STYLE_UI_BTN_ICONPOS,
                JQMCommon.STYLE_UI_BTN_ACTIVE);
    }

    private static final String STYLE_UI_BTN_RIGHT = "ui-btn-right";
    private static final String STYLE_UI_BTN_LEFT = "ui-btn-left";

    public static enum PosOnBand { RIGHT, LEFT }

    public PosOnBand getPosOnBand() {
        if (JQMCommon.hasStyle(this, STYLE_UI_BTN_RIGHT)) return PosOnBand.RIGHT;
        else if (JQMCommon.hasStyle(this, STYLE_UI_BTN_LEFT)) return PosOnBand.LEFT;
        else return null;
    }

    /**
     * Works in case of this button placed on Header, Popup, ...
     */
    public void setPosOnBand(PosOnBand value) {
        if (value == null) {
            getElement().removeClassName(STYLE_UI_BTN_RIGHT);
            getElement().removeClassName(STYLE_UI_BTN_LEFT);
        } else {
            switch (value) {
            case LEFT:
                getElement().addClassName(STYLE_UI_BTN_LEFT);
                break;
            case RIGHT:
                getElement().addClassName(STYLE_UI_BTN_RIGHT);
                break;
            }
        }
    }

}
