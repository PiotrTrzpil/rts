package rts.view.mapView;

import rts.misc.ConstRect;
import rts.misc.Coords;

// TODO: Auto-generated Javadoc
/**
 * Ramka do zaznaczania obiektow
 */
public class SelectionFrame
{
    /** The frame. */
    private ConstRect frame;
    /** punkt poczatkowy. */
    private Coords begin;
    /** The active. */
    private boolean active = false;
    private static final ConstRect viewBounds = new ConstRect(0, 0, MapView.dimX, MapView.dimY);

    /**
     * Instantiates a new selection frame.
     */
    public SelectionFrame()
    {
        // this.mapView = mapView;
        // frame = new Rect();
        // / begin = new EditableCoords();
    }

    /**
     * Activate.
     * 
     * @param start the start
     */
    public void activate(final Coords start)
    {
        frame = new ConstRect(start);
        // frame.setX(start.getX());
        // frame.setY(start.getY());
        begin = start;
        // begin.setX(start.getX());
        // begin.setY(start.getY());
        // frame.setWidth(0);
        // frame.setHeight(0);
        active = true;
    }

    /**
     * Move.
     * 
     * @param pos the pos
     */
    public void move(final Coords pos)
    {
        if(!active)
        {
            return;
        }
        frame = new ConstRect(begin, pos).cropBy(viewBounds);
        // frame.setBetween(begin, pos);
        // Rect.cropByO(frame, new ConstRect(new Coords(), MapView.dimX,
        // MapView.dimY));
        // frame.cropBy(frame, frame)
    }

    /**
     * Gets the rect to draw.
     * 
     * @return the rect to draw
     */
    public ConstRect getRectToDraw()
    {
        // if(!active)
        // {
        // return null;
        // }
        return frame;// new ColorShape(Color.blue, frame);
        // return new Drawable()
        // {
        // @Override
        // public void draw(final GameGraphics g)
        // {
        // g.setColor(Color.blue);
        // g.getGraphics().drawRect(frame.getX(), frame.getY(),
        // frame.getWidth(),
        // frame.getHeight());
        // }
        // };
    }

    /**
     * Gets the on screen selected area.
     * 
     * @return the on screen selected area
     */
    public ConstRect getOnScreenSelectedArea()
    {
        if(!active)
        {
            return null;
        }
        active = false;
        return frame;
    }

    /**
     * Checks if is active.
     * 
     * @return true, if is active
     */
    public boolean isActive()
    {
        return active;
    }
}