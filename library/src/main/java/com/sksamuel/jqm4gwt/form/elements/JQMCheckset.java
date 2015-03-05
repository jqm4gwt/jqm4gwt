package com.sksamuel.jqm4gwt.form.elements;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasOrientation;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.Orientation;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.form.JQMFieldset;
import com.sksamuel.jqm4gwt.html.FormLabel;
import com.sksamuel.jqm4gwt.html.Legend;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 May 2011 08:17:31
 *
 *         A widget that is composed of 1 or more checkboxes.
 *
 *         The child checkboxes are grouped together and can be set to be
 *         vertical or horizontal.
 *
 * <p><a href="http://demos.jquerymobile.com/1.4.5/checkboxradio-checkbox/">Checkboxes</a></p>
 * <p><a href="http://demos.jquerymobile.com/1.4.5/forms/#checkboxes">Form elements - Checkboxes</a></p>
 *
 * <h3>Use in UiBinder Templates</h3>
 *
 * When working with JQMCheckset in
 * {@link com.google.gwt.uibinder.client.UiBinder UiBinder} templates, you
 * can add Checkboes via child elements. For example:
 * <pre>
 * &lt;jqm:form.elements.JQMCheckset>
 *    &lt;jqm:check>&lt;jqm:form.elements.JQMCheckbox name="cb20" text="Check1"/&gt;&lt;/jqm:check&gt;
 *    &lt;jqm:check>&lt;jqm:form.elements.JQMCheckbox name="cb21" text="Check2"/&gt;&lt;/jqm:check&gt;
 * &lt;/jqm:form.elements.JQMCheckset>
 * </pre>
 *
 */
