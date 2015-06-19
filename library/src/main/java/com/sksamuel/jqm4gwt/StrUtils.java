package com.sksamuel.jqm4gwt;

import java.util.ArrayList;
import java.util.List;

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

}
