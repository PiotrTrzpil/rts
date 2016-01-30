package rts.model.ingameEvents;

import rts.model.AbstractUnit;
import rts.model.serverUnits.Unit;

public class ActivateUnitEvent extends UnitEvent
{
    public ActivateUnitEvent(final AbstractUnit unit)
    {
        super(unit, 0);
    }

    @Override
    public void execute()
    {
        ((Unit) unit).activate();
    }
}
