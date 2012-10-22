jqm4gwt
=======

JQuery Mobile wrapper for GWT is a project that wraps the JQuery Mobile framework into a gwt widget library that can be used in a GWT application. This allows GWT developers to develop mobile web pages with all the benefits of jQuery Mobile without having to hand code javascript. In addition, projects that currently leverage GWT can add a mobile channel with ease.

Note that because this project is a wrapper into Java, you do not have to invoke javascript native methods, nor do you have to pass inline javascript to generic methods. You create widgets exactly as you do in the normal GWT widgetset. In fact you don't have to have any understanding of how jquery mobile works behind the scenes (although of course it helps if you do). A quick example to create a jquery mobile enhanced button is `JQMButton button = new JQMButton("Click me")`

The project mirrors the release cycle of JQuery Mobile. That is, when version x.y is released of JQM we try to then release version x.y of jqm4gwt to match. Bug fixes in our code are released as x.y.z.

Jqm4gwt also provides a useful form framework that enables rapid prototyping of form pages, including ajax submission and validation. See http://code.google.com/p/jqm4gwt/wiki/FormFramework

Please contribute with pull requests!

Examples
========

Examples and demos are hosted on a google app engine instance here: http://jqm4gwt.appspot.com/examples.html (This is quite out of date now - best off checking out the jqm4gwt-examples project and running locally).

These include the typical hello world and a show case of JQuery Mobile widgets and events.

What's Supported
========

The full JQuery Mobile widget set is supported. That is pages, dialogs, popups, buttons, form elements, list boxes, navigation, etc. Jqm4gwt goes further and supports standard GWT MVP constructs (Activities, Places, etc) and mixes that with the JQuery Mobile navigation system seemlessly.

Getting Started
========

Read the [quick start guide](https://github.com/sksamuel/jqm4gwt/wiki/Getting-Started). You will need to be familiar with GWT already.

Latest Release 1.2.0
========

Dependencies are available from maven central:

  	<dependency>
			<groupId>com.sksamuel.jqm4gwt</groupId>
			<artifactId>jqm4gwt</artifactId>
			<version>1.2.0</version>
  	</dependency>

Spread the word
================

If you like jqm4gwt then [cheer on ohloh](https://www.ohloh.net/stack_entries/new?project_id=jqm4gwt&ref=sample) ![ohloh logo](https://www.ohloh.net/images/stack/iusethis/static_logo.png)

Related Projects
================

If you are looking for a GWT wrapper for the desktop [jQuery UI](http://jqueryui.com) then [gwtquery-ui](http://code.google.com/p/gwtquery-ui/) is where to look.

For a GWT wrapper of JQuery then [gwtquery](http://code.google.com/p/gwtquery) is where to go.
