package com.sksamuel.jqm4gwt;

import java.util.Collection;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMPageEvent.PageState;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.sksamuel.jqm4gwt.toolbar.JQMPanel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 23:55:27
 *         <p/>
 *         A {@link JQMPage} is the base container for a single page. Any JQM
 *         widgets can be added to the page. You can consider a JQMPage as like
 *         a GWT "view" in the MVP paradigm.
 *         <p/>
 *         A {@link JQMPage} is also a {@link JQMWidget} and thus all the usual
 *         methods are available.
 */
public class JQMPage extends JQMContainer implements HasFullScreen<JQMPage> {

    static int counter = 1;

    /**
     * The primary content div
     */
    private JQMContent content;

    public boolean firstShow = false;

    protected JQMHeader header;

    protected JQMFooter footer;

    protected JQMPanel panel;

    private boolean contentCentered;
    private boolean windowResizeInitialized;

    private double contentHeightPercent;

    /**
     * Create a new {@link JQMPage}. Using this constructor, the page will not be rendered until a containerID has been
     * assigned.
     */
    private JQMPage() {
        setRole("page");
        content = createContent();
    }

    /**
     * Create a new {@link JQMPage} with an automatically assigned page id,
     * and then add the given widgets serially to the page layout.
     */
    public JQMPage(Collection<Widget> widgets) {
        this();
        withContainerId();
        if (widgets != null)
            add(widgets);
    }

    /**
     * Creates a {@link JQMPage} with the given id
     *
     * @param containerId the id to use as this page's id
     */
    public @UiConstructor JQMPage(String containerId) {
        this();
        this.setContainerId(containerId);
    }

    /**
     * Assigns a default containerId of 'page' followed by the instance number. This can only be called once. All
     * subsequent attempts on this instance will result in an IllegalStateException.
     * @return the instance being operated on as part of a Fluent API
     */
    @Override
    public JQMContainer withContainerId() {
        setContainerId("page" + (counter++));
        return this;
    }

    /**
     * Sets the containerId so it can be referenced by name. This can only be set once. All subsequent attempts on this
     * instance will result in an IllegalStateException.
     * @param containerId
     */
    @Override
    public void setContainerId(String containerId) {
        if (getId() == null) {
            super.setContainerId(containerId);
            JQMContext.attachAndEnhance(this);
        } else if (! containerId.equals(getId())) {
            throw new IllegalStateException("Attempt to change JQMPage with containerId '" + getId() + "' to '" + containerId + "' failed - once set, it cannot be changed.");
        }
    }

    /**
     * Create a new {@link JQMPage} with an automatically assigned page id,
     * and then add the given widgets serially to the page layout.
     */
    public JQMPage(Widget... widgets) {
        this();
        withContainerId();
        if (widgets != null)
            add(widgets);
    }

    /**
     * Sets the footer of the page. Alias for setFooter(). Any existing footer
     * will be replaced.
     */
    public void add(JQMFooter footer) {
        setFooter(footer);
    }

	/**
	 * Logical add operation. If you do this you are responsible for adding it
	 * to the DOM yourself.
	 *
	 * @param child
	 *            the child widget to be added
	 */
	protected void addLogical(Widget child) {
		// Detach new child.
		child.removeFromParent();

		// Logical attach.
		getChildren().add(child);

		// Adopt.
		adopt(child);
	}

	/**
	 * Logical remove operation. If you do this you are responsible for removing it
	 * from the DOM yourself.
	 *
	 * @param w
	 *            the child widget to be removed
	 */
	protected boolean removeLogical(Widget w) {
		// Validate.
		if (w.getParent() != this) {
			return false;
		}
		// Orphan.
		try {
			orphan(w);
		} finally {
			// Logical detach.
			getChildren().remove(w);
		}
		return true;
	}

    /**
     * Sets the header of the page. Alias for setHeader(). Any existing header
     * will be replaced.
     */
    public void add(JQMHeader header) {
        setHeader(header);
    }

    public void add(JQMPanel panel) {
    	setPanel(panel);
    }

