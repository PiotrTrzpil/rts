package rts.view.animation;

import java.util.ArrayList;
import java.util.Timer;
import rts.controller.Timers;
import rts.misc.Coords;
import rts.view.mapView.DrawableImage;

// TODO: Auto-generated Javadoc
/**
 * Animacja stanowa, skladajaca sie z kilku animacji
 */
public class StateAnimation implements AbstractAnimation
{
    /** The states. */
    private final ArrayList<AnimationPattern> states;
    /** The current. */
    private AbstractAnimation current;
    /** The current index. */
    private int currentIndex;
    /** The position. */
    private final Coords position;
    /** The state animation pattern. */
    private final StateAnimationPattern stateAnimationPattern;
    /** The ended. */
    private boolean ended;

    /**
     * Instantiates a new state animation.
     * 
     * @param stateAnimationPattern the state animation pattern
     * @param position2 the position2
     */
    public StateAnimation(final StateAnimationPattern stateAnimationPattern, final Coords position2)
    {
        this.stateAnimationPattern = stateAnimationPattern;
        position = position2;
        states = stateAnimationPattern.getStates();
    }
    /**
     * Przejscie do nastepnego stanu
     */
    private void nextState()
    {
        if(currentIndex == states.size())
        {
            if(stateAnimationPattern.isLooping())
            {
                currentIndex = 0;
            }
            else
            {
                return;
            }
        }
        if(current != null)
        {
            current.stop();
        }
        current = states.get(currentIndex).newAnimation(position);
        current.scheduleOn(Timers.animTimer());
        currentIndex++;
    }
    /**
     * Przejscie do najblizszego stanu kluczowego
     */
    public void nextKeyState()
    {
        int nextState = -1;
        final ArrayList<Integer> indexes = stateAnimationPattern.keyIndexes;
        for(final Integer index : indexes)
        {
            if(index >= currentIndex)
            {
                nextState = index;
                break;
            }
        }
        if(nextState == -1)
        {
            nextState = indexes.get(0);
        }
        nextState = nextState % states.size();
        currentIndex = nextState;
        //     P.pr("GOTO: " + currentIndex);
        current = states.get(currentIndex).newAnimation(position);
        current.scheduleOn(Timers.animTimer());
        currentIndex++;
    }
    /**
     * Resetuje animacje
     */
    public void reset()
    {
        currentIndex = 0;
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#scheduleOn(java.util.Timer)
     */
    public void scheduleOn(final Timer effectTimer1)
    {
        nextState();
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#stop()
     */
    public void stop()
    {
        current.stop();
        ended = true;
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#hasEnded()
     */
    public boolean hasEnded()
    {
        if(ended)
        {
            return true;
        }
        if(stateAnimationPattern.isLooping())
        {
            return false;
        }
        return currentIndex == states.size() ? current.hasEnded() : false;
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#getDrawableImage()
     */
    public DrawableImage getDrawableImage()
    {
        if(current == null)
        {
            return new DrawableImage();
        }
        if(current.hasEnded())
        {
            nextState();
        }
        return current.getDrawableImage();
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#getDrawableImage(rts.misc.Coords)
     */
    @Override
    public DrawableImage getDrawableImage(final Coords point)
    {
        if(current == null)
        {
            return new DrawableImage();
        }
        if(current.hasEnded())
        {
            nextState();
        }
        return current.getDrawableImage(point);
    }
}
