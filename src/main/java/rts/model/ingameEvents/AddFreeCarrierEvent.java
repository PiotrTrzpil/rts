/**
 *
 */
package rts.model.ingameEvents;

import rts.model.AbstractUnit;
import rts.model.serverUnits.Carrier;

public class AddFreeCarrierEvent extends UnitEvent
{
    public AddFreeCarrierEvent(final AbstractUnit unit)
    {
        super(unit, 0);
    }

    @Override
    public void execute()
    {
        final Carrier carrier = (Carrier) unit;
        carrier.getOwner().getWorkerOvermind().addFreeCarrier(carrier);
    }
}