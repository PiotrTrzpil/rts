package rts.controller.netEvents.toServerEvents;

import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.CommandCenter;
import rts.model.ObjectID;
import rts.model.ServerObjectHolder;
import rts.model.PlayerObject;
import rts.model.serverUnits.Unit;

public class ToServerUnitAttackEvent extends ToServerUnitEvent
{
    private final ObjectID target;

    public ToServerUnitAttackEvent(final ObjectID unit, final ObjectID target)
    {
        super(unit);
        // TODO Auto-generated constructor stub
        this.target = target;
    }
    public ObjectID getTargetID()
    {
        return target;
    }
    @Override
    protected void executeUnitCommand(final Unit unit, final CommandCenter commCenter,
        final ServerObjectHolder objectHolder) throws ObjectNotFoundException
    {
        final PlayerObject object = objectHolder.getObject(target);
        commCenter.clearCommands(unit);
        commCenter.attack(unit, object);
    }
}
