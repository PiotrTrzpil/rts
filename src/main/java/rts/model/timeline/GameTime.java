package rts.model.timeline;

import rts.misc.Coords;

// TODO: Auto-generated Javadoc
/**
 * Pojemnik na czas.
 */
public class GameTime
{
    /** Przechowywany czas. */
    private long time;

    /**
     * Instantiates a new game time.
     * 
     * @param amount zadany czas
     */
    public GameTime(final long amount)
    {
        time = amount;
    }
    /**
     * oblicza czas przemieszczenia sie obiektu z punktu a do b z zadanym
     * modulem predkosci
     * 
     * @param a punkt a
     * @param b punkt a
     * @param velocity modul predkosci
     * 
     * @return the long
     */
    public static long moveTime(final Coords a, final Coords b, final float velocity)
    {
        return Math.round(a.distance(b) / velocity);
    }
    /**
     * Dodaje czas
     * 
     * @param addtime czas do dodania
     * 
     * @return the game time
     */
    public GameTime add(final long addtime)
    {
        time += addtime;
        return this;
    }
    /**
     * Gets the time.
     * 
     * @return the time
     */
    public long getTime()
    {
        return time;
    }
}
