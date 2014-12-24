package com.sksamuel.jqm4gwt.toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.list.JQMList;
import com.sksamuel.jqm4gwt.list.JQMListItem;
import com.sksamuel.jqm4gwt.toolbar.JQMTabsEvent.TabsState;

/**
 * @author SlavaP
 *
 * <p> See <a href="http://demos.jquerymobile.com/1.4.5/tabs/">Tabs widget</a></p>
 * <p> See also <a href="http://api.jqueryui.com/tabs/">jQueryUI Tabs widget</a></p>
 *
 */
public class JQMTabs extends JQMWidget {

    /**
     * Predefined tab event, see {@link JQMTabs#setTabEvent(String)}
     */
    public static final String ACTIVATE_TAB_ON_HOVER = "mouseover";
    public static final String ACTIVATE_TAB_ON_CLICK = "click";

    /**
     * Controls the height of the tabs widget and each panel.
     */
    public static enum HeightStyle {
        /** All panels will be set to the height of the tallest panel. */
        AUTO("auto"),

        /** Expand to the available height based on the tabs' parent height. */
        FILL("fill"),

        /** Each panel will be only as tall as its content. */
        CONTENT("content");

        private final String jqmVal;

        private HeightStyle(String jqmVal) {
            this.jqmVal = jqmVal;
        }

        public String getJqmValue() {
            return jqmVal;
        }

        public static HeightStyle fromJqmValue(String jqmValue) {
            if (jqmValue == null || jqmValue.isEmpty()) return null;
            for (HeightStyle i : HeightStyle.values()) {
                if (i.getJqmValue().equals(jqmValue)) return i;
            }
            return null;
        }
    }

    public static enum ShowEffect {
        FADE_IN("fadeIn"), SLIDE_DOWN("slideDown");

        private final String jqmVal;

        private ShowEffect(String jqmVal) {
            this.jqmVal = jqmVal;
        }

        public String getJqmValue() {
            return jqmVal;
        }

        public static ShowEffect fromJqmValue(String jqmValue) {
            if (jqmValue == null || jqmValue.isEmpty()) return null;
            for (ShowEffect i : ShowEffect.values()) {
                if (i.getJqmValue().equals(jqmValue)) return i;
            }
            return null;
        }
    }

    public static enum HideEffect {
        FADE_OUT("fadeOut"), SLIDE_UP("slideUp");

        private final String jqmVal;

        private HideEffect(String jqmVal) {
            this.jqmVal = jqmVal;
        }

        public String getJqmValue() {
            return jqmVal;
        }

        public static HideEffect fromJqmValue(String jqmValue) {
            if (jqmValue == null || jqmValue.isEmpty()) return null;
            for (HideEffect i : HideEffect.values()) {
                if (i.getJqmValue().equals(jqmValue)) return i;
            }
            return null;
        }
    }

    private static final String ERROR_HEADER =
            "JQMButton OR JQMListItem can be used for tab headers, but not simultaneously!";

    private static final String DATA_ACTIVE = "data-active";
    private static final String ARIA_SELECTED = "aria-selected";

    private FlowPanel flow;
    private JQMNavBar navbar;
    private JQMList list;

    public JQMTabs() {
        flow = new FlowPanel();
        initWidget(flow);
        JQMCommon.setDataRole(this, "tabs");
        setStyleName("jqm4gwt-tabs");
    }

