package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.JQMCommon;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 *
 *         An implementation of the HTML5 number input type. On systems that do
 *         not support this, it will be degraded into a standard text element.
 *
 *         On most mobile devices this input will result in the soft keyboard
 *         showing the number pad.
 *
 * http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-text.html
 *
 */
public class JQMNumber extends JQMText {

    private static final String STYLE_ERRORCONTAIN = "jqm4gwt-errorcontain";

    private static final RegExp INT_REGEX = RegExp.compile("^[-+]?[0-9]*$");

    //private static final NumberConstants numConsts = LocaleInfo.getCurrentLocale().getNumberConstants();

    /** See <a href="https://www.regular-expressions.info/floatingpoint.html">Matching Floating Point Numbers with a Regular Expression</a>
     *  <br> Browsers are not consistent about decimal separator, could be period or comma,
     *  see <a href="https://stackoverflow.com/a/28395573">How to handle floats and decimal separators with html5 input type number</a>
     */
    private static final RegExp DOUBLE_REGEX0 = RegExp.compile("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");
    private static final RegExp DOUBLE_REGEX1 = RegExp.compile("^[-+]?[0-9]*\\,?[0-9]+([eE][-+]?[0-9]+)?$");

    private boolean validateAsInt;
    private boolean validateAsDouble;

    private HandlerRegistration blurReg;

	public JQMNumber() {
		this(null);
	}

	public JQMNumber(String text) {
		super(text);
		setType("number");
		setStep("any");
	}

	public String getStep() {
	    return JQMCommon.getAttribute(input.getElement(), "step");
	}

    /**
     * Some browsers (for instance Firefox) are quite strict about values entered into number widget.
     * Default is any, which allows entering fractional values, though up/down buttons will increment/decrement by 1.
     *
     * @param value - any, 1, 0.1, 0.01, ...
     */
	public void setStep(String value) {
	    JQMCommon.setAttribute(input.getElement(), "step", value);
	}

	public String getMin() {
	    return JQMCommon.getAttribute(input.getElement(), "min");
	}

	/** See <a href="https://www.w3schools.com/tags/att_input_min.asp">HTML input min Attribute</a> */
	public void setMin(String value) {
        JQMCommon.setAttribute(input.getElement(), "min", value);
    }

	public String getMax() {
        return JQMCommon.getAttribute(input.getElement(), "max");
    }

	/** See <a href="https://www.w3schools.com/tags/att_input_min.asp">HTML input max Attribute</a> */
	public void setMax(String value) {
        JQMCommon.setAttribute(input.getElement(), "max", value);
    }

    public boolean isValidateAsInt() {
        return validateAsInt;
    }

    public void setValidateAsInt(boolean validateAsInt) {
        this.validateAsInt = validateAsInt;
        checkBlurHandler();
    }

    public boolean isValidateAsDouble() {
        return validateAsDouble;
    }

    public void setValidateAsDouble(boolean validateAsDouble) {
        this.validateAsDouble = validateAsDouble;
        checkBlurHandler();
    }

    /** See <a href="https://stackoverflow.com/a/18853866">Html5 validity.valid</a> */
    private static native boolean isInputValid(Element elt) /*-{
        if (!elt.validity) return true; // we don't know
        return elt.validity.valid; // html5 validation
    }-*/;

    private boolean validate() {
        boolean isValid = isInputValid(input.getElement());
        if (isValid) {
            String s = getValue();
            if (!Empty.is(s)) {
                if (validateAsInt) {
                    isValid = INT_REGEX.test(s);
                }
                if (validateAsDouble) {
                    isValid = DOUBLE_REGEX0.test(s);
                    if (!isValid) isValid = DOUBLE_REGEX1.test(s);
                }
            }
        }
        if (isValid) {
            this.removeStyleName(STYLE_ERRORCONTAIN);
        } else {
            this.addStyleName(STYLE_ERRORCONTAIN);
        }
        return isValid;
    }

    private void checkBlurHandler() {
        if (blurReg != null || !isAttached()) return;
        if (validateAsInt || validateAsDouble) {
            blurReg = addBlurHandler(blur -> validate());
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        checkBlurHandler();
    }

    @Override
    protected void onUnload() {
        if (blurReg != null) {
            blurReg.removeHandler();
            blurReg = null;
        }
        super.onUnload();
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        super.setValue(value, fireEvents);
        validate();
    }

    public Integer getIntValue() {
        if (!validate()) return null;
        Integer rslt = null;
        try {
            rslt = Integer.parseInt(getValue());
        } catch (Exception e) {
            // nothing, can continue
        }
        return rslt;
    }

    public Double getDoubleValue() {
        if (!validate()) return null;
        Double rslt = null;
        String s = getValue();
        try {
            rslt = Double.parseDouble(s);
        } catch (Exception e) {
            if (s != null) {
                if (s.indexOf('.') >= 0) s = s.replaceAll("\\.", ",");
                else if (s.indexOf(',') >= 0) s = s.replaceAll("\\,", ".");
            }
            try {
                rslt = Double.parseDouble(s);
            } catch (Exception e1) {
                // nothing, can continue
            }
        }
        return rslt;
    }
}
