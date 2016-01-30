package rts.view.clientModel;

import rts.controller.UserPlayer;
import rts.misc.Coords;
import rts.model.AbstractOwnableObject;
import rts.model.ObjectID;
import rts.view.clientModel.misc.RelationFlag;
import rts.view.clientModel.misc.Selectable;
import rts.view.panels.InfoTab;

// TODO: Auto-generated Javadoc
/**
 * Reprezentacja obiektu gracza po stronie klienta.
 */
public abstract class PlayerObjectSprite extends MapObjectSprite implements Selectable, AbstractOwnableObject
{
    /** The info tab. */
    protected InfoTab infoTab;
    /** The unit id. */
    private final ObjectID unitID;
    /** The owner. */
    private final UserPlayer owner;
    /** The health points. */
    private int healthPoints;
    /** The max hp. */
    private final int maxHP;

    /**
     * Instantiates a new object sprite.
     * 
     * @param sprite the sprite
     * @param c the c
     * @param unitID the unit id
     * @param owner the owner
     * @param maxHP the max hp
     */
    public PlayerObjectSprite(final ObjectImage sprite, final Coords c, final ObjectID unitID,
        final UserPlayer owner, final int maxHP)
    {
        super(sprite, c);
        this.unitID = unitID;
        this.owner = owner;
        this.maxHP = maxHP;
        healthPoints = maxHP;
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.misc.Selectable#getInfoTab()
     */
    public InfoTab getInfoTab()
    {
        return infoTab;
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.misc.Selectable#setInfoTab(rts.view.panels.InfoTab)
     */
    public void setInfoTab(final InfoTab info)
    {
        infoTab = info;
    }
    // public abstract Drawable getSelection();
    /**
     * Zwraca relacje budynku do danego gracza
     * 
     * @param ownerOther the owner other
     * @return the relation to
     */
    public RelationFlag getRelationTo(final UserPlayer ownerOther)
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
    /**
     * Gets the owner.
     * 
     * @return the owner
     */
    public UserPlayer getOwner()
    {
        return owner;
    }
    /* (non-Javadoc)
     * @see rts.model.AbstractOwnableObject#getID()
     */
    public ObjectID getID()
    {
        return unitID;
    }
    /**
     * Sets the hP.
     * 
     * @param healthPoints the new hP
     */
    public void setHP(final int healthPoints)
    {
        this.healthPoints = healthPoints;
    }
    /**
     * Gets the hP.
     * 
     * @return the hP
     */
    public int getHP()
    {
        return healthPoints;
    }
    /**
     * Gets the max hp.
     * 
     * @return the max hp
     */
    public int getMaxHP()
    {
        return maxHP;
    }
}
