package com.sksamuel.jqm4gwt.events;

/**
 * Handler interface for {@link TapEvent} events.<br><br>
 * 
 * This event's handler will make a transition to the next page; {@link WrappedJQueryEvent#stopImmediatePropagation()}/{@link WrappedJQueryEvent#preventDefault()}
 * will be called on the event object.
 * Otherwise you will risk an unwanted tap on the widget at that same location on the next page on some browsers/devices.<br><br>
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 *
 */
public abstract class TapHandlerForPageSwitch implements TapHandler {

	@Override
	public void onTap(TapEvent event) {
		// without the following two lines, in Android 4.3/Phonegap 3.0.0 for example when a "tap" event would trigger a Page change,
		// the tap would be wrongly executed twice (once more on whatever is at the same location in next page);
		// they are also needed in case multiple same-type JQM events are added to the same element as GWT will dispatch to all handlers anyway
		event.getJQueryEvent().stopImmediatePropagation();
		event.getJQueryEvent().preventDefault();
		
		onSafeTap(event);
	}

	public abstract void onSafeTap(TapEvent event);

}
