package com.sksamuel.jqm4gwt.panel;

import com.google.gwt.dom.client.Element;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasOrientation;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 22:09:41
 *         <p/>
 *         An implementation of {@link JQMPanel} for control groups.
 */
public class JQMControlGroup extends JQMPanel implements HasOrientation<JQMControlGroup>, HasMini<JQMControlGroup> {

    protected JQMControlGroup(Element element, String styleName) {
        super(element, "controlgroup", styleName);
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
}
