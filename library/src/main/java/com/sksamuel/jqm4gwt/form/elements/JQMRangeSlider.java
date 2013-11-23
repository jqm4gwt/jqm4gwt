package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;

/**
 * See <a href="http://view.jquerymobile.com/1.3.2/dist/demos/widgets/sliders/rangeslider.html">Range Slider</a>
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
        hi = new JQMSlider(range);
    }

    public JQMSlider getLo() {
        return lo;
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

    public Double getHiValue() {
        return hi.getValue();
    }

    public void setHiValue(Double value) {
        hi.setValue(value);
    }

    public Double getStep() {
        return lo.getStep();
    }

    public void setStep(Double value) {
        lo.setStep(value);
        hi.setStep(value);
    }

    public void setIntStep(int value) {
        setStep(new Double(value));
    }

    public Double getMin() {
        return lo.getMin();
    }

    public void setMin(Double min) {
        lo.setMin(min);
        hi.setMin(min);
    }

    public void setIntMin(int min) {
        setMin(new Double(min));
    }

    public Double getMax() {
        return lo.getMax();
    }

    public void setMax(Double max) {
        lo.setMax(max);
        hi.setMax(max);
    }

    public void setIntMax(int max) {
        setMax(new Double(max));
    }
}
