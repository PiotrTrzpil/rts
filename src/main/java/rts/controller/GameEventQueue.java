package rts.controller;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import rts.misc.P;

// TODO: Auto-generated Javadoc
/**
 * Podstawowa kolejka operacji wykonywanych przez kontroler. Dostep do modelu w
 * zalozeniu ma byc mozliwy tylko w watku oblugujacym ta kolejke.
 */
public abstract class GameEventQueue extends GameThread
{
    /** Blokujaca kolejka. */
    private final BlockingQueue<Command> queue;

    /**
     * Instantiates a new game event queue.
     */
    public GameEventQueue()
    {
        queue = new LinkedBlockingQueue<Command>();
    }
    /**
     * Wstawia operacje na koniec kolejki
     * 
     * @param event operacja
     */
    public void add(final Command event)
    {
        try
        {
            queue.put(event);
        }
        catch(final InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Pobiera pierwsza operacje z kolejki
     * 
     * @return the command
     */
    public Command takeEvent()
    {
        Command event = null;
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
     * Nieskonczona petla wykonania rozkazow pobieranych z kolejki.
     */
    @Override
    public void job()
    {
        try
        {
            takeEvent().execute();
        }
        catch(final NullPointerException e)
        {
            P.er("took null");
        }
    };
    //    public void run()
    //    {
    //        while(running)
    //        {
    //
    //        }
    //    }
    //    public void stop()
    //    {
    //        running = false;
    //        if(thread != null)
    //        {
    //            thread.interrupt();
    //        }
    //    }
}
