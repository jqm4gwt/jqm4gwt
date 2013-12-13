package com.sksamuel.jqm4gwt;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Widget;

/**
 * See <a href="http://view.jquerymobile.com/1.3.2/dist/demos/widgets/dialog/">Dialogs</a>
 *
 * <p/> Also see <a href="http://vernonkesner.com/blog/2013/04/10/jquery-mobile-dialogs-and-popups/">
 * jQuery Mobile: Dialogs and Popups</a>
 *
 * @author SlavaP
 *
 */
public class JQMDialog extends JQMPage {

    /**
     * Creates a {@link JQMDialog} with the given id
     *
     * @param containerId the id to use as this dialog's id
     */
    public @UiConstructor JQMDialog(String containerId) {
        super(containerId);
    }

    /**
     * Create a new {@link JQMDialog} with an automatically assigned dialog id,
     * and then add the given widgets serially to the dialog layout.
     */
    public JQMDialog(Widget... widgets) {
        super(widgets);
    }

    @Override
    protected String getDfltRole() {
        return "dialog";
    }

}
