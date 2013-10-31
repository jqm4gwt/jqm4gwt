package com.sksamuel.jqm4gwt.list;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FieldSetElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.panel.JQMControlGroup;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 11:21:29
 */
public class JQMListItem extends Widget implements HasText<JQMListItem>, HasClickHandlers, HasTapHandlers {

    /**
     * Element to hold the count bubble
     */
    private Element countElem;

    /**
     * Element to hold the image (thumbnail or icon)
     */
    private Element imageElem;

    /**
     * The element that contains the link, if any
     */
    private Element anchor;

    /**
     * The element that holds the aside content
     */
    private Element asideElem;

    /**
     * The element that holds the content of the "main" text
     */
    private Element headerElem;

    private JQMList list;

    public class CheckboxControlGroup extends JQMControlGroup {

        protected CheckboxControlGroup(Element element, String styleName) {
            super(element, styleName);
        }
    }

    private CheckboxControlGroup checkBoxCtrlGrp;
    private Element checkBoxElem;
    private InputElement checkBoxInput;


    /**
     * Create empty {@link JQMListItem}
     */
    @UiConstructor
    public JQMListItem() {
        setElement(Document.get().createLIElement());
        setStyleName("jqm4gwt-listitem");
        setId();
    }

    /**
     * Create {@link JQMListItem} with the initial content set to the value of the text.
     */
    public JQMListItem(String text) {
        this();
        if (text == null) throw new RuntimeException("Cannot create list item with null text");
        setText(text);
    }

    /**
     * Create a linked {@link JQMListItem} with the inital content set to the
     * value of the param text and the link set to the value of the param url.
     */
    public JQMListItem(String text, String url) {
        this(text);
        if (url != null) setUrl(url);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        setUrl("#");
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addTapHandler(TapHandler handler) {
        setUrl("#");
        HandlerRegistration defaultRegistration = addHandler(handler, TapEvent.getType());
        // this is not a native browser event so we will have to manage it via JS
        return new JQMHandlerRegistration(getElement(), JQMComponentEvents.TAP_EVENT, defaultRegistration);
    }

    /**
     * Adds a header element containing the given text.
     *
     * @param n    the Hn element to use, eg if n is 2 then a <h2>element is
     *             created
     * @param html the value to set as the inner html of the <hn> element
     */
    public JQMListItem addHeaderText(int n, String html) {
        Element e = Document.get().createHElement(n);
        e.setInnerHTML(html);
        attachChild(e);
        return this;
    }

    /**
     * Adds a paragraph element containing the given text
     *
     * @param html the value to set as the inner html of the p element
     */
    public JQMListItem addText(String html) {
        Element e = Document.get().createPElement();
        e.setInnerHTML(html);
        attachChild(e);
        return this;
    }

    private void attachChild(Element elem) {
        if (anchor == null) getElement().appendChild(elem);
        else if (checkBoxCtrlGrp != null) checkBoxCtrlGrp.getElement().appendChild(elem);
        else anchor.appendChild(elem);
    }

    private void removeChild(Element elem) {
        if (anchor == null) getElement().removeChild(elem);
        else if (checkBoxCtrlGrp != null) checkBoxCtrlGrp.getElement().removeChild(elem);
        else anchor.removeChild(elem);
    }

    private native void bind(String id, JQMListItem item) /*-{
        $wnd.$(document).on("tap", "#"+id, function(event) { item.@com.sksamuel.jqm4gwt.list.JQMListItem::onTap()(); })
    }-*/;

    private native void unbind(String id) /*-{
        $wnd.$(document).off("tap", "#"+id)
    }-*/;

    @Override
    protected void onLoad()
    {
        bind(getElement().getId(), this);
    }

    @Override
    protected void onUnload()
    {
    	unbind(getElement().getId());
    }

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
        return headerElem != null ? headerElem.getInnerText() : null;
    }

    private void moveAnchorChildrenTo(Element elt) {
        for (int k = 0; k < anchor.getChildCount(); k++) {
            Node node = anchor.getChild(k);
            anchor.removeChild(node);
            elt.appendChild(node);
        }
    }

