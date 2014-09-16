package com.sksamuel.jqm4gwt.plugins.datebox;

import java.util.Date;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPageEvent;
import com.sksamuel.jqm4gwt.form.elements.JQMText;

/**
 * <p> When you add {@literal <inherits name='com.sksamuel.Jqm4gwt-datebox' />} to yourApp.gwt.xml
 * the following scripts will be included automatically to resulting war: </p>
 * <pre> jqm-datebox.comp.calbox.min.js, jqm-datebox.min.css,
 * jquery.mobile.datebox.i18n.en_US.utf8.js, and datebox.png (for "new style" support) </pre>
 * <p> You can add additional languages by including something like: </p>
 * <pre> {@literal <script src="javascript/jquery.mobile.datebox.i18n.ru.utf8.js"></script>} </pre>
 * <p> into your main html after yourApp.nocache.js script. </p>
 *
 * See also:
 * <p><a href="http://dev.jtsage.com/jQM-DateBox2/demos/mode/calbox.html">jQueryMobile - DateBox</a></p>
 * <p><a href="http://dev.jtsage.com/jQM-DateBox2/demos/install.html">Install instructions</a></p>
 *
 */
public class JQMCalBox extends JQMText {

    /** <a href="http://dev.jtsage.com/jQM-DateBox2/demos/api/dateformat.html">Available Date Format Options</a> */
    public static final String FMT_MMDDYY = "%m/%d/%y";

    // HasValue<String> declared in JQMText and cannot be overridden as HasValue<Date> in this class.
    // So we are going to return well formatted string representation of date as getValue() result,
    // and expecting the same string format when setValue() method called.
    // By default ISO 8601 format is used, but it could be changed by setting valueStrFmt property.
    public static final DateTimeFormat VALUE_DFLT_STR_FMT = DateTimeFormat.getFormat("yyyy-MM-dd");
    private static DateTimeFormat valueStrFmt = VALUE_DFLT_STR_FMT;

    public static final String YEAR_PICK_NOW = "NOW";

    protected static final String MODE_CALBOX = "\"mode\": \"calbox\"";
    protected static final String USE_NEW_STYLE = "\"useNewStyle\":";
    protected static final String OVERRIDE_DATE_FMT = "\"overrideDateFormat\":";
    protected static final String NO_HEADER = "\"calNoHeader\":";
    protected static final String USE_PICKERS = "\"calUsePickers\":";
    protected static final String WEEK_START_DAY = "\"overrideCalStartDay\":";
    protected static final String USE_TODAY_BUTTON = "\"useTodayButton\":";
    protected static final String SQUARE_DATE_BUTTONS = "\"calControlGroup\":";
    protected static final String USE_CLEAR_BUTTON = "\"useClearButton\":";
    protected static final String YEAR_PICK_MIN = "\"calYearPickMin\":";
    protected static final String YEAR_PICK_MAX = "\"calYearPickMax\":";
    protected static final String LOCK_INPUT = "\"lockInput\":";
    protected static final String BUTTON_ICON = "\"buttonIcon\":";

    private Boolean useNewStyle = true;
    private String dateFormat = null;
    private Boolean usePickers = null;
    private Integer weekStartDay = null;
    private Boolean useTodayButton = null;
    private Boolean squareDateButtons = null;
    private Boolean useClearButton = null;
    private Boolean editable = true;
    private String yearPickMin = null;
    private String yearPickMax = null;
    private Boolean lockInput = null;
    private String buttonIcon = null;

    private Date delayedSetDate = null; // used when not initialized yet

    private boolean isInternSetDate;
    private Date internDateToSet; // works when isInternSetDate == true

    private boolean invalidateUnlockedInputOnBlur = true;

    static {
        addJsParts();
    }

    private static native void addJsParts() /*-{
        $wnd.mobileDateboxCallbackFalse = function() { return false; };
    }-*/;

    public JQMCalBox() {
        this(null);
    }

