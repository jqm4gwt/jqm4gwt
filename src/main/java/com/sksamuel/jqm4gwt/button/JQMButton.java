package com.sksamuel.jqm4gwt.button;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIcon;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasRel;
import com.sksamuel.jqm4gwt.HasShadow;
import com.sksamuel.jqm4gwt.HasTransition;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContainer;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.Transition;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 14:02:24
 * 
 *         An implementation of a Jquery mobile button
 * 
 * @link 
 *       http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/buttons/buttons-types
 *       .html
 * 
 */
public class JQMButton extends JQMWidget implements HasText, HasRel, HasTransition<JQMButton>, HasClickHandlers, HasInline<JQMButton>,
		HasIcon<JQMButton>, HasCorners, HasShadow, HasMini {

	/**
	 * Create a {@link JQMButton} with the given text that does not link to
	 * anything. This button would only react to events if a link is added or
	 * a click handler is attached.
	 * 
	 * @param text
	 *              the text to display on the button
	 */
	public JQMButton(String text) {
		initWidget(new Anchor(text));
		setStyleName("jqm4gwt-button");
		setDataRole("button");
		setId();
	}

	/**
	 * Convenience constructor that creates a button that shows the given
	 * {@link JQMPage} when clicked. The link will use a Transition.POP type.
	 * 
	 * Note that the page param is an already instantiated page and thus will
	 * be immediately inserted into the DOM. Do not use this constructor when
	 * you want to lazily add the page.
	 * 
	 * @param text
	 *              the text to display on the button
	 * 
	 * @param page
	 *              the {@link JQMPage} to create a link to
	 * 
	 */
	public JQMButton(String text, final JQMContainer c) {
		this(text, c, null);
	}

	/**
	 * Convenience constructor that creates a button that shows the given
	 * {@link JQMPage} when clicked.
	 * 
	 * Note that the page param is an already instantiated page and thus will
	 * be immediately inserted into the DOM. Do not use this constructor when
	 * you want to lazily add the page.
	 * 
	 * @param text
	 *              the text to display on the button
	 * 
	 * @param page
	 *              the {@link JQMPage} to create a link to
	 * 
	 * @param t
	 *              the transition type to use
	 */
	public JQMButton(String text, final JQMContainer c, final Transition t) {
		this(text, "#" + c.getId(), t);
		setRel(c.getRelType());
	}

	/**
	 * Convenience constructor that creates a button that shows the given url
	 * when clicked. The link will use a Transition.POP type.
	 * 
	 * Note that the page param is an already instantiated page and thus will
	 * be immediately inserted into the DOM. Do not use this constructor when
	 * you want to lazily add the page.
	 * 
	 * @param text
	 *              the text to display on the button
	 * 
	 * @param url
	 *              the HTTP url to create a link to
	 * 
	 */
	public JQMButton(String text, String url) {
		this(text);
		if (url != null)
			setAttribute("href", url);
	}

	/**
	 * Convenience constructor that creates a button that shows the given url
	 * when clicked.
	 * 
	 * Note that the page param is an already instantiated page and thus will
	 * be immediately inserted into the DOM. Do not use this constructor when
	 * you want to lazily add the page.
	 * 
	 * @param text
	 *              the text to display on the button
	 * 
	 * @param url
	 *              the HTTP url to create a link to
	 * 
	 * @param t
	 *              the transition type to use
	 */
	public JQMButton(String text, String url, final Transition t) {
		this(text);
		if (url != null)
			setAttribute("href", url);
		if (t != null)
			setTransition(t);
	}

	protected JQMButton(Widget widget) {
		initWidget(widget);
		setStyleName("jqm4gwt-button");
		setDataRole("button");
		setId();
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public IconPos getIconPos() {
		String string = getAttribute("data-iconpos");
		return string == null ? null : IconPos.valueOf(string);
	}

	@Override
	public String getRel() {
		return getElement().getAttribute("rel");
	}

	@Override
	public String getText() {
		String innerText = getElement().getInnerText();
		return innerText;
	}

	@Override
	public Transition getTransition() {
		String attr = getElement().getAttribute("data-transition");
		if (attr == null)
			return null;
		return Transition.valueOf(attr);
	}

	@Override
	public boolean isCorners() {
		return "true".equals(getAttribute("data-corners"));
	}

	/**
	 * Returns true if this button is set to load the linked page as a dialog
	 * page
	 * 
	 * @return true if this link will show as a dialog
	 */
	public boolean isDialog() {
		return "true".equals(getAttribute("data-rel"));
	}

	@Override
	public boolean isIconShadow() {
		return "true".equals(getAttribute("data-iconshadow"));
	}

	/**
	 * @return true if this button is set to inline
	 */
	@Override
	public boolean isInline() {
		return "true".equals(getAttribute("data-line"));
	}

	@Override
	public boolean isMini() {
		return "true".equals(getAttribute("data-mini"));
	}

	@Override
	public JQMButton removeIcon() {
		getElement().removeAttribute("data-icon");
		return this;
	}

	/**
	 * Sets this buttom to be a back button. This will override any URL set on
	 * the button.
	 */
	public void setBack(boolean back) {
		if (back)
			getElement().setAttribute("data-rel", "back");
		else
			getElement().removeAttribute("data-rel");
	}

	@Override
	public void setCorners(boolean corners) {
		setAttribute("data-corners", String.valueOf(corners));
	}

	/**
	 * Sets this buttom to be a dialog button. This changes the look and feel
	 * of the page that is loaded as a consequence of clicking on this button.
	 */
	public void setDialog(boolean dialog) {
		if (dialog)
			setAttribute("data-rel", "dialog");
		else
			removeAttribute("data-rel");
	}

	/**
	 * Short cut for setRel("external");
	 */
	public JQMButton setExternal() {
		setRel("external");
		return this;
	}

	/**
	 * Sets the icon used by this button. See {@link DataIcon}.
	 * @return 
	 */
	@Override
	public JQMButton setIcon(DataIcon icon) {
		if (icon == null)
			removeIcon();
		else
			getElement().setAttribute("data-icon", icon.getJqmValue());
		return this;
	}

	@Override
	public JQMButton setIcon(String src) {
		if (src == null)
			removeIcon();
		else
			getElement().setAttribute("data-icon", src);
		return this;
	}

	/**
	 * Sets the position of the icon. If you desire an icon only button then
	 * set the position to {@link IconPos.NOTEXT}
	 */
	@Override
	public void setIconPos(IconPos pos) {
		if (pos == null)
			getElement().removeAttribute("data-iconpos");
		else
			getElement().setAttribute("data-iconpos", pos.getJqmValue());
	}

	public void setIconShadow(boolean shadow) {
		getElement().setAttribute("data-iconshadow", String.valueOf(shadow));
	}

	/**
	 * Sets this button to be inline.
	 * 
	 * NOTE: If this button is inside a {@link JQMButtonGroup} then you must
	 * call setInline(boolean) on the button group itself and not each button
	 * individually.
	 * 
	 * @param inline
	 *              true to change to line or false to switch to full width
	 * @return 
	 */
	@Override
	public JQMButton setInline(boolean inline) {
		if (inline)
			setAttribute("data-inline", "true");
		else
			removeAttribute("data-inline");
		return this;
	}

	/**
	 * If set to true then renders a smaller version of the standard-sized element.
	 */
	@Override
	public void setMini(boolean mini) {
		setAttribute("data-mini", String.valueOf(mini));
	}

	@Override
	public void setRel(String rel) {
		if (rel == null)
			getElement().removeAttribute("data-rel");
		else
			getElement().setAttribute("data-rel", rel);
	}

	/**
	 * Applies the drop shadow style to the select button if set to true.
	 */
	@Override
	public void setShadow(boolean shadow) {
		setAttribute("data-shadow", String.valueOf(shadow));
	}

	@Override
	public void setText(String text) {
		// if the button has already been rendered then we need to go down
		// deep until we get the
		// final span
		Element e = getElement();
		while (e.getFirstChildElement() != null) {
			e = e.getFirstChildElement();
		}
		e.setInnerText(text);
	}

	/**
	 * Sets the transition to be used by this button when loading the URL.
	 */
	@Override
	public JQMButton setTransition(Transition transition) {
		if (transition != null)
			setAttribute("data-transition", transition.getJQMValue());
		else
			removeAttribute("data-transition");
		return this;
	}

	public JQMButton setTransitionReverse(boolean reverse) {
		if (reverse)
			setAttribute("data-direction", "reverse");
		else
			removeAttribute("data-direction");
		return this;
	}

}
