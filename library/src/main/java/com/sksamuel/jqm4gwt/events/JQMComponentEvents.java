package com.sksamuel.jqm4gwt.events;

/**
 * Constants that map to JQM component events.
 * See {@link http://api.jquerymobile.com/category/events/}
 *
 * @author Andrei Costescu costescuandrei@gmail.com 31 Oct 2013
 */
public interface JQMComponentEvents {

	// TODO: add all other JQM component event types & implement them (currently only TAP_EVENT
    // is implemented, TAP_HOLD_EVENT is not yet made available on components, but is implemented);
    // ! Not sure we should add all events to components, it's highly questionable.
	// Maybe we should add all these events to JQMWidget directly, although some might not make
    // sense for all widgets - instead of doing it like GWT does with one interface for each event
    // type depending on component (JQM documentation doesn't say for which component type each one is meant anyway)

	String TAP_EVENT = "tap";
	String TAP_HOLD_EVENT = "taphold";
	String VCLICK = "vclick";
	String CHANGE = "change";
	String INPUT = "input";
	String ORIENTATIONCHANGE = "orientationchange";

}
