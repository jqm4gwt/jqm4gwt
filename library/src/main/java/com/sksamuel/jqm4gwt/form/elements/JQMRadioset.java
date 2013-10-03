package com.sksamuel.jqm4gwt.form.elements;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasOrientation;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.Orientation;
import com.sksamuel.jqm4gwt.form.JQMFieldset;
import com.sksamuel.jqm4gwt.html.Legend;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 May 2011 08:17:31
 *
 * <p> A widget that is a collection of one or more radio buttons. All radio
 *         buttons must belong to a radioset. All radio buttons in a set are
 *         grouped and styled together. </p>
 *
 * <h3>Use in UiBinder Templates</h3>
 *
 * When working with JQMRadioset in
 * {@link com.google.gwt.uibinder.client.UiBinder UiBinder} templates, you
 * can add Radio buttons via child elements. For example:
 * <pre>
 * &lt;jqm:form.elements.JQMRadioset>
 *    &lt;jqm:radio>
 *      &lt;jqm:form.elements.JQMRadio text="radio_A_Text" value="a"/>
 *      &lt;jqm:form.elements.JQMRadio text="radio_B_Text" value="b"/>
 *    &lt;/jqm:radio>
 * &lt;/jqm:form.elements.JQMRadioset>
 * </pre>
 *
 */
