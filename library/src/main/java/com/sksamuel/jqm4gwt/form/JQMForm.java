package com.sksamuel.jqm4gwt.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.form.elements.JQMFormWidget;
import com.sksamuel.jqm4gwt.form.validators.NotNullOrEmptyValidator;
import com.sksamuel.jqm4gwt.form.validators.Validator;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 21:36:02
 *         <p/>
 *         A {@link JQMForm} is a standard GWT panel that offers extra
 *         functionality for quick building of input forms. The framework offers
 *         built in validation and error reporting and simplified submission
 *         processing.
 *         <p/>
 *         All widgets for the form should be added in the implementing classes
 *         implementation of fieldAttach();
 *         <p/>
 *         Any {@link JQMSubmit} widgets that are added will be automatically
 *         wired to submit this form. Alternatively, any widget can be set to
 *         programatically submit the form by invoking submit();
 */
public class JQMForm extends FlowPanel {

    private static final String STYLE_VALIDATED = "jqm4gwt-fieldvalidated";

    private static final String STYLE_ERRORCONTAIN = "jqm4gwt-errorcontain";

    private static final String JQM4GWT_ERROR_LABEL_STYLENAME = "jqm4gwt-error";
    /**
     * The amount to adjust error scroll by so the error is not right at very
     * top
     */
    private static final int ERROR_SCROLL_OFFSET = 80;

    private static final String STYLE_ERROR_TYPE = "jqm4gwt-errortype-";

    private final FlowPanel generalErrors = new FlowPanel();

    private final List<Label> errors = new ArrayList();

    /**
     * The SubmissionHandler is invoked when the form is successfully
     * submitted.
     */
    private SubmissionHandler submissionHandler;

    /**
     * A mapping between the validators and the labels they use to show errors
     */
    private final Map<Validator, Label> validatorLabels = new HashMap();

    /**
     * A map containing the widgets and the validators that should be invoked
     * on those
     */
    private final Map<JQMFormWidget, Collection<Validator>> widgetValidators = new HashMap();

    /**
     * A map containing the validators and the elements/widgets that should
     * have the class changed depending on the result of the validation
     */
    private final Map<Validator, Widget> notifiedWidgets = new HashMap();

    protected JQMForm(SubmissionHandler handler) {
        this();
        setSubmissionHandler(handler);
    }

    /**
     * Constructor used by UiBinder. A SubmissionHandler must be set before calling submit.
     */
    public JQMForm() {
        add(generalErrors);
    }

    public SubmissionHandler getSubmissionHandler() {
        return submissionHandler;
    }

    public void setSubmissionHandler(SubmissionHandler submissionHandler) {
        this.submissionHandler = submissionHandler;
    }

