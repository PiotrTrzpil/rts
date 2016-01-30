package rts.view.animation;

import rts.misc.Coords;

public interface AbstractAnimationPattern
{
    public AbstractAnimation newAnimation(final Coords position);
}
