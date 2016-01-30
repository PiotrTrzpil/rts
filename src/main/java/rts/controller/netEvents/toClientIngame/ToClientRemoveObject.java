package rts.controller.netEvents.toClientIngame;

import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.ObjectID;

public class ToClientRemoveObject implements ToClientEvent
{
    private final ObjectID objectID;

    public ToClientRemoveObject(final ObjectID objectID)
    {
        super();
        this.objectID = objectID;
    }
    public ObjectID getObjectID()
    {
        return objectID;
    }
    public void execute(final ClientController controller)
    {
        try
        {
            controller.getViewModel().getObjectHolder().remove(objectID);
        }
        catch(final ObjectNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
