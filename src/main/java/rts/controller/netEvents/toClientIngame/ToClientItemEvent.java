package rts.controller.netEvents.toClientIngame;

import rts.model.Item;
import rts.model.ObjectID;

public class ToClientItemEvent extends ToClientBuildingEvent
{
    private final Item item;
    private final int number;

    public ToClientItemEvent(final ObjectID building, final Item item, final int number)
    {
        super(building);
        this.item = item;
        this.number = number;
    }
    public Item getItem()
    {
        return item;
    }
    public int getNumber()
    {
        return number;
    }
}
