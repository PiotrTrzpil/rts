package rts.controller.netEvents.toServerEvents;

import rts.controller.connection.ServerSideConnection;
import rts.controller.server.ServerController;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.ObjectID;
import rts.model.serverBuildings.Building;

public class ToServerBuildingEvent implements ToServerEvent
{
    private final ObjectID id;

    public ObjectID getBuildingID()
    {
        return id;
    }
    public ToServerBuildingEvent(final ObjectID id)
    {
        this.id = id;
    }
    @Override
    public void execute(final ServerController controller, final ServerSideConnection source)
    {
        try
        {
            final Building building = (Building) controller.getModel().getObjectHolder().getObject(
                id);
            building.handleEvent(this);
        }
        catch(final ObjectNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
