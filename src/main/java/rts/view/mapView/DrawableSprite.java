package rts.view.mapView;

import rts.misc.Coords;
import rts.misc.Line;

// TODO: Auto-generated Javadoc
/**
 * The Class DrawableSprite.
 */
public class DrawableSprite implements Drawable, Comparable<DrawableSprite>
{
    /** The image. */
    private final DrawableImage image;
    /** Punkt dzieki ktoremu okreslane jest przyslanianie obiektow przez inne. */
    private final Coords referencePoint;
    /** Linia dzieki ktorej okreslane jest przyslanianie obiektow przez inne. */
    private final Line referenceLine;
    private final DrawableList drawables;
    //    /** The draw point. */
    private final Coords drawPoint;

    /**
     * Instantiates a new drawable sprite.
     * 
     * @param image the image
     * @param referencePoint the reference point
     * @param referenceLine the reference line
     */
    public DrawableSprite(final DrawableImage image, final Coords referencePoint,
        final Line referenceLine)
    {
        this.image = image;
        drawPoint = image.getPosition();
        this.referencePoint = referencePoint;
        this.referenceLine = referenceLine;
        drawables = new DrawableList();
    }
    public void addDrawable(final Drawable d)
    {
        drawables.add(d);
    }
    /**
     * Linia dzieki ktorej okreslane jest przyslanianie obiektow przez inne.
     * 
     * @return the reference line
     */
    public Line getReferenceLine()
    {
        return referenceLine;
    }
    /**
     * Punkt dzieki ktoremu okreslane jest przyslanianie obiektow przez inne.
     * 
     * @return the reference point
     */
    public Coords getReferencePoint()
    {
        return referencePoint;
    }
    @Override
    public void draw(final GameGraphics g)
    {
        image.draw(g);
        for(final Drawable drawable : drawables)
        {
            drawable.draw(g);
        }
    }
    @Override
    public int compareTo(final DrawableSprite o2)
    {
        final Line l = getReferenceLine().orthogonal(o2.getReferencePoint());
        final int r = (l.getB().getX() - l.getA().getX());
        if(r < 0)
        {
            return 1;
        }
        else if(r > 0)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
    public Coords getDrawPoint()
    {
        return drawPoint;
    }
}
