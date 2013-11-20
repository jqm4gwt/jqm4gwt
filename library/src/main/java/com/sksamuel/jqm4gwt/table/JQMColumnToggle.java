package com.sksamuel.jqm4gwt.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.layout.JQMTable;

/**
 * See <a href="http://view.jquerymobile.com/1.3.2/dist/demos/widgets/table-column-toggle/">Table: Column Toggle</a>
 * <p/><b>WARNING!</b> You'd better use fixed jQuery Mobile 1.3.2 from "jqm4gwt\standalone\misc\fixed jquery mobile",
 * because critical "columns stay hidden when shrinking and resizing the browser window"
 * bug is not fixed in official 1.3.2 version.
 * @author slavap
 *
 */
public class JQMColumnToggle extends Widget {

    private final TableElement table;
    private final TableSectionElement tHead;
    private final TableSectionElement tBody;

    private String colNames;

    private String cells;

    // Column Name, Priority pairs
    private final Map<String, String> columns = new LinkedHashMap<String, String>();

    private List<String> dataStr;
    private List<Widget> dataObj;

    public JQMColumnToggle() {
        table = Document.get().createTableElement();
        table.setId(Document.get().createUniqueId());
        JQMCommon.setDataRole(table, "table");
        JQMCommon.setAttribute(table, "data-mode", "columntoggle");
        table.addClassName("ui-responsive");
        table.addClassName("table-stroke");

        tHead = Document.get().createTHeadElement();
        tBody = Document.get().createTBodyElement();
        table.appendChild(tHead);
        table.appendChild(tBody);

        setElement(table);
    }

    public String getColNames() {
        return colNames;
    }

