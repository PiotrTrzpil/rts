package rts.controller.netEvents.toClientIngame;

import rts.controller.PlayerID;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.misc.Coords;
import rts.model.ObjectID;
import rts.model.UnitType;
import rts.model.serverUnits.Unit;

public class ToClientCreateUnit implements ToClientEvent
{
    private final UnitType unitType;
    private final ObjectID objectID;
    private final PlayerID playerID;
    private final Coords position;

    public ToClientCreateUnit(final UnitType unitType, final ObjectID objectID,
        final PlayerID playerID, final Coords position)
    {
        super();
        this.unitType = unitType;
        this.objectID = objectID;
        this.playerID = playerID;
        this.position = position;
    }
    public ToClientCreateUnit(final Unit unit)
    {
        super();
        unitType = unit.getType();
        objectID = unit.getID();
        playerID = unit.getOwner().getID();
        position = unit.getPosition();
    }
    public UnitType getUnitType()
    {
        return unitType;
    }
    public ObjectID getObjectID()
    {
        return objectID;
    }
    public PlayerID getPlayerID()
    {
        return playerID;
    }
    public Coords getPosition()
    {
        return position;
    }
    public void execute(final ClientController controller)
    {
        controller.getViewModel().getObjectHolder().createUnit(unitType, objectID, playerID,
            position);
    }
}
