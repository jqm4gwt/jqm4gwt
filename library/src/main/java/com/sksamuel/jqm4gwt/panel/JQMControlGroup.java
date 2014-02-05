package com.sksamuel.jqm4gwt.panel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasOrientation;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.Orientation;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 22:09:41
 *         <p/>
 *         An implementation of {@link JQMPanel} for control groups.
 */
public class JQMControlGroup extends JQMPanel implements HasOrientation<JQMControlGroup>,
        HasMini<JQMControlGroup>, HasCorners<JQMControlGroup> {

    protected JQMControlGroup(Element element, String styleName) {
        super(element, "controlgroup", styleName);
    }

    public JQMControlGroup() {
        this(Document.get().createDivElement(), "jqm4gwt-controlgroup");
    }

    public JQMControlGroup(Widget... widgets) {
        this();
        for (Widget w : widgets) {
            add(w);
        }
    }

    @Override
    public boolean isHorizontal() {
        return "true".equals(getAttribute("data-type"));
    }

    @Override
    public boolean isMini() {
        return "true".equals(getAttribute("data-mini"));
    }

    @Override
    public boolean isVertical() {
        return !isHorizontal();
    }

    @Override
    public void setHorizontal() {
        setAttribute("data-type", "horizontal");
    }

    @Override
    public JQMControlGroup withHorizontal() {
        setHorizontal();
        return this;
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        setAttribute("data-mini", String.valueOf(mini));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMControlGroup withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public void setVertical() {
        removeAttribute("data-type");
    }

    @Override
    public JQMControlGroup withVertical() {
        setVertical();
      	return this;
    }

    public void setOrientation(Orientation value) {
        HasOrientation.Support.setOrientation(this, value);
    }

    public Orientation getOrientation() {
        return HasOrientation.Support.getOrientation(this);
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
    public JQMControlGroup withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    protected native void refresh(String id) /*-{
        $wnd.$("#" + id).controlgroup("refresh");
    }-*/;

    /**
     * After dynamic changes to widgets, can be called in conjunction with JQMContext.render().
     * <p/> Example:
     * <p/> JQMContext.render(grp.getElement().getId());
     * <p/> grp.refresh();
     */
    public void refresh() {
        refresh(getElement().getId());
    }
}
