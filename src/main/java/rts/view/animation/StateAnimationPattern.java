package rts.view.animation;

import java.util.ArrayList;
import rts.misc.Coords;

// TODO: Auto-generated Javadoc
/**
 * Wzor animacji stanowej
 */
public class StateAnimationPattern implements AbstractAnimationPattern
{
    /** Lista animacji skladowych. */
    private final ArrayList<AnimationPattern> states;
    /** The looping. */
    private boolean looping;
    /** Indeksy kluczowe. */
    protected final ArrayList<Integer> keyIndexes;

    /**
     * Instantiates a new state animation pattern.
     */
    public StateAnimationPattern()
    {
        states = new ArrayList<AnimationPattern>();
        keyIndexes = new ArrayList<Integer>();
    }
    /**
     * Dodaje animacje jako stan.
     * 
     * @param anim the anim
     * @param significant the significant
     */
    public void addAnimation(final AnimationPattern anim, final boolean significant)
    {
        if(significant)
        {
            keyIndexes.add(states.size());
        }
        states.add(anim);
    }
    /**
     * Sets the shift.
     * 
     * @param x the x
     * @param y the y
     */
    public void setShift(final int x, final int y)
    {
        for(final AnimationPattern pattern : states)
        {
            pattern.setShift(x, y);
        }
    }
    /* (non-Javadoc)
     * @see rts.view.animation.AbstractAnimationPattern#newAnimation(rts.misc.Coords)
     */
    @Override
    public AbstractAnimation newAnimation(final Coords position)
    {
        return new StateAnimation(this, position);
    }
    /**
     * Gets the states.
     * 
     * @return the states
     */
    protected ArrayList<AnimationPattern> getStates()
    {
        return states;
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
}
