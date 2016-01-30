package rts.misc;

import rts.view.mapView.GameGraphics;

// TODO: Auto-generated Javadoc
/**
 * Owal
 */
public class Oval extends Shape
{
    /** Srodek. */
    private final Coords center;
    /** The width. */
    private final int width;
    /** The height. */
    private final int height;

    /**
     * Instantiates a new oval.
     * 
     * @param center the center
     * @param width the width
     * @param height the height
     */
    public Oval(final Coords center, final int width, final int height)
    {
        super();
        this.center = center;
        this.width = width;
        this.height = height;
    }
    /**
     * Gets the center.
     * 
     * @return the center
     */
    public Coords getCenter()
    {
        return center;
    }
    /**
     * Gets the width.
     * 
     * @return the width
     */
    public int getWidth()
    {
        return width;
    }
    /**
     * Gets the height.
     * 
     * @return the height
     */
    public int getHeight()
    {
        return height;
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
        // if(filled)
        // {
        // g.fillOval(this);
        // }
        // else
        // {
        g.drawOval(this);
        // }
    }
}
