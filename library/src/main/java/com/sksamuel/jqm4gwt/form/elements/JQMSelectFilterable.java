package com.sksamuel.jqm4gwt.form.elements;

/**
 * See <a href="http://demos.jquerymobile.com/1.4.3/selectmenu-custom-filter/">Filterable inside custom select</a>
 * <p/> See also <a href="https://github.com/jquery/jquery-mobile/blob/9cb1040f1da9309c30b70eccbbfb54a8ddf253aa/demos/selectmenu-custom-filter/index.php">Github Demos</a>
 *
 * @author SlavaP
 */
public class JQMSelectFilterable extends JQMSelect {

    private static final String SELECT_FILTERABLE_STYLENAME = "jqm4gwt-select-filterable";

    public JQMSelectFilterable() {
        super();
        addStyleName(SELECT_FILTERABLE_STYLENAME);
        setNative(false);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        bindLifecycleEvents(select.getElement().getId());
    }

    @Override
    protected void onUnload() {
        unbindLifecycleEvents(select.getElement().getId());
        super.onUnload();
    }

    private native void bindLifecycleEvents(String id) /*-{

        $wnd.$.mobile.document
            // The custom selectmenu plugin generates an ID for the listview by suffixing the ID of the
            // native widget with "-menu". Upon creation of the listview widget we want to place an
            // input field before the list to be used for a filter.
            .on( "listviewcreate", "#" + id + "-menu,#title-" + id + "-menu", function( e ) {
                var input,
                    listview = $wnd.$( e.target ),
                    form = listview.jqmData( "filter-form" );

                $wnd.$( "#" + id + "-listbox" ).addClass( "ui-select-filterable" ); // for CSS styling

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
                listview.filterable({ input: input,
                    children: "> li:not(:jqmData(placeholder='true'))" });
            })

            // The custom select list may show up as either a popup or a dialog, depending how much
            // vertical room there is on the screen. If it shows up as a dialog, then the form containing
            // the filter input field must be transferred to the dialog so that the user can continue to
            // use it for filtering list items.
            .on( "pagebeforeshow", "#" + id + "-dialog,#title-" + id + "-dialog", function( e ) {
                var dialog = $wnd.$( e.target ),
                    listview = dialog.find( "ul" ),
                    form = listview.jqmData( "filter-form" );

                dialog.addClass( "ui-select-filterable" ); // for CSS styling

                // Attach a reference to the listview as a data item to the dialog, because during the
                // pagehide handler below the selectmenu widget will already have returned the listview
                // to the popup, so we won't be able to find it inside the dialog with a selector.
                dialog.jqmData( "listview", listview );

                // Place the form before the listview in the dialog.
                listview.before( form );
            })

            // After the dialog is closed, the form containing the filter input is returned to the popup.
            .on( "pagehide", "#" + id + "-dialog,#title-" + id + "-dialog", function( e ) {
                var listview = $wnd.$( e.target ).jqmData( "listview" ),
                    form = listview.jqmData( "filter-form" );

                // Put the form back in the popup. It goes ahead of the listview.
                listview.before( form );
            });
    }-*/;

    private native void unbindLifecycleEvents(String id) /*-{
        $wnd.$.mobile.document
            .off( "listviewcreate", "#" + id + "-menu,#title-" + id + "-menu" )
            .off( "pagebeforeshow pagehide", "#" + id + "-dialog,#title-" + id + "-dialog" );
    }-*/;

}
