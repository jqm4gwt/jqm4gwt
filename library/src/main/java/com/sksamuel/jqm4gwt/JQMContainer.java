package com.sksamuel.jqm4gwt;

import java.util.Collection;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.events.HasJQMEventHandlers;
import com.sksamuel.jqm4gwt.events.JQMEventFactory;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;

/**
 * @author Stephen K Samuel samspade79@gmail.com 16 Sep 2012 00:22:18
 * <p/>
 * A JQMContainer is any "page level" widget, such as dialogs, popups and pages themselves,
 * which are directly attached to the DOM.
 * It is meant to contain the common functionality for showing these containers.
 */
public abstract class JQMContainer extends ComplexPanel implements HasId<JQMContainer>,
        HasTheme<JQMContainer>, HasTransition<JQMContainer>, HasCorners<JQMContainer>,
        HasJQMEventHandlers {

    private static int counter = 1;
    private static final String AUTOINC_SUFFIX = "++";

    /**
     * Set to true once the container has been enhanced by jQuery Mobile.
     */
    private boolean enhanced;

    protected String id;

    protected JQMContainer() {
        setElement(Document.get().createDivElement());
    }

    protected JQMContainer(String id, String role) {
        this();
        setContainerId(id);
        setRole(role);
    }

    protected void setRole(String role) {
        String s = JQMCommon.getDataRole(this);
        if (s != null && !s.isEmpty()) removeStyleName("jqm4gwt-" + s);
        JQMCommon.setDataRole(this, role);
        addStyleName("jqm4gwt-" + role);
    }

    private static String generateContainerId() {
        return "container" + (counter++);
    }

    /**
     * Assigns a default containerId of 'container' followed by the instance number.
     * @return the instance being operated on as part of a Fluent API
     */
    public JQMContainer withContainerId() {
        setContainerId(generateContainerId());
        return this;
    }

    /**
     * Sets the containerId so it can be referenced by name.
     * @param containerId
     */
    public void setContainerId(String containerId) {
        if (containerId == null)
            throw new IllegalArgumentException("id for JQMContainer cannot be null");
        if (containerId.contains(" "))
            throw new IllegalArgumentException("id for JQMContainer cannot contain space");

        if (containerId.isEmpty()) {
            this.id = generateContainerId();
        } else if (containerId.endsWith(AUTOINC_SUFFIX)) {
            this.id = containerId.substring(0, containerId.length() - AUTOINC_SUFFIX.length()) + (counter++);
        } else {
            this.id = containerId;
        }

        getElement().setId(this.id);
        setAttribute("data-url", this.id);
    }

    public String getContainerId() {
        return this.id;
    }

    /**
     * Sets the containerId so it can be referenced by name.
     * @return the instance being operated on as part of a Fluent API
     */
    public JQMContainer withContainerId(String containerId) {
        setContainerId(containerId);
        return this;
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
        Element elt = getElement();
        add(w, elt);
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
        return JQMCommon.getTheme(this);
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
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JQMContainer withId(String id) {
        setId(id);
        return this;
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setTheme(this, themeName);
    }

    @Override
    public JQMContainer withTheme(String themeName) {
        setTheme(themeName);
           return this;
    }

    @Override
    public Transition getTransition() {
        return JQMCommon.getTransition(getElement());
    }

    @Override
    public void setTransition(Transition transition) {
        JQMCommon.setTransition(getElement(), transition);
    }

    @Override
    public JQMContainer withTransition(Transition transition) {
        setTransition(transition);
        return this;
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && JQMCommon.isVisible(this);
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
    public JQMContainer withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    @Override
    public HandlerRegistration addJQMEventHandler(String jqmEventName, EventHandler handler) {

        Type<EventHandler> t = JQMEventFactory.getType(jqmEventName, EventHandler.class);

        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, jqmEventName, t);
    }

}
