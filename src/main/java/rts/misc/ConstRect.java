package rts.misc;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import rts.view.mapView.GameGraphics;

// TODO: Auto-generated Javadoc
/**
 * Nieedytowalny prostokat.
 */
public class ConstRect extends Shape
{
    /** Przechowuje wspolrzedna x lewego gornego rogu prostokata. */
    private final int x;
    /** Przechowuje wspolrzedna y lewego gornego rogu prostokata. */
    private final int y;
    /** Przechowuje szerokosc prostokata. */
    private final int width;
    /** Przechowuje wysokosc prostokata. */
    private final int height;

    /**
     * Tworzy prostokat o lewym gornym rogu w punkcie (0,0), o zerowych
     * wymiarach
     */
    public ConstRect()
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }
    /**
     * Tworzy prostokat o lewym gornym rogu w danym punkcie, o zadanych
     * wymiarach
     * 
     * @param upperLeft lewy gorny rog
     * @param width szerokosc
     * @param height wysokosc
     */
    public ConstRect(final Coords upperLeft, final int width, final int height)
    {
        x = upperLeft.getX();
        y = upperLeft.getY();
        this.width = width;
        this.height = height;
    }
    /**
     * Tworzy prostokat o lewym gornym rogu w danym punkcie, o zadanych
     * wymiarach
     * 
     * @param x wspolrzedna x punktu
     * @param y wspolrzedna y punktu
     * @param width szerokosc
     * @param height wysokosc
     */
    public ConstRect(final int x, final int y, final int width, final int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    /**
     * Tworzy najmniejszy prostokat zawierajacy zadane punkty
     * 
     * @param a punkt a
     * @param b punkt b
     */
    public ConstRect(final Coords a, final Coords b)
    {
        final int aX = Math.min(a.getX(), b.getX());
        final int aY = Math.min(a.getY(), b.getY());
        final int bX = Math.max(a.getX(), b.getX());
        final int bY = Math.max(a.getY(), b.getY());
        x = aX;
        y = aY;
        width = bX - aX;
        height = bY - aY;
    }
    /**
     * Tworzy prostokat identyczny do zadanego prostokata
     * 
     * @param rect zadany prostokat
     */
    public ConstRect(final ConstRect rect)
    {
        x = rect.x;
        y = rect.y;
        width = rect.width;
        height = rect.height;
    }
    /**
     * Tworzy prostokat z obiektu Rectangle2D
     * 
     * @param rect obiekt Rectangle2D
     */
    public ConstRect(final Rectangle2D rect)
    {
        x = (int) Math.round(rect.getX());
        y = (int) Math.round(rect.getY());
        width = (int) Math.round(rect.getWidth());
        height = (int) Math.round(rect.getHeight());
    }
    /**
     * Tworzy prostokat o lewym gornym rogu w danym punkcie, o zadanych
     * wymiarach
     * 
     * @param upperLeft lewy gorny rog
     * @param dimensions zadane wymiary
     */
    public ConstRect(final Coords upperLeft, final Size dimensions)
    {
        x = upperLeft.getX();
        y = upperLeft.getY();
        width = dimensions.getDimX();
        height = dimensions.getDimY();
    }
    /**
     * Instantiates a new const rect.
     * 
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     */
    public ConstRect(final double x, final double y, final double width, final double height)
    {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
        this.width = (int) Math.round(width);
        this.height = (int) Math.round(height);
    }
    /**
     * Tworzy prostokat w danym punkcie, o zerowych wymiarach
     * 
     * @param a the a
     */
    public ConstRect(final Coords a)
    {
        x = a.getX();
        y = a.getY();
        width = 0;
        height = 0;
    }
    /**
     * Tworzy prostokat o zadanych wymiarach, ze srodkiem w danym punkcie
     * 
     * @param position srodek prostokata
     * @param dimensions zadane wymiary
     * @return stworzony prostokat
     */
    public static ConstRect fromCenter(final CoordsDouble position, final Size dimensions)
    {
        return new ConstRect(position.moveBy(-(double) dimensions.getDimX() / 2,
            -(double) dimensions.getDimY() / 2).toCoords(), dimensions.getDimX(), dimensions
                .getDimY());
    }
    /**
     * Ucina obszar prostokata znajdujacy sie poza drugim prostokatem
     * 
     * @param another drugi prostokat
     * @return zmodyfikowany prostokat
     */
    public ConstRect cropBy(final ConstRect another)
    {
        // final ConstRect ret = new ConstRect(this);
        int xx = x, yy = y, w = width, h = height;
        if(x < another.x)
        {
            xx = another.x;
            w = width - (xx - x);// another.x + another.width - xx - 1;
        }
        else if(x + width > another.x + another.width)
        {
            w = another.x + another.width - x - 1;
        }
        if(y < another.y)
        {
            yy = another.y;
            h = height - (yy - y);
        }
        else if(y + height > another.y + another.height)
        {
            h = another.y + another.height - y - 1;
        }
        return new ConstRect(xx, yy, w, h);
    }
    /**
     * Gets the upper left.
     * 
     * @return the upper left
     */
    public Coords getUpperLeft()
    {
        return new Coords(x, y);
    }
    /**
     * Gets the lower right.
     * 
     * @return the lower right
     */
    public Coords getLowerRight()
    {
        return new Coords(x + width, y + height);
    }
    /**
     * Gets the upper right.
     * 
     * @return the upper right
     */
    public Coords getUpperRight()
    {
        return new Coords(x + width, y);
    }
    /**
     * Gets the lower left.
     * 
     * @return the lower left
     */
    public Coords getLowerLeft()
    {
        return new Coords(x, y + height);
    }
    /**
     * Sprawdza, czy prostokat zawiera dany punkt
     * 
     * @param point dany punkt
     * @return true, jesli prostokat zawiera dany punkt
     */
    public boolean contains(final Coords point)
    {
        return toRectangle().contains(point.toPoint());
    }
    public boolean contains(final ConstRect building)
    {
        return this.contains(building.getUpperLeft()) && this.contains(building.getLowerRight());
    }
    /**
     * Sprawdza, czy istnieje punkt nalezacy do obu prostokatow
     * 
     * @param other drugi prostokat
     * @return true, jesli istnieje punkt nalezacy do obu prostokatow
     */
    public boolean intersects(final Rectangle2D other)
    {
        return other.intersects(new Rectangle2D.Float(x, y, width, height));
    }
    /**
     * Sprawdza, czy istnieje punkt nalezacy do obu prostokatow
     * 
     * @param other drugi prostokat
     * @return true, jesli istnieje punkt nalezacy do obu prostokatow
     */
    public boolean intersects(final ConstRect other)
    {
        final Rectangle rect = toRectangle();
        if(width == 0 && height == 0 && other.width == 0 && other.height == 0)
        {
            return new Point(x, y).equals(new Point(other.x, other.y));
        }
        else if(width == 0 && height == 0)
        {
            return other.toRectangle().contains(new Point(x, y));
        }
        else if(other.width == 0 && other.height == 0)
        {
            return rect.contains(new Point(other.x, other.y));
        }
        else if(other.width == 0 || other.height == 0)
        {
            return rect.intersectsLine(new Line2D.Double(other.x, other.y, other.x + other.width,
                    other.y + other.height));
        }
        else if(width == 0 || height == 0)
        {
            return other.toRectangle().intersectsLine(
                new Line2D.Double(x, y, x + width, y + height));
        }
        else
        {
            return rect.intersects(other.toRectangle());
        }
    }
    /**
     * Zwraca punkt bedacy srodkiem prostokata
     * 
     * @return srodek prostokata
     */
    public Coords getCenter()
    {
        return new Coords(Math.round(x + width / 2), Math.round(y + height / 2));
    }
    /**
     * Dla zadanego punktu zwraca najblizszy punkt w prostokacie
     * 
     * @param point zadany punkt
     * @return najblizszy punkt w prostokacie
     */
    public Coords bound(final Coords point)
    {
        int xx = point.getX();
        int yy = point.getY();
        if(xx < x)
        {
            xx = x;
        }
        if(xx > x + width)
        {
            xx = x + width;
        }
        if(yy < y)
        {
            yy = y;
        }
        if(yy > y + height)
        {
            yy = y + height;
        }
        return new Coords(xx, yy);
    }
    /**
     * Zwraca prostokat zawarty w calosci w drugim prostokacie, rowny co do
     * rozmiaru i najblizszy polozeniu aktualnego prostokata
     * 
     * @param other drugi prostokat
     * @return przesuniety prostokat, lub null gdy nie da sie zmiescic
     */
    public ConstRect boundBy(final ConstRect other)
    {
        int posX = x;
        int posY = y;
        final int dimX = width;
        final int dimY = height;
        if(posX < other.getX())
        {
            posX = other.getX();
        }
        else if(posX + dimX > other.getWidth())
        {
            posX = (other.getWidth() - dimX);
        }
        if(posY < other.getY())
        {
            posY = other.getY();
        }
        else if(posY + dimY > other.getHeight())
        {
            posY = (other.getHeight() - dimY);
        }
        return new ConstRect(posX, posY, dimX, dimY);
    }
    /**
     * Zwraca linie odpowiadajace bokom prostokata
     * 
     * @return linie odpowiadajace bokom prostokata
     */
    public List<Line> getEdgeLines()
    {
        final List<Line> lineList = new ArrayList<Line>();
        lineList.add(new Line(getUpperLeft(), getLowerLeft()));
        lineList.add(new Line(getLowerLeft(), getLowerRight()));
        lineList.add(new Line(getLowerRight(), getUpperRight()));
        lineList.add(new Line(getUpperRight(), getUpperLeft()));
        return lineList;
    }
    /**
     * Zwraca punkt przeciecia tego prostokata z odcinkiem laczacym srodek
     * prostokata z zadanym punktem.
     * 
     * @param otherPoint zadany punkt
     * @return punkt przeciecia lub null jesli zadany punkt lezy wewnatrz
     *         prostokata
     */
    public Coords getFromCenterIntersectionPoint(final Coords otherPoint)
    {
        final Line line = new Line(getCenter(), otherPoint);
        final List<Line> edgeLines = getEdgeLines();
        Coords point;
        for(final Line edge : edgeLines)
        {
            point = line.intersection(edge);
            if(point != null)
            {
                return point;
            }
        }
        return null;
    }
    /**
     * Zwraca obiekt Rectangle odpowiadajacy temu prostokatowi
     * 
     * @return the rectangle
     */
    public Rectangle toRectangle()
    {
        return new Rectangle(x, y, width, height);
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return ("Rect: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
    }
    /**
     * Gets the x.
     * 
     * @return the x
     */
    public int getX()
    {
        return x;
    }
    /**
     * Gets the y.
     * 
     * @return the y
     */
    public int getY()
    {
        return y;
    }
    /**
     * Gets the width.
     * 
     * @return the width
     */
    public int getWidth()
    {
        return width;
    }
    /**
     * Gets the height.
     * 
     * @return the height
     */
    public int getHeight()
    {
        return height;
    }
    public ConstRect setX(final int xx)
    {
        return new ConstRect(xx, y, width, height);
    }
    public ConstRect setY(final int yy)
    {
        return new ConstRect(x, yy, width, height);
    }
    public ConstRect setWidth(final int w)
    {
        return new ConstRect(x, y, w, height);
    }
    public ConstRect setHeight(final int h)
    {
        return new ConstRect(x, y, width, h);
    }
    /**
     * Przesuwa prostokat o wektor.
     * 
     * @param rx wspolrzedna x wektora
     * @param ry wspolrzedna y wektora
     * @return zmodyfikowany prostokat
     */
    public ConstRect moveBy(final int rx, final int ry)
    {
        return new ConstRect(x + rx, y + ry, width, height);
    }
    /**
     * Przesuwa prostokat o wektor.
     * 
     * @param v wektor
     * @return zmodyfikowany prostokat
     */
    public ConstRect moveBy(final GameVector v)
    {
        return new ConstRect((int) Math.round(x + v.getX()), (int) Math.round(y + v.getY()), width,
                height);
    }
    /**
     * Przesuwa prostokat o wektor polozenia danego punktu
     * 
     * @param v dany punkt
     * @return zmodyfikowany prostokat
     */
    public ConstRect moveBy(final Coords v)
    {
        return new ConstRect(x + v.getX(), y + v.getY(), width, height);
    }
    /*
     * (non-Javadoc)
     *
     * @see rts.misc.Shape#moveShape(rts.misc.GameVector)
     */
    //    @Override
    //    public Shape moveShape(final GameVector v)
    //    {
    //        //        super.moveShape(v);
    //
    //        return moveBy(v);
    //    }
    /**
     * Multiply by.
     * 
     * @param number the number
     * @return zmodyfikowany prostokat
     */
    public ConstRect multiplyBy(final double number)
    {
        return new ConstRect(x * number, y * number, width * number, height * number);
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if(!(object instanceof ConstRect))
        {
            return false;
        }
        final ConstRect rect = (ConstRect) object;
        return rect.x == x && rect.y == y && rect.width == width && rect.height == height;
    }
    /**
     * Przesuwa wszystkie boki prostokata od srodka o zadana odleglosc
     * 
     * @param amount zadany rozmiar
     * @return zmodyfikowany prostokat
     */
    public ConstRect expandFromCenter(final int amount)
    {
        return new ConstRect(new Coords(getX() - amount, getY() - amount), getWidth() + 2 * amount,
                getHeight() + 2 * amount);
    }
    /**
     * Zwieksza wymiary prostokata o zadany rozmiar
     * 
     * @param amount zadany rozmiar
     * @return zmodyfikowany prostokat
     */
    public ConstRect expandSize(final int amount)
    {
        return new ConstRect(x, y, width + amount, height + amount);
    }
    /**
     * Draw.
     * 
     * @param g the g
     */
    public void draw(final Graphics g)
    {
        g.drawRect(x, y, width, height);
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
        // // g.fillRect(this);
        // }
        // else
        // {
        g.drawRect(this);
        // }
    }
    /**
     * Skaluje prostokat bez zmiany proporcji, aby zmiescic go w drugim
     * prostokacie
     * 
     * @param other drugi prostokat
     * @return zmodyfikowany prostokat
     */
    public ConstRect scaleToFitIn(final ConstRect other)
    {
        final double xx = Math.abs((double) other.width / width);
        final double yy = Math.abs((double) other.height / height);
        int w, h;
        if(xx <= yy)
        {
            w = other.getWidth();
            h = (int) Math.round(height * ((double) other.width / width));
        }
        else
        {
            h = other.getHeight();
            w = (int) Math.round(width * ((double) other.height / height));
        }
        final ConstRect constRect = new ConstRect(0, 0, w, h);
        return constRect;
    }
    /**
     * Przesuwa prostokat, aby byl wspolsrodkowy z drugim prostokatem
     * 
     * @param other drugi prostokat
     * @return zmodyfikowany prostokat
     */
    public ConstRect centerIn(final ConstRect other)
    {
        final CoordsDouble center = new CoordsDouble(other.x + other.width / 2, other.y
            + other.height / 2);
        return new ConstRect(center.moveBy(-(double) width / 2, -(double) height / 2).toCoords(),
                width, height);
    }
}
