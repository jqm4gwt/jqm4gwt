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

	private static Transition	defaultTransition	= Transition.POP;
	private static boolean 		defaultTransistionDirection = false;
	private static boolean		defaultChangeHash = true;

	public static native void disableHashListening() /*-{
										$wnd.$.mobile.hashListeningEnabled = false;
										}-*/;

	/**
	 * Appends the given {@link JQMPage} to the DOM so that JQueryMobile is
	 * able to manipulate it and render it. This should only be done once per
	 * page, otherwise duplicate HTML would be added to the DOM and this would
	 * result in elements with overlapping IDs.
	 * 
	 */
	public static void attachAndEnhance(JQMContainer container) {
		RootPanel.get().add(container);
		enhance(container);
		container.setEnhanced(true);
	}

	/**
	 * Programatically change the displayed page to the given {@link JQMPage}
	 * instance. This uses the default transition which is Transition.POP
	 */
	public static void changePage(JQMContainer container) {
		changePage(container, defaultTransition);
	}

	/**
	 * Change the displayed page to the given {@link JQMPage} instance using
	 * the supplied transition.
	 */
	public static void changePage(JQMContainer container, Transition transition) {
		Mobile.changePage("#" + container.getId(), transition, defaultTransistionDirection, defaultChangeHash);
	}

	private static void enhance(JQMContainer c) {
		render(c.getId());
	}

	public static Transition getDefaultTransition() {
		return defaultTransition;
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

	public native static void initializePage() /*-{
									$wnd.$.mobile.initializePage();
									}-*/;

	/**
	 * Ask JQuery Mobile to "render" the element with the given id.
	 */
	public static void render(String id) {
		if (id == null || "".equals(id))
			throw new IllegalArgumentException("render for empty id not possible");
		renderImpl(id);
	}

	private static native void renderImpl(String id) /*-{
										$wnd.$("#" + id).page();
										}-*/;

	public static void setDefaultTransition(Transition defaultTransition) {
		JQMContext.defaultTransition = defaultTransition;
	}
	public static void setDefaultTransition(Transition defaultTransition, boolean direction) {
		JQMContext.defaultTransition = defaultTransition;
		defaultTransistionDirection = direction;
	}
	
	public static void setDefaultChangeHash(boolean defaultChangeHash) {
		JQMContext.defaultChangeHash = defaultChangeHash;
	}

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
