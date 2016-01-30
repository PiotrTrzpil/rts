package rts.controller.connection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Polaczenie po stronie klienta
 */
public abstract class ClientSideConnection
{
    private final BlockingQueue<NetEvent> queue;

    public ClientSideConnection()
    {
        queue = new LinkedBlockingQueue<NetEvent>();
    }
    /**
     * Wysyla zdarzenie do serwera
     * 
     * @param obj zdarzenia
     */
    public abstract void send(final ToServerEvent obj);
    /**
     * Wstawienie odebranego zdarzenia do kolejki
     * 
     * @param obj zdarzenia
     */
    protected void receive(final NetEvent obj)
    {
        if(ViewModel.console != null)
        {
            ViewModel.console.print("CLIENT SIDE Recived: " + obj);
        }
        try
        {
            queue.put(obj);
        }
        catch(final InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public NetEvent take() throws InterruptedException
    {
        return queue.take();
    }
    public abstract void close();
}
