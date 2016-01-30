package rts.controller.netEvents.toClientIngame;

import rts.model.ObjectID;
import rts.model.TrainID;
import rts.model.UnitType;

public class ToClientAddTraining extends ToClientBuildingEvent
{
    private final UnitType type;
    private final TrainID newID;

    public ToClientAddTraining(final ObjectID building, final UnitType type, final TrainID newID)
    {
        super(building);
        this.type = type;
        this.newID = newID;
    }
    public TrainID getTrainID()
    {
        return newID;
    }
    public UnitType getUnitType()
    {
        return type;
    }
}
