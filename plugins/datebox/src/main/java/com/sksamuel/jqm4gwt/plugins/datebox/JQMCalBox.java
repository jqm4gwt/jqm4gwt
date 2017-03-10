package com.sksamuel.jqm4gwt.plugins.datebox;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JsUtils;
import com.sksamuel.jqm4gwt.StrUtils;
import com.sksamuel.jqm4gwt.form.elements.JQMText;
import com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBoxEvent.DisplayChangeData;
import com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBoxEvent.OffsetData;

/**
 * <p> When you add {@literal <inherits name='com.sksamuel.Jqm4gwt-datebox' />} to yourApp.gwt.xml
 * the following scripts will be included automatically to resulting war: </p>
 * <pre> jtsage-datebox-calbox-nnn.min.js, jtsage-datebox-nnn.min.css,
 * jtsage-datebox.i18n.en.utf8.min.js, and datebox.png (in case you'll want a custom icon) </pre>
 * <p> You can add additional languages by injecting something like: </p>
 * <pre> jtsage-datebox.i18n.ru.utf8.min.js </pre>
 * <p> after your application.onModuleLoad() called, see ScriptUtils.waitJqmLoaded() </p>
 *
 * See also:
 * <p><a href="http://dev.jtsage.com/DateBox/">DateBox</a></p>
 * <p><a href="http://dev.jtsage.com/DateBox/doc/2-0-installing/">Installing instructions</a></p>
 *
 */
public class JQMCalBox extends JQMText {

    /** <a href="http://dev.jtsage.com/DateBox/doc/3-3-output/">Date Format Options</a> */
    public static final String FMT_MMDDYY = "%m/%d/%y";

    // HasValue<String> declared in JQMText and cannot be overridden as HasValue<Date> in this class.
    // So we are going to return well formatted string representation of date as getValue() result,
    // and expecting the same string format when setValue() method called.
    // By default ISO 8601 format is used, but it could be changed by setting valueStrFmt property.
    public static final DateTimeFormat VALUE_DFLT_STR_FMT = DateTimeFormat.getFormat("yyyy-MM-dd");
    private static DateTimeFormat valueStrFmt = VALUE_DFLT_STR_FMT;

    public static final String YEAR_PICK_NOW = "NOW";

    protected static final String MODE_CALBOX       = "\"mode\": \"calbox\"";
    protected static final String USE_INLINE        = "\"useInline\":"; // Show control inline in the page, negating any open and close actions
    protected static final String USE_INLINE_BLIND  = "\"useInlineBlind\":"; // Attach the control directly to the input element, and roll it down from there when opened
    protected static final String HIDE_CONTAINER    = "\"hideContainer\":"; // Cause the original fieldcontain to be hidden on the page - really only appropriate with "useInline"
    protected static final String OVERRIDE_DATE_FMT = "\"overrideDateFormat\":";
    protected static final String WEEK_START_DAY    = "\"overrideCalStartDay\":";
    protected static final String DIALOG_LABEL      = "\"overrideDialogLabel\":";
    protected static final String USE_CLEAR_BUTTON  = "\"useClearButton\":";
    protected static final String LOCK_INPUT        = "\"lockInput\":";
    protected static final String BUTTON_ICON       = "\"buttonIcon\":";
    protected static final String NEXT_MONTH_ICON   = "\"calNextMonthIcon\":";
    protected static final String PREV_MONTH_ICON   = "\"calPrevMonthIcon\":";

    // See http://dev.jtsage.com/DateBox/doc/5-0-control/
    // CalBox Specific - Display
    protected static final String SHOW_DAYS            = "\"calShowDays\":";
    protected static final String SHOW_WEEK            = "\"calShowWeek\":";
    protected static final String SHOW_ONE_MONTH_ONLY  = "\"calOnlyMonth\":";
    protected static final String HIGHLIGHT_TODAY      = "\"calHighToday\":";
    protected static final String HIGHLIGHT_SELECTED   = "\"calHighPick\":";
    protected static final String COMPACT_DATE_BUTTONS = "\"calControlGroup\":";

    // See http://dev.jtsage.com/DateBox/doc/5-0-control/
    // CalBox Specific - Control
    protected static final String USE_TODAY_BUTTON    = "\"useTodayButton\":";
    protected static final String USE_TOMORROW_BUTTON = "\"useTomorrowButton\":";
    protected static final String USE_PICKERS         = "\"calUsePickers\":";
    protected static final String USE_PICKERS_ICONS   = "\"calUsePickersIcons\":";
    protected static final String YEAR_PICK_MIN       = "\"calYearPickMin\":";
    protected static final String YEAR_PICK_MAX       = "\"calYearPickMax\":";
    protected static final String MIN_YEAR            = "\"minYear\":";
    protected static final String MAX_YEAR            = "\"maxYear\":";
    protected static final String MIN_DAYS            = "\"minDays\":";
    protected static final String MAX_DAYS            = "\"maxDays\":";
    protected static final String NO_HEADER           = "\"calNoHeader\":";
    protected static final String NO_TITLE            = "\"useHeader\":"; // Refers to the header with the close button and the title

    // See http://dev.jtsage.com/DateBox/doc/3-1-themes/
    protected static final String THEME              = "\"theme\":";            // false means inherited theme
    protected static final String THEME_HEADER       = "\"themeHeader\":";      // Theme for header
    protected static final String THEME_MODAL        = "\"useModalTheme\":";    // Theme for modal background of control. Shade the background with this color swatch. From the default themes, “a” is a very light grey, “b” is a slighly darker grey.
    protected static final String THEME_DATE         = "\"themeDate\":";        // Theme for otherwise un-specified date buttons
    protected static final String THEME_DATETODAY    = "\"themeDateToday\":";   // Theme for “today”
    protected static final String THEME_DATEPICK     = "\"themeDatePick\":";    // Theme for choosen date (used last after other options fail)
    protected static final String THEME_DAYHIGH      = "\"themeDayHigh\":";     // Theme for highlighted DAYS
    protected static final String THEME_DATEHIGH     = "\"themeDateHigh\":";    // Theme for highlighted DATES
    protected static final String THEME_DATEHIGH_ALT = "\"themeDateHighAlt\":"; // Theme for highlighted ALTERNATE DATES
    protected static final String THEME_DATEHIGH_REC = "\"themeDateHighRec\":"; // Theme for highlighted RECURRING DATES

