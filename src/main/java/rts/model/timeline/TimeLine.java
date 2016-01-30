package rts.model.timeline;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import rts.controller.Command;
import rts.controller.GameEventQueue;
import rts.model.AbstractUnit;
import rts.model.ingameEvents.GameEvent;
import rts.model.ingameEvents.UnitEvent;

// TODO: Auto-generated Javadoc
/**
 * Klasa zarzadzajaca zdarzeniami.
 */
public class TimeLine
{
    // private final LinkedList<EventTask> timeline;
    /** Timer uzywany do ustawiania wykonania zdarzen po okreslonym czasie. */
    private final Timer timer;
    /** Kolejka watku kontrolera. */
    private final GameEventQueue queue;
    /** Mapa zdarzen jednostek. */
    private final UnitEventMap eventMap;

    /**
     * Instantiates a new time line.
     * 
     * @param queue kolejka
     */
    public TimeLine(final GameEventQueue queue)
    {
        this.queue = queue;
        timer = new Timer();
        // timeline = new LinkedList<EventTask>();
        eventMap = new UnitEventMap();
    }
    /**
     * Wstawia sekwencje zdarzen do wykonania
     * 
     * @param sequence sekwencja zdarzen
     */
    public void put(final EventSequence sequence)
    {
        final List<GameEvent> list = sequence.getList();
        for(final GameEvent event : list)
        {
            EventTask task = null;
            if(event instanceof UnitEvent)
            {
                final UnitEvent unitEvent = (UnitEvent) event;
                task = new UnitTask(unitEvent);
                eventMap.put((UnitTask) task);
            }
            else
            {
                task = new EventTask(event);
            }
            try
            {
                long delay = event.getTime();
                delay = delay < 0 ? 0 : delay;
                timer.schedule(task, delay);
            }
            catch(final Exception ee)
            {
                ee.printStackTrace();
                //P.pr(event.getFromPreviousDelayTime());
            }
        }
    }
    /**
     * Usuwa wszystkie zaplanowane zdarzenia zwiazane z dana jednostka.
     * 
     * @param unit zadana jednostka
     */
    public void clearEvents(final AbstractUnit unit)
    {
        eventMap.remove(unit);
    }

    /**
     * Zaplanowane zadanie wykonania zdarzenia.
     */
    protected class EventTask extends TimerTask
    {
        /** The event. */
        protected GameEvent event;

        /**
         * Instantiates a new event task.
         * 
         * @param event the event
         */
        public EventTask(final GameEvent event)
        {
            super();
            this.event = event;
        }
        /*
         * (non-Javadoc)
         * 
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run()
        {
            queue.add(new Command()
            {
                @Override
                public void execute()
                {
                    event.execute();
                }
            });
        }
    }

    /**
     * Zadanie dotyczace jednostki
     */
    protected class UnitTask extends EventTask
    {
        /**
         * Instantiates a new unit task.
         * 
         * @param event the event
         */
        public UnitTask(final UnitEvent event)
        {
            super(event);
        }
        /*
         * (non-Javadoc)
         * 
         * @see rts.controller.timeline.TimeLine.EventTask#run()
         */
        @Override
        public void run()
        {
            queue.add(new Command()
            {
                @Override
                public void execute()
                {
                    eventMap.remove(UnitTask.this);
                    event.execute();
                }
            });
        }
    }
}
