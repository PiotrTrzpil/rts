package rts.model.ingameEvents;


public class NullEvent extends GameEvent
{
    public NullEvent(final long time)
    {
        super(time);
    }
    @Override
    public void execute()
    {
    }
}
