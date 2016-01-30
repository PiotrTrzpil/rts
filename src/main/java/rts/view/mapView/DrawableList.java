package rts.view.mapView;

import java.util.LinkedList;

public class DrawableList extends LinkedList<Drawable> implements Drawable
{
    @Override
    public void draw(final GameGraphics g)
    {
        for(final Drawable d : this)
        {
            d.draw(g);
        }
    }
}
