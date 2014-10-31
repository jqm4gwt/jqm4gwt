package com.sksamuel.jqm4gwt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMPageEvent.PageState;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMEvent;
import com.sksamuel.jqm4gwt.events.JQMOrientationChangeHandler;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;
import com.sksamuel.jqm4gwt.toolbar.JQMPanel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 4 May 2011 23:55:27
 * <p/>
 * A {@link JQMPage} is the base container for a single page. Any JQM widgets can be added
 * to the page. You can consider a JQMPage as like a GWT "view" in the MVP paradigm.
 */
public class JQMPage extends JQMContainer implements HasFullScreen<JQMPage> {

    public static final String UI_DIALOG_BACKGROUND = "ui-dialog-background";
    public static final String DATA_DOM_CACHE = "data-dom-cache";
    public static final String JQM4GWT_DLG_TRANSPARENT = "jqm4gwt-dialog-transparent";

    private static final String STYLE_UI_DIALOG = "ui-dialog";
    private static final String UI_DIALOG_CONTAIN = "ui-dialog-contain";
    private static final String UI_BODY_INHERIT = "ui-body-inherit";

    private static final String JQM4GWT_FIXED_HIDDEN = "jqm4gwt-fixed-hidden";

    private static int counter = 1;

    /** Needed to find out JQMPage by its Element received usually from JS */
    private static final Map<Element, JQMPage> allPages = new HashMap<Element, JQMPage>(); // there is no WeakHashMap in GWT

    /** The primary content div */
    private JQMContent content;

    public boolean firstShow = false;

    protected HasJqmHeader header;
    protected HasJqmFooter footer;
    protected JQMPanel panel;

    private boolean contentCentered;
    private double contentHeightPercent;
    private boolean pseudoFixedToolbars;

    private double hideFixedToolbarsIfContentAreaPercentBelow = 50.0d;
    private int hideFixedToolbarsIfVirtualKeyboard = 0; // threshold(height) for keyboard detection

    private boolean windowResizeInitialized;
    private boolean orientationChangeInitialized;
    private int initialWindowHeight;
    private int hiddenHeaderH;
    private boolean hiddenHeaderFixed;
    private int hiddenFooterH;
    private boolean hiddenFooterFixed;

    private boolean transparent;
    private Element transparentPrevPage;
    private boolean transparentPrevPageClearCache;
    private boolean transparentDoPrevPageLifecycle;

    /**
     * Create a new {@link JQMPage}. Using this constructor, the page will not be rendered
     * until a containerID has been assigned.
     */
    private JQMPage() {
        allPages.put(getElement(), this);
        setRole(getDfltRole());
        content = createContent();
    }

    public static JQMPage findPage(Element elt) {
        return elt == null ? null : allPages.get(elt);
    }

    /** Could be overridden by descendants */
    protected String getDfltRole() {
        return "page";
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
        setContainerId(getDfltRole() + (counter++));
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
	 * @param w - the child widget to be removed
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
        if (widget instanceof HasJqmHeader) {
            setHeader((HasJqmHeader) widget);
            return;
        } else if (widget instanceof HasJqmFooter) {
            setFooter((HasJqmFooter) widget);
            return;
        } else if (widget instanceof JQMContent) {
            throw new RuntimeException("Do not add content widgets here, call createContent() instead");
        }
        getPrimaryContent().add(widget);

        // if page is already enhanced then we need to enhance the content manually
        // if (enhanced) triggerCreate();
    }

    private static native void bindLifecycleEvents(JQMPage p, Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded

        var page = $wnd.$(elt);

        page.on("pagecreate", function(event, ui) {
            p.@com.sksamuel.jqm4gwt.JQMPage::doPageInit()();
        });

        page.on("pageshow", function(event, ui) {
            p.@com.sksamuel.jqm4gwt.JQMPage::doPageShow(Lcom/google/gwt/dom/client/Element;)(ui.prevPage.get(0));
        });

        page.on("pagehide", function(event, ui) {
            p.@com.sksamuel.jqm4gwt.JQMPage::doPageHide(Lcom/google/gwt/dom/client/Element;)(ui.nextPage.get(0));
        });

        page.on("pagebeforehide", function(event, ui) {
            p.@com.sksamuel.jqm4gwt.JQMPage::doPageBeforeHide(Lcom/google/gwt/dom/client/Element;)(ui.nextPage.get(0));
        });

        page.on("pagebeforeshow", function(event, ui) {
            p.@com.sksamuel.jqm4gwt.JQMPage::doPageBeforeShow(Lcom/google/gwt/dom/client/Element;)(ui.prevPage.get(0));
        });
    }-*/;

