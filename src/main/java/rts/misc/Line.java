package rts.misc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import rts.view.mapView.GameGraphics;

// TODO: Auto-generated Javadoc
/**
 * Odcinek miedzy dwoma punktami
 */
public class Line extends Shape
{
    /** The a. */
    private final Coords a;
    /** The b. */
    private final Coords b;
    /** The xm. */
    private double xm;
    /** The ym. */
    private double ym;

    /**
     * Instantiates a new line.
     */
    public Line()
    {
        a = new Coords();
        b = new Coords();
        prepareParams();
    }
    /**
     * Instantiates a new line.
     * 
     * @param a the a
     * @param b the b
     */
    public Line(final Coords a, final Coords b)
    {
        this.a = a;
        this.b = b;
        prepareParams();
    }
    /**
     * Instantiates a new line.
     * 
     * @param ax the ax
     * @param ay the ay
     * @param bx the bx
     * @param by the by
     */
    public Line(final int ax, final int ay, final int bx, final int by)
    {
        a = new Coords(ax, ay);
        b = new Coords(bx, by);
        prepareParams();
    }
    /**
     * Instantiates a new line.
     * 
     * @param ax the ax
     * @param ay the ay
     * @param bx the bx
     * @param by the by
     */
    public Line(final float ax, final float ay, final float bx, final float by)
    {
        a = new Coords((int) ax, (int) ay);
        b = new Coords((int) bx, (int) by);
        prepareParams();
    }
    /**
     * oblicza parametry nachylenia odcinka
     */
    private void prepareParams()
    {
        final int xx = b.getX() - a.getX();
        final int yy = b.getY() - a.getY();
        if(xx == 0)
        {
            xm = 0;
            ym = Math.signum(yy);
        }
        else if(yy == 0)
        {
            ym = 0;
            xm = Math.signum(xx);
        }
        else if(Math.abs(xx) > Math.abs(yy))
        {
            xm = Math.signum(xx);
            ym = ((double) yy) / (Math.abs(xx));
        }
        else
        {
            ym = Math.signum(yy);
            xm = ((double) xx) / (Math.abs(yy));
        }
    }
    /**
     * Zwraca punkty znajdujace sie na odciku, w zadanych odstepach
     * 
     * @param spaceBetween odstepy
     * @return the points on line
     */
    public List<Coords> getPointsOnLine(final int spaceBetween)
    {
        final List<Coords> points = new ArrayList<Coords>();
        double coordX = a.getX();
        double coordY = a.getY();
        while(Math.abs(coordX - a.getX()) <= Math.abs(b.getX() - a.getX())
            && Math.abs(coordY - a.getY()) <= Math.abs(b.getY() - a.getY()))
        {
            points.add(new Coords(coordX, coordY));
            coordX += xm * spaceBetween;
            coordY += ym * spaceBetween;
        }
        return points;
    }
    /**
     * Zwraca punkt przeciecia prostej reprezentowanej przez ten odcinek z inna
     * prosta.
     * 
     * @param other the other
     * @return the coords
     */
    public Coords intersection(final Line other)
    {
        final double xs = other.xm;
        final double ys = other.ym;
        final double aa = a.getX();
        final double bb = a.getY();
        final double cc = other.a.getX();
        final double dd = other.a.getY();
        if(!intersects(other))
        {
            return null;
        }
        final double s = ((aa - cc) * ym / xm + dd - bb) / (ym * xs / xm - ys);
        return new Coords(xs * s + cc, ys * s + dd);
    }
    /**
     * Sprawdza czy inny odcinek przecina ten
     * 
     * @param other the other
     * @return true, if successful
     */
    public boolean intersects(final Line other)
    {
        return toLine2D().intersectsLine(other.toLine2D());
    }
    /**
     * Zwraca linie prostopadla do tej linii, przechodzaca przez dany punkt
     * 
     * @param giv dany punkt
     * @return linia prostopadla
     */
    public Line orthogonal(final Coords giv)
    {
        final double ax = xm;
        final double ay = ym;
        final double aa = a.getX();
        final double bb = a.getY();
        final double bx = xm;
        final double by = -1 / ym;
        final double cc = giv.getX();
        final double dd = giv.getY();
        final double l = dd - bb - (ay / ax) * (cc - aa);
        final double m = (ay / ax) * bx - by;
        final double f = l / m;
        final int xx = (int) Math.round(bx * f + cc);
        final int yy = (int) Math.round(by * f + dd);
        final Line neww = new Line(giv.getX(), giv.getY(), xx, yy);
        return neww;
    }
    /**
     * Tworzy wektor z tego odcinka, od punktu a do b, o zadanej dlugosci
     * 
     * @param length zadana dlugosc
     * @return wektor
     */
    public GameVector getVector(final float length)
    {
        final double rx = b.getX() - a.getX();
        final double ry = b.getY() - a.getY();
        final double c = Math.sqrt(1 / (rx * rx + ry * ry));
        return new GameVector(length * c * rx, length * c * ry);
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
        g.drawLine(this);
    }
    /**
     * Draw.
     * 
     * @param g the g
     */
    public void draw(final Graphics2D g)
    {
        g.setColor(Color.red);
        g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
    }
    /**
     * Gets the a.
     * 
     * @return the a
     */
    public Coords getA()
    {
        return a;
    }
    /**
     * Gets the b.
     * 
     * @return the b
     */
    public Coords getB()
    {
        return b;
    }
    /**
     * Oblicza dlugosc odcinka
     * 
     * @return dlugosc odcinka
     */
    public float getLength()
    {
        final int xx = b.getX() - a.getX();
        final int yy = b.getY() - a.getY();
        return (float) Math.sqrt(xx * xx + yy * yy);
    }
    /**
     * To line2 d.
     * 
     * @return the line2 d
     */
    public Line2D toLine2D()
    {
        return new Line2D.Double(a.getX(), a.getY(), b.getX(), b.getY());
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return new String("[" + a.getX() + "," + a.getY() + "] -> [" + b.getX() + "," + b.getY()
            + "]");
    }
}
