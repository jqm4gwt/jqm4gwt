package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 May 2011 00:24:06
 *
 * <p/> An implementation of a jquery mobile "slider" widget.
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/slider/">Slider</a>
 */
public class JQMSlider extends JQMFieldContainer implements HasValue<Double>, HasMini<JQMSlider>,
        HasText<JQMSlider>, HasCorners<JQMSlider>, HasChangeHandlers, HasClickHandlers, HasTapHandlers {

    private final FormLabel label = new FormLabel();

    /**
     * The input to use as the base element
     */
    private final TextBox input = new TextBox();

    private boolean ignoreChange;

    // There is null internal state, but no such/corresponding UI state.
    // null internal state is needed to properly support data binding libraries (Errai for example).
    private Double internVal;

    /**
     * Create a new {@link JQMSlider} with no label and default values for the min and max
     */
    public JQMSlider() {
        super();
        init();
    }

    public JQMSlider(FlowPanel externFlow) {
        super(externFlow);
        init();
    }

    private void init() {
        String id = Document.get().createUniqueId();
        label.setFor(id);
        input.getElement().setId(id);
        input.getElement().setAttribute("type", "range");
        internVal = 0d;
        input.getElement().setAttribute("value", "0");
        input.getElement().setAttribute("min", "0");
        input.getElement().setAttribute("max", "100");
        add(label);
        add(input);
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (!ignoreChange) {
                    Double old = internVal;
                    internVal = getUiValue();
                    // in case of JQMRangeSlider this onChange() occurs for both Lo and Hi sliders,
                    // even if only one of them was changed, so we have to check if it's really changed.
                    boolean eq = internVal == old || internVal != null && internVal.equals(old);
                    if (!eq) ValueChangeEvent.fire(JQMSlider.this, getValue());
                }
            }
        });
    }

    /**
     * Create a new {@link JQMSlider} with the given label and default values for the min and max
     *
     * @param text the label text
     */
    public JQMSlider(String text) {
        this();
        label.setText(text);
    }

    /**
     * Create a new {@link JQMSlider} with the given label and min and max values
     *
     * @param text the label text
     * @param min  the minimum value of the slider
     * @param max  the maximum value of the slider
     */
    public JQMSlider(String text, Double min, Double max) {
        this(text);
        setMax(max);
        setMin(min);
    }

    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return flow.addDomHandler(handler, ChangeEvent.getType());
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return flow.addDomHandler(handler, ClickEvent.getType());
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Double> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void disable() {
        disable(input.getElement().getId());
    }

    // XXX: Warning! 'String id' cannot be replaced by 'Element elt', because $("#" + id) not equal $(elt) for this widget!
    private static native void disable(String id) /*-{
        $wnd.$("#" + id).slider('disable');
    }-*/;

    public void enable() {
        enable(input.getElement().getId());
    }

    private static native void enable(String id) /*-{
        $wnd.$("#" + id).slider('enable');
    }-*/;

    /**
     * Returns the text of the label
     */
    public String getLabelText() {
        return label.getText();
    }

    @Override
    public String getText() {
        return getLabelText();
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(input);
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setTheme(input, themeName);
    }

    @Override
    public JQMSlider withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    public String getTrackTheme() {
        return input.getElement().getAttribute("data-track-theme");
    }

    /**
     * Sets the theme swatch for the slider
     */
    public void setTrackTheme(String theme) {
        JQMCommon.setAttribute(input, "data-track-theme", theme);
    }

    /**
     * returns the current value of the slider
     */
    @Override
    public Double getValue() {
        Double rslt = getUiValue();
        if (rslt == null) return internVal;

        if (internVal == null) {
            Double min = getMin();
            if (rslt == min || rslt != null && rslt.equals(min)) return null;
        }
        internVal = rslt;
        return rslt;
    }

    /**
     * @return - null if slider is not created/initialized/attached yet and therefore have no UI value.
     */
    private Double getUiValue() {
        String v = JQMCommon.getVal(input.getElement().getId());
        if (v == null || v.isEmpty()) return null;
        return Double.valueOf(v);
    }

    /**
     * Can raise ChangeEvent, block it manually by setting ignoreChange (if needed).
     */
    // XXX: Warning! 'String id' cannot be replaced by 'Element elt', because $("#" + id) not equal $(elt) for this widget!
    private static native void refresh(String id, String value) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$("#" + id).val(value).slider("refresh");
    }-*/;

    private static native void refresh(String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$("#" + id).slider("refresh");
    }-*/;

    private static native void refreshMin(String id, String min) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$("#" + id).attr("min", min).slider("refresh");
    }-*/;

    private static native void refreshMax(String id, String max) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$("#" + id).attr("max", max).slider("refresh");
    }-*/;

    private static native void refreshStep(String id, String step) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$("#" + id).attr("step", step).slider("refresh");
    }-*/;

    public boolean isHighlight() {
        return "true".equals(JQMCommon.getAttribute(input, "data-highligh"));
    }

    public void setHighlight(boolean highlight) {
        if (highlight) JQMCommon.setAttribute(input, "data-highlight", String.valueOf(highlight));
        else JQMCommon.setAttribute(input, "data-highlight", null);
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(input);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(input, mini);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMSlider withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    /**
     * Sets the text of the label
     *
     * @param text is the new text to display
     */
    public void setLabelText(String text) {
        label.setText(text);
    }

    @Override
    public void setText(String text) {
        setLabelText(text);
    }

    @Override
    public JQMSlider withText(String text) {
        setText(text);
        return this;
    }

    public Double getStep() {
        String v = input.getElement().getAttribute("step");
        if (v == null || v.isEmpty()) return 1d;
        return Double.valueOf(v);
    }

    public void setStep(Double value) {
        String s = doubleToNiceStr(value);
        JQMCommon.setAttribute(input, "step", s);
        ignoreChange = true;
        try {
            refreshStep(input.getElement().getId(), s);
        } finally {
            ignoreChange = false;
        }
    }

    public int getStepInt() {
        Double v = getStep();
        return v == null ? 0 : v.intValue();
    }

    public void setStepInt(int value) {
        setStep(new Double(value));
    }

    public double getStepDouble() {
        Double v = getStep();
        return v == null ? 0 : v.doubleValue();
    }

    public void setStepDouble(double value) {
        setStep(value);
    }

    /**
     * Returns the max value of the slider
     */
    public Double getMax() {
        String v = input.getElement().getAttribute("max");
        if (v == null || v.isEmpty()) return null;
        return Double.valueOf(v);
    }

    /**
     * Returns the min value of the slider
     */
    public Double getMin() {
        String v = input.getElement().getAttribute("min");
        if (v == null || v.isEmpty()) return null;
        return Double.valueOf(v);
    }

    /**
     * Sets the new max range for the slider.
     *
     * @param max the new max range
     */
    public void setMax(Double max) {
        String maxStr = doubleToNiceStr(max);
        JQMCommon.setAttribute(input, "max", maxStr);
        refreshMax(input.getElement().getId(), maxStr);
        validateValue();
    }

    public int getMaxInt() {
        Double v = getMax();
        return v == null ? 0 : v.intValue();
    }

    public void setMaxInt(int max) {
        setMax(new Double(max));
    }

    public double getMaxDouble() {
        Double v = getMax();
        return v == null ? 0 : v.doubleValue();
    }

    public void setMaxDouble(double max) {
        setMax(max);
    }

    /**
     * Sets the new min range for the slider
     *
     * @param min the new min range
     */
    public void setMin(Double min) {
        String minStr = doubleToNiceStr(min);
        JQMCommon.setAttribute(input, "min", minStr);
        refreshMin(input.getElement().getId(), minStr);
        validateValue();
    }

    public int getMinInt() {
        Double v = getMin();
        return v == null ? 0 : v.intValue();
    }

    public void setMinInt(int min) {
        setMin(new Double(min));
    }

    public double getMinDouble() {
        Double v = getMin();
        return v == null ? 0 : v.doubleValue();
    }

    public void setMinDouble(double min) {
        setMin(min);
    }

    private void validateValue() {
        Double val = getValue();
        if (val != null) {
            Double min = getMin();
            if (min != null && val.compareTo(min) < 0) {
                setValue(min);
                return;
            }
            Double max = getMax();
            if (max != null && val.compareTo(max) > 0) {
                setValue(max);
                return;
            }
        }
    }

    private static String doubleToNiceStr(Double value) {
        final String valStr;
        if (value != null) {
            double d = value.doubleValue();
            int i = (int) d;
            valStr = i == d ? String.valueOf(i) : String.valueOf(d);
        } else {
            valStr = null;
        }
        return valStr;
    }

    private void setInputValueAttr(Double value) {
        JQMCommon.setAttribute(input, "value", doubleToNiceStr(value));
    }

    /**
     * Sets the value of the slider to the given value
     *
     * @param value the new value of the slider, must be in the range of the slider
     */
    @Override
    public void setValue(Double value) {
        setValue(value, false);
    }

    public int getValueInt() {
        Double v = getValue();
        return v == null ? 0 : v.intValue();
    }

    public void setValueInt(int value) {
        setValue(new Double(value));
    }

    public double getValueDouble() {
        Double v = getValue();
        return v == null ? 0 : v.doubleValue();
    }

    // GWT Designer has strange problem with showing properties, which are defined as Double,
    // but works just fine with double.
    public void setValueDouble(double value) {
        setValue(value);
    }

    /**
     * Sets the value of the slider to the given value
     *
     * @param value the new value of the slider, must be in the range of the slider
     */
    @Override
    public void setValue(Double value, boolean fireEvents) {
        Double old = getValue();
        if (old == value || old != null && old.equals(value)) return;
        internVal = value;
        setInputValueAttr(value);
        ignoreChange = true;
        try {
            refresh(input.getElement().getId(), doubleToNiceStr(value));
        } finally {
            ignoreChange = false;
        }
        if (fireEvents) ValueChangeEvent.fire(this, value);
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCorners(input);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCorners(input, corners);
    }

    @Override
    public JQMSlider withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }
}
