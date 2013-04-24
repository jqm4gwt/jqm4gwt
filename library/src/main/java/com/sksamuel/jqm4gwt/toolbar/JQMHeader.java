package com.sksamuel.jqm4gwt.toolbar;

import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 21:21:13
 * 
 *         This class models a Jquery Mobile header element. It can contain
 *         text, and two optional buttons, one left and one right.
 * 
 * @link 
 *       http://jquerymobile.com/demos/1.2.0/docs/toolbars/docs-headers.html
 * 
 */
public class JQMHeader extends JQMToolbar {

	/**
	 * Left button, keep reference so we can replace
	 */
	private JQMButton	left;

	/**
	 * Right button, keep reference so we can replace
	 */
	private JQMButton	right;

    /**
   	 * Creates a new {@link JQMHeader} with no text and no automatic
   	 * back button.
   	 */
   	public JQMHeader() {
   		this(null, false);
   	}

	/**
	 * Create a new {@link JQMHeader} with the given text and no automatic
	 * back button.
	 */
	public JQMHeader(String text) {
		this(text, false);
	}

	/**
	 * Create a new {@link JQMHeader}. If back is true then an automatic back
	 * button will be created.
	 */
	public JQMHeader(String text, boolean back) {
		super("header", "jpm4gwt-header", text);
	}

	/**
	 * Create a new {@link JQMHeader} with the given text and left and right
	 * buttons
	 */
	public JQMHeader(String text, JQMButton left, JQMButton right) {
		this(text, false);
		setLeftButton(left);
		setRightButton(right);
	}

	/**
	 * Internal method for creating a button
	 */
	protected JQMButton createButton(String text, String url, DataIcon icon) {
		JQMButton button = new JQMButton(text, url);
		if (icon != null)
			button.withBuiltInIcon(icon);
		return button;
	}

	public JQMButton getLeft() {
		return left;
	}

	public JQMButton getRight() {
		return right;
	}

	/**
	 * Sets the given button in the left position and sets that buttons role
	 * to back. Any existing left button will be replaced.
	 * 
	 * @param button
	 *              the new back button
	 */
	public void setBackButton(JQMButton button) {
		button.withRel("back");
		setLeftButton(button);
	}

	/**
	 * Creates an auto back button with the given text and replaces the
	 * current left button, if any.
	 * 
	 * @param text
	 *              the the text for the button
	 * 
	 * @return the created {@link JQMButton}
	 */
	public JQMButton setBackButton(String text) {
		JQMButton button = new JQMButton(text);
		button.withBuiltInIcon(DataIcon.LEFT);
		button.setBack(true);
		setBackButton(button);
		return button;
	}

	/**
	 * Creates a new back button which replaces the current left button, if
	 * any.
	 * 
	 * @return
	 * 
	 * @return the created {@link JQMButton}
	 */
	public JQMButton setBackButton(String text, JQMPage parent) {
		return setBackButton(text);
	}

