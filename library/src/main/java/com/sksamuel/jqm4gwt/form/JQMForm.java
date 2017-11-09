package com.sksamuel.jqm4gwt.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.form.elements.JQMFormWidget;
import com.sksamuel.jqm4gwt.form.validators.NotNullOrEmptyValidator;
import com.sksamuel.jqm4gwt.form.validators.Validator;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 21:36:02
 *         <br><br>
 *         A {@link JQMForm} is a standard GWT panel that offers extra
 *         functionality for quick building of input forms. The framework offers
 *         built in validation and error reporting and simplified submission
 *         processing.
 *         <br><br>
 *         Any {@link JQMSubmit} widgets that are added will be automatically
 *         wired to submit this form. Alternatively, any widget can be set to
 *         programatically submit the form by invoking submit();
 */
public class JQMForm extends FlowPanel {

    /** For example icon can be added: "ui-icon-alert ui-btn-icon-left" **/
    public static String globalValidationErrorStyles;

    private static final String STYLE_OK_VALIDATED = "jqm4gwt-fieldvalidated";

    private static final String STYLE_ERRORCONTAIN = "jqm4gwt-errorcontain";

    private static final String STYLE_ERROR_TYPE = "jqm4gwt-errortype-";

    private static final String STYLE_FORM_REQUIRED = "jqm4gwt-form-required";

    private static final String STYLE_FORM_VALIDATOR = "jqm4gwt-form-validator-";

    private static final String JQM4GWT_ERROR_LABEL_STYLENAME = "jqm4gwt-error";
    private static final String JQM4GWT_GENERAL_ERROR_LABEL_STYLENAME = "jqm4gwt-general-error";

    private final FlowPanel generalErrors = new FlowPanel();

    private final Map<Label, Widget> errors = new LinkedHashMap<>();

    /** A mapping between the validators and the labels they use to show errors */
    private final Map<Validator, Label> validatorLabels = new HashMap<Validator, Label>();

    /** A map containing the widgets and the validators that should be invoked on those */
    private final Map<JQMFormWidget, Collection<Validator>> widgetValidators =
            new HashMap<JQMFormWidget, Collection<Validator>>();

    private final Map<JQMFormWidget, Collection<HandlerRegistration>> widgetHandlers =
            new HashMap<JQMFormWidget, Collection<HandlerRegistration>>();

    /**
     * A map containing the validators and the elements/widgets that should
     * have the class changed depending on the result of the validation
     */
    private final Map<Validator, Widget> notifiedWidgets = new HashMap<Validator, Widget>();

    /** The SubmissionHandler is invoked when the form is successfully submitted. */
    private SubmissionHandler<?> submissionHandler;

    protected JQMForm(SubmissionHandler<?> handler) {
        this();
        setSubmissionHandler(handler);
    }

    /**
     * Constructor used by UiBinder. A SubmissionHandler must be set before calling submit.
     */
    public JQMForm() {
        setStyleName("jqm4gwt-form");
        add(generalErrors);
    }

    public SubmissionHandler<?> getSubmissionHandler() {
        return submissionHandler;
    }

    public void setSubmissionHandler(SubmissionHandler<?> submissionHandler) {
        this.submissionHandler = submissionHandler;
    }

