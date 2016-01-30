package rts.model.ingameEvents;

import rts.misc.Coords;
import rts.misc.CoordsDouble;
import rts.misc.Velocity;
import rts.model.AbstractUnit;

// TODO: Auto-generated Javadoc
/**
 * Zdarzenie zmiany wektora ruchu jednostki
 */
public class ChangeMoveEvent extends UnitEvent
{
    /** Polozenie w ktorym nastepuje zmiana. */
    private final CoordsDouble position;
    /** Nowy wektor ruchu. */
    private final Velocity vector;
    /** Chwila wykonania. */
    private long executionTime;

    /**
     * Instantiates a new change move event.
     * 
     * @param unit the unit
     * @param a the a
     * @param time the time
     * @param velocity the velocity
     */
    public ChangeMoveEvent(final AbstractUnit unit, final Coords a, final long time,
        final Velocity velocity)
    {
        super(unit, time);
        position = new CoordsDouble(a);
        vector = new Velocity(velocity);
    }
    /*
     * (non-Javadoc)
     * 
     * @see rts.controller.ingameEvents.GameEvent#execute()
     */
    @Override
    public void execute()
    {
        unit.setPosition(position);
        unit.setCurrentMove(this);
        executionTime = System.currentTimeMillis();
    }
    /**
     * Gets the vector.
     * 
     * @return the vector
     */
    public Velocity getVector()
    {
        return vector;
    }
    /**
     * Gets the position.
     * 
     * @return the position
     */
    public CoordsDouble getPosition()
    {
        return position;
    }
    /**
     * Gets the execution time.
     * 
     * @return the execution time
     */
    public long getExecutionTime()
    {
        return executionTime;
    }
}
