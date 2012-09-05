package com.sksamuel.jqm4gwt;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Stephen K Samuel samspade79@gmail.com 17 Jul 2011 17:22:17
 * 
 */
public class LoadingIndicator extends Composite {

	public LoadingIndicator() {
		Image image = new Image(ImageResources.INSTANCE.ajaxLoader());
		initWidget(image);
	}

}
