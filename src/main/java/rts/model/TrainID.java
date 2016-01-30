package rts.model;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * Numer identyfikujacy trening jednostki.
 */
public class TrainID implements Comparable<TrainID>, Serializable
{
    /** The id. */
    private final int id;
    /** The ids. */
    private static int ids;

    /**
     * Instantiates a new train id.
     * 
     * @param id the id
     */
    public TrainID(final int id)
    {
        super();
        this.id = id;
    }
    /**
     * New id.
     * 
     * @return the train id
     */
    public static TrainID newID()
    {
        return new TrainID(ids++);
    }
    /**
     * Gets the iD.
     * 
     * @return the iD
     */
    public int getID()
    {
        return id;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final TrainID o)
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
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if(obj instanceof TrainID)
        {
            final TrainID trainID = (TrainID) obj;
            return trainID.id == id;
        }
        return false;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "TrainID:" + id;
    }
}
