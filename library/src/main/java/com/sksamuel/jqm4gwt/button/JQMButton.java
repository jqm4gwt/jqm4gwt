package com.sksamuel.jqm4gwt.button;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasCorners;
import com.sksamuel.jqm4gwt.HasIcon;
import com.sksamuel.jqm4gwt.HasIconShadow;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.HasRel;
import com.sksamuel.jqm4gwt.HasTransition;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMContainer;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.Transition;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 14:02:24
 *         <p/>
 *         An implementation of a Jquery mobile button
 * @link http://jquerymobile.com/demos/1.2.0/docs/buttons/buttons-types.html
 */
public class JQMButton extends JQMWidget implements HasText<JQMButton>, HasRel<JQMButton>, HasTransition<JQMButton>, HasClickHandlers, HasInline<JQMButton>,
        HasIcon<JQMButton>, HasCorners, HasIconShadow, HasMini<JQMButton> {

    /**
     * Create a {@link JQMButton} with the given text that does not link to
     * anything. This button would only react to events if a link is added or
     * a click handler is attached.
     *
     * @param text the text to display on the button
     */
    public @UiConstructor JQMButton(String text) {
        this(new Anchor(text));
    }

    /**
     * Convenience constructor that creates a button that shows the given
     * {@link JQMPage} when clicked. The link will use a Transition.POP type.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param c    the {@link JQMContainer} to create a link to
     */
    public JQMButton(String text, final JQMContainer c) {
        this(text, c, null);
    }

    /**
     * Convenience constructor that creates a button that shows the given
     * {@link JQMPage} when clicked.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param c    the {@link JQMContainer} to create a link to
     * @param t    the transition type to use
     */
    public JQMButton(String text, final JQMContainer c, final Transition t) {
        this(text, "#" + c.getId(), t);
        withRel(c.getRelType());
    }

    /**
     * Convenience constructor that creates a button that shows the given url
     * when clicked. The link will use a Transition.POP type.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param url  the HTTP url to create a link to
     */
    public JQMButton(String text, String url) {
        this(text, url, null);
    }

    /**
     * Convenience constructor that creates a button that shows the given url
     * when clicked.
     * <p/>
     * Note that the page param is an already instantiated page and thus will
     * be immediately inserted into the DOM. Do not use this constructor when
     * you want to lazily add the page.
     *
     * @param text the text to display on the button
     * @param url  the HTTP url to create a link to
     * @param t    the transition type to use
     */
    public JQMButton(String text, String url, final Transition t) {
        this(text);
        if (url != null)
            setHref(url);
        if (t != null)
            withTransition(t);
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
        Element e = getElement();
        while (e.getFirstChildElement() != null) {
            e = e.getFirstChildElement();
        }
        return e.getInnerText();
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

    public String getHref() {
        return getAttribute("href");
    }

    public void setHref(String url) {
         setAttribute("href", url);
    }

    public JQMButton withHref(String url) {
        setHref(url);
        return this;
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
    public JQMButton setBack(boolean back) {
        if (back)
            getElement().setAttribute("data-rel", "back");
        else
            getElement().removeAttribute("data-rel");
        return this;
    }

    @Override
    public void setCorners(boolean corners) {
        setAttribute("data-corners", String.valueOf(corners));
    }

    @Override
    public JQMButton withCorners(boolean corners) {
        setCorners(corners);
        return this;
    }

    /**
     * Sets this buttom to be a dialog button. This changes the look and feel
     * of the page that is loaded as a consequence of clicking on this button.
     */
    public JQMButton setDialog(boolean dialog) {
        if (dialog)
            setAttribute("data-rel", "dialog");
        else
            removeAttribute("data-rel");
        return this;
    }

    /**
     * Short cut for withRel("external");
     */
    public JQMButton setExternal() {
        withRel("external");
        return this;
    }

    /**
     * Sets the icon used by this button. See {@link DataIcon}.
     */
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

    /**
     * Sets the icon used by this button. See {@link DataIcon}.
     */
    @Override
    public JQMButton withBuiltInIcon(DataIcon icon) {
        setBuiltInIcon(icon);
        return this;
    }

    @Override
    public JQMButton withIconURL(String src) {
        setIconURL(src);
        return this;
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
    public JQMButton withIconPos(IconPos pos) {
        setIconPos(pos);
        return this;
    }

    /**
     * Applies the drop shadow style to the select button if set to true.
     */
    @Override
    public void setIconShadow(boolean shadow) {
        setAttribute("data-iconshadow", String.valueOf(shadow));
    }

    /**
     * Applies the drop shadow style to the select button if set to true.
     */
    @Override
    public JQMButton withIconShadow(boolean shadow) {
        setIconShadow(shadow);
        return this;
    }


    /**
     * Sets this button to be inline.
     * <p/>
     * NOTE: If this button is inside a {@link JQMButtonGroup} then you must
     * call withInline(boolean) on the button group itself and not each button
     * individually.
     *
     * @param inline true to change to line or false to switch to full width
     */
    @Override
    public void setInline(boolean inline) {
        if (inline)
            setAttribute("data-inline", "true");
        else
            removeAttribute("data-inline");
    }

    /**
     * Sets this button to be inline.
     * <p/>
     * NOTE: If this button is inside a {@link JQMButtonGroup} then you must
     * call withInline(boolean) on the button group itself and not each button
     * individually.
     *
     * @param inline true to change to line or false to switch to full width
     */
    @Override
    public JQMButton withInline(boolean inline) {
        setInline(inline);
        return this;
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public void setMini(boolean mini) {
        setAttribute("data-mini", String.valueOf(mini));
    }

    /**
     * If set to true then renders a smaller version of the standard-sized element.
     */
    @Override
    public JQMButton withMini(boolean mini) {
        setMini(mini);
        return this;
    }

    @Override
    public void setRel(String rel) {
        if (rel == null)
            getElement().removeAttribute("data-rel");
        else
            getElement().setAttribute("data-rel", rel);
    }

    @Override
    public JQMButton withRel(String rel) {
        setRel(rel);
        return this;
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

    @Override
    public JQMButton withText(String text) {
        setText(text);
        return this;
    }

    /**
     * Sets the transition to be used by this button when loading the URL.
     */
    @Override
    public void setTransition(Transition transition) {
        if (transition != null)
            setAttribute("data-transition", transition.getJQMValue());
        else
            removeAttribute("data-transition");
    }

    /**
     * Sets the transition to be used by this button when loading the URL.
     */
    @Override
    public JQMButton withTransition(Transition transition) {
        setTransition(transition);
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
