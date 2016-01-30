package rts.model.ingameEvents;

// TODO: Auto-generated Javadoc
/**
 * Zdarzenie w swiecie gry
 */
public abstract class GameEvent
{
    private long time;

    public GameEvent(final long time)
    {
        this.time = time;
    }
    public void setTime(final long time2)
    {
        time = time2;
    }
    public long getTime()
    {
        return time;
    }

    /** Czas jaki musi minac od poprzedniego zdarzenia do wykonania tego. */
    private long fromPreviousDelayTime;
    /** Czas od momentu umieszczenia na linii czasu do momentu wykonania. */
    private long delayTime;

    /**
     * Instantiates a new game event.
     * 
     * @param time Czas jaki musi minac od poprzedniego zdarzenia do wykonania
     *            tego.
     */
    //        public GameEvent(final long time)
    //        {
    //            fromPreviousDelayTime = time;
    //        }
    /**
     * Wykonanie zdarzenia
     */
    public abstract void execute();
    /**
     * Gets the time.
     * 
     * @return the time
     */
    public long getFromPreviousDelayTime()
    {
        return fromPreviousDelayTime;
    }
    public void setDelayTime(final long delayTime)
    {
        this.delayTime = delayTime;
    }
    public long getDelayTime()
    {
        return delayTime;
    }
}
