package com.sksamuel.jqm4gwt.table;

import java.util.List;

import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.StrUtils;

public class ColumnDef {
    private String title;
    private String name; // needed in case of data binding, because title is subject to i18n
    private String priority;
    private boolean headGroup;
    private int colspan; // needed for 'Grouped column headers' mode
    private int rowspan;

    public ColumnDef() {
    }

    public ColumnDef(String title, String priority) {
        this();
        this.title = title;
        this.priority = priority;
    }

    /**
     * @param str - expected format: colspan;rowspan=title~name=priority
     * <br> If you need tilde in title or name then use \ to preserve it, like this: \~
     */
    public static ColumnDef create(String str, boolean headGroup) {
        if (str == null) return null;
        String spanStr = null;
        if (headGroup) { // colspan;rowspan part is expected for head group
            int j = str.indexOf('=');
            if (j >= 0) {
                spanStr = str.substring(0, j).trim();
                str = str.substring(j + 1);
            }
        }
        ColumnDef col = new ColumnDef();
        col.headGroup = headGroup;
        int j = str.lastIndexOf('='); // searching from the end to prevent html title breakage
        final String title;
        if (j >= 0) {
            String s = str.substring(j + 1).trim();
            if (s != null && !s.isEmpty()) col.priority = s;
            title = str.substring(0, j);
        } else {
            title = str;
        }
        List<String> lst = StrUtils.split(title, "~");
        if (!Empty.is(lst)) {
            col.title = StrUtils.replaceAllBackslashSeparators(lst.get(0), "~");
            if (lst.size() > 1) {
                col.name = StrUtils.replaceAllBackslashSeparators(lst.get(1), "~");
            }
        }
        if (spanStr != null && !spanStr.isEmpty()) {
            j = spanStr.indexOf(';');
            if (j >= 0) {
                String s = spanStr.substring(0, j).trim();
                if (s != null && !s.isEmpty()) col.colspan = Integer.parseInt(s);
                s = spanStr.substring(j + 1).trim();
                if (s != null && !s.isEmpty()) col.rowspan = Integer.parseInt(s);
            } else {
                col.colspan = Integer.parseInt(spanStr);
            }
        }
        return col;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    /** regular column which must be placed in groups row */
    public boolean isRegularInGroupRow() {
        return !headGroup && colspan <= 1 && rowspan > 1;
    }

    /** @return - true in case this ColumnDef is explicitly or implicitly considered as complex head group */
    public boolean isGroup() {
        return headGroup || (rowspan <= 1 && colspan > 1);
    }

    public boolean isHeadGroup() {
        return headGroup;
    }

    /** @param headGroup - when true explicitly defines this ColumnDef as complex head group. */
    public void setHeadGroup(boolean headGroup) {
        this.headGroup = headGroup;
    }
}
