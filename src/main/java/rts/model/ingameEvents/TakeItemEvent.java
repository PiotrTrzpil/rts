package rts.model.ingameEvents;

import rts.controller.netEvents.toClientIngame.ToClientCarriedItem;
import rts.model.Item;
import rts.model.serverBuildings.Building.ItemSupplier;
import rts.model.serverUnits.Carrier;

public class TakeItemEvent extends UnitEvent
{
    private final ItemSupplier building;
    private final Item item;

    public TakeItemEvent(final Carrier unit, final ItemSupplier building, final Item item)
    {
        super(unit, 0);
        this.building = building;
        this.item = item;
    }
    @Override
    public void execute()
    {
        final Carrier carrier = (Carrier) unit;
        carrier.takeItem(building, item);
        carrier.getEnviroment().sendToAll(new ToClientCarriedItem(carrier.getID(), item));
    }
    @Override
    public void cleaning()
    {
        building.cancelPendingTake(item);
    }
}
