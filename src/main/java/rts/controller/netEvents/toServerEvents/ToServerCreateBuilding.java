package rts.controller.netEvents.toServerEvents;

import rts.controller.connection.ServerSideConnection;
import rts.controller.server.ServerController;
import rts.misc.Coords;
import rts.model.BuildingType;

public class ToServerCreateBuilding implements ToServerEvent
{
    private final BuildingType buildingType;
    private final Coords buildPoint;

    public ToServerCreateBuilding(final BuildingType buildingType, final Coords buildPoint)
    {
        this.buildingType = buildingType;
        this.buildPoint = buildPoint;
    }
    public BuildingType getBuildingType()
    {
        return buildingType;
    }
    public Coords getBuildPoint()
    {
        return buildPoint;
    }
    @Override
    public void execute(final ServerController controller, final ServerSideConnection source)
    {
        controller.getModel().getObjectHolder().createBuilding(buildingType, source.getPlayer(),
            buildPoint, true);
    }
}
