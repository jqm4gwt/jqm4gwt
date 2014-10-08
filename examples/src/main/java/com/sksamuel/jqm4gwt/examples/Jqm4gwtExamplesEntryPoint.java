package com.sksamuel.jqm4gwt.examples;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.ScriptUtils;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.examples.collapsible.DynamicCollapsibleDemoPage;
import com.sksamuel.jqm4gwt.examples.dynotable.DynamicTableDemoPage;
import com.sksamuel.jqm4gwt.examples.events.EventsDemoPage;
import com.sksamuel.jqm4gwt.examples.helloworld.HelloWorldPage;
import com.sksamuel.jqm4gwt.examples.lists.ListViewDemoPage;
import com.sksamuel.jqm4gwt.examples.showcase.FormShowcasePage;
import com.sksamuel.jqm4gwt.examples.transition.TransitionDemoPage;
import com.sksamuel.jqm4gwt.examples.uibinder.TestPanel1;
import com.sksamuel.jqm4gwt.examples.uibinder.TestView1;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jqm4gwtExamplesEntryPoint implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
    @Override
	public void onModuleLoad() {
        ScriptUtils.waitJqmLoaded(new Callback<Void, Throwable>() {

            @Override
            public void onSuccess(Void result) {
                runExamples();
            }

            @Override
            public void onFailure(Throwable reason) {
                Window.alert(reason.getMessage());
            }
        });
	}

    private static void runExamples() {
        JQMContext.disableHashListening();

        JQMPage page = new JQMPage("examples");

        page.add(new JQMHeader("Examples"));

        page.add(new Paragraph("Welcome to the jqm4gwt examples and demo gallery. Choose a demo below to show. "
                + "All these demos were built using the jqm4gwt project."));
        page.add(new Paragraph("Don't just test on a browser, test in your mobile or tablet device!"));
        page.add(new JQMButton("Helloworld", new HelloWorldPage()));
        page.add(new JQMButton("Transitions", new TransitionDemoPage("tdemo")));
        page.add(new JQMButton("Form showcase", new FormShowcasePage()));
        page.add(new JQMButton("Events", new EventsDemoPage()));
        page.add(new JQMButton("Advanced Lists", new ListViewDemoPage()));
        page.add(new JQMButton("Dynamic Table", new DynamicTableDemoPage()));
        page.add(new JQMButton("Dynamic Collapsible", new DynamicCollapsibleDemoPage()));
        page.add(new JQMButton("Popups", new PopupExamplePage()));
        JQMButton btn = new JQMButton("UiBinder - All Widgets", TestView1.createPage());
        btn.getElement().getStyle().setColor("#ffa200");
        page.add(btn);

        page.add(new JQMFooter("jqm4gwt open source project"));

        @SuppressWarnings("unused")
        TestPanel1 pa = new TestPanel1();

        JQMContext.changePage(page);
    }
}
