package com.sksamuel.jqm4gwt.plugins.datebox;

import java.util.Date;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
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

    protected static final String MODE_CALBOX = "\"mode\": \"calbox\"";
    protected static final String USE_NEW_STYLE = "\"useNewStyle\":";
    protected static final String OVERRIDE_DATE_FMT = "\"overrideDateFormat\":";
    protected static final String NO_HEADER = "\"calNoHeader\":";
    protected static final String USE_PICKERS = "\"calUsePickers\":";
    protected static final String WEEK_START_DAY = "\"overrideCalStartDay\":";
    protected static final String USE_TODAY_BUTTON = "\"useTodayButton\":";
    protected static final String SQUARE_DATE_BUTTONS = "\"calControlGroup\":";

    // input is private in parent, so have to be initialized indirectly on initInput()
    private TextBox input = null;

    private Boolean useNewStyle = true;
    private String dateFormat = null;
    private Boolean usePickers = null;
    private Integer weekStartDay = null;
    private Boolean useTodayButton = null;
    private Boolean squareDateButtons = null;

    public JQMCalBox() {
        this(null);
    }

    public JQMCalBox(String text) {
        super(text);
        setType("date");
        initInput();
        setInputAttribute("data-role", "datebox");
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
        sb.append('}');
        return sb.toString();
    }

    protected void refreshDataOptions() {
        setInputAttribute("data-options", constructDataOptions());
    }

    private void initInput() {
        Widget widget = getWidget();
        if (widget instanceof ComplexPanel) {
            ComplexPanel p = (ComplexPanel) widget;
            for (int i = 0; i < p.getWidgetCount(); i++) {
                Widget w = p.getWidget(i);
                if (w instanceof TextBox) {
                    input = (TextBox) w;
                    break;
                }
            }
        }
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
        String s = input.getElement().getId();
        String fmt = internGetOption(s, "dateFormat");
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

    public void setDate(Date d) {
        if (input == null || d == null) return;
        String s = input.getElement().getId();
        internSetDate(s, d.getTime());
        JsDate jsd = internGetDate(s);

        // had to update text manually, because JS function 'setTheDate' didn't do that for some reason
        String fs = internFormat(s, getActiveDateFormat(), jsd);
        input.setText(fs);
    }

    public Date getDate() {
        if (input == null) return null;
        String s = input.getElement().getId();
        JsDate jsd = internGetDate(s);
        return new Date((long) jsd.getTime());
    }

    /**
     * Doesn't change anything, just formats passed date as string.
     */
    public String formatDate(Date d) {
        if (d == null) return null;
        String s = input.getElement().getId();
        JsDate jsd = JsDate.create(d.getTime());
        return internFormat(s, getActiveDateFormat(), jsd);
    }

    private native JsDate internGetDate(String id) /*-{
        return $wnd.$("#" + id).datebox('getTheDate');
    }-*/;

    private native String internFormat(String id, String fmt, JsDate d) /*-{
        return $wnd.$("#" + id).datebox('callFormat', fmt, d);
    }-*/;

    private native void internSetDate(String id, double d) /*-{
        $wnd.$("#" + id).datebox('setTheDate', new $wnd.Date(d));
    }-*/;

    // partial copy of __fmt() and __() functions from jqm-datebox.comp.calbox.js
    // datebox('option') is not in official documentation, see also:
    // http://stackoverflow.com/a/8217857
    // http://dev.jtsage.com/jQM-DateBox2/demos/api/events.html
    private native String internGetOption(String id, String val) /*-{
        var o = $wnd.$("#" + id).datebox('option');
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