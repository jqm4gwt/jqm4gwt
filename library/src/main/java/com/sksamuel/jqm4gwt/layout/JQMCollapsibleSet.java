package com.sksamuel.jqm4gwt.layout;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasIconPos;
import com.sksamuel.jqm4gwt.HasInset;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 10:44:29
 *         <p/>
 *         An implementation of a group or set of collapsibles. When
 *         {@link JQMCollapsible} widgets are placed inside a
 *         {@link JQMCollapsibleSet} they behave like an accordian widget - that
 *         is only one can be open at any time. If a user opens another
 *         collapsible panel, then any others will be closed automatically.
 * @link http://jquerymobile.com/demos/1.2.0/docs/content/content-collapsible-set.html
 */
public class JQMCollapsibleSet extends JQMWidget implements HasIconPos, HasMini, HasInset<JQMCollapsibleSet> {

    private final FlowPanel flow;

    public JQMCollapsibleSet() {
        flow = new FlowPanel();
        initWidget(flow);
        setDataRole("collapsible-set");
        setId();
    }

    /**
     * Adds the given collapsible to the end of this set
     */
    @UiChild(tagname = "collapsible")
    public void add(JQMCollapsible c) {
        flow.add(c);
    }

    public String getCollapsedIcon() {
        return getAttribute("data-collapsed-icon");
    }

    public String getContentTheme() {
        return getAttribute("data-content-theme");
    }

    public String getExpandedIcon() {
        return getAttribute("data-expanded-icon");
    }

    @Override
    public IconPos getIconPos() {
        String string = getAttribute("data-iconpos");
        return string == null ? null : IconPos.valueOf(string);
    }

    @Override
    public boolean isInset() {
        return getAttributeBoolean("data-inset");
    }

    @Override
    public boolean isMini() {
        return "true".equals(getAttribute("data-mini"));
    }

    /**
     * Removes the given collapsible from this set
     */
    public void remove(JQMCollapsible c) {
        flow.remove(c);
    }

    public void removeCollapsedIcon() {
        getElement().removeAttribute("data-collapsed-icon");
    }

    public void removeExpandedIcon() {
        getElement().removeAttribute("data-expanded-icon");
    }

    public void setCollapsedIcon(DataIcon icon) {
        setAttribute("data-collapsed-icon", icon.getJqmValue());
    }

    public void setContentTheme(String theme) {
        setAttribute("data-content-theme", theme);
    }

    public void setExpandedIcon(DataIcon icon) {
        getElement().setAttribute("data-expanded-icon", icon.getJqmValue());
    }

    /**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    @Override
    public JQMCollapsibleSet setIconPos(IconPos pos) {
        if (pos == null)
            getElement().removeAttribute("data-iconpos");
        else
            getElement().setAttribute("data-iconpos", pos.getJqmValue());
        return this;
    }

    @Override
    public JQMCollapsibleSet setInset(boolean inset) {
        setAttribute("data-inset", String.valueOf(inset));
        return this;
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMCollapsibleSet setMini(boolean mini) {
        setAttribute("data-mini", String.valueOf(mini));
        return this;
    }

}
