package rts.view.clientModel;

import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.CoordsDouble;
import rts.misc.Line;
import rts.view.mapView.DrawableImage;
import rts.view.mapView.DrawableSprite;

// TODO: Auto-generated Javadoc
/**
 * The Class MapObjectSprite.
 */
public abstract class MapObjectSprite
{
    /** Obrazek. */
    private final ObjectImage sprite;
    /** Polozenie. */
    protected CoordsDouble position;
    /** Czy obiekt jest widoczny. */
    private boolean visible;

    /**
     * Instantiates a new map object sprite.
     * 
     * @param sprite the sprite
     * @param c the c
     */
    public MapObjectSprite(final ObjectImage sprite, final Coords c)
    {
        this.sprite = sprite;
        position = new CoordsDouble(c);
        visible = true;
    }
    /**
     * Zwraca wymiary polozenie
     * 
     * @return the bounds
     */
    public ConstRect getBounds()
    {
        return ConstRect.fromCenter(position, sprite.getDimensions());
    }
    /**
     * Gets the drawable sprite.
     * 
     * @return the drawable sprite
     */
    public DrawableSprite getDrawableSprite()
    {
        final ConstRect rect = getBounds();
        final Line line = new Line(rect.getUpperLeft(), rect.getLowerRight());
        final Coords pos = position.moveBy(sprite.getDrawShift()).toCoords();
        final DrawableSprite drawableSprite = new DrawableSprite(new DrawableImage(sprite
                .getImage(), pos), pos, line);
        return drawableSprite;
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
     * Sets the visible.
     * 
     * @param b the new visible
     */
    public void setVisible(final boolean b)
    {
        visible = b;
    }
    /**
     * Checks if is visible.
     * 
     * @return true, if is visible
     */
    public boolean isVisible()
    {
        return visible;
    }
}
