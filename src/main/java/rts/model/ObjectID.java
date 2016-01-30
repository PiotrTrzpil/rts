package rts.model;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * Numer identyfikujacy obiekt nalezacy do gracza
 */
public class ObjectID implements Comparable<ObjectID>, Serializable
{
    /** The next id. */
    private static int nextID;
    public final int id;

    private ObjectID(final int id)
    {
        this.id = id;
    }
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final ObjectID idOther)
    {
        if(id > idOther.id)
        {
            return 1;
        }
        if(id < idOther.id)
        {
            return -1;
        }
        return 0;
    }
    /**
     * New id.
     * 
     * @return the object id
     */
    public static ObjectID newID()
    {
        return new ObjectID(nextID++);
    }
    @Override
    public String toString()
    {
        return "ObjectID:" + id;
    }
}
