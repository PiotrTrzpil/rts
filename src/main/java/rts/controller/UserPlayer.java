package rts.controller;

import java.io.Serializable;
import rts.misc.Coords;

// TODO: Auto-generated Javadoc
/**
 * Ubozsza wersja klasy przechowujacej informacje o graczu
 */
public class UserPlayer implements Serializable, Comparable<UserPlayer>
{
    /** Nazwa gracza. */
    private final String name;
    /** Identyfikator gracza. */
    private final PlayerID id;
    /** Pozycja miejsca startowego gracza. */
    private final Coords startPlace;

    /**
     * Instantiates a new user player.
     * 
     * @param id the id
     * @param name the name
     * @param startPlace2 the start place2
     */
    public UserPlayer(final PlayerID id, final String name, final Coords startPlace2)
    {
        this.id = id;
        this.name = name;
        startPlace = startPlace2;
    }
    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }
    /**
     * Gets the start point.
     * 
     * @return the start point
     */
    public Coords getStartPoint()
    {
        return startPlace;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if(obj instanceof UserPlayer)
        {
            final UserPlayer abstractPlayer = (UserPlayer) obj;
            return abstractPlayer.id.equals(id);
        }
        return false;
    }
    /**
     * Gets the iD.
     * 
     * @return the iD
     */
    public PlayerID getID()
    {
        return id;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final UserPlayer o)
    {
        return id.compareTo(o.id);
    }
}
