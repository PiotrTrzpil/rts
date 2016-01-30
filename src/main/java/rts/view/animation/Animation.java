package rts.view.animation;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import rts.misc.Coords;
import rts.view.animation.AnimationPattern.Frame;
import rts.view.mapView.DrawableImage;

// TODO: Auto-generated Javadoc
/**
 * Animacja jednostanowa, zawierajaca wiecej niz jeden obrazek.
 */
public class Animation extends TimerTask implements AbstractAnimation
{
    /** The position. */
    private final Coords position;
    /** Lista obrazkow (klatek) animacji. */
    private final ArrayList<Frame> frames;
    /** Aktualny obrazek */
    private int currentFrame;
    /** Czas jaki uplynal od poczatku animacji. */
    private long elapsedTime;
    /** Calkowity czas trwania. */
    private final int totalTime;
    /** Czy animacja sie zakonczyla. */
    private boolean ended = false;
    /** Chwila poczatku animacji. */
    private long startTime;
    /** Wzor animacji. */
    private final AnimationPattern pattern;

    /**
     * Instantiates a new animation.
     * 
     * @param pattern the pattern
     * @param position the position
     */
    public Animation(final AnimationPattern pattern, final Coords position)
    {
        this.pattern = pattern;
        this.position = position.moveBy(pattern.getShift());
        frames = pattern.getFrames();
        totalTime = pattern.getTotalTime();
        startTime = System.currentTimeMillis();
    }
    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run()
    {
        elapsedTime = System.currentTimeMillis() - startTime;
        updateState();
    }
    /**
     * Aktualizuje stan aimacjiS
     */
    public void updateState()
    {
        if(elapsedTime >= totalTime)
        {
            if(pattern.isLooping())
            {
                elapsedTime = elapsedTime % totalTime;
                startTime = System.currentTimeMillis() - elapsedTime;
                currentFrame = 0;
            }
            else
            {
                cancel();
                ended = true;
                return;
            }
        }
        if(frames.size() > 1)
        {
            while(elapsedTime > frames.get(currentFrame).endTime)
            {
                currentFrame++;
            }
        }
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#hasEnded()
     */
    public boolean hasEnded()
    {
        return ended;
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#getDrawableImage()
     */
    public DrawableImage getDrawableImage()
    {
        return new DrawableImage(frames.get(currentFrame).image, position);
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#getDrawableImage(rts.misc.Coords)
     */
    public DrawableImage getDrawableImage(final Coords point)
    {
        return new DrawableImage(frames.get(currentFrame).image, point.moveBy(pattern.getShift()));
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#scheduleOn(java.util.Timer)
     */
    public void scheduleOn(final Timer effectTimer)
    {
        effectTimer.schedule(this, 0, pattern.getFrameDuration());
    }
    /* (non-Javadoc)
     * @see rts.view.animation.Anim#stop()
     */
    public void stop()
    {
        cancel();
        ended = true;
    }
}
