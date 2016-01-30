package rts.view.mapView;

import java.awt.Image;
import rts.misc.Coords;

public class DrawableImage implements Drawable
{
    private final Image image;
    private Coords position;

    public DrawableImage(final Image image, final Coords position)
    {
        this.image = image;
        this.position = position;
    }
    public DrawableImage()
    {
        image = null;
        position = new Coords(0, 0);
    }
    @Override
    public void draw(final GameGraphics g)
    {
        g.drawImage(image, position);
    }
    public Coords getPosition()
    {
        return position;
    }
    public void setPosition(final Coords coords)
    {
        position = coords;
    }
}
