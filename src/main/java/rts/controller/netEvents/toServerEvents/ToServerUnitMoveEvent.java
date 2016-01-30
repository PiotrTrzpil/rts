package rts.controller.netEvents.toServerEvents;

import rts.misc.Coords;
import rts.model.CommandCenter;
import rts.model.ObjectID;
import rts.model.ServerObjectHolder;
import rts.model.serverUnits.Unit;

public class ToServerUnitMoveEvent extends ToServerUnitEvent
{
    private final Coords destination;

    public ToServerUnitMoveEvent(final ObjectID id, final Coords end)
    {
        super(id);
        destination = end;
    }
    public Coords getDestination()
    {
        return destination;
    }
    @Override
    protected void executeUnitCommand(final Unit unit, final CommandCenter commCenter,
        final ServerObjectHolder objectHolder)
    {
        commCenter.clearCommands(unit);
        commCenter.move(unit, destination);
    }
}
