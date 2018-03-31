package com.sksamuel.jqm4gwt.form.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIcon;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasNative;
import com.sksamuel.jqm4gwt.HasPreventFocusZoom;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
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
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 10:58:58
 * <br> An implementation of a jQuery mobile select element.
 * <br> See <a href="http://demos.jquerymobile.com/1.4.5/selectmenu/">Select menu</a>
 */
public class JQMSelect extends JQMFieldContainer implements HasNative<JQMSelect>, HasText<JQMSelect>,
        HasFocusHandlers, HasChangeHandlers, HasClickHandlers, HasTapHandlers, HasValue<String>,
        JQMFormWidget, HasIcon<JQMSelect>, HasPreventFocusZoom, HasCorners<JQMSelect>,
        HasMini<JQMSelect>, Focusable {

    /** JQMSelect non-native popup window has close button on the left side,
     *  but it could be changed globally to the right side.
     **/
    public static boolean closeBtnPosRight = false;

    public static class Option {
        private String value;
        private String text;
        private String filterText;
        private boolean placeholder;
        private boolean selected;
        private boolean disabled;

        // Icons are supported by JQMSelectWithIcons
        private DataIcon icon;
        private String customIcon;

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

        public DataIcon getIcon() {
            return icon;
        }

        public void setIcon(DataIcon icon) {
            this.icon = icon;
        }

        public String getCustomIcon() {
            return customIcon;
        }

        public void setCustomIcon(String customIcon) {
            this.customIcon = customIcon;
        }

        public String getFilterText() {
            return filterText;
        }

        public void setFilterText(String filterText) {
            this.filterText = filterText;
        }
    }

    public static interface ChangeOptionsAction {
        /**
         * Something like that:
         * <pre>
         * if (!select.isEmpty()) select.clear();
         * select.beginAddOptions();
         * try {
         *     select.addOption(...);
         *     return true;
         * } finally {
         *     select.endAddOptions();
         * }
         * </pre>
         *
         * @return - must return true in case options were changed
         */
        boolean doChange(JQMSelect select);
    }

    private static final String SELECT_STYLENAME = "jqm4gwt-select";

    protected class ListBoxEx extends ListBox {

        public ListBoxEx() {
        }

        @Override
        public void setOptionText(OptionElement option, String text, Direction dir) {
            super.setOptionText(option, text, dir);
        }

        @Override
        public String getOptionText(OptionElement option) {
            return super.getOptionText(option);
        }
    }

    protected final ListBoxEx select;
    protected Integer mandatorySelIdx; // See checkSelectedIndex()

    /** Unique search index: value, index in select */
    protected final Map<String, Integer> selectIdx = new HashMap<String, Integer>(); // search index
    private boolean nonUniqueFindFirst;

    protected final FormLabel label;

    private Element uiSelect;

    private String menuStyleNames;

    private static boolean jsServed;

    private boolean valueChangeHandlerInitialized;
    private boolean blurHandlerAdded;

    private boolean transparent = true;
    private Element transparentPrevPage;
    private boolean transparentPrevPageClearCache;
    private boolean transparentDoPrevPageLifecycle;

    /** See {@link JQMSelect#getDelayedValue()} */
    private String delayedValue;
    private Boolean delayedFireEvents;
    private boolean addingOptions;
    private ArrayList<OptionElement> addingOptionList;

    private String multiValueSeparator = ",";

    public static String CLEAR_BUTTON_TEXT = "-----";
    public static String SELECT_ALL_BUTTON_TEXT = "All";

    private String clearButtonText;
    private boolean showClearButton;

    private String selectAllButtonText;
    private boolean showSelectAllButton;

    private boolean showTextOnPopup = true;

    /**
     * Creates a new {@link JQMSelect} with no label text.
     */
    public JQMSelect() {
        this(null);
    }

    /**
     * Creates a new {@link JQMSelect} with the given label text.
     */
    public JQMSelect(String text) {
        String id = Document.get().createUniqueId();

        label = new FormLabel();
        label.setFor(id);
        add(label);

        select = new ListBoxEx();
        select.getElement().setId(id);
        add(select);
        addChangeHandler(event -> changed());
        setText(text);
        addStyleName(SELECT_STYLENAME);
    }

    protected void changed() {
        if (!isMultiple()) mandatorySelIdx = select.getSelectedIndex();
        fireValueChange(getValue());
    }

    private void fireValueChange(String value) {
        if (valueChangeHandlerInitialized) {
            ValueChangeEvent.fire(this, value);
        }
        if (isAttached() && blurHandlerAdded && !isNative() && isInstance(select.getElement())) {
            // TODO: could be fixed in next versions of jqm, but in 1.4.5 this problem exists
            // For example to rerun JQMForm's validation for this dropdown
            DomEvent.fireNativeEvent(Document.get().createBlurEvent(), select);
        }
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        blurHandlerAdded = true;
        return select.addBlurHandler(handler);
    }

    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return select.addChangeHandler(handler);
    }

    private static native void repositionPopup(String id) /*-{
        var p = $wnd.$("#" + id + "-listbox");
        if (p.data('mobile-popup') !== undefined) {
            var $link = $wnd.$("#" + id + "-button");
            var offset = $link.offset();
            var opt = {
                x: offset.left + $link.outerWidth() / 2,
                y: offset.top + $link.outerHeight() / 2,
                transition: $link.jqmData("transition"),
                positionTo: $link.jqmData("position-to")
            };
            p.popup("reposition", opt);
        }
    }-*/;

    /**
     * Should be used for proper popup positioning in case of lazy/delayed population on first click to JQMSelect, like:
     * <pre>{@code
     * combo.addClickHandler(event -> Scheduler.get().scheduleDeferred(() ->
     *     fetchAndPopulateOptions(done -> combo.repositionPopup())));
     * }</pre>
     */
    public void repositionPopup() {
        String id = getSelectElt().getId();
        if (!Empty.is(id)) repositionPopup(id);
    }

    @Override
    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        if (handler == null) return null;
        return isNative() ? select.addClickHandler(handler)
                : flow.addDomHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        EventTarget target = event.getNativeEvent().getEventTarget();
                        if (target != null) {
                            Element elt = target.cast();
                            findUiSelect();
                            if (uiSelect != null) {
                                Element btn = JQMCommon.findChild(uiSelect, "ui-btn");
                                if (btn != null) {
                                    while (elt != null) { // filter out non-button related elements (label for instance)
                                        if (elt == btn) {
                                            handler.onClick(event);
                                            return;
                                        }
                                        elt = elt.getParentElement();
                                    }
                                }
                            }
                        }
                    }
                }, ClickEvent.getType());
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
        addOption(option.value, option.text, option.filterText,
                  option.isPlaceholder(), option.isSelected(), option.isDisabled(),
                  option.getIcon(), option.getCustomIcon());
    }

    /**
     * Adds an option with the given text. The text is also used as the value.
     * The option is added at the end of the list of options.
     * <br>
     * If you want to specify a value diferent from the display text, then
     * invoke addOption(String, String).
     */
    public void addOption(String text) {
        addOption(text, text);
    }

    public void addOptions(String... texts) {
        if (texts != null) {
            for (String s : texts) addOption(s, s);
        }
    }

    /**
     * @param values - comma separated list of option values, i.e. dropdown values. Useful for UiBinder.
     */
    public void setOptionValues(String values) {
        if (values == null || values.isEmpty()) {
            clear();
        } else {
            String[] arr = values.split(",");
            for (int i = 0; i < arr.length; i++) {
                addOption(arr[i], arr[i]);
            }
        }
    }

    private static void prepareOption(OptionElement opt, String value, String filterText,
                           boolean placeholder, boolean selected, boolean disabled,
                           DataIcon icon, String customIcon) {

        if (value == null) JQMCommon.setAttribute(opt, "value", null);
        if (filterText != null && !filterText.isEmpty()) {
            JQMCommon.setFilterText(opt, filterText);
        }
        if (placeholder) JQMCommon.setAttribute(opt, "data-placeholder", "true");
        if (selected) JQMCommon.setAttribute(opt, "selected", "selected");
        if (disabled) JQMCommon.setAttribute(opt, "disabled", "disabled");
        if (icon != null) JQMCommon.setIcon(opt, icon);
        else if (customIcon != null) JQMCommon.setIcon(opt, customIcon);
    }

    private void addToSelectIdx(String key, Integer value) {
        if (nonUniqueFindFirst) {
            if (!selectIdx.containsKey(key)) { // preserves first non-unique item
                selectIdx.put(key, value);
            }
        } else { // overwrites already existing, i.e. last one has higher priority than first one.
            selectIdx.put(key, value);
        }
    }

    public void addOption(String value, String text, String filterText,
                          boolean placeholder, boolean selected,
                          boolean disabled, DataIcon icon, String customIcon) {

        if (addingOptions) {
            OptionElement opt = Document.get().createOptionElement();
            select.setOptionText(opt, text, null/*dir*/);
            opt.setValue(value);
            prepareOption(opt, value, filterText, placeholder, selected, disabled, icon, customIcon);
            addingOptionList.add(opt);
        } else {
            select.addItem(text, value);
            final int i;
            if (value == null || (filterText != null && !filterText.isEmpty())
                    || placeholder || selected || disabled || icon != null || customIcon != null) {
                SelectElement selElt = select.getElement().cast();
                NodeList<OptionElement> opts = selElt.getOptions();
                i = opts.getLength() - 1;
                OptionElement opt = opts.getItem(i);
                prepareOption(opt, value, filterText, placeholder, selected, disabled, icon, customIcon);
                addToSelectIdx(opt.getValue(), i);
            } else {
                i = select.getItemCount() - 1;
                String v = select.getValue(i);
                addToSelectIdx(v, i);
            }
            if (i == 0) {
                if (delayedValue == null) {
                    if (!isMultiple()) internSetSelectedIndex(0); // for compatibility with previous versions
                } else {
                    checkSelectedIndex();
                }
            } else {
                checkSelectedIndex();
            }
            if (delayedValue != null) tryResolveDelayed();
        }
    }

    protected void rebuildSearchIndex() {
        selectIdx.clear();
        SelectElement selElt = select.getElement().cast();
        NodeList<OptionElement> opts = selElt.getOptions();
        for (int i = 0; i < opts.getLength(); i++) {
            String v = opts.getItem(i).getValue();
            addToSelectIdx(v, i);
        }
    }

    /**
     * Improves performance in case of mass/long options list population, always use with try..finally block.
     */
    public void beginAddOptions(int initialCapacity) {
        addingOptions = true;
        addingOptionList = initialCapacity < 0 ? new ArrayList<OptionElement>()
                                               : new ArrayList<OptionElement>(initialCapacity);
    }

    public void beginAddOptions() {
        beginAddOptions(-1);
    }

    public void endAddOptions() {
        addingOptions = false;
        if (!addingOptionList.isEmpty()) {
            SelectElement selElt = select.getElement().cast();
            int i = selElt.getOptions().getLength();
            for (OptionElement opt : addingOptionList) {
                selElt.add(opt, null/*to tail*/);
                addToSelectIdx(opt.getValue(), i);
                i++;
            }
            checkSelectedIndex();
        }
        addingOptionList = null;
        if (delayedValue != null) tryResolveDelayed();
    }

    /** May be needed in ChangeOptionsAction.doChange() implementation */
    public SelectElement getSelectElt() {
        return select.getElement().cast();
    }

    public void massChangeOptions(ChangeOptionsAction action) {
        if (action == null) return;
        String v = getValue();
        final String val = v != null ? v : getDelayedValue();
        boolean changed = action.doChange(this);
        if (!changed) return;
        rebuildSearchIndex();
        if (val != null) {
            if (!val.equals(getValue())) {
                setValue(val, false/*fireEvents*/);
            }
        } else {
            setSelectedIndex(-1); // any addOption() call resets selectedIndex from -1 to 0, so we have to fix that
        }
    }

    public void addOption(String value, String text, String filterText,
                          boolean placeholder, boolean selected, boolean disabled) {
        addOption(value, text, filterText, placeholder, selected, disabled,
                  null/*icon*/, null/*customIcon*/);
    }

    public void addOption(String value, String text, String filterText) {
        addOption(value, text, filterText, false/*placeholder*/, false/*selected*/, false/*disabled*/);
    }

    /**
     * Adds an option with the given value and text. The option is added at
     * the end of the list of options.
     */
    public void addOption(String value, String text) {
        addOption(value, text, null/*filterText*/);
    }

    public JQMSelect withOption(String value, String text) {
        addOption(value, text);
        return this;
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

    public String getOptionIcon(String optValue) {
        if (optValue == null) return null;
        List<OptionElement> opts = getOptions();
        for (OptionElement opt : opts) {
            if (optValue.equals(opt.getValue())) {
                return JQMCommon.getCustomIcon(opt);
            }
        }
        return null;
    }

    public List<OptionElement> getOptions() {
        SelectElement selElt = select.getElement().cast();
        NodeList<OptionElement> opts = selElt.getOptions();
        List<OptionElement> rslt = new ArrayList<OptionElement>(opts != null ? opts.getLength() : 0);
        if (opts != null) {
            for (int i = 0; i < opts.getLength(); i++) {
                OptionElement opt = opts.getItem(i);
                rslt.add(opt);
            }
        }
        return rslt;
    }

    public OptionElement getOption(int index) {
        if (index < 0) return null;
        SelectElement selElt = select.getElement().cast();
        NodeList<OptionElement> opts = selElt.getOptions();
        return index < opts.getLength() ? opts.getItem(index) : null;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        if (!valueChangeHandlerInitialized) valueChangeHandlerInitialized = true;
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public IconPos getIconPos() {
        return JQMCommon.getIconPos(select);
    }

    /**
     * Sets the position of the icon.
     * @param pos - if null means set default value, i.e. RIGHT.
     **/
    @Override
    public void setIconPos(IconPos pos) {
        JQMCommon.setIconPos(select, pos);
        final String prefix = JQMCommon.STYLE_UI_BTN_ICONPOS;
        String newPos = pos != null ? prefix + pos.getJqmValue() : prefix + IconPos.RIGHT.getJqmValue();
        refreshIconPos(getElement(), prefix, newPos);
    }

    public void setIconPosNone(boolean value) {
        if (!value) {
            setIconPos(null);
            return;
        }
        JQMCommon.setAttribute(select, JQMCommon.DATA_ICONPOS, "");
        refreshIconPos(getElement(), JQMCommon.STYLE_UI_BTN_ICONPOS, "");
    }

    public boolean isIconPosNone() {
        if (!select.getElement().hasAttribute(JQMCommon.DATA_ICONPOS)) return false;
        String v = JQMCommon.getAttribute(select, JQMCommon.DATA_ICONPOS);
        return "".equals(v);
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
        checkSelectedIndex();
        return select.getSelectedIndex();
    }

    /**
     * @return - true if selectedIndex has been fixed, refresh() must be called manually in that case.
     *
     * <br><br> Unfortunately jqm likes to change selectedIndex from -1 to 0 (for example it happens
     * on initialization, i.e. selectmenucreate). Also this behavior is browser specific, so
     * we need this guard method to make sure that null value is supported properly.
     *
     * <br><b>IE11 needs</b> much more attention, because even if selectedIndex set/fixed to -1,
     * browser anyway always considers item0 as selected, so choosing first item from dropdown list is not
     * working for empty combo. <br> The only way to <b>solve this</b> - always add {@code<option value="" text=""></option>}
     * to JQMSelect. <br> See special logic in getValue(0) for returning null in such case.
     */
    protected boolean checkSelectedIndex() {
        if (mandatorySelIdx == null) return false;
        int i = select.getSelectedIndex();
        if (mandatorySelIdx != i) {
            if (isMultiple()) return false;
            select.setSelectedIndex(mandatorySelIdx);
            return true;
        } else {
            return false;
        }
    }

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
        if (!isMultiple()) {
            return getValue(getSelectedIndex());
        } else {
            SelectElement selElt = select.getElement().cast();
            NodeList<OptionElement> opts = selElt.getOptions();
            StringBuilder sb = null;
            for (int i = 0; i < opts.getLength(); i++) {
                OptionElement opt = opts.getItem(i);
                if (opt.isSelected()) {
                    String v = opt.getValue();
                    if (sb == null) {
                        sb = new StringBuilder();
                        sb.append(v);
                    } else {
                        sb.append(multiValueSeparator);
                        sb.append(v);
                    }
                }
            }
            if (sb == null) return null;
            else {
                String rslt = sb.toString();
                if (rslt.isEmpty()) rslt = null;
                return rslt;
            }
        }
    }

    /**
     * Returns the currently selected text (makes sense if options are populated by pairs: value & text.
     */
    public String getSelectedText() {
        if (!isMultiple()) {
            int idx = getSelectedIndex();
            return idx == -1 ? null : getOptionText(idx);
        } else {
            SelectElement selElt = select.getElement().cast();
            NodeList<OptionElement> opts = selElt.getOptions();
            StringBuilder sb = null;
            for (int i = 0; i < opts.getLength(); i++) {
                OptionElement opt = opts.getItem(i);
                if (opt.isSelected()) {
                    String v = select.getOptionText(opt);
                    if (sb == null) {
                        sb = new StringBuilder();
                        sb.append(v);
                    } else {
                        sb.append(multiValueSeparator);
                        sb.append(v);
                    }
                }
            }
            if (sb == null) return null;
            else {
                String rslt = sb.toString();
                if (rslt.isEmpty()) rslt = null;
                return rslt;
            }
        }
    }

    private boolean isMultiValNotEmpty() {
        SelectElement selElt = select.getElement().cast();
        NodeList<OptionElement> opts = selElt.getOptions();
        for (int i = 0; i < opts.getLength(); i++) {
            OptionElement opt = opts.getItem(i);
            if (opt.isSelected()) return true;
        }
        return false;
    }

    /**
     * Returns the value at the given index
     */
    public String getValue(int index) {
        if (index == -1) return null;
        String v = select.getValue(index);
        if (index == 0 && v != null && v.isEmpty()) return null;
        return v;
    }

    /**
     * setValue() can be called before options are populated (or in the middle of their population).
     * For example: asynchronous options loading or data binding scenarios.
     * <br> if value != null and cannot be resolved immediately through current options,
     * it will be memorized and probably resolved later, when more options are added.
     * <br> On successful resolution regular setValue() will be called (and events fired if it was requested).
     * <br> Calling clear() resets delayedValue processing.
     */
    public String getDelayedValue() {
        return delayedValue;
    }

    /**
     * Returns the index of the last option (or first, see {@link JQMSelect#setNonUniqueFindFirst(boolean)})
     * that matches the given value or -1 if no such option exists.
     */
    public int indexOf(String value) {
        if (value == null) return -1;
        Integer i = selectIdx.get(value);
        return i == null ? -1 : i;
    }

    public boolean isNonUniqueFindFirst() {
        return nonUniqueFindFirst;
    }

    /** if select contains non-unique items, this property controls how indexOf() search will be working. */
    public void setNonUniqueFindFirst(boolean value) {
        if (this.nonUniqueFindFirst != value) {
            this.nonUniqueFindFirst = value;
            if (!selectIdx.isEmpty()) rebuildSearchIndex();
        }
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
     * Sets this select to allow multiple selections.
     */
    public void setMultiple(boolean value) {
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
        close(select.getElement());
    }

    private static native void close(Element elt) /*-{
        $wnd.$(elt).selectmenu('close');
    }-*/;

    /** Programmatically open a select menu */
    public void open() {
        open(select.getElement());
    }

    private static native void open(Element elt) /*-{
        $wnd.$(elt).selectmenu('open');
    }-*/;

    /**
     * Refreshes the select after a programmatic options change has taken place.
     */
    public void refresh() {
        refresh(select.getElement());
    }

    private static native void refresh(Element elt) /*-{
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
        if (indexOf >= 0) {
            int i = getSelectedIndex();
            if (i == indexOf) internSetSelectedIndex(-1);
            select.removeItem(indexOf);
        }
    }

    /**
     * @return - true when there are <b>no value</b> and <b>no options</b> defined.
     */
    public boolean isEmpty() {
        return getSelectedIndex() == -1 && getOptionCount() == 0;
    }

    /**
     * Clears all (options too). Then refresh() should be called to update visual state.
     */
    public void clear() {
        clear(false/*delayValue*/);
    }

    public void clear(boolean delayValue) {
        if (!delayValue) clearDelayed();
        else {
            String v = getValue();
            if (v != null) {
                delayedValue = v;
                delayedFireEvents = false;
            }
            // else - delayedValue is not cleared
        }
        select.clear();
        selectIdx.clear();
        internSetSelectedIndex(-1);
    }

    private void clearDelayed() {
        delayedValue = null;
        delayedFireEvents = null;
    }

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(select);
    }

    private void findUiSelect() {
        if (uiSelect == null && isInstance(select.getElement())) {
            uiSelect = JQMCommon.findChild(getElement(), "ui-select");
        }
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.applyTheme(select, themeName);
        findUiSelect();
        if (uiSelect != null) {
            Element btn = JQMCommon.findChild(uiSelect, "ui-btn");
            if (btn != null) JQMButton.setTheme(btn, themeName);
        }
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
        String oldIcon = JQMCommon.getCustomIcon(select.getElement());
        oldIcon = oldIcon == null ? "" : JQMCommon.STYLE_UI_ICON + oldIcon;

        JQMCommon.setIcon(select, null);

        refreshIcon(getElement(), oldIcon, "");
        return this;
    }

    /**
     * Sets the icon used by this button. See {@link DataIcon}.
     */
    @Override
    public void setBuiltInIcon(DataIcon icon) {
        String oldIcon = JQMCommon.getCustomIcon(select.getElement());
        oldIcon = oldIcon == null ? "" : JQMCommon.STYLE_UI_ICON + oldIcon;

        JQMCommon.setIcon(select, icon);

        String newIcon = JQMCommon.getCustomIcon(select.getElement());
        newIcon = newIcon == null ? "" : JQMCommon.STYLE_UI_ICON + newIcon;
        refreshIcon(getElement(), oldIcon, newIcon);
    }

    @Override
    public void setIconURL(String src) {
        String oldIcon = JQMCommon.getCustomIcon(select.getElement());
        oldIcon = oldIcon == null ? "" : JQMCommon.STYLE_UI_ICON + oldIcon;

        JQMCommon.setIcon(select.getElement(), src);

        String newIcon = JQMCommon.getCustomIcon(select.getElement());
        newIcon = newIcon == null ? "" : JQMCommon.STYLE_UI_ICON + newIcon;
        refreshIcon(getElement(), oldIcon, newIcon);
    }

    private static native void refreshIcon(Element elt, String oldIcon, String newIcon) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$(elt).children().find('.ui-btn').removeClass(oldIcon).addClass(newIcon);
    }-*/;

    private static native void refreshIconPos(Element elt, String iconPosPrefix, String newIconPos) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var s = '(^|\\s)' + iconPosPrefix + '\\S+';
        var regex = new RegExp(s, 'g');
        $wnd.$(elt).children().find('.ui-btn').removeClass(function(index, className) {
            return (className.match(regex) || []).join(' ');
        }).addClass(newIconPos);
    }-*/;

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

    public boolean isIconNoDisc() {
        return JQMCommon.isIconNoDisc(this);
    }

    public void setIconNoDisc(boolean value) {
        JQMCommon.setIconNoDisc(this, value);
    }

    public boolean isIconAlt() {
        return JQMCommon.isIconAlt(this);
    }

    /**
     * @param value - if true "white vs. black" icon style will be used
     */
    public void setIconAlt(boolean value) {
        JQMCommon.setIconAlt(this, value);
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
     * Change the selection to the option at the given index, refresh() is called automatically.
     * <br> Setting the selected index programmatically does <em>NOT</em>
     * cause the {@link ChangeHandler#onChange(ChangeEvent)}
     * nor {@link ValueChangeHandler#onValueChange(ValueChangeEvent)}
     * events to be fired.
     * <br> Call {@link JQMSelect#setValue(String, boolean)} with <b>true</b> if you need them raised.
     */
    public void setSelectedIndex(int index) {
        clearDelayed();
        internSetSelectedIndex(index);
        refresh();
    }

    protected void internSetSelectedIndex(int index) {
        select.setSelectedIndex(index);
        mandatorySelIdx = index;
    }

    public void setSelectedValue(String value, boolean ignoreCase) {
        if (value == null) {
            setSelectedIndex(-1);
            return;
        }
        if (!ignoreCase) {
            int i = indexOf(value);
            setSelectedIndex(i);
            return;
        } else {
            int i = indexOf(value);
            if (i >= 0) {
                setSelectedIndex(i);
                return;
            }
        }
        // Can be optimized with search index as well, but it's expensive to support additional index
        for (int k = 0; k < select.getItemCount(); k++) {
            String v = select.getValue(k);
            boolean eq = value.equalsIgnoreCase(v);
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
        if (!isMultiple()) {
            int newIdx = value == null ? -1 : indexOf(value);
            setNewSelectedIndex(newIdx, fireEvents);
            if (newIdx == -1 && value != null) {
                delayedValue = value;
                delayedFireEvents = fireEvents;
            }
        } else {
            if (value == null || value.isEmpty() || value.trim().isEmpty()) {
                boolean oldValNotEmpty = fireEvents ? isMultiValNotEmpty() : false;
                boolean changed = unselectAllOptions();
                clearDelayed();
                if (changed) refresh();
                if (fireEvents && oldValNotEmpty) fireValueChange(null);
            } else {
                if (getOptionCount() == 0) {
                    delayedValue = value;
                    delayedFireEvents = fireEvents;
                } else {
                    Set<String> newVals = new HashSet<String>();
                    if (!verifyMultiValue(value, newVals)) {
                        boolean oldValNotEmpty = fireEvents ? isMultiValNotEmpty() : false;
                        boolean changed = unselectAllOptions();
                        delayedValue = value;
                        delayedFireEvents = fireEvents;
                        if (changed) refresh();
                        if (fireEvents && oldValNotEmpty) fireValueChange(null);
                        return;
                    }
                    setNewMultiVals(newVals, fireEvents);
                }
            }
        }
    }

    private void setNewSelectedIndex(int newIdx, boolean fireEvents) {
        int oldIdx = fireEvents ? getSelectedIndex() : -1;
        setSelectedIndex(newIdx);
        if (fireEvents) {
            newIdx = getSelectedIndex();
            if (oldIdx != newIdx) {
                fireValueChange(getValue(newIdx));
            }
        }
    }

    private void setNewMultiVals(Set<String> newVals, boolean fireEvents) {
        StringBuilder sb = fireEvents ? new StringBuilder() : null;
        boolean changed = selectOptions(newVals, sb);
        if (changed) refresh();
        if (fireEvents && changed && sb != null) {
            String newVal = sb.toString();
            if (newVal.isEmpty()) newVal = null;
            fireValueChange(newVal);
        }
    }

    private boolean verifyMultiValue(String value, Set<String> newVals) {
        if (!newVals.isEmpty()) newVals.clear();
        String[] arr = value.split(multiValueSeparator);
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].trim();
            if (s.isEmpty()) continue;
            int idx = indexOf(s);
            if (idx == -1) {
                return false;
            }
            newVals.add(s);
        }
        return true;
    }

    private boolean unselectAllOptions() {
        SelectElement selElt = select.getElement().cast();
        NodeList<OptionElement> opts = selElt.getOptions();
        boolean changed = false;
        for (int i = 0; i < opts.getLength(); i++) {
            OptionElement opt = opts.getItem(i);
            if (opt.isSelected()) {
                opt.setSelected(false);
                changed = true;
            }
        }
        return changed;
    }

    private boolean selectOptions(Set<String> selected, StringBuilder sbSelectedValues) {
        SelectElement selElt = select.getElement().cast();
        NodeList<OptionElement> opts = selElt.getOptions();
        boolean changed = false;
        for (int i = 0; i < opts.getLength(); i++) {
            OptionElement opt = opts.getItem(i);
            String v = opt.getValue();
            boolean sel = selected.contains(v);
            if (opt.isSelected() != sel) {
                opt.setSelected(sel);
                changed = true;
            }
            if (sbSelectedValues != null && sel) {
                if (sbSelectedValues.length() > 0) sbSelectedValues.append(multiValueSeparator);
                sbSelectedValues.append(v);
            }
        }
        return changed;
    }

    private void tryResolveDelayed() {
        if (delayedValue == null) return;
        if (!isMultiple()) {
            int newIdx = indexOf(delayedValue);
            if (newIdx == -1) return;
            boolean fireEvents = delayedFireEvents != null ? delayedFireEvents : false;
            setNewSelectedIndex(newIdx, fireEvents);
        } else {
            Set<String> newVals = new HashSet<String>();
            if (!verifyMultiValue(delayedValue, newVals)) return;
            boolean fireEvents = delayedFireEvents != null ? delayedFireEvents : false;
            clearDelayed();
            setNewMultiVals(newVals, fireEvents);
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
        findUiSelect();
        if (uiSelect != null) {
            JQMCommon.setInlineEx(uiSelect, value, JQMCommon.STYLE_UI_BTN_INLINE);
        }
    }

    public boolean isHidePlaceholderMenuItems() {
        String s = JQMCommon.getAttribute(select, "data-hide-placeholder-menu-items");
        if (s == null || s.isEmpty()) return true;
        return !"false".equals(s);
    }

    /**
     * Default is true.
     * <br> Sets whether placeholder menu items are hidden.
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

    private static native void bindCreated(Element elt, JQMSelect sel) /*-{
        $wnd.$(elt).on( 'selectmenucreate', function( event, ui ) {
            sel.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::created()();
        });
    }-*/;

    private static native void unbindCreated(Element elt) /*-{
        $wnd.$(elt).off( 'selectmenucreate' );
    }-*/;

    /**
     * Unfortunately it's not called in case of manual JQMContext.render(),
     * though widget is getting created and enhanced.
     */
    private void created() {
        if (checkSelectedIndex()) refresh();
    }

    private static native boolean isInstance(Element elt) /*-{
        var i = $wnd.$(elt).selectmenu("instance");
        if (i) return true;
        else return false;
    }-*/;

    @Override
    protected void onLoad() {
        super.onLoad();
        Element elt = select.getElement();
        bindCreated(elt, this);
        if (!jsServed) {
            jsServed = true;
            serveSelects();
        }
        if (isInstance(select.getElement())) {
            if (checkSelectedIndex()) refresh();
        }
    }

    @Override
    protected void onUnload() {
        Element elt = select.getElement();
        unbindCreated(elt);
        super.onUnload();
    }

    private static native boolean isPageSelectDialog(String pageId) /*-{
        var isDialog = false;
        var SEL = "." + @com.sksamuel.jqm4gwt.form.elements.JQMSelect::SELECT_STYLENAME
                      + " .ui-select > select";
        $wnd.$(SEL).each(function() {
            var id = $wnd.$(this).attr("id");
            if (id) {
                var s = id + "-dialog";
                if (s === pageId) {
                    isDialog = true;
                    return false;
                }
            }
        });
        return isDialog;
    }-*/;

    protected static JQMSelect findCombo(Element elt) {
        Widget w = JQMCommon.findWidget(elt);
        while (w != null) {
            if (w instanceof JQMSelect) return (JQMSelect) w;
            w = w.getParent();
        }
        return null;
    }

    private static native void serveSelects() /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded

        $wnd.$.widget( "mobile.selectmenu", $wnd.$.mobile.selectmenu, {
            _buildList: function() {
                this._super();
                if (this.isMultiple) {
                    var combo = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::findCombo(Lcom/google/gwt/dom/client/Element;)(this.select[0]);
                    if (combo && combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::doAssignPopupTitle()()) {
                        this.headerTitle.text(combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::getText()());
                    }
                }
            }
        });

        var SEL = "." + @com.sksamuel.jqm4gwt.form.elements.JQMSelect::SELECT_STYLENAME
                      + " .ui-select > select";
        $wnd.$.mobile.document
            // Upon creation of the select menu, we want to make use of the fact that the ID of the
            // listview it generates starts with the ID of the select menu itself, plus the suffix "-menu".
            .on( "selectmenucreate", SEL, function( event ) {
                // process additional CSS styling classes
                var combo = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::findCombo(Lcom/google/gwt/dom/client/Element;)
                                (event.target);
                var addnl = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::getMenuStyleNames()();
                if (addnl) {
                    var selectmenu = $wnd.$( event.target ),
                        id = selectmenu.attr( "id" ),
                        lb = $wnd.$( "#" + id + "-listbox" );
                    lb.addClass( addnl );
                }
                if (@com.sksamuel.jqm4gwt.form.elements.JQMSelect::closeBtnPosRight) {
                    var selectmenu = $wnd.$( event.target ),
                        id = selectmenu.attr( "id" ),
                        lb = $wnd.$( "#" + id + "-listbox" );

                    lb.children(".ui-header").first()
                        .children(".ui-btn.ui-btn-left.ui-icon-delete").first()
                        .removeClass("ui-btn-left").addClass("ui-btn-right");
                }
                if (combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::doAssignPopupTitle()()) {
                    var selectmenu = $wnd.$( event.target ),
                        id = selectmenu.attr( "id" ),
                        lb = $wnd.$( "#" + id + "-listbox" );

                    var title = lb.children(".ui-header").first().children(".ui-title").first();
                    title.text(combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::getText()());
                }
                var isClear = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::isShowClearButton()();
                if (isClear === true) {
                    var selectmenu = $wnd.$( event.target ),
                        id = selectmenu.attr( "id" ),
                        listview = $wnd.$( "#" + id + "-menu" );

                    var clearBtn = listview.jqmData( "clear-button" );
                    if (!clearBtn) {
                        var clearText = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::getClearButtonText()();
                        if (!clearText) clearText = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::CLEAR_BUTTON_TEXT;

                        clearBtn = $wnd.$("<button class='ui-btn ui-mini clear-button'>" //style='margin:0;border-bottom:0;border-left:0;border-right:0;text-align:left;'
                                          + clearText + "</button>");
                        clearBtn.on('click', function() {
                            combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::closeAndClearValue()();
                        });
                        listview.before( clearBtn ).jqmData( "clear-button", clearBtn );
                    }
                }
                var isSelectAll = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::isShowSelectAllButton()();
                if (isSelectAll === true) {
                    var selectmenu = $wnd.$( event.target ),
                        id = selectmenu.attr( "id" ),
                        listview = $wnd.$( "#" + id + "-menu" );

                    var selectAllBtn = listview.jqmData( "select-all-button" );
                    if (!selectAllBtn) {
                        var selectAllText = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::getSelectAllButtonText()();
                        if (!selectAllText) selectAllText = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::SELECT_ALL_BUTTON_TEXT;

                        selectAllBtn = $wnd.$("<button class='ui-btn ui-mini select-all-button'>" //style='margin:0;border-bottom:0;border-left:0;border-right:0;text-align:left;'
                                              + selectAllText + "</button>");
                        selectAllBtn.on('click', function() {
                            combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::closeAndSelectAll()();
                        });
                        listview.before( selectAllBtn ).jqmData( "select-all-button", selectAllBtn );
                    }
                }
            })
            // The custom select list may show up as either a popup or a dialog, depending on how much
            // vertical room there is on the screen. If it shows up as a dialog, then we have to
            // process transparent property.
            .on( "pagecontainerbeforeshow", function( event, data ) {
                var pageId = data.toPage && data.toPage.attr("id");
                // We only handle the appearance of a dialog generated by a selectmenu
                var isDlg = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::isPageSelectDialog(Ljava/lang/String;)
                                (pageId);
                if (!isDlg) return;
                var dialog = data.toPage;

                if (@com.sksamuel.jqm4gwt.form.elements.JQMSelect::closeBtnPosRight) {
                    dialog.children(".ui-dialog-contain").first()
                        .children(".ui-header").first()
                        .children(".ui-btn.ui-btn-left.ui-icon-delete").first()
                        .removeClass("ui-btn-left").addClass("ui-btn-right");
                }

                var comboId = pageId.replace("-dialog", "");
                var combo = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::findCombo(Lcom/google/gwt/dom/client/Element;)
                                ($wnd.$("#" + comboId)[0]);

                var addnl = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::getMenuStyleNames()();
                if (addnl) dialog.addClass( addnl );

                var listview = dialog.find( "ul" );
                var clearBtn = listview.jqmData( "clear-button" );
                var selectAllBtn = listview.jqmData( "select-all-button" );

                // Attach a reference to the listview as a data item to the dialog, because during the
                // pagecontainerhide handler below the selectmenu widget will already have returned the
                // listview to the popup, so we won't be able to find it inside the dialog with a selector.
                dialog.jqmData( "listview", listview );
                // Place clearBtn before the listview in the dialog.
                if (clearBtn) {
                    // 110% is a hack, we should just add 32px
                    //clearBtn.css("margin", "-16px 0 16px -16px").css("width", "110%");
                    listview.before( clearBtn );
                }
                if (selectAllBtn) {
                    // 110% is a hack, we should just add 32px
                    //selectAllBtn.css("margin", "-16px 0 16px -16px").css("width", "110%");
                    listview.before( selectAllBtn );
                }
                combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::doDlgBeforeShow(Lcom/google/gwt/dom/client/Element;Lcom/google/gwt/dom/client/Element;)
                    (dialog[0], data.prevPage.get(0));
            })
            // After the dialog is closed, we have to process transparent property as well.
            .on( "pagecontainerhide", function( event, data ) {
                var pageId = data.prevPage && data.prevPage.attr("id");
                // We only handle the disappearance of a dialog generated by a filterable selectmenu
                var isDlg = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::isPageSelectDialog(Ljava/lang/String;)
                                (pageId);
                if (!isDlg) return;
                var dialog = data.prevPage;

                var listview = data.prevPage.jqmData( "listview" );
                var clearBtn = listview.jqmData( "clear-button" );
                if (clearBtn) {
                    //clearBtn.css("margin", "0").css("width", "100%");
                    listview.before( clearBtn );
                }
                var selectAllBtn = listview.jqmData( "select-all-button" );
                if (selectAllBtn) {
                    //selectAllBtn.css("margin", "0").css("width", "100%");
                    listview.before( selectAllBtn );
                }

                var comboId = pageId.replace("-dialog", "");
                var combo = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::findCombo(Lcom/google/gwt/dom/client/Element;)
                                ($wnd.$("#" + comboId)[0]);
                combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelect::doDlgHide(Lcom/google/gwt/dom/client/Element;Lcom/google/gwt/dom/client/Element;)
                    (dialog[0], data.toPage.get(0));
            });
    }-*/;

    /**
     * @param dialog - select list's dialog, which is showing
     * @param prevPage - actually the page on which select widget is placed
     */
    protected void doDlgBeforeShow(Element dialog, Element prevPage) {
        if (transparent && prevPage != null) {
            transparentPrevPage = prevPage;
            prevPage.addClassName(JQMPage.UI_DIALOG_BACKGROUND);
            String s = prevPage.getAttribute(JQMPage.DATA_DOM_CACHE);
            if ("true".equals(s)) {
                transparentPrevPageClearCache = false;
            } else {
                transparentPrevPageClearCache = true;
                prevPage.setAttribute(JQMPage.DATA_DOM_CACHE, "true");
            }
            if (!transparentDoPrevPageLifecycle) {
                JQMPage prev = JQMPage.findPage(transparentPrevPage);
                if (prev != null) prev.unbindLifecycleEvents();
            }
            //Element dlgContain = JQMCommon.findChild(dialog, JQMPage.UI_DIALOG_CONTAIN);
            //if (dlgContain != null) dlgContain.addClassName(JQMPage.UI_BODY_INHERIT);
        } else {
            transparentPrevPage = null;
            transparentPrevPageClearCache = false;
            //Element dlgContain = JQMCommon.findChild(dialog, JQMPage.UI_DIALOG_CONTAIN);
            //if (dlgContain != null) dlgContain.removeClassName(JQMPage.UI_BODY_INHERIT);
        }

        if (transparent) dialog.addClassName(JQMPage.JQM4GWT_DLG_TRANSPARENT);
        else dialog.removeClassName(JQMPage.JQM4GWT_DLG_TRANSPARENT);
    }

    /**
     * @param dialog - select list's dialog, which is hiding
     * @param nextPage - actually the page on which select widget is placed
     */
    protected void doDlgHide(Element dialog, Element nextPage) {
        if (transparentPrevPage != null) {
            transparentPrevPage.removeClassName(JQMPage.UI_DIALOG_BACKGROUND);
            if (transparentPrevPageClearCache) {
                transparentPrevPage.removeAttribute(JQMPage.DATA_DOM_CACHE);
            }
            if (!transparentDoPrevPageLifecycle) {
                final JQMPage prev = JQMPage.findPage(transparentPrevPage);
                if (prev != null) {
                    Scheduler.get().scheduleFinally(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            prev.bindLifecycleEvents();
                        }
                    });
                }
            }
            transparentPrevPage = null;
            transparentPrevPageClearCache = false;
        }
    }

    public boolean isDlgTransparent() {
        return transparent;
    }

    /**
     * @param transparent - select list may show up as either a popup or a dialog,
     * depending how much vertical room there is on the screen.
     * If it shows up as a dialog then true means show faded previous page under dialog window
     * and don't bother prev page with lifecycle events (show, hide, ...).
     */
    public void setDlgTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isDlgTransparentDoPrevPageLifecycle() {
        return transparentDoPrevPageLifecycle;
    }

    /**
     * By default all lifecycle events (show, hide, ...) are blocked on previous page
     * when dlgTransparent is true for this select widget. This behavior can be disabled by setting
     * this property to true, so lifecycle events will be called for previous page.
     */
    public void setDlgTransparentDoPrevPageLifecycle(boolean transparentDoPrevPageLifecycle) {
        this.transparentDoPrevPageLifecycle = transparentDoPrevPageLifecycle;
    }

    public String getMultiValueSeparator() {
        return multiValueSeparator;
    }

    public void setMultiValueSeparator(String multiValueSeparator) {
        this.multiValueSeparator = multiValueSeparator;
    }

    public String getMenuStyleNames() {
        return menuStyleNames;
    }

    /**
     * For non-native mode (popup or dialog with listview), you can style listview by defining rule:
     * <br> .ui-selectmenu .ui-selectmenu-list { ... }
     * <br> For additional flexibility you can specify custom classes to be added together with ui-selectmenu
     * @param menuStyleNames - space separated custom classes
     */
    public void setMenuStyleNames(String menuStyleNames) {
        this.menuStyleNames = menuStyleNames;
    }

    /** Should be used in case we want to move select to another parent panel. */
    public void moveTo(Panel panel) {
        if (panel == null) return;
        Widget p = getParent();
        if (p == panel) return;
        if (isInstance(select.getElement())) { // on iOS moving JQMSelect from one panel to another is making it "stuck"
            String v = getValue();
            panel.add(this);
            refresh();
            setValue(v);
        } else {
            panel.add(this);
        }
    }

    public String getClearButtonText() {
        return clearButtonText;
    }

    /**
     * @param clearButtonText - if null or empty then CLEAR_BUTTON_TEXT will be used.
     */
    public void setClearButtonText(String clearButtonText) {
        this.clearButtonText = clearButtonText;
    }

    public boolean isShowClearButton() {
        return showClearButton;
    }

    /** Clear button will be shown on dropdown dialog with text defined by ClearButtonText. */
    public void setShowClearButton(boolean showClearButton) {
        this.showClearButton = showClearButton;
    }

    protected void closeAndClearValue() {
        this.close();
        this.setValue(null, true/*fireEvents*/);
    }

    public String getSelectAllButtonText() {
        return selectAllButtonText;
    }

    /**
     * @param selectAllButtonText - if null or empty then SELECT_ALL_BUTTON_TEXT will be used.
     */
    public void setSelectAllButtonText(String selectAllButtonText) {
        this.selectAllButtonText = selectAllButtonText;
    }

    public boolean isShowSelectAllButton() {
        return showSelectAllButton;
    }

    /** SelectAll button will be shown on dropdown dialog with text defined by SelectAllButtonText. */
    public void setShowSelectAllButton(boolean showSelectAllButton) {
        this.showSelectAllButton = showSelectAllButton;
    }

    protected void closeAndSelectAll() {
        this.close();
        if (isMultiple()) {
            setNewMultiVals(selectIdx.keySet(), true/*fireEvents*/);
        }
    }

    public boolean isShowTextOnPopup() {
        return showTextOnPopup;
    }

    /**
     * @param showTextOnPopup - if true (default) then non-empty text/label will be shown on popup title, even if labelHidden is true.
     */
    public void setShowTextOnPopup(boolean showTextOnPopup) {
        this.showTextOnPopup = showTextOnPopup;
    }

    protected boolean doAssignPopupTitle() {
        if (!isShowTextOnPopup()) return false;
        if (!isLabelHidden() || !Empty.is(getText())) return true;
        return false;
    }

}
