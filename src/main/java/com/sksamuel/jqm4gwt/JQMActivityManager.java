package com.sksamuel.jqm4gwt;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 6 Sep 2012 01:25:22
 * 
 * When using the {@link JQMActivityManager} you must disable jQuery Mobile hash listening, 
 * otherwise jQuery Mobile will intercept hash changes, and so they will not propgate to the GWT activity manager.
 * 
 * Add this override to your HTML page. 
 * 
 * 	$(document).bind("mobileinit", function(){
 * 		$.mobile.hashListeningEnabled = false;
 * 	});
 *
 */
public class JQMActivityManager extends ActivityManager {

	class JQMAwareDisplay implements AcceptsOneWidget {

		@Override
		public void setWidget(IsWidget w) {
			JQMPage page = (JQMPage) w;
			JQMContext.changePage(page);
		}
	}

	private static native void disableHashListening() /*-{
										$wnd.$.mobile.hashListeningEnabled = false;
										}-*/;

	public JQMActivityManager(ActivityMapper mapper, EventBus eventBus) {
		super(mapper, eventBus);
		setDisplay(new JQMAwareDisplay());
		disableHashListening();
	}
}
