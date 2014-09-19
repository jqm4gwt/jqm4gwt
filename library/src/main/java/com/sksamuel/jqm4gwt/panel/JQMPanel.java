package com.sksamuel.jqm4gwt.panel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasId;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.form.elements.JQMSelect;
import com.sksamuel.jqm4gwt.list.JQMListItem;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 22:13:36
 * <p/>
 * A panel that contains multiple child widgets. The child widgets can
 * be either {@link JQMWidget} widgets or regular GWT {@link Widget} widgets.
 * <p/>
 * The panel can use any HTML element type as the containing element. So
 * for example, this class can be used by {@link JQMListItem} as an &lt;li>
 * element panel, or by {@link JQMSelect} as a &lt;select> element panel.
 * <p/>
 * This is the reason for the existence of this class. Jquery mobile
 * uses many elements as containers for other elements. GWT does not
 * natively support panels other than div based panels.
 * <p/>
 * This panel implements {@link HasTheme} but this does not necessarily
 * have any effect. It depends on where the panel is being used.
 * <p/>
 * Typcially this class will only be used for implementing JQM Widgets
 * and users of the jqm4gwt framework will want to use a normal GWT
 * {@link Panel} instance.
 */
public class JQMPanel extends ComplexPanel implements HasId<JQMPanel>, HasTheme<JQMPanel> {

    /**
     * Creates a new {@link JQMPanel} with a given element.
     *
     * @param element the element to use as the container for this panel. Must
     *                not be null.
     */
    public JQMPanel(Element element) {
        this(element, null, null);
    }

    /**
     * Creates a new {@link JQMPanel} with a given element and datarole.
     *
     * @param element  the element to use as the container for this panel. Must
     *                 not be null.
     * @param dataRole the value of the data-role attribute to set. Can be null.
     */
    public JQMPanel(Element element, String dataRole) {
        this(element, dataRole, null);
    }

    /**
     * Creates a new {@link JQMPanel} with a given element, datarole and
     * stylename.
     *
     * @param element   the element to use as the container for this panel. Must
     *                  not be null.
     * @param dataRole  the value of the data-role attribute to set. Can be null.
     * @param styleName the value of the class attribute to set. Can be null
     */
    public JQMPanel(Element element, String dataRole, String styleName) {
        setElement(element);
        if (styleName != null)
            setStyleName(styleName);
        if (dataRole != null)
            setDataRole(dataRole);
        setId();
    }

    @Override
    public void add(Widget w) {
        Element elt = getElement();
        add(w, elt);
    }

    @Override
    public void clear() {
        Node child = getElement().getFirstChild();
        while (child != null) {
            getElement().removeChild(child);
            child = getElement().getFirstChild();
        }
    }

    protected String getAttribute(String name) {
        return getElement().getAttribute(name);
    }

    @Override
    public String getId() {
        return getElement().getId();
    }

    @Override
    public final String getTheme() {
        return getAttribute("data-theme");
    }

    public void hide() {
        hide(getElement());
    }

    private static native void hide(Element elt) /*-{
        $wnd.$(elt).hide();
    }-*/;

    public void insert(IsWidget w, int beforeIndex) {
        insert(asWidgetOrNull(w), beforeIndex);
    }

    public void insert(Widget w, int beforeIndex) {
        Element elt = getElement();
        insert(w, elt, beforeIndex, true);
    }

    protected void removeAttribute(String name) {
        getElement().removeAttribute(name);
    }

    protected void setAttribute(String name, String value) {
        getElement().setAttribute(name, value);
    }

    /**
     * Sets the data-role attribute to the given value.
     */
    protected void setDataRole(String role) {
        setAttribute("data-role", role);
    }

    /**
     * Assign an automatically generated id
     */
    protected void setId() {
        withId(Document.get().createUniqueId());
    }

    @Override
    public void setId(String id) {
        getElement().setId(id);
    }

    @Override
    public final JQMPanel withId(String id) {
        setId(id);
        return this;
    }

    @Override
    public final void setTheme(String themeName) {
        setAttribute("data-theme", themeName);
    }

    @Override
    public final JQMPanel withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    public void show() {
        show(getElement());
    }

    private static native void show(Element elt) /*-{
        $wnd.$(elt).show();
    }-*/;
}