    /**
     * Add the given submit button to the form and automatically have it set
     * to submit the form on a click event.
     */
    public void add(JQMSubmit submit) {
        super.add(submit);
        submit.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                submit();
            }
        });
    }

    /**
     * Adds the given widget and sets it to be required. Then this field will
     * be checked to ensure it has a value set before the form will be
     * submitted. In effect, setting a field to required adds an implicit
     * "not null or empty" validator.
     */
    public void addRequired(JQMFormWidget widget) {
        addRequired(widget, true);
    }

    public void addRequired(JQMFormWidget widget, boolean immediate) {
        addRequired(widget, "This field cannot be empty", immediate);
    }

    public void addRequired(JQMFormWidget widget, String msg) {
        addRequired(widget, msg, true);
    }

    /**
     * @param immediate - if true then validator will be called during onBlur() event
     */
    public void addRequired(JQMFormWidget widget, String msg, boolean immediate) {
        add(widget);
        setRequired(widget, msg, immediate);
    }

    /**
     * This method will automatically add a label element which will be made
     * visible with an error message when validate is called on this field and
     * fails.
     * <br>
     * The label element will be located immediately after the supplied widget
     * (as first sibling).
     * <br>
     * If the widget is not null then the an onBlur handler will be registered
     * that will trigger validation for this validator only.
     *
     * @param widget    the element to which the error message should be
     *                  associated with. If this param is null then the error will
     *                  be added as a generic error.
     * @param validator the validator that will perform the validation
     */
    @Deprecated
    public void addValidator(JQMFormWidget widget, Validator validator) {
        addValidator(validator, widget);
    }

    /**
     * see addValidator(null, validator);
     */
    public void addValidator(Validator validator) {
        addValidator(validator, (JQMFormWidget) null);
    }

    public void addValidator(Validator validator, boolean immediate, JQMFormWidget... firingWidgets) {
        addValidator(null, validator, immediate, firingWidgets);
    }

    public void addValidator(Validator validator, JQMFormWidget... firingWidgets) {
        addValidator(null, validator, true, firingWidgets);
    }

    private void addErrorLabel(Validator validator, Label label, Widget widget) {
        errors.put(label, widget); // keep a list of the errors for easily iteration later
        validatorLabels.put(validator, label); // connect the label and the validator
    }

    /**
     * Adds a validator and binds it to the collection of widgets. The widget
     * list can be empty.
     *
     * @param validator      the validator that will be invoked
     * @param notifiedWidget the widget that will be notified of the error. If null
     *                       then the firing widget will be used.
     * @param firingWidgets  the list of widgets that will fire the validator
     * @param immediate - if true then validator will be called during firingWidgets onBlur() event
     * @param positionErrorAfter - optional, if defined then error label will be placed
     * right after this widget, otherwise it will be added as the current last one.
     */
    public void addValidator(Widget notifiedWidget, Validator validator, boolean immediate,
                             Widget positionErrorAfter, JQMFormWidget... firingWidgets) {

        boolean labelAdded = false;
        if (firingWidgets != null) {
            for (JQMFormWidget w : firingWidgets) {
                Label la = w.addErrorLabel();
                if (la == null) continue;
                labelAdded = true;
                addErrorLabel(validator, la, w.asWidget());
            }
        }
        if (!labelAdded) {
            Label label = new InlineLabel(); // create a label that will show the validation error
            label.setStyleName(JQM4GWT_ERROR_LABEL_STYLENAME);
            if (globalValidationErrorStyles != null && !globalValidationErrorStyles.isEmpty()) {
                JQMCommon.addStyleNames(label, globalValidationErrorStyles);
            }
            label.setVisible(false);
            if (positionErrorAfter == null) {
                addErrorLabel(validator, label, null/*widget*/);
                // add the error label to the document as the next child of this form container
                add(label);
            } else {
                boolean inserted = false;
                Widget w = positionErrorAfter;
                while (w != null) {
                    int i = getWidgetIndex(w);
                    if (i >= 0) {
                        i++; // next after w
                        while (i < getWidgetCount()) {
                            Widget wi = getWidget(i);
                            if (wi instanceof Label && JQMCommon.hasStyle(wi, JQM4GWT_ERROR_LABEL_STYLENAME)) {
                                i++; // next after previous errors
                            } else {
                                break;
                            }
                        }
                        insert(label, i);
                        inserted = true;
                        break;
                    }
                    w = w.getParent();
                }
                if (!inserted) {
                    addErrorLabel(validator, label, null/*widget*/);
                    add(label);
                } else {
                    addErrorLabel(validator, label, positionErrorAfter);
                }
            }
        }

        registerValidatorWithFiringWidgets(validator, firingWidgets, immediate);
        boolean required = validator instanceof NotNullOrEmptyValidator;
        String validatorClass = STYLE_FORM_VALIDATOR + getShortClassName(validator.getClass());
        if (notifiedWidget != null) {
            notifiedWidgets.put(validator, notifiedWidget);
            notifiedWidget.getElement().addClassName(validatorClass);
            if (required) notifiedWidget.getElement().addClassName(STYLE_FORM_REQUIRED);
        } else if (firingWidgets != null) {
            for (JQMFormWidget w : firingWidgets) {
                w.asWidget().getElement().addClassName(validatorClass);
                if (required) w.asWidget().getElement().addClassName(STYLE_FORM_REQUIRED);
            }
        }
    }

    public void removeValidator(JQMFormWidget firingWidget, Validator validator) {
        if (firingWidget == null || validator == null) return;
        Collection<Validator> validators = widgetValidators.get(firingWidget);
        if (validators == null || !validators.contains(validator)) return;
        validators.remove(validator);
        if (validators.isEmpty()) {
            widgetValidators.remove(firingWidget);
            Collection<HandlerRegistration> handlers = widgetHandlers.get(firingWidget);
            if (handlers != null) {
                for (HandlerRegistration h : handlers) h.removeHandler();
                widgetHandlers.remove(firingWidget);
            }
        }
        Label label = validatorLabels.get(validator);
        if (label != null) {
            remove(label);
            errors.remove(label);
            validatorLabels.remove(validator);
        }
        boolean required = validator instanceof NotNullOrEmptyValidator;
        String validatorClass = STYLE_FORM_VALIDATOR + getShortClassName(validator.getClass());
        Widget notifiedWidget = notifiedWidgets.get(validator);
        if (notifiedWidget != null) {
            notifiedWidgets.remove(validator);
            notifiedWidget.getElement().removeClassName(validatorClass);
            if (required) notifiedWidget.getElement().removeClassName(STYLE_FORM_REQUIRED);
            removeStyles(validator, notifiedWidget);
        }
        firingWidget.asWidget().getElement().removeClassName(validatorClass);
        if (required) firingWidget.asWidget().getElement().removeClassName(STYLE_FORM_REQUIRED);
        removeStyles(validator, firingWidget.asWidget());
    }

    public void addValidator(Widget notifiedWidget, Validator validator, boolean immediate,
                             JQMFormWidget... firingWidgets) {

        final Widget pos;
        if (firingWidgets != null && firingWidgets.length > 0) {
            pos = firingWidgets[firingWidgets.length - 1].asWidget();
        } else {
            pos = notifiedWidget;
        }
        addValidator(notifiedWidget, validator, immediate, pos, firingWidgets);
    }

    public void clearValidationErrors() {
        for (Label label : validatorLabels.values()) {
            label.setVisible(false);
        }
        clearValidationStyles();
    }

    /** Remove all validation styles */
    public void clearValidationStyles() {
        for (JQMFormWidget widget : widgetValidators.keySet()) {
            UIObject ui = widget.asWidget();
            Collection<Validator> validators = widgetValidators.get(widget);
            for (Validator v : validators) {
                if (notifiedWidgets.containsKey(v)) {
                    ui = notifiedWidgets.get(v);
                }
                removeStyles(v, ui);
            }
        }
    }

    public static class FormErrorWidgets {
        public final Label label;
        public final Widget widget;

        public FormErrorWidgets(Label label, Widget widget) {
            this.label = label;
            this.widget = widget;
        }
    }

    public FormErrorWidgets getFirstError() {
        for (Entry<Label, Widget> i : errors.entrySet()) {
            Label label = i.getKey();
            if (label.isVisible()) {
                return new FormErrorWidgets(label, i.getValue());
            }
        }
        return null;
    }

    public void hideFormProcessingDialog() {
        Mobile.hideLoadingDialog();
    }

    private void registerValidatorWithFiringWidget(final JQMFormWidget widget, Validator validator,
                                                   boolean immediate) {

        boolean registered = widgetValidators.get(widget) != null;

        // add a blur handler to call validate on this widget but only if
        // this is the first time this widget has been registered with a validator
        if (immediate) {
            if (!registered) {
                HandlerRegistration blurReg = widget.addBlurHandler(new BlurHandler() {
                    @Override
                    public void onBlur(BlurEvent event) {
                        validate(widget);
                    }
                });
                ArrayList<HandlerRegistration> handlerLst = new ArrayList<HandlerRegistration>();
                handlerLst.add(blurReg);
                widgetHandlers.put(widget, handlerLst);
            }
        }

        if (!registered) {
            widgetValidators.put(widget, new ArrayList<Validator>());
        }
        widgetValidators.get(widget).add(validator);
    }

    private void registerValidatorWithFiringWidgets(Validator validator, JQMFormWidget[] widgets,
                                                    boolean immediate) {
        if (widgets != null)
            for (JQMFormWidget widget : widgets) {
                registerValidatorWithFiringWidget(widget, validator, immediate);
            }
    }

    public int getValidatorCount(JQMFormWidget widget) {
        if (widget == null) return 0;
        Collection<Validator> validators = widgetValidators.get(widget);
        return validators != null ? validators.size() : 0;
    }

    public Validator getValidator(JQMFormWidget widget, int validatorIndex) {
        if (widget == null || validatorIndex < 0) return null;
        Collection<Validator> validators = widgetValidators.get(widget);
        if (validators == null || validatorIndex >= validators.size()) return null;
        int i = 0;
        Iterator<Validator> iter = validators.iterator();
        while (iter.hasNext()) {
            Validator v = iter.next();
            if (i == validatorIndex) return v;
            i++;
        }
        return null;
    }

    private static String getShortClassName(Class<?> clazz) {
        if (clazz == null) return null;
        String s = clazz.getName();
        int p = s.lastIndexOf('.');
        if (p >= 0) return s.substring(p + 1);
        else return s;
    }

    private static void removeStyles(Validator validator, UIObject ui) {
        ui.removeStyleName(STYLE_ERROR_TYPE + getShortClassName(validator.getClass()));
        ui.removeStyleName(STYLE_ERRORCONTAIN);
        ui.removeStyleName(STYLE_OK_VALIDATED);
    }

    public void scrollToFirstError() {
        FormErrorWidgets err = getFirstError();
        if (err == null) return;
        // trying to make both label and widget visible into view
        if (err.label != null) err.label.getElement().scrollIntoView();
        if (err.widget != null) err.widget.getElement().scrollIntoView();
    }

    /**
     * Set a general error on the form.
     */
    public void setError(String string) {
        Label errorLabel = new Label(string);
        errorLabel.setStyleName(JQM4GWT_ERROR_LABEL_STYLENAME);
        errorLabel.addStyleName(JQM4GWT_GENERAL_ERROR_LABEL_STYLENAME);
        generalErrors.add(errorLabel);
        generalErrors.getElement().scrollIntoView();
    }

    /**
     * Sets the given widget to be required with a custom message. Then this
     * field will be checked to ensure it has a value set before the form will be submitted.
     * <br>
     * In effect, setting a field to required adds an implicit "not null or empty" validator.
     */
    public void setRequired(JQMFormWidget widget, String msg) {
        setRequired(widget, msg, true);
    }

    public void setRequired(JQMFormWidget widget, String msg, boolean immediate) {
        addValidator(new NotNullOrEmptyValidator(widget, msg), immediate, widget);
    }

    public boolean isRequired(JQMFormWidget widget) {
        if (widget == null) return false;
        Collection<Validator> validators = widgetValidators.get(widget);
        if (validators == null) return false;
        Iterator<Validator> iter = validators.iterator();
        while (iter.hasNext()) {
            Validator validator = iter.next();
            if (validator instanceof NotNullOrEmptyValidator) return true;
        }
        return false;
    }

    public void removeRequired(JQMFormWidget widget) {
        if (widget == null) return;
        Collection<Validator> validators = widgetValidators.get(widget);
        if (validators == null) return;
        Validator notNullV = null;
        List<Validator> notNullLst = null;
        Iterator<Validator> iter = validators.iterator();
        while (iter.hasNext()) {
            Validator validator = iter.next();
            if (validator instanceof NotNullOrEmptyValidator) {
                if (notNullV == null) notNullV = validator;
                else {
                    if (notNullLst == null) notNullLst = new ArrayList<Validator>();
                    notNullLst.add(validator);
                }
            }
        }
        if (notNullV != null) removeValidator(widget, notNullV);
        if (notNullLst != null) {
            for (Validator v : notNullLst) removeValidator(widget, v);
        }
    }

    public void showFormProcessingDialog(String msg) {
        Mobile.showLoadingDialog(msg);
    }

    /**
     * This method is invoked when the form is ready for submission. Typically
     * this method would be called from one of your submission buttons
     * automatically but it is possible to invoke it programmatically.
     * <br>
     * Before validation, the general errors are cleared.
     * <br>
     * If the validation phase is passed the the submission handler will be
     * invoked. Before the handler is invoked, the page loading dialog will be
     * shown so that async requests can complete in the background.
     * <br>
     * The {@link SubmissionHandler} must hide the loading dialog by calling
     * hideFormProcessingDialog() on the form or by calling Mobile.hideLoadingDialog()
     */
    public void submit(String... submitMsgs) {
        if (submissionHandler == null)
            throw new IllegalStateException(
                    "No SubmissionHandler has been set for this Form and it is in an invalid " +
                    "state for submit() until one has been defined.");
        generalErrors.clear();
        boolean validated = validate(false/*scrollToFirstError*/);
        if (validated) {
            String s = null;
            if (submitMsgs.length > 0) s = submitMsgs[0];
            if (s == null || s.isEmpty()) s = "Submitting form";
            showFormProcessingDialog(s);
            @SuppressWarnings("unchecked")
            SubmissionHandler<JQMForm> h = (SubmissionHandler<JQMForm>) submissionHandler;
            h.onSubmit(this);
        } else {
            scrollToFirstError();
        }
    }

    /**
     * Perform validation for all validators, setting error messages where appropriate.
     *
     * @param scrollToFirstError - if true then in case of unsuccessful validation first error widget will be scrolled into view.
     * @return true if validation was successful for all validators, otherwise false.
     */
    public boolean validate(boolean scrollToFirstError) {
        boolean validated = true;

        for (JQMFormWidget widget : widgetValidators.keySet()) {
            if (!validate(widget))
                validated = false;
        }

        if (!validated && scrollToFirstError) scrollToFirstError();
        return validated;
    }

    /**
     * Perform validation for all validators, setting error messages where appropriate.
     *
     * @return true if validation was successful for all validators, otherwise false.
     */
    public boolean validate() {
        return validate(true/*scrollToFirstError*/);
    }

    /**
     * Performs validation for a single widget, first resetting all validation
     * messages on that widget.
     */
    protected boolean validate(JQMFormWidget widget) {

        boolean validated = true;
        Collection<Validator> validators = widgetValidators.get(widget);
        for (Validator v : validators)
            if (!validate(v, widget.asWidget()))
                validated = false;

        return validated;
    }

    /**
     * Perform validation for a single validator
     *
     * @param ui the {@link UIObject} to change the stylesheet on
     * @return true if this validator was successfully applied or false otherwise
     */
    protected boolean validate(Validator validator, UIObject ui) {

        if (notifiedWidgets.containsKey(validator))
            ui = notifiedWidgets.get(validator);

        String msg = validator.validate();
        if (msg == null || msg.length() == 0) {
            validationStyles(validator, null, ui, true);
            return true;
        } else {
            validationStyles(validator, msg, ui, false);
            return false;
        }
    }

    private void validationStyles(Validator validator, String msg, UIObject ui, boolean pass) {
        removeStyles(validator, ui);

        final Label label = validatorLabels.get(validator);
        if (pass) {
            // delay cleaning to allow normal button click processing
            Scheduler.get().scheduleEntry(new ScheduledCommand() {
                @Override
                public void execute() {
                    label.setText(null);
                    label.setVisible(false);
                }
            });
        } else {
            label.setVisible(true);
            label.setText(msg);
            ui.addStyleName(STYLE_ERROR_TYPE + getShortClassName(validator.getClass()));
        }

        if (ui.getStyleName().contains(STYLE_ERROR_TYPE)) {
            ui.addStyleName(STYLE_ERRORCONTAIN);
        } else {
            ui.addStyleName(STYLE_OK_VALIDATED);
        }
    }
}
