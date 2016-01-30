package rts.misc;

import java.awt.Point;
import rts.view.mapView.GameGraphics;

// TODO: Auto-generated Javadoc
/**
 * Nieedytowalny punkt w przestrzeni dwuwymiarowej
 */
public class Coords extends Shape// extends AbstractCoords
{
    /** The x. */
    private final int x;
    /** The y. */
    private final int y;

    /**
     * Instantiates a new coords.
     */
    public Coords()
    {
        this(0, 0);
    }
    /**
     * Instantiates a new coords.
     * 
     * @param x the x
     * @param y the y
     */
    public Coords(final int x, final int y)
    {
        this.x = (x);
        this.y = (y);
    }
    /**
     * Instantiates a new coords.
     * 
     * @param coords the coords
     */
    public Coords(final Coords coords)
    {
        x = (coords.getX());
        y = (coords.getY());
    }
    /**
     * Instantiates a new coords.
     * 
     * @param point the point
     */
    public Coords(final Point point)
    {
        x = (point.x);
        y = (point.y);
    }
    /**
     * Instantiates a new coords.
     * 
     * @param coordX the coord x
     * @param coordY the coord y
     */
    public Coords(final double coordX, final double coordY)
    {
        x = ((int) Math.round(coordX));
        y = ((int) Math.round(coordY));
    }
    /**
     * Zwraca wektor polozenia tego punktu
     * 
     * @return the game vector
     */
    public GameVector vector()
    {
        return new GameVector(x, y);
    }
    /**
     * Gets the x.
     * 
     * @return the x
     */
    public int getX()
    {
        return x;
    }
    /**
     * Gets the y.
     * 
     * @return the y
     */
    public int getY()
    {
        return y;
    }
    /**
     * To point.
     * 
     * @return the point
     */
    public Point toPoint()
    {
        return new Point(getX(), getY());
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Coords: [" + getX() + ", " + getY() + "]";
    }
    /**
     * Distance.
     * 
     * @param another the another
     * @return the double
     */
    public double distance(final Coords another)
    {
        final int xDiff = another.x - x;
        final int yDiff = another.y - y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        // toPoint().distance(another.toPoint());
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object another)
    {
        if(!(another instanceof Coords))
        {
            return false;
        }
        final Coords coords = (Coords) another;
        return coords.getX() == getX() && coords.getY() == getY();
    }
    /**
     * Zwraca punkt przemieszczony wzgledem tego
     * 
     * @param rx the rx
     * @param ry the ry
     * @return the coords
     */
    public Coords moveBy(final int rx, final int ry)
    {
        return new Coords(getX() + rx, getY() + ry);
    }
    /**
     * Zwraca punkt przemieszczony wzgledem tego
     * 
     * @param vector wektor
     * @return the coords
     */
    public Coords moveBy(final GameVector vector)
    {
        return new Coords(getX() + vector.getX(), getY() + vector.getY());
    }
    /**
     * Zwraca punkt przemieszczony wzgledem tego o wektor polozenia innego
     * punktu.
     * 
     * @param other inny punkt
     * @return the coords
     */
    public Coords moveBy(final Coords other)
    {
        return new Coords(getX() + other.getX(), getY() + other.getY());
    }
    /**
     * Mnozy wspolrzedne polozenia
     * 
     * @param number the number
     * @return the coords
     */
    public Coords multiplyBy(final double number)
    {
        return new Coords(x * number, y * number);
    }
    /**
     * Neguje wspolrzedne polozenia
     * 
     * @return the coords
     */
    public Coords negate()
    {
        return new Coords(-x, -y);
    }
    /*
     * (non-Javadoc)
     *
     * @see rts.misc.Shape#draw(rts.view.mapView.GameGraphics)
     */
    @Override
    public void draw(final GameGraphics g)
    {
        // super.draw(g);
        g.setFilled(false);
        g.drawRect(new ConstRect(x - 1, y - 1, 3, 3));
    }
}
