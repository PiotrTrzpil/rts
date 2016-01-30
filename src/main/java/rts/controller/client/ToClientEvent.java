package rts.controller.client;

import rts.controller.netEvents.NetEvent;

// TODO: Auto-generated Javadoc
/**
 * Zdarzenie przed startem gry
 */
public interface ToClientEvent extends NetEvent
{
    /**
     * Execute.
     * 
     * @param controller the controller
     */
    public void execute(ClientController controller);
}