    private Boolean useInline = null;
    private Boolean useInlineBlind = null;
    private Boolean hideContainer = null;
    private String dateFormat = null;
    private Integer weekStartDay = null;
    private String dialogLabel = null;
    private Boolean useClearButton = null;
    private Boolean editable = true;
    private Boolean lockInput = null;
    private String buttonIcon = null;
    private String nextMonthIcon = null;
    private String prevMonthIcon = null;

    private Boolean useTodayButton = null;
    private Boolean useTomorrowButton = null;
    private Boolean usePickers = null;
    private Boolean usePickersIcons = null;
    private String yearPickMin = null;
    private String yearPickMax = null;
    private Integer minYear = null;
    private Integer maxYear = null;
    private Integer minDays = null;
    private Integer maxDays = null;
    private Boolean noHeader = null;
    private Boolean noTitle = null;

    private Boolean showDays = null;
    private Boolean showWeek = null;
    private Boolean showOneMonthOnly = null;
    private Boolean highlightToday = null;
    private Boolean highlightSelected = null;
    private Boolean compactDateButtons = null;

    private String theme = null;
    private String themeHeader = null;
    private String themeModal = null;
    private String themeDate = null;
    private String themeDateToday = null;
    private String themeDatePick = null;
    private String themeDayHigh = null;
    private String themeDateHigh = null;
    private String themeDateHighAlt = null;
    private String themeDateHighRec = null;

    /**
     * GWT Date and JsDate are both created in current browser's timezone.
     * Calbox's setTheDate() takes date and use year/month/day from it (time and timezone are ignored).
     * <br> Example: we are in PST (i.e. GMT-8) timezone.
     * So new JsDate(0) gives us: 12/31/1969 16:00 GMT-8
     * <br> Therefore we cannot use 0 constant, and need proper number for our timezone,
     * which is 2.88E7 in that case.
     * <br> For person in GMT timezone this constant is 0 of course.
     */
    @SuppressWarnings("deprecation")
    private static final double NULL_DATE = new Date(70, 0, 1).getTime();

    private Date delayedSetDate = null; // used when not initialized yet

    private boolean isInternSetDate;
    private Date internDateToSet; // works when isInternSetDate == true

    private boolean invalidateUnlockedInputOnBlur = true;

    /** Additional information can be added to days (1..31) buttons. */
    public static interface GridDateFormatter {
        String format(int yyyy, int mm, int dd, String iso8601, boolean selectedDateVisible);
    }

    public static interface GridDateFormatterEx extends GridDateFormatter {
        /**
         * @return - additional space separated classes for CSS styling (coloring, shaping, ...)
         */
        String getStyleNames(int yyyy, int mm, int dd, String iso8601, boolean selectedDateVisible);
    }

    private GridDateFormatter gridDateFormatter;

    public static class JsDateBoxData extends JavaScriptObject {

        protected JsDateBoxData() {}

        /** Numeric Date */
        public final native int getDate() /*-{
            return this.date;
        }-*/;

        /** Numeric Month, zero based */
        public final native int getMonth() /*-{
            return this.month;
        }-*/;

        /** Date can be selected */
        public final native boolean getEnabled() /*-{
            return this.enabled;
        }-*/;

        /** You can disable any date button right in GridDateBox.beforeAppend() handler.
         *  <br> Example:
         *  <pre>
         *    data.setEnabled(false);
         *    JQMCommon.setEnabled(box, false);
         *    box.getStyle().setColor("red");
         *  </pre>
         **/
        public final native void setEnabled(boolean value) /*-{
            this.enabled = value;
        }-*/;
    }

    public static interface GridDateBox {
        void beforeAppend(Element box, JsDateBoxData data);
    }

    private GridDateBox gridDateBox;

    private boolean calBoxHandlerAdded;
    private boolean blurHandlerAdded;
    private boolean isInternalBlur;

