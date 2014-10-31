package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptionElement;
import com.sksamuel.jqm4gwt.JQMCommon;

/**
 * See <a href="http://demos.jquerymobile.com/1.4.5/selectmenu-custom-filter/">Filterable inside custom select</a>
 * <p/> See also <a href="https://github.com/jquery/jquery-mobile/blob/9cb1040f1da9309c30b70eccbbfb54a8ddf253aa/demos/selectmenu-custom-filter/index.php">Github Demos</a>
 *
 * @author SlavaP
 */
public class JQMSelectFilterable extends JQMSelect {

    private static final String SELECT_FILTERABLE_STYLENAME = "jqm4gwt-select-filterable";

    private String menuStyleNames;

    private Element listView;

    public JQMSelectFilterable() {
        super();
        addStyleName(SELECT_FILTERABLE_STYLENAME);
        setNative(false);
    }

    public String getMenuStyleNames() {
        return menuStyleNames;
    }

    /**
     * There is predefined ui-select-filterable CSS class added to menu dialog/popup.
     * <p/> You can style listview by defining rule .ui-selectmenu.ui-select-filterable .ui-selectmenu-list { ... }
     * <p/> For additional flexibility you can specify custom classes to be added together with ui-select-filterable
     * @param dialogStyleNames - space separated custom classes
     */
    public void setMenuStyleNames(String dialogStyleNames) {
        this.menuStyleNames = dialogStyleNames;
    }

    @Override
    public Boolean doFiltering(Element elt, Integer index, String searchValue) {
        // TODO: remove when this bug is fixed: https://github.com/jquery/jquery-mobile/issues/7677
        String s = JQMCommon.getFilterText(elt);
        if (s == null || s.isEmpty()) {
            s = JQMCommon.getAttribute(elt, "data-option-index");
            if (s != null && !s.isEmpty()) {
                try {
                    int i = Integer.parseInt(s);
                    OptionElement opt = getOption(i);
                    if (opt != null) {
                        s = JQMCommon.getFilterText(opt);
                        if (s != null && !s.isEmpty()) {
                            JQMCommon.setFilterText(elt, s);
                        }
                    }
                } catch (NumberFormatException ex) {
                    // nothing, can continue safely
                }
            }
        }
        return super.doFiltering(elt, index, searchValue);
    }

    private void setListView(Element listViewElt) {
        listView = listViewElt;
        if (listView != null && JQMCommon.isFilterableReady(listView)) {
            JavaScriptObject origFilter = JQMCommon.getFilterCallback(listView);
            JQMCommon.bindFilterCallback(this, listView, origFilter);
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        bindLifecycleEvents(select.getElement().getId(), this);
    }

    @Override
    protected void onUnload() {
        unbindLifecycleEvents(select.getElement().getId());
        super.onUnload();
    }

    private static native void bindLifecycleEvents(String id, JQMSelectFilterable combo) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$.mobile.document
            // The custom selectmenu plugin generates an ID for the listview by suffixing the ID of the
            // native widget with "-menu". Upon creation of the listview widget we want to place an
            // input field before the list to be used for a filter.
            .on( "listviewcreate", "#" + id + "-menu", function( e ) {
                var input,
                    listview = $wnd.$( e.target ),
                    form = listview.jqmData( "filter-form" );

                var lb = $wnd.$( "#" + id + "-listbox" );
                lb.addClass( "ui-select-filterable" ); // for CSS styling
                var addnl = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::getMenuStyleNames()();
                if (addnl) { lb.addClass( addnl ); }

                // We store the generated form in a variable attached to the popup so we avoid creating a
                // second form/input field when the listview is destroyed/rebuilt during a refresh.
                if ( !form ) {
                    input = $wnd.$( "<input data-type='search'></input>" );
                    form = $wnd.$( "<form></form>" ).append( input );
                    form.submit( function () { return false; } ); // fix for ENTER key press
                    input.textinput();
                    listview.before( form ).jqmData( "filter-form", form ) ;
                    form.jqmData( "listview", listview );
                } else {
                    input = form.find( "input" );
                }
                // Instantiate a filterable widget on the newly created listview and
                // indicate that the generated input is to be used for the filtering.
                listview.filterable({ input: input, children: "> li:not(:jqmData(placeholder='true'))" });
                combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::setListView(Lcom/google/gwt/dom/client/Element;)(listview[0]);
            })

            // The custom select list may show up as either a popup or a dialog, depending how much
            // vertical room there is on the screen. If it shows up as a dialog, then the form containing
            // the filter input field must be transferred to the dialog so that the user can continue to
            // use it for filtering list items.
            .on( "pagebeforeshow", "#" + id + "-dialog", function( e ) {
                var dialog = $wnd.$( e.target ),
                    listview = dialog.find( "ul" ),
                    form = listview.jqmData( "filter-form" );

                dialog.addClass( "ui-select-filterable" ); // for CSS styling
                var addnl = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::getMenuStyleNames()();
                if (addnl) { dialog.addClass( addnl ); }

                // Attach a reference to the listview as a data item to the dialog, because during the
                // pagehide handler below the selectmenu widget will already have returned the listview
                // to the popup, so we won't be able to find it inside the dialog with a selector.
                dialog.jqmData( "listview", listview );

                // Place the form before the listview in the dialog.
                listview.before( form );
            })

            // After the dialog is closed, the form containing the filter input is returned to the popup.
            .on( "pagehide", "#" + id + "-dialog", function( e ) {
                var listview = $wnd.$( e.target ).jqmData( "listview" ),
                    form = listview.jqmData( "filter-form" );

                // Put the form back in the popup. It goes ahead of the listview.
                listview.before( form );
            });
    }-*/;

    private static native void unbindLifecycleEvents(String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$.mobile.document
            .off( "listviewcreate", "#" + id + "-menu" )
            .off( "pagebeforeshow pagehide", "#" + id + "-dialog" );
    }-*/;

}
