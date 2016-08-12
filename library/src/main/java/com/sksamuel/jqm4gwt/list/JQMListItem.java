package com.sksamuel.jqm4gwt.list;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.FieldSetElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasRel;
import com.sksamuel.jqm4gwt.HasText;
import com.sksamuel.jqm4gwt.HasTransition;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.events.HasTapHandlers;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.TapEvent;
import com.sksamuel.jqm4gwt.events.TapHandler;
import com.sksamuel.jqm4gwt.form.elements.JQMFilterable;
import com.sksamuel.jqm4gwt.html.CustomFlowPanel;
import com.sksamuel.jqm4gwt.panel.JQMControlGroup;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 11:21:29
 */
public class JQMListItem extends CustomFlowPanel implements HasText<JQMListItem>, HasClickHandlers,
        HasTapHandlers, HasRel<JQMListItem>, HasTransition<JQMListItem> {

    public static final String STYLE_UI_LI_HAS_THUMB = "ui-li-has-thumb";
    public static final String STYLE_UI_LI_HAS_ICON = "ui-li-has-icon";

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
    private CustomFlowPanel anchorPanel;

    /** Split button element */
    private Element split;
    private String splitTheme;

    /**
     * The element that holds the aside content
     */
    private Element asideElem;

    /**
     * The element that holds the content of the "main" text
     */
    private Element headerElem;

    private JQMList list;

    public class LiControlGroup extends JQMControlGroup {

        protected LiControlGroup(Element element, String styleName) {
            super(element, styleName);
            // ui-controlgroup-horizontal is TOO intrusive, so we leave this group as vertical
            // and correct ui-controlgroup-vertical unwanted intrusions in jqm4gwt.css
        }
    }

    private LiControlGroup controlGroup;
    private ComplexPanel controlGroupRoot;
    private TextBox checkBoxInput;

    private HandlerRegistration clickHandler;
    private HandlerRegistration tapHandler;

    private Object tag;

    /**
     * Create empty {@link JQMListItem}
     */
    @UiConstructor
    public JQMListItem() {
        super(DOM.createElement(LIElement.TAG));
        setStyleName("jqm4gwt-listitem");
        setId();
    }

    /**
     * Create {@link JQMListItem} with the initial content set to the value of the text.
     */
    public JQMListItem(String text) {
        this();
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

    public JQMListItem(String text, IconPos checkBox) {
        this(text);
        setCheckBox(checkBox);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @Override
    public HandlerRegistration addTapHandler(TapHandler handler) {
        // this is not a native browser event so we will have to manage it via JS
        return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, JQMComponentEvents.TAP_EVENT, TapEvent.getType());
    }

    private boolean isSplitClicked(Element elt) {
        if (split == null || elt == null) return false;
        Element element = elt;
        while (element != null) {
            if (element == split) return true;
            element = element.getParentElement();
        }
        return false;
    }

    public boolean isSplitClicked(EventTarget target) {
        if (target == null) return false;
        Element element = Element.as(target);
        return isSplitClicked(element);
    }

    /**
     * Adds a header element containing the given text.
     *
     * @param n - the Hn element to use, e.g. if n is 2 then a {@code <h2>} element is created.
     * @param html - the value to set as the inner html of the {@code <hn>} element.
     */
    public JQMListItem addHeaderText(int n, String html) {
        Element e = Document.get().createHElement(n);
        e.setInnerHTML(html);
        attachChild(e);
        return this;
    }

    /**
     * Adds a paragraph element containing the given text.
     *
     * @param html - the value to set as the inner html of the p element.
     */
    public JQMListItem addText(String html) {
        Element e = Document.get().createPElement();
        e.setInnerHTML(html);
        attachChild(e);
        return this;
    }

    /**
     * Adds a div element containing the given text.
     *
     * @param html - the value to set as the inner html of the div element.
     */
    public JQMListItem addDiv(String html) {
        Element e = Document.get().createDivElement();
        e.setInnerHTML(html);
        attachChild(e);
        return this;
    }

    private void insertFirstChild(Element elem) {
        if (anchor == null) getElement().insertFirst(elem);
        else if (controlGroup != null) controlGroup.getElement().insertFirst(elem);
        else anchor.insertFirst(elem);
    }

    private void attachChild(Element elem) {
        if (anchor == null) getElement().appendChild(elem);
        else if (controlGroup != null) controlGroup.getElement().appendChild(elem);
        else anchor.appendChild(elem);
    }

    private void removeChild(Element elem) {
        if (anchor == null) getElement().removeChild(elem);
        else if (controlGroup != null) controlGroup.getElement().removeChild(elem);
        else anchor.removeChild(elem);
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
        if (countElem == null) return null;
        return Integer.parseInt(countElem.getInnerText());
    }

    /**
     * Returns the value of the "main" text element
     */
    @Override
    public String getText() {
        return headerElem != null ? headerElem.getInnerText() : null;
    }

    private void moveAnchorChildrenTo(Element elt, Element... excludes) {
        List<Node> move = new ArrayList<Node>();
        for (int k = 0; k < anchor.getChildCount(); k++) {
            Node node = anchor.getChild(k);
            if (excludes.length > 0) {
                boolean exclude = false;
                for (int n = 0; n < excludes.length; n++) {
                    if (node == excludes[n]) {
                        exclude = true;
                        break;
                    }
                }
                if (exclude) continue;
            }
            move.add(node);
        }
        for (int i = 0; i < move.size(); i++) {
            Node node = move.get(i);
            anchor.removeChild(node);
            elt.appendChild(node);
        }
    }

    private void moveAnchorChildrenToThis() {
        moveAnchorChildrenTo(getElement());
    }

    private void moveThisChildrenToAnchor() {
        Element elt = getElement();
        int cnt = elt.getChildCount();
        if (cnt == 0) return;
        List<Node> move = new ArrayList<Node>(cnt);
        for (int i = 0; i < cnt; i++) {
            move.add(elt.getChild(i));
        }
        for (int i = 0; i < move.size(); i++) {
            Node node = move.get(i);
            elt.removeChild(node);
            anchor.appendChild(node);
        }
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
            imageElem.removeFromParent();
            imageElem = null;
        }
        getElement().removeClassName(STYLE_UI_LI_HAS_THUMB);
        return this;
    }

    /**
     * Remove the url from this list item changing the item into a read only
     * item.
     */
    public JQMListItem removeHref() {
        if (anchor == null) return this;

        if (anchorPanel != null) {
            List<Widget> lst = new ArrayList<Widget>();
            for (int i = anchorPanel.getWidgetCount() - 1; i >= 0; i--) {
                Widget w = anchorPanel.getWidget(i);
                anchorPanel.remove(i);
                lst.add(0, w);
            }
            remove(anchorPanel);
            cleanUpLI();
            for (Widget w : lst) this.add(w);
        } else {
            moveAnchorChildrenToThis();
            getElement().removeChild(anchor);
        }
        anchor = null;
        anchorPanel = null;
        setSplitHref(null);
        return this;
    }

    /**
     * @deprecated use {@link #removeHref()} directly
     */
    @Deprecated
    public JQMListItem removeUrl() {
    	return removeHref();
    }

    private void cleanUpLI() {
        Element elt = getElement();
        for (int i = elt.getChildCount() - 1; i >= 0; i--) {
            elt.removeChild(elt.getChild(i));
        }
        setStyleName("jqm4gwt-listitem");
    }

    /**
     * Sets the content of the aside. The aside is supplemental content that
     * is positioned to the right of the main content.
     */
    public void setAside(String text) {
        if (text == null)
            throw new RuntimeException("Cannot set aside to null. Call removeAside() if you wanted to remove the aside text");

        if (asideElem == null)
            createAndAttachAsideElem();
        asideElem.setInnerText(text);
    }

    public JQMListItem withAside(String text) {
        setAside(text);
        return this;
    }

    /**
     * Set the count bubble value. If null this will throw a runtime
     * exception. To remove a count bubble call removeCount()
     */
    public void setCount(Integer count) {
        if (count == null)
            throw new RuntimeException("Cannot set count to null. Call removeCount() if you wanted to remove the bubble");

        if (countElem == null)
            createAndAttachCountElement();
        countElem.setInnerText(count.toString());
    }

    public JQMListItem withCount(Integer count) {
        setCount(count);
        return this;
    }

    private JQMListItem setId() {
        getElement().setId(Document.get().createUniqueId());
        return this;
    }

    public String getId() {
        return getElement().getId();
    }

    /**
     * Sets the image to be used to the given source url.
     * <br> The same as setImage(), but image is marked as icon class.
     */
    public void setIcon(String src) {
        setImage(src);
        if (imageElem != null) {
            imageElem.removeClassName("jqm4gwt-listitem-thumb");
            imageElem.addClassName("jqm4gwt-listitem-icon");
        }
    }

    /** The same as {@link JQMListItem#setIcon(String)} */
    public JQMListItem withIcon(String src) {
        setIcon(src);
        return this;
    }

    /**
     * Sets the image to be used to the given source url.
     * <br> The same as setImage(), but image is marked as thumbnail class.
     */
    public void setThumbnail(String src) {
        setImage(src);
        if (imageElem != null) {
            imageElem.removeClassName("jqm4gwt-listitem-icon");
            imageElem.addClassName("jqm4gwt-listitem-thumb");
        }
    }

    /** The same as {@link JQMListItem#setThumbnail(String)} */
    public JQMListItem withThumbnail(String src) {
        setThumbnail(src);
        return this;
    }

    /**
     * Sets the image on this list item to the given source url.
     * <br> Neither 'jqm4gwt-listitem-thumb' nor 'jqm4gwt-listitem-icon' class is added.
     */
    public void setImage(String src) {
        if (src == null) {
            throw new RuntimeException("Cannot set image to null. Call removeImage() if you wanted to remove the image");
        }

        if (imageElem == null) {
            imageElem = Document.get().createImageElement();
            // must be first child according to jquery.mobile-1.4.x.css
            if (anchor != null) anchor.insertFirst(imageElem);
            else insertFirstChild(imageElem);
        }
        imageElem.setAttribute("src", src);
        getElement().addClassName(STYLE_UI_LI_HAS_THUMB);
    }

    /** The same as {@link JQMListItem#setImage(String)} */
    public JQMListItem withImage(String src) {
        setImage(src);
        return this;
    }

    /**
     * Adds secondary image to this list item. It's forcefully added directly to &lt;li&gt; element.
     * <br> Additional CSS is needed to control appearance of this image, for example right side
     * icon on the static band can be implemented, see <b>jqm4gwt-list-static-item-img-right</b> CSS rule.
     */
    public ImageElement addSecondaryImage(String src) {
        if (src == null) {
            throw new RuntimeException("Cannot set secondary image to null.");
        }
        ImageElement img = Document.get().createImageElement();
        img.setAttribute("src", src);
        getElement().appendChild(img);
        return img;
    }

    /** For UiBinder, the same as {@link JQMListItem#addSecondaryImage(String)} */
    public void setSecondaryImage(String src) {
        addSecondaryImage(src);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        addItemActivationHandlers();
    }

    @Override
    protected void onUnload() {
        removeItemActivationHandlers();
        super.onUnload();
    }

    public JQMList getList() {
        return list;
    }

    protected JQMListItem setList(JQMList jqmList) {
        removeItemActivationHandlers();
        this.list = jqmList;
        addItemActivationHandlers();

        return this;
    }

    private void removeItemActivationHandlers() {
        if (clickHandler != null)
            clickHandler.removeHandler();
        if (tapHandler != null)
            tapHandler.removeHandler();
    }

    private void addItemActivationHandlers() {
        if (list != null && anchor != null) {
            // why 2 handlers for this?
            // 'tap' bubbles correctly but is not generated on all child widget types for bubbling usage;
            // on some devices 'tap' happens sooner then click event and can trigger actions
            // 'click' is native - generated by more widgets but it might come too late sometimes
            if (clickHandler == null) {
                clickHandler = addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        boolean isSplit = (event != null)
                                ? isSplitClicked(event.getNativeEvent().getEventTarget()) : false;
                        list.setClickItem(JQMListItem.this, isSplit);
                    }
                });
            }
            if (tapHandler == null) {
                tapHandler = addTapHandler(new TapHandler() {
                    @Override
                    public void onTap(TapEvent event) {
                        boolean isSplit = (event != null) ? isSplitClicked(event
                                .getJQueryEvent().getEventTarget()) : false;
                        list.setClickItem(JQMListItem.this, isSplit);
                    }
                });
            }
        }
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
     * @deprecated use {@link #setHref(String)} directly
     */
    @Deprecated
    public void setUrl(String url) {
        setHref(url);
    }

    /**
     * Sets the url to link to for this item. If this item was a read only
     * item it automatically becomes linkable.
     */
    public void setHref(String url) {
        if (url == null)
            throw new RuntimeException("Cannot set URL to null. Call removeUrl() if you wanted to remove the URL");
        if (anchor == null) {
            if (controlGroupRoot != null) {
                //!!! following code is semi-working, it's not reconstructed item correctly
                remove(controlGroupRoot);
                anchor = Document.get().createAnchorElement();
                anchor.setAttribute("href", url);
                moveThisChildrenToAnchor();
                cleanUpLI();
                prepareAnchorForControlGroup();
                getElement().appendChild(anchor);
                checkAnchorPanel();
            } else {
                // need to make anchor and move children to it
                anchor = Document.get().createAnchorElement();
                moveThisChildrenToAnchor();
                getElement().appendChild(anchor);
            }
            addItemActivationHandlers();
        }
        anchor.setAttribute("href", url);
    }

    public String getHref() {
        return anchor != null ? anchor.getAttribute("href") : null;
    }

    public void setSplitHref(String url) {
        if (url == null) {
            if (split != null) {
                getElement().removeChild(split);
                split = null;
                checkSplitPadding();
            }
            return;
        }
        if (split != null) return;
        if (anchor == null) setUrl("#");
        split = Document.get().createAnchorElement();
        split.setAttribute("href", url);
        getElement().insertAfter(split, anchor);
        setSplitTheme(splitTheme);
        checkSplitPadding();
    }

    public void setDataIcon(DataIcon icon) {
        JQMCommon.setIcon(getElement(), icon);
    }

    public DataIcon getDataIcon() {
        return JQMCommon.getIcon(getElement());
    }

    public void setSplitIcon(DataIcon icon) {
        JQMCommon.setIcon(getElement(), icon);
    }

    public DataIcon getSplitIcon() {
        return JQMCommon.getIcon(getElement());
    }

    public void setSplitTheme(String theme) {
        splitTheme = theme;
        if (split != null) JQMCommon.setTheme(split, theme);
    }

    public String getSplitTheme() {
        if (split == null) return splitTheme;
        splitTheme = JQMCommon.getTheme(split);
        return splitTheme;
    }

    @Override
    public JQMListItem withText(String text) {
        setText(text);
        return this;
    }

    private void checkSplitPadding() {
        // if (anchor == null || controlGroup == null) return;
        // Not needed anymore in jqm 1.4.x
        // anchor.getStyle().setPaddingRight(split == null ? 0 : 42, Unit.PX);
    }

    private void prepareAnchorForControlGroup() {
        if (anchor == null) return;
        anchor.getStyle().setPadding(0, Unit.PX);
        checkSplitPadding();
    }

    private void createControlGroup(boolean linkable) {
        if (controlGroup != null) return;

        if (linkable) {
            if (anchor == null) setUrl("#");
            prepareAnchorForControlGroup();
        } else {
            removeUrl();
        }

        // groupRoot needs to be either "label" for checkbox or "div" for other elements (radio group for example)
        CustomFlowPanel groupRoot = new CustomFlowPanel(checkBoxInput == null
                                                        ? DOM.createDiv() : DOM.createLabel());
        setStyleName(groupRoot.getElement(), "jqm4gwt-li-band");
        JQMCommon.setCorners(groupRoot, false);
        Style st = groupRoot.getElement().getStyle();
        st.setBorderWidth(0, Unit.PX);
        st.setMarginTop(0, Unit.PX);
        st.setMarginBottom(0, Unit.PX);
        st.setPaddingTop(0, Unit.PX);
        st.setPaddingBottom(0, Unit.PX);

        FieldSetElement fldSet = Document.get().createFieldSetElement();
        LiControlGroup grp = new LiControlGroup(fldSet, "jqm4gwt-li-controls");
        groupRoot.add(grp);

        if (anchor != null) {
            if (imageElem != null && anchor.equals(imageElem.getParentElement())) {
                moveAnchorChildrenTo(fldSet, imageElem/*exclude*/);
            } else {
                moveAnchorChildrenTo(fldSet);
            }
        }
        controlGroupRoot = groupRoot;
        controlGroup = grp;
        if (anchor != null) checkAnchorPanel();
        else add(controlGroupRoot);
    }

    private void checkAnchorPanel() {
        if (anchorPanel == null) {
            anchorPanel = new CustomFlowPanel(anchor);
            int anchorIdx = -1;
            Node parent = anchor.getParentNode();
            if (parent != null && parent == getElement()) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (parent.getChild(i) == anchor) {
                        anchorIdx = i;
                        break;
                    }
                }
            }
            if (anchorIdx >= 0) insert(anchorPanel, anchorIdx);
            else add(anchorPanel);
        }
        if (controlGroupRoot != null && controlGroupRoot.getParent() != anchorPanel) {
            anchorPanel.add(controlGroupRoot);
        }
    }

    /**
     * true - prepare and allow to add widgets to this list box item.
     *
     * @param linkable - if true &lt;a> will be forcefully created, so row will be clickable.
     */
    public void setControlGroup(boolean value, boolean linkable) {
        if (value) {
            createControlGroup(linkable);
        } else if (controlGroup != null) {
            if (anchorPanel != null) remove(anchorPanel);
            else if (anchor != null) getElement().removeChild(anchor);
            anchor = null;
            anchorPanel = null;
            setSplitHref(null);
            controlGroupRoot = null;
            controlGroup = null;
            checkBoxInput = null;
        }
    }

    public void setControlGroup(boolean value) {
        setControlGroup(value, true/*linkable*/);
    }

    public boolean isControlGroup() {
        return controlGroup != null;
    }

    public LiControlGroup getControlGroup() {
        return controlGroup;
    }

    /**
     * CheckBox will be created for this list item.
     * <p>See <a href="http://stackoverflow.com/a/13931919">Checkbox in ListView</a></p>
     */
    public void setCheckBox(IconPos iconPos) {
        if (checkBoxInput != null) {
            if (iconPos == null) {
                controlGroup.remove(checkBoxInput);
                checkBoxInput = null;

                // refresh control group
                setControlGroup(false);
                setControlGroup(true);
            } else {
                JQMCommon.setIconPos(controlGroupRoot, iconPos);
            }
            return;
        }

        if (iconPos == null) return;
        TextBox cb = new TextBox();
        cb.getElement().setAttribute("type", "checkbox");
        checkBoxInput = cb;

        // controlGroupRoot needs to be either "label" for checkbox or "div" for other elements (radio group for example)
        setControlGroup(false);
        setControlGroup(true);
        JQMCommon.setIconPos(controlGroupRoot, iconPos);
        controlGroup.insert(cb, 0);
    }

    public IconPos getCheckBox() {
        if (checkBoxInput == null) return null;
        return JQMCommon.getIconPos(controlGroupRoot);
    }

    public boolean isCheckBox() {
        return checkBoxInput != null;
    }

    public boolean isChecked() {
        if (checkBoxInput == null) return false;
        InputElement cb = checkBoxInput.getElement().cast();
        return cb.isChecked();
    }

    private static native void setChecked(InputElement elt, boolean value) /*-{
        var w = $wnd.$(elt);
        if (w.data('mobile-checkboxradio') !== undefined) {
            w.prop('checked', value).checkboxradio('refresh');
        } else {
            w.prop('checked', value);
        }
    }-*/;

    public void setChecked(boolean value) {
        if (checkBoxInput == null || isChecked() == value) return;
        InputElement cb = checkBoxInput.getElement().cast();
        setChecked(cb, value);
    }

    /**
     * Currently possible only in checkBox and control group modes.
     * <p>See {@link JQMListItem#setCheckBox(IconPos)} and {@link JQMListItem#setControlGroup(boolean)}
     */
    public void addWidget(Widget w) {
        if (w == null || controlGroup == null) return;
        controlGroup.add(w);
    }

    @Override
    public int getWidgetCount() {
        if (controlGroup == null) return 0;
        return controlGroup.getWidgetCount();
    }

    @Override
    public Widget getWidget(int index) {
        if (controlGroup == null) return null;
        return controlGroup.getWidget(index);
    }

    public boolean isActiveHighlight() {
        if (anchor == null) return false;
        return JQMCommon.isBtnActive(anchor);
    }

    public void setActiveHighlight(boolean value) {
        if (anchor == null) return;
        JQMCommon.setBtnActive(anchor, value);
    }

    /**
     * @return - true if this item was filtered out by {@link JQMFilterable}.
     */
    public boolean isFilteredOut() {
        return JQMCommon.hasStyle(this, "ui-screen-hidden");
    }

    @Override
    public String getRel() {
        return anchor != null ? JQMCommon.getAttribute(anchor, "data-rel") : null;
    }

    @Override
    public void setRel(String rel) {
        if (anchor == null) {
            if (rel != null && !rel.isEmpty()) {
                setUrl("#");
            } else {
                return;
            }
        }
        if (anchor != null) JQMCommon.setAttribute(anchor, "data-rel", rel);
    }

    @Override
    public JQMListItem withRel(String rel) {
        setRel(rel);
        return this;
    }

    public String getSplitRel() {
        return split != null ? JQMCommon.getAttribute(split, "data-rel") : null;
    }

    public void setSplitRel(String rel) {
        if (split == null) {
            if (rel != null && !rel.isEmpty()) {
                setSplitHref("#");
            } else {
                return;
            }
        }
        if (split != null) JQMCommon.setAttribute(split, "data-rel", rel);
    }

    /**
     * Returns true if this list item is set to load a popup
     */
    public boolean isPopup() {
        return "popup".equals(getRel());
    }

    public void setPopup(boolean popup) {
        setRel(popup ? "popup" : null);
    }

    public JQMListItem withPopup(boolean popup) {
        setPopup(popup);
        return this;
    }

    public String getPopupPos() {
        return anchor != null ? JQMCommon.getPopupPos(anchor) : null;
    }

    public void setPopupPos(String pos) {
        if (anchor == null) {
            if (pos != null && !pos.isEmpty()) {
                setUrl("#");
            } else {
                return;
            }
        }
        if (anchor != null) JQMCommon.setPopupPos(anchor, pos);
    }

    /**
     * Returns true if this list item is set to load the linked page as a dialog page
     */
    public boolean isDialog() {
        return Mobile.DATA_ROLE_DIALOG.equals(getRel());
    }

    /**
     * Sets this list item to call a dialog item. This changes the look and feel
     * of the page that is loaded as a consequence of clicking on this item.
     */
    public void setDialog(boolean dialog) {
        setRel(dialog ? Mobile.DATA_ROLE_DIALOG : null);
    }

    public JQMListItem withDialog(boolean dialog) {
        setDialog(dialog);
        return this;
    }

    @Override
    public Transition getTransition() {
        return anchor != null ? JQMCommon.getTransition(anchor) : null;
    }

    /**
     * Sets the transition to be used by this list item when loading the URL.
     */
    @Override
    public void setTransition(Transition transition) {
        if (anchor == null) {
            if (transition != null) {
                setUrl("#");
            } else {
                return;
            }
        }
        if (anchor != null) JQMCommon.setTransition(anchor, transition);
    }

    @Override
    public JQMListItem withTransition(Transition transition) {
        setTransition(transition);
        return this;
    }

    /**
     * Returns true if this list item split part is set to load a popup
     */
    public boolean isSplitPopup() {
        return "popup".equals(getSplitRel());
    }

    public void setSplitPopup(boolean popup) {
        setSplitRel(popup ? "popup" : null);
    }

    public JQMListItem withSplitPopup(boolean popup) {
        setSplitPopup(popup);
        return this;
    }

    public String getSplitPopupPos() {
        return split != null ? JQMCommon.getPopupPos(split) : null;
    }

    public void setSplitPopupPos(String pos) {
        if (split == null) {
            if (pos != null && !pos.isEmpty()) {
                setSplitHref("#");
            } else {
                return;
            }
        }
        if (split != null) JQMCommon.setPopupPos(split, pos);
    }

    /**
     * Returns true if this list item split part is set to load the linked page as a dialog page
     */
    public boolean isSplitDialog() {
        return Mobile.DATA_ROLE_DIALOG.equals(getSplitRel());
    }

    /**
     * Sets this list item split part to call a dialog item. This changes the look and feel
     * of the page that is loaded as a consequence of clicking on this item.
     */
    public void setSplitDialog(boolean dialog) {
        setSplitRel(dialog ? Mobile.DATA_ROLE_DIALOG : null);
    }

    public JQMListItem withSplitDialog(boolean dialog) {
        setSplitDialog(dialog);
        return this;
    }

    public Transition getSplitTransition() {
        return split != null ? JQMCommon.getTransition(split) : null;
    }

    /**
     * Sets the transition to be used by this list item split part when loading the URL.
     */
    public void setSplitTransition(Transition transition) {
        if (split == null) {
            if (transition != null) {
                setSplitHref("#");
            } else {
                return;
            }
        }
        if (split != null) JQMCommon.setTransition(split, transition);
    }

    public JQMListItem withSplitTransition(Transition transition) {
        setSplitTransition(transition);
        return this;
    }

    public Object getTag() {
        return tag;
    }

    /**
     * Additional information can be attached to list item (for example linked JQMListDivider).
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

}
