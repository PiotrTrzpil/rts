package rts.misc;

public class CoordsDouble// extends Coords
{
    private final double x;
    private final double y;

    public CoordsDouble()
    {
        this(0, 0);
    }
    public CoordsDouble(final CoordsDouble coords)
    {
        x = coords.x;
        y = coords.y;
    }
    public CoordsDouble(final double coordX, final double coordY)
    {
        x = coordX;
        y = coordY;
    }
    public CoordsDouble(final int coordX, final int coordY)
    {
        x = coordX;
        y = coordY;
    }
    public CoordsDouble(final Coords coords)
    {
        x = coords.getX();
        y = coords.getY();
    }
    public CoordsDouble moveBy(final GameVector v)
    {
        return new CoordsDouble(getX() + v.getX(), getY() + v.getY());
    }
    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
    public Coords toCoords()
    {
        return new Coords(x, y);
    }
    public CoordsDouble moveBy(final double x, final double y)
    {
        return new CoordsDouble(getX() + x, getY() + y);
    }
}
