package com.sksamuel.jqm4gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Stephen K Samuel samspade79@gmail.com 17 Jul 2011 17:20:38
 * 
 */
public interface ImageResources extends ClientBundle {

	public static ImageResources	INSTANCE	= GWT.create(ImageResources.class);

	@Source("ajax-loader.gif")
	ImageResource ajaxLoader();

}
