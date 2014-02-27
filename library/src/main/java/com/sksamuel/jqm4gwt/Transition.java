package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 May 2011 23:41:05
 *
 * Enum representing the different transitions methods available in JQM.
 *
 */
public enum Transition {
    FADE("fade"), POP("pop"), FLIP("flip"), TURN("turn"), FLOW("flow"),
    SLIDE_FADE("slidefade"), SLIDE("slide"), SLIDE_UP("slideup"), SLIDE_DOWN("slidedown"),
    NONE("none");

    private final String jqmValue;

    private Transition(String jqmValue) {
        this.jqmValue = jqmValue;
    }

    /** Returns the string value that JQM expects */
    public String getJQMValue() {
        return jqmValue;
    }

}
