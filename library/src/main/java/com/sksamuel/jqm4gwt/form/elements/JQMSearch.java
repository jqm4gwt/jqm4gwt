package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.events.JQMChangeHandler;
import com.sksamuel.jqm4gwt.events.JQMComponentEvents;
import com.sksamuel.jqm4gwt.events.JQMEvent;
import com.sksamuel.jqm4gwt.events.JQMEventFactory;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration;
import com.sksamuel.jqm4gwt.events.JQMHandlerRegistration.WidgetHandlerCounter;
import com.sksamuel.jqm4gwt.events.JQMInputHandler;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 *
 * <p/> Text element stylised as a search box.
 *
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/forms/#search">Search Input</a>
 *
 */
public class JQMSearch extends JQMText {

	/**
	 * Create a new {@link JQMSearch} with no label text
	 */
	public JQMSearch() {
		this(null);
	}

	/**
	 * Create a new {@link JQMSearch} with the given label text
	 *
	 * @param text - the text to use as the label
	 */
	public JQMSearch(String text) {
		super(text);
		setType("search");
		initChangeHandler();
	}

	@Override
    public String getValue() {
        return JQMCommon.getVal(getInputId());
    }

	@Override
    public void setValue(String value, boolean fireEvents) {
	    JQMCommon.setVal(getInputId(), value);
	    if (fireEvents) DomEvent.fireNativeEvent(Document.get().createChangeEvent(), input);
	}

	/**
	 * Standard GWT ChangeEvent is not working for search input, that's why we have to activate
	 * it manually by listening jQuery change event.
	 */
	private void initChangeHandler() {
	    JQMChangeHandler handler = new JQMChangeHandler() {
            @Override
            public void onEvent(JQMEvent<?> event) {
                DomEvent.fireNativeEvent(Document.get().createChangeEvent(), input);
            }};
	    JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(GwtEvent.Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, JQMComponentEvents.CHANGE,
        JQMEventFactory.getType(JQMComponentEvents.CHANGE, JQMChangeHandler.class));
	}

	/**
     * Occurs on every entered/deleted symbol.
     * <p/><b>Warning!</b> Clear button does not raise this event, use
     * addValueChangeHandler() to react on it.
	 */
	public HandlerRegistration addInputHandler(JQMInputHandler handler) {
	    if (handler == null) return null;
	    return JQMHandlerRegistration.registerJQueryHandler(new WidgetHandlerCounter() {
            @Override
            public int getHandlerCountForWidget(GwtEvent.Type<?> type) {
                return getHandlerCount(type);
            }
        }, this, handler, JQMComponentEvents.INPUT,
        JQMEventFactory.getType(JQMComponentEvents.INPUT, JQMInputHandler.class));
	}

}