    /**
     * Adds a widget to the primary content container of this page.
     *
     * @param widget the widget to add to the primary content
     */
    @Override
    public void add(Widget widget) {
        if (widget instanceof JQMContent)
            throw new RuntimeException("Do not add content widgets here, call createContent instead");
        getPrimaryContent().add(widget);
        // if page is already enhanced then we need to enhance the content
        // manually
        // if (enhanced)
        // triggerCreate();
    }

    private native void bindLifecycleEvents(JQMPage p, String id) /*-{

        $wnd.$('div[data-url="' + id + '"]').bind("pageinit",
            function(event, ui) {
                p.@com.sksamuel.jqm4gwt.JQMPage::doPageInit()();
            });

        $wnd.$('div[data-url="' + id + '"]').bind("pageshow",
            function(event, ui) {
                p.@com.sksamuel.jqm4gwt.JQMPage::doPageShow()();
            });

        $wnd.$('div[data-url="' + id + '"]').bind("pagehide",
            function(event, ui) {
                p.@com.sksamuel.jqm4gwt.JQMPage::doPageHide()();
            });

        $wnd.$('div[data-url="' + id + '"]').bind("pagebeforehide",
            function(event, ui) {
                p.@com.sksamuel.jqm4gwt.JQMPage::doPageBeforeHide()();
            });

        $wnd.$('div[data-url="' + id + '"]').bind("pagebeforeshow",
            function(event, ui) {
                p.@com.sksamuel.jqm4gwt.JQMPage::doPageBeforeShow()();
            });

    }-*/;

    private native void unbindLifecycleEvents(String id) /*-{
        $wnd.$('div[data-url="' + id + '"]').unbind("pageinit");
        $wnd.$('div[data-url="' + id + '"]').unbind("pageshow");
        $wnd.$('div[data-url="' + id + '"]').unbind("pagehide");
        $wnd.$('div[data-url="' + id + '"]').unbind("pagebeforehide");
        $wnd.$('div[data-url="' + id + '"]').unbind("pagebeforeshow");

    }-*/;

    @Override
    protected void onLoad()
    {
        bindLifecycleEvents(this, getId());
    }

    @Override
    protected void onUnload()
    {
    	unbindLifecycleEvents(getId());
    }

    @Override
    public void clear() {
        throw new RuntimeException("You called clear on the page, you probably wanted to call clear on a content panel");
    }

    /**
     * Creates a content container on this page and returns it. Content can
     * then be added to this secondary container. There is no limit to the
     * number of secondary content containers that can be created.
     */
    public JQMContent createContent() {
        JQMContent content = new JQMContent();
        add(content, getElement());
        return content;
    }

    public String getCookie(String name) {
        return Cookies.getCookie(name);
    }

    protected int getCookieInteger(String value) {
        return Integer.parseInt(value);
    }

    public String getParameter(String name) {
        return Window.Location.getParameter(name);
    }

    /**
     * Returns the primary content container
     *
     * @returns the primary content container
     */
    public JQMContent getPrimaryContent() {
        return content;
    }

    private Element getToolBar(String role) {
        Element element = getElement().getFirstChildElement();
        while (element != null) {
            if (role.equals(element.getAttribute("data-role"))) {
                return element;
            }
            element = element.getNextSiblingElement();
        }
        return null;
    }

    public boolean hasCookie(String name) {
        return getCookie(name) != null;
    }

    /**
     *
     */
    protected boolean hasParameter(String name) {
        return getParameter(name) != null;
    }

    /**
     * Returns true if this page has an auto generated back button
     */
    public boolean isBackButton() {
        return "true".equals(getAttribute("data-add-back-btn"));
    }

    @Override
    public boolean isFullScreen() {
        return "true".equals(getAttribute("data-fullscreen"));
    }

    /**
     * Triggered on the page being hidden, before its transition begins.
     */
    protected void onPageBeforeHide() {
    }

    protected void doPageBeforeHide() {
        onPageBeforeHide();
        JQMPageEvent.fire(this, PageState.BEFORE_HIDE);
    }

