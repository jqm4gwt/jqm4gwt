jqm4gwt   [![Build Status](https://travis-ci.org/jqm4gwt/jqm4gwt.png)](https://travis-ci.org/jqm4gwt/jqm4gwt)
=======

JQuery Mobile wrapper for GWT is a project that wraps the JQuery Mobile framework into a gwt widget library
that can be used in a GWT application. This allows GWT developers to develop mobile web apps with and leverage
the vibrant jQuery Mobile widgets community without having to hand code javascript. In addition, projects that
currently leverage GWT can add a mobile channel with ease.

Note that because this project is a wrapper into Java, you do not have to invoke javascript native methods,
nor do you have to pass inline javascript to generic methods. You create widgets exactly as you do in the
normal GWT widgetset. In fact you don't have to have any understanding of how jquery mobile works behind
the scenes (although of course it helps if you do).

A quick example to create a jquery mobile enhanced button is: 
```java
JQMButton button = new JQMButton("Click me");
```
or the same as UiBinder template:
```xml
<ui:UiBinder xmlns:b="urn:import:com.sksamuel.jqm4gwt.button">
<b:JQMButton text="Click me" />
```

The project mirrors the release cycle of JQuery Mobile.
That is, when version x.y is released of JQM we try to then release version x.y of jqm4gwt to match.
Bug fixes in our code are released as x.y.z.

There is [full support for GWT's UiBinder](https://github.com/jqm4gwt/jqm4gwt/wiki/Using-jqm4gwt-with-UiBinder),
and a self-contained version of the module providing all of the dependent JQueryMobile JavaScript, CSS, and images.

Jqm4gwt also provides a useful form framework that enables rapid prototyping of form pages, including ajax submission and validation.
See http://code.google.com/p/jqm4gwt/wiki/FormFramework

For more information, see the [Frequently Asked Questions] (https://github.com/sksamuel/jqm4gwt/wiki/Frequently-Asked-Questions).

Please contribute with pull requests! See the [Contributors Guidelines] (https://github.com/sksamuel/jqm4gwt/wiki/Contributors-Guidelines) to assure the health of the project.

Examples
========

[Examples and demos are hosted on a google app engine instance](http://jqm4gwt.appspot.com/examples.html)

These include the typical hello world and a show case of JQuery Mobile widgets and events.

What's Supported
========

The full JQuery Mobile widget set is supported. That is pages, dialogs, popups, buttons, form elements, list boxes, navigation, etc.
Jqm4gwt goes further and supports standard GWT MVP constructs (Activities, Places, etc) and mixes that with the JQuery Mobile navigation system seemlessly.
JQueryMobile is exposed to GWT users via both a simple [Fluent-style](http://en.wikipedia.org/wiki/Fluent_interface) API and via
[GWT's UiBinder](https://developers.google.com/web-toolkit/doc/latest/DevGuideUiBinder).

Getting Started
========

Read the [quick start guide](https://github.com/jqm4gwt/jqm4gwt/wiki/Getting-Started). You will need to be familiar with GWT already.

Getting It
========

Latest Release: 1.4.3.Final released 28-July-2014
 * [jqm4gwt Remote Module] (http://search.maven.org/remotecontent?filepath=com/sksamuel/jqm4gwt/jqm4gwt-remote/1.4.3.Final/jqm4gwt-remote-1.4.3.Final.jar)
 * [jqm4gwt Library Module] (http://search.maven.org/remotecontent?filepath=com/sksamuel/jqm4gwt/jqm4gwt-library/1.4.3.Final/jqm4gwt-library-1.4.3.Final.jar)
 * [Remote Sources] (http://search.maven.org/remotecontent?filepath=com/sksamuel/jqm4gwt/jqm4gwt-remote/1.4.3.Final/jqm4gwt-remote-1.4.3.Final-sources.jar)
 * [Library Sources] (http://search.maven.org/remotecontent?filepath=com/sksamuel/jqm4gwt/jqm4gwt-library/1.4.3.Final/jqm4gwt-library-1.4.3.Final-sources.jar)

Dependencies are available from Maven Central:

```xml
<dependency>
  <groupId>com.sksamuel.jqm4gwt</groupId>
  <artifactId>jqm4gwt-remote</artifactId>
  <version>1.4.3.Final</version>
</dependency>
```

Spread the word and join the conversation!
================

If you like jqm4gwt then [cheer on ohloh](https://www.ohloh.net/stack_entries/new?project_id=jqm4gwt&ref=sample)
[![ohloh logo](https://www.ohloh.net/images/stack/iusethis/static_logo.png)](https://www.ohloh.net/stack_entries/new?project_id=jqm4gwt&ref=sample)

Want to talk about it? Join the [discussion group for jqm4gwt on Google Groups](https://groups.google.com/forum/?fromgroups#!forum/jqm4gwt).

Related Projects
================

If you are looking for a GWT wrapper for the desktop [jQuery UI](http://jqueryui.com) 
then [gwtquery-ui](http://code.google.com/p/gwtquery-ui/) 
or [koobe/gwtquery-ui](https://github.com/koobe/gwtquery-ui) is where to look.
For a GWT wrapper of JQuery then [gwtquery](http://code.google.com/p/gwtquery) is where to go.


## License
```
This software is licensed under the Apache 2 license, quoted below.

Copyright 2014 Stephen Samuel

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```