	/**
	 * Sets the left button to be the given button. Any existing button is
	 * removed.
	 * 
	 * @param button
	 *              the button to set on the left slot
	 */
	public void setLeftButton(JQMButton button) {
		if (left != null)
			remove(left);
		button.setStyleName("ui-btn-left");

		left = button;
		insert(left, 0);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and then sets that
	 * button in the left slot. Any existing right button will be replaced.
	 * 
	 * This button will not link to a page by default and therefore will only
	 * react if a click handler is registered. This is useful if you want the
	 * button to do something other than navigate.
	 * 
	 * @param text
	 *              the text for the button
	 * 
	 * @return the created button
	 */
	public JQMButton setLeftButton(String text) {
		return setLeftButton(text, (String) null, (DataIcon) null);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and using the given
	 * icon and then sets that button in the left slot. Any existing right
	 * button will be replaced.
	 * 
	 * This button will not link to a page by default and therefore will only
	 * react if a click handler is registered. This is useful if you want the
	 * button to do something other than navigate.
	 * 
	 * @param text
	 *              the text for the button
	 * @param icon
	 *              the icon to use or null if no icon is required
	 * 
	 * @return the created button
	 */
	public JQMButton setLeftButton(String text, DataIcon icon) {
		return setLeftButton(text, (String) null, icon);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given {@link JQMPage} and then sets that button in the left slot. Any
	 * existing right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param page
	 *              the optional page for the button to link to, if null then
	 *              this button does not navigate by default
	 * 
	 * @return the created button
	 */
	public JQMButton setLeftButton(String text, JQMPage page) {
		return setLeftButton(text, page, null);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given {@link JQMPage} and with the given icon and then sets that button
	 * in the left slot. Any existing right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param page
	 *              the optional page for the button to link to, if null then
	 *              this button does not navigate by default
	 * @param icon
	 *              the icon to use or null if no icon is required
	 * 
	 * @return the created button
	 */
	public JQMButton setLeftButton(String text, JQMPage page, DataIcon icon) {
		if (page == null)
			throw new RuntimeException("page cannot be null");
		return setLeftButton(text, "#" + page.getId(), icon);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given url and then sets that button in the left slot. Any existing
	 * right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param url
	 *              the optional url for the button, if null then this button
	 *              does not navigate by default
	 * 
	 * @return the created button
	 */
	public JQMButton setLeftButton(String text, String url) {
		return setLeftButton(text, url, null);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given url and with the given icon and then sets that button in the left
	 * slot. Any existing right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param url
	 *              the optional url for the button, if null then this button
	 *              does not navigate by default
	 * @param icon
	 *              the icon to use or null if no icon is required
	 * 
	 * @return the created button
	 */
	public JQMButton setLeftButton(String text, String url, DataIcon icon) {
		JQMButton button = createButton(text, url, icon);
		setLeftButton(button);
		return button;
	}

	/**
	 * Sets the right button to be the given button. Any existing button is
	 * removed.
	 * 
	 * @param button
	 *              the button to set on the right slot
	 */
	public void setRightButton(JQMButton button) {
		if (right != null)
			remove(right);
		button.setStyleName("ui-btn-right");

		right = button;
		add(right);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and then sets that
	 * button in the right slot. Any existing right button will be replaced.
	 * 
	 * This button will not link to a page by default and therefore will only
	 * react if a click handler is registered. This is useful if you want the
	 * button to do something other than navigate.
	 * 
	 * @param text
	 *              the text for the button
	 * 
	 * @return the created button
	 */
	public JQMButton setRightButton(String text) {
		return setRightButton(text, (String) null, (DataIcon) null);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and using the given
	 * icon and then sets that button in the right slot. Any existing right
	 * button will be replaced.
	 * 
	 * This button will not link to a page by default and therefore will only
	 * react if a click handler is registered. This is useful if you want the
	 * button to do something other than navigate.
	 * 
	 * @param text
	 *              the text for the button
	 * @param icon
	 *              the icon to use or null if no icon is required
	 * 
	 * @return the created button
	 */
	public JQMButton setRightButton(String text, DataIcon icon) {
		return setRightButton(text, (String) null, icon);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given {@link JQMPage} and then sets that button in the right slot. Any
	 * existing right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param page
	 *              the optional page for the button to link to, if null then
	 *              this button does not navigate by default
	 * 
	 * @return the created button
	 */
	public JQMButton setRightButton(String text, JQMPage page) {
		return setRightButton(text, page, null);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given {@link JQMPage} and with the given icon and then sets that button
	 * in the right slot. Any existing right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param page
	 *              the optional page for the button to link to, if null then
	 *              this button does not navigate by default
	 * @param icon
	 *              the icon to use or null if no icon is required
	 * 
	 * @return the created button
	 */
	public JQMButton setRightButton(String text, JQMPage page, DataIcon icon) {
		return setRightButton(text, "#" + page.getId(), icon);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given url and then sets that button in the right slot. Any existing
	 * right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param url
	 *              the optional url for the button, if null then this button
	 *              does not navigate by default
	 * 
	 * @return the created button
	 */
	public JQMButton setRightButton(String text, String url) {
		return setRightButton(text, url, null);
	}

	/**
	 * Creates a new {@link JQMButton} with the given text and linking to the
	 * given url and with the given icon and then sets that button in the
	 * right slot. Any existing right button will be replaced.
	 * 
	 * @param text
	 *              the text for the button
	 * @param url
	 *              the optional url for the button, if null then this button
	 *              does not navigate by default
	 * @param icon
	 *              the icon to use or null if no icon is required
	 * 
	 * @return the created button
	 */
	public JQMButton setRightButton(String text, String url, DataIcon icon) {
		JQMButton button = createButton(text, url, icon);
		setRightButton(button);
		return button;
	}
}
