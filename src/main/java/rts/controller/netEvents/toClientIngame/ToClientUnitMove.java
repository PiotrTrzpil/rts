package rts.controller.netEvents.toClientIngame;

import java.util.List;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.misc.Coords;
import rts.model.ObjectID;

public class ToClientUnitMove implements ToClientEvent
{
    private final ObjectID unitID;
    private final Coords start;
    private final List<Coords> path;

    public ToClientUnitMove(final ObjectID id, final Coords start, final List<Coords> path)
    {
        super();
        unitID = id;
        this.start = start;
        this.path = path;
    }
    public ObjectID getUnitID()
    {
        return unitID;
    }
    public Coords getStart()
    {
        return start;
    }
    public List<Coords> getPath()
    {
        return path;
    }
    public void execute(final ClientController controller)
    {
        controller.getViewModel().getObjectHolder().move(unitID, start, path);
    }
}
