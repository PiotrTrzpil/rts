package rts.model.timeline;

import java.util.LinkedList;
import java.util.List;
import rts.model.ingameEvents.GameEvent;

// TODO: Auto-generated Javadoc
/**
 * Sekwencja zdarzen sluzy do budowy ciagu zdarzen przed dodaniem ich do linii
 * czasu.
 */
public class EventSequence
{
    /** Lista zdarzen. */
    private final List<GameEvent> events;
    /** Obiekt czasu. */
    private final GameTime time;

    /**
     * Instantiates a new event sequence.
     */
    public EventSequence()
    {
        events = new LinkedList<GameEvent>();
        time = new GameTime(0);// System.currentTimeMillis());
    }
    /**
     * Dodaje zdarzenie do tej sekwencji
     * 
     * @param gameEvent the game event
     * 
     * @return the event sequence
     */
    public EventSequence add(final GameEvent gameEvent)
    {
        time.add(gameEvent.getTime());
        gameEvent.setTime(time.getTime());
        events.add(gameEvent);
        return this;
    }
    /**
     * Dodaje wszystkie zdarzenia z danej sekwencji do tej sekwencji,
     * odpowiednio zmieniajac ich czasy, aby wykonaly sie po zdarzeniach z tej
     * sekwencji.
     * 
     * @param sequence the sequence
     * 
     * @return the event sequence
     */
    public EventSequence add(final EventSequence sequence)
    {
        for(final GameEvent event : sequence.getList())
        {
            event.setTime(time.getTime() + event.getTime());
            // P.pr("set: " + event.getTime());
            events.add(event);
        }
        time.add(sequence.time.getTime());
        return this;
    }
    // /**
    // * Adds the.
    // *
    // * @param gameEvent the game event
    // * @return the event sequence
    // */
    // public EventSequence add(final GameEvent gameEvent)
    // {
    // time.add(gameEvent.getFromPreviousDelayTime());
    // gameEvent.setDelayTime(time.getTime() +
    // gameEvent.getFromPreviousDelayTime());
    // events.add(gameEvent);
    // return this;
    // }
    // /**
    // * Adds the.
    // *
    // * @param sequence the sequence
    // * @return the event sequence
    // */
    // public EventSequence add(final EventSequence sequence)
    // {
    // for(final GameEvent event : sequence.getList())
    // {
    // event.setDelayTime(time.getTime() + event.getFromPreviousDelayTime());
    // P.pr("Set: " + event.getDelayTime());
    // events.add(event);
    // }
    // time.add(sequence.time.getTime());
    // return this;
    // }
    /**
     * Gets the list.
     * 
     * @return the list
     */
    public List<GameEvent> getList()
    {
        // TODO Auto-generated method stub
        return events;
    }
}
