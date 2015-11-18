package com.sksamuel.jqm4gwt.form.elements;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.sksamuel.jqm4gwt.Empty;
import com.sksamuel.jqm4gwt.JQMCommon;

/**
 * See <a href="http://demos.jquerymobile.com/1.4.5/selectmenu-custom-filter/">Filterable inside custom select</a>
 * <br> See also <a href="https://github.com/jquery/jquery-mobile/blob/9cb1040f1da9309c30b70eccbbfb54a8ddf253aa/demos/selectmenu-custom-filter/index.php">Github Demos</a>
 *
 * @author SlavaP
 */
public class JQMSelectFilterable extends JQMSelect {

    public static String CLEAR_BUTTON_TEXT = "-----";

    private static final String SELECT_FILTERABLE_STYLENAME = "jqm4gwt-select-filterable";

    private static final String FILTERABLE_SELECT = "filterable-select";

    private static boolean jsServed;

    private Element filterable;

    private boolean showClearButton;

    public JQMSelectFilterable() {
        super();
        addStyleName(SELECT_FILTERABLE_STYLENAME);
        select.addStyleName(FILTERABLE_SELECT);
        setNative(false);
    }

    @Override
    public String getMenuStyleNames() {
        String s = super.getMenuStyleNames();
        // for CSS styling
        String rslt = "ui-select-filterable";
        if (!Empty.is(s)) rslt += " " + s;
        return rslt;
    }

    /**
     * There is a predefined ui-select-filterable CSS class, which is always added to menu dialog/popup.
     * <br> You can style listview by defining rule .ui-selectmenu.ui-select-filterable .ui-selectmenu-list { ... }
     * <br> For additional flexibility you can specify custom classes to be added together with ui-select-filterable
     * @param menuStyleNames - space separated custom classes
     */
    @Override
    public void setMenuStyleNames(String menuStyleNames) {
        super.setMenuStyleNames(menuStyleNames);
    }

    /* @Override
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
    } */

