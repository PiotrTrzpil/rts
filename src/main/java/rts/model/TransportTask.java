package rts.model;

import rts.model.serverBuildings.Building.ItemDemander;
import rts.model.serverBuildings.Building.ItemSupplier;
import rts.model.serverUnits.Carrier;

public class TransportTask
{
    private final ItemSupplier source;
    private final ItemDemander dest;
    private final Item itemID;
    private Carrier carrier;

    public Carrier getCarrier()
    {
        return carrier;
    }

    public TransportTask(final ItemSupplier source, final ItemDemander dest, final Item itemID)
    {
        this.source = source;
        this.dest = dest;
        this.itemID = itemID;
    }

    public Item getItem()
    {
        return itemID;
    }

    public ItemSupplier getSource()
    {
        return source;
    }

    public ItemDemander getDestination()
    {
        return dest;
    }

    public void setCarrier(final Carrier carrier)
    {
        this.carrier = carrier;

    }
}
