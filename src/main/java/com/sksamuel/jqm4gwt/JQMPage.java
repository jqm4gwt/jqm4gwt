package com.sksamuel.jqm4gwt;

import java.util.Collection;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 23:55:27
 * 
 *         A {@link JQMPage} is the base container for a single page. Any JQM
 *         widgets can be added to the page. You can consider a JQMPage as like
 *         a GWT "view" in the MVP paradigm.
 * 
 *         A {@link JQMPage} is also a {@link JQMWidget} and thus all the usual
 *         methods are available.
 * 
 */
public class JQMPage extends ComplexPanel implements HasFullScreen, HasTheme {

	static int	counter	= 1;

	public static native void triggerCreate() /*-{
										$wnd.$('body').trigger('create');
										}-*/;

	/**
	 * id of the page
	 */
	private final String		id;

	/**
	 * The primary content div
	 */
	private final JQMContent	content;

	public boolean			firstShow	= false;

	/**
	 * Set to true once the page has been enhanced by jQuery Mobile.
	 */
	private boolean			enhanced;

	protected JQMHeader		header;

	protected JQMFooter		footer;

	/**
	 * Create a new {@link JQMPage} with an automatically assigned page id.
	 * 
	 */
	public JQMPage() {
		this("page" + (counter++));
	}

	/**
	 * Create a new {@link JQMPage} with an automatically assigned page id,
	 * and then add the given widgets serially to the page layout.
	 */
	public JQMPage(Collection<Widget> widgets) {
		this();
		if (widgets != null)
			add(widgets);
	}

	/**
	 * Creates a {@link JQMPage} with the given id
	 * 
	 * @param id
	 *              the id to use as this page's id
	 */
	public JQMPage(String id) {
		if (id == null)
			throw new RuntimeException("id for page cannot be null");
		if (id.contains(" "))
			throw new RuntimeException("id for page cannot contain space");
		this.id = id;

		setElement(Document.get().createDivElement());
		setStyleName("jqm4gwt-page");
		setDataRole("page");
		setAttribute("data-url", id);

		// create the primary content div and add this to the super class
		// containing widget
		content = createContent();

		JQMContext.appendPage(this);
		bindLifecycleEvents(this, getId());
	}

	/**
	 * Create a new {@link JQMPage} with an automatically assigned page id,
	 * and then add the given widgets serially to the page layout.
	 */
	public JQMPage(Widget... widgets) {
		this();
		if (widgets != null)
			add(widgets);
	}

	/**
	 * Adds the given collection of widgets to the primary content panel
	 */
	public void add(Collection<Widget> widgets) {
		for (Widget widget : widgets)
			add(widget);
	}

	/**
	 * Sets the footer of the page. Alias for setFooter(). Any existing footer
	 * will be replaced.
	 */
	public void add(JQMFooter footer) {
		setFooter(footer);
	}

	/**
	 * Sets the header of the page. Alias for setHeader(). Any existing header
	 * will be replaced.
	 */
	public void add(JQMHeader header) {
		setHeader(header);
	}

	/**
	 * Adds a widget to the primary content container of this page.
	 * 
	 * @param widget
	 *              the widget to add to the primary content
	 */
	@Override
	public void add(Widget widget) {
		if (widget instanceof JQMContent)
			throw new RuntimeException("Do not add content widgets here, call createContent instead");
		getPrimaryContent().add(widget);
		// if page is already enhanced then we need to enhance the content manually
		// if (enhanced)
		// triggerCreate();
	}

	@Override
	public boolean remove(Widget w) {
		return getPrimaryContent().remove(w);
	}

	@Override
	public boolean remove(int index) {
		return getPrimaryContent().remove(index);
	}

	/**
	 * Adds the given array of widgets to the primary content container of
	 * this page.
	 * 
	 * @param widgets
	 *              the widgets to add to the primary content
	 */
	public void add(Widget[] widgets) {
		for (Widget widget : widgets)
			add(widget);
	}

	private native void bindLifecycleEvents(JQMPage p, String id) /*-{
												
												$wnd.$('div[data-url="' + id + '"]').bind("pageshow",
												function(event, ui) {
												p.@com.sksamuel.jqm4gwt.JQMPage::onPageShow()();
												});
												
												$wnd.$('div[data-url="' + id + '"]').bind("pagehide",
												function(event, ui) {
												p.@com.sksamuel.jqm4gwt.JQMPage::onPageHide()();
												});
												
																								$wnd.$('div[data-url="' + id + '"]').bind("pagebeforehide",
												function(event, ui) {
												p.@com.sksamuel.jqm4gwt.JQMPage::onPageBeforeHide()();
												});
												
																								$wnd.$('div[data-url="' + id + '"]').bind("pagebeforeshow",
												function(event, ui) {
												p.@com.sksamuel.jqm4gwt.JQMPage::onPageBeforeShow()();
												});
												
												}-*/;

	@Override
	public void clear() {
		throw new RuntimeException("You called clear on the page, you probably wanted to call clear on a content panel");
	}

	/**
	 * Creates a content container on this page and returns it. Content can
	 * then be added to this secondary container. There is no limit to the
	 * number of secondary content containers that can be created.
	 */
	public JQMContent createContent() {
		JQMContent content = new JQMContent();
		add(content, getElement());
		return content;
	}

	/**
	 * Returns the value of the attribute with the given name
	 */
	protected String getAttribute(String name) {
		return getElement().getAttribute(name);
	}

	public String getCookie(String name) {
		return Cookies.getCookie(name);
	}

	protected int getCookieInteger(String value) {
		return Integer.parseInt(value);
	}

	public String getDataUrl() {
		return getAttribute("data-url");
	}

	public String getId() {
		return id;
	}

	public String getParameter(String name) {
		return Window.Location.getParameter(name);
	}

	/**
	 * Returns the primary content container
	 * 
	 * @returns the primary content container
	 */
	public JQMContent getPrimaryContent() {
		return content;
	}

	@Override
	public String getTheme() {
		return getAttribute("data-theme");
	}

	private Element getToolBar(String role) {
		Element element = getElement().getFirstChildElement();
		while (element != null) {
			if (role.equals(element.getAttribute("data-role"))) {
				return element;
			}
			element = element.getNextSiblingElement();
		}
		return null;
	}

	public boolean hasCookie(String name) {
		return getCookie(name) != null;
	}

	/**
	 * 
	 */
	protected boolean hasParameter(String name) {
		return getParameter(name) != null;
	}

	/**
	 * Returns true if this page has an auto generated back button
	 */
	public boolean isBackButton() {
		return "true".equals(getAttribute("data-add-back-btn"));
	}

	public boolean isEnhanced() {
		return enhanced;
	}

	@Override
	public boolean isFullScreen() {
		return "true".equals(getAttribute("data-fullscreen"));
	}

	/**
	 * Triggered on the page being hidden, before its transition begins.
	 */
	protected void onPageBeforeHide() {
	}

	/**
	 * Triggered on the page being shown, before its transition begins.
	 */
	protected void onPageBeforeShow() {
	}

	/**
	 * Triggered on the page being hidden, after its transition completes.
	 */
	protected void onPageHide() {

	}

	/**
	 * Triggered on the page being shown, after its transition completes.
	 */
	protected void onPageShow() {
		// workarround clickhandler removal by jquery
		if (!firstShow) {
			firstShow = true;
			if (header != null)
				header.doFixHandlers();
		}
	}

	/**
	 * Removes the attribute with the given name
	 * 
	 * @param name
	 *              the name of the attribute to remove
	 */
	protected void removeAttribute(String name) {
		getElement().removeAttribute(name);
	}

	/**
	 * Removes the footer element set on this page. If no footer is set then
	 * this has no effect.
	 */
	public void removeFooter() {
		footer = null;
		Element element = getToolBar("footer");
		if (element != null)
			getElement().removeChild(element);
	}

	/**
	 * Removes the header element set on this page. If no header is set then
	 * this has no effect.
	 */
	public void removeHeader() {
		header = null;
		Element element = getToolBar("header");
		if (element != null)
			getElement().removeChild(element);
	}

	/**
	 * Sets the value of the attribute with the given name to the given value.
	 * 
	 */
	protected void setAttribute(String name, String value) {
		getElement().setAttribute(name, value);
	}

	/**
	 * Sets whether or not this page should have an auto generated back
	 * button. If so, it will be placed in the left slot and override any left
	 * button already there.
	 * 
	 * If you want a back button in the right, then programatically create a
	 * button, set it to back using setBack(), and call setRightButton() with
	 * the button as the param.
	 */
	public void setBackButton(boolean backButton) {
		if (backButton) {
			getElement().setAttribute("data-add-back-btn", "true");
		} else {
			getElement().removeAttribute("data-add-back-btn");
		}
	}

	/**
	 * Sets the data-role attribute to the given value.
	 * 
	 * @param value
	 *              the value to set the data-role attribute to
	 */
	protected void setDataRole(String value) {
		setAttribute("data-role", value);
	}

	public void setEnhanced(boolean enchanced) {
		this.enhanced = enchanced;
	}

	/**
	 * Sets the footer element, overriding an existing footer if any.
	 */
	public void setFooter(JQMFooter footer) {
		removeFooter();
		this.footer = footer;
		getElement().appendChild(footer.getElement());
	}

	@Override
	public void setFullScreen(boolean fs) {
		if (fs) {
			setAttribute("data-fullscreen", "true");
		} else {
			removeAttribute("data-fullscreen");
		}
	}

	/**
	 * Sets the header element, overriding an existing header if any.
	 */
	public void setHeader(JQMHeader header) {
		removeHeader();
		this.header = header;
		getElement().insertBefore(header.getElement(), getElement().getFirstChild());
	}

	public JQMHeader setHeader(String text) {
		JQMHeader header = new JQMHeader(text);
		setHeader(header);
		return header;
	}

	@Override
	public void setTheme(String themeName) {
		setAttribute("data-theme", themeName);
	}

	/**
	 * Sets the title of this page, which will be used as the contents of the
	 * title tag when this page is the visible page.
	 */
	@Override
	public void setTitle(String title) {
		setAttribute("data-title", title);
	}

	@Override
	public String toString() {
		return "JQMPage [id=" + id + "]";
	}

}