    /**
     * Add the given submit button to the form and automatically have it set
     * to submit the form on a click event.
     */
    protected void add(JQMSubmit submit) {
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

    public void addRequired(JQMFormWidget widget, String msg, boolean immediate) {
        add(widget);
        setRequired(widget, msg, immediate);
    }

    /**
     * This method will automatically add a label element which will be made
     * visible with an error message when validate is called on this field and
     * fails.
     * <p/>
     * The label element will be located immediately after the supplied widget
     * (as first sibling).
     * <p/>
     * If the widget is not null then the an onBlur handler will be registered
     * that will trigger validation for this validator only.
     *
     * @param widget    the element to which the error message should be
     *                  associated with. If this param is null then the error will
     *                  be added as a generic error.
     * @param validator the validator that will perform the validation
     */
    // @Deprecated
    // public void addValidator(final JQMFormWidget widget, Validator
    // validator) {
    // addValidator(validator, widget);

    // final ValidationTuple tuple = new ValidationTuple();
    // tuple.label = new InlineLabel();
    // tuple.label.getElement().withId(Document.get().createUniqueId());
    // tuple.label.setStyleName(JQM4GWT_ERROR_STYLENAME);
    // tuple.validator = validator;
    //
    // if (widget == null) {
    // generalValidators.add(tuple);
    // } else {
    // Collection<ValidationTuple> collection =
    // widgetValidators.get(widget);
    // if (collection == null) {
    // collection = new ArrayList();
    // widgetValidators.put(widget, collection);
    // }
    // collection.add(tuple);
    // }
    //
    // // hide and attach the label to the appropriate place
    // tuple.label.setVisible(false);
    // add(tuple.label);
    //
    // errors.add(tuple.label);
    //
    // // add a blur handler to call validate on this widget
    // if (widget != null) {
    // widget.addBlurHandler(new BlurHandler() {
    //
    // @Override
    // public void onBlur(BlurEvent event) {
    // validate(widget);
    // }
    // });
    // }
    // }
    @Deprecated
    public void addValidator(JQMFormWidget widget, Validator validator) {
        addValidator(validator, widget);
    }

    /**
     * @see addValidator(null, validator);
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

    /**
     * Adds a validator and binds it to the collection of widgets. The widget
     * list can be empty.
     *
     * @param validator      the validator that will be invoked
     * @param notifiedWidget the widget that will be notified of the error. If null
     *                       then the firing widget will be used.
     * @param firingWidgets  the list of widgets that will fire the validator
     */
    public void addValidator(Widget notifiedWidget, Validator validator, boolean immediate, JQMFormWidget... firingWidgets) {

        // create a label that will show the validation error
        Label label = new InlineLabel();
        // the label must have an ID set because we will need to get the
        // element later for scrolling
        label.getElement().setId(Document.get().createUniqueId());
        label.setStyleName(JQM4GWT_ERROR_LABEL_STYLENAME);
        label.setVisible(false);

        // keep a list of the errors for easily iteration later
        errors.add(label);

        // connect the label and the validator
        validatorLabels.put(validator, label);

        // register the validator with the firing widgets
        registerValidatorWithFiringWidgets(validator, firingWidgets, immediate);

        if (notifiedWidget != null)
            notifiedWidgets.put(validator, notifiedWidget);

        // add the error label to the document as the next child of this
        // form container
        add(label);
    }

    public void clearValidationErrors() {
        for (Label label : validatorLabels.values()) {
            label.setVisible(false);
        }
        clearValidationStyles();
    }

    /**
     * Remove all validation styles
     */
    public void clearValidationStyles() {
        for (JQMFormWidget widget : widgetValidators.keySet()) {
            UIObject ui = widget.asWidget();
            Collection<Validator> validators = widgetValidators.get(widget);
            for (Validator v : validators) {
                if (notifiedWidgets.containsKey(v))
                    ui = notifiedWidgets.get(v);
                removeStyles(v, ui);
            }
        }
    }

    private int getFirstErrorOffset() {
        for (Label label : errors) {
            if (label.isVisible()) {
                String id = label.getElement().getId();
                return JQMContext.getTop(id);
            }
        }
        return 0;
    }

    public void hideFormProcessingDialog() {
        Mobile.hideLoadingDialog();
    }

    private void registerValidatorWithFiringWidget(final JQMFormWidget widget, Validator validator, boolean immediate) {
        // add a blur handler to call validate on this widget but only if
        // this is the first time this widget has been registered with a
        // validator
        if (immediate)
            if (widgetValidators.get(widget) == null)
                widget.addBlurHandler(new BlurHandler() {

                    @Override
                    public void onBlur(BlurEvent event) {
                        validate(widget);
                    }
                });

        if (widgetValidators.get(widget) == null)
            widgetValidators.put(widget, new ArrayList());
        widgetValidators.get(widget).add(validator);
    }

    private void registerValidatorWithFiringWidgets(Validator validator, JQMFormWidget[] widgets, boolean immediate) {
        if (widgets != null)
            for (JQMFormWidget widget : widgets) {
                registerValidatorWithFiringWidget(widget, validator, immediate);
            }
    }

    private void removeStyles(Validator validator, UIObject ui) {
        ui.removeStyleName(STYLE_ERROR_TYPE + validator.getClass().getName());
        ui.removeStyleName(STYLE_ERRORCONTAIN);
        ui.removeStyleName(STYLE_VALIDATED);
    }

    protected void scrollToFirstErorr() {
        int y = getFirstErrorOffset() - ERROR_SCROLL_OFFSET;
        Mobile.silentScroll(y);
    }

    /**
     * Set a general error on the form.
     */
    public void setError(String string) {
        Label errorLabel = new Label(string);
        errorLabel.setStyleName(JQM4GWT_ERROR_LABEL_STYLENAME);
        generalErrors.add(errorLabel);
        Window.scrollTo(0, 0);
    }

    /**
     * Sets the given widget to be required with a custom message. Then this
     * field will be checked to ensure it has a value set before the form will
     * be submitted.
     * <p/>
     * In effect, setting a field to required adds an implicit
     * "not null or empty" validator.
     */
    public void setRequired(JQMFormWidget widget, String msg) {
        setRequired(widget, msg);
    }

    public void setRequired(JQMFormWidget widget, String msg, boolean immediate) {
        addValidator(new NotNullOrEmptyValidator(widget, msg), immediate, widget);
    }

    public void showFormProcessingDialog(String msg) {
        Mobile.showLoadingDialog(msg);
    }

    /**
     * This method is invoked when the form is ready for submission. Typically
     * this method would be called from one of your submission buttons
     * automatically but it is possible to invoke it programatically.
     * <p/>
     * Before validation, the general errors are cleared.
     * <p/>
     * If the validation phase is passed the the submission handler will be
     * invoked. Before the handler is invoked, the page loading dialog will be
     * shown so that async requests can complete in the background.
     * <p/>
     * The {@link SubmissionHandler} must hide the loading dialog by calling
     * hideFormProcessingDialog() on the form or by calling
     * Mobile.hideLoadingDialog()
     */
    public void submit() {
        if (submissionHandler == null)
            throw new IllegalStateException("No SubmissionHandler has been set for this Form and it is in an invalid " +
                    "state for submit() until one has been defined.");
        generalErrors.clear();
        boolean validated = validate();
        if (validated) {
            showFormProcessingDialog("Submitting form");
            submissionHandler.onSubmit(this);
        } else {
            scrollToFirstErorr();
        }
    }

    /**
     * Perform validation for all validators, setting error messages where
     * appropriate.
     *
     * @return true if validation was successful for all validators, otherwise
     *         false.
     */
    public boolean validate() {
        boolean validated = true;

        // do widget validation first
        for (JQMFormWidget widget : widgetValidators.keySet()) {
            if (!validate(widget))
                validated = false;
        }

        // now validate the non-widget specific validators
        // for (ValidationTuple tuple : generalValidators) {
        // if (!validate(tuple))
        // validated = false;
        // }

        return validated;
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
     * @return true if this validator was successfully applied or false
     *         otherwise
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

        Label label = validatorLabels.get(validator);
        if (pass) {
            label.setText(null);
            label.setVisible(false);

        } else {
            label.setVisible(true);
            label.setText(msg);
            ui.addStyleName(STYLE_ERROR_TYPE + validator.getClass().getName());
        }

        if (ui.getStyleName().contains(STYLE_ERROR_TYPE)) {
            ui.addStyleName(STYLE_ERRORCONTAIN);
        } else {
            ui.addStyleName(STYLE_VALIDATED);
        }
    }
}
