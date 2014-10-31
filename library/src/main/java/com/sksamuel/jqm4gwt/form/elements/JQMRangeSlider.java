package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;

/**
 * See <a href="http://demos.jquerymobile.com/1.4.5/rangeslider/">Range Slider</a>
 *
 * @author slavap
 *
 */
public class JQMRangeSlider extends JQMFieldContainer implements HasText<JQMRangeSlider>,
        HasMini<JQMRangeSlider> {

    protected final FlowPanel range;

    private final JQMSlider lo;
    private final JQMSlider hi;

    public JQMRangeSlider() {
        range = new FlowPanel();
        range.getElement().setId(Document.get().createUniqueId());
        JQMCommon.setDataRole(range, "rangeslider");
        add(range);

        lo = new JQMSlider(range);
        lo.addValueChangeHandler(new ValueChangeHandler<Double>() {
            @Override
            public void onValueChange(ValueChangeEvent<Double> event) {
                Double loV = event.getValue();
                Double hiV = hi.getValue();
                if (loV == hiV || loV != null && loV.equals(hiV)) return;
                if (loV != null && loV.compareTo(hiV) > 0) {
                    hi.setValue(loV, true/*fireEvents*/);
                    lo.setValue(loV, true/*fireEvents*/); // could be set successfully after hi update only
                }
            }
        });

        hi = new JQMSlider(range);
        hi.addValueChangeHandler(new ValueChangeHandler<Double>() {
            @Override
            public void onValueChange(ValueChangeEvent<Double> event) {
                Double loV = lo.getValue();
                Double hiV = event.getValue();
                if (hiV == loV || hiV != null && hiV.equals(loV)) return;
                if (hiV != null && hiV.compareTo(loV) < 0) {
                    lo.setValue(hiV, true/*fireEvents*/);
                    hi.setValue(hiV, true/*fireEvents*/); // could be set successfully after lo update only
                }
            }
        });
    }

    public JQMSlider lo() {
        return lo;
    }

    public JQMSlider getLo() {
        return lo;
    }

    public JQMSlider hi() {
        return hi;
    }

    public JQMSlider getHi() {
        return hi;
    }

    @Override
    public String getText() {
        return lo.getText();
    }

    @Override
    public void setText(String text) {
        lo.setText(text);
        hi.setText(text);
    }

    @Override
    public JQMRangeSlider withText(String text) {
        setText(text);
        return this;
    }

    public boolean isHighlight() {
        String v = JQMCommon.getAttribute(range, "data-highligh");
        return v == null || v.isEmpty() || "true".equals(v);
    }

    public void setHighlight(boolean highlight) {
        if (highlight) JQMCommon.setAttribute(range, "data-highlight", null);
        else JQMCommon.setAttribute(range, "data-highlight", String.valueOf(highlight));
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(range);
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setTheme(range, themeName);
    }

    @Override
    public JQMRangeSlider withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    public String getTrackTheme() {
        return range.getElement().getAttribute("data-track-theme");
    }

    /**
     * Sets the theme swatch for the slider
     */
    public void setTrackTheme(String theme) {
        JQMCommon.setAttribute(range, "data-track-theme", theme);
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(range);
    }

    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(range, mini);
    }

    @Override
    public JQMRangeSlider withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    public void disable() {
        lo.disable();
        hi.disable();
    }

    public void enable() {
        lo.enable();
        hi.enable();
    }

    public Double getLoValue() {
        return lo.getValue();
    }

    public void setLoValue(Double value) {
        lo.setValue(value);
    }

    public double getLoValueDouble() {
        return lo.getValueDouble();
    }

    // GWT Designer has strange problem with showing properties, which are defined as Double,
    // but works just fine with double.
    public void setLoValueDouble(double value) {
        lo.setValueDouble(value);
    }

    public int getLoValueInt() {
        return lo.getValueInt();
    }

    public void setLoValueInt(int value) {
        lo.setValueInt(value);
    }

    public Double getHiValue() {
        return hi.getValue();
    }

    public void setHiValue(Double value) {
        hi.setValue(value);
    }

    public double getHiValueDouble() {
        return hi.getValueDouble();
    }

    public void setHiValueDouble(double value) {
        hi.setValueDouble(value);
    }

    public int getHiValueInt() {
        return hi.getValueInt();
    }

    public void setHiValueInt(int value) {
        hi.setValueInt(value);
    }

    public Double getStep() {
        return lo.getStep();
    }

    public void setStep(Double value) {
        lo.setStep(value);
        hi.setStep(value);
    }

    public int getStepInt() {
        return lo.getStepInt();
    }

    public void setStepInt(int value) {
        lo.setStepInt(value);
        hi.setStepInt(value);
    }

    public double getStepDouble() {
        return lo.getStepDouble();
    }

    public void setStepDouble(double value) {
        lo.setStepDouble(value);
        hi.setStepDouble(value);
    }

    public Double getMin() {
        return lo.getMin();
    }

    public void setMin(Double min) {
        lo.setMin(min);
        hi.setMin(min);
    }

    public int getMinInt() {
        return lo.getMinInt();
    }

    public void setMinInt(int min) {
        lo.setMinInt(min);
        hi.setMinInt(min);
    }

    public double getMinDouble() {
        return lo.getMinDouble();
    }

    public void setMinDouble(double min) {
        lo.setMinDouble(min);
        hi.setMinDouble(min);
    }

    public Double getMax() {
        return lo.getMax();
    }

    public void setMax(Double max) {
        lo.setMax(max);
        hi.setMax(max);
    }

    public int getMaxInt() {
        return lo.getMaxInt();
    }

    public void setMaxInt(int max) {
        lo.setMaxInt(max);
        hi.setMaxInt(max);
    }

    public double getMaxDouble() {
        return lo.getMaxDouble();
    }

    public void setMaxDouble(double max) {
        lo.setMaxDouble(max);
        hi.setMaxDouble(max);
    }
}
