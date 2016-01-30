package rts.controller.netEvents.toClientIngame;

import rts.model.ObjectID;
import rts.model.TrainID;

public class ToClientRemoveTraining extends ToClientBuildingEvent
{
    private final TrainID trainID;

    public TrainID getTrainID()
    {
        return trainID;
    }
    public ToClientRemoveTraining(final ObjectID building, final TrainID newID)
    {
        super(building);
        trainID = newID;
    }
}
