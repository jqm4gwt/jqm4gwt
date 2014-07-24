package com.sksamuel.jqm4gwt.list;

import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasHTML;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 Jul 2011 14:36:20
 *
 *         An implementation of a list divider.
 *
 */
public class JQMListDivider extends CustomFlowPanel implements HasText<JQMListDivider>,
        HasHTML<JQMListDivider> {

	public static final String ATTR_NAME = "data-role";
	public static final String ATTR_VALUE = "list-divider";

    private Object tag;
    private JQMList list;

    public JQMListDivider() {
        super(Document.get().createLIElement());
        getElement().setAttribute(ATTR_NAME, ATTR_VALUE);
        getElement().setId(Document.get().createUniqueId());
    }

    public JQMListDivider(String text) {
		this();
		setText(text);
	}

    public String getId() {
        return getElement().getId();
    }

    @UiChild(tagname="widget")
    public void addWidget(Widget w) {
        if (w == null) return;
        add(w);
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

    @Override
    public String getHTML() {
        return getElement().getInnerHTML();
    }

    @Override
    public void setHTML(String html) {
        getElement().setInnerHTML(html);
    }

    @Override
    public JQMListDivider withHTML(String html) {
        setHTML(html);
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

    public String getTagStr() {
        return tag != null ? tag.toString() : null;
    }

    public void setTagStr(String value) {
        setTag(value);
    }

    public JQMList getList() {
        return list;
    }

    protected JQMListDivider setList(JQMList jqmList) {
        this.list = jqmList;
        return this;
    }

}
