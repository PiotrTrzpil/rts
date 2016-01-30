package rts.view.clientModel.misc;

import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.Line;
import rts.model.map.Tile;

public class StaticObject// extends MapObject
{

    private final int dimX = 6;
    private final int dimY = 6;

    private final Coords pos;
    private final ConstRect bounds;

    public StaticObject(final int x, final int y)
    {
        pos = new Coords(x * Tile.SIZE + (Tile.SIZE >> 1), y * Tile.SIZE + (Tile.SIZE >> 1));
        bounds = new ConstRect(pos, dimX, dimY);

    }

    public Coords getRefPoint()
    {
        return pos;
    }

    public Line getRefLine()
    {

        return new Line(bounds.getUpperLeft(), bounds.getLowerRight());
    }

    public Coords getPos()
    {
        return pos;
    }

}
