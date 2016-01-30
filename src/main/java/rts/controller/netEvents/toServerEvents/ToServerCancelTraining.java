package rts.controller.netEvents.toServerEvents;

import rts.model.ObjectID;
import rts.model.TrainID;

public class ToServerCancelTraining extends ToServerBuildingEvent
{
    private final TrainID trainID;

    public ToServerCancelTraining(final ObjectID id, final TrainID trainID2)
    {
        super(id);
        trainID = trainID2;
    }
    public TrainID getTrainID()
    {
        return trainID;
    }
}
