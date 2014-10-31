package com.sksamuel.jqm4gwt.layout;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMContext;
import com.sksamuel.jqm4gwt.JQMWidget;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 May 2011 23:54:07
 *
 * <p/>    The {@link JQMTable} widget is a panel that allows other widgets to
 *         be added in a regular grid. The grid is regular in the sense that
 *         there is no concept of "cell span" like a normal HTML table has.
 *
 * <p/>    This table can accept any {@link JQMWidget} or any regular GWT
 *         {@link Widget} as a child element. Each added widget is wrapped in a
 *         div element.
 *
 * <p/>    The table can be resized after being created by calling
 *         withColumns(int). That can be an expensive operation, see the javadoc
 *         for the withColumns(int) method for more information on why.
 *
 * <p/>    Tables must have at least 1 and at most 5 columns
 *
 * <p>See <a href="http://demos.jquerymobile.com/1.4.5/grids/">Grids</a></p>
 *
 */
public class JQMTable extends JQMWidget {

    /**
     * The number of columns this table has.
     */
    private int columns;

    private int[] percentage;

    /**
     * The container to hold the child widget
     */
    private final FlowPanel flow;

    /**
     * Create a new table with initial number of columns as 2.
     */
    public JQMTable() {
        this(2);
    }

    /**
     * Create a new table with the given number of columns.
     */
    public JQMTable(int columns) {
        flow = new FlowPanel();
        initWidget(flow);

        setStyleName("jqm4gwt-table");
        setColumns(columns);
    }

    private static class JQMTableCell extends FlowPanel {
    }

    /**
     * Add the given {@link Widget} into the next available cell. This call
     * will wrap to a new row if the existing row is already filled.
     *
     * The given widget will be wrapped inside a div with the appropriate
     * JQuery Mobile class name given (eg, "ui-block-a" for the first cell,
     * etc). This created div will have an automatically assigned id.
     *
     * @param addStyleNames - space separated additional style names for this particular cell
     * @return the widget that was created to wrap the given content
     */
    @UiChild(tagname = "cell")
    public Widget add(Widget widget, String addStyleNames) {

        int size = getElement().getChildCount();
        String klass = getCellStyleName(size);

        JQMTableCell cell = new JQMTableCell();
        Element cellElt = cell.getElement();
        cellElt.setId(Document.get().createUniqueId());
        removeAllCellStyles(cellElt);
        cellElt.addClassName(klass);
        prepareCellPercentStyle(size, cell);
        if (addStyleNames != null && !addStyleNames.isEmpty()) {
            JQMCommon.addStyleNames(cell, addStyleNames);
        }
        cell.add(widget);

        flow.add(cell);

        JQMContext.render(cell.getElement().getId());

        return cell;
    }

    public Widget add(Widget widget) {
        return add(widget, null/*cellStyleNames*/);
    }

    /**
     * @return - index of column, which contains this widget,
     *           or -1 if this widget doesn't belong/related to the current table.
     */
    public int findParentColumn(Widget widget) {
        if (widget == null) return -1;
        Widget w = widget.getParent();
        while (w != null) {
            if (w instanceof JQMTableCell) {
                return flow.getWidgetIndex(w);
            }
            w = w.getParent();
        }
        return -1;
    }

    /**
     * Removes all cells from the table.
     */
    public void clear() {
        flow.clear();
    }

    /**
     * Returns the appropriate stylename for the given cell position
     */
    private String getCellStyleName(int pos) {
        int column = pos % columns;
        switch (column) {
        case 0:
            return "ui-block-a";
        case 1:
            return "ui-block-b";
        case 2:
            return "ui-block-c";
        case 3:
            return "ui-block-d";
        case 4:
            return "ui-block-e";
        default:
            throw new RuntimeException("internal error");
        }
    }

    private void prepareCellPercentStyle(int pos, Widget w) {
        Style st = w.getElement().getStyle();
        if (percentage == null || percentage.length != columns) {
            st.clearFloat();
            st.clearWidth();
        } else {
            int column = pos % columns;
            st.setFloat(Style.Float.LEFT);
            st.setWidth(percentage[column], Unit.PCT);
            if (percentage[column] == 0) st.setDisplay(Display.NONE);
            else st.clearDisplay();
        }
    }

    private static void removeAllCellStyles(Element elt) {
        if (elt == null) return;
        elt.removeClassName("ui-block-a");
        elt.removeClassName("ui-block-b");
        elt.removeClassName("ui-block-c");
        elt.removeClassName("ui-block-d");
        elt.removeClassName("ui-block-e");
    }

