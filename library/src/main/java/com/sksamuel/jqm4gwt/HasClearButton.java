package com.sksamuel.jqm4gwt;

/**
 * For widgets that can have clear button.
 */
public interface HasClearButton<T> {

    boolean isClearButton();

    void setClearButton(boolean value);

    T withClearButton(boolean value);
}
