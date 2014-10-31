package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 May 2011 23:41:05
 *
 * <p/> Enum representing the different transitions methods available in JQM.
 * <p/> See <a href="http://demos.jquerymobile.com/1.4.5/transitions/">Transitions</a>
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
    public String getJqmValue() {
        return jqmValue;
    }

    public static Transition fromJqmValue(String jqmValue) {
        if (jqmValue == null || jqmValue.isEmpty()) return null;
        for (Transition i : Transition.values()) {
            if (i.getJqmValue().equals(jqmValue)) return i;
        }
        return null;
    }

}
