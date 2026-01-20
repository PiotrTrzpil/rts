package rts.model.serverUnits;

//import animation.UnitAnimation;
import rts.controller.server.ServerPlayer;
import rts.misc.Coords;
import rts.model.AbstractUnit;
import rts.model.ObjectID;
import rts.model.PlayerObject;
import rts.model.ServerModel;
import rts.model.UnitType;
import rts.model.ingameEvents.ChangeMoveEvent;

// TODO: Auto-generated Javadoc
/**
 * Jednostka
 */
public abstract class Unit extends PlayerObject implements AbstractUnit
{
    /** Typ jednostki. */
    private UnitType type;
    /** Modul predkosci maksymalnej. */
    public static final float MAX_VEL = 0.1F;
    /** Ostatnio wykonana zmiana ruchu. */
    private ChangeMoveEvent currentMove;

    /**
     * Inits the parametrs.
     * 
     * @param type2 the type2
     * @param position the position
     * @param player the player
     * @param newID the new id
     * @param model the map env2
     */
    public void initParametrs(final UnitType type2, final Coords position,
        final ServerPlayer player, final ObjectID newID, final ServerModel model)
    {
        super.initParametrs(model, position, player, newID, type2.getMaxHealthPoints());
        type = type2;
    }
    /*
     * (non-Javadoc)
     * 
     * @see rts.model.AbstractUnit#getMaxVelocity()
     */
    public float getMaxVelocity()
    {
        return MAX_VEL;
    }
    /*
     * (non-Javadoc)
     * 
     * @see rts.model.AbstractUnit#getType()
     */
    public UnitType getType()
    {
        return type;
    }
    /*
     * (non-Javadoc)
     * 
     * @see rts.model.ServerOwnableObject#deactivate()
     */
    @Override
    public void deactivate()
    {
        mapEnv.getCommCenter().clearCommands(this);
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * rts.model.AbstractUnit#setCurrentMove(rts.model.ingameEvents.ChangeMoveEvent
     * )
     */
    public void setCurrentMove(final ChangeMoveEvent changeMoveEvent)
    {
        currentMove = changeMoveEvent;
    }
    /**
     * Gets the current move.
     * 
     * @return the current move
     */
    public ChangeMoveEvent getCurrentMove()
    {
        return currentMove;
    }
    public int getAttackDistance()
    {
        return 0;
    }
    public int getDamage()
    {
        return 0;
    }
}
