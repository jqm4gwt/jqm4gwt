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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIconPos;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 12 Jul 2011 15:42:39
 */
public class JQMCheckbox extends JQMWidget implements HasText<JQMCheckbox>, HasValue<Boolean>,
        HasMini<JQMCheckbox>, HasIconPos<JQMCheckbox>, HasCorners<JQMCheckbox>, HasInline<JQMCheckbox> {

    private TextBox input;

    private FormLabel label;

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
        //CustomFlowPanel flow = new CustomFlowPanel(Document.get().createFormElement());
        FlowPanel flow = new FlowPanel();
        initWidget(flow);
        CustomFlowPanel la = new CustomFlowPanel(label.getElement());
        flow.add(la);
        la.add(input);

        // OLD code: worked OK, except of nested JQMList case.
        // FlowPanel flow = new FlowPanel();
        // flow.add(input);
        // flow.add(label);

        setInput(input);
        setLabel(label);
        if (id != null && !id.isEmpty()) setId(id);
    }

    @UiConstructor
    public JQMCheckbox(String name, String text) {
        this();
        String id = Document.get().createUniqueId();
        TextBox input = new TextBox();
        input.getElement().setId(id);
        input.getElement().setAttribute("type", getInputType());
        input.setName(name);

        FormLabel label = new FormLabel();
        label.setFor(id);
        label.setText(text);

        init(input, label, null/* id for flow is not needed */);
    }

    protected String getInputType() {
        return "checkbox";
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

    /**
     * Will be raised before actual switch, so getValue() will return "old" value.
     * <br> For getting "new" value use addValueChangeHandler().
     * <br> If used as header widget on JQMCollapsible then manual refresh() call is needed to update checkmark.
     */
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
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
        return JQMCommon.isMini(input);
    }

    /** If set to true then renders a smaller version of the standard-sized element. */
    @Override
    public void setMini(boolean mini) {
        JQMCommon.setMini(input, mini);
    }

    /** If set to true then renders a smaller version of the standard-sized element. */
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

    public void refresh() {
        refresh(input.getElement());
    }

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

    @Override
    public String getTheme() {
        return JQMCommon.getTheme(input);
    }

    @Override
    public void setTheme(String themeName) {
        JQMCommon.setTheme(input, themeName);
        JQMButton.setTheme(label.getElement(), themeName); // to support dynamic theme changing
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

    private static native void bindCreated(Element elt, JQMCheckbox cb) /*-{
        $wnd.$(elt).on( 'checkboxradiocreate', function( event, ui ) {
            cb.@com.sksamuel.jqm4gwt.form.elements.JQMCheckbox::created()();
        });
    }-*/;

    private static native void unbindCreated(Element elt) /*-{
        $wnd.$(elt).off( 'checkboxradiocreate' );
    }-*/;

    private void created() {
        // workaround for issue with data-iconpos resetting on js checkbox initialization
        IconPos pos = getIconPos();
        if (pos != iconPos) {
            setIconPos(iconPos);
            refresh();
        }
        // Also data-corners is ignored, and 'ui-corner-all' class is added on init
        if (JQMCommon.isCorners(label) != JQMCommon.isCornersEx(label)) {
            setCorners(JQMCommon.isCorners(label));
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        bindCreated(getElement(), this);
    }

    @Override
    protected void onUnload() {
        unbindCreated(getElement());
        super.onUnload();
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
