package rts.controller.server;

import java.util.HashSet;
import rts.controller.PlayerID;
import rts.controller.UserPlayer;
import rts.controller.connection.ServerSideConnection;
import rts.controller.netEvents.NetEvent;
import rts.misc.Coords;
import rts.model.WorkerOvermind;
import rts.model.serverBuildings.Building;
import rts.model.serverUnits.Unit;

// TODO: Auto-generated Javadoc
/**
 * Klasa gracza przechowywana przez serwer
 */
public class ServerPlayer extends UserPlayer //implements Comparable<ServerPlayer>
{
    /** Budynki nalezace do gracza. */
    private final HashSet<Building> buildings;
    /** Polaczenie po stronie serwera zwiazane z graczem. */
    private final ServerSideConnection connection;
    /** Zarzadca gospodarki. */
    private final WorkerOvermind workerOvermind;
    /** Jednostki nalezace do gracza. */
    private final HashSet<Unit> units;
    /** Czy gracz jest polaczony. */
    private boolean connected;

    /**
     * Instantiates a new server player.
     * 
     * @param id Identyfikator gracza
     * @param name Nazwa gracza
     * @param connection polaczenie po stronie serwera zwiazane z graczem
     * @param startPlace
     */
    public ServerPlayer(final PlayerID id, final String name,
        final ServerSideConnection connection, final Coords startPlace)
    {
        super(id, name, startPlace);
        //        this.id = id;
        //        this.name = name;
        this.connection = connection;
        //        this.startPlace = startPlace;
        buildings = new HashSet<Building>();
        units = new HashSet<Unit>();
        workerOvermind = new WorkerOvermind();
    }
    /**
     * Send.
     * 
     * @param object the object
     */
    public void send(final NetEvent object)
    {
        connection.send(object);
    }
    //    /**
    //     * Sets the start place.
    //     *
    //     * @param place the new start place
    //     */
    //    public void setStartPlace(final Coords place)
    //    {
    //        startPlace = place;
    //    }
    /**
     * Gets the buildings.
     * 
     * @return the buildings
     */
    public HashSet<Building> getBuildings()
    {
        return buildings;
    }
    /**
     * Gets the units.
     * 
     * @return the units
     */
    public HashSet<Unit> getUnits()
    {
        return units;
    }
    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    protected ServerSideConnection getConnection()
    {
        return connection;
    }
    //    /**
    //     * Gets the iD.
    //     *
    //     * @return the iD
    //     */
    //    @Override
    //    public PlayerID getID()
    //    {
    //        return id;
    //    }
    //    /**
    //     * Gets the name.
    //     *
    //     * @return the name
    //     */
    //    @Override
    //    public String getName()
    //    {
    //        return name;
    //    }
    //    /**
    //     * Gets the start point.
    //     *
    //     * @return the start point
    //     */
    //    @Override
    //    public Coords getStartPoint()
    //    {
    //        return startPlace;
    //    }
    /**
     * Tworzy obiekt identyfikujacy tego gracza po stronie klienta
     * 
     * @return the user player
     */
    public UserPlayer createUserPlayer()
    {
        final UserPlayer userPlayer = new UserPlayer(getID(), getName(), getStartPoint());
        // userPlayer.setStartPlace(startPlace);
        return userPlayer;
    }
    //    /*
    //     * (non-Javadoc)
    //     *
    //     * @see java.lang.Object#equals(java.lang.Object)
    //     */
    //    @Override
    //    public boolean equals(final Object obj)
    //    {
    //        if(obj instanceof ServerPlayer)
    //        {
    //            final ServerPlayer serverPlayer = (ServerPlayer) obj;
    //            return serverPlayer.id.equals(id);
    //        }
    //        return false;
    //    }
    /**
     * Gets the worker overmind.
     * 
     * @return the worker overmind
     */
    public WorkerOvermind getWorkerOvermind()
    {
        // TODO Auto-generated method stub
        return workerOvermind;
    }
    /**
     * Checks if is connected.
     * 
     * @return true, if is connected
     */
    public boolean isConnected()
    {
        return connected;
    }
    /**
     * Sets the connected.
     * 
     * @param cond the new connected
     */
    public void setConnected(final boolean cond)
    {
        connected = cond;
    }
}
