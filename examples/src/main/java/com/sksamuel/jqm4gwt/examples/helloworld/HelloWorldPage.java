package com.sksamuel.jqm4gwt.examples.helloworld;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMPage;
import com.sksamuel.jqm4gwt.button.JQMButton;
import com.sksamuel.jqm4gwt.form.elements.JQMCheckbox;
import com.sksamuel.jqm4gwt.html.Paragraph;
import com.sksamuel.jqm4gwt.toolbar.JQMFooter;
import com.sksamuel.jqm4gwt.toolbar.JQMHeader;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 18:00:50
 *
 *         A starter example that shows header, footer and some text saying
 *         "hello world"
 *
 */
public class HelloWorldPage extends JQMPage {

    private int counter = 1;

    public HelloWorldPage() {
        super("helloworld");

        JQMHeader h = new JQMHeader("Hello world header");
        add(h);
        h.setBackButton(true);

        add(new Paragraph("Hello world. Boy am I original!"));

        JQMButton btn = new JQMButton("Add CheckBox");
        btn.setMini(true);
        btn.setInline(true);
        btn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                final String s = "Checkbox" + String.valueOf(counter++);
                JQMCheckbox check = new JQMCheckbox(s, s);
                check.setId(s);
                check.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> event) {
                        Window.alert(s + " onValueChange() fired, value is " + event.getValue());
                    }
                });
                add(check);
                JQMContext.render(HelloWorldPage.this.getElement().getId());
            }
        });
        add(btn);

        JQMFooter f = new JQMFooter("Hello world footer");
        add(f);
    }

}
