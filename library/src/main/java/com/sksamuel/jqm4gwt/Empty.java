package com.sksamuel.jqm4gwt;

import java.util.Collection;
import java.util.Map;

/**
 * Null safe empty check for different data types.
 */
public class Empty {

    private Empty() {} // static class

    public static boolean is(String s) {
        return s == null || s.length() == 0; // optimize like Guava: s.isEmpty() -> s.length() == 0
    }

    public static boolean is(Collection<?> collect) {
        return collect == null || collect.isEmpty();
    }

    public static boolean is(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * @return - if value is null returns replaceWithIfNull, otherwise just value.
     */
    public static <T> T nvl(T value, T replaceWithIfNull) {
        return value != null ? value : replaceWithIfNull;
    }

    /** The same as {@link Empty#nvl(Object, Object)} */
    public static <T> T nonNull(T value, T replaceWithIfNull) {
        return value != null ? value : replaceWithIfNull;
    }

    /**
     * @return - similar to nvl(), but returns replaceWithIfEmpty in case of null and empty,
     *           otherwise just value.
     */
    public static String nonEmpty(String value, String replaceWithIfEmpty) {
        return Empty.is(value) ? replaceWithIfEmpty : value;
    }

    public static <T> Collection<T> nonEmpty(Collection<T> value, Collection<T> replaceWithIfEmpty) {
        return Empty.is(value) ? replaceWithIfEmpty : value;
    }

    public static <K, V> Map<K, V> nonEmpty(Map<K, V> value, Map<K, V> replaceWithIfEmpty) {
        return Empty.is(value) ? replaceWithIfEmpty : value;
    }
}
