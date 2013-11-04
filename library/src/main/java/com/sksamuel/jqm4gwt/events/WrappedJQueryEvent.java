package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;

public class WrappedJQueryEvent {

	public final JavaScriptObject jQueryEvent;

	public WrappedJQueryEvent(JavaScriptObject jQueryEvent) {
		this.jQueryEvent = jQueryEvent;
	}

	public JavaScriptObject getNative() {
		return jQueryEvent;
	}

	/**
	 * Returns the element that was the actual target of this event. It must
	 * usually be cast to another type using methods such as
	 * {@link Element#is(JavaScriptObject)} and
	 * {@link Element#as(JavaScriptObject)}.
	 * 
	 * @return the target element
	 */
	public final native EventTarget getEventTarget() /*-{
		return this.@com.sksamuel.jqm4gwt.events.WrappedJQueryEvent::jQueryEvent.target;
	}-*/;

	/**
	 * See jQuery documentation.
	 */
	public final native void stopPropagation() /*-{
		return this.@com.sksamuel.jqm4gwt.events.WrappedJQueryEvent::jQueryEvent.stopPropagation();
	}-*/;

	/**
	 * See jQuery documentation.
	 */
	public final native void stopImmediatePropagation() /*-{
		return this.@com.sksamuel.jqm4gwt.events.WrappedJQueryEvent::jQueryEvent.stopImmediatePropagation();
	}-*/;

	/**
	 * See jQuery documentation.
	 */
	public final native void preventDefault() /*-{
		return this.@com.sksamuel.jqm4gwt.events.WrappedJQueryEvent::jQueryEvent.preventDefault();
	}-*/;

}