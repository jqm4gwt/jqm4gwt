package com.sksamuel.jqm4gwt.form.elements;

public class JQMSelectWithIcons extends JQMSelect {

    private static final String SELECT_ICONS_STYLENAME = "jqm4gwt-select-icons";

    public JQMSelectWithIcons() {
        super();
        addStyleName(SELECT_ICONS_STYLENAME);
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

    private static native void bindLifecycleEvents(String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$.mobile.document
            // The custom selectmenu plugin generates an ID for the listview by suffixing the ID of the
            // native widget with "-menu". Upon creation of the listview widget we want to add item icons.
            .on( "listviewcreate", "#" + id + "-menu", function( e ) {
                var select =  $wnd.$( "#" + id ),
                    listview = $wnd.$( e.target );

                var pos = select.attr('data-iconpos');
                var posLeft = pos !== undefined && pos !== null && pos === 'left';

                // iterate through each of the OPTION elements for this SELECT element
                $wnd.$.each(select.find('option'), function (index, element) {
                    // if the OPTION element has the `data-icon` attribute
                    var icon = $wnd.$(element).attr('data-icon');
                    if (icon !== undefined && icon !== null) {
                        // update the menu for this SELECT by adding an icon SPAN element
                        // to each of the OPTION elements that has a `data-icon` attribute
                        if (posLeft) {
                            listview.children().eq(index).find('.ui-btn').prepend(
                                '<span class="ui-icon ui-icon-' + icon
                                + ' ui-btn-icon-left ui-icon-shadow" />');
                        } else {
                            listview.children().eq(index).find('.ui-btn').append(
                                '<span class="ui-icon ui-icon-' + icon
                                + ' ui-btn-icon-right ui-icon-shadow" />');
                        }
                    }
                });
            });
    }-*/;

    private static native void unbindLifecycleEvents(String id) /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        $wnd.$.mobile.document.off( "listviewcreate", "#" + id + "-menu" );
    }-*/;

}
