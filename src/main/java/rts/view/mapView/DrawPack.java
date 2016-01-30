package rts.view.mapView;

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import rts.misc.GameVector;
import rts.model.map.Tile;

// TODO: Auto-generated Javadoc
/**
 * W obiekcie tej klasy zbierane sa wszystkie obiekty do narysowania w swiecie
 * gry.
 */
public class DrawPack
{
    private final GameVector viewShift;
    private final List<List<Drawable>> layers;
    /** The sprites. */
    private final List<DrawableSprite> sprites;
    /** The tiles. */
    private final List<Tile> tiles;

    public DrawPack(final List<DrawableSprite> sprites, final List<Tile> tiles2,
        final GameVector viewShift)
    {
        this.sprites = sprites;
        Collections.sort(sprites);
        tiles = tiles2;
        this.viewShift = viewShift;
        layers = new LinkedList<List<Drawable>>();
    }
    public void addLayer(final List<Drawable> drawable)
    {
        layers.add(drawable);
    }
    public void addLayer(final Drawable drawable)
    {
        final List<Drawable> linkedList = new LinkedList<Drawable>();
        linkedList.add(drawable);
        layers.add(linkedList);
    }
    /**
     * Draw.
     * 
     * @param g the g
     */
    public void draw(final Graphics g)
    {
        final GameGraphics gg = new GameGraphics(g, viewShift);
        for(final Tile tile : tiles)
        {
            tile.draw(gg);
        }
        for(final DrawableSprite sprite : sprites)
        {
            sprite.draw(gg);
        }
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(final List<Drawable> layer : layers)
        {
            for(final Drawable d : layer)
            {
                d.draw(gg);
            }
        }
    }
}
