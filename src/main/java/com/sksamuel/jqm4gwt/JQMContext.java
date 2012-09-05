package com.sksamuel.jqm4gwt;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 12:57:43
 * 
 *         The {@link JQMContent} provides methods that facilitate interaction
 *         between GWT, JQM and the DOM.
 * 
 */
public class JQMContext {

	/**
	 * Appends the given {@link JQMPage} to the DOM so that JQueryMobile is
	 * able to manipulate it and render it. This should only be done once per
	 * page, otherwise duplicate HTML would be added to the DOM and this would
	 * result in elements with overlapping IDs.
	 * 
	 */
	static void appendPage(JQMPage page) {
		RootPanel.get().add(page);
		render(page.getId());
	}

	/**
	 * Programatically change the displayed page to the given {@link JQMPage}
	 * instance. This uses the default transition which is Transition.POP
	 */
	public static void changePage(JQMPage page) {
		changePage(page, Transition.POP);
	}

	public native static void initializePage() /*-{
									$wnd.$.mobile.initializePage();
									}-*/;

	/**
	 * Change the displayed page to the given {@link JQMPage} instance using
	 * the supplied transition.
	 */
	public static void changePage(JQMPage page, Transition transition) {
		Mobile.changePage("#" + page.getId(), transition, false, true);
	}

	/**
	 * Return the pixel offset of an element from the left of the document.
	 * This can also be thought of as the y coordinate of the top of the
	 * elements bounding box.
	 * 
	 * @param id
	 *              the id of the element to find the offset
	 */
	public native static int getLeft(String id) /*-{
									return $wnd.$("#" + id).offset().left;
									}-*/;

	/**
	 * Return the pixel offset of an element from the top of the document.
	 * This can also be thought of as the x coordinate of the left of the
	 * elements bounding box.
	 * 
	 * @param id
	 *              the id of the element to find the offset
	 */
	public native static int getTop(String id) /*-{
									return $wnd.$("#" + id).offset().top;
									}-*/;

	/**
	 * Ask JQuery Mobile to "render" the element with the given id.
	 */
	public static void render(String id)
	{
		if (id == null || "".equals(id)) throw new IllegalArgumentException("render for empty id not possible");
		renderImpl(id);
	}
	private static native void renderImpl(String id) /*-{
									$wnd.$("#" + id).page();
									}-*/;

	/**
	 * Scroll the page to the y-position of the given element. The element
	 * must be attached to the DOM (obviously!).
	 * 
	 * This method will not fire jquery mobile scroll events.
	 */
	public static void silentScroll(Element e) {
		if (e.getId() != null)
			Mobile.silentScroll(getTop(e.getId()));
	}

	/**
	 * Scroll the page to the y-position of the given widget. The widget must
	 * be attached to the DOM (obviously!)
	 * 
	 * This method will not fire jquery mobile scroll events.
	 */
	public static void silentScroll(Widget widget) {
		silentScroll(widget.getElement());
	}
}
