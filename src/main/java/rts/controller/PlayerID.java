package rts.controller;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * identyfikator gracza
 */
public class PlayerID implements Serializable, Comparable<PlayerID>
{
    /** The id. */
    private final int id;
    /** The ids. */
    private static int ids;

    /**
     * Instantiates a new player id.
     * 
     * @param i the i
     */
    public PlayerID(final int i)
    {
        id = i;
        // TODO Auto-generated constructor stub
    }
    /**
     * New id.
     * 
     * @return the player id
     */
    public static PlayerID newID()
    {
        return new PlayerID(ids++);
    }
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final PlayerID o)
    {
        if(id > o.id)
        {
            return 1;
        }
        if(id < o.id)
        {
            return -1;
        }
        return 0;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if(obj instanceof PlayerID)
        {
            final PlayerID playerID = (PlayerID) obj;
            return playerID.id == id;
        }
        return false;
    }
    /**
     * Gets the.
     * 
     * @return the int
     */
    public int get()
    {
        return id;
    }
}
