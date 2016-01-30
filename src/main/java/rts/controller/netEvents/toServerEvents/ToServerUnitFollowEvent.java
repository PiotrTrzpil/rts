package rts.controller.netEvents.toServerEvents;

import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.CommandCenter;
import rts.model.ObjectID;
import rts.model.ServerObjectHolder;
import rts.model.serverUnits.Unit;

public class ToServerUnitFollowEvent extends ToServerUnitEvent
{
    private final ObjectID target;

    public ToServerUnitFollowEvent(final ObjectID unit, final ObjectID target)
    {
        super(unit);
        this.target = target;
    }
    public ObjectID getTargeID()
    {
        return target;
    }
    @Override
    protected void executeUnitCommand(final Unit unit, final CommandCenter commCenter,
        final ServerObjectHolder objectHolder) throws ObjectNotFoundException
    {
        final Unit targetUnit = (Unit) objectHolder.getObject(target);
        commCenter.clearCommands(unit);
        commCenter.follow(unit, targetUnit);
    }
}
