package rts.model.ingameEvents;

import rts.model.AbstractUnit;

public abstract class UnitEvent extends GameEvent
{
    protected final AbstractUnit unit;

    public UnitEvent(final AbstractUnit unit, final long time)
    {
        super(time);
        this.unit = unit;
    }
    public AbstractUnit getUnit()
    {
        return unit;
    }
    public void cleaning()
    {
    }
}
