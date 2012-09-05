package com.sksamuel.jqm4gwt.list;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 11:21:29
 * 
 */
public class JQMListItem extends Widget implements HasText, HasClickHandlers {

	/**
	 * Element to hold the count bubble
	 */
	private Element	countElem;

	/**
	 * Element to hold the image (thumbnail or icon)
	 */
	private Element	imageElem;

	/**
	 * The element that contains the link, if any
	 */
	private Element	anchor;

	/**
	 * The element that holds the aside content
	 */
	private Element	asideElem;

	/**
	 * The element that holds the content of the "main" text
	 */
	private Element	headerElem;

	private JQMList	list;

	/**
	 * Create a read only {@link JQMList} with the initial content set to the
	 * value of the text
	 */
	JQMListItem(String text) {
		if (text == null)
			throw new RuntimeException("Cannot create list item with null text");

		setElement(Document.get().createLIElement());
		setStyleName("jqm4gwt-listitem");
		setText(text);
		setId();
		bind(getElement().getId(), this);
	}

	/**
	 * Create a linked {@link JQMList} with the inital content set to the
	 * value of the @param text and the link set to the value of the @param
	 * url
	 * 
	 * @param text
	 * @param url
	 */
	JQMListItem(String text, String url) {
		this(text);
		if (url != null)
			setUrl(url);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	/**
	 * Adds a header element containing the given text.
	 * 
	 * @param n
	 *              the Hn element to use, eg if n is 2 then a <h2>element is
	 *              created
	 * 
	 * @param html
	 *              the value to set as the inner html of the <hn> element
	 */
	public void addHeaderText(int n, String html) {
		Element e = Document.get().createHElement(n);
		e.setInnerHTML(html);
		attachChild(e);
	}

	/**
	 * Adds a paragraph element containing the given text
	 * 
	 * @param html
	 *              the value to set as the inner html of the p element
	 */
	public void addText(String html) {
		Element e = Document.get().createPElement();
		e.setInnerHTML(html);
		attachChild(e);
	}

	private void attachChild(Element elem) {
		if (anchor == null)
			getElement().appendChild(elem);
		else
			anchor.appendChild(elem);
	}

	private native void bind(String id, JQMListItem item) /*-{
										$wnd.$("#"+id).live("tap", function(event) { item.@com.sksamuel.jqm4gwt.list.JQMListItem::onTap()(); });
										}-*/;

	private void createAndAttachAsideElem() {
		asideElem = Document.get().createPElement();
		asideElem.setClassName("ui-li-aside");
		attachChild(asideElem);
	}

	private void createAndAttachCountElement() {
		countElem = Document.get().createSpanElement();
		countElem.setClassName("ui-li-count");
		attachChild(countElem);
	}

	/**
	 * Returns the value of the count bubble or null if no count has been set
	 */
	public Integer getCount() {
		if (countElem == null)
			return null;
		return Integer.parseInt(countElem.getInnerText());
	}

	/**
	 * Returns the value of the "main" text element
	 */
	@Override
	public String getText() {
		return headerElem.getInnerText();
	}

	private void moveAnchorChildrenToThis() {
		for (int k = 0; k < anchor.getChildCount(); k++) {
			Node node = anchor.getChild(k);
			anchor.removeChild(node);
			getElement().appendChild(node);
		}
	}

	private void moveThisChildrenToAnchor() {
		for (int k = 0; k < getElement().getChildCount(); k++) {
			Node node = getElement().getChild(k);
			getElement().removeChild(node);
			anchor.appendChild(node);
		}
	}

	protected void onTap() {
		list.setClickItem(this);
	}

	/**
	 * Removes the value of the aside element, if any. It is safe to call this
	 * method regardless of if an aside has been set or not.
	 */
	public void removeAside() {
		if (asideElem != null) {
			getElement().removeChild(asideElem);
			asideElem = null;
		}
	}

	/**
	 * Removes the value of the count element if any. It is safe to call this
	 * method regardless of if a count has been set or not.
	 */
	public void removeCount() {
		if (countElem != null) {
			getElement().removeChild(countElem);
			countElem = null;
		}
	}

	/**
	 * Removes the value of the image element if any. It is safe to call this
	 * method regardless of if an image has been set or not.
	 */
	public void removeImage() {
		if (imageElem != null) {
			getElement().removeChild(imageElem);
			imageElem = null;
		}
	}

	/**
	 * Remove the url from this list item changing the item into a read only
	 * item.
	 */
	public void removeUrl() {
		if (anchor == null)
			return;
		moveAnchorChildrenToThis();
		getElement().removeChild(anchor);
		anchor = null;
	}

	/**
	 * Sets the content of the aside. The aside is supplemental content that
	 * is positioned to the right of the main content.
	 */
	public void setAside(String text) {
		if (text == null)
			throw new RuntimeException(
					"Cannot set aside to null. Call removeAside() if you wanted to remove the aside text");

		if (asideElem == null)
			createAndAttachAsideElem();
		asideElem.setInnerText(text);
	}

	/**
	 * Set the count bubble value. If null this will throw a runtime
	 * exception. To remove a count bubble call removeCount()
	 */
	public JQMListItem setCount(Integer count) {
		if (count == null)
			throw new RuntimeException(
					"Cannot set count to null. Call removeCount() if you wanted to remove the bubble");

		if (countElem == null)
			createAndAttachCountElement();
		countElem.setInnerText(count.toString());

		return this;
	}

	/**
	 * Sets the image to be used to the given src. The image will be set as an
	 * icon.
	 */
	public void setIcon(String src) {
		setImage(src, true);
	}

	private void setId() {
		getElement().setId(Document.get().createUniqueId());
	}

	/**
	 * Sets the image on this list item to the image at the given src. If icon
	 * is true then the image will be set as an icon, otherwise it will be set
	 * as a thumbnail.
	 */
	public void setImage(String src, boolean icon) {
		if (src == null)
			throw new RuntimeException(
					"Cannot set image to null. Call removeImage() if you wanted to remove the image");

		if (imageElem == null) {
			imageElem = Document.get().createImageElement();
			attachChild(imageElem);
		}
		imageElem.setAttribute("src", src);

		if (icon)
			imageElem.setAttribute("class", "ui-li-icon");
		else
			imageElem.removeAttribute("class");
	}

	public void setList(JQMList jqmList) {
		this.list = jqmList;
	}

	/**
	 * Sets the content of the "main" text to the given value.
	 */
	@Override
	public void setText(String text) {
		if (headerElem == null) {
			headerElem = Document.get().createHElement(3);
			attachChild(headerElem);
		}
		headerElem.setInnerText(text);
	}

	/**
	 * Sets the image element to type thumbnail and uses the @param src for
	 * the source url of the image
	 */
	public void setThumbnail(String src) {
		setImage(src, false);
	}

	/**
	 * Sets the url to link to for this item. If this item was a read only
	 * item it automatically becomes linkable.
	 */
	public void setUrl(String url) {
		if (url == null)
			throw new RuntimeException("Cannot set URL to null. Call removeUrl() if you wanted to remove the URL");

		if (anchor == null) {
			// need to make anchor and move children to it
			anchor = Document.get().createAnchorElement();
			moveThisChildrenToAnchor();
			getElement().appendChild(anchor);
		}
		anchor.setAttribute("href", url);

	}

}