    /**
     * Triggered on the page being shown, before its transition begins.
     */
    protected void onPageBeforeShow() {
    }

    protected void doPageBeforeShow() {
        onPageBeforeShow();
        JQMPageEvent.fire(this, PageState.BEFORE_SHOW);
    }

    /**
     * Triggered on the page being hidden, after its transition completes.
     */
    protected void onPageHide() {
    }

    protected void doPageHide() {
        onPageHide();
        JQMPageEvent.fire(this, PageState.HIDE);
    }

    /**
     * Triggered on the page being hidden, after its transition completes.
     */
    protected void onPageShow() {
    }

    protected void doPageShow() {
        onPageShow();
        JQMPageEvent.fire(this, PageState.SHOW);
        if (contentCentered || contentHeightPercent > 0) {
            recalcContentHeightPercent();
            centerContent();
            if (!windowResizeInitialized) {
                windowResizeInitialized = true;
                Window.addResizeHandler(new ResizeHandler() {
                    @Override
                    public void onResize(ResizeEvent event) {
                        recalcContentHeightPercent();
                        if (contentCentered) centerContent();
                    }
                });
            }
        }
    }

    /**
     * Triggered on the page being initialized, after initialization occurs.
     */
    protected void onPageInit() {
    }

    protected void doPageInit() {
        onPageInit();
        JQMPageEvent.fire(this, PageState.INIT);
    }

    public HandlerRegistration addPageHandler(JQMPageEvent.Handler handler) {
        return addHandler(handler, JQMPageEvent.getType());
    }

    @Override
    public boolean remove(int index) {
        return getPrimaryContent().remove(index);
    }

    @Override
    public boolean remove(Widget w) {
        return getPrimaryContent().remove(w);
    }

    /**
	 * Removes the footer element set on this page. If no footer is set then
	 * this has no effect.
	 */
	public void removeFooter() {
		if (footer != null) {
			removeLogical(footer);
			footer = null;
			removeToolBar("footer");
		}
	}

	/**
	 * Removes the header element set on this page. If no header is set then
	 * this has no effect.
	 */
	public void removeHeader() {
		if (header != null) {
			removeLogical(header);
			header = null;
			removeToolBar("header");
		}
	}

	public void removePanel() {
		if (panel != null) {
			removeLogical(panel);
			panel = null;
			removeToolBar("panel");
		}
	}

    private void removeToolBar(String name) {
        Element element = getToolBar(name);
        if (element != null)
            getElement().removeChild(element);
    }

    /**
     * Sets whether or not this page should have an auto generated back
     * button. If so, it will be placed in the left slot and override any left
     * button already there.
     * <p/>
     * If you want a back button in the right, then programatically create a
     * button, set it to back using setBack(), and call setRightButton() with
     * the button as the param.
     */
    public void setBackButton(boolean backButton) {
        if (backButton) {
            getElement().setAttribute("data-add-back-btn", "true");
        } else {
            getElement().removeAttribute("data-add-back-btn");
        }
    }

    /**
     * Sets the footer element, overriding an existing footer if any.
     */
    public void setFooter(JQMFooter footer) {
        removeFooter();
        this.footer = footer;
        addLogical(footer);
        getElement().appendChild(footer.getElement());
    }

    public JQMFooter getFooter()
    {
    	return footer;
    }

    @Override
    public void setFullScreen(boolean fs) {
        if (fs) {
            setAttribute("data-fullscreen", "true");
        } else {
            removeAttribute("data-fullscreen");
        }
    }

    @Override
    public JQMPage withFullScreen(boolean fs) {
        setFullScreen(fs);
        return this;
    }

    /**
     * Sets the header element, overriding an existing header if any.
     */
    public void setHeader(JQMHeader header) {
        removeHeader();
        this.header = header;

        addLogical(header);

        if(panel == null)
        {
        	getElement().insertBefore(header.getElement(), getElement().getFirstChild());
        }
        else
        {
        	getElement().insertAfter(header.getElement(), panel.getElement());
        }
    }

