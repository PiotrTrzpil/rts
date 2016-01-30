package rts.controller.netEvents.toServerEvents;

import rts.model.ObjectID;
import rts.model.UnitType;

public class ToServerAddTraining extends ToServerBuildingEvent
{

    private final UnitType type;

    public UnitType getType()
    {
        return type;
    }

    public ToServerAddTraining(final ObjectID building, final UnitType type)
    {
        super(building);

        this.type = type;

    }

}
