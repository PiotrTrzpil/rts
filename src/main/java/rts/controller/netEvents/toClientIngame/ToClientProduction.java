package rts.controller.netEvents.toClientIngame;

import rts.model.ObjectID;

public class ToClientProduction extends ToClientBuildingEvent
{
    private final int productionTime;

    public ToClientProduction(final ObjectID id, final int productionTime)
    {
        super(id);
        this.productionTime = productionTime;
    }
    public int getProductionTime()
    {
        return productionTime;
    }
}
