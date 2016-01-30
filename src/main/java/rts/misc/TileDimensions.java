package rts.misc;

import java.awt.Dimension;
import rts.model.map.Tile;

// TODO: Auto-generated Javadoc
/**
 * Wymiary w jednostkach kwadratow terenu mapy.
 */
public class TileDimensions extends Size
{
    public TileDimensions()
    {
        super();
    }
    public TileDimensions(final Dimension dim)
    {
        super(dim);
    }
    public TileDimensions(final int x, final int y)
    {
        super(x, y);
    }
    public TileDimensions(final int size)
    {
        super(size);
    }
    public Size convertToSize()
    {
        return new Size(getDimX() << Tile.SIZE_LOG2, getDimY() << Tile.SIZE_LOG2);
    }
}
