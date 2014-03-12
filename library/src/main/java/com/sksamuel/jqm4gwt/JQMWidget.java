package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.events.HasJQMEventHandlers;
import com.sksamuel.jqm4gwt.events.JQMEvent;
import com.sksamuel.jqm4gwt.events.JQMEventHandler;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 17:02:40
 *         <p/>
 *         An extension of the standard GWT {@link Widget} that adds
 *         functionality common to all JQM elements, as well as convenience
 *         methods used by subclasses.
 *         <p/>
 *         The {@link JQMWidget} is an extension of composite because
 *         {@link JQMWidget}s do not typically add new functionality (in terms
 *         of new elements), they are mostly compositions of existing HTML
 *         elements.
 *         <p/>
 *         This abstract superclass does not define the nature of the
 *         composition in use. Implementating subclasses must decide how to
 *         compose and thus call initWidget() themselves.
 */
public abstract class JQMWidget extends Composite implements HasTheme<JQMWidget>, HasId<JQMWidget>,
        HasDataRole, HasEnabled ,HasJQMEventHandlers{

    /**
     * Returns the value of the attribute with the given name
     */
    public String getAttribute(String name) {
        return JQMCommon.getAttribute(this, name);
    }

    public boolean getAttributeBoolean(String name) {
        return "true".equalsIgnoreCase(getAttribute(name));
    }

    @Override
    public String getDataRole() {
        return JQMCommon.getDataRole(this);
    }

    /**
     * Returns the ID set on the main element
     */
    @Override
    public String getId() {
        return getElement().getId();
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(this);
    }

    /**
     * Removes the attribute with the given name
     *
     * @param name the name of the attribute to remove
     */
    protected void removeAttribute(String name) {
        getElement().removeAttribute(name);
    }

    public void removeDataRole() {
        JQMCommon.removeDataRole(this);
    }

    /**
     * Sets the value of the attribute with the given name to the given value.
     */
    public void setAttribute(String name, String value) {
        JQMCommon.setAttribute(this, name, value);
    }

    /**
     * Sets the data-role attribute to the given value.
     *
     * @param value the value to set the data-role attribute to
     */
    @Override
    public void setDataRole(String value) {
        JQMCommon.setDataRole(this, value);
    }

    /**
     * Assigns an automatically generated ID to this widget. This method uses
     * the default GWT id creation methods in Document.get().createUniqueId()
     */
    protected void setId() {
        withId(Document.get().createUniqueId());
    }

    @Override
    public final void setId(String id) {
        getElement().setId(id);
    }

    /**
     * The same as {@link JQMWidget#setId(String)}, but needed for UiBinder templates
     * where setId() cannot be used, because id="xxx" has different predefined meaning.
     */
    public final void setWidgetId(String id) {
        setId(id);
    }

    @Override
    public final JQMWidget withId(String id) {
        setId(id);
        return this;
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setTheme(this, themeName);
    }

    @Override
    public JQMWidget withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    @Override
    public void setEnabled(boolean b) {
        JQMCommon.setEnabled(this, b);
    }

    @Override
    public boolean isEnabled() {
        return JQMCommon.isEnabled(this);
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && JQMCommon.isVisible(this);
    }

	@Override
	public HandlerRegistration addJQMEventHandler(String eventName ,JQMEventHandler handler){

	       return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
				@Override
				public int getHandlerCountForWidget(Type<?> type) {
					return getHandlerCount(type);
				}
	        }, this, handler, eventName, JQMEvent.getType());
	}

}
