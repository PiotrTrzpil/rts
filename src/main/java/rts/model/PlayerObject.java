package rts.model;

import rts.controller.server.ServerPlayer;
import rts.misc.Coords;
import rts.view.clientModel.misc.RelationFlag;

// TODO: Auto-generated Javadoc
/**
 * Obiekt nalezacy do gracza, moze to byc budynek lub jednostka.
 */
public abstract class PlayerObject extends MapObject implements
        Comparable<PlayerObject>
{
    /** Gracz posiadajacy ten obiekt. */
    private ServerPlayer owner;
    /** Punkty zycia. */
    private int healthPoints;
    private int maxHP;
    /** Jesli obiekt jest zniszczony, to pole jest false. */
    private boolean alive = true;
    /** Identyfikator obiektu. */
    private ObjectID objectID;
    /** The map env. */
    protected ServerModel mapEnv;

    /**
     * Inits the parametrs.
     * 
     * @param mapEnv2 the map env2
     * @param position the c
     * @param owner the owner
     * @param objectID the object id
     * @param maxHP
     */
    public void initParametrs(final ServerModel mapEnv2, final Coords position,
        final ServerPlayer owner, final ObjectID objectID, final int maxHP)
    {
        super.initParametrs(position);
        this.maxHP = maxHP;
        mapEnv = mapEnv2;
        this.objectID = objectID;
        this.owner = owner;
        healthPoints = maxHP;
    }
    /**
     * Deactivate.
     */
    public void deactivate()
    {
    }
    /**
     * Activate.
     */
    public void activate()
    {
    }
    /**
     * Gets the enviroment.
     * 
     * @return the enviroment
     */
    public ServerModel getEnviroment()
    {
        return mapEnv;
    }
    /**
     * Checks if is alive.
     * 
     * @return true, if is alive
     */
    public boolean isAlive()
    {
        return alive;
    }
    /**
     * Zwraca relacje (wrogi/przyjazny) w stosunku do danego gracza
     * 
     * @param ownerOther dany gracz
     * @return relacja
     */
    public RelationFlag getRelationTo(final ServerPlayer ownerOther)
    {
        if(ownerOther.equals(getOwner()))
        {
            return RelationFlag.FRIENDLY;
        }
        else
        {
            return RelationFlag.HOSTILE;
        }
    }
    //    public void setOwner(final ServerPlayer own)
    //    {
    //        owner = own;
    //    }
    /**
     * Gets the owner.
     * 
     * @return the owner
     */
    public ServerPlayer getOwner()
    {
        return owner;
    }
    /**
     * Wywolywana, gdy obiekt odnosi obrazenia
     * 
     * @param damage ilosc obrazen
     */
    public void beHit(final int damage)
    {
        healthPoints -= damage;
        if(healthPoints <= 0)
        {
            alive = false;
        }
    }
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final PlayerObject o)
    {
        return getID().compareTo(o.getID());
    }
    /**
     * Gets the iD.
     * 
     * @return the iD
     */
    public ObjectID getID()
    {
        return objectID;
    }
    /**
     * Zwraca ilosc punktow zycia
     * 
     * @return the hP
     */
    public int getHP()
    {
        return healthPoints;
    }
    public int getMaxHP()
    {
        return maxHP;
    }
}
