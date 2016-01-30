package rts.model;

import rts.misc.Coords;
import rts.misc.CoordsDouble;

public interface AbstractMapObject
{
    public void setPosition(final CoordsDouble coordsDouble);
    public Coords getPosition();
}
