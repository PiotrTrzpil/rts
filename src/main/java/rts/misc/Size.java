package rts.misc;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * Wymiary
 */
public class Size implements Serializable
{
    /** The dim x. */
    private final int dimX;
    /** The dim y. */
    private final int dimY;

    public Size(final java.awt.Dimension dim)
    {
        dimX = dim.height;
        dimY = dim.width;
    }
    public Size(final int x, final int y)
    {
        dimX = x;
        dimY = y;
    }
    public Size()
    {
        dimX = 0;
        dimY = 0;
    }
    public Size(final int size)
    {
        dimX = size;
        dimY = size;
    }
    /**
     * Gets the dim y.
     * 
     * @return the dim y
     */
    public int getDimY()
    {
        return dimY;
    }
    /**
     * Gets the dim x.
     * 
     * @return the dim x
     */
    public int getDimX()
    {
        return dimX;
    }
    public Size setDimY(final int y)
    {
        return new Size(dimX, y);
    }
    public Size setDimX(final int x)
    {
        return new Size(x, dimY);
    }
    public Size add(final int i)
    {
        return new Size(dimX + i, dimY + i);
    }
}
