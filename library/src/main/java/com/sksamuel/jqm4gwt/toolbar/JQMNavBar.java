package com.sksamuel.jqm4gwt.toolbar;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Label;
import com.sksamuel.jqm4gwt.IconPos;
import com.sksamuel.jqm4gwt.JQMCommon;
import com.sksamuel.jqm4gwt.JQMWidget;
import com.sksamuel.jqm4gwt.Mobile;
import com.sksamuel.jqm4gwt.button.JQMButton;

/**
 * @author Stephen K Samuel samspade79@gmail.com 24 Jul 2011 23:09:12
 *
 * <p/>    jQuery Mobile has a very basic navbar widget that is useful for
 *         providing up to 5 buttons with optional icons in a bar, typically
 *         within a header or footer.
 *
 *
 * <p><a href="http://demos.jquerymobile.com/1.4.5/navbar/">Navbar</a></p>
 *
 *
 */
public class JQMNavBar extends JQMWidget implements HasFixedPosition {

    private final UListElement ul;

    private List<JQMButton> buttons;

    private boolean highlightLastClicked;

    /**
     * Create a new {@link JQMNavBar} with no content
     */
    public JQMNavBar() {
        Label label = new Label();
        initWidget(label);
        setDataRole("navbar");
        setStyleName("jqm4gwt-navbar");
        ul = Document.get().createULElement();
        label.getElement().appendChild(ul);
    }

    @UiChild(limit = 5, tagname = "button")
    public void add(final JQMButton button) {
        if (button == null) return;

        if (buttons == null) buttons = new ArrayList<JQMButton>();
        buttons.add(button);
        IconPos btnP = button.getIconPos();
        IconPos p = getIconPos();
        if (btnP == null && p != null) button.setIconPos(p);

        LIElement li = Document.get().createLIElement();
        li.appendChild(button.getElement());
        ul.appendChild(li);

        // button.addClickHandler(...) is not working without the following code
        DOM.sinkEvents(li, Event.ONCLICK);
        DOM.setEventListener(li, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                DomEvent.fireNativeEvent(event, button);
                checkHighlightLastClicked(button);
            }
        });
    }

    private void checkHighlightLastClicked(final JQMButton button) {
        if (highlightLastClicked) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    Mobile.clearActiveClickedLink();
                    JQMCommon.setBtnActive(button, true);
                }
            });
        }
    }

    @UiChild(limit = 1, tagname = "buttonActive")
    public void addActive(final JQMButton button) {
        if (button == null) return;
        add(button);
        JQMCommon.setBtnActive(button, true);
    }

    /**
     * Restore the button's active state each time the page is shown while it exists in the DOM.
     */
    @UiChild(limit = 1, tagname = "buttonActivePersist")
    public void addActivePersist(final JQMButton button) {
        if (button == null) return;
        addActive(button);
        button.getElement().addClassName("ui-state-persist");
    }

    public void remove(JQMButton button) { // TODO
    }

    @Override
    public boolean isFixed() {
        return "fixed".equals(getAttribute("data-position"));
    }

    @Override
    public void setFixed(boolean fixed) {
        if (fixed)
            setAttribute("data-position", "fixed");
        else
            removeAttribute("data-position");
    }

    public IconPos getIconPos() {
        return JQMCommon.getIconPos(this);
    }

    /**
     * Sets the position of the icon. If you desire an icon only button then
     * set the position to IconPos.NOTEXT
     */
    public void setIconPos(IconPos pos) {
        if (pos == null) {
            JQMCommon.setIconPos(this, pos);
        } else {
            IconPos oldPos = getIconPos();
            JQMCommon.setIconPos(this, pos);
            for (int i = 0; i < buttons.size(); i++) {
                JQMButton btn = buttons.get(i);
                IconPos p = btn.getIconPos();
                if (p == null || oldPos == null || p.equals(oldPos)) btn.setIconPos(pos);
            }
        }
    }

    public boolean isHighlightLastClicked() {
        return highlightLastClicked;
    }

    /**
     * Special visual mode - if true then the last clicked button will be permanently highlighted.
     */
    public void setHighlightLastClicked(boolean highlightLastClicked) {
        this.highlightLastClicked = highlightLastClicked;
    }

    public int getButtonCount() {
        return buttons != null ? buttons.size() : 0;
    }

    public JQMButton getButton(int index) {
        return (buttons != null && index >= 0 && index < buttons.size()) ? buttons.get(index) : null;
    }
}