    private void setFilterableElt(Element filterableElt) {
        filterable = filterableElt;
        if (filterable != null && JQMCommon.isFilterableReady(filterable)) {
            JavaScriptObject origFilter = JQMCommon.getFilterCallback(filterable);
            JQMCommon.bindFilterCallback(this, filterable, origFilter);
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (!jsServed) {
            jsServed = true;
            serveSelectsFilterable();
        }
    }

    @Override
    protected void onUnload() {
        super.onUnload();
    }

    private static native boolean isPageSelectFilterableDialog(String pageId) /*-{
        var isDialog = false;
        var SEL = "." + @com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::FILTERABLE_SELECT;
        $wnd.$(SEL).each(function() {
            var id = $wnd.$(this).attr("id");
            if (id) {
                var s = id + "-dialog";
                if (s === pageId) {
                    isDialog = true;
                    return false;
                }
            }
        });
        return isDialog;
    }-*/;

    private static native void serveSelectsFilterable() /*-{
        if ($wnd.$ === undefined || $wnd.$ === null) return; // jQuery is not loaded
        var SEL = "." + @com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::FILTERABLE_SELECT;
        $wnd.$.mobile.document
        // Upon creation of the select menu, we want to make use of the fact that the ID of the
        // listview it generates starts with the ID of the select menu itself, plus the suffix "-menu".
        // We retrieve the listview and insert a search input before it.
        .on( "selectmenucreate", SEL, function( event ) {
            var input,
                selectmenu = $wnd.$( event.target ),
                id = selectmenu.attr( "id" ),
                listview = $wnd.$( "#" + id + "-menu" ),
                form = listview.jqmData( "filter-form" );

            var combo = @com.sksamuel.jqm4gwt.form.elements.JQMSelect::findCombo(Lcom/google/gwt/dom/client/Element;)
                            (event.target);

            // We store the generated form in a variable attached to the popup so we avoid creating a
            // second form/input field when the listview is destroyed/rebuilt during a refresh.
            if ( !form ) {
                input = $wnd.$( "<input data-type='search'></input>" );
                form = $wnd.$( "<form style='padding-top: 1px;'></form>" ).append( input );
                form.submit( function () { return false; } ); // fix for ENTER key press
                input.textinput();
                listview.before( form ).jqmData( "filter-form", form ) ;
                form.jqmData( "listview", listview );

                var isClear = combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::isShowClearButton()();
                if (isClear === true) {
                    var clearText = @com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::CLEAR_BUTTON_TEXT;
                    var clearBtn = $wnd.$("<button class='ui-btn ui-mini' style='margin:0'>"
                                          + clearText + "</button>");
                    clearBtn.on('click', function() {
                         combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::closeAndClearValue()();
                    });
                    listview.before( clearBtn ).jqmData( "clear-button", clearBtn );
                }
            } else {
                input = form.find( "input" );
            }
            // Instantiate a filterable widget on the newly created selectmenu widget and indicate that
            // the generated input form element is to be used for the filtering.
            selectmenu.filterable({
                input: input,
                children: "> option[data-placeholder != 'true'][value]"
            })
            // Rebuild the custom select menu's list items to reflect the results of the filtering
            // done on the select menu.
            .on( "filterablefilter", function() {
                selectmenu.selectmenu( "refresh" );
            });
            combo.@com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::setFilterableElt(Lcom/google/gwt/dom/client/Element;)
                (selectmenu[0]); // listview[0]
        })
        // The custom select list may show up as either a popup or a dialog, depending on how much
        // vertical room there is on the screen. If it shows up as a dialog, then the form containing
        // the filter input field must be transferred to the dialog so that the user can continue to
        // use it for filtering list items.
        .on( "pagecontainerbeforeshow", function( event, data ) {
            var pageId = data.toPage && data.toPage.attr("id");
            // We only handle the appearance of a dialog generated by a filterable selectmenu
            var isDlg = @com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::isPageSelectFilterableDialog(Ljava/lang/String;)
                            (pageId);
            if (!isDlg) return;
            var dialog = data.toPage;
            var listview = dialog.find( "ul" );
            var form = listview.jqmData( "filter-form" );
            var clearBtn = listview.jqmData( "clear-button" );

            // Attach a reference to the listview as a data item to the dialog, because during the
            // pagecontainerhide handler below the selectmenu widget will already have returned the
            // listview to the popup, so we won't be able to find it inside the dialog with a selector.
            dialog.jqmData( "listview", listview );
            // Place the form before the listview in the dialog.
            listview.before( form );
            if (clearBtn) {
                // 110% is a hack, we should just add 32px
                clearBtn.css("margin", "0 -16px 0 -16px").css("width", "110%");
                listview.before( clearBtn );
            }
        })
        // After the dialog is closed, the form containing the filter input is returned to the popup.
        .on( "pagecontainerhide", function( event, data ) {
            var pageId = data.prevPage && data.prevPage.attr("id");
            // We only handle the disappearance of a dialog generated by a filterable selectmenu
            var isDlg = @com.sksamuel.jqm4gwt.form.elements.JQMSelectFilterable::isPageSelectFilterableDialog(Ljava/lang/String;)
                            (pageId);
            if (!isDlg) return;
            var listview = data.prevPage.jqmData( "listview" );
            var form = listview.jqmData( "filter-form" );
            var clearBtn = listview.jqmData( "clear-button" );
            // Put the form back in the popup. It goes ahead of the listview.
            listview.before( form );
            if (clearBtn) {
                clearBtn.css("margin", "0").css("width", "100%");
                listview.before( clearBtn );
            }
        });
    }-*/;

    public boolean isShowClearButton() {
        return showClearButton;
    }

    /** Clear button will be shown on filtering dialog with text defined by CLEAR_BUTTON_TEXT field. */
    public void setShowClearButton(boolean showClearButton) {
        this.showClearButton = showClearButton;
    }

    protected void closeAndClearValue() {
        this.close();
        this.setValue(null, true/*fireEvents*/);
    }

}
