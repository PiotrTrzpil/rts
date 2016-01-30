package rts.misc;

import java.awt.geom.Ellipse2D;
import rts.view.mapView.GameGraphics;

// TODO: Auto-generated Javadoc
/**
 * Owal w reprezentacji zmiennoprzecinkowej
 */
public class OvalDouble extends Shape
{
    /** The center. */
    private final CoordsDouble center;
    /** The width. */
    private final double width;
    /** The height. */
    private final double height;

    /**
     * Instantiates a new oval double.
     * 
     * @param center the center
     * @param width the width
     * @param height the height
     */
    public OvalDouble(final CoordsDouble center, final double width, final double height)
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
    public CoordsDouble getCenter()
    {
        return center;
    }
    /**
     * Gets the width.
     * 
     * @return the width
     */
    public double getWidth()
    {
        return width;
    }
    /**
     * Gets the height.
     * 
     * @return the height
     */
    public double getHeight()
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
        // g.getGraphics().fill(toEllipse2D());
        // }
        // else
        // {
        g.getGraphics().draw(toEllipse2D());
        // }
    }
    /**
     * To ellipse2 d.
     * 
     * @return the ellipse2 d. double
     */
    public Ellipse2D.Double toEllipse2D()
    {
        final CoordsDouble upperLeft = getUpperLeft();
        return new Ellipse2D.Double(upperLeft.getX(), upperLeft.getY(), width, height);
    }
    /**
     * Gets the upper left.
     * 
     * @return the upper left
     */
    public CoordsDouble getUpperLeft()
    {
        return new CoordsDouble(center.getX() - width / 2, center.getY() - height / 2);
    }
}
