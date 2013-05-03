package com.sksamuel.jqm4gwt.form;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.SubmitButton;
import com.sksamuel.jqm4gwt.*;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 * 
 *         An implementation of a submit button. Submit buttons are tightly
 *         integrated with forms and have special semantics when added to a
 *         {@link JQMForm}
 * 
 */
public class JQMSubmit extends JQMWidget implements HasText<JQMSubmit>, HasClickHandlers, HasTransition<JQMSubmit>, HasIcon<JQMSubmit> {

	private final SubmitButton	submit;

    /**
     * Nullary constructor.
     */
    public JQMSubmit() {
        this(null);
    }

	/**
	 * Create a {@link JQMSubmit} with the given label
	 */
	public JQMSubmit(String text) {

		submit = new SubmitButton(text);
		initWidget(submit);

		setStyleName("jqm4gwt-submit");
		setDataRole("button");
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return submit.addClickHandler(handler);
	}

	@Override
	public String getText() {
		return submit.getText();
	}

	@Override
	public void setText(String text) {
		submit.setText(text);
	}

    @Override
    public JQMSubmit withText(String text) {
        setText(text);
        return this;
    }

    @Override
    public Transition getTransition() {
        String attr = getElement().getAttribute("data-transition");
        if (attr == null)
            return null;
        return Transition.valueOf(attr);
    }

    @Override
    public void setTransition(Transition transition) {
        if (transition != null)
            setAttribute("data-transition", transition.getJQMValue());
        else
            removeAttribute("data-transition");
    }

    @Override
    public JQMSubmit withTransition(Transition transition) {
        setTransition(transition);
        return this;
    }

    @Override
    public JQMSubmit removeIcon() {
        getElement().removeAttribute("data-icon");
        return this;
    }

    @Override
    public void setBuiltInIcon(DataIcon icon) {
        if (icon == null)
            removeIcon();
        else
            setIconURL(icon.getJqmValue());
    }

    @Override
    public void setIconURL(String src) {
        if (src == null)
            removeIcon();
        else
            getElement().setAttribute("data-icon", src);
    }

    @Override
    public JQMSubmit withBuiltInIcon(DataIcon icon) {
        setBuiltInIcon(icon);
        return this;
    }

    @Override
    public JQMSubmit withIconURL(String src) {
        setIconURL(src);
        return this;
    }

    @Override
    public IconPos getIconPos() {
        String string = getAttribute("data-iconpos");
        return string == null ? null : IconPos.valueOf(string);
    }

    /**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    @Override
    public void setIconPos(IconPos pos) {
        if (pos == null)
            getElement().removeAttribute("data-iconpos");
        else
            getElement().setAttribute("data-iconpos", pos.getJqmValue());
    }

    /**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    @Override
    public JQMSubmit withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

}
