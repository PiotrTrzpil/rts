package rts.controller.netEvents.toClientIngame;

import java.util.Set;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.model.map.PathNode;

public class ToClientPathNodes implements ToClientEvent
{
    public final Set<PathNode> nodes;

    public ToClientPathNodes(final Set<PathNode> newNodes)
    {
        nodes = newNodes;
        // TODO Auto-generated constructor stub
    }
    @Override
    public void execute(final ClientController controller)
    {
        controller.getViewModel().getObjectHolder().setPathNodes(nodes);
    }
}
