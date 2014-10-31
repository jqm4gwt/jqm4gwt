package com.sksamuel.jqm4gwt.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIconPos;
import com.sksamuel.jqm4gwt.HasInset;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 10:44:29
 * <p/>
 * An implementation of a group or set of collapsibles.
 * When {@link JQMCollapsible} widgets are placed inside a {@link JQMCollapsibleSet}
 * they behave like an accordion widget - that is only one can be open at any time.
 * If a user opens another collapsible panel, then any others will be closed automatically.
 *
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/collapsibleset/">Collapsible set</a>
 */
public class JQMCollapsibleSet extends JQMWidget implements HasIconPos<JQMCollapsibleSet>,
        HasMini<JQMCollapsibleSet>, HasInset<JQMCollapsibleSet>, HasCorners<JQMCollapsibleSet> {

    private final FlowPanel flow;

    public JQMCollapsibleSet() {
        flow = new FlowPanel();
        initWidget(flow);
        setDataRole("collapsibleset");
        setId();
    }

    /**
     * Adds the given collapsible to the end of this set
     */
    @UiChild(tagname = "collapsible")
    public void add(JQMCollapsible c) {
        flow.add(c);
    }

    public int getCollapsibleCount() {
        return flow.getWidgetCount();
    }

    public JQMCollapsible getCollapsible(int index) {
        return (JQMCollapsible) flow.getWidget(index);
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

    public JQMCollapsibleSet removeCollapsedIcon() {
        removeAttribute("data-collapsed-icon");
        return this;
    }

    public JQMCollapsibleSet removeExpandedIcon() {
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

    @Override
    public boolean isInset() {
        return getAttributeBoolean("data-inset");
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(this);
    }

    /**
     * Removes the given collapsible from this set
     */
    public void remove(JQMCollapsible c) {
        flow.remove(c);
    }

    public void setContentTheme(String theme) {
        setAttribute("data-content-theme", theme);
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
    public JQMCollapsibleSet withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

    @Override
    public void setInset(boolean inset) {
        setAttribute("data-inset", String.valueOf(inset));
    }

    @Override
    public JQMCollapsibleSet withInset(boolean inset) {
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
    public JQMCollapsibleSet withMini(boolean mini) {
        setMini(mini);
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
    public JQMCollapsibleSet withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    public void refresh() {
        refresh(getElement());
    }

    private static native void refresh(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-collapsibleset') !== undefined) {
            w.collapsibleset('refresh');
        }
    }-*/;

}
