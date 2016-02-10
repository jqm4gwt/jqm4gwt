package com.sksamuel.jqm4gwt.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasIconPos;
import com.sksamuel.jqm4gwt.HasInset;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMContainer;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;
import com.sksamuel.jqm4gwt.html.Heading;
import com.sksamuel.jqm4gwt.layout.JQMCollapsibleEvent.CollapsibleState;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 May 2011 00:04:18
 * <br>
 * A {@link JQMCollapsible} is a panel that shows a header and can reveal content
 * once the header is expanded. This is similar to the GWT {@link DisclosurePanel}.
 *
 * <br> See <a href="http://demos.jquerymobile.com/1.4.5/collapsible/">Collapsible</a>
 */
public class JQMCollapsible extends JQMContainer implements HasText<JQMCollapsible>,
        HasIconPos<JQMCollapsible>, HasMini<JQMCollapsible>, HasInset<JQMCollapsible> {

    private final Heading header;

    private CustomFlowPanel headerPanel;

    /**
     * Creates a new {@link JQMCollapsible} with the no header text and
     * preset to collapsed.
     */
    public JQMCollapsible() {
        this(null, true);
    }

    /**
     * Creates a new {@link JQMCollapsible} with the given header text and
     * preset to collapsed.
     */
    public JQMCollapsible(String text) {
        this(text, true);
    }

    /**
     * Creates a new {@link JQMCollapsible} with the given header text and
     * collapsed if param collapsed is true.
     * <br>
     * The created header will use a h3 element.
     *
     * @param collapsed if true then the {@link JQMCollapsible} will be collapsed
     *                  by default, if false it will be open by default
     */
    public JQMCollapsible(String text, boolean collapsed) {
        this(text, 3, collapsed);
    }

    /**
     * Creates a new {@link JQMCollapsible} with the given header text and
     * collapsed if param collapsed is true.
     * <br>
     * The created header will use a &lt;hN&gt; element where N is determined by the param headerN.
     * <br>
     * Once the {@link JQMCollapsible} has been created it is not possible to
     * change the &lt;hN&gt; tag used for the header.
     */
    public JQMCollapsible(String text, int headerN, boolean collapsed) {

        header = new Heading(headerN);
        add(header);

        setRole("collapsible");
        setCollapsed(collapsed);
        setText(text);
    }

    public HandlerRegistration addCollapsibleHandler(JQMCollapsibleEvent.Handler handler) {
        return addHandler(handler, JQMCollapsibleEvent.getType());
    }

    protected void onExpanded() {
    }

    protected void onCollapsed() {
    }

    protected void doExpanded() {
        onExpanded();
        JQMCollapsibleEvent.fire(this, CollapsibleState.EXPANDED);
    }

    protected void doCollapsed() {
        onCollapsed();
        JQMCollapsibleEvent.fire(this, CollapsibleState.COLLAPSED);
    }

    private static native void bindLifecycleEvents(JQMCollapsible collap, Element collapElt) /*-{
        var p = $wnd.$(collapElt);
        p.on("collapsibleexpand", function(event, ui) {
            collap.@com.sksamuel.jqm4gwt.layout.JQMCollapsible::doExpanded()();
        });
        p.on("collapsiblecollapse", function(event, ui) {
            collap.@com.sksamuel.jqm4gwt.layout.JQMCollapsible::doCollapsed()();
        });
    }-*/;

    private static native void unbindLifecycleEvents(Element collapElt) /*-{
        var p = $wnd.$(collapElt);
        p.off("collapsiblecollapse");
        p.off("collapsibleexpand");
    }-*/;

    @Override
    protected void onLoad() {
        super.onLoad();
        bindLifecycleEvents(this, getElement());
    }

    @Override
    protected void onUnload() {
        unbindLifecycleEvents(getElement());
        super.onUnload();
    }

    /**
     * Add a widget to the content part of this {@link JQMCollapsible} instance.
     */
    @Override
    @UiChild(tagname="widget")
    public void add(Widget widget) {
        super.add(widget);
    }

    @UiChild(tagname="headerWidget")
    public void addHeaderWidget(Widget w) {
        if (w == null) return;
        if (headerPanel == null) {
            header.getElement().setInnerHTML(null);
            headerPanel = new CustomFlowPanel(header.getElement());
            add(headerPanel);
        }
        headerPanel.add(w);
    }

    /**
     * Removes all Widgets from the content part of this {@link JQMCollapsible} instance.
     */
    @Override
    public void clear() {
        super.clear();
    }

    public DataIcon getCollapsedIcon() {
        return DataIcon.fromJqmValue(getAttribute("data-collapsed-icon"));
    }

    public void setCollapsedIcon(DataIcon icon) {
        setAttribute("data-collapsed-icon", icon != null ? icon.getJqmValue() : null);
    }

    public DataIcon getExpandedIcon() {
        return DataIcon.fromJqmValue(getAttribute("data-expanded-icon"));
    }

    public void setExpandedIcon(DataIcon icon) {
        setAttribute("data-expanded-icon", icon != null ? icon.getJqmValue() : null);
    }

    public JQMCollapsible removeCollapsedIcon() {
        removeAttribute("data-collapsed-icon");
        return this;
    }

    public JQMCollapsible removeExpandedIcon() {
        removeAttribute("data-expanded-icon");
        return this;
    }

    public String getContentTheme() {
        return getAttribute("data-content-theme");
    }

    @Override
    public IconPos getIconPos() {
        return JQMCommon.getIconPos(this);
    }

    /**
     * Returns the text on the header element
     */
    @Override
    public String getText() {
        return header.getText();
    }

    /**
     * Returns true if this {@link JQMCollapsible} is currently collapsed.
     */
    public boolean isCollapsed() {
        return getAttributeBoolean("data-collapsed");
    }

    /**
     * Programmatically set the collapsed state of this widget.
     */
    public void setCollapsed(boolean collapsed) {
        if (collapsed) removeAttribute("data-collapsed");
        else setAttribute("data-collapsed", "false");
    }

    /**
     * Programmatically set the collapsed state of this widget.
     */
    public JQMCollapsible withCollapsed(boolean collapsed) {
        setCollapsed(collapsed);
        return this;
    }

    @Override
    public boolean isInset() {
        return getAttributeBoolean("data-inset");
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(this);
    }

    /**
     * Removes a widget from the content part of this {@link JQMCollapsible}
     * instance.
     *
     * @return true if the widget was removed
     */
    @Override
    public boolean remove(Widget widget) {
        return super.remove(widget);
    }

    public JQMCollapsible setContentTheme(String theme) {
        setAttribute("data-content-theme", theme);
        return this;
    }

    /**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    @Override
    public void setIconPos(IconPos pos) {
        JQMCommon.setIconPos(this, pos);
    }

    /**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    @Override
    public JQMCollapsible withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

    @Override
    public void setInset(boolean inset) {
        setAttribute("data-inset", String.valueOf(inset));
    }

    @Override
    public JQMCollapsible withInset(boolean inset) {
        setInset(inset);
        return this;
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(this, mini);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMCollapsible withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    /**
     * Sets the text on the header element
     */
    @Override
    public void setText(String text) {
        if (headerPanel != null) {
            super.remove(headerPanel);
            headerPanel = null;
        }
        header.setText(text);
    }

    @Override
    public JQMCollapsible withText(String text) {
        setText(text);
        return this;
    }

    public void expand() {
        execExpand(getElement());
    }

    public void collapse() {
        execCollapse(getElement());
    }

    private static native void execExpand(Element elt) /*-{
        $wnd.$(elt).collapsible("expand");
    }-*/;

    private static native void execCollapse(Element elt) /*-{
        $wnd.$(elt).collapsible("collapse");
    }-*/;

    /** Needed for header widgets to prevent expand/collapse on their clicks. */
    public void discardHeaderClick(ClickEvent event) {
        if (event == null) return;

        // Example: we use radioset on collapsible header, so stopPropagation() is needed
        // to suppress collapsible open/close behavior.
        // But preventDefault() is not needed, otherwise radios won't switch.
        // event.preventDefault(); // For example, clicked anchors will not take the browser to a new URL

        event.stopPropagation();
        makeHeaderInactive(header.getElement());
    }

    /** On iOS if placed on header button is pressed then header remains in "pressed" state. */
    private static native void makeHeaderInactive(Element header) /*-{
        $wnd.$(header).find("a.ui-btn").first().removeClass($wnd.$.mobile.activeBtnClass);
    }-*/;
}
