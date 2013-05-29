package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 13 May 2011 11:14:24
 * 
 *         Utility methods. The static methods in this class map through to the
 *         equivilent JQM method in $.mobile
 * 
 */
public class Mobile {

	/**
	 * Invokes the $.mobile.changePage method
	 */
	private static native void changePage(String url, String t, boolean r, boolean ch) /*-{
															$wnd.$.mobile.changePage(url, { transition: t, reverse: r, changeHash : ch } );
															}-*/;

	/**
	 * Invokes the $.mobile.changePage method
	 */
	static void changePage(String url, Transition t, boolean reverse, boolean changeHash) {
		changePage(url, t.getJQMValue(), reverse, changeHash);
	}

	/**
	 * Hide the page loading dialog.
	 */
	public static native void hideLoadingDialog() /*-{
											$wnd.$.mobile.loading('hide');
											}-*/;

	/**
	 * Ask JQuery Mobile to "render" the element with the given id.
	 */
	public static void render(String id)
	{
		JQMContext.render(id);
	}

	/**
	 * 
	 */
	public static native void showLoadingDialog(String msg) /*-{
											$wnd.$.mobile.loading('show', {text: msg, textVisible: true});
											}-*/;

	/**
	 * Scroll to a particular Y position without triggering scroll event
	 * listeners.
	 * 
	 * @param y
	 *              Pass any number to scroll to that Y location.
	 */
	public static native void silentScroll(int y) /*-{
									$wnd.$.mobile.silentScroll(y);
									}-*/;
}
