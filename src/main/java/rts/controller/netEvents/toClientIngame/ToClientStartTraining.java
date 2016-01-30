package rts.controller.netEvents.toClientIngame;

import rts.model.ObjectID;
import rts.model.TrainID;

public class ToClientStartTraining extends ToClientBuildingEvent
{
    private final TrainID newID;

    public TrainID getTrainID()
    {
        return newID;
    }
    public ToClientStartTraining(final ObjectID building, final TrainID newID)
    {
        super(building);
        this.newID = newID;
    }
}
