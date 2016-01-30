package rts.controller.connection;

import rts.controller.netEvents.NetEvent;
import rts.controller.server.ServerNetBuffer;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Lokalne polaczanie od strony serwera.
 */
public class ServerSideConnectionOffline extends ServerSideConnection
{

    /** Odpowiadajace polaczenie od strony uzytkownika. */
    private final ClientSideConnectionOffline clientSideConn;

    /**
     * Instantiates a new server side connection offline.
     * 
     * @param queue kolejka serwera
     * @param clientSideConn polaczenie od strony uzytkownika
     */
    public ServerSideConnectionOffline(final ServerNetBuffer queue,
        final ClientSideConnectionOffline clientSideConn)
    {
        super(queue);
        this.clientSideConn = clientSideConn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * rts.controller.connection.ServerSideConnection#send(rts.controller.netEvents
     * .NetEvent)
     */
    @Override
    public void send(final NetEvent object)
    {
        if(ViewModel.console != null)
        {
            ViewModel.console.print("SERVER SIDE [Offline] Sent: " + object);
        }
        clientSideConn.receive(object);
    }
}