    /**
     * Returns the index of the widget container.
     */
    public int indexOf(Widget w) {
        return flow.getWidgetIndex(w);
    }

    /**
     * Adds the given {@link Widget} before the given position.
     *
     * This is an O(n) operation because after the widget is inserted all the
     * remaining cells need to have their style sheets updated to reflect
     * their new position.
     *
     */
    public Widget insert(Widget w, int beforeIndex) {

        FlowPanel widgetWrapper = new FlowPanel();
        widgetWrapper.getElement().setId(Document.get().createUniqueId());
        widgetWrapper.add(w);

        flow.insert(w, beforeIndex);

        JQMContext.render(widgetWrapper.getElement().getId());
        rebase();

        return widgetWrapper;
    }

    /**
     * Update the stylesheets of all cells
     */
    private void rebase() {
        for (int k = 0; k < flow.getWidgetCount(); k++) {
            Widget widget = flow.getWidget(k);
            String cellStyleName = getCellStyleName(k);
            removeAllCellStyles(widget.getElement());
            prepareCellPercentStyle(k, widget);
            widget.getElement().addClassName(cellStyleName);
        }
    }

    private void updateCellPercents() {
        for (int k = 0; k < flow.getWidgetCount(); k++) {
            Widget widget = flow.getWidget(k);
            prepareCellPercentStyle(k, widget);
        }
    }

    /**
     * Remove the widget at the given index.
     *
     * This is an O(n) operation because after the widget is removed all the
     * remaining cells need to have their style sheets updated to reflect
     * their new position.
     *
     * @return true if the cell was removed
     */
    public boolean remove(int index) {
        if (flow.remove(index)) {
            rebase();
            return true;
        }
        return false;
    }

    /**
     * Removes the given widget from the table, if it is a child of the table.
     * NOTE: The widget passed in must be a container widget as returned by
     * the add or insert methods.
     *
     * This is an O(n) operation because after the widget is removed all the
     * remaining cells need to have their style sheets updated to reflect
     * their new position.
     *
     * @return true if the cell was removed
     *
     */
    public boolean remove(Widget w) {
        int indexOf = indexOf(w);
        if (indexOf >= 0) return remove(indexOf);
        return false;
    }

    /**
     * Removes the last cell
     */
    public void removeLast() {
        flow.remove(flow.getWidgetCount() - 1);
    }

    /**
     * This is an O(n) operation where n is the number of child widgets. This
     * is because in a jquery mobile table the cell position is determined by
     * the classname assigned. So all cells in column0 are assigned a
     * classname of ui-block-a, and so on.
     *
     * When the relative position of a cell is changed, the classname must be
     * changed. Therefore all add and remove operations (except for the last
     * element) and resize operations (such as this method) results in a
     * shuffle which requires an iteration through all elements to update
     * their appropriate classname.
     *
     * If the new column count is the same as the old column count then this
     * is a no-op call.
     */
    public void setColumns(int n) {
        if (n < 1) throw new IllegalArgumentException("Min column count is 1");
        if (n > 5) throw new IllegalArgumentException("Max column count is 5");
        if (n == columns) return;
        this.columns = n;
        refresh(this.columns);
    }

    public int getColumns() {
        return this.columns;
    }

    private void refresh(int n) {
        setTableStyleName(n);
        rebase();
    }

