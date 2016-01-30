package rts.controller.netEvents.toClientIngame;

import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;

public class ToClientMessage implements ToClientEvent
{
    private final String text;

    public ToClientMessage(final String text)
    {
        this.text = text;
    }
    public String getText()
    {
        return text;
    }
    @Override
    public void execute(final ClientController controller)
    {
        controller.getViewModel().getChat().receiveText(text);
    }
}
