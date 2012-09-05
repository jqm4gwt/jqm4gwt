package com.sksamuel.jqm4gwt.layout;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasIconPos;
import com.sksamuel.jqm4gwt.HasMini;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.html.Heading;

/**
 * @author Stephen K Samuel samspade79@gmail.com 10 May 2011 00:04:18
 * 
 *         A {@link JQMCollapsible} is a panel that shows a header and can
 *         reveal content once the header is expanded. This is similar to the
 *         GWT {@link DisclosurePanel}.
 * 
 * @link http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/content/content-
 *       collapsible.html
 * 
 */
public class JQMCollapsible extends JQMWidget implements HasText, HasIconPos, HasMini {

	/**
	 * The container for the elements of the collapsible.
	 */
	private final FlowPanel	flow;

	private final Heading	header;

	/**
	 * Creates a new {@link JQMCollapsible} with the given header text and
	 * preset to collapsed.
	 */
	public JQMCollapsible(String text) {
		this(text, true);
	}

	/**
	 * Creates a new {@link JQMCollapsible} with the given header text and
	 * collapsed if @param collapsed is true.
	 * 
	 * The created header will use a h3 element.
	 * 
	 * @param collapsed
	 *              if true then the {@link JQMCollapsible} will be collapsed
	 *              by default, if false it will be open by default
	 */
	public JQMCollapsible(String text, boolean collapsed) {
		this(text, 3, collapsed);
	}

	/**
	 * Creates a new {@link JQMCollapsible} with the given header text and
	 * collapsed if @param collapsed is true.
	 * 
	 * The created header will use a <hN> element where N is determined by the @param
	 * headerN.
	 * 
	 * Once the {@link JQMCollapsible} has been created it is not possible to
	 * change the <hN> tag used for the header.
	 */
	public JQMCollapsible(String text, int headerN, boolean collapsed) {
		flow = new FlowPanel();
		initWidget(flow);

		header = new Heading(headerN);
		flow.add(header);

		setDataRole("collapsible");
		setCollapsed(collapsed);
		setText(text);
	}

	/**
	 * Add a widget to the content part of this {@link JQMCollapsible}
	 * instance
	 */
	public void add(Widget widget) {
		flow.add(widget);
	}

	/**
	 * Removes all Widgets from the content part of this
	 * {@link JQMCollapsible} instance.
	 */
	public void clear() {
		flow.clear();
	}

	@Override
	public IconPos getIconPos() {
		String string = getAttribute("data-iconpos");
		return string == null ? null : IconPos.valueOf(string);
	}

	/**
	 * Returns the text on the header element
	 */
	@Override
	public String getText() {
		return header.getText();
	}

	/**
	 * Returns true if this {@link JQMCollapsible} is currently collapsed.
	 */
	public boolean isCollapsed() {
		return "true".equals(getElement().getAttribute("data-collapsed"));
	}

	@Override
	public boolean isMini() {
		return "true".equals(getAttribute("data-mini"));
	}

	/**
	 * Removes a widget from the content part of this {@link JQMCollapsible}
	 * instance.
	 * 
	 * @return true if the widget was removed
	 */
	public boolean remove(Widget widget) {
		return flow.remove(widget);
	}

	/**
	 * Programatically set the collapsed state of this widget.
	 */
	public void setCollapsed(boolean collapsed) {
		if (collapsed)
			getElement().setAttribute("data-collapsed", "true");
		else
			getElement().removeAttribute("data-collapsed");
	}

	/**
	 * Sets the position of the icon. If you desire an icon only button then
	 * set the position to {@link IconPos.NOTEXT}
	 */
	@Override
	public void setIconPos(IconPos pos) {
		if (pos == null)
			getElement().removeAttribute("data-iconpos");
		else
			getElement().setAttribute("data-iconpos", pos.getJqmValue());
	}

	/**
	 * If set to true then renders a smaller version of the standard-sized element.
	 */
	@Override
	public void setMini(boolean mini) {
		setAttribute("data-mini", String.valueOf(mini));
	}

	/**
	 * Sets the text on the header element
	 */
	@Override
	public void setText(String text) {
		header.setText(text);
	}
}
