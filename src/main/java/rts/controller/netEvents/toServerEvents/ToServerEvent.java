package rts.controller.netEvents.toServerEvents;

import rts.controller.connection.ServerSideConnection;
import rts.controller.netEvents.NetEvent;
import rts.controller.server.ServerController;

// TODO: Auto-generated Javadoc
/**
 * Zdarzenie od klienta do serwera
 */
public interface ToServerEvent extends NetEvent
{
    public void execute(final ServerController controller, ServerSideConnection source);
}
