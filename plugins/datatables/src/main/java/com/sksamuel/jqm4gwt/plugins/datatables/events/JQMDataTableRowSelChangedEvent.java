package com.sksamuel.jqm4gwt.plugins.datatables.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class JQMDataTableRowSelChangedEvent extends GwtEvent<JQMDataTableRowSelChangedEvent.Handler> {

    public interface Handler extends EventHandler {
        void onRowSelChanged(JQMDataTableRowSelChangedEvent event);
    }

    public static class RowSelChangedData {

        public final Element row;
        public final boolean selected;
        public final JavaScriptObject rowData;

        public RowSelChangedData(Element row, boolean selected, JavaScriptObject rowData) {
            this.row = row;
            this.selected = selected;
            this.rowData = rowData;
        }
    }

    private final RowSelChangedData data;

    public JQMDataTableRowSelChangedEvent(RowSelChangedData data) {
        this.data = data;
    }

    static Type<JQMDataTableRowSelChangedEvent.Handler> TYPE;

    /**
     * Fires an {@link JQMDataTableRowSelChangedEvent} on all registered handlers in the handler source.
     *
     * @param <S> The handler source type
     * @param source - the source of the handlers
     */
    public static <S extends HasAttachHandlers> void fire(S source, RowSelChangedData data) {
        if (TYPE != null) {
            JQMDataTableRowSelChangedEvent event = new JQMDataTableRowSelChangedEvent(data);
            source.fireEvent(event);
        }
    }

    public static Type<JQMDataTableRowSelChangedEvent.Handler> getType() {
      if (TYPE == null) {
          TYPE = new Type<JQMDataTableRowSelChangedEvent.Handler>();
      }
      return TYPE;
    }

    public RowSelChangedData getData() {
        return data;
    }

    @Override
    public final Type<JQMDataTableRowSelChangedEvent.Handler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(JQMDataTableRowSelChangedEvent.Handler handler) {
        handler.onRowSelChanged(this);
    }

}
