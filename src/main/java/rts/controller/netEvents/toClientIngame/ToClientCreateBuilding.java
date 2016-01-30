package rts.controller.netEvents.toClientIngame;

import rts.controller.PlayerID;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.misc.Coords;
import rts.model.BuildingType;
import rts.model.ObjectID;
import rts.model.serverBuildings.Building;

public class ToClientCreateBuilding implements ToClientEvent
{
    private final BuildingType buildingType;
    private final ObjectID objectID;
    private final PlayerID playerID;
    private final Coords buildPoint;

    public ToClientCreateBuilding(final BuildingType buildingType, final ObjectID objectID,
        final PlayerID playerID, final Coords buildPoint)
    {
        this.buildingType = buildingType;
        this.objectID = objectID;
        this.playerID = playerID;
        this.buildPoint = buildPoint;
    }
    public ToClientCreateBuilding(final Building building)
    {
        buildingType = building.getType();
        objectID = building.getID();
        playerID = building.getOwner().getID();
        buildPoint = building.getPosition();
    }
    public BuildingType getBuildingType()
    {
        return buildingType;
    }
    public ObjectID getObjectID()
    {
        return objectID;
    }
    public PlayerID getPlayerID()
    {
        return playerID;
    }
    public Coords getBuildPoint()
    {
        return buildPoint;
    }
    public void execute(final ClientController controller)
    {
        controller.getViewModel().getObjectHolder().createBuilding(buildingType, objectID,
            playerID, buildPoint);
    }
}
