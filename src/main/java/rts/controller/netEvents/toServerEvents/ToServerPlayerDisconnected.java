package rts.controller.netEvents.toServerEvents;

import rts.controller.connection.ServerSideConnection;
import rts.controller.server.ServerController;

// TODO: Auto-generated Javadoc
/**
 * Wysylana wewnetrznie w serwerze, gdy gracz zostaje rozlaczony
 */
public class ToServerPlayerDisconnected implements ToServerEvent
{
    @Override
    public void execute(final ServerController controller, final ServerSideConnection source)
    {
        controller.playerDisconnected(source);
    }
}