    /**
     * @param colNames - comma separated column names with optional priority (1 = highest, 6 = lowest).
     * <p/> Column name can be valid HTML, i.e. &lt;abbr title="Rotten Tomato Rating">Rating&lt;/abbr>=1
     * <p/> Example: Rank,Movie Title,Year=3,Reviews=5
     * <p/> To make a column persistent so it's not available for hiding, just omit priority.
     * This will make the column visible at all widths and won't be available in the column chooser menu.
     */
    public void setColNames(String colNames) {
        if (this.colNames == colNames || this.colNames != null && this.colNames.equals(colNames)) return;
        this.colNames = colNames;
        if (this.colNames == null || this.colNames.isEmpty()) {
            setColumns(null);
            return;
        }
        String[] arr = this.colNames.split(",");
        Map<String, String> cols = new LinkedHashMap<String, String>();
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].trim();
            int j = s.lastIndexOf('=');
            String prio = null;
            if (j >= 0) {
                prio = s.substring(j + 1);
                s = s.substring(0, j);
            }
            cols.put(s, prio);
        }
        setColumns(cols);
    }

    public String getCells() {
        return cells;
    }

    /**
     * @param cells - comma separated table cells, each string/cell can be valid HTML
     * <p/> Example: &lt;th>1&lt;/th>, The Matrix, 1999, 8.7, &lt;th>2&lt;/th>, Falling Down, 1993, 7.5
     */
    public void setCells(String cells) {
        if (this.cells == cells || this.cells != null && this.cells.equals(cells)) return;
        this.cells = cells;
        if (this.cells == null || this.cells.isEmpty()) {
            setDataStr(null);
            return;
        }
        String[] arr = this.cells.split(",");
        List<String> lst = new ArrayList<String>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].trim();
            lst.add(s);
        }
        setDataStr(lst);
    }

    private void setColumns(Map<String, String> cols) {
        int cnt = columns.size();
        int newCnt = cols != null ? cols.size() : 0;
        columns.clear();
        if (cols != null) columns.putAll(cols);
        removeChildren(tHead);
        populateHead();
        if (cnt == newCnt) {
            if (tBody.getChildCount() == 0) populateBody();
            return;
        }
        removeChildren(tBody);
        populateBody();
    }

    private void populateHead() {
        if (columns.isEmpty()) return;
        TableRowElement tr = Document.get().createTRElement();
        for (Entry<String, String> i : columns.entrySet()) {
            TableCellElement th = Document.get().createTHElement();
            if (i.getValue() != null) {
                JQMCommon.setAttribute(th, "data-priority", i.getValue());
            }
            th.setInnerHTML(i.getKey());
            tr.appendChild(th);
        }
        tHead.appendChild(tr);
    }

    private void populateBody() {
        if (dataStr != null) {
            int i = 0;
            for (String s : dataStr) {
                addToBody(s, i++);
            }
            return;
        }
        if (dataObj != null) {
            int i = 0;
            for (Widget w : dataObj) {
                addToBody(w, i++);
            }
            return;
        }
    }

    private void addToBody(String cell, int index) {
        if (cell == null || index < 0 || columns.isEmpty()) return;
        int row = index / columns.size();
        int col = index % columns.size();
        TableRowElement r = getRow(row);
        if (r == null) return;
        boolean addTh = cell.startsWith("<th>") || cell.startsWith("<TH>");
        TableCellElement c = getCol(r, col, addTh);
        if (c == null) return;
        if (addTh) {
            String s = cell.substring("<th>".length(), cell.length() - "</th>".length()).trim();
            c.setInnerHTML(s);
        } else {
            c.setInnerHTML(cell);
        }
    }

    private void addToBody(Widget w, int index) {
        if (index < 0 || columns.isEmpty()) return;
        int row = index / columns.size();
        int col = index % columns.size();
        TableRowElement r = getRow(row);
        if (r == null) return;
        TableCellElement c = getCol(r, col, false/*addTh*/);
        if (c == null) return;
        removeChildren(c);
        if (w != null) {
            c.appendChild(w.getElement());
            activateClickHandlers(w);
        }
    }

    private TableRowElement getRow(int row) {
        int cnt = -1;
        for (int i = 0; i < tBody.getChildCount(); i++) {
            Node child = tBody.getChild(i);
            if (child instanceof TableRowElement) {
                cnt++;
                if (cnt == row) return (TableRowElement) child;
            }
        }
        TableRowElement r = null;
        for (int i = cnt; i < row; i++) {
            r = Document.get().createTRElement();
            tBody.appendChild(r);
        }
        return r;
    }

    private static TableCellElement getCol(TableRowElement r, int col, boolean addTh) {
        if (r == null || col < 0) return null;
        int cnt = -1;
        for (int i = 0; i < r.getChildCount(); i++) {
            Node child = r.getChild(i);
            if (child instanceof TableCellElement) {
                cnt++;
                if (cnt == col) return (TableCellElement) child;
            }
        }
        TableCellElement c = null;
        for (int i = cnt; i < col; i++) {
            c = addTh ? Document.get().createTHElement() : Document.get().createTDElement();
            r.appendChild(c);
        }
        return c;
    }

    private static void removeChildren(Node node) {
        if (node == null) return;
        for (int i = node.getChildCount() - 1; i >= 0; i--) {
            node.removeChild(node.getChild(i));
        }
    }

    private void setDataStr(List<String> lst) {
        dataObj = null;
        dataStr = lst;
        removeChildren(tBody);
        populateBody();
    }

    @SuppressWarnings("unused")
    private void setDataObj(List<Widget> lst) {
        dataObj = lst;
        dataStr = null;
        removeChildren(tBody);
        populateBody();
    }

    @UiChild(tagname = "cell")
    public void addCellWidget(Widget w) {
        if (dataStr != null) {
            removeChildren(tBody);
            dataStr = null;
        }
        if (dataObj == null) dataObj = new ArrayList<Widget>();
        dataObj.add(w);
        addToBody(w, dataObj.size() - 1);
    }

    /**
     * For example: JQMHeader -> JQMTable -> FlowPanel -> buttons
     */
    private static void activateClickHandlers(final Widget w) {
        if (w == null) return;

        if (w instanceof HasClickHandlers) {
            com.google.gwt.user.client.Element e = w.getElement();
            DOM.sinkEvents(e, Event.ONCLICK);
            DOM.setEventListener(e, new EventListener() {
                @Override
                public void onBrowserEvent(Event event) {
                    DomEvent.fireNativeEvent(event, w);
                }
            });
        }
        if (w instanceof JQMTable) {
            for (int i = 0; i < ((JQMTable) w).size(); i++) {
                activateClickHandlers(((JQMTable) w).get(i));
            }
        } else if (w instanceof ComplexPanel) {
            // button.addClickHandler(...) is not working without the following code
            ComplexPanel p = (ComplexPanel) w;
            Iterator<Widget> iter = p.iterator();
            while (iter.hasNext()) {
                Widget i = iter.next();
                activateClickHandlers(i);
            }
        } else if (w instanceof HasOneWidget) {
            activateClickHandlers(((HasOneWidget) w).getWidget());
        }
    }

}
