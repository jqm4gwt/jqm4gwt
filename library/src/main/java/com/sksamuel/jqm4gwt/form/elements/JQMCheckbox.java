package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIconPos;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.HasTheme;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMPageEvent;
import com.sksamuel.jqm4gwt.html.FormLabel;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 15:42:39
 */
public class JQMCheckbox extends Composite implements HasText<JQMCheckbox>, HasValue<Boolean>,
        HasMini<JQMCheckbox>, HasTheme<JQMCheckbox>, HasIconPos<JQMCheckbox>,
        HasCorners<JQMCheckbox>, HasInline<JQMCheckbox> {

    private TextBox input;

    private FormLabel label;

    private String id;

    private boolean valueChangeHandlerInitialized;

    // There are three internal states: null, false, true AND only two ui states: false, true.
    // Three internal states are needed to properly support data binding libraries (Errai for example).
    private Boolean internVal;

    private IconPos iconPos;

    /**
     * Should be followed by calls to set Input, Label and Id)
     */
    public JQMCheckbox() {
    }

    protected void init(TextBox input, FormLabel label, String id) {
        FlowPanel flow = new FlowPanel();
        initWidget(flow);
        flow.add(label);
        flow.add(input);

        setInput(input);
        setLabel(label);
        setId(id);
    }

    @UiConstructor
    public JQMCheckbox(String name, String text) {
        this();
        String id = Document.get().createUniqueId();
        TextBox input = new TextBox();
        input.getElement().setId(id);
        input.getElement().setAttribute("type", "checkbox");
        input.setName(name);

        FormLabel label = new FormLabel();
        label.setFor(id);
        label.setText(text);

        init(input, label, id);
    }

    public FormLabel getLabel() {
        return label;
    }

    public void setLabel(FormLabel label) {
        this.label = label;
        /*label.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
            }
        }, ClickEvent.getType());*/
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        // Initialization code
        if (!valueChangeHandlerInitialized) {
            valueChangeHandlerInitialized = true;
            input.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ValueChangeEvent.fire(JQMCheckbox.this, getValue());
                }
            });
        }
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public String getId() {
        return id;
    }

    public TextBox getInput() {
        return input;
    }

    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public boolean isMini() {
        InputElement e = input.getElement().cast();
        return "true".equals(e.getAttribute("data-mini"));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        InputElement e = input.getElement().cast();
        e.setAttribute("data-mini", String.valueOf(mini));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMCheckbox withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public JQMCheckbox withText(String text) {
        setText(text);
        return this;
    }

    public boolean isChecked() {
        InputElement e = input.getElement().cast();
        return e.isChecked();
    }

    @Override
    public Boolean getValue() {
        boolean checked = isChecked();
        if (checked) {
            internVal = true;
            return true;
        } else {
            if (internVal == null) return null;
            internVal = false;
            return false;
        }
    }

    @Override
    public void setValue(Boolean value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        boolean oldUiChecked = isChecked();
        Boolean oldValue = fireEvents ? getValue() : null;
        internVal = value;
        boolean newUiChecked = value == null ? false : value;
        if (oldUiChecked != newUiChecked) {
            InputElement e = input.getElement().cast();
            if (input.isAttached()) setChecked(e, newUiChecked);
            else e.setChecked(newUiChecked);
            e.setDefaultChecked(newUiChecked);
        }
        if (fireEvents) {
            boolean eq = internVal == oldValue || internVal != null && internVal.equals(oldValue);
            if (!eq) ValueChangeEvent.fire(this, internVal);
        }
    }

    private static native void setChecked(Element elt, boolean value) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-checkboxradio') !== undefined) {
            w.prop('checked', value).checkboxradio('refresh');
        } else {
            w.prop('checked', value);
        }
    }-*/;

    private static native void refresh(Element elt) /*-{
      var w = $wnd.$(elt);
      if (w.data('mobile-checkboxradio') !== undefined) {
          w.checkboxradio('refresh');
      }
    }-*/;

    public void setInput(TextBox input) {
        this.input = input;
        input.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                internVal = isChecked(); // user touched the checkbox, we must take current ui value
                //showMsg("setInput: " + (internVal ? "checked" : "unchecked"));
            }
        });
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTheme() {
        return input.getElement().getAttribute("data-theme");
    }

    @Override
    public void setTheme(String themeName) {
        if (themeName == null) {
            input.getElement().removeAttribute("data-theme");
        } else {
            input.getElement().setAttribute("data-theme", themeName);
        }
    }

    @Override
    public JQMCheckbox withTheme(String themeName) {
        setTheme(themeName);
        return this;
    }

    @Override
    public IconPos getIconPos() {
        return JQMCommon.getIconPos(label);
    }

    @Override
    public void setIconPos(IconPos pos) {
        iconPos = pos;
        JQMCommon.setIconPos(label, pos);
    }

    @Override
    public JQMCheckbox withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        // workaround for issue with data-iconpos resetting on js checkbox initialization
        Widget p = getParent();
        while (p != null) {
            if (p instanceof JQMPage) {
                ((JQMPage) p).addPageHandler(new JQMPageEvent.DefaultHandler() {
                    @Override
                    public void onInit(JQMPageEvent event) {
                        super.onInit(event);
                        IconPos pos = getIconPos();
                        if (pos != iconPos) {
                            setIconPos(iconPos);
                            refresh(input.getElement());
                        }
                        // Also data-corners is ignored, and 'ui-corner-all' class is added on init
                        if (JQMCommon.isCorners(label) != JQMCommon.isCornersEx(label)) {
                            setCorners(JQMCommon.isCorners(label));
                        }
                    }
                });
                break;
            }
            p = p.getParent();
        }
    }

    @Override
    public boolean isCorners() {
        return JQMCommon.isCornersEx(label);
    }

    @Override
    public void setCorners(boolean corners) {
        JQMCommon.setCornersEx(label, corners);
    }

    @Override
    public JQMCheckbox withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    @Override
    public void setInline(boolean value) {
        JQMCommon.setInlineEx(getElement(), value, "ui-inline");
    }

    @Override
    public boolean isInline() {
        return JQMCommon.isInlineEx(getElement(), "ui-inline");
    }

    @Override
    public JQMCheckbox withInline(boolean value) {
        setInline(value);
        return this;
    }

}