public class JQMRadioset extends JQMWidget implements HasText<JQMRadioset>, HasValue<String>,
        HasSelectionHandlers<String>, HasOrientation<JQMRadioset>, HasMini<JQMRadioset>,
        JQMFormWidget, HasClickHandlers {

    private boolean valueChangeHandlerInitialized;

    /**
     * The panel that is used for the controlgroup container
     */
    private JQMFieldset fieldset;

    private Legend legend;

    /**
     * The panel that acts as the fieldcontain container
     */
    private final FlowPanel flow;

    /**
     * The input's that are used for the radio buttons
     */
    private final List<TextBox> radios = new ArrayList<TextBox>();

    /**
     * Creates a new {@link JQMRadioset} with no label
     *
     */
    public JQMRadioset() {
        this(null);
    }

    /**
     * Creates a new {@link JQMRadioset} with the label text set to the given
     * value
     *
     * @param text
     *            the text for the label
     */
    public JQMRadioset(String text) {
        flow = new FlowPanel();
        flow.getElement().setId(Document.get().createUniqueId());
        initWidget(flow);
        setDataRole("fieldcontain");
        setupFieldset(text);
    }

    private void setupFieldset(String labelText) {
        if (fieldset != null) flow.remove(fieldset);
        // the fieldset is the inner container and is contained inside the flow
        fieldset = new JQMFieldset();
        fieldset.getElement().setId(Document.get().createUniqueId());
        flow.add(fieldset);

        // the legend must be added to the fieldset
        legend = new Legend();
        legend.setText(labelText);
        fieldset.add(legend);
    }

    BlurHandler blurHandler;
    ArrayList<HandlerRegistration> blurHandlers = new ArrayList<HandlerRegistration>();

    private void addRadiosBlurHandler(final BlurHandler handler) {
        for (TextBox radio : radios) {
            blurHandlers.add(radio.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    handler.onBlur(null);
                }
            }));
        }
    }

    private void clearBlurHandlers() {
        for (HandlerRegistration blurHandler : blurHandlers) blurHandler.removeHandler();
        blurHandlers.clear();
    }

    @Override
    protected void onLoad() {
        if (blurHandler != null && blurHandlers.size() == 0) addRadiosBlurHandler(blurHandler);
    }

    @Override
    protected void onUnload() {
        clearBlurHandlers();
    }

    /**
     * no-op implementation required for {@link JQMFormWidget}
     */
    @Override
    public HandlerRegistration addBlurHandler(final BlurHandler handler) {
        this.blurHandler = handler;
        clearBlurHandlers();
        addRadiosBlurHandler(handler);
        return null;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public Label addErrorLabel() {
        return null;
    }

    /**
     * Adds a new radio button to this radioset using the given string for both
     * the value and the text. Returns a JQMRadio instance which can be used to
     * change the value and label of the radio button.
     *
     * This method is the same as calling addRadio(String, String) with the same
     * string twice.
     *
     * @param value
     *            the value to associate with this radio option. This will be
     *            the value returned by methods that query the selected value.
     *            The value will also be used for the label.
     *
     * @return a JQMRadio instance to adjust the added radio button
     */
    public JQMRadio addRadio(String value) {
        return addRadio(value, value);
    }

    /**
     * Adds a new radio button to this radioset using the given value and text.
     * Returns a JQMRadio instance which can be used to change the value and
     * label of the radio button.
     *
     * @param value
     *            the value to associate with this radio option. This will be
     *            the value returned by methods that query the selected value.
     *
     * @param text
     *            the label to show for this radio option.
     *
     * @return a JQMRadio instance to adjust the added radio button
     */
    public JQMRadio addRadio(String value, String text) {
        JQMRadio radio = new JQMRadio(value, text);
        addRadio(radio);
        return radio;
    }

    /**
     * UiBinder call method to add a radio button
     *
     * @param radio
     */
    @UiChild(tagname = "radio")
    public void addRadio(JQMRadio radio) {
        radio.setName(fieldset.getId());
        radios.add(radio.getInput());
        fieldset.add(radio.getInput());
        fieldset.add(radio.getLabel());
    }

    public void clear() {
        radios.clear();
        setupFieldset(getText());
    }

    @Override
    public void setTheme(String themeName) {
        for (TextBox radio : radios) applyTheme(radio, themeName);
    }

    @Override
    public JQMWidget withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        // Initialization code
        if (!valueChangeHandlerInitialized) {
            valueChangeHandlerInitialized = true;
            for (TextBox radio : radios) {
                radio.addChangeHandler(new ChangeHandler() {
                    @Override
                    public void onChange(ChangeEvent event) {
                        SelectionEvent.fire(JQMRadioset.this, getValue());
                        ValueChangeEvent.fire(JQMRadioset.this, getValue());
                    }
                });
            }
        }
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Alias for getValue()
     */
    public String getSelectedValue() {
        return getValue();
    }

    /**
     * Returns the text used for the main label
     */
    @Override
    public String getText() {
        return legend.getText();
    }

    /**
     *
     * Note: this method will return null until after the jquery init phase has
     * completed. That means if you call getValue() on an element in the intial
     * construction of a page then it will return null.
     *
     * @return the value of the currently selected radio button or null if no
     *         button is currently selected.
     *
     *
     */
    @Override
    public String getValue() {
        for (TextBox radio : radios) {
            Element element = radio.getElement();
            if (isChecked(element.getId())) {
                return element.getAttribute("value");
            }
        }
        return null;
    }

    /**
     * Returns the value of the radio option at the given index.
     *
     * @return the value of the k'th radio option
     */
    public String getValue(int k) {
        return radios.get(k).getElement().getAttribute("value");
    }

    /**
     * Returns the value of the button that has the given id
     *
     * @return the value of the button with the given id
     */
    private String getValueForId(String id) {
        for (int k = 0; k < fieldset.getWidgetCount(); k++) {
            Widget widget = fieldset.getWidget(k);
            if (id.equals(widget.getElement().getAttribute("id")))
                return widget.getElement().getAttribute("value");
        }
        return null;
    }

    @Override
    public boolean isHorizontal() {
        return fieldset.isHorizontal();
    }

    @Override
    public boolean isVertical() {
        return fieldset.isVertical();
    }

    /**
     * Removes the given {@link JQMRadio} from this radioset
     *
     * @param radio
     *            the radio to remove
     */
    public void removeRadio(JQMRadio radio) {
        removeRadio(radio);
    }

    /**
     * Removes the {@link JQMRadio} button that has the given value
     */
    public void removeRadio(String value) {
        // TODO traverse all elements removing anything that has a "for" for
        // this id or actually has this id
    }

    @Override
    public void setHorizontal() {
        fieldset.withHorizontal();
    }

    @Override
    public JQMRadioset withHorizontal() {
        setHorizontal();
        return this;
    }

    /**
     * Sets the selected value. This is an alias for setValue(String)
     *
     * @see #setValue(String)
     */
    public void setSelectedValue(String value) {
        setValue(value);
    }

    @Override
    public void setText(String text) {
        legend.setText(text);
    }

    @Override
    public void setValue(String value) {
        setValue(value, false);
    }

    /**
     * Sets the currently selected radio to the given value.
     */
    @Override
    public void setValue(String value, boolean fireEvents) {
        String oldValue = fireEvents ? getValue() : null;
        boolean changed = false;
        for (TextBox radio : radios) { // first we have to uncheck already checked radios (UI refresh issue)
            if (!isChecked(radio)) continue;
            boolean checked = value != null && value.equals(radio.getValue()) ? true : false;
            if (!checked) {
                setChecked(radio, false);
                changed = true;
            }
        }
        for (TextBox radio : radios) {
            boolean checked = value != null && value.equals(radio.getValue()) ? true : false;
            if (isChecked(radio) != checked) {
                setChecked(radio, checked);
                changed = true;
            }
        }
        if (fireEvents && changed) {
            String newValue = getValue();
            boolean eq = newValue == oldValue || newValue != null && newValue.equals(oldValue);
            if (!eq) {
                SelectionEvent.fire(this, newValue);
                ValueChangeEvent.fire(this, newValue);
            }
        }
    }

    protected void setChecked(TextBox radio, boolean value) {
        if (radio == null) return;
        if (radio.isAttached()) setCheckedAndRefresh(radio.getElement(), value);
        else setChecked(radio.getElement(), value);
    }

    protected boolean isChecked(TextBox radio) {
        return radio != null && isChecked(radio.getElement().getId());
    }

    private native boolean isChecked(String id) /*-{
        return $wnd.$("input#" + id).prop("checked") ? true : false;
    }-*/;

    private native void setCheckedAndRefresh(Element e, boolean value) /*-{
        $wnd.$(e).prop('checked', value).checkboxradio('refresh');
    }-*/;

    private native void setChecked(Element e, boolean value) /*-{
        $wnd.$(e).prop('checked', value);
    }-*/;

    protected native void refreshAll() /*-{
        $wnd.$("input[type='radio']").checkboxradio("refresh");
    }-*/;

    @Override
    public void setVertical() {
        fieldset.withVertical();
    }

    @Override
    public JQMRadioset withVertical() {
        setVertical();
        return this;
    }

    /**
     * Returns the number of radio options set on this radioset
     *
     * @return the integer number of options
     */
    public int size() {
        return radios.size();
    }

    @Override
    public JQMRadioset withText(String text) {
        setText(text);
        return this;
    }

    public void setOrientation(Orientation value) {
        switch (value) {
            case HORIZONTAL:
                setHorizontal();
                break;

            case VERTICAL:
                setVertical();
                break;
        }
    }

    public IconPos getIconPos() {
        String string = fieldset.getElement().getAttribute("data-iconpos");
        return string == null ? null : IconPos.valueOf(string);
    }

    /**
     * Sets the position of the icon.
     */
    public void setIconPos(IconPos pos) {
        if (pos == null)
            fieldset.getElement().removeAttribute("data-iconpos");
        else
            fieldset.getElement().setAttribute("data-iconpos", pos.getJqmValue());
    }

    @Override
    public boolean isMini() {
        return "true".equals(fieldset.getElement().getAttribute("data-mini"));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        fieldset.getElement().setAttribute("data-mini", String.valueOf(mini));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMRadioset withMini(boolean mini) {
        setMini(mini);
        return this;
    }
}