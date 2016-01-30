package rts.view.input;

import java.awt.LayoutManager;
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class MouseEventHandler.
 */
public abstract class MouseEventHandler extends JPanel
{

    /**
     * Instantiates a new mouse event handler.
     */
    public MouseEventHandler()
    {
        super();
    }

    /**
     * Instantiates a new mouse event handler.
     * 
     * @param manager the manager
     */
    public MouseEventHandler(final LayoutManager manager)
    {
        super(manager);
    }

    public void setListener(final MainInputListener inputListener)
    {
        addMouseListener(inputListener);
        addMouseMotionListener(inputListener);
    }

    /**
     * Handle mouse event.
     * 
     * @param event the event
     */
    protected void handleMouseEvent(final MouseInputEvent event)
    {
        try
        {
            MouseEventHandler.class.getDeclaredMethod("handleMouseEvent", event.getClass()).invoke(
                this, event);
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }
    };

    /**
     * Handle mouse event.
     * 
     * @param event the event
     */
    protected void handleMouseEvent(final MouseMovedEvent event)
    {
    };

    /**
     * Handle mouse event.
     * 
     * @param event the event
     */
    protected void handleMouseEvent(final MouseButtonEvent event)
    {
    };
    //
    //    /**
    //     * Handle mouse event.
    //     *
    //     * @param event the event
    //     */
    //    protected void handleMouseEvent(final MouseDraggedEvent event)
    //    {
    //    };

}
