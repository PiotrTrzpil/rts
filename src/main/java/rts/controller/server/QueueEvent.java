package rts.controller.server;

import rts.controller.connection.ServerSideConnection;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toServerEvents.ToServerEvent;

// TODO: Auto-generated Javadoc
/**
 * Opakowanie zdarzenia i polaczenia z ktorego przyszlo.
 */
public class QueueEvent
{
    /** The connection. */
    private final ServerSideConnection connection;
    /** The message. */
    private final ToServerEvent event;

    /**
     * Instantiates a new queue event.
     * 
     * @param netEvent the message2
     * @param playerConnection the player connection
     */
    public QueueEvent(final ToServerEvent netEvent, final ServerSideConnection playerConnection)
    {
        event = netEvent;
        connection = playerConnection;
    }
    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    public ServerSideConnection getConnection()
    {
        return connection;
    }
    /**
     * Gets the message.
     * 
     * @return the message
     */
    public NetEvent getEvent()
    {
        return event;
    }
}
