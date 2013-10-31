package com.sksamuel.jqm4gwt.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sksamuel.jqm4gwt.JQMContext;

/**
 * This class is responsible for registering and unregistering JQM events.
 * 
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public class JQMHandlerRegistration implements HandlerRegistration {

	private Element element;
	private String jqmEventName;
	private HandlerRegistration defaultRegistration;
	private JavaScriptObject handlerFunction;

	public JQMHandlerRegistration(Element element, String jqmEventName, HandlerRegistration defaultRegistration) {
		this.element = element;
		this.jqmEventName = jqmEventName;
		this.defaultRegistration = defaultRegistration;
		handlerFunction = JQMContext.addJQMEvent(element, jqmEventName);
	}

	@Override
	public void removeHandler() {
		defaultRegistration.removeHandler();
		JQMContext.removeJQMEventHandler(element, jqmEventName, handlerFunction);
	}

}