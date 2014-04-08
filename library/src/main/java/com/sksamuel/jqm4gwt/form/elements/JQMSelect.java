package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIcon;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasNative;
import com.sksamuel.jqm4gwt.HasPreventFocusZoom;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.form.JQMFieldContainer;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 10:58:58
 * <p/> An implementation of a jQuery mobile select element.
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.2/selectmenu/">Select menu</a>
 */
public class JQMSelect extends JQMFieldContainer implements HasNative<JQMSelect>, HasText<JQMSelect>,
        HasFocusHandlers, HasChangeHandlers, HasClickHandlers, HasTapHandlers, HasValue<String>,
        JQMFormWidget, HasIcon<JQMSelect>, HasPreventFocusZoom, HasCorners<JQMSelect>,
        HasMini<JQMSelect>, Focusable {


    public static class Option {
        private String value;
        private String text;
        private boolean placeholder;
        private boolean selected;
        private boolean disabled;

        public Option() {}

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(boolean placeholder) {
            this.placeholder = placeholder;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
    }

    private static final String SELECT_STYLENAME = "jqm4gwt-select";

    private final ListBox select;

    private final FormLabel label;

    private boolean valueChangeHandlerInitialized;

    /**
     * Creates a new {@link JQMSelect} with no label text.
     */
    public JQMSelect() {
        this(null);
    }

    /**
     * Creates a new {@link JQMSelect}with the given label text.
     */
    public JQMSelect(String text) {
        String id = Document.get().createUniqueId();

        label = new FormLabel();
        label.setFor(id);
        add(label);

        select = new ListBox(false);
        select.getElement().setId(id);
        add(select);

        setText(text);
        addStyleName(SELECT_STYLENAME);
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return select.addBlurHandler(handler);
    }

    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return select.addChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return select.addClickHandler(handler);
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
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return addDomHandler(handler, FocusEvent.getType());
    }

    @UiChild
    public void addOption(Option option) {
        addOption(option.value, option.text, option.isPlaceholder(), option.isSelected(),
                  option.isDisabled());
    }

    /**
     * Adds an option with the given text. The text is also used as the value.
     * The option is added at the end of the list of options.
     * <p/>
     * If you want to specify a value diferent from the display text, then
     * invoke addOption(String, String).
     */
    public void addOption(String text) {
        addOption(text, text);
    }

    private void addOption(String value, String text, boolean placeholder, boolean selected,
                           boolean disabled) {
        select.addItem(text, value);

        if (value == null || placeholder || selected || disabled) {
            SelectElement selElt = select.getElement().cast();
            NodeList<OptionElement> opts = selElt.getOptions();
            OptionElement opt = opts.getItem(opts.getLength() - 1);
            if (value == null) JQMCommon.setAttribute(opt, "value", null);
            if (placeholder) JQMCommon.setAttribute(opt, "data-placeholder", "true");
            if (selected) JQMCommon.setAttribute(opt, "selected", "selected");
            if (disabled) JQMCommon.setAttribute(opt, "disabled", "disabled");
        }
    }

    /**
     * Adds an option with the given value and text. The option is added at
     * the end of the list of options.
     */
    public void addOption(String value, String text) {
        addOption(value, text, false/*placeholder*/, false/*selected*/, false/*disabled*/);
    }

    public int getOptionCount() {
        return select.getItemCount();
    }

    public String getOptionText(int index) {
        return select.getItemText(index);
    }

    public String getOptionValue(int index) {
        return select.getValue(index);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        // Initialization code
        if (!valueChangeHandlerInitialized) {
            valueChangeHandlerInitialized = true;
            addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    ValueChangeEvent.fire(JQMSelect.this, getValue());
                }
            });
        }
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public IconPos getIconPos() {
        return JQMCommon.getIconPos(select);
    }

    /** Sets the position of the icon. */
    @Override
    public void setIconPos(IconPos pos) {
        JQMCommon.setIconPos(select, pos);
    }

    /** Sets the position of the icon. */
    @Override
    public JQMSelect withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

    @Override
    public boolean isMini() {
        return JQMCommon.isMini(select);
    }

    /** If set to true then renders a smaller version of the standard-sized element. */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(select, mini);
    }

    /** If set to true then renders a smaller version of the standard-sized element. */
    @Override
    public JQMSelect withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    /**
     * Returns the index of the currently selected option
     */
    public int getSelectedIndex() {
        return getSelectedIndex(select.getElement().getId());
    }

    private native int getSelectedIndex(String id) /*-{
        var select = $wnd.$("select#" + id);
        return select.length > 0 ? select[0].selectedIndex : -1;
    }-*/;

    public String getSelectedValue() {
        return getValue();
    }

    @Override
    public int getTabIndex() {
        return select.getTabIndex();
    }

    @Override
    public String getText() {
        return label.getText();
    }

    /**
     * Returns the currently selected value
     */
    @Override
    public String getValue() {
        return getValue(getSelectedIndex());
    }

    /**
     * Returns the value at the given index
     */
    public String getValue(int index) {
        return index == -1 ? null : select.getValue(index);
    }

    /**
     * Returns the index of the first option that matches the given value or
     * -1 if no such option exists.
     */
    public int indexOf(String value) {
        for (int k = 0; k < select.getItemCount(); k++) {
            String v = select.getValue(k);
            if (value == null) {
                if (v == null) return k;
            } else if (value.equals(v)) {
                return k;
            }
        }
        return -1;
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
    public JQMSelect withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    public boolean isMultiple() {
        String s = JQMCommon.getAttribute(select, "multiple");
        return s != null && !s.isEmpty() && "multiple".equals(s);
    }

    /**
     * Sets this select to allow mulitple selections.
     */
    public void setMulitple(boolean value) {
        JQMCommon.setAttribute(select, "multiple", value ? "multiple" : null);
    }

    /**
     * Returns true if the select is set to render in native mode.
     */
    @Override
    public boolean isNative() {
        // an element is native by default. So this returns true if native is not set to false.
        String s = JQMCommon.getAttribute(select, "data-native-menu");
        if (s == null || s.isEmpty()) return true;
        return !"false".equals(s);
    }

    @Override
    public void setNative(boolean value) {
        JQMCommon.setAttribute(select, "data-native-menu", value ? null : "false");
    }

    @Override
    public JQMSelect withNative(boolean value) {
        setNative(value);
        return this;
    }

    @Override
    public boolean isPreventFocusZoom() {
        return "true".equals(JQMCommon.getAttribute(select, "data-prevent-focus-zoom"));
    }

    /**
     * This option disables page zoom temporarily when a custom select is focused,
     * which prevents iOS devices from zooming the page into the select.
     * By default, iOS often zooms into form controls, and the behavior is often
     * unnecessary and intrusive in mobile-optimized layouts.
     */
    @Override
    public void setPreventFocusZoom(boolean b) {
        JQMCommon.setAttribute(select, "data-prevent-focus-zoom", b ? "true" : null);
    }

    public boolean isShadow() {
        String s = JQMCommon.getAttribute(select, "data-shadow");
        if (s == null || s.isEmpty()) return true;
        return !"false".equals(s);
    }

    public void setShadow(boolean value) {
        JQMCommon.setAttribute(select, "data-shadow", value ? null : "false");
    }

    /** Programmatically close an open select menu */
    public void close() {
        close(select.getElement().getId());
    }

    private native void close(String id) /*-{
        $wnd.$("#" + id).selectmenu('close');
    }-*/;

    /** Programmatically open a select menu */
    public void open() {
        open(select.getElement().getId());
    }

    private native void open(String id) /*-{
        $wnd.$("#" + id).selectmenu('open');
    }-*/;

    /**
     * Refreshes the select after a programmatic change has taken place.
     */
    public void refresh() {
        refresh(select.getElement());
    }

    private native void refresh(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-selectmenu') !== undefined) {
            w.selectmenu('refresh');
        }
    }-*/;

    /**
     * Remove the first option that matches the given value
     */
    public void removeOption(String value) {
        int indexOf = indexOf(value);
        if (indexOf >= 0)
            select.removeItem(indexOf);
    }

    public void clear() {
    	select.clear();
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(select);
    }

    @Override
    public void setTheme(String themeName) {
    	JQMCommon.applyTheme(select, themeName);
    }

    @Override
    public JQMWidget withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    @Override
    public void setAccessKey(char key) {
        select.setAccessKey(key);
    }

    @Override
    public void setFocus(boolean focused) {
        select.setFocus(focused);
    }

    @Override
    public JQMSelect removeIcon() {
        JQMCommon.setIcon(select, null);
        return this;
    }

    /**
     * Sets the icon used by this button. See {@link DataIcon}.
     */
    @Override
    public void setBuiltInIcon(DataIcon icon) {
        JQMCommon.setIcon(select, icon);
    }

    @Override
    public void setIconURL(String src) {
        if (src == null)
            removeIcon();
        else
            select.getElement().setAttribute("data-icon", src);
    }

    /**
     * Sets the icon used by this button. See {@link DataIcon}.
     */
    @Override
    public JQMSelect withBuiltInIcon(DataIcon icon) {
        setBuiltInIcon(icon);
        return this;
    }

    @Override
    public JQMSelect withIconURL(String src) {
        setIconURL(src);
        return this;
    }

    public String getOverlayTheme() {
        return JQMCommon.getAttribute(select, "data-overlay-theme");
    }

    /**
     * Sets the color of the overlay layer for the dialog-based custom select menus
     * and the outer border of the smaller custom menus.
     */
    public void setOverlayTheme(String theme) {
        JQMCommon.setAttribute(select, "data-overlay-theme", theme);
    }

    /**
     * Change the selection to the option at the given index.
     * <p/> Setting the selected index programmatically does <em>NOT</em>
     * cause the {@link ChangeHandler#onChange(ChangeEvent)}
     * nor {@link ValueChangeHandler#onValueChange(ValueChangeEvent)}
     * events to be fired.
     * <p/> Call {@link JQMSelect#setValue(String, boolean)} with <b>true</b> if you need them raised.
     */
    public void setSelectedIndex(int index) {
        select.setSelectedIndex(index);
        refresh();
    }

    public void setSelectedValue(String value, boolean ignoreCase) {
        for (int k = 0; k < select.getItemCount(); k++) {
            String v = select.getValue(k);
            if (v == null) {
                if (value == null) {
                    setSelectedIndex(k);
                    return;
                }
                continue;
            }
            boolean eq = ignoreCase ? v.equalsIgnoreCase(value) : v.equals(value);
            if (eq) {
                setSelectedIndex(k);
                return;
            }
        }
    }

    @Override
    public void setTabIndex(int index) {
        select.setTabIndex(index);
    }

    /**
     * Set the text of the label element.
     */
    @Override
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public JQMSelect withText(String text) {
        setText(text);
        return this;
    }

    /**
     * Sets the selected value to the given value. If no option matches the
     * given value then the selected is removed.
     */
    @Override
    public void setValue(String value) {
        setValue(value, false);
    }

    /**
     * Sets the selected value to the given value. If no option matches the
     * given value then the selected is removed.
     */
    @Override
    public void setValue(String value, boolean fireEvents) {
        int newIdx = value == null ? -1 : indexOf(value);
        int oldIdx = fireEvents ? getSelectedIndex() : -1;
        setSelectedIndex(newIdx);
        if (fireEvents) {
            newIdx = getSelectedIndex();
            if (oldIdx != newIdx) {
                ValueChangeEvent.fire(this, getValue(newIdx));
            }
        }
    }

    public String getCloseText() {
        return JQMCommon.getAttribute(select, "data-close-text");
    }

    /**
     * Customizes the text of the close button which is helpful for translating this
     * into different languages. The close button is displayed as an icon-only button
     * by default so the text isn't visible on-screen, but is read by screen readers
     * so this is an important accessibility feature.
     */
    public void setCloseText(String value) {
        JQMCommon.setAttribute(select, "data-close-text", value);
    }

    public boolean getSelectInline() {
        return JQMCommon.isInline(select);
    }

    /** If set to true, this will make the select/dropdown act like an inline widget
     * so the width is determined by the widget's text.
     */
    public void setSelectInline(boolean value) {
        JQMCommon.setInline(select, value);
    }

    public boolean isHidePlaceholderMenuItems() {
        String s = JQMCommon.getAttribute(select, "data-hide-placeholder-menu-items");
        if (s == null || s.isEmpty()) return true;
        return !"false".equals(s);
    }

    /**
     * Default is true.
     * <p/> Sets whether placeholder menu items are hidden.
     * When true, the menu item used as the placeholder for the select menu widget
     * will not appear in the list of choices.
     */
    public void setHidePlaceholderMenuItems(boolean value) {
        JQMCommon.setAttribute(select, "data-hide-placeholder-menu-items", value ? null : "false");
    }

    @Override
    protected Widget getDataFilterWidget() {
        return select;
    }

}
