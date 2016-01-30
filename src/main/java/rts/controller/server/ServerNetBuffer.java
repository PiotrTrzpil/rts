package rts.controller.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import rts.controller.GameThread;
import rts.controller.connection.ServerSideConnection;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.misc.P;
import rts.misc.exceptions.UnknownEventException;

// TODO: Auto-generated Javadoc
/**
 * Kolejka serwera do ktorej przychodza zdarzenia od polaczonych klientow.
 * Zdarzenia sa opakowywane i przekazywane do modulu interpretujacego.
 */
public class ServerNetBuffer extends GameThread implements Runnable
{
    /** Kolejka. */
    private final BlockingQueue<QueueEvent> queue;
    /** Serwer obslugujacy zdarzenia. */
    private Server handler;

    /**
     * Instantiates a new server queue.
     */
    public ServerNetBuffer()
    {
        queue = new LinkedBlockingQueue<QueueEvent>();
    }
    @Override
    protected void job()
    {
        final QueueEvent takeEvent = take();// .execute();
        try
        {
            if(takeEvent == null)
            {
                throw new NullPointerException("Null server event");
            }
            handler.manageEvent(takeEvent);
        }
        catch(final UnknownEventException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Pobiera pierwsze zdarzenie z kolejki.
     * 
     * @return zdarzenie z kolejki
     */
    private QueueEvent take()
    {
        QueueEvent event = null;
        try
        {
            event = queue.take();
        }
        catch(final InterruptedException e)
        {
            P.er("ServerBuffer interrupted");
        }
        return event;
    }
    /**
     * Wykonywana przez watki polaczenia uzytkownikow. Dodaje zdarzenie na
     * koniec kolejki.
     * 
     * @param event zdarzenie
     * @param serverSideConnection polaczenie z ktorego przyszlo zdarzenie.
     */
    public void putEvent(final ToServerEvent event, final ServerSideConnection serverSideConnection)
    {
        try
        {
            queue.put(new QueueEvent(event, serverSideConnection));
        }
        catch(final InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Ustawia modul obslugujacy zdarzenia.
     * 
     * @param server nowy modul obslugujacy zdarzenia
     */
    public void setServer(final Server server)
    {
        handler = server;
    }
}
