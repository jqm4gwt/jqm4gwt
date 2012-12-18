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
 * By using this class, the default ajax listening from jquery mobile is disabled, and all hash changes will be intercepted by the GWT MVP history manager. 
 * 
 * 
 *
 */
public class JQMActivityManager extends ActivityManager {

	class JQMAwareDisplay implements AcceptsOneWidget {

		@Override
		public void setWidget(IsWidget w) {
			// instead of setting the widget on the underlying display, we
			// will tell JQM to show it
			JQMPage page = (JQMPage) w;
			if (w != null)
				JQMContext.changePage(page);
		}
	}

	public JQMActivityManager(ActivityMapper mapper, EventBus eventBus) {
		super(mapper, eventBus);
		super.setDisplay(new JQMAwareDisplay());
		// GWT will monitor the hashes for us
		JQMContext.disableHashListening();
		// disable page hash changes
		JQMContext.setDefaultChangeHash(false);
	}

	@Override
	public void setDisplay(AcceptsOneWidget display) {
		throw new RuntimeException(
				"JQMActivityMapper does not support custom displays. All JQM pages must be added to the RootPanel and this will be done automatically");
	}
}
