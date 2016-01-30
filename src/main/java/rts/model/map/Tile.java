package rts.model.map;

import java.awt.Image;
import rts.misc.Coords;
import rts.view.mapView.Drawable;
import rts.view.mapView.GameGraphics;

// TODO: Auto-generated Javadoc
/**
 * Kwadrat bedacy elementem terenu i jednostka budowy budynkow.
 */
public class Tile implements Drawable
{
    /** The Constant SIZE. */
    public static final int SIZE = 32;
    /** The Constant SIZE_LOG2. */
    public static final int SIZE_LOG2 = 5;
    /** The bg. */
    private final Image bg;
    /** The reachable. */
    private boolean reachable = true;
    /** The buildable. */
    private boolean buildable = true;
    /** The ind x. */
    public final int indX;
    /** The ind y. */
    public final int indY;
    /** The center. */
    private final Coords center;
    /** The upper left. */
    private final Coords upperLeft;

    /**
     * Instantiates a new tile.
     * 
     * @param x the x
     * @param y the y
     * @param im the im
     */
    public Tile(final int x, final int y, final Image im)
    {
        indX = x;
        indY = y;
        bg = im;
        upperLeft = new Coords(indX << Tile.SIZE_LOG2, indY << Tile.SIZE_LOG2);
        center = new Coords((indX << Tile.SIZE_LOG2) + (SIZE >> 1), (indY << Tile.SIZE_LOG2)
            + (SIZE >> 1));
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
     * Gets the position.
     * 
     * @return the position
     */
    public Coords getPosition()
    {
        return upperLeft;
    }
    /**
     * Sets the reachable.
     * 
     * @param val the new reachable
     */
    public void setReachable(final boolean val)
    {
        buildable = (val ? buildable : false);
        reachable = val;
    }
    /**
     * Sets the buildable.
     * 
     * @param val the new buildable
     */
    public void setBuildable(final boolean val)
    {
        buildable = val;
        reachable = (!val ? reachable : true);
    }
    /**
     * Checks if is buildable.
     * 
     * @return true, if is buildable
     */
    public boolean isBuildable()
    {
        return buildable;
    }
    /**
     * Checks if is reachable.
     * 
     * @return true, if is reachable
     */
    public boolean isReachable()
    {
        return reachable;
    }
    /* (non-Javadoc)
     * @see rts.view.mapView.Drawable#draw(rts.view.mapView.GameGraphics)
     */
    @Override
    public void draw(final GameGraphics g)
    {
        g.drawImage(bg, upperLeft);
    }
}