    private void clearActiveClicked() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Mobile.clearActiveClickedLink();
                doActiveHighlight(); // fix iOS tabs double highlighting in case of
            }                        // doBeforeActivate() false result (tabs switch prohibited)
        });
    }

    @UiChild(tagname = "button")
    public void addHeader(final JQMButton button) {
        if (list != null) throw new IllegalArgumentException(ERROR_HEADER);
        if (navbar == null) {
            navbar = new JQMNavBar();
            navbar.addStyleName("jqm4gwt-tabs-header-btn");
            flow.insert(navbar, 0);
            navbar.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    clearActiveClicked();
                }
            }, ClickEvent.getType());
        }
        navbar.add(button);
        if (navbar.getButtonCount() > 5) navbar.setColumns(5); // prevents default two column placing
        initHrefs();
    }

    public int getButtonCount() {
        return navbar != null ? navbar.getButtonCount() : 0;
    }

    public JQMButton getButton(int index) {
        if (navbar == null) return null;
        return navbar.getButton(index);
    }

    @UiChild(tagname = "listitem")
    public void addHeader(JQMListItem item) {
        if (navbar != null) throw new IllegalArgumentException(ERROR_HEADER);
        if (list == null) {
            list = new JQMList();
            list.setInset(true);
            list.addStyleName("jqm4gwt-tabs-header-list");
            flow.insert(list, 0);
            list.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    clearActiveClicked();
                }
            });
        }
        list.appendItem(item);
        initHrefs();
    }

    public int getListItemCount() {
        return list != null ? list.getItems().size() : 0;
    }

    public JQMListItem getListItem(int index) {
        if (list == null) return null;
        return list.getItem(index);
    }

    @UiChild(tagname = "content")
    public void addContent(final Widget widget) {
        widget.addStyleName("jqm4gwt-tabs-content");
        flow.add(widget);
        initHrefs();
    }

    private void initHrefs() {
        if (navbar == null && list == null) return;

        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < flow.getWidgetCount(); i++) {
            Widget w = flow.getWidget(i);
            if (w == navbar || w == list) continue;
            String id = w.getElement().getId();
            if (id == null || id.isEmpty()) {
                id = Document.get().createUniqueId();
                w.getElement().setId(id);
            }
            ids.add(id);
        }
        if (navbar != null) {
            for (int i = 0; i < navbar.getButtonCount(); i++) {
                if (i >= ids.size()) break;
                JQMButton btn = navbar.getButton(i);
                btn.setAttribute("data-ajax", "false");
                btn.setHref("#" + ids.get(i));
            }
        } else if (list != null) {
            List<JQMListItem> items = list.getItems();
            if (items != null) {
                int j = 0;
                for (int i = 0; i < items.size(); i++) {
                    JQMListItem li = items.get(i);
                    if (li == null) continue;
                    if (j >= ids.size()) break;
                    li.getElement().setAttribute("data-ajax", "false");
                    li.setHref("#" + ids.get(j));
                    j++;
                }
            }
        }
    }

    public IconPos getIconPos() {
        if (navbar == null) return null;
        return navbar.getIconPos();
    }

    /**
     * Works only in case of JQMButton were used for tab headers.
     *  Sets the position of the icons on tab headers.
     * If you desire an icon only button then set the position to IconPos.NOTEXT.
     */
    public void setIconPos(IconPos pos) {
       if (navbar != null) navbar.setIconPos(pos);
    }

    private static native void bindCreated(Element elt, JQMTabs tabs) /*-{
        $wnd.$(elt).on( 'tabscreate', function( event, ui ) {
            tabs.@com.sksamuel.jqm4gwt.toolbar.JQMTabs::created()();
        });
    }-*/;

    private static native void unbindCreated(Element elt) /*-{
        $wnd.$(elt).off( 'tabscreate' );
    }-*/;

    private void created() {
        // workaround for issue with listview active item resetting on initialization
        // (buttons are processed here as well just for symmetry).
        doActiveHighlight();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        Element elt = getElement();
        bindEvents(this, elt);
        bindCreated(elt, this);
    }

    @Override
    protected void onUnload() {
        Element elt = getElement();
        unbindEvents(elt);
        unbindCreated(elt);
        super.onUnload();
    }

    private static LIElement getBtnLi(JQMButton btn) {
        Element elt = btn.getElement().getParentElement();
        while (elt != null) {
            if (LIElement.is(elt)) {
                return elt.cast();
            }
            elt = elt.getParentElement();
        }
        return null;
    }

    private static boolean isSelected(Element elt) {
        if (elt == null) return false;
        String sel = JQMCommon.getAttribute(elt, ARIA_SELECTED);
        return sel != null && "true".equals(sel);
    }

    public void checkActiveHighlighted() {
        doActiveHighlight();
    }

    private void doActiveHighlight() {
        if (navbar != null) {
            for (int i = 0; i < navbar.getButtonCount(); i++) {
                JQMButton btn = navbar.getButton(i);
                LIElement li = getBtnLi(btn);
                if (li == null) continue;
                if (isSelected(li)) {
                    JQMCommon.setBtnActive(btn, true);
                    for (int j = 0; j < navbar.getButtonCount(); j++) {
                        if (i == j) continue;
                        JQMButton b = navbar.getButton(j);
                        if (JQMCommon.isBtnActive(b)) JQMCommon.setBtnActive(b, false);
                    }
                    break;
                }
            }
        } else if (list != null) {
            List<JQMListItem> items = list.getItems();
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    JQMListItem li = items.get(i);
                    if (li == null) continue;
                    if (isSelected(li.getElement())) {
                        li.setActiveHighlight(true);
                        for (int j = 0; j < items.size(); j++) {
                            if (i == j) continue;
                            JQMListItem liTmp = items.get(j);
                            if (liTmp == null) continue;
                            if (liTmp.isActiveHighlight()) liTmp.setActiveHighlight(false);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void clearHighlight() {
        if (navbar != null) {
            for (int j = 0; j < navbar.getButtonCount(); j++) {
                JQMButton b = navbar.getButton(j);
                if (JQMCommon.isBtnActive(b)) JQMCommon.setBtnActive(b, false);
            }
        } else if (list != null) {
            List<JQMListItem> items = list.getItems();
            if (items != null) {
                for (int j = 0; j < items.size(); j++) {
                    JQMListItem li = items.get(j);
                    if (li == null) continue;
                    if (li.isActiveHighlight()) li.setActiveHighlight(false);
                }
            }
        }
    }

    private static native void bindEvents(JQMTabs tabs, Element tabsElt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$(tabsElt).on("tabsactivate", function(event, ui) {
            tabs.@com.sksamuel.jqm4gwt.toolbar.JQMTabs::doActivate(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)
                    (ui.newTab.attr('id'), ui.oldTab.attr('id'), ui.newPanel.attr('id'), ui.oldPanel.attr('id'));
        });
        $wnd.$(tabsElt).on("tabsbeforeactivate", function(event, ui) {
            var v = tabs.@com.sksamuel.jqm4gwt.toolbar.JQMTabs::doBeforeActivate(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)
                    (ui.newTab.attr('id'), ui.oldTab.attr('id'), ui.newPanel.attr('id'), ui.oldPanel.attr('id'));
            if (v === false) {
                event.preventDefault();
                event.stopPropagation();
            }
        });
    }-*/;

    private static native void unbindEvents(Element tabsElt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$(tabsElt).off("tabsactivate");
        $wnd.$(tabsElt).off("tabsbeforeactivate");
    }-*/;

    protected String hrefToId(String href) {
        if (href == null || href.isEmpty()) return href;
        String s = href.trim();
        if (s.isEmpty()) return href;
        if (s.charAt(0) == '#') return s.substring(1);
        else return href;
    }

    protected List<Widget> findHeaders(boolean byHrefs, String... ids) {
        if (ids.length == 0) return null;
        Map<String, Widget> found = new HashMap<String, Widget>();

        if (list != null) {
            List<JQMListItem> items = list.getItems();
            for (int i = 0; i < items.size(); i++) {
                JQMListItem li = items.get(i);
                if (li == null) continue;
                for (String id : ids) {
                    if (id == null || id.isEmpty()) continue;
                    if (!byHrefs && id.equals(li.getElement().getId())
                            || byHrefs && id.equals(hrefToId(li.getHref()))) {
                        found.put(id, li);
                        break;
                    }
                }
            }
        } else if (navbar != null) {
            for (int i = 0; i < navbar.getButtonCount(); i++) {
                JQMButton btn = navbar.getButton(i);
                LIElement li = getBtnLi(btn);
                if (li == null) continue;
                for (String id : ids) {
                    if (id == null || id.isEmpty()) continue;
                    if (!byHrefs && id.equals(li.getId())
                            || byHrefs && id.equals(hrefToId(btn.getHref()))) {
                        found.put(id, btn);
                        break;
                    }
                }
            }
        }

        if (found.isEmpty()) return null;
        List<Widget> rslt = new ArrayList<Widget>(ids.length);
        for (String id : ids) {
            if (id == null || id.isEmpty()) rslt.add(null);
            else rslt.add(found.get(id));
        }
        return rslt;
    }

    public int getContentCount() {
        int cnt = 0;
        for (int i = 0; i < flow.getWidgetCount(); i++) {
            Widget w = flow.getWidget(i);
            if (w == navbar || w == list) continue;
            cnt++;
        }
        return cnt;
    }

    public Widget getContent(int index) {
        int cnt = 0;
        for (int i = 0; i < flow.getWidgetCount(); i++) {
            Widget w = flow.getWidget(i);
            if (w == navbar || w == list) continue;
            if (cnt == index) return w;
            cnt++;
        }
        return null;
    }

    public List<Widget> getContents() {
        ArrayList<Widget> rslt = null;
        for (int i = 0; i < flow.getWidgetCount(); i++) {
            Widget w = flow.getWidget(i);
            if (w == navbar || w == list) continue;
            if (rslt == null) rslt = new ArrayList<Widget>(flow.getWidgetCount());
            rslt.add(w);
        }
        return rslt;
    }

    public List<Widget> findContents(String... ids) {
        if (ids.length == 0) return null;
        Map<String, Widget> found = new HashMap<String, Widget>();
        for (int i = 0; i < flow.getWidgetCount(); i++) {
            Widget w = flow.getWidget(i);
            if (w == navbar || w == list) continue;
            String contentId = w.getElement().getId();
            for (String id : ids) {
                if (id == null || id.isEmpty()) continue;
                if (id.equals(contentId)) {
                    found.put(id, w);
                    break;
                }
            }
        }
        if (found.isEmpty()) return null;
        List<Widget> rslt = new ArrayList<Widget>(ids.length);
        for (String id : ids) {
            if (id == null || id.isEmpty()) rslt.add(null);
            else rslt.add(found.get(id));
        }
        return rslt;
    }

    /**
     *  If the tabs are currently collapsed, oldTabHeader and oldTabContent will be null.
     *   If the tabs are collapsing, newTabHeader and newTabContent will be null.
     *
     * @param newTabHeader - JQMButton or JQMListItem
     * @param oldTabHeader - JQMButton or JQMListItem
     * @param newTabContent - Widget
     * @param oldTabContent - Widget
     */
    protected void onActivate(Widget newTabHeader, Widget oldTabHeader,
                              Widget newTabContent, Widget oldTabContent) {
    }

    /**
     *  If the tabs are currently collapsed, oldTabHeader and oldTabContent will be null.
     *   If the tabs are collapsing, newTabHeader and newTabContent will be null.
     *
     * @param newTabHeader - JQMButton or JQMListItem
     * @param oldTabHeader - JQMButton or JQMListItem
     * @param newTabContent - Widget
     * @param oldTabContent - Widget
     */
    protected boolean onBeforeActivate(Widget newTabHeader, Widget oldTabHeader,
                                       Widget newTabContent, Widget oldTabContent) {
        return true;
    }

    protected void execOnActivate(String newTabId, String oldTabId,
                                  String newPanelId, String oldPanelId) {
        List<Widget> headers = findHeaders(false/*byHrefs*/, newTabId, oldTabId);
        if (headers == null) headers = findHeaders(true/*byHrefs*/, newPanelId, oldPanelId);
        List<Widget> contents = findContents(newPanelId, oldPanelId);
        if (headers == null || contents == null) return;
        onActivate(headers.get(0), headers.get(1), contents.get(0), contents.get(1));
        JQMTabsEvent.fire(this, TabsState.ACTIVATE, headers.get(0), headers.get(1),
                contents.get(0), contents.get(1));
    }

    protected boolean execOnBeforeActivate(String newTabId, String oldTabId,
                                           String newPanelId, String oldPanelId) {
        List<Widget> headers = findHeaders(false/*byHrefs*/, newTabId, oldTabId);
        if (headers == null) headers = findHeaders(true/*byHrefs*/, newPanelId, oldPanelId);
        List<Widget> contents = findContents(newPanelId, oldPanelId);
        if (headers == null || contents == null) return true;
        try {
            JQMTabsEvent.fire(this, TabsState.BEFORE_ACTIVATE, headers.get(0), headers.get(1),
                    contents.get(0), contents.get(1));
        } catch (Exception e) {
            return false; // tabs should not be switched
        }
        return onBeforeActivate(headers.get(0), headers.get(1), contents.get(0), contents.get(1));
    }

    protected void doActivate(String newTabId, String oldTabId, String newPanelId, String oldPanelId) {
        execOnActivate(newTabId, oldTabId, newPanelId, oldPanelId);

        final String activeTabId;
        final String activePanId;

        if ((newTabId == null || newTabId.isEmpty()) && (oldTabId == null || oldTabId.isEmpty())) {
            activeTabId = null;
            final String s;
            if (isCollapsible()) {
                s = newPanelId != null && !newPanelId.isEmpty() ? newPanelId : oldPanelId;
            } else {
                s = newPanelId;
            }
            activePanId = s != null && !s.isEmpty() ? s : null;
        } else {
            activePanId = null;
            final String s;
            if (isCollapsible()) {
                s = newTabId != null && !newTabId.isEmpty() ? newTabId : oldTabId;
            } else {
                s = newTabId;
            }
            activeTabId = s != null && !s.isEmpty() ? s : null;
        }

        if (activeTabId != null || activePanId != null) {
            if (list != null) {
                List<JQMListItem> items = list.getItems();
                for (int i = 0; i < items.size(); i++) {
                    JQMListItem li = items.get(i);
                    if (li == null) continue;
                    final boolean act;
                    if (activeTabId != null) {
                        act = activeTabId.equals(li.getElement().getId());
                    } else if (activePanId != null) {
                        act = activePanId.equals(hrefToId(li.getHref()));
                    } else {
                        continue;
                    }
                    if (li.isActiveHighlight() != act) li.setActiveHighlight(act);
                }
            } else if (navbar != null) {
                for (int i = 0; i < navbar.getButtonCount(); i++) {
                    JQMButton btn = navbar.getButton(i);
                    LIElement li = getBtnLi(btn);
                    if (li == null) continue;
                    final boolean act;
                    if (activeTabId != null) {
                        act = activeTabId.equals(li.getId());
                    } else if (activePanId != null) {
                        act = activePanId.equals(hrefToId(btn.getHref()));
                    } else {
                        continue;
                    }
                    if (JQMCommon.isBtnActive(btn) != act) JQMCommon.setBtnActive(btn, act);
                }
            }
        }
    }

    protected boolean doBeforeActivate(String newTabId, String oldTabId, String newPanelId, String oldPanelId) {
        boolean rslt = execOnBeforeActivate(newTabId, oldTabId, newPanelId, oldPanelId);
        if (!rslt) clearActiveClicked(); // fix iOS tabs double highlighting
        return rslt;
    }

    public boolean isCollapsible() {
        String s = JQMCommon.getAttribute(this, "data-collapsible");
        return s != null && "true".equals(s);
    }

    /**
     * @param value - when true, the active panel can be closed.
     */
    public void setCollapsible(boolean value) {
        JQMCommon.setAttribute(this, "data-collapsible", value ? "true" : null);
    }

    public int getActiveIndex() {
        Element elt = getElement();
        if (isReady(elt)) {
            return getActive(elt);
        } else {
            String s = JQMCommon.getAttribute(this, DATA_ACTIVE);
            if (s == null || s.isEmpty()) return 0;
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    /**
     * @return - true if there is active tab/index (collapsible tabs may have no active tab/index).
     */
    public boolean isActiveIndex() {
        Element elt = getElement();
        if (isReady(elt)) {
            return isActive(elt);
        } else {
            String s = JQMCommon.getAttribute(this, DATA_ACTIVE);
            if (s == null || s.isEmpty()) return false;
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    /**
     * @param tabIndex - the zero-based index of the tab/panel that is active (open).
     * A negative value selects tabs/panels going backward from the last one.
     */
    public void setActiveIndex(int tabIndex) {
        JQMCommon.setAttribute(this, DATA_ACTIVE, String.valueOf(tabIndex));
        Element elt = getElement();
        if (isReady(elt)) {
            setActive(elt, tabIndex);
        }
    }

    private static native void setActive(Element elt, int index) /*-{
        $wnd.$(elt).tabs("option", "active", index);
    }-*/;

    private static native void removeActive(Element elt) /*-{
        $wnd.$(elt).tabs("option", "active", false);
    }-*/;

    private static native int getActive(Element elt) /*-{
        var v = $wnd.$(elt).tabs("option", "active");
        if (v === false) return 0;
        else return v;
    }-*/;

    private static native boolean isActive(Element elt) /*-{
        var v = $wnd.$(elt).tabs("option", "active");
        if (v === false) return false;
        else return true;
    }-*/;

    public void removeActiveIndex() {
        JQMCommon.setAttribute(this, DATA_ACTIVE, null);
        Element elt = getElement();
        if (isReady(elt)) {
            removeActive(elt);
            clearHighlight();
        }
    }

    public boolean isCollapseAll() {
        String s = JQMCommon.getAttribute(this, DATA_ACTIVE);
        return s != null && "false".equals(s);
    }

    /**
     * @param value - true will collapse all panels. This requires the collapsible option to be true.
     */
    public void setCollapseAll(boolean value) {
        if (value) JQMCommon.setAttribute(this, DATA_ACTIVE, "false");
        else JQMCommon.setAttribute(this, DATA_ACTIVE, null);
    }

    public String getTabDisabled() {
        return JQMCommon.getAttribute(this, "data-disabled");
    }

    /**
     * @param value - Multiple types supported:
     *  Boolean: Enable or disable all tabs.
     *  Array: An array containing the zero-based indexes of the tabs that should be disabled,
     * e.g., [ 0, 2 ] would disable the first and third tab.
     */
    public void setTabDisabled(String value) {
        JQMCommon.setAttribute(this, "data-disabled", value);
    }

    public String getTabEvent() {
        return JQMCommon.getAttribute(this, "data-event");
    }

    /**
     * @param value - The type of event that the tabs should react to in order to activate the tab.
     * To activate on hover, use "mouseover" {@link JQMTabs#ACTIVATE_TAB_ON_HOVER}.
     *  Default value is "click" {@link JQMTabs#ACTIVATE_TAB_ON_CLICK} .
     */
    public void setTabEvent(String value) {
        JQMCommon.setAttribute(this, "data-event", value);
    }

    public HeightStyle getHeightStyle() {
        String s = JQMCommon.getAttribute(this, "data-height-style");
        return HeightStyle.fromJqmValue(s);
    }

    public void setHeightStyle(HeightStyle value) {
        JQMCommon.setAttribute(this, "data-height-style", value != null ? value.getJqmValue() : null);
    }

    /**
     * Process any tabs that were added or removed directly in the DOM and recompute
     * the height of the tab panels. Results depend on the content and the heightStyle option.
     */
    public void refresh() {
        refresh(getElement());
    }

    private static native void refresh(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('ui-tabs') !== undefined) {
            w.tabs('refresh');
        }
    }-*/;

    private static native boolean isReady(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return false; // jQuery is not loaded
        var w = $wnd.$(elt);
        return w.data('ui-tabs') !== undefined;
    }-*/;

    /**
     * Initialize/enhance dynamically created JQMTabs, refresh() is not working in dynamic case.
     */
    public void renderTabs() {
        renderTabs(getElement());
        doActiveHighlight();
    }

    private static native void renderTabs(Element elt) /*-{
        $wnd.$(elt).tabs().enhanceWithin();
    }-*/;

    public HandlerRegistration addTabsHandler(JQMTabsEvent.Handler handler) {
        return addHandler(handler, JQMTabsEvent.getType());
    }

    public ShowEffect getShowEffect() {
        return ShowEffect.fromJqmValue(JQMCommon.getAttribute(this, "data-show"));
    }

    public void setShowEffect(ShowEffect value) {
        JQMCommon.setAttribute(this, "data-show", value != null ? value.getJqmValue() : null);
    }

    public HideEffect getHideEffect() {
        return HideEffect.fromJqmValue(JQMCommon.getAttribute(this, "data-hide"));
    }

    public void setHideEffect(HideEffect value) {
        JQMCommon.setAttribute(this, "data-hide", value != null ? value.getJqmValue() : null);
    }
}
