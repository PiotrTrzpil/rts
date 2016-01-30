package rts.view.animation;

import java.awt.Image;
import java.util.Timer;
import rts.misc.Coords;
import rts.view.mapView.DrawableImage;

// TODO: Auto-generated Javadoc
/**
 * Animacja zawierajaca tylko jeden obrazek
 */
public class SingleFrameAnimation implements AbstractAnimation
{
    /** The current. */
    private final Image current;
    /** The ended. */
    private boolean ended;
    /** The position. */
    private final Coords position;
    /** The pattern. */
    private final AnimationPattern pattern;

    /**
     * Instantiates a new single frame animation.
     * 
     * @param pattern the pattern
     * @param position the position
     */
    public SingleFrameAnimation(final AnimationPattern pattern, final Coords position)
    {
        this.pattern = pattern;
        this.position = position.moveBy(pattern.getShift());
        if(!pattern.getFrames().isEmpty())
        {
            current = pattern.getFrames().get(0).image;
        }
        else
        {
            current = null;
        }
        ended = false;
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#getDrawableImage()
     */
    public DrawableImage getDrawableImage()
    {
        return new DrawableImage(current, position);
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#getDrawableImage(rts.misc.Coords)
     */
    public DrawableImage getDrawableImage(final Coords point)
    {
        return new DrawableImage(current, point.moveBy(pattern.getShift()));
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#scheduleOn(java.util.Timer)
     */
    public void scheduleOn(final Timer effectTimer)
    {
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#stop()
     */
    public void stop()
    {
        ended = true;
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#hasEnded()
     */
    @Override
    public boolean hasEnded()
    {
        return ended;
    }
}
