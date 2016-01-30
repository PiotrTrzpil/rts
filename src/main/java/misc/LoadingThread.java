package misc;

import java.util.concurrent.LinkedBlockingQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class LoadingThread.
 */
public class LoadingThread implements Runnable
{

    /** The queue. */
    private final LinkedBlockingQueue<LoadTask> queue;

    /**
     * Instantiates a new loading thread.
     */
    protected LoadingThread()
    {
        queue = new LinkedBlockingQueue<LoadTask>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                final LoadTask task = queue.take();
                task.load();
            }
        }
        catch(final InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Adds the.
     * 
     * @param event the event
     */
    public void add(final LoadTask event)
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
     * Checks if is empty.
     * 
     * @return true, if is empty
     */
    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

}