    /**
     *
     * @param percents - comma separated percent size for each column.
     * <p/>For example: 10,30,30,30 defines table/grid with four columns.
     */
    public void setPercentageColumns(String percents) {
        if (percents == null || percents.isEmpty()) {
            percentage = null;
            refresh(this.columns);
            return;
        }
        String[] arr = percents.split(",");
        percentage = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i].trim();
            percentage[i] = Integer.parseInt(s);
        }
        if (arr.length == this.columns) updateCellPercents();
        else setColumns(arr.length);
    }

    public String getPercentageColumns() {
        if (percentage == null) return null;
        String s = "";
        for (int i = 0; i < percentage.length-1; i++) {
            s += String.valueOf(percentage[i]) + ",";
        }
        s += String.valueOf(percentage[percentage.length-1]);
        return s;
    }

    /**
     * @param colIndexes - hides specified columns and proportionally spreads their space
     * to visible columns. If no columns specified, makes all columns visible.
     */
    public void hidePercentageColumns(Integer... colIndexes) {
        if (percentage == null) return;
        if (colIndexes == null || colIndexes.length == 0) {
            updateCellPercents();
            return;
        }
        int[] arr = new int[percentage.length];
        System.arraycopy(percentage, 0, arr, 0, percentage.length);
        int avl = 0;
        for (int i = 0; i < colIndexes.length; i++) {
            Integer idx = colIndexes[i];
            if (idx == null || idx < 0 || idx >= arr.length) continue;
            avl += arr[idx];
            arr[idx] = 0;
        }
        if (avl == 0) return; // nothing to spread
        int minVal = Integer.MAX_VALUE;
        int minIdx = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] <= 0) continue;
            if (arr[i] < minVal) {
                minVal = arr[i];
                minIdx = i;
            }
        }
        if (minIdx >= 0) {
            double[] coeffs = new double[arr.length];
            double coeffsSum = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] <= 0) {
                    coeffs[i] = 0;
                } else if (arr[i] == minVal) {
                    coeffs[i] = 1;
                } else {
                    coeffs[i] = ((double) arr[i]) / ((double) minVal);
                }
                coeffsSum += coeffs[i];
            }
            if (coeffsSum > 0) {
                double oneChunk = avl / coeffsSum;
                long incSum = 0;
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] <= 0) continue;
                    double v = oneChunk * coeffs[i];
                    long incV = Math.round(Math.floor(v));
                    incSum += incV;
                    arr[i] += incV;
                }
                if (incSum < avl && minIdx >= 0) {
                    arr[minIdx] += avl - incSum;
                }
            }
        }
        int[] saved = percentage;
        percentage = arr;
        try {
            updateCellPercents();
        } finally {
            percentage = saved;
        }
    }

    public void showAllPercentageColumns() {
        hidePercentageColumns();
    }

    public void hideAllPercentageColumns() {
        showPercentageColumns();
    }

    /**
     * @param colIndexes - shows only specified columns and proportionally spreads hidden columns space
     * to these columns. If no columns specified, makes all columns hidden.
     */
    public void showPercentageColumns(Integer... colIndexes) {
        if (percentage == null) return;
        if (colIndexes == null || colIndexes.length == 0) {
            Integer[] arr = new Integer[percentage.length];
            for (int i = 0; i < percentage.length; i++) {
                arr[i] = i;
            }
            hidePercentageColumns(arr);
            return;
        }
        Set<Integer> hideCols = new HashSet<Integer>(percentage.length);
        for (int i = 0; i < percentage.length; i++) hideCols.add(i);
        for (int i = 0; i < colIndexes.length; i++) hideCols.remove(colIndexes[i]);
        hidePercentageColumns(hideCols.toArray(new Integer[0]));
    }

    /**
     * Sets the style sheet of the container element to the appropriate
     * classname for the given number of columns.
     */
    private void setTableStyleName(int columns) {
        String klass = "ui-grid-";
        switch (columns) {
        case 1:
            klass += "solo";
            break;
        case 2:
            klass += "a";
            break;
        case 3:
            klass += "b";
            break;
        case 4:
            klass += "c";
            break;
        case 5:
            klass += "d";
            break;
        }
        Element elt = flow.getElement();
        elt.removeClassName("ui-grid-solo");
        elt.removeClassName("ui-grid-a");
        elt.removeClassName("ui-grid-b");
        elt.removeClassName("ui-grid-c");
        elt.removeClassName("ui-grid-d");
        elt.removeClassName("ui-grid-e");
        elt.addClassName(klass);
    }

    /**
     * Returns the number of cells which is not necessarily a multiple of the
     * number of columns.
     *
     * @return the number of cells.
     */
    public int size() {
        return flow.getWidgetCount();
    }

    /**
     * @return - i-th cell, i must be in 0..size()-1
     */
    public Widget get(int i) {
        return flow.getWidget(i);
    }

    public Widget getFirstVisibleCell() {
        for (int i = 0; i < flow.getWidgetCount(); i++) {
            Widget w = flow.getWidget(i);
            String disp = w.getElement().getStyle().getDisplay();
            if (disp == null || disp.isEmpty()) return w;
            if (Display.NONE.getCssName().equals(disp)) continue;
            return w;
        }
        return null;
    }

    public Widget getLastVisibleCell() {
        for (int i = flow.getWidgetCount() - 1; i >= 0; i--) {
            Widget w = flow.getWidget(i);
            String disp = w.getElement().getStyle().getDisplay();
            if (disp == null || disp.isEmpty()) return w;
            if (Display.NONE.getCssName().equals(disp)) continue;
            return w;
        }
        return null;
    }
}
