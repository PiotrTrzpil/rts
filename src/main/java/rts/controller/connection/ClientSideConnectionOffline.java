package rts.controller.connection;

import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Lokalne polaczenie od strony klienta.
 */
public class ClientSideConnectionOffline extends ClientSideConnection
{
    /** Odpowiadajace polaczenie serwera. */
    private ServerSideConnectionOffline serverConnection;

    /**
     * Sets the server side.
     * 
     * @param con the new server side
     */
    public void setServerSide(final ServerSideConnectionOffline con)
    {
        serverConnection = con;
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * rts.controller.connection.ClientSideConnection#send(rts.controller.netEvents
     * .NetEvent)
     */
    @Override
    public void send(final ToServerEvent obj)
    {
        if(ViewModel.console != null)
        {
            ViewModel.console.print("CLIENT SIDE [Offline] Sent: " + obj);
        }
        serverConnection.receive(obj);
    }
    @Override
    public void close()
    {
    }
}
