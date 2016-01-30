package rts.model;

import rts.misc.Coords;
import rts.misc.CoordsDouble;

// TODO: Auto-generated Javadoc
/**
 * Obiekt znajdujacy sie na mapie.
 */
public abstract class MapObject
{
    /** Polozenie obiektu. */
    private CoordsDouble position;

    /**
     * Inicjalizacja parametrow
     * 
     * @param c the c
     */
    public void initParametrs(final Coords c)
    {
        position = new CoordsDouble(c);
    }
    /**
     * Sets the position.
     * 
     * @param coordsDouble the new position
     */
    public void setPosition(final CoordsDouble coordsDouble)
    {
        position = coordsDouble;
    }
    /**
     * Gets the position.
     * 
     * @return the position
     */
    public Coords getPosition()
    {
        return position.toCoords();
    }
    /**
     * Odleglosc od innego obiektu
     * 
     * @param other the other
     * @return the double
     */
    public double distance(final MapObject other)
    {
        return other.getPosition().distance(getPosition());
    }
}
