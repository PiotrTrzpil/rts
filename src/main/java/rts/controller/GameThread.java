package rts.controller;

// TODO: Auto-generated Javadoc
/**
 * The Class GameThread.
 */
public abstract class GameThread implements Runnable
{
    /** Trwa dzialanie */
    private volatile boolean running;
    /** Watek obslugujacy. */
    private Thread thread;

    /**
     * Start.
     */
    public synchronized void start()
    {
        if(running == false)
        {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    /**
     * Stop.
     */
    public synchronized void stop()
    {
        running = false;
        if(thread != null)
        {
            thread.interrupt();
        }
    }
    /**
     * Nieskonczona petla wykonania.
     */
    public void run()
    {
        while(running)
        {
            job();
        }
    }
    /**
     * Wykonanie jakiejs pracy
     */
    protected abstract void job();
}
