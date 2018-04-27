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
            <br> y - Year
            <br> m - Month
            <br> d - Date
            <br> h - Hour
            <br> i - Minute
            <br> s - Second
            <br> a - Meridiem
            <br> p - Special Case - offset changed by the picker controls (month/year)
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

    public static class OffsetData {

        public final Date newDate;

        /**
         * <br> y - Year
         * <br> m - Month
         * <br> d - Date
         * <br> h - Hour
         * <br> i - Minute
         * <br> s - Second
         * <br> a - Meridiem
         */
        public final String changeType;

        /** Amount of change, +/- */
        public final int changeAmount;

        public OffsetData(Date newDate, String changeType, int changeAmount) {
            this.newDate = newDate;
            this.changeType = changeType;
            this.changeAmount = changeAmount;
        }

        @Override
        public String toString() {
            return "OffsetData [newDate=" + newDate
                    + ", changeType=" + changeType + ", changeAmount=" + changeAmount + "]";
        }
    }

    public interface Handler extends EventHandler {
        /**
         * Triggered when the calendar display is changed - but only if the "selected" date
         * is not in the visible natural month. (If the date is still "visible", but in the previous
         * or next month, this event will still fire)
         */
        void onDisplayChange(JQMCalBoxEvent event);

        /**
         * Triggered when the datebox control is changed.
         * <br> See <a href="http://dev.jtsage.com/DateBox/api/offset/">offset event</a>
         */
        void onOffset(JQMCalBoxEvent event);
    }

    public static class DefaultHandler implements Handler {
        @Override
        public void onDisplayChange(JQMCalBoxEvent event) {
        }

        @Override
        public void onOffset(JQMCalBoxEvent event) {
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

    public static <S extends HasAttachHandlers> void fire(S source, OffsetData data) {
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

    private final DisplayChangeData dataDisplay;
    private final OffsetData dataOffset;

    protected JQMCalBoxEvent(DisplayChangeData data) {
        this.dataDisplay = data;
        this.dataOffset = null;
    }

    protected JQMCalBoxEvent(OffsetData data) {
        this.dataDisplay = null;
        this.dataOffset = data;
    }

    public DisplayChangeData getData() {
        return dataDisplay;
    }

    public OffsetData getDataOffset() {
        return dataOffset;
    }

    @Override
    public final Type<JQMCalBoxEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public String toDebugString() {
        assertLive();
        String s = super.toDebugString();
        if (dataDisplay != null) {
            s += " dataDisplay = " + dataDisplay.toString();
        } else if (dataOffset != null) {
            s += " dataOffset = " + dataOffset.toString();
        }
        return s;
    }

    @Override
    protected void dispatch(JQMCalBoxEvent.Handler handler) {
        if (this.dataDisplay != null) handler.onDisplayChange(this);
        else if (this.dataOffset != null) handler.onOffset(this);
    }


}
