package rts.controller.connection;

import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.controller.server.ServerNetBuffer;
import rts.controller.server.ServerPlayer;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Polaczenie z klientem po stronie serwera
 */
public abstract class ServerSideConnection
{
    /** Kolejka zdarzen od uzytkownikow. */
    protected ServerNetBuffer serverQueue;
    /** The player. */
    protected ServerPlayer player;

    /**
     * Instantiates a new server side connection.
     * 
     * @param serverQueue the server queue
     */
    public ServerSideConnection(final ServerNetBuffer serverQueue)
    {
        this.serverQueue = serverQueue;
    }
    /**
     * Gets the player.
     * 
     * @return the player
     */
    public ServerPlayer getPlayer()
    {
        return player;
    }
    /**
     * Sets the player.
     * 
     * @param player the new player
     */
    public void setPlayer(final ServerPlayer player)
    {
        this.player = player;
    }
    /**
     * Wyslanie wiadomosci do uzytkownika
     * 
     * @param event zdarzenie
     */
    public abstract void send(final NetEvent event);
    /**
     * Odebranie zdarzenia od uzytkownika.
     * 
     * @param event zdarzenie
     */
    public void receive(final ToServerEvent event)
    {
        if(ViewModel.console != null)
        {
            ViewModel.console.print("SERVER SIDE Recived: " + event);
        }
        // System.out.println("SERVER SIDE [Online] Recived: " + message);
        serverQueue.putEvent(event, this);
    }
    /**
     * Close.
     */
    public void close()
    {
    }
}