    public JQMHeader setHeader(String text) {
        JQMHeader header = new JQMHeader(text);
        setHeader(header);
        return header;
    }

    public JQMHeader getHeader() {
    	return header;
    }

    public void setPanel(JQMPanel panel) {
        removePanel();
        this.panel = panel;
        addLogical(panel);
        getElement().insertBefore(panel.getElement(), getElement().getFirstChild());
    }

    public JQMPanel getPanel() {
    	return panel;
    }

    /**
     * Sets the title of this page, which will be used as the contents of the
     * title tag when this page is the visible page.
     */
    @Override
    public void setTitle(String title) {
        setAttribute("data-title", title);
    }

    @Override
    public String toString() {
        return "JQMPage [id=" + id + "]";
    }

    /**
     * Additional class names can be added directly to content div for better custom styling.
     * The same idea as UiBinder's embedded addStyleNames functionality.
     * <p> Example:
     * <pre>&lt;jqm:JQMPage contentAddStyleNames="aaa bbb ccc"/></pre></p>
     */
    public void setContentAddStyleNames(String value) {
        if (value == null || value.isEmpty()) return;
        String[] arr = value.split(" ");
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].trim();
            if (s.isEmpty()) continue;
            content.addStyleName(s);
        }
    }

    public boolean isContentCentered() {
        return contentCentered;
    }

    /**
     * Content div will be centered between Header and Footer (they must be defined as fixed="true").
     */
    public void setContentCentered(boolean contentCentered) {
        boolean oldVal = this.contentCentered;
        this.contentCentered = contentCentered;
        if (oldVal != this.contentCentered && content != null && content.isAttached()) {
            if (this.contentCentered) centerContent();
            else clearCenterContent();
        }
    }

    public double getContentHeightPercent() {
        return contentHeightPercent;
    }

    public void setContentHeightPercent(double contentHeightPercent) {
        double oldVal = this.contentHeightPercent;
        this.contentHeightPercent = contentHeightPercent;
        if (oldVal != this.contentHeightPercent && content != null && content.isAttached()) {
            recalcContentHeightPercent();
            if (this.contentCentered) centerContent();
        }
    }

    /**
     * Forcefully centers (just once) page content (needed when content size is changed, because
     * there is no good way to get resize notification for DOM elements).
     * <p> Warning! - contentCentered property is not affected, you have to change it manually. </p>
     * <p> See <a href="http://stackoverflow.com/questions/3444719/how-to-know-when-an-dom-element-moves-or-is-resized">
     * How to know when an DOM element moves or is resized</a></p>
     */
    public void centerContent() {
        int headerH = header == null ? 0 : header.getOffsetHeight();
        int footerH = footer == null ? 0 : footer.getOffsetHeight();
        int windowH = Window.getClientHeight();
        int contentH = content.getOffsetHeight();
        int marginTop = (windowH - headerH - footerH - contentH) / 2;
        if (marginTop < 0) marginTop = 0;
        content.getElement().getStyle().setProperty("marginTop", String.valueOf(marginTop) + "px");
    }

    /**
     * Forcefully recalculates (if defined, and just once) page content height
     * (needed when content area size is changed, because there is no good way to get resize
     * notification for DOM elements).
     */
    public void recalcContentHeightPercent() {
        if (contentHeightPercent > 0) {
            int headerH = header == null ? 0 : header.getOffsetHeight();
            int footerH = footer == null ? 0 : footer.getOffsetHeight();
            int windowH = Window.getClientHeight();
            double h = (windowH - headerH - footerH) * 0.01d * contentHeightPercent;
            h = Math.floor(h);
            content.getElement().getStyle().setProperty("minHeight", String.valueOf(Math.round(h)) + "px");
        } else {
            content.getElement().getStyle().clearProperty("minHeight");
        }
    }

    /**
     * Content is not centered anymore, i.e. aligned to the top (just once).
     * <p> Warning! - contentCentered property is not affected, you have to change it manually. </p>
     */
    public void clearCenterContent() {
        content.getElement().getStyle().clearProperty("marginTop");
    }

}