public class JQMCheckset extends JQMFieldContainer implements HasText<JQMCheckset>,
        HasSelectionHandlers<String>, HasOrientation<JQMCheckset>, HasMini<JQMCheckset>,
        HasClickHandlers, HasTapHandlers, JQMFormWidget {

    private JQMFieldset fieldset;

    private Legend legend;

    private final List<JQMCheckbox> checks = new ArrayList<JQMCheckbox>();

    private String theme;

    /**
     * Creates a new {@link JQMCheckset} with no label text
     */
    public JQMCheckset() {
        this(null);
    }

    /**
     * Creates a new {@link JQMCheckset} with the label set to the given text.
     *
     * @param text - the display text for the label
     */
    public JQMCheckset(String text) {
        setupFieldset(text);
    }

    private void setupFieldset(String labelText) {
        if (fieldset != null) {
            boolean horz = fieldset.isHorizontal();
            boolean vert = fieldset.isVertical();
            IconPos iconPos = getIconPos();
            boolean mini = isMini();

            remove(fieldset);
            fieldset = new JQMFieldset();

            if (horz) fieldset.setHorizontal();
            if (vert) fieldset.setVertical();
            setIconPos(iconPos);
            setMini(mini);
        } else {
            // the fieldset is the inner container and is contained inside the flow
            fieldset = new JQMFieldset();
        }
        fieldset.getElement().setId(Document.get().createUniqueId());
        add(fieldset);

        legend = new Legend();
        fieldset.add(legend);

        setText(labelText);
    }

    BlurHandler blurHandler;
    ArrayList<HandlerRegistration> blurHandlers = new ArrayList<HandlerRegistration>();

    private void addLabelsBlurHandler(final BlurHandler handler) {
        for (JQMCheckbox cb : checks) {
            FormLabel label = cb.getLabel();
            blurHandlers.add(label.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    handler.onBlur(null);
                }
            }, ClickEvent.getType()));
        }
    }

    private void clearBlurHandlers() {
        for (HandlerRegistration blurHandler : blurHandlers) blurHandler.removeHandler();
        blurHandlers.clear();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (blurHandler != null && blurHandlers.size() == 0) addLabelsBlurHandler(blurHandler);
    }

    @Override
    protected void onUnload() {
        clearBlurHandlers();
        super.onUnload();
    }

    @Override
    public HandlerRegistration addBlurHandler(final BlurHandler handler) {
        this.blurHandler = handler;
        clearBlurHandlers();
        addLabelsBlurHandler(handler);
        return null;
    }

    @UiChild(tagname = "check")
    public void addCheckbox(JQMCheckbox checkbox) {
        checks.add(checkbox);
        checkbox.setTheme(theme);
        fieldset.add(checkbox);
    }

    public void clear() {
        checks.clear();
        setupFieldset(getText());
    }

    @Override
    public String getTheme() {
        if (checks.isEmpty()) return theme;
        return checks.get(0).getTheme();
    }

    @Override
    public void setTheme(String themeName) {
        theme = themeName;
        for (JQMCheckbox cb : checks) cb.setTheme(theme);
    }

    @Override
    public JQMWidget withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addTapHandler(TapHandler handler) {
        // this is not a native browser event so we will have to manage it via JS
        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, JQMComponentEvents.TAP_EVENT, TapEvent.getType());
    }

    @Override
    public Label addErrorLabel() {
        return null;
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
        return null;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return null;
    }

    /**
     * Returns the value of the legend text
     */
    @Override
    public String getText() {
        return legend.getText();
    }

    /**
     * Returns the id of any checkbox that is checked or null if no checkbox
     * in this checkset has been selected
     *
     * @return the first selected value
     */
    @Override
    public String getValue() {
        for (JQMCheckbox box : checks) {
            if (box.isChecked())
                return box.getId();
        }
        return null;
    }

    /*private String getValue(Element element) {
        while (element != null) {
            if ("label".equalsIgnoreCase(element.getTagName())
                    && element.getAttribute("class") != null
                    && (element.getAttribute("class").contains("ui-btn-active")
                            || element.getAttribute("class").contains("ui-btn-down")))
                return element.getAttribute("for");
            String value = getValue(element.getFirstChildElement());
            if (value != null)
                return value;
            element = element.getNextSiblingElement();
        }
        return null;
    }*/

    /**
     * Returns true if at least one checkbox in this checkset is selected.
     */
    public boolean hasSelection() {
        return getValue() != null;
    }

    /**
     * Returns true if the checkbox with the given id is selected.
     *
     * @param id - the id of the checkbox to test
     */
    public boolean isSelected(String id) {
        for (JQMCheckbox box : checks) {
            if (id.equals(box.getId()))
                return box.isChecked();
        }
        return false;
    }

    /**
     * @param id
     * @param label
     */
    public void removeCheck(String id, String label) {
        // TODO traverse all elements removing anything that has a "for" for
        // this id or actually has this id
    }

    @Override
    public boolean isHorizontal() {
        return fieldset.isHorizontal();
    }

    @Override
    public void setHorizontal() {
        fieldset.withHorizontal();
    }

    @Override
    public JQMCheckset withHorizontal() {
        setHorizontal();
        return this;
    }

    @Override
    public boolean isVertical() {
        return fieldset.isVertical();
    }

    @Override
    public void setVertical() {
        fieldset.withVertical();
    }

    @Override
    public JQMCheckset withVertical() {
        setVertical();
        return this;
    }

    public void setOrientation(Orientation value) {
        HasOrientation.Support.setOrientation(this, value);
    }

    public Orientation getOrientation() {
        return HasOrientation.Support.getOrientation(this);
    }

    /**
     * Sets the value of the legend text.
     */
    @Override
    public void setText(String text) {
        legend.setText(text);
    }

    @Override
    public JQMCheckset withText(String text) {
        setText(text);
        return this;
    }

    /**
     * Sets the checkbox with the given value to be checked
     */
    @Override
    public void setValue(String id) {
        setValue(id, false);
    }

    @Override
    public void setValue(String id, boolean fireEvents) {
        for (JQMCheckbox box : checks) {
            if (id.equals(box.getId())) {
                box.setValue(true, fireEvents);
                return;
            }
        }
    }

    public IconPos getIconPos() {
        return JQMCommon.getIconPos(fieldset);
    }

    /**
     * Sets the position of the icon.
     */
    public void setIconPos(IconPos pos) {
        JQMCommon.setIconPos(fieldset, pos);
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(fieldset);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(fieldset, mini);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMCheckset withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    public void refresh() {
        refreshAll(fieldset.getElement());
    }

    private static native void refreshAll(Element elt) /*-{
        $wnd.$(elt).find("input[type='checkbox']").each(function() {
            var w = $wnd.$(this);
            if (w.data('mobile-checkboxradio') !== undefined) {
                w.checkboxradio('refresh');
            }
        });
    }-*/;
}
