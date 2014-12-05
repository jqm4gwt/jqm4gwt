package com.sksamuel.jqm4gwt.plugins.datebox;

import java.util.Date;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JQMCalBoxEvent extends GwtEvent<JQMCalBoxEvent.Handler> {

    public static class DisplayChangeData {

        /** the new date shown */
        public final Date shownDate;

        /** current user-selected date */
        public final Date selectedDate;

        /** Field Changed:
            y - Year
            m - Month
            d - Date
            h - Hour
            i - Minute
            s - Second
            a - Meridiem
            p - Special Case - offset changed by the picker controls (month/year)
         */
        public final String thisChange;

        /**
         * Amount of change, +/- (0 in the case of thisChange == "p")
         */
        public final int thisChangeAmount;

        public DisplayChangeData(Date shownDate, Date selectedDate,
                String thisChange, int thisChangeAmount) {
            this.shownDate = shownDate;
            this.selectedDate = selectedDate;
            this.thisChange = thisChange;
            this.thisChangeAmount = thisChangeAmount;
        }

        @Override
        public String toString() {
            return "DisplayChangeData [shownDate=" + shownDate + ", selectedDate=" + selectedDate
                    + ", thisChange=" + thisChange + ", thisChangeAmount=" + thisChangeAmount + "]";
        }
    }

    public interface Handler extends EventHandler {
        /**
         * Triggered when the calendar display is changed - but only if the "selected" date
         * is not in the visible natural month. (If the date is still "visible", but in the previous
         * or next month, this event will still fire)
         */
        void onDisplayChange(JQMCalBoxEvent event);
    }

    public static class DefaultHandler implements Handler {
        @Override
        public void onDisplayChange(JQMCalBoxEvent event) {
        }
    }

    static Type<JQMCalBoxEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMCalBoxEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     */
    public static <S extends HasAttachHandlers> void fire(S source, DisplayChangeData data) {
      if (TYPE != null) {
          JQMCalBoxEvent event = new JQMCalBoxEvent(data);
          source.fireEvent(event);
      }
    }

    public static Type<JQMCalBoxEvent.Handler> getType() {
      if (TYPE == null) {
          TYPE = new Type<JQMCalBoxEvent.Handler>();
      }
      return TYPE;
    }

    private final DisplayChangeData data;

    protected JQMCalBoxEvent(DisplayChangeData data) {
        this.data = data;
    }

    public DisplayChangeData getData() {
        return data;
    }

    @Override
    public final Type<JQMCalBoxEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        return super.toDebugString() + " data = " + data != null ? data.toString() : null;
    }

    @Override
    protected void dispatch(JQMCalBoxEvent.Handler handler) {
        handler.onDisplayChange(this);
    }


}
