package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;

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
public abstract class JQMWidget extends Composite implements HasTheme<JQMWidget>, HasId<JQMWidget>, HasDataRole, HasEnabled {

    /**
     * Returns the value of the attribute with the given name
     */
    protected String getAttribute(String name) {
        return getElement().getAttribute(name);
    }


    private static final String STYLE_UI_DISABLED = "ui-disabled";


    public boolean getAttributeBoolean(String name) {
        return "true".equalsIgnoreCase(getAttribute(name));
    }

    @Override
    public String getDataRole() {
        return getAttribute("data-role");
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
        return getAttribute("data-theme");
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
        removeAttribute("data-role");
    }

    /**
     * Sets the value of the attribute with the given name to the given value.
     */
    public void setAttribute(String name, String value) {
        if (value == null)
            getElement().removeAttribute(name);
        else
            getElement().setAttribute(name, value);
    }

    /**
     * Sets the data-role attribute to the given value.
     *
     * @param value the value to set the data-role attribute to
     */
    @Override
    public void setDataRole(String value) {
        setAttribute("data-role", value);
    }

    /**
     * Assigns an automatically generated ID to this widget. This method uses
     * the default GWT id creation methods in Document.get().createUniqueId()
     */
    protected void setId() {
        setId(Document.get().createUniqueId());
    }

    @Override
    public final JQMWidget setId(String id) {
        getElement().setId(id);
        return this;
    }

    @Override
    public JQMWidget setTheme(String themeName) {
        setAttribute("data-theme", themeName);
        return this;
    }

    public void setEnabled(boolean b) {
        if (isEnabled() != b) {
            if (b) removeStyleName(STYLE_UI_DISABLED);
            else addStyleName(STYLE_UI_DISABLED);
        }
    }

    public boolean isEnabled() {
        String styleName = getStyleName();
        return styleName != null ? !styleName.contains(STYLE_UI_DISABLED) : true;
    }
    
    public static void applyTheme(Widget w, String themeName) {
        if (themeName == null)
            w.getElement().removeAttribute("data-theme");
        else
            w.getElement().setAttribute("data-theme", themeName);
    }
}
