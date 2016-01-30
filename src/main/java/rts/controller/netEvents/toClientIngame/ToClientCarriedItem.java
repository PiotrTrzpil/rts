package rts.controller.netEvents.toClientIngame;

import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.Item;
import rts.model.ObjectID;
import rts.view.clientModel.UnitSprite;

public class ToClientCarriedItem implements ToClientEvent
{
    private final Item item;
    private final ObjectID carrierID;

    public ToClientCarriedItem(final ObjectID carrierID, final Item item)
    {
        this.carrierID = carrierID;
        this.item = item;
    }
    public Item getItem()
    {
        return item;
    }
    public ObjectID getCarrierID()
    {
        return carrierID;
    }
    @Override
    public void execute(final ClientController controller)
    {
        UnitSprite carrier;
        try
        {
            carrier = (UnitSprite) controller.getViewModel().getObjectHolder().getObject(carrierID);
            carrier.setItem(item);
        }
        catch(final ObjectNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