    private boolean created;

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
        //setType("date"); // it's servicing by jtsage-datebox, so type must not be set as "date"
        setInputAttribute("data-role", "datebox");
        input.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (isInternalBlur) return;
                if (lockInput != null && !lockInput && invalidateUnlockedInputOnBlur) {
                    String oldText = input.getText();
                    if (oldText == null || oldText.isEmpty()) return;
                    if (oldText.trim().isEmpty()) {
                        input.setText("");
                        ValueChangeEvent.fire(input, getValue());
                        return;
                    }
                    if (!smartConvertInputText()) updateInputText();
                    String newText = input.getText();
                    if (!oldText.equals(newText)) ValueChangeEvent.fire(input, getValue());
                }
            }
        });
        refreshDataOptions();
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        blurHandlerAdded = true;
        return super.addBlurHandler(handler);
    }

    protected static String bool2Str(boolean value) {
        return value ? "true" : "false";
    }

    protected String constructDataOptions() {
        StringBuilder sb = new StringBuilder();
        sb.append('{').append(MODE_CALBOX);
        if (useInline != null) {
            sb.append(',').append(USE_INLINE).append(bool2Str(useInline));
        }
        if (useInlineBlind != null) {
            sb.append(',').append(USE_INLINE_BLIND).append(bool2Str(useInlineBlind));
        }
        if (hideContainer != null) {
            sb.append(',').append(HIDE_CONTAINER).append(bool2Str(hideContainer));
        }
        if (dateFormat != null && !dateFormat.isEmpty()) {
            sb.append(',').append(OVERRIDE_DATE_FMT).append('"').append(dateFormat).append('"');
        }
        if (usePickers != null) {
            sb.append(',').append(USE_PICKERS).append(bool2Str(usePickers));
        }
        if (usePickersIcons != null) {
            sb.append(',').append(USE_PICKERS_ICONS).append(bool2Str(usePickersIcons));
        }
        if (noHeader != null) {
            sb.append(',').append(NO_HEADER).append(bool2Str(noHeader));
        }
        if (noTitle != null) {
            sb.append(',').append(NO_TITLE).append(bool2Str(!noTitle));
        }
        if (weekStartDay != null) {
            sb.append(',').append(WEEK_START_DAY).append(String.valueOf(weekStartDay));
        }
        if (useTodayButton != null) {
            sb.append(',').append(USE_TODAY_BUTTON).append(bool2Str(useTodayButton));
        }
        if (useTomorrowButton != null) {
            sb.append(',').append(USE_TOMORROW_BUTTON).append(bool2Str(useTomorrowButton));
        }
        if (showDays != null) {
            sb.append(',').append(SHOW_DAYS).append(bool2Str(showDays));
        }
        if (showWeek != null) {
            sb.append(',').append(SHOW_WEEK).append(bool2Str(showWeek));
        }
        if (showOneMonthOnly != null) {
            sb.append(',').append(SHOW_ONE_MONTH_ONLY).append(bool2Str(showOneMonthOnly));
        }
        if (highlightToday != null) {
            sb.append(',').append(HIGHLIGHT_TODAY).append(bool2Str(highlightToday));
        }
        if (highlightSelected != null) {
            sb.append(',').append(HIGHLIGHT_SELECTED).append(bool2Str(highlightSelected));
        }
        if (compactDateButtons != null) {
            sb.append(',').append(COMPACT_DATE_BUTTONS).append(bool2Str(compactDateButtons));
        }
        if (dialogLabel != null && !dialogLabel.isEmpty()) {
            sb.append(',').append(DIALOG_LABEL).append('"').append(dialogLabel).append('"');
        }
        if (useClearButton != null) {
            sb.append(',').append(USE_CLEAR_BUTTON).append(bool2Str(useClearButton));
        }
        if (editable != null && editable == false) {
            sb.append(',').append("\"beforeOpenCallback\": \"mobileDateboxCallbackFalse\"");
            getElement().addClassName("jqm4gwt-non-editable");
        } else {
            getElement().removeClassName("jqm4gwt-non-editable");
        }
        if (yearPickMin != null && !yearPickMin.isEmpty()) {
            if (YEAR_PICK_NOW.equals(yearPickMin)) {
                sb.append(',').append(YEAR_PICK_MIN).append('"').append(yearPickMin).append('"');
            } else {
                sb.append(',').append(YEAR_PICK_MIN).append(yearPickMin);
            }
            if (minYear == null) {
                Integer minYY = calcMinYear();
                if (minYY != null && minYY.intValue() > 0) {
                    sb.append(',').append(MIN_YEAR).append(String.valueOf(minYY));
                }
            }
        }
        if (yearPickMax != null && !yearPickMax.isEmpty()) {
            if (YEAR_PICK_NOW.equals(yearPickMax)) {
                sb.append(',').append(YEAR_PICK_MAX).append('"').append(yearPickMax).append('"');
            } else {
                sb.append(',').append(YEAR_PICK_MAX).append(yearPickMax);
            }
            if (maxYear == null) {
                Integer maxYY = calcMaxYear();
                if (maxYY != null && maxYY.intValue() > 0) {
                    sb.append(',').append(MAX_YEAR).append(String.valueOf(maxYY));
                }
            }
        }
        if (minYear != null) {
            sb.append(',').append(MIN_YEAR).append(String.valueOf(minYear));
        }
        if (maxYear != null) {
            sb.append(',').append(MAX_YEAR).append(String.valueOf(maxYear));
        }
        if (minDays != null) {
            sb.append(',').append(MIN_DAYS).append(String.valueOf(minDays));
        }
        if (maxDays != null) {
            sb.append(',').append(MAX_DAYS).append(String.valueOf(maxDays));
        }
        if (lockInput != null) {
            sb.append(',').append(LOCK_INPUT).append(bool2Str(lockInput));
        }
        if (buttonIcon != null && !buttonIcon.isEmpty()) {
            sb.append(',').append(BUTTON_ICON).append('"').append(buttonIcon).append('"');
        }
        if (nextMonthIcon != null && !nextMonthIcon.isEmpty()) {
            sb.append(',').append(NEXT_MONTH_ICON).append('"').append(nextMonthIcon).append('"');
        }
        if (prevMonthIcon != null && !prevMonthIcon.isEmpty()) {
            sb.append(',').append(PREV_MONTH_ICON).append('"').append(prevMonthIcon).append('"');
        }
        if (theme != null && !theme.isEmpty()) {
            sb.append(',').append(THEME).append('"').append(theme).append('"');
        }
        if (themeHeader != null && !themeHeader.isEmpty()) {
            sb.append(',').append(THEME_HEADER).append('"').append(themeHeader).append('"');
        }
        if (themeModal != null && !themeModal.isEmpty()) {
            sb.append(',').append(THEME_MODAL).append('"').append(themeModal).append('"');
        }
        if (themeDate != null && !themeDate.isEmpty()) {
            sb.append(',').append(THEME_DATE).append('"').append(themeDate).append('"');
        }
        if (themeDateToday != null && !themeDateToday.isEmpty()) {
            sb.append(',').append(THEME_DATETODAY).append('"').append(themeDateToday).append('"');
        }
        if (themeDatePick != null && !themeDatePick.isEmpty()) {
            sb.append(',').append(THEME_DATEPICK).append('"').append(themeDatePick).append('"');
        }
        if (themeDayHigh != null && !themeDayHigh.isEmpty()) {
            sb.append(',').append(THEME_DAYHIGH).append('"').append(themeDayHigh).append('"');
        }
        if (themeDateHigh != null && !themeDateHigh.isEmpty()) {
            sb.append(',').append(THEME_DATEHIGH).append('"').append(themeDateHigh).append('"');
        }
        if (themeDateHighAlt != null && !themeDateHighAlt.isEmpty()) {
            sb.append(',').append(THEME_DATEHIGH_ALT).append('"').append(themeDateHighAlt).append('"');
        }
        if (themeDateHighRec != null && !themeDateHighRec.isEmpty()) {
            sb.append(',').append(THEME_DATEHIGH_REC).append('"').append(themeDateHighRec).append('"');
        }
        sb.append('}');
        return sb.toString();
    }

    public Integer calcMinYear() {
        if (minYear != null) return minYear;

        if (yearPickMin != null && !yearPickMin.isEmpty()) {
            if (YEAR_PICK_NOW.equals(yearPickMin)) {
                @SuppressWarnings("deprecation")
                int curYY = (new Date()).getYear() + 1900;
                return curYY;
            } else {
                int yy = Integer.parseInt(yearPickMin);
                if (yy < 1800) return minYear;
                else return yy;
            }
        } else {
            return minYear;
        }
    }

    public Integer calcMaxYear() {
        if (maxYear != null) return maxYear;

        if (yearPickMax != null && !yearPickMax.isEmpty()) {
            if (YEAR_PICK_NOW.equals(yearPickMax)) {
                @SuppressWarnings("deprecation")
                int curYY = (new Date()).getYear() + 1900;
                return curYY;
            } else {
                int yy = Integer.parseInt(yearPickMax);
                if (yy < 1800) return maxYear;
                else return yy;
            }
        } else {
            return maxYear;
        }
    }

    protected void refreshDataOptions() {
        setInputAttribute("data-options", constructDataOptions());
    }

    private void setInputAttribute(String name, String value) {
        if (input == null) return;
        input.getElement().setAttribute(name, value);
    }

    public Boolean getUseInline() {
        return useInline;
    }

    /** Show control inline in the page, negating any open and close actions */
    public void setUseInline(Boolean useInline) {
        this.useInline = useInline;
        refreshDataOptions();
    }

    public Boolean getUseInlineBlind() {
        return useInlineBlind;
    }

    /** Attach the control directly to the input element, and roll it down from there when opened */
    public void setUseInlineBlind(Boolean useInlineBlind) {
        this.useInlineBlind = useInlineBlind;
        refreshDataOptions();
    }

    public Boolean getHideContainer() {
        return hideContainer;
    }

    /** Cause the original fieldcontain to be hidden on the page - really only appropriate with "useInline" */
    public void setHideContainer(Boolean value) {
        this.hideContainer = value;
        refreshDataOptions();
    }

    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat - <a href="http://dev.jtsage.com/DateBox/doc/3-3-output/">Date Format Options</a>
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        refreshDataOptions();
    }

    public String getActiveDateFormat() {
        if (dateFormat != null) return dateFormat;
        if (input == null) return null;
        // see __fmt() in jtsage-datebox-calbox.js
        String fmt = internGetLangOptionStr(input.getElement(), "dateFormat");
        return fmt;
    }

    public JsArrayString getMonthNames() {
        if (input == null) return null;
        JavaScriptObject months = internGetLangOption(input.getElement(), "monthsOfYear");
        return months.cast();
    }

    public JsArrayString getMonthShortNames() {
        if (input == null) return null;
        JavaScriptObject months = internGetLangOption(input.getElement(), "monthsOfYearShort");
        return months.cast();
    }

    public static JsArrayString getClazzMonthNames() {
        JavaScriptObject months = internGetProtoLangOption("monthsOfYear");
        return months.cast();
    }

    public static JsArrayString getClazzMonthShortNames() {
        JavaScriptObject months = internGetProtoLangOption("monthsOfYearShort");
        return months.cast();
    }

    public Boolean getUsePickers() {
        return usePickers;
    }

    public void setUsePickers(Boolean usePickers) {
        this.usePickers = usePickers;
        if (this.usePickers != null && this.usePickers && noHeader == null) noHeader = true;
        refreshDataOptions();
    }

    public Boolean getUsePickersIcons() {
        return usePickersIcons;
    }

    /**
     * Only works with calNoHeader and calUsePickers turned on (true).
     */
    public void setUsePickersIcons(Boolean value) {
        this.usePickersIcons = value;
        refreshDataOptions();
    }

    public Boolean getNoHeader() {
        return noHeader;
    }

    /** Hide standard header (by default a plus button, the Month/Year combo, and a minus button) */
    public void setNoHeader(Boolean noHeader) {
        this.noHeader = noHeader;
        refreshDataOptions();
    }

    public Boolean getNoTitle() {
        return noTitle;
    }

    /** Refers to the header with the close button and the title */
    public void setNoTitle(Boolean value) {
        this.noTitle = value;
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

    public String getDialogLabel() {
        return dialogLabel;
    }

    /**
     * Needed in case for example you don't want placeholder to be shown as date selection dialog title.
     */
    public void setDialogLabel(String dialogLabel) {
        this.dialogLabel = dialogLabel;
        refreshDataOptions();
    }

    public Boolean getUseTodayButton() {
        return useTodayButton;
    }

    public void setUseTodayButton(Boolean useTodayButton) {
        this.useTodayButton = useTodayButton;
        refreshDataOptions();
    }

    public Boolean getUseTomorrowButton() {
        return useTomorrowButton;
    }

    public void setUseTomorrowButton(Boolean useTomorrowButton) {
        this.useTomorrowButton = useTomorrowButton;
        refreshDataOptions();
    }

    public Boolean getShowDays() {
        return showDays;
    }

    public void setShowDays(Boolean showDays) {
        this.showDays = showDays;
        refreshDataOptions();
    }

    public Boolean getShowWeek() {
        return showWeek;
    }

    public void setShowWeek(Boolean showWeek) {
        this.showWeek = showWeek;
        refreshDataOptions();
    }

    public Boolean getShowOneMonthOnly() {
        return showOneMonthOnly;
    }

    public void setShowOneMonthOnly(Boolean showOneMonthOnly) {
        this.showOneMonthOnly = showOneMonthOnly;
        refreshDataOptions();
    }

    public Boolean getHighlightToday() {
        return highlightToday;
    }

    public void setHighlightToday(Boolean highlightToday) {
        this.highlightToday = highlightToday;
        refreshDataOptions();
    }

    public Boolean getHighlightSelected() {
        return highlightSelected;
    }

    public void setHighlightSelected(Boolean highlightSelected) {
        this.highlightSelected = highlightSelected;
        refreshDataOptions();
    }

    public Boolean getCompactDateButtons() {
        return compactDateButtons;
    }

    public void setCompactDateButtons(Boolean compactDateButtons) {
        this.compactDateButtons = compactDateButtons;
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
     * <br> Finally, the string "NOW" (UPCASE!) will use the current year (today's date, not the picker year).
     */
    public void setYearPickMax(String yearPickMax) {
        this.yearPickMax = yearPickMax;
        refreshDataOptions();
    }

    public Integer getMinDays() {
        return minDays;
    }

    /** See <a href="http://dev.jtsage.com/DateBox/api/minDays/">Minimum amount of days before today</a> */
    public void setMinDays(Integer value) {
        minDays = value;
        refreshDataOptions();
    }

    public Integer getMaxDays() {
        return maxDays;
    }

    /** See <a href="http://dev.jtsage.com/DateBox/api/maxDays/">Maximum number of days past today</a> */
    public void setMaxDays(Integer value) {
        maxDays = value;
        refreshDataOptions();
    }

    public Integer getMinYear() {
        return minYear;
    }

    /** See <a href="http://dev.jtsage.com/DateBox/api/minYear/">Minimum allowed year</a> */
    public void setMinYear(Integer value) {
        minYear = value;
        refreshDataOptions();
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    /** See <a href="http://dev.jtsage.com/DateBox/api/maxYear/">Maximum allowed year</a> */
    public void setMaxYear(Integer value) {
        maxYear = value;
        refreshDataOptions();
    }

    public Boolean getLockInput() {
        return lockInput;
    }

    /**
     * When false - user can type-in date manually (default is true, i.e. no manual typing).
     */
    public void setLockInput(Boolean lockInput) {
        this.lockInput = lockInput;
        refreshDataOptions();
    }

    public String getButtonIcon() {
        return buttonIcon;
    }

    /**
     * This is the class of the button in the input element.
     * <br>Default value is calendar.
     * <br><a href="http://demos.jquerymobile.com/1.4.5/icons/">Icons</a>
     */
    public void setButtonIcon(String buttonIcon) {
        this.buttonIcon = buttonIcon;
        refreshDataOptions();
    }

    public String getNextMonthIcon() {
        return nextMonthIcon;
    }

    /**
     * This allows customization of the Next Month button in the calendar header.
     * <br>Default value is plus.
     * <br><a href="http://demos.jquerymobile.com/1.4.5/icons/">Icons</a>
     */
    public void setNextMonthIcon(String nextMonthIcon) {
        this.nextMonthIcon = nextMonthIcon;
        refreshDataOptions();
    }

    public String getPrevMonthIcon() {
        return prevMonthIcon;
    }

    /**
     * This allows customization of the Previous Month button in the calendar header.
     * <br>Default value is minus.
     * <br><a href="http://demos.jquerymobile.com/1.4.5/icons/">Icons</a>
     */
    public void setPrevMonthIcon(String prevMonthIcon) {
        this.prevMonthIcon = prevMonthIcon;
        refreshDataOptions();
    }

    @Override
    public String getTheme() {
        if (theme == null || theme.isEmpty()) return super.getTheme();
        else return theme;
    }

    @Override
    public void setTheme(String theme) {
        super.setTheme(theme);
        this.theme = theme;
        refreshDataOptions();
    }

    public String getThemeHeader() {
        return themeHeader;
    }

    public void setThemeHeader(String themeHeader) {
        this.themeHeader = themeHeader;
        refreshDataOptions();
    }

    public String getThemeModal() {
        return themeModal;
    }

    public void setThemeModal(String themeModal) {
        this.themeModal = themeModal;
        refreshDataOptions();
    }

    public String getThemeDate() {
        return themeDate;
    }

    public void setThemeDate(String themeDate) {
        this.themeDate = themeDate;
        refreshDataOptions();
    }

    public String getThemeDateToday() {
        return themeDateToday;
    }

    public void setThemeDateToday(String themeDateToday) {
        this.themeDateToday = themeDateToday;
        refreshDataOptions();
    }

    public String getThemeDatePick() {
        return themeDatePick;
    }

    public void setThemeDatePick(String themeDatePick) {
        this.themeDatePick = themeDatePick;
        refreshDataOptions();
    }

    public String getThemeDayHigh() {
        return themeDayHigh;
    }

    public void setThemeDayHigh(String themeDayHigh) {
        this.themeDayHigh = themeDayHigh;
        refreshDataOptions();
    }

    public String getThemeDateHigh() {
        return themeDateHigh;
    }

    public void setThemeDateHigh(String themeDateHigh) {
        this.themeDateHigh = themeDateHigh;
        refreshDataOptions();
    }

    public String getThemeDateHighAlt() {
        return themeDateHighAlt;
    }

    public void setThemeDateHighAlt(String themeDateHighAlt) {
        this.themeDateHighAlt = themeDateHighAlt;
        refreshDataOptions();
    }

    public String getThemeDateHighRec() {
        return themeDateHighRec;
    }

    public void setThemeDateHighRec(String themeDateHighRec) {
        this.themeDateHighRec = themeDateHighRec;
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
        public void onValueChange(final ValueChangeEvent<String> event) {
            if (calBox.isInternSetDate) return;
            if (event instanceof CalBoxValueChangeEvent) {
                handler.onValueChange(event);
                return;
            }
            if (calBox.lockInput != null && !calBox.lockInput) {
                // On manual input calBox.getValue() is still old here, so we have to wait
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        ValueChangeEvent<String> newEvent = new CalBoxValueChangeEvent(calBox.getValue());
                        handler.onValueChange(newEvent);
                        doArtificialBlur();
                    }
                });
            } else {
                ValueChangeEvent<String> newEvent = new CalBoxValueChangeEvent(calBox.getValue());
                handler.onValueChange(newEvent);
                doArtificialBlur();
            }
        }

        private void doArtificialBlur() {
            if (calBox.isAttached() && calBox.created && calBox.blurHandlerAdded) {
                calBox.isInternalBlur = true;
                try { // For example to rerun JQMForm's validation for this dropdown
                    DomEvent.fireNativeEvent(Document.get().createBlurEvent(), calBox.input);
                } finally {
                    calBox.isInternalBlur = false;
                }
            }
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

    private static native void bindCreated(Element elt, JQMCalBox cal) /*-{
        $wnd.$(elt).on( 'dateboxcreate', function( event, ui ) {
            cal.@com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox::created()();
        });
    }-*/;

    private static native void unbindCreated(Element elt) /*-{
        $wnd.$(elt).off( 'dateboxcreate' );
    }-*/;

    private void created() {
        created = true;
        setDate(delayedSetDate);
        initGridDateFormatter();
        initGridDateBoxBeforeAppend();
        initDisplayChange();
        initOffset();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        bindCreated(input.getElement(), this);
        if (created) setDate(delayedSetDate);
    }

    @Override
    protected void onUnload() {
        final Date d = getDate();
        unbindCreated(input.getElement());
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
        if (w.data('jtsage-datebox') !== undefined) {
            w.datebox('refresh');
        }
    }-*/;

    private static native boolean isCalboxReady(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return false; // jQuery is not loaded
        var w = $wnd.$(elt);
        if (w.data('jtsage-datebox') !== undefined) {
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
        Element elt = input.getElement();
        JsDate jsd = internGetDate(elt);
        String fs = internFormat(elt, getActiveDateFormat(), jsd);
        input.setText(fs);
    }

    /** @return - true in case input text was successfully processed by "smart" converter. */
    private boolean smartConvertInputText() {
        Element elt = input.getElement();
        String v = input.getValue();
        if (v != null && !v.isEmpty()) {
            v = v.trim();
            // supports (mmddyy or mmddyyyy) or (ddmmyy or ddmmyyyy) input without any separators
            if (!v.isEmpty() && StrUtils.isDigitsOnly(v) && (v.length() == 6 || v.length() == 8)) {
                boolean mmFirst = true;
                String fmt = getDateFormat();
                if (!Empty.is(fmt)) {
                    int ddPos = fmt.indexOf("%d");
                    int mmPos = fmt.indexOf("%m");
                    if (ddPos >= 0 && mmPos >= 0) {
                        mmFirst = mmPos < ddPos;
                    }
                }
                final int mm;
                final int dd;
                if (mmFirst) {
                    mm = Integer.parseInt(v.substring(0, 2));
                    dd = Integer.parseInt(v.substring(2, 4));
                } else {
                    dd = Integer.parseInt(v.substring(0, 2));
                    mm = Integer.parseInt(v.substring(2, 4));
                }
                int yy = Integer.parseInt(v.substring(4));
                Date d = new Date();
                @SuppressWarnings("deprecation")
                int currentYear = d.getYear() + 1900;
                if (yy < 100) {
                    if (yy + 2000 <= currentYear + 20) yy += 2000;
                    else yy += 1900;
                }
                if (dd >= 1 && dd <= 31 && mm >= 1 && mm <= 12 && yy >= 1900 && yy <= currentYear + 100) {
                    Integer minYY = calcMinYear();
                    if (minYY != null && yy < minYY.intValue()) return false;
                    Integer maxYY = calcMaxYear();
                    if (maxYY != null && yy > maxYY.intValue()) return false;

                    @SuppressWarnings("deprecation")
                    Date newd = new Date(yy - 1900, mm - 1, dd);
                    isInternSetDate = true;
                    internDateToSet = newd;
                    try {
                        // ValueChange may occur!
                        internalSetDate(elt, yy, mm - 1, dd);
                    } finally {
                        isInternSetDate = false;
                    }
                    return true;
                }
            }
        }
        return false;
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
        return JQMContext.jsDateToDate(jsd);
    }

    public String getIso8601() {
        if (input == null) return null;
        if (!isReady()) return dateAsIso8601(delayedSetDate);

        if (isInternSetDate) return dateAsIso8601(internDateToSet);

        String s = input.getText();
        // open/close calendar even without any selection, sets js control's date to Now,
        // but we don't want this behavior, i.e. text is empty means no date is chosen!
        if (s == null || s.isEmpty()) return null;

        JsDate jsd = internGetDate(input.getElement());
        if (jsd == null) return null;
        int yyyy = jsd.getFullYear();
        int mm = jsd.getMonth() + 1;
        int dd = jsd.getDate();
        return dateAsIso8601(yyyy, mm, dd);
    }

    @SuppressWarnings("deprecation")
    public static String dateAsIso8601(Date d) {
        if (d == null) return null;
        int yyyy = d.getYear() + 1900;
        int mm = d.getMonth() + 1;
        int dd = d.getDate();
        return dateAsIso8601(yyyy, mm, dd);
    }

    /**
     * @return - date in ISO8601 format, i.e. 2015-01-09
     */
    public static String dateAsIso8601(int yyyy, int mm, int dd) {
        StringBuilder sb = new StringBuilder();
        String s = String.valueOf(yyyy);
        int p = 4 - s.length();
        while (p > 0) {
            sb.append('0');
            p--;
        }
        sb.append(s).append('-');

        s = String.valueOf(mm);
        p = 2 - s.length();
        while (p > 0) {
            sb.append('0');
            p--;
        }
        sb.append(s).append('-');

        s = String.valueOf(dd);
        p = 2 - s.length();
        while (p > 0) {
            sb.append('0');
            p--;
        }
        sb.append(s);

        return sb.toString();
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

    private static native boolean internIsSelDateVisible(Element elt) /*-{
        return $wnd.$(elt).datebox('dateVisible');
    }-*/;

    /**
     * @return - true if the selected calendar date is visible.
     * <br> Only valid for calbox, otherwise it will always return true.
     */
    public boolean isSelectedDateVisible() {
        return internIsSelDateVisible(input.getElement());
    }

    private static native void internalSetDate(Element elt, double d) /*-{
        $wnd.$(elt).datebox('setTheDate', new $wnd.Date(d));
    }-*/;

    private static native void internalSetDate(Element elt, int year, int month, int dayOfMonth) /*-{
        $wnd.$(elt).datebox('setTheDate', new $wnd.Date(year, month, dayOfMonth));
    }-*/;

    private void internSetDate(Date d) {
        final double v = d == null ? NULL_DATE : d.getTime();
        final Element elt = input.getElement();
        JsDate jsd = internGetDate(elt);
        if (jsd != null) {
            double t = jsd.getTime();
            if (t == v) return; // setTheDate() is expensive!
        }
        isInternSetDate = true;
        internDateToSet = d;
        try {
            // ValueChange may occur!
            internalSetDate(elt, v);
        } finally {
            isInternSetDate = false;
        }
    }

    // datebox('option') gives a full options list, it's inherited from the jquery-ui widget library
    // and working because jquery.ui.widget.js has the following declaration:
    // option: function( key, value ) { ... }, see https://github.com/jquery/jquery-ui/blob/master/ui/widget.js
    //
    // If you need to retrieve a single option, you can also do this (see https://github.com/jtsage/jquery-mobile-datebox/issues/340#issuecomment-61712507):
    // var myOption = $wnd.$(elt).datebox('getOption', 'dateFormat');
    // 'getOption' is jtsage-datebox function with i18n knowledge, 'option' is jquery-ui function without i18n knowledge.
    //
    // See also: http://stackoverflow.com/a/8217857
    //       and http://dev.jtsage.com/DateBox/api/cat-event/
    //       and http://stackoverflow.com/a/15337587
    //
    private static native JavaScriptObject internGetLangOption(Element elt, String val) /*-{
        // var o = $wnd.$(elt).datebox('option');
        return $wnd.$(elt).datebox('getOption', val);
    }-*/;

    private static native String internGetLangOptionStr(Element elt, String val) /*-{
        // var o = $wnd.$(elt).datebox('option');
        return $wnd.$(elt).datebox('getOption', val);
    }-*/;

    /** Gives "Global"/JsPrototype language option.
     *  <br> From jtsage-datebox.js partial copy of: __: function(val) { ... }
     *  <br> See for example: jtsage-datebox.i18n.ru.utf8.min.js
     **/
    private static native JavaScriptObject internGetProtoLangOption(String val) /*-{
        var o = $wnd.jQuery.jtsage.datebox.prototype.options;
        var lang = o.lang[o.useLang];
        if (typeof lang !== 'undefined' && typeof lang[val] !== 'undefined') {
            return lang[val];
        }
        return o.lang['default'][val];
    }-*/;

    /**
     * @param mm - month 0-11, Jan = 0 .. Dec = 11
     * @param dd - day 1-31
     */
    private String formatGridDate(int yyyy, int mm, int dd, String iso8601, boolean selectedDateVisible) {
        if (gridDateFormatter == null) {
            return String.valueOf(dd);
        } else {
            return gridDateFormatter.format(yyyy, mm, dd, iso8601, selectedDateVisible);
        }
    }

    private void formatGridDateEx(int yyyy, int mm, int dd, String iso8601, boolean selectedDateVisible,
            JavaScriptObject result) {
        if (!(gridDateFormatter instanceof GridDateFormatterEx)) {
            JsUtils.setObjValue(result, "text", String.valueOf(dd));
            JsUtils.setObjValue(result, "class", "");
        } else {
            String text = gridDateFormatter.format(yyyy, mm, dd, iso8601, selectedDateVisible);
            String cls  = ((GridDateFormatterEx) gridDateFormatter).getStyleNames(
                    yyyy, mm, dd, iso8601, selectedDateVisible);
            JsUtils.setObjValue(result, "text", text);
            JsUtils.setObjValue(result, "class", cls);
        }
    }

    private int getGridDateFormatterType() {
        if (gridDateFormatter == null) return 0;
        if (gridDateFormatter instanceof GridDateFormatterEx) return 2;
        return 1;
    }

    private static native void initGridDateFormatter(Element elt, JQMCalBox ctrl) /*-{
        if (ctrl === null) {
            var v = $wnd.$(elt).datebox( 'getOption', 'calFormatter' );
            if (v !== false) $wnd.$(elt).datebox( { 'calFormatter': false } );
        } else {
            $wnd.$(elt).datebox( { 'calFormatter': function( date ) {
                var t = ctrl.@com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox::getGridDateFormatterType()();
                if (t === 0) return date.Date;
                else if (t === 1) {
                    var s = ctrl.@com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox::formatGridDate(
                        IIILjava/lang/String;Z)
                        (date.Year, date.Month, date.Date, date.ISO, date.dateVisible);
                    return s;
                } else {
                    var rslt = {};
                    ctrl.@com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox::formatGridDateEx(
                        IIILjava/lang/String;ZLcom/google/gwt/core/client/JavaScriptObject;)
                        (date.Year, date.Month, date.Date, date.ISO, date.dateVisible, rslt);
                    return rslt;
                }
            }});
        }
    }-*/;

    private void gridDateBoxBeforeAppend(Element box, JavaScriptObject data) {
        if (gridDateBox != null) {
            JsDateBoxData d = data.cast();
            gridDateBox.beforeAppend(box, d);
        }
    }

    // See http://dev.jtsage.com/DateBox/api/calBeforeAppendFunc/
    private static native void initGridDateBoxBeforeAppend(Element elt, JQMCalBox ctrl) /*-{
        if (ctrl === null) {
            $wnd.$(elt).datebox({ 'calBeforeAppendFunc': function(t) { return t; } });
        } else {
            $wnd.$(elt).datebox({ 'calBeforeAppendFunc': function(t) {
                if (t) {
                    ctrl.@com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox::gridDateBoxBeforeAppend(
                        Lcom/google/gwt/dom/client/Element;Lcom/google/gwt/core/client/JavaScriptObject;)
                        (t[0], t.data());
                }
                return t;
            }});
        }
    }-*/;

    private void initGridDateFormatter() {
        if (!isReady()) return;
        initGridDateFormatter(input.getElement(), gridDateFormatter != null ? this : null);
    }

    private void initGridDateBoxBeforeAppend() {
        if (!isReady()) return;
        initGridDateBoxBeforeAppend(input.getElement(), gridDateBox != null ? this : null);
    }

    public GridDateFormatter getGridDateFormatter() {
        return gridDateFormatter;
    }

    /** Additional information can be added to days (1..31) buttons. */
    public void setGridDateFormatter(GridDateFormatter gridDateFormatter) {
        this.gridDateFormatter = gridDateFormatter;
        initGridDateFormatter();
    }

    public GridDateBox getGridDateBoxBeforeAppend() {
        return gridDateBox;
    }

    /** This option allows you to define a custom function that is called on the generated
     *  calbox grid box of each date.
     *  <br> See <a href="http://dev.jtsage.com/DateBox/api/calBeforeAppendFunc/">calBeforeAppendFunc</a>
     **/
    public void setGridDateBoxBeforeAppend(GridDateBox value) {
        this.gridDateBox = value;
        initGridDateBoxBeforeAppend();
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

    public HandlerRegistration addCalBoxHandler(JQMCalBoxEvent.Handler handler) {
        if (handler == null) return null;
        HandlerRegistration rslt = addHandler(handler, JQMCalBoxEvent.getType());
        if (!calBoxHandlerAdded) {
            calBoxHandlerAdded = true;
            initDisplayChange();
            initOffset();
        }
        return rslt;
    }

    private static native void initDisplayChange(Element elt, JQMCalBox ctrl) /*-{
        if (ctrl == null) {
            $wnd.$(elt).off('datebox.displayChange');
        } else {
            $wnd.$(elt).on('datebox.displayChange', function (e, p) {
                if ( p.method === 'displayChange' ) {
                    var changeAmount = p.thisChangeAmount == null ? 0 : p.thisChangeAmount;
                    ctrl.@com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox::fireDisplayChange(
                        Lcom/google/gwt/core/client/JsDate;Lcom/google/gwt/core/client/JsDate;Ljava/lang/String;I)
                        (p.shownDate, p.selectedDate, p.thisChange, changeAmount);
                }
            });
        }
    }-*/;

    private static native void initOffset(Element elt, JQMCalBox ctrl) /*-{
        if (ctrl == null) {
            $wnd.$(elt).off('datebox.offset');
        } else {
            $wnd.$(elt).on('datebox.offset', function (e, p) {
                if ( p.method === 'offset' ) {
                    var changeAmount = p.amount == null ? 0 : p.amount;
                    ctrl.@com.sksamuel.jqm4gwt.plugins.datebox.JQMCalBox::fireOffset(
                        Lcom/google/gwt/core/client/JsDate;Ljava/lang/String;I)
                        (p.newDate, p.type, changeAmount);
                }
            });
        }
    }-*/;

    private void initDisplayChange() {
        if (!calBoxHandlerAdded || !isReady()) return;
        initDisplayChange(input.getElement(), this);
    }

    private void initOffset() {
        if (!calBoxHandlerAdded || !isReady()) return;
        initOffset(input.getElement(), this);
    }

    private void fireDisplayChange(JsDate shownDate, JsDate selectedDate,
                                   String thisChange, int thisChangeAmount) {
        JQMCalBoxEvent.fire(this, new DisplayChangeData(JQMContext.jsDateToDate(shownDate),
                JQMContext.jsDateToDate(selectedDate), thisChange, thisChangeAmount));
    }

    private void fireOffset(JsDate newDate, String changeType, int changeAmount) {
        JQMCalBoxEvent.fire(this, new OffsetData(JQMContext.jsDateToDate(newDate),
                changeType, changeAmount));
    }

}
