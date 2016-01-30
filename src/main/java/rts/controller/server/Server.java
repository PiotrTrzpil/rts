package rts.controller.server;

import java.util.List;
import rts.controller.Command;
import rts.controller.ServerGameQueue;
import rts.controller.connection.ServerSideConnection;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.misc.exceptions.UnknownEventException;

// TODO: Auto-generated Javadoc
/**
 * Serwer odbierajacy zdarzenia.
 */
public abstract class Server
{
    /** The connections. */
    private final List<ServerSideConnection> connections;
    /** The game queue. */
    protected final ServerGameQueue gameQueue;
    /** The controller. */
    private final ServerController controller;

    /**
     * Instantiates a new server.
     * 
     * @param controller the controller
     * @param gameQueue the game queue
     * @param connections the connections
     */
    public Server(final ServerController controller, final ServerGameQueue gameQueue,
        final List<ServerSideConnection> connections)
    {
        this.controller = controller;
        this.gameQueue = gameQueue;
        this.connections = connections;
    }
    /**
     * Umieszcza zdarzenie w kolejce watku kontrolera serwera.
     * 
     * @param message the message
     * @throws UnknownEventException the unknown event exception
     */
    protected void manageEvent(final QueueEvent message) throws UnknownEventException
    {
        final ServerSideConnection connection = message.getConnection();
        final NetEvent event = message.getEvent();
        if(event instanceof ToServerEvent)
        {
            final ToServerEvent netServerEvent = (ToServerEvent) event;
            gameQueue.add(new Command()
            {
                @Override
                public void execute()
                {
                    netServerEvent.execute(controller, connection);
                }
            });
        }
        else
        {
            throw new UnknownEventException(event.toString());
        }
    }
    /**
     * Gets the connections.
     * 
     * @return the connections
     */
    public List<ServerSideConnection> getConnections()
    {
        return connections;
    }
    /**
     * Removes the connection.
     * 
     * @param connection the connection
     */
    public void removeConnection(final ServerSideConnection connection)
    {
        connections.remove(connection);
        connection.close();
    }
    /**
     * Removes the all.
     */
    public void removeAll()
    {
        for(final ServerSideConnection connection : connections)
        {
            connection.close();
        }
        connections.clear();
    }
    /**
     * Adds the connection.
     * 
     * @param connection the connection
     */
    public void addConnection(final ServerSideConnection connection)
    {
        connections.add(connection);
    }
    /**
     * Send to all.
     * 
     * @param message the message
     */
    public void sendToAll(final NetEvent message)
    {
        for(final ServerSideConnection c : connections)
        {
            c.send(message);
        }
    }
}
