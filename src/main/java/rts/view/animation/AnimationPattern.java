package rts.view.animation;

import java.awt.Image;
import java.util.ArrayList;
import rts.misc.Coords;
import rts.misc.GameVector;

// TODO: Auto-generated Javadoc
/**
 * Wzor animacji, z ktorego tworzone sa pojedyncze animacje
 */
public class AnimationPattern implements AbstractAnimationPattern
{
    /** Lista obrazkow. */
    private final ArrayList<Frame> frames;
    /** Calkowity czas dzialania. */
    private int totalTime;
    /** Czas trwania jednej ramki. */
    private int frameDuration;
    /** Przesuniecie obrazka wzgledem pozycji animacji. */
    private GameVector positionShift;
    /** Czy animacja jest powtarzalna. */
    private boolean looping;

    /**
     * Instantiates a new animation pattern.
     * 
     * @param frameDuration the frame duration
     */
    public AnimationPattern(final int frameDuration)
    {
        this.frameDuration = frameDuration;
        frames = new ArrayList<Frame>();
        positionShift = new GameVector();
    }
    /**
     * Skaluje czasy trwania klatek, by cala animacja zmiescila sie w danym
     * czasie
     * 
     * @param time dany czas
     */
    public void scaleToTime(final int time)
    {
        frameDuration = Math.round((float) time / frames.size());
        totalTime = 0;
        for(final Frame frame : frames)
        {
            totalTime += frameDuration;
            frame.endTime = totalTime;
        }
    }
    /**
     * Dodaje klatke
     * 
     * @param image the image
     */
    public void addFrame(final Image image)
    {
        totalTime += frameDuration;
        frames.add(new Frame(image, totalTime));
    }
    /**
     * Zwraca liste ramek
     * 
     * @return the frames
     */
    public ArrayList<Frame> getFrames()
    {
        return (ArrayList<Frame>) frames.clone();
    }
    /**
     * Gets the total time.
     * 
     * @return the total time
     */
    public int getTotalTime()
    {
        return totalTime;
    }
    /**
     * Ustawa przesuniecie
     * 
     * @param x the x
     * @param y the y
     */
    public void setShift(final int x, final int y)
    {
        positionShift = new GameVector(x, y);
    }
    /**
     * Gets the shift.
     * 
     * @return the shift
     */
    public GameVector getShift()
    {
        return positionShift;
    }
    /**
     * Gets the frame duration.
     * 
     * @return the frame duration
     */
    public int getFrameDuration()
    {
        return frameDuration;
    }
    /**
     * Sets the looping.
     * 
     * @param val the new looping
     */
    public void setLooping(final boolean val)
    {
        looping = val;
    }
    /**
     * Checks if is looping.
     * 
     * @return true, if is looping
     */
    public boolean isLooping()
    {
        return looping;
    }
    /* (non-Javadoc)
     * @see rts.view.animation.AnimPattern#newAnimation(rts.misc.Coords)
     */
    @Override
    public AbstractAnimation newAnimation(final Coords position)
    {
        if(frames.size() < 2)
        {
            return new SingleFrameAnimation(this, position);
        }
        else
        {
            return new Animation(this, position);
        }
    }

    /**
     * Klatka animacji
     */
    protected static class Frame
    {
        /** Obrazek. */
        protected Image image;
        /** Czas zakonczenia danej klatki wzgledem poczatku animacji. */
        protected int endTime;

        /**
         * Instantiates a new frame.
         * 
         * @param image the image
         * @param time the time
         */
        public Frame(final Image image, final int time)
        {
            this.image = image;
            endTime = time;
        }
    }
}
