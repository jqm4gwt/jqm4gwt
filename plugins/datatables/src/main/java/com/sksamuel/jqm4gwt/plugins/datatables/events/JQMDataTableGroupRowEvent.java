package com.sksamuel.jqm4gwt.plugins.datatables.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JQMDataTableGroupRowEvent extends GwtEvent<JQMDataTableGroupRowEvent.Handler> {

    public interface Handler extends EventHandler {
        void onGroupRowClick(JQMDataTableGroupRowEvent event);
    }

    public static class GroupRowData {

        public final Element row;
        public final boolean switchAscDesc;

        public GroupRowData(Element row, boolean switchAscDesc) {
            this.row = row;
            this.switchAscDesc = switchAscDesc;
        }
    }

    private final GroupRowData data;

    private boolean stopDfltClick;

    public JQMDataTableGroupRowEvent(GroupRowData data) {
        this.data = data;
    }

    static Type<JQMDataTableGroupRowEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMDataTableGroupRowEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     * @return - true means default click processing must be suppressed/skipped.
     */
    public static <S extends HasAttachHandlers> boolean fire(S source, GroupRowData data) {
        if (TYPE != null) {
            JQMDataTableGroupRowEvent event = new JQMDataTableGroupRowEvent(data);
            source.fireEvent(event);
            return event.isStopDfltClick();
        }
        return false;
    }

    public static Type<JQMDataTableGroupRowEvent.Handler> getType() {
      if (TYPE == null) {
          TYPE = new Type<JQMDataTableGroupRowEvent.Handler>();
      }
      return TYPE;
    }

    public GroupRowData getData() {
        return data;
    }

    @Override
    public final Type<JQMDataTableGroupRowEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(JQMDataTableGroupRowEvent.Handler handler) {
        handler.onGroupRowClick(this);
    }

    public boolean isStopDfltClick() {
        return stopDfltClick;
    }

    public void setStopDfltClick(boolean stopDfltClick) {
        this.stopDfltClick = stopDfltClick;
    }

}
