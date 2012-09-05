package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.sksamuel.jqm4gwt.DataIcon;
import com.sksamuel.jqm4gwt.HasIcon;
import com.sksamuel.jqm4gwt.HasInline;
import com.sksamuel.jqm4gwt.HasNative;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.html.FormLabel;

/**
 * @author Stephen K Samuel samspade79@gmail.com 5 May 2011 10:58:58
 * 
 *         An implementation of a jQuery mobile select element.
 * 
 * @link http://jquerymobile.com/demos/1.1.1/docs/forms/selects/options.html
 * 
 */
public class JQMSelect extends JQMWidget implements HasNative, HasText, HasFocusHandlers, HasChangeHandlers, HasClickHandlers,
		HasValue<String>, JQMFormWidget, HasIcon, HasInline, HasPreventFocusZoom {

	private static final String	SELECT_STYLENAME	= "jqm4gwt-select";

	private final ListBox		select;

	private final FormLabel		label;

	/**
	 * The panel that will contain the labe and select widgets
	 */
	private final FlowPanel		flow;

	/**
	 * Creates a new {@link JQMSelect} with no label text.
	 */
	public JQMSelect() {
		this(null);
	}

	/**
	 * Creates a new {@link JQMSelect}with the given label text.
	 */
	public JQMSelect(String text) {
		String id = Document.get().createUniqueId();

		flow = new FlowPanel();
		initWidget(flow);

		label = new FormLabel();
		label.setFor(id);
		flow.add(label);

		select = new ListBox(false);
		select.getElement().setId(id);
		flow.add(select);

		setText(text);
		setStyleName(SELECT_STYLENAME);
		setDataRole("fieldcontain");
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return select.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return select.addChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return select.addClickHandler(handler);
	}

	@Override
	public Label addErrorLabel() {
		return null;
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler, FocusEvent.getType());
	}

	/**
	 * Adds an option with the given text. The text is also used as the value.
	 * The option is added at the end of the list of options.
	 * 
	 * If you want to specify a value diferent from the display text, then
	 * invoke addOption(String, String).
	 */
	public void addOption(String text) {
		addOption(text, text);
	}

	/**
	 * Adds an option with the given value and text. The option is added at
	 * the end of the list of options.
	 */
	public void addOption(String value, String text) {
		select.addItem(text, value);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Programatically close an open select menu
	 */
	public void close() {
		close(select.getElement().getId());
	}

	private native void close(String id) /*-{
								$wnd.$("#" + id).selectmenu('close');
								}-*/;

	@Override
	public IconPos getIconPos() {
		String string = getAttribute("data-iconpos");
		return string == null ? null : IconPos.valueOf(string);
	}

	/**
	 * Returns the index of the currently selected option
	 */
	public int getSelectedIndex() {
		return getSelectedIndex(select.getElement().getId());
	}

	private native int getSelectedIndex(String id) /*-{
									var select = $wnd.$("select#" + id);
									return select[0].selectedIndex;
									}-*/;

	public String getSelectedValue() {
		return getValue();
	}

	@Override
	public String getText() {
		return label.getText();
	}

	/**
	 * Returns the currently selected value
	 */
	@Override
	public String getValue() {
		return getValue(getSelectedIndex());
	}

	/**
	 * Returns the value at the given index
	 */
	public String getValue(int index) {
		return select.getValue(index);
	}

	/**
	 * Returns the index of the first option that matches the given value or
	 * -1 if no such option exists.
	 */
	public int indexOf(String value) {
		for (int k = 0; k < select.getItemCount(); k++) {
			if (value.equals(select.getValue(k)))
				return k;
		}
		return -1;
	}

	public boolean isCorners() {
		return "true".equals(getAttribute("data-corners"));
	}

	public boolean isIconShadow() {
		return "true".equals(getAttribute("data-iconshadow"));
	}

	@Override
	public boolean isInline() {
		return "true".equals(select.getElement().getAttribute("data-inline"));
	}

	public boolean isMultiple() {
		return "true".equals(select.getElement().getAttribute("multiple"));
	}

	/**
	 * Returns true if the select is set to render in native mode.
	 */
	@Override
	public boolean isNative() {
		// an element is native by default. So this returns true if native
		// is not set to false.
		return !"false".equals(select.getElement().getAttribute("data-native-menu"));
	}

	@Override
	public boolean isPreventFocusZoom() {
		return "true".equals(select.getElement().getAttribute("data-prevent-focus-zoom"));
	}

	public boolean isShadow() {
		return "true".equals(getAttribute("data-shadow"));
	}

	/**
	 * Programatically open a select menu
	 */
	public void open() {
		close(select.getElement().getId());
	}

	private native void open(String id) /*-{
							$wnd.$("#" + id).selectmenu('open');
							}-*/;

	/**
	 * Refreshes the select after a programatic change has taken place.
	 */
	public void refresh() {
		refresh(select.getElement().getId());
	}

	private native void refresh(String id) /*-{
								var select = $wnd.$("select#" + id);
								select.selectmenu("refresh");
								}-*/;

	@Override
	public void removeIcon() {
		getElement().removeAttribute("data-icon");
	}

	/**
	 * Remove the first option that matches the given value
	 */
	public void removeOption(String value) {
		int indexOf = indexOf(value);
		if (indexOf >= 0)
			select.removeItem(indexOf);
	}

	public void setCorners(boolean corners) {
		setAttribute("data-corners", String.valueOf(corners));
	}

	/**
	 * Sets the icon used by this button. See {@link DataIcon}.
	 */
	@Override
	public void setIcon(DataIcon icon) {
		if (icon == null)
			removeIcon();
		else
			getElement().setAttribute("data-icon", icon.getJqmValue());
	}

	@Override
	public void setIcon(String src) {
		if (src == null)
			removeIcon();
		else
			getElement().setAttribute("data-icon", src);
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
	 * Applies the theme shadow to the select button if set to true.
	 */
	public void setIconShadow(boolean iconShadow) {
		setAttribute("data-iconshadow", String.valueOf(iconShadow));
	}

	@Override
	public void setInline(boolean inline) {
		if (inline)
			getElement().setAttribute("data-inline", "true");
		else
			getElement().removeAttribute("data-inline");
	}

	/**
	 * Sets this select to allow mulitple selections.
	 */
	public void setMulitple(boolean m) {
		if (m)
			getElement().setAttribute("multiple", "true");
		else
			getElement().removeAttribute("multiple");
	}

	@Override
	public void setNative(boolean b) {
		if (b)
			getElement().removeAttribute("data-menu-native");
		else
			getElement().setAttribute("data-menu-native", "false");
	}

	/**
	 * Set the placeholder option value.
	 * 
	 * It's common for developers to include a "null" option in their select
	 * element to force a user to choose an option. If a placeholder option is
	 * present in your markup, jQuery Mobile will hide them in the overlay
	 * menu, showing only valid choices to the user, and display the
	 * placeholder text inside the menu as a header.
	 */
	public void setPlaceholder(String placeholder) {
		addOption(null, placeholder);
	}

	/**
	*This option disables page zoom temporarily when a custom select is focused, which prevents iOS devices from zooming the page into the select. 
	*By default, iOS often zooms into form controls, and the behavior is often unnecessary and intrusive in mobile-optimized layouts.
	*/
	@Override
	public void setPreventFocusZoom(boolean b) {
		setAttribute("data-prevent-focus-zoom", String.valueOf(b));
	}

	/**
	 * Change the selection to the option at the given index.
	 */
	public void setSelectedIndex(int index) {
		select.setSelectedIndex(index);
	}

	private native void setSelectedIndex(String id, int index) /*-{
											$wnd.$("#" + id).attr('selectedIndex', index);
											var select = $wnd.$("select#" + id);
											select.selectedIndex = index;
											select.selectmenu("refresh");
											}-*/;

	/**
	 * 
	 */
	public void setSelectedValue(String value) {
		for (int k = 0; k < select.getItemCount(); k++) {
			if (select.getValue(k).equalsIgnoreCase(value)) {
				select.setSelectedIndex(k);
				return;
			}
		}
	}

	/**
	 * Applies the drop shadow style to the select button if set to true.
	 */
	public void setShadow(boolean shadow) {
		setAttribute("data-shadow", String.valueOf(shadow));
	}

	/**
	 * Set the text of the label element.
	 */
	@Override
	public void setText(String text) {
		label.setText(text);
	}

	/**
	 * Sets the selected value to the given value. If no option matches the
	 * given value then the selected is removed.
	 */
	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	/**
	 * Sets the selected value to the given value. If no option matches the
	 * given value then the selected is removed.
	 * 
	 * The fireEvents param is ignored.
	 */
	@Override
	public void setValue(String value, boolean ignored) {
		int indexOf = indexOf(value);
		if (indexOf >= 0)
			setSelectedIndex(indexOf);
	}

}
