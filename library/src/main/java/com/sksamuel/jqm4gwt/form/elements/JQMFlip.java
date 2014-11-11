package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
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
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPageEvent;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:21:09
 *
 * <p><a href="http://demos.jquerymobile.com/1.4.5/flipswitch/">Flip switch</a></p>
 *
 */
public class JQMFlip extends JQMFieldContainer implements HasText<JQMFlip>, HasValue<String>,
        HasChangeHandlers, HasClickHandlers, HasTapHandlers, HasMini<JQMFlip>, HasCorners<JQMFlip> {

    private final FormLabel label = new FormLabel();

    // The GWT widget to use as the select element
    private final ListBox select = new ListBox();

    private String value1;
    private String value2;

    private boolean valueChangeHandlerInitialized;

    // There are three internal states: null, value1, value2 AND only two ui states: value1, value2.
    // Three internal states are needed to properly support data binding libraries (Errai for example).
    private String internVal;

    /** setValue() in progress */
    private boolean inSetValue;

    private String trackTheme;

    /**
     * Creates a new {@link JQMFlip} widget with the given label text and
     * initialized with two options taking their details from the given
     * parameters.
     *
     * This constructor sets the label of each toggle to be the same as the
     * value. If you want a seperate label and value use the JQMFlip(String,
     * String, String, String, String) constructor.
     *
     * @param text
     *            the label text to use
     * @param value1
     *            the value and label of the first toggle
     * @param value2
     *            the value and label of the second toggle
     *
     */
    public JQMFlip(String text, String value1, String value2) {
        this(text, value1, value1, value2, value2);
    }

    /**
     * Creates a new {@link JQMFlip} widget with the given label text and
     * initialized with two options taking their details from the given
     * parameters.
     *
     * @param text
     *            the label text to use
     * @param value1
     *            the value of the first toggle
     * @param label1
     *            the text to display for the first toggle
     * @param value2
     *            the value of the second toggle
     * @param label2
     *            the text to display for the second toggle
     *
     */
    public JQMFlip(String text, String value1, String label1, String value2, String label2) {
        this();
        setText(text);
        select.addItem(label1, value1);
        select.addItem(label2, value2);
        setValue1(value1);
        setValue2(value2);
    }

    public JQMFlip() {
        String id = Document.get().createUniqueId();
        label.setFor(id);
        select.setName(id);
        select.getElement().setId(id);
        JQMCommon.setDataRole(select.getElement(), "flipswitch");
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (inSetValue) return;
                switch (getSelectedIndex()) {
                case 0:
                    internVal = getValue1();
                    break;
                case 1:
                    internVal = getValue2();
                    break;
                default:
                    internVal = null;
                }
            }
        });
        add(label);
        add(select);
    }

    public String getLabel1() {
        return select.getItemText(0);
    }

    public void setLabel1(String label1) {
        if (select.getItemCount() > 1)
            select.setItemText(0, label1);
        else
            select.addItem(label1, getValue1());
    }

    public String getLabel2() {
        return select.getItemText(0);
    }

    public void setLabel2(String label2) {
        if (select.getItemCount() > 2)
            select.setItemText(1, label2);
        else
            select.addItem(label2, getValue2());
    }

    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return select.addChangeHandler(handler);
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        // Initialization code
        if (!valueChangeHandlerInitialized) {
            valueChangeHandlerInitialized = true;
            addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    ValueChangeEvent.fire(JQMFlip.this, getValue());
                }
            });
        }
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public int getSelectedIndex() {
        return select.getSelectedIndex();
    }

    /**
     * Returns the value of the legend text.
     */
    @Override
    public String getText() {
        return label.getText();
    }

    /**
     * Returns the currently selected value or null if there is no currently
     * selected button
     */
    @Override
    public String getValue() {
        switch (getSelectedIndex()) {
        case 0:
            if (internVal == null) return null;
            internVal = getValue1();
            return internVal;
        case 1:
            internVal = getValue2();
            return internVal;
        default:
            return null;
        }
    }

    protected void refresh() {
        refresh(select.getElement());
    }

    private static native void refresh(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var w = $wnd.$(elt);
        if (w.data('mobile-flipswitch') !== undefined) {
            w.flipswitch('refresh');
        }
    }-*/;

    private static native boolean isReady(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var w = $wnd.$(elt);
        return w.data('mobile-flipswitch') !== undefined;
    }-*/;

    /**
     * Sets the currently selected index.
     */
    public void setSelectedIndex(int i) {
        select.setSelectedIndex(i);
        refresh(); // updates UI and always resets invalid index to 0
    }

    /**
     * Sets the value of the legend text.
     */
    @Override
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public JQMFlip withText(String text) {
        setText(text);
        return this;
    }

    public void setTextHidden(boolean value) {
        JQMCommon.setLabelHidden(this, value);
    }

    /**
     * Sets the currently selected value
     */
    @Override
    public void setValue(String value) {
        setValue(value, false);
    }

    /**
     * Sets the currently selected value.
     */
    @Override
    public void setValue(String value, boolean fireEvents) {
        int newIdx = value == null ? 0 : value.equals(getValue1()) ? 0
                                       : value.equals(getValue2()) ? 1 : 0;
        int oldIdx = getSelectedIndex();
        String oldVal = fireEvents ? getValue() : null;
        internVal = value;
        if (oldIdx != newIdx) {
            inSetValue = true;
            try {
                setSelectedIndex(newIdx);
            } finally {
                inSetValue = false;
            }
        }

        if (fireEvents) {
            boolean eq = internVal == oldVal || internVal != null && internVal.equals(oldVal);
            if (!eq) ValueChangeEvent.fire(this, internVal);
        }
    }

    /**
     * Sets the value of the first option to the given string
     */
    public void setValue1(String newValue) {
        value1 = newValue;
    }

    public String getValue1() {
        return value1;
    }

    /**
     * Sets the value of the second option to the given string
     */
    public void setValue2(String newValue) {
        value2 = newValue;
    }

    public String getValue2() {
        return value2;
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(select);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(select, mini);
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMFlip withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(select);
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.applyTheme(select, themeName);
    }

    public String getDataWrapper() {
        return JQMCommon.getDataWrapper(select);
    }

    public void setDataWrapper(String wrapper) {
        JQMCommon.setDataWrapper(select, wrapper);
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCorners(select);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCorners(select, corners);
    }

    @Override
    public JQMFlip withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    public String getTrackTheme() {
        return trackTheme;
    }

    /** Sets the theme for the track button */
    public void setTrackTheme(String value) {
        // data-track-theme is not available for flipswitch, so we have to hack it
        trackTheme = value != null ? value.trim() : value;
        refreshTrackTheme();
    }

    private void refreshTrackTheme() {
        Element elt = select.getElement();
        if (!select.isAttached() || !isReady(elt)) return;
        Element par = elt.getParentElement();
        if (par == null) return;
        Element track = JQMCommon.findChild(par, "ui-flipswitch-on");
        if (track != null && JQMCommon.hasStyle(track, "ui-btn")) {
            if (trackTheme != null && !trackTheme.isEmpty()) {
                track.removeClassName("ui-btn-inherit");
                JQMButton.setTheme(track, trackTheme);
            } else {
                JQMButton.setTheme(track, "inherit");
                track.removeAttribute("data-theme");
            }
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (trackTheme != null && !trackTheme.isEmpty()) {
            Widget p = getParent();
            while (p != null) {
                if (p instanceof JQMPage) {
                    ((JQMPage) p).addPageHandler(new JQMPageEvent.DefaultHandler() {
                        @Override
                        public void onShow(JQMPageEvent event) {
                            super.onShow(event);
                            refreshTrackTheme();
                        }
                    });
                    break;
                }
                p = p.getParent();
            }
            if (!(p instanceof JQMPage)) refreshTrackTheme();
        }
    }

}
