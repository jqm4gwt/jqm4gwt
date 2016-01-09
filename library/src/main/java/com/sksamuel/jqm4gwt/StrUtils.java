package com.sksamuel.jqm4gwt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StrUtils {

    private StrUtils() {} // static class

    // See http://stackoverflow.com/a/2709855
    //private static final String COMMA_SPLIT = "(?<!\\\\),";
    //private static final String BACKSLASH_COMMA = "\\\\,";

    /** Splits by commas, backslash commas are ignored(preserved). */
    public static List<String> commaSplit(String s) {
        return split(s, ",");
    }

    /** Splits string by separator, if separator in string is backslashed, i.e. prefixed by \ then
     *  it's ignored(preserved).
     **/
    public static List<String> split(String s, String separator) {
        if (s == null) return null;
        List<String> rslt = null;
        if (s.isEmpty() || isEmpty(separator)) {
            rslt = new ArrayList<>(1);
            rslt.add(s);
            return rslt;
        }

        //return s.split(COMMA_SPLIT); - NOT WORKING when compiled to JS

        int start = 0;
        int p = start;
        do {
            int j = s.indexOf(separator, p);
            if (j == -1) {
                if (rslt == null) {
                    rslt = new ArrayList<>(1);
                    rslt.add(s);
                    return rslt;
                }
                rslt.add(s.substring(start));
                break;
            }
            if (j > 0 && s.charAt(j - 1) == '\\') {
                p = j + 1;
                continue;
            } else {
                if (rslt == null) rslt = new ArrayList<String>();
                rslt.add(s.substring(start, j));
                start = j + 1;
                p = start;
            }

        } while (true);

        return rslt;
    }

    public static String replaceAllBackslashCommas(String s) {
        return replaceAllBackslashSeparators(s, ",");
    }

    public static String replaceAllBackslashSeparators(String s, String separator) {
        if (s == null) return null;
        if (s.isEmpty() || isEmpty(separator)) return s;

        //return s.replaceAll(BACKSLASH_COMMA, ","); - NOT WORKING when compiled to JS

        int p = s.lastIndexOf('\\' + separator);
        if (p == -1) return s;

        StringBuilder sb = new StringBuilder(s);
        sb.deleteCharAt(p);
        int sepLen = separator.length();
        int i = p - sepLen;
        while (i >= 1) {
            if (sb.charAt(i - 1) == '\\' && separator.equals(sb.substring(i, i + sepLen))) {
                sb.deleteCharAt(i - 1);
                i = i - 2;
            } else {
                i--;
            }
        }
        return sb.toString();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0; // optimize like Guava: s.isEmpty() -> s.length() == 0
    }

    /** Splits list of a=b pairs, if line doesn't have = then it just goes to keys.
     *  <br> trim() is called for all result keys and values.
     **/
    public static Map<String, String> splitKeyValue(List<String> lst) {
        if (lst == null || lst.isEmpty()) return null;
        Map<String, String> rslt = null;
        for (String s : lst) {
            s = s.trim();
            if (s.isEmpty() || s.equals("=")) continue;
            int j = s.indexOf('=');
            if (j >= 0) {
                String k = s.substring(0, j).trim();
                String v = s.substring(j + 1).trim();
                if (k.isEmpty() && v.isEmpty()) continue;
                if (rslt == null) rslt = new LinkedHashMap<>();
                rslt.put(k, v);
            } else {
                if (rslt == null) rslt = new LinkedHashMap<>();
                rslt.put(s, null);
            }
        }
        return rslt;
    }

    /** Returns only digits from specified string */
    public static String getDigitsOnly(String s) {
        if (isEmpty(s)) return s;
        String rslt = s.trim();
        if (isEmpty(rslt)) return rslt;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rslt.length(); i++) {
            char ch = rslt.charAt(i);
            if (Character.isDigit(ch)) sb.append(ch);
        }
        return sb.toString();
    }

    /** Returns true if specified string contains only digits */
    public static boolean isDigitsOnly(String s) {
        if (isEmpty(s)) return false;
        String rslt = s.trim();
        if (isEmpty(rslt)) return false;
        for (int i = 0; i < rslt.length(); i++) {
            char ch = rslt.charAt(i);
            if (!Character.isDigit(ch)) return false;
        }
        return true;
    }

}
