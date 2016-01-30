package rts.view.mapView;

import java.awt.Color;
import rts.misc.Shape;

public class ColorShape implements Drawable
{
    private final Color color;
    private final Shape shape;

    public ColorShape(final Color color, final Shape shape)
    {
        this.color = color;
        this.shape = shape;
    }

    public void draw(final GameGraphics g)
    {
        g.setColor(color);
        shape.draw(g);
    }

    public Color getColor()
    {
        return color;
    }

    public Shape getShape()
    {
        return shape;
    }

}