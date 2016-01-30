package rts.view.input;

import rts.misc.Coords;

// TODO: Auto-generated Javadoc
/**
 * The Class MouseMovedEvent.
 */
public class MouseMovedEvent extends MouseInputEvent
{

    /** The location. */
    private Coords location;

    /** The move distance x. */
    private int moveDistanceX;

    /** The move distance y. */
    private int moveDistanceY;

    private final State state;

    public enum State
    {
        MOVED,
        DRAGGED;
    }

    public MouseMovedEvent(final State state)
    {
        this.state = state;

    }

    /**
     * Gets the location.
     * 
     * @return the location
     */
    public Coords getLocation()
    {
        return location;
    }

    /**
     * Sets the location.
     * 
     * @param x the x
     * @param y the y
     */
    protected void setLocation(final int x, final int y)
    {
        location = new Coords(x, y);
    }

    /**
     * Gets the move distance x.
     * 
     * @return the move distance x
     */
    public int getMoveDistanceX()
    {
        return moveDistanceX;
    }

    /**
     * Sets the move distance x.
     * 
     * @param moveDistanceX the new move distance x
     */
    protected void setMoveDistance(final int moveDistanceX, final int moveDistanceY)
    {
        this.moveDistanceX = moveDistanceX;
        this.moveDistanceY = moveDistanceY;
    }

    /**
     * Gets the move distance y.
     * 
     * @return the move distance y
     */
    public int getMoveDistanceY()
    {
        return moveDistanceY;
    }

    public boolean isMouseMoved()
    {
        return state == State.MOVED;
    }

    public boolean isMouseDragged()
    {
        return state == State.DRAGGED;
    }
}
