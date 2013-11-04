package com.sksamuel.jqm4gwt.form.elements;

public class JQMFile extends JQMText {

    public JQMFile() {
        this(null);
    }

    public JQMFile(String text) {
        super(text);
        setType("file");
    }

}
