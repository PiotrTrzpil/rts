package rts.controller.netEvents.toServerEvents;

import rts.controller.connection.ServerSideConnection;
import rts.controller.netEvents.toClientIngame.ToClientMessage;
import rts.controller.server.ServerController;

public class ToServerChatMessage implements ToServerEvent
{
    private final String text;

    public ToServerChatMessage(final String text)
    {
        this.text = text;
        // TODO Auto-generated constructor stub
    }
    public String getText()
    {
        return text;
    }
    @Override
    public void execute(final ServerController controller, final ServerSideConnection source)
    {
        controller.getModel().sendToAll(
            new ToClientMessage(source.getPlayer().getName() + " : " + text));
    }
}
