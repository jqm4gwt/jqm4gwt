package com.sksamuel.jqm4gwt.list;

import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasText;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 14:36:20
 * 
 *         An implementation of a list divider.
 * 
 */
public class JQMListDivider extends Widget implements HasText<JQMListDivider> {

	public static final String ATTR_NAME = "data-role";
	public static final String ATTR_VALUE = "list-divider";
    
    private Object tag;
    
    @UiConstructor
    public JQMListDivider(String text) {
		setElement(Document.get().createLIElement());
		getElement().setAttribute(ATTR_NAME, ATTR_VALUE);
		getElement().setId(Document.get().createUniqueId());
		setText(text);
	}

	/**
	 * Returns the text currently set on the divider.
	 * 
	 * @return the divider text
	 */
	@Override
	public String getText() {
		return getElement().getInnerText();
	}

	/**
	 * Sets the text of this divider. Changes by this method will create
	 * through to the underlying list.
	 */
	@Override
	public void setText(String text) {
		getElement().setInnerText(text);
	}

    @Override
    public JQMListDivider withText(String text) {
        setText(text);
        return this;
    }
    
    public Object getTag() {
        return tag;
    }
    
    /**
     * Additional information can be attached to divider (for example linked JQMListItem).
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }
}
