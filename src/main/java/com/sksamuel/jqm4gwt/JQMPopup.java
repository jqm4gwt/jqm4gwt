package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 16 Sep 2012 00:26:35
 *
 */
public class JQMPopup extends JQMContainer implements HasId, HasTransition, HasTheme {

	private static int	counter	= 1;

	/**
	 * Creates a new {@link JQMPopup} with an automatically assigned id in the form popup-xx where XX is an incrementing number.
	 */
	public JQMPopup() {
		this("popup-" + (counter++));
	}

	public JQMPopup(String id) {
		super(id, "popup");
	}

	private native void _close(String id) /*-{
								$('#' + id).popup("close")
								}-*/;

	private native void _open(String id) /*-{
								$('#' + id).popup("open")
								}-*/;

	public void close() {
		_close(id);
	}

	public void open() {
		_open(id);
	}

	@Override
	public String toString() {
		return "JQMPopup [id=" + id + "]";
	}

	public void setPadding(boolean padding) {
		if (padding) {
			addStyleName("ui-content");
		} else {
			removeStyleName("ui-content");
		}
	}

	public void setPosition(String pos) {
		if (!pos.startsWith("#") && !pos.equals("window") && !pos.equals("origin"))
			throw new IllegalArgumentException("Position must be origin, window, or an id selector");
		getElement().setAttribute("data-position-to", pos);
	}

}
