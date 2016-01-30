package rts.view.input;

import java.awt.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MouseInputEvent.
 */
public abstract class MouseInputEvent extends GameInputEvent
{

    /** The source. */
    private Component source;

    /**
     * Gets the source.
     * 
     * @return the source
     */
    public Component getSource()
    {
        return source;
    }

    /**
     * Sets the source.
     * 
     * @param source the new source
     */
    public void setSource(final Component source)
    {
        this.source = source;
    }

}
