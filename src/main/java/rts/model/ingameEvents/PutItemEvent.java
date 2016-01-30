package rts.model.ingameEvents;

import rts.controller.netEvents.toClientIngame.ToClientCarriedItem;
import rts.model.Item;
import rts.model.serverBuildings.Building.ItemDemander;
import rts.model.serverUnits.Carrier;

public class PutItemEvent extends UnitEvent
{
    private final ItemDemander building;

    public PutItemEvent(final Carrier unit, final ItemDemander building)
    {
        super(unit, 0);
        this.building = building;
    }
    @Override
    public void execute()
    {
        final Carrier carrier = (Carrier) getUnit();
        carrier.putItem(building);
        final Item item = carrier.getItem();
        carrier.getEnviroment().sendToAll(new ToClientCarriedItem(carrier.getID(), item));
    }
    @Override
    public void cleaning()
    {
        final Carrier carrier = (Carrier) getUnit();
        building.cancelPendingPut(carrier.getItem());
    }
}
