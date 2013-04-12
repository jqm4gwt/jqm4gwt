package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Collection;

/**
 * @author Stephen K Samuel samspade79@gmail.com 16 Sep 2012 00:22:18
 *         <p/>
 *         A JQMContainer is any "page level" widget, such as dialogs, popups and pages themselves, which are directly attached to the DOM.
 *         It is meant to contain the common functionality for showing these containers.
 */
public abstract class JQMContainer extends ComplexPanel implements HasId<JQMContainer>, HasTheme<JQMContainer>, HasTransition<JQMContainer> {

    public static native void triggerCreate() /*-{
                                $wnd.$('body').trigger('create');
								}-*/;

    /**
     * Set to true once the container has been enhanced by jQuery Mobile.
     */
    private boolean enhanced;

    protected final String id;

    protected JQMContainer(String id, String role) {
        if (id == null)
            throw new RuntimeException("id for page cannot be null");
        if (id.contains(" "))
            throw new RuntimeException("id for page cannot contain space");
        this.id = id;
        setElement(Document.get().createDivElement());
        setAttribute("data-url", id);
        setAttribute("data-role", role);
        setStyleName("jqm4gwt-" + role);
    }

    /**
     * Adds the given collection of widgets to the primary content panel of this container
     */
    public void add(Collection<Widget> widgets) {
        for (Widget widget : widgets)
            add(widget);
    }

    @Override
    public void add(Widget w) {
        add(w, getElement());
    }

    /**
     * Adds the given array of widgets to the primary content container of
     * this container.
     *
     * @param widgets the widgets to add to the primary content
     */
    public void add(Widget[] widgets) {
        for (Widget widget : widgets)
            add(widget);
    }

    /**
     * Returns the value of the attribute with the given name
     */
    protected String getAttribute(String name) {
        return getElement().getAttribute(name);
    }

    public String getDataUrl() {
        return getAttribute("data-url");
    }

    @Override
    public String getId() {
        return id;
    }

    public String getRelType() {
        return null;
    }

    @Override
    public String getTheme() {
        return getElement().getAttribute("data-theme");
    }

    @Override
    public Transition getTransition() {
        String value = getElement().getAttribute("data-transition");
        return value == null ? null : Transition.valueOf(value);
    }

    public boolean isEnhanced() {
        return enhanced;
    }

    /**
     * Removes the attribute with the given name
     *
     * @param name the name of the attribute to remove
     */
    protected void removeAttribute(String name) {
        getElement().removeAttribute(name);
    }

    /**
     * Sets the value of the attribute with the given name to the given value.
     */
    protected void setAttribute(String name, String value) {
        getElement().setAttribute(name, value);
    }

    public void setEnhanced(boolean enchanced) {
        this.enhanced = enchanced;
    }

    @Override
    public JQMContainer setId(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JQMContainer setTheme(String themeName) {
        getElement().setAttribute("data-theme", themeName);
        return this;
    }

    @Override
    public JQMContainer setTransition(Transition transition) {
        getElement().setAttribute("data-transition", transition.getJQMValue());
        return this;
    }
}
