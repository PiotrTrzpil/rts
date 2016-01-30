package rts.controller.netEvents.toServerEvents;

import rts.controller.connection.ServerSideConnection;
import rts.controller.server.ServerController;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.CommandCenter;
import rts.model.ObjectID;
import rts.model.ServerModel;
import rts.model.ServerObjectHolder;
import rts.model.serverUnits.Unit;

public abstract class ToServerUnitEvent implements ToServerEvent
{
    private final ObjectID unitID;

    public ToServerUnitEvent(final ObjectID id)
    {
        unitID = id;
    }
    public ObjectID getUnitID()
    {
        return unitID;
    }
    @Override
    public void execute(final ServerController controller, final ServerSideConnection source)
    {
        try
        {
            final ServerModel model = controller.getModel();
            final Unit unit = (Unit) model.getObjectHolder().getObject(unitID);
            executeUnitCommand(unit, model.getCommCenter(), model.getObjectHolder());
        }
        catch(final ObjectNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    protected abstract void executeUnitCommand(final Unit unit, final CommandCenter commCenter,
        final ServerObjectHolder objectHolder) throws ObjectNotFoundException;
}