    public void bindLifecycleEvents() {
        bindLifecycleEvents(this, this.getElement());
    }

    private static native void unbindLifecycleEvents(Element elt) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var page = $wnd.$(elt);
        page.off("pagecreate");
        page.off("pageshow");
        page.off("pagehide");
        page.off("pagebeforehide");
        page.off("pagebeforeshow");
    }-*/;

    public void unbindLifecycleEvents() {
        unbindLifecycleEvents(this.getElement());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        bindLifecycleEvents(this, getElement());
    }

    @Override
    protected void onUnload() {
    	unbindLifecycleEvents(getElement());
    	super.onUnload();
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
        Element elt = getElement();
        add(content, elt);
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

    protected boolean hasParameter(String name) {
        return getParameter(name) != null;
    }

    /**
     * Returns true if this page has an auto generated back button
     */
    public boolean isBackButton() {
        JQMHeader h = getHeader();
        return h != null && h.isBackButton();
    }

    /**
     * See {@link JQMHeader#setBackButton(boolean)}
     */
    public void setBackButton(boolean value) {
        JQMHeader h = getHeader();
        if (h != null) h.setBackButton(value);
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

    /**
     * @param nextPage - DOM element that we are transitioning to.
     */
    protected void doPageBeforeHide(Element nextPage) {
        onPageBeforeHide();
        JQMPageEvent.fire(this, PageState.BEFORE_HIDE);
    }

    /**
     * Triggered on the page being shown, before its transition begins.
     */
    protected void onPageBeforeShow() {
    }

    public boolean isDialog() {
        return JQMCommon.hasStyle(getElement(), STYLE_UI_DIALOG);
    }

    /**
     * @param prevPage - DOM element that we are transitioning away from.
     * Could be null when the first page is transitioned in during application startup.
     */
    protected void doPageBeforeShow(Element prevPage) {
        onPageBeforeShow();
        JQMPageEvent.fire(this, PageState.BEFORE_SHOW);

        if (isDialog()) {
            if (transparent && prevPage != null) {
                transparentPrevPage = prevPage;
                prevPage.addClassName(UI_DIALOG_BACKGROUND);
                String s = prevPage.getAttribute(DATA_DOM_CACHE);
                if ("true".equals(s)) {
                    transparentPrevPageClearCache = false;
                } else {
                    transparentPrevPageClearCache = true;
                    prevPage.setAttribute(DATA_DOM_CACHE, "true");
                }
                if (!transparentDoPrevPageLifecycle) {
                    JQMPage prev = allPages.get(transparentPrevPage);
                    if (prev != null) JQMPage.unbindLifecycleEvents(prev.getElement());
                }
                if (content != null) content.addStyleName(UI_BODY_INHERIT);
                Element dlgContain = JQMCommon.findChild(getElement(), UI_DIALOG_CONTAIN);
                if (dlgContain != null) dlgContain.addClassName(UI_BODY_INHERIT);
            } else {
                transparentPrevPage = null;
                transparentPrevPageClearCache = false;
                if (content != null) content.removeStyleName(UI_BODY_INHERIT);
                Element dlgContain = JQMCommon.findChild(getElement(), UI_DIALOG_CONTAIN);
                if (dlgContain != null) dlgContain.removeClassName(UI_BODY_INHERIT);
            }
        }
    }

    /**
     * Triggered on the page being hidden, after its transition completes.
     */
    protected void onPageHide() {
    }

    /**
     * @param nextPage - DOM element that we are transitioning to.
     */
    protected void doPageHide(Element nextPage) {
        onPageHide();
        JQMPageEvent.fire(this, PageState.HIDE);

        if (transparentPrevPage != null) {
            transparentPrevPage.removeClassName(UI_DIALOG_BACKGROUND);
            if (transparentPrevPageClearCache) {
                transparentPrevPage.removeAttribute(DATA_DOM_CACHE);
            }
            if (!transparentDoPrevPageLifecycle) {
                final JQMPage prev = allPages.get(transparentPrevPage);
                if (prev != null) {
                    Scheduler.get().scheduleFinally(new ScheduledCommand() {
                        @Override
                        public void execute() {
                            JQMPage.bindLifecycleEvents(prev, prev.getElement());
                        }
                    });
                }
            }
            transparentPrevPage = null;
            transparentPrevPageClearCache = false;
        }
    }

    /**
     * Triggered on the page being hidden, after its transition completes.
     */
    protected void onPageShow() {
    }

    /**
     * @param prevPage - DOM element that we are transitioning away from.
     * Could be null when the first page is transitioned in during application startup.
     */
    protected void doPageShow(Element prevPage) {
        initialWindowHeight = Window.getClientHeight();
        onPageShow();
        JQMPageEvent.fire(this, PageState.SHOW);
        if (contentCentered || pseudoFixedToolbars || contentHeightPercent > 0
                || hideFixedToolbarsIfContentAreaPercentBelow > 0
                || hideFixedToolbarsIfVirtualKeyboard > 0) {
            processFixedToolbars();
            recalcContentHeightPercent();
            centerContent();
            initWindowResize();
            if (hideFixedToolbarsIfVirtualKeyboard > 0) initOrientationChange();
        }
    }

    private void initWindowResize() {
        if (windowResizeInitialized) return;
        windowResizeInitialized = true;
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                processFixedToolbars();
                recalcContentHeightPercent();
                centerContent();
            }
        });
    }

    private void initOrientationChange() {
        if (orientationChangeInitialized) return;
        orientationChangeInitialized = true;
        this.addJQMEventHandler(JQMComponentEvents.ORIENTATIONCHANGE,
                new JQMOrientationChangeHandler() {
                    @Override
                    public void onEvent(JQMEvent<?> event) {
                        int h = Window.getClientHeight();
                        int w = Window.getClientWidth();
                        String s = JQMOrientationChangeHandler.Process.getOrientation(event);
                        // iOS and Android give opposite values (iOS - already rotated, Android - not yet rotated)
                        if (JQMOrientationChangeHandler.LANDSCAPE.equals(s)) {
                            initialWindowHeight = Math.min(w, h);
                        } else if (JQMOrientationChangeHandler.PORTRAIT.equals(s)) {
                            initialWindowHeight = Math.max(w, h);
                        }
                        //Window.alert(s + ", height: " + initialWindowHeight);
                        if (hideFixedToolbarsIfVirtualKeyboard > 0) {
                            processFixedToolbars();
                            recalcContentHeightPercent();
                            centerContent();
                        }
                    }
                });
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
			removeLogical(footer.getFooterStage());
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
			removeLogical(header.getHeaderStage());
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
     * Sets the footer element, overriding an existing footer if any.
     */
    public void setFooter(HasJqmFooter footer) {
        removeFooter();
        this.footer = footer;
        if (this.footer == null) return;

        addLogical(footer.getFooterStage());
        getElement().appendChild(footer.getJqmFooter().getElement());
    }

    public JQMFooter getFooter() {
    	return footer != null ? footer.getJqmFooter() : null;
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
    public void setHeader(HasJqmHeader header) {
        removeHeader();
        this.header = header;
        if (this.header == null) return;

        addLogical(header.getHeaderStage());

        if (panel == null) {
        	getElement().insertBefore(header.getJqmHeader().getElement(), getElement().getFirstChild());
        } else {
        	getElement().insertAfter(header.getJqmHeader().getElement(), panel.getElement());
        }
    }

    public JQMHeader setHeader(String text) {
        JQMHeader header = new JQMHeader(text);
        header.setBackButton(true);
        setHeader(header);
        return header;
    }

    public JQMHeader getHeader() {
    	return header != null ? header.getJqmHeader() : null;
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
        JQMCommon.addStyleNames(content, value);
    }

    public boolean isContentCentered() {
        return contentCentered;
    }

    /**
     * Content band will be centered between Header and Footer
     * (they must be defined as fixed="true" OR page.pseudoFixedToolbars="true").
     */
    public void setContentCentered(boolean contentCentered) {
        boolean oldVal = this.contentCentered;
        this.contentCentered = contentCentered;
        if (oldVal != this.contentCentered && content != null && content.isAttached()) {
            if (this.contentCentered) {
                centerContent();
                initWindowResize();
            } else {
                clearCenterContent();
            }
        }
    }

    public boolean isPseudoFixedToolbars() {
        return pseudoFixedToolbars;
    }

    /**
     * If content is short or scrollable (see {@link JQMPage#setContentHeightPercent(double)}),
     * we can get "fixed" header and footer without using CSS position: fixed (it's not working
     * quite good in mobile browsers). Just setPseudoFixedToolbars(true) and footer's position will
     * be calculated to be at the bottom of the page.
     */
    public void setPseudoFixedToolbars(boolean value) {
        boolean oldVal = this.pseudoFixedToolbars;
        this.pseudoFixedToolbars = value;
        if (oldVal != this.pseudoFixedToolbars && content != null && content.isAttached()) {
            if (this.pseudoFixedToolbars) {
                centerContent();
                initWindowResize();
            } else {
                centerContent();
            }
        }
    }

    public double getContentHeightPercent() {
        return contentHeightPercent;
    }

    /**
     * Defines content band's height as percent of available content area's height.
     */
    public void setContentHeightPercent(double contentHeightPercent) {
        double oldVal = this.contentHeightPercent;
        this.contentHeightPercent = contentHeightPercent;
        if (oldVal != this.contentHeightPercent && content != null && content.isAttached()) {
            recalcContentHeightPercent();
            centerContent();
            initWindowResize();
        }
    }

    public double getHideFixedToolbarsIfContentAreaPercentBelow() {
        return hideFixedToolbarsIfContentAreaPercentBelow;
    }

    /**
     * Fixed Header and Footer will be hidden if content area height percent is below
     * than specified percent of window height (default is 50%).
     * <p/> Useful in cases with activated virtual keyboard, especially on Android.
     */
    public void setHideFixedToolbarsIfContentAreaPercentBelow(double value) {
        double oldVal = hideFixedToolbarsIfContentAreaPercentBelow;
        hideFixedToolbarsIfContentAreaPercentBelow = value;
        if (oldVal != hideFixedToolbarsIfContentAreaPercentBelow
                && content != null && content.isAttached()) {
            processFixedToolbars();
            centerContent();
            initWindowResize();
        }
    }

    public int getHideFixedToolbarsIfVirtualKeyboard() {
        return hideFixedToolbarsIfVirtualKeyboard;
    }

    /**
     * Fixed Header and Footer will be hidden if virtual/on-screen keyboard is activated.
     * <p/> This property is used as threshold(keyboard's height) in pixels
     * for keyboard detection (to work must be > 0, default is 0).
     */
    public void setHideFixedToolbarsIfVirtualKeyboard(int value) {
        int oldVal = hideFixedToolbarsIfVirtualKeyboard;
        hideFixedToolbarsIfVirtualKeyboard = value;
        if (oldVal != hideFixedToolbarsIfVirtualKeyboard
                && content != null && content.isAttached()) {
            processFixedToolbars();
            centerContent();
            initWindowResize();
            if (hideFixedToolbarsIfVirtualKeyboard > 0) initOrientationChange();
        }
    }

    private void showFixedToolbars() {
        final JQMHeader header = getHeader();
        if (header != null) {
            Element headerElt = header.getElement();
            if (headerElt.hasClassName(JQM4GWT_FIXED_HIDDEN)) {
                headerElt.removeClassName(JQM4GWT_FIXED_HIDDEN);
                if (hiddenHeaderFixed) {
                    headerElt.addClassName("ui-header-fixed");
                    Element pageElt = getElement();
                    pageElt.addClassName("ui-page-header-fixed");
                    header.setFixed(true);
                    header.updatePagePadding();
                }
            }
        }
        final JQMFooter footer = getFooter();
        if (footer != null) {
            Element footerElt = footer.getElement();
            if (footerElt.hasClassName(JQM4GWT_FIXED_HIDDEN)) {
                footerElt.removeClassName(JQM4GWT_FIXED_HIDDEN);
                if (hiddenFooterFixed) {
                    footerElt.addClassName("ui-footer-fixed");
                    Element pageElt = getElement();
                    pageElt.addClassName("ui-page-footer-fixed");
                    footer.setFixed(true);
                    footer.updatePagePadding();
                }
            }
        }
    }

    private void hideFixedToolbars(int headerH, int footerH) {
        final JQMHeader header = getHeader();
        if (header != null && (header.isFixed() || pseudoFixedToolbars)
                && !header.getElement().hasClassName(JQM4GWT_FIXED_HIDDEN)) {
            Element headerElt = header.getElement();
            if (header.isFixed()) {
                header.setFixed(false);
                headerElt.removeClassName("ui-header-fixed");
                hiddenHeaderFixed = true;
            } else {
                hiddenHeaderFixed = false;
            }
            headerElt.addClassName(JQM4GWT_FIXED_HIDDEN);
            hiddenHeaderH = headerH;
            if (hiddenHeaderFixed) {
                Element pageElt = getElement();
                pageElt.removeClassName("ui-page-header-fixed");
                pageElt.getStyle().clearPaddingTop();
            }
        }
        final JQMFooter footer = getFooter();
        if (footer != null && (footer.isFixed() || pseudoFixedToolbars)
                && !footer.getElement().hasClassName(JQM4GWT_FIXED_HIDDEN)) {
            Element footerElt = footer.getElement();
            if (footer.isFixed()) {
                footer.setFixed(false);
                footerElt.removeClassName("ui-footer-fixed");
                hiddenFooterFixed = true;
            } else {
                hiddenFooterFixed = false;
            }
            footerElt.addClassName(JQM4GWT_FIXED_HIDDEN);
            hiddenFooterH = footerH;
            if (hiddenFooterFixed) {
                Element pageElt = getElement();
                pageElt.removeClassName("ui-page-footer-fixed");
                pageElt.getStyle().clearPaddingBottom();
            }
        }
    }

    private boolean isFixedToolbarsHidden() {
        final JQMHeader header = getHeader();
        final JQMFooter footer = getFooter();
        return (header != null && header.getElement().hasClassName(JQM4GWT_FIXED_HIDDEN))
            || (footer != null && footer.getElement().hasClassName(JQM4GWT_FIXED_HIDDEN));
    }

    private void processFixedToolbars() {
        if (!isVisible() || header == null && footer == null) return;

        if (hideFixedToolbarsIfContentAreaPercentBelow <= 0 && hideFixedToolbarsIfVirtualKeyboard <= 0) {
            showFixedToolbars();
            return;
        }
        final int headerH;
        final int footerH;
        if (isFixedToolbarsHidden()) {
            headerH = hiddenHeaderH;
            footerH = hiddenFooterH;
        } else {
            final JQMHeader header = getHeader();
            headerH = header == null ? 0 : header.getOffsetHeight();
            final JQMFooter footer = getFooter();
            footerH = footer == null ? 0 : footer.getOffsetHeight();
        }
        int windowH = Window.getClientHeight();

        if (hideFixedToolbarsIfVirtualKeyboard > 0 &&
                initialWindowHeight - windowH >= hideFixedToolbarsIfVirtualKeyboard) {
            hideFixedToolbars(headerH, footerH);
            return;
        }
        if (hideFixedToolbarsIfContentAreaPercentBelow > 0) {
            int contentAreaH = windowH - headerH - footerH;
            if (contentAreaH <= 0) {
                hideFixedToolbars(headerH, footerH);
                return;
            }
            double minContentAreaH = windowH * 0.01d * hideFixedToolbarsIfContentAreaPercentBelow;
            if (contentAreaH < minContentAreaH) {
                hideFixedToolbars(headerH, footerH);
                return;
            }
        }
        showFixedToolbars();
    }

    /**
     * Forcefully centers (just once) page content (needed when content size is changed, because
     * there is no good way to get resize notification for DOM elements).
     * <p> Warning! - contentCentered property is not affected, you have to change it manually. </p>
     * <p> See <a href="http://stackoverflow.com/questions/3444719/how-to-know-when-an-dom-element-moves-or-is-resized">
     * How to know when an DOM element moves or is resized</a></p>
     */
    public void centerContent() {
        if (!isVisible()) return;

        Integer windowH = null;
        int headerH = 0;
        int footerH = 0;
        int contentH = 0;
        int marginTop = 0;

        final JQMHeader header = getHeader();
        final JQMFooter footer = getFooter();

        if (contentCentered) {
            boolean isFixedHeader = header != null && (header.isFixed() || pseudoFixedToolbars);
            boolean isFixedFooter = footer != null && (footer.isFixed() || pseudoFixedToolbars);
            boolean needCentering = (header == null && footer == null)
                    || (isFixedHeader && isFixedFooter)
                    || (isFixedHeader && footer == null) || (isFixedFooter && header == null);
            if (needCentering && !isFixedToolbarsHidden()) {
                contentH = content.getOffsetHeight();
                if (contentH > 0) {
                    if (header != null) headerH = header.getOffsetHeight();
                    if (footer != null) footerH = footer.getOffsetHeight();
                    windowH = Window.getClientHeight();
                    marginTop = (windowH - headerH - footerH - contentH) / 2;
                }
            }
        }
        if (marginTop <= 0) content.getElement().getStyle().clearMarginTop();
        else content.getElement().getStyle().setMarginTop(marginTop, Unit.PX);

        if (footer != null) {
            int footerMarginTop = 0;
            footerH = footer.getOffsetHeight();
            if (pseudoFixedToolbars && footerH > 0 && !footer.isFixed()) {
                if (windowH == null) {
                    windowH = Window.getClientHeight();
                    contentH = content.getOffsetHeight();
                    if (header != null) headerH = header.getOffsetHeight();
                }
                footerMarginTop = windowH - headerH - contentH - marginTop - footerH;
            }
            if (footerMarginTop <= 0) footer.getElement().getStyle().clearMarginTop();
            else footer.getElement().getStyle().setMarginTop(footerMarginTop, Unit.PX);
        }
    }

    /**
     * Forcefully recalculates (if defined, and just once) page content height
     * (needed when content area size is changed, because there is no good way to get resize
     * notification for DOM elements).
     */
    public void recalcContentHeightPercent() {
        if (!isVisible()) return;
        Element contentElt = content.getElement();
        if (contentHeightPercent > 0) {
            final JQMHeader header = getHeader();
            final JQMFooter footer = getFooter();
            int headerH = header == null || isFixedToolbarsHidden() ? 0 : header.getOffsetHeight();
            int footerH = footer == null || isFixedToolbarsHidden() ? 0 : footer.getOffsetHeight();
            int windowH = Window.getClientHeight();

            int clientH = contentElt.getPropertyInt("clientHeight");
            int offsetH = contentElt.getPropertyInt("offsetHeight");
            int diff = offsetH - clientH; // border, ...
            if (diff < 0) diff = 0;

            double h = (windowH - headerH - footerH - diff) * 0.01d * contentHeightPercent;
            h = Math.floor(h);
            contentElt.getStyle().setProperty("minHeight", String.valueOf(Math.round(h)) + "px");
            contentElt.getStyle().setProperty("paddingTop", "0");
            contentElt.getStyle().setProperty("paddingBottom", "0");
        } else {
            contentElt.getStyle().clearProperty("minHeight");
            contentElt.getStyle().clearProperty("paddingTop");
            contentElt.getStyle().clearProperty("paddingBottom");
        }
    }

    /**
     * Content is not centered anymore, i.e. aligned to the top (just once).
     * <p> Warning! - contentCentered property is not affected, you have to change it manually. </p>
     */
    public void clearCenterContent() {
        centerContent();
    }

    public boolean isDlgTransparent() {
        return transparent;
    }

    /**
     * @param transparent - needed when this page is shown in dialog mode,
     * then true means show faded previous page under dialog window
     * and don't bother prev page with lifecycle events (show, hide, ...).
     *
     * <p/> See <a href="http://tqcblog.com/2012/04/19/transparent-jquery-mobile-dialogs/">Transparent jQuery mobile dialogs</a>
     */
    public void setDlgTransparent(boolean transparent) {
        this.transparent = transparent;
        if (this.transparent) addStyleName(JQM4GWT_DLG_TRANSPARENT);
        else removeStyleName(JQM4GWT_DLG_TRANSPARENT);
    }

    public boolean isDlgTransparentDoPrevPageLifecycle() {
        return transparentDoPrevPageLifecycle;
    }

    /**
     * By default all lifecycle events (show, hide, ...) are blocked on previous page
     * when dlgTransparent is true for this page. This behavior can be disabled by setting
     * this property to true, so lifecycle events will be called for previous page.
     */
    public void setDlgTransparentDoPrevPageLifecycle(boolean transparentDoPrevPageLifecycle) {
        this.transparentDoPrevPageLifecycle = transparentDoPrevPageLifecycle;
    }

    /**
     * In case of non-transparent dialog its background theme can be changed.
     */
    public void setDlgOverlayTheme(String theme) {
        JQMCommon.setAttribute(this, "data-overlay-theme", theme);
    }

    public String getDlgOverlayTheme() {
        return JQMCommon.getAttribute(this, "data-overlay-theme");
    }

    /**
     * There is no "correct" way to restore page after it was called as dialog,
     * so this method is ugly hack, but it's useful and working.
     */
    public void restoreRolePage() {
        JQMCommon.setDataRole(this, "page");
        JQMCommon.setDataDialog(this, false);
        removeStyleName(STYLE_UI_DIALOG);
        Element elt = getElement();
        Element dlgContain = JQMCommon.findChild(elt, UI_DIALOG_CONTAIN);
        if (dlgContain != null) {
            JQMCommon.moveChildren(dlgContain, elt);
            elt.removeChild(dlgContain);
        }
        JQMHeader h = getHeader();
        if (h != null) {
            Element btn = JQMCommon.findChild(h.getElement(), "ui-btn-icon-notext");
            if (btn != null && "#".equals(JQMCommon.getAttribute(btn, "href"))
                    && (DataIcon.DELETE == JQMCommon.getIcon(btn)
                        || DataIcon.DELETE == JQMCommon.getStyleIcon(btn))) {
                h.getElement().removeChild(btn);
            }
        }
    }

    /**
     * There is no "correct" way to restore dialog after it was called as page,
     * so this method is ugly hack, but it's useful and working.
     */
    public void restoreRoleDialog() {
        JQMCommon.setDataRole(this, Mobile.DATA_ROLE_DIALOG);
        internRestoreDialog(getElement());
    }

    /** XXX: Again it's ugly hack, actually partial copy of mobile.dialog._create() method. */
    private static native void internRestoreDialog(Element elt) /*-{
        var p = $wnd.$(elt);
        if (p.data('mobile-page') === undefined || p.hasClass("ui-dialog")) {
            return;
        }
        var corners = p.page("option", "corners");
        p.addClass("ui-dialog").wrapInner($wnd.$( "<div/>", { "role" : "dialog",
                "class" : "ui-dialog-contain ui-overlay-shadow" + (corners ? " ui-corner-all" : "")
        }));
    }-*/;

    public void openDialog() {
        if (JQMCommon.isDataDialog(getElement())) {
            JQMContext.changePage(this);
        } else {
            JQMContext.changePage(this, true/*dialog*/);
        }
    }

    public void closeDialog() {
        if (isDialog()) {
            Mobile.back();
        } else if (Mobile.DATA_ROLE_DIALOG.equals(JQMCommon.getDataRole(this))) {
            internCloseDialog(getElement());
        }
    }

    private static native void internCloseDialog(Element elt) /*-{
        $wnd.$(elt).dialog("close");
    }-*/;

    public static enum DlgCloseBtn {
        RIGHT("right"), NONE("none"), LEFT("left");

        private final String jqmVal;

        private DlgCloseBtn(String jqmVal) {
            this.jqmVal = jqmVal;
        }

        /**
         * Returns the string value that JQM expects
         */
        public String getJqmValue() {
            return jqmVal;
        }

        public static DlgCloseBtn fromJqmValue(String jqmValue) {
            if (jqmValue == null || jqmValue.isEmpty()) return null;
            for (DlgCloseBtn i : DlgCloseBtn.values()) {
                if (i.getJqmValue().equals(jqmValue)) return i;
            }
            return null;
        }
    }

    public DlgCloseBtn getDlgCloseBtn() {
        return DlgCloseBtn.fromJqmValue(JQMCommon.getAttribute(this, "data-close-btn"));
    }

    public void setDlgCloseBtn(DlgCloseBtn value) {
        JQMCommon.setAttribute(this, "data-close-btn", value != null ? value.getJqmValue() : null);
    }

    public String getDlgCloseBtnText() {
        return JQMCommon.getAttribute(this, "data-close-btn-text");
    }

    public void setDlgCloseBtnText(String value) {
        JQMCommon.setAttribute(this, "data-close-btn-text", value);
    }

}
