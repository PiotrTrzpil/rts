package rts.model.timeline;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import rts.model.AbstractUnit;
import rts.model.ingameEvents.UnitEvent;
import rts.model.timeline.TimeLine.UnitTask;

// TODO: Auto-generated Javadoc
/**
 * Mapa uzywana przez linie czasu. Przyporzadkowuje listy zadan jednostkom.
 */
public class UnitEventMap
{
    /** The map. */
    private final Map<AbstractUnit, LinkedList<UnitTask>> map;

    /**
     * Instantiates a new event map.
     */
    public UnitEventMap()
    {
        map = new HashMap<AbstractUnit, LinkedList<UnitTask>>();
    }
    /**
     * Umieszcza zadanie mapujac je do zwiazanej z nim jednostki.
     * 
     * @param task zadanie
     */
    public void put(final UnitTask task)
    {
        final AbstractUnit unit = ((UnitEvent) task.event).getUnit();
        LinkedList<UnitTask> scheduledList = map.get(unit);
        if(scheduledList == null)
        {
            scheduledList = new LinkedList<UnitTask>();
            map.put(unit, scheduledList);
        }
        scheduledList.add(task);
    }
    /**
     * Usuwa wszystkie zadania zwiazane z jednostka
     * 
     * @param unit jednostka
     */
    public void remove(final AbstractUnit unit)
    {
        final LinkedList<UnitTask> scheduledList = map.get(unit);
        if(scheduledList != null)
        {
            for(final UnitTask eventTask : scheduledList)
            {
                eventTask.cancel();
                ((UnitEvent) eventTask.event).cleaning();
            }
            map.remove(unit);
        }
    }
    /**
     * Usuwa zadanie, usuwajac mapowanie, jesli zadanie jest ostatnie.
     * 
     * @param unitTask zadanie
     */
    public void remove(final UnitTask unitTask)
    {
        final AbstractUnit unit = ((UnitEvent) unitTask.event).getUnit();
        final LinkedList<UnitTask> scheduledList = map.get(unit);
        scheduledList.remove(unitTask);
        if(scheduledList.size() == 0)
        {
            map.remove(unit);
        }
    }
}