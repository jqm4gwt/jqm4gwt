package com.sksamuel.jqm4gwt;

import java.util.ArrayList;
import java.util.List;

public class StrUtils {

    private StrUtils() {} // static class

    // See http://stackoverflow.com/a/2709855
    //private static final String COMMA_SPLIT = "(?<!\\\\),";
    //private static final String BACKSLASH_COMMA = "\\\\,";

    /** Splits by commas, backslash commas are ignored(preserved). */
    public static String[] commaSplit(String s) {
        if (s == null) return null;
        //return s.split(COMMA_SPLIT); - NOT WORKING when compiled to JS

        if (s.isEmpty()) return new String[] { s };
        List<String> rslt = null;
        int start = 0;
        int p = start;
        do {
            int j = s.indexOf(',', p);
            if (j == -1) {
                if (rslt == null) return new String[] { s };
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

        return rslt != null ? rslt.toArray(new String[0]) : null;
    }

    public static String replaceAllBackslashCommas(String s) {
        if (s == null) return null;
        //return s.replaceAll(BACKSLASH_COMMA, ","); - NOT WORKING when compiled to JS

        if (s.isEmpty()) return s;
        int p = s.lastIndexOf("\\,");
        if (p == -1) return s;

        StringBuilder sb = new StringBuilder(s);
        sb.deleteCharAt(p);
        int i = p - 1;
        while (i >= 1) {
            if (sb.charAt(i) == ',' && sb.charAt(i - 1) == '\\') {
                sb.deleteCharAt(i - 1);
                i = i - 2;
            } else {
                i--;
            }
        }
        return sb.toString();
    }

}