    public JQMCalBox(String text) {
        super(text);
        //setType("date"); // it's servicing by jqm-datebox, so type must not be set as "date"
        setInputAttribute("data-role", "datebox");
        input.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (lockInput != null && !lockInput && invalidateUnlockedInputOnBlur) {
                    String oldText = input.getText();
                    updateInputText();
                    String newText = input.getText();
                    boolean eq = true;
                    if (oldText != null) eq = oldText.equals(newText);
                    else if (newText != null) eq = newText.equals(oldText);
                    if (!eq) ValueChangeEvent.fire(input, getValue());
                }
            }
        });
        refreshDataOptions();
    }

    protected String bool2Str(boolean value) {
        return value ? "true" : "false";
    }

    protected String constructDataOptions() {
        StringBuilder sb = new StringBuilder();
        sb.append('{').append(MODE_CALBOX);
        if (useNewStyle != null) {
            sb.append(',').append(USE_NEW_STYLE).append(bool2Str(useNewStyle));
        }
        if (dateFormat != null && !dateFormat.isEmpty()) {
            sb.append(',').append(OVERRIDE_DATE_FMT).append('"').append(dateFormat).append('"');
        }
        if (usePickers != null) {
            sb.append(',').append(NO_HEADER).append(bool2Str(usePickers));
            sb.append(',').append(USE_PICKERS).append(bool2Str(usePickers));
        }
        if (weekStartDay != null) {
            sb.append(',').append(WEEK_START_DAY).append(String.valueOf(weekStartDay));
        }
        if (useTodayButton != null) {
            sb.append(',').append(USE_TODAY_BUTTON).append(bool2Str(useTodayButton));
        }
        if (squareDateButtons != null) {
            sb.append(',').append(SQUARE_DATE_BUTTONS).append(bool2Str(squareDateButtons));
        }
        if (useClearButton != null) {
            sb.append(',').append(USE_CLEAR_BUTTON).append(bool2Str(useClearButton));
        }
        if (editable != null && editable == false) {
            sb.append(',').append("\"openCallback\": \"mobileDateboxCallbackFalse\"");
            //sb.append(',').append("\"openCallback\": \"function(){return false;}\"");
            getElement().addClassName("jqm4gwt-non-editable");
        } else {
            getElement().removeClassName("jqm4gwt-non-editable");
        }
        if (yearPickMin != null && !yearPickMin.isEmpty()) {
            sb.append(',').append(YEAR_PICK_MIN).append('"').append(yearPickMin).append('"');
        }
        if (yearPickMax != null && !yearPickMax.isEmpty()) {
            sb.append(',').append(YEAR_PICK_MAX).append('"').append(yearPickMax).append('"');
        }
        if (lockInput != null) {
            sb.append(',').append(LOCK_INPUT).append(bool2Str(lockInput));
        }
        if (buttonIcon != null && !buttonIcon.isEmpty()) {
            sb.append(',').append(BUTTON_ICON).append('"').append(buttonIcon).append('"');
        }
        sb.append('}');
        return sb.toString();
    }

    protected void refreshDataOptions() {
        setInputAttribute("data-options", constructDataOptions());
    }

    private void setInputAttribute(String name, String value) {
        if (input == null) return;
        input.getElement().setAttribute(name, value);
    }

    public Boolean getUseNewStyle() {
        return useNewStyle;
    }

    public void setUseNewStyle(Boolean useNewStyle) {
        this.useNewStyle = useNewStyle;
        refreshDataOptions();
    }

    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat - <a href="http://dev.jtsage.com/jQM-DateBox2/demos/api/dateformat.html">Available Date Format Options</a>
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        refreshDataOptions();
    }

    public String getActiveDateFormat() {
        if (dateFormat != null) return dateFormat;
        if (input == null) return null;
        String fmt = internGetOption(input.getElement(), "dateFormat");
        return fmt;
    }

    public Boolean getUsePickers() {
        return usePickers;
    }

    public void setUsePickers(Boolean usePickers) {
        this.usePickers = usePickers;
        refreshDataOptions();
    }

    public Integer getWeekStartDay() {
        return weekStartDay;
    }

    /**
     * @param weekStartDay - 0-6, where 0=Sunday, 1=Monday...
     */
    public void setWeekStartDay(Integer weekStartDay) {
        this.weekStartDay = weekStartDay;
        refreshDataOptions();
    }

    public Boolean getUseTodayButton() {
        return useTodayButton;
    }

    public void setUseTodayButton(Boolean useTodayButton) {
        this.useTodayButton = useTodayButton;
        refreshDataOptions();
    }

    public Boolean getSquareDateButtons() {
        return squareDateButtons;
    }

    public void setSquareDateButtons(Boolean squareDateButtons) {
        this.squareDateButtons = squareDateButtons;
        refreshDataOptions();
    }

    public Boolean getUseClearButton() {
        return useClearButton;
    }

    public void setUseClearButton(Boolean useClearButton) {
        this.useClearButton = useClearButton;
        refreshDataOptions();
    }

    public Boolean getEditable() {
        return editable;
    }

    /**
     * Read only mode for this widget, if false - open calendar button will be disabled and input locked.
     */
    public void setEditable(Boolean editable) {
        this.editable = editable;
        if (!editable) lockInput = true;
        refreshDataOptions();
    }

    public String getYearPickMin() {
        return yearPickMin;
    }

    /**
     * See {@link JQMCalBox#setYearPickMax(String)}
     */
    public void setYearPickMin(String yearPickMin) {
        this.yearPickMin = yearPickMin;
        refreshDataOptions();
    }

    public String getYearPickMax() {
        return yearPickMax;
    }

    /**
     * yearPickMin and yearPickMax - valid options are an integer less than 1800,
     * which will be added/subtracted from the current year
     * (with Max, use a negative integer to go into the past - negative numbers for min will be abs()ed appropriatly),
     * or if the number is greater than 1800, it will be assumed to be a hard year.
     * <p/> Finally, the string "NOW" (UPCASE!) will use the current year (today's date, not the picker year).
     */
    public void setYearPickMax(String yearPickMax) {
        this.yearPickMax = yearPickMax;
        refreshDataOptions();
    }

    public Boolean getLockInput() {
        return lockInput;
    }

    public void setLockInput(Boolean lockInput) {
        this.lockInput = lockInput;
        refreshDataOptions();
    }

    public String getButtonIcon() {
        return buttonIcon;
    }

    /**
     * This is the class of the button in the input element. Any valid ui-icon-xxx is fine.
     * <p/>Default value is calendar.
     */
    public void setButtonIcon(String buttonIcon) {
        this.buttonIcon = buttonIcon;
        refreshDataOptions();
    }

    public boolean isInvalidateUnlockedInputOnBlur() {
        return invalidateUnlockedInputOnBlur;
    }

    /**
     * When lockInput == false this property controls if proper/parsed date is forcefully set on blur/exit (default: true)
     */
    public void setInvalidateUnlockedInputOnBlur(boolean invalidateUnlockedInputOnBlur) {
        this.invalidateUnlockedInputOnBlur = invalidateUnlockedInputOnBlur;
    }

    public static DateTimeFormat getValueStrFmt() {
        return valueStrFmt;
    }

    public static void setValueStrFmt(DateTimeFormat fmt) {
        valueStrFmt = fmt;
    }

    protected static class CalBoxValueChangeEvent extends ValueChangeEvent<String> {
        public CalBoxValueChangeEvent(String value) {
            super(value);
        }
    }

    protected static class CalBoxValueChangeHandler implements ValueChangeHandler<String> {

        private final ValueChangeHandler<String> handler;
        private final JQMCalBox calBox;

        public CalBoxValueChangeHandler(ValueChangeHandler<String> handler, JQMCalBox calBox) {
            this.handler = handler;
            this.calBox = calBox;
        }

        @Override
        public void onValueChange(ValueChangeEvent<String> event) {
            ValueChangeEvent<String> newEvent = event instanceof CalBoxValueChangeEvent
                    ? event : new CalBoxValueChangeEvent(calBox.getValue());
            handler.onValueChange(newEvent);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        ValueChangeHandler<String> newHandler = new CalBoxValueChangeHandler(handler, this);
        return input.addValueChangeHandler(newHandler);
    }

    @Override
    public String getValue() {
        Date d = getDate();
        return d == null ? null : valueStrFmt.format(d);
    }

    @Override
    public void setValue(String value) {
        if (value == null || value.isEmpty()) {
            setDate(null);
            return;
        }
        Date d = valueStrFmt.parse(value);
        setDate(d);
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        Date oldD = fireEvents ? getDate() : null;
        setValue(value);
        if (fireEvents) {
            Date newD = getDate();
            boolean eq = newD != null ? newD.equals(oldD) : oldD == null;
            if (!eq) ValueChangeEvent.fire(input, getValue());
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        Widget p = getParent();
        while (p != null) {
            if (p instanceof JQMPage) {
                ((JQMPage) p).addPageHandler(new JQMPageEvent.DefaultHandler() {
                    @Override
                    public void onInit(JQMPageEvent event) {
                        super.onInit(event);
                        setDate(delayedSetDate);
                    }
                });
                break;
            }
            p = p.getParent();
        }
    }

    @Override
    protected void onUnload() {
        final Date d = getDate();
        super.onUnload();
        delayedSetDate = d;
    }

    public void setDate(Date d) {
        setDate(d, false);
    }

    /**
     * Refresh after a programmatic change has taken place.
     */
    public void refresh() {
        refresh(input.getElement());
    }

    private native void refresh(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-datebox') !== undefined) {
            w.datebox('refresh');
        }
    }-*/;

    private static native boolean isCalboxReady(Element elt) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-datebox') !== undefined) {
            return true;
        } else {
            return false;
        }
    }-*/;

    private boolean isReady() {
        return input.isAttached() && isCalboxReady(input.getElement());
    }

    /**
     * @param fireEvents - when true {@link com.google.gwt.event.logical.shared.ValueChangeEvent}
     * will be fired if date changed.
     */
    public void setDate(Date d, boolean fireEvents) {
        if (input == null) return;
        if (!isReady()) {
            delayedSetDate = d;
            return;
        }
        Date oldD = fireEvents ? getDate() : null;
        if (d == null) {
            internSetDate(d);
            input.setText("");
        } else {
            internSetDate(d);
            // had to update text manually, because JS function 'setTheDate' didn't do that for some reason
            updateInputText();
        }
        if (fireEvents) {
            Date newD = getDate();
            boolean eq = newD != null ? newD.equals(oldD) : oldD == null;
            if (!eq) ValueChangeEvent.fire(input, getValue());
        }
    }

    private void updateInputText() {
        JsDate jsd = internGetDate(input.getElement());
        String fs = internFormat(input.getElement(), getActiveDateFormat(), jsd);
        input.setText(fs);
    }

    public Date getDate() {
        if (input == null) return null;
        if (!isReady()) return delayedSetDate;

        if (isInternSetDate) return internDateToSet;

        String s = input.getText();
        // open/close calendar even without any selection, sets js control's date to Now,
        // but we don't want this behavior, i.e. text is empty means no date is chosen!
        if (s == null || s.isEmpty()) return null;

        JsDate jsd = internGetDate(input.getElement());
        double msec = jsd.getTime();
        return new Date((long) msec);
    }

    /**
     * Doesn't change anything, just formats passed date as string according to widget's current settings.
     */
    public String formatDate(Date d) {
        if (d == null) return null;
        JsDate jsd = JsDate.create(d.getTime());
        return internFormat(input.getElement(), getActiveDateFormat(), jsd);
    }

    private static native String internFormat(Element elt, String fmt, JsDate d) /*-{
        return $wnd.$(elt).datebox('callFormat', fmt, d);
    }-*/;

    private static native JsDate internGetDate(Element elt) /*-{
        return $wnd.$(elt).datebox('getTheDate');
    }-*/;

    private static native void internalSetDate(Element elt, double d) /*-{
        $wnd.$(elt).datebox('setTheDate', new $wnd.Date(d));
    }-*/;

    private void internSetDate(Date d) {
        isInternSetDate = true;
        internDateToSet = d;
        try {
            // ValueChange may occur!
            internalSetDate(input.getElement(), d == null ? 0d : d.getTime());
        } finally {
            isInternSetDate = false;
        }
    }

    // partial copy of __fmt() and __() functions from jqm-datebox.comp.calbox.js
    // datebox('option') is not in official documentation, see also:
    // http://stackoverflow.com/a/8217857
    // http://dev.jtsage.com/jQM-DateBox2/demos/api/events.html
    private static native String internGetOption(Element elt, String val) /*-{
        var o = $wnd.$(elt).datebox('option');
        if (typeof o.lang[o.useLang][val] !== 'undefined') {
            return o.lang[o.useLang][val];
        }
        if (typeof o[o.mode+'lang'] !== 'undefined'
                && typeof o[o.mode+'lang'][val] !== 'undefined') {
            return o[o.mode+'lang'][val];
        }
        return o.lang['default'][val];
    }-*/;

}
