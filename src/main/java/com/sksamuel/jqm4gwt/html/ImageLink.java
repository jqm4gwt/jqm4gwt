package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 17 Jul 2011 15:38:47
 * 
 *         An implementation of an anchor tag that wraps an image tag. Eg, <a
 *         href="mylink"><img src="myimage"/></a>
 * 
 */
public class ImageLink extends Widget {

	private ImageElement	img;
	private AnchorElement	a;

	public ImageLink(String href, String src) {
		a = Document.get().createAnchorElement();
		a.setAttribute("data-role", "none");
		setElement(a);
		a.setAttribute("href", href);
		a.setAttribute("rel", "external");
		a.setAttribute("data-rel", "external");
		img = Document.get().createImageElement();
		img.setAttribute("src", src);
		a.appendChild(img);
	}

	/**
	 * Set the width of the image
	 */
	public void setWidth(String width) {
		img.setAttribute("width", width);
	}

	/**
	 * Set the height of the image
	 */
	public void setHeight(String height) {
		img.setAttribute("height", height);
	}
}