    private void moveAnchorChildrenToThis() {
        moveAnchorChildrenTo(getElement());
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
    public JQMListItem removeAside() {
        if (asideElem != null) {
            getElement().removeChild(asideElem);
            asideElem = null;
        }
        return this;
    }

    /**
     * Removes the value of the count element if any. It is safe to call this
     * method regardless of if a count has been set or not.
     */
    public JQMListItem removeCount() {
        if (countElem != null) {
            getElement().removeChild(countElem);
            countElem = null;
        }
        return this;
    }

    /**
     * Removes the value of the image element if any. It is safe to call this
     * method regardless of if an image has been set or not.
     */
    public JQMListItem removeImage() {
        if (imageElem != null) {
            getElement().removeChild(imageElem);
            imageElem = null;
        }
        return this;
    }

    /**
     * Remove the url from this list item changing the item into a read only
     * item.
     */
    public JQMListItem removeUrl() {
        if (anchor == null)
            return null;
        moveAnchorChildrenToThis();
        getElement().removeChild(anchor);
        anchor = null;
        return this;
    }

    /**
     * Sets the content of the aside. The aside is supplemental content that
     * is positioned to the right of the main content.
     */
    public JQMListItem setAside(String text) {
        if (text == null)
            throw new RuntimeException("Cannot set aside to null. Call removeAside() if you wanted to remove the aside text");

        if (asideElem == null)
            createAndAttachAsideElem();
        asideElem.setInnerText(text);
        return this;
    }

    /**
     * Set the count bubble value. If null this will throw a runtime
     * exception. To remove a count bubble call removeCount()
     */
    public JQMListItem setCount(Integer count) {
        if (count == null)
            throw new RuntimeException("Cannot set count to null. Call removeCount() if you wanted to remove the bubble");

        if (countElem == null)
            createAndAttachCountElement();
        countElem.setInnerText(count.toString());

        return this;
    }

    /**
     * Sets the image to be used to the given src. The image will be set as an
     * icon.
     */
    public JQMListItem setIcon(String src) {
        setImage(src, true);
        return this;
    }

    private JQMListItem setId() {
        getElement().setId(Document.get().createUniqueId());
        return this;
    }

    /**
     * Sets the image on this list item to the image at the given src. If icon
     * is true then the image will be set as an icon, otherwise it will be set
     * as a thumbnail.
     */
    public JQMListItem setImage(String src, boolean icon) {
        if (src == null)
            throw new RuntimeException("Cannot set image to null. Call removeImage() if you wanted to remove the image");

        if (imageElem == null) {
            imageElem = Document.get().createImageElement();
            attachChild(imageElem);
        }
        imageElem.setAttribute("src", src);

        if (icon)
            imageElem.setAttribute("class", "ui-li-icon");
        else
            imageElem.removeAttribute("class");
        return this;
    }

    protected JQMListItem setList(JQMList jqmList) {
        this.list = jqmList;
        return this;
    }

    /**
     * Sets the content of the "main" text to the given value.
     */
    @Override
    public void setText(String text) {
        if (text == null) {
            if (headerElem != null) {
                removeChild(headerElem);
                headerElem = null;
            }
            return;
        }

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
    public JQMListItem setThumbnail(String src) {
        setImage(src, false);
        return this;
    }

    /**
     * Sets the url to link to for this item. If this item was a read only
     * item it automatically becomes linkable.
     */
    public JQMListItem setUrl(String url) {
        if (url == null)
            throw new RuntimeException("Cannot set URL to null. Call removeUrl() if you wanted to remove the URL");
        if (anchor == null) {
            // need to make anchor and move children to it
            anchor = Document.get().createAnchorElement();
            moveThisChildrenToAnchor();
            getElement().appendChild(anchor);
        }
        anchor.setAttribute("href", url);
        return this;
    }

    /** Can be used in UiBinder */
    public void setHref(String url) {
        setUrl(url);
    }

    @Override
    public JQMListItem withText(String text) {
        setText(text);
        return this;
    }

    /**
     * CheckBox will be created for this list item.
     * <p>See <a href="http://stackoverflow.com/a/13931919">Checkbox in ListView</a></p>
     */
    public void setCheckBox(IconPos iconPos) {
        if (checkBoxElem != null) {
            JQMCommon.setIconPos(checkBoxElem, iconPos);
            return;
        }

        if (anchor == null) setUrl("#");
        anchor.getStyle().setPadding(0, Unit.PX);
        // TODO: here should be set padding for split icon case

        LabelElement label = Document.get().createLabelElement();
        setStyleName(label, "jqm4gwt-li-checkbox");
        JQMCommon.setCorners(label, false);
        JQMCommon.setIconPos(label, iconPos);
        Style st = label.getStyle();
        st.setBorderWidth(0, Unit.PX);
        st.setMarginTop(0, Unit.PX);
        st.setMarginBottom(0, Unit.PX);

        FieldSetElement fldSet = Document.get().createFieldSetElement();
        CheckboxControlGroup grp = new CheckboxControlGroup(fldSet, "jqm4gwt-li-checkbox-ctrls");
        label.appendChild(fldSet);

        TextBox cb = new TextBox();
        cb.getElement().setAttribute("type", "checkbox");
        fldSet.appendChild(cb.getElement());

        moveAnchorChildrenTo(fldSet);
        checkBoxElem = label;
        checkBoxCtrlGrp = grp;
        checkBoxInput = cb.getElement().cast();
        anchor.appendChild(checkBoxElem);
    }

    public IconPos getCheckBox() {
        if (checkBoxElem == null) return null;
        return JQMCommon.getIconPos(checkBoxElem);
    }

    public boolean isChecked() {
        if (checkBoxInput == null) return false;
        return checkBoxInput.isChecked();
    }

    private native void setChecked(InputElement e, boolean value) /*-{
        $wnd.$(e).prop('checked', value).checkboxradio('refresh');
    }-*/;

    public void setChecked(boolean value) {
        if (checkBoxInput == null || isChecked() == value) return;
        setChecked(checkBoxInput, value);
    }

    /**
     * Currently possible only in checkBox mode, {@link JQMListItem#setCheckBox(IconPos iconPos)}
     */
    public void addWidget(Widget w) {
        if (w == null || checkBoxCtrlGrp == null) return;
        checkBoxCtrlGrp.add(w);
    }
}
