package rts.controller;

// TODO: Auto-generated Javadoc
/**
 * The Class GameThread.
 */
public abstract class GameThread implements Runnable
{
    /** Trwa dzialanie */
    private boolean running;
    /** Watek obslugujacy. */
    private Thread thread;

    /**
     * Start.
     */
    public void start()
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
    public void stop()
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
