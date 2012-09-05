package com.sksamuel.jqm4gwt.toolbar;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.Transition;
import com.sksamuel.jqm4gwt.button.JQMButton;

public class JQMToolBarButton extends JQMButton
{
	public JQMToolBarButton(String text) {
		super(text);
		setId();
	}
	public JQMToolBarButton(String text, String url) {
		super(text,url);
	}
	public JQMToolBarButton(String text, JQMPage page) {
		super(text, page);
	}
	public JQMToolBarButton(String text, String url, Transition t) {
		super(text, url, t);
	}
	public JQMToolBarButton(String text, JQMPage page, Transition t) {
		super(text, page, t);
	}



	private ClickHandler handler;
	private DelegatingHandlerRegistration hr;
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) 
	{
		this.handler = handler;
		hr = new DelegatingHandlerRegistration(super.addClickHandler(handler));
		return hr;
	}
	void doFixHandlers() 
	{
		a = Anchor.wrap(Document.get().getElementById(getId()));
		hr.setDelegate(a.addClickHandler(handler));
	}
	private static class DelegatingHandlerRegistration implements HandlerRegistration
	{
		HandlerRegistration delegateHandler;
		public DelegatingHandlerRegistration(HandlerRegistration handler) 
		{
			delegateHandler = handler;
		}
		public void setDelegate(HandlerRegistration handler) 
		{
			delegateHandler = handler;
		}
		@Override
		public void removeHandler() 
		{
			delegateHandler.removeHandler();
		}
	}
}
