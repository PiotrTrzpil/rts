package rts.model;

import java.util.LinkedList;
import java.util.List;
import rts.misc.Coords;
import rts.model.serverBuildings.BuildingControl;
import rts.model.serverUnits.Carrier;
import rts.model.serverUnits.Unit;

// TODO: Auto-generated Javadoc
/**
 * Klasa zarzadzajaca tragarzami gracza.
 */
public class WorkerOvermind// extends TimerTask
{
    /** Lista wolnych tragarzy. */
    private final List<Carrier> freeCarriers;
    /** System transportu. */
    private final TransportSystem transportSystem;

    /**
     * Instantiates a new worker overmind.
     */
    public WorkerOvermind()
    {
        freeCarriers = new LinkedList<Carrier>();
        transportSystem = new TransportSystem();
    }
    public TransportTask carrierWork()
    {
        if(!freeCarriers.isEmpty())
        {
            final TransportTask task = transportSystem.findTransportTask();
            if(task != null)
            {
                final Carrier carr;
                carr = (Carrier) getClosestWorker(task.getSource(), freeCarriers);
                if(carr != null)
                {
                    task.setCarrier(carr);
                    return task;
                }
            }
        }
        return null;
    }
    /**
     * Gets the closest worker.
     * 
     * @param source the source
     * @param list the list
     * @return the closest worker
     */
    private Unit getClosestWorker(final BuildingControl source,
        final List<? extends Unit> list)
    {
        if(source == null)
        {
            return null;
        }
        Unit closest = null;
        double dist, shortest = Double.MAX_VALUE;
        final Coords exitPosition = source.getExitPosition();
        for(final Unit unit : list)
        {
            final int x = Math.abs(exitPosition.getX() - unit.getPosition().getX());
            final int y = Math.abs(exitPosition.getY() - unit.getPosition().getY());
            dist = Math.sqrt(x * x + y * y);
            if(dist < shortest)
            {
                shortest = dist;
                closest = unit;
            }
        }
        return closest;
    }
    /**
     * Removes the free carrier.
     * 
     * @param c the c
     */
    public void removeFreeCarrier(final Carrier c)
    {
        freeCarriers.remove(c);
    }
    /**
     * Adds the free carrier.
     * 
     * @param c the c
     */
    public void addFreeCarrier(final Carrier c)
    {
        freeCarriers.add(c);
    }
    /**
     * Gets the transport queue.
     * 
     * @return the transport queue
     */
    public TransportSystem getTransportSystem()
    {
        return transportSystem;
    }
}
