package rts.view.mapView;

import java.awt.Color;
import java.awt.event.KeyEvent;
import rts.controller.Command;
import rts.misc.ConstRect;
import rts.view.input.KeyInputEvent;
import rts.view.input.MouseButton;
import rts.view.input.MouseButtonEvent;
import rts.view.input.MouseMovedEvent;
import rts.view.panels.LeftPanel;
import rts.view.panels.Tab;

/**
 * Normalny tryb kontroli, dodatkowo pozwala zaznaczac obiekty
 */
public class ControlModeNormal extends ControlModeAbstract
{
    /** Ramka zaznaczajaca obiekty. */
    private final SelectionFrame selectionFrame;

    /**
     * Instantiates a new control mode normal.
     */
    public ControlModeNormal()
    {
        selectionFrame = new SelectionFrame();
    }
    /*
     * (non-Javadoc)
     * 
     * @see rts.view.mapView.ControlModeAbstract#getObjectsToDraw(rts.misc.Rect)
     */
    @Override
    public Drawable getObjectsToDraw(final ConstRect visibleArea)
    {
        // final List<Drawable> list = new ArrayList<Drawable>();
        if(selectionFrame.isActive())
        {
            final ConstRect frame = viewModel.getMapView()
                    .convToMap(selectionFrame.getRectToDraw());
            return new Drawable()
            {
                @Override
                public void draw(final GameGraphics g)
                {
                    g.setColor(Color.BLUE);
                    g.drawRect(frame);
                }
            };
            // list.add(new ColorShape(Color.BLUE, frame));
        }
        return new DrawableNull();
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * rts.view.mapView.ControlModeAbstract#handleMouseEvent(rts.view.input.
     * MouseMovedEvent)
     */
    @Override
    public void handleMouseEvent(final MouseMovedEvent event)
    {
        super.handleMouseEvent(event);
        selectionFrame.move(event.getLocation());
        viewModel.getQueue().add(new Command()
        {
            public void execute()
            {
                viewModel.getObjectHolder().setPointerPosition(viewModel.getMapView().convToMap(
                    event.getLocation()));
            }
        });
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * rts.view.mapView.ControlModeAbstract#handleMouseEvent(rts.view.input.
     * MouseButtonEvent)
     */
    @Override
    public void handleMouseEvent(final MouseButtonEvent event)
    {
        super.handleMouseEvent(event);
        if(event.wasPressed(MouseButton.RMB) && !(this instanceof ControlModeUnitGroup))
        {
            final LeftPanel leftPanel = viewModel.getLeftPanel();
            leftPanel.loadNewInfoTab(leftPanel.getInfoTab());
            leftPanel.changeTabTo(Tab.INFO);
        }
        else if(event.wasPressed(MouseButton.LMB))
        {
            selectionFrame.activate(event.getLocation());
        }
        else if(event.wasReleased(MouseButton.LMB) && selectionFrame.isActive())
        {
            final ConstRect r = selectionFrame.getOnScreenSelectedArea();
            if(event.isShiftPressed())
            {
                viewModel.getQueue().add(new Command()
                {
                    public void execute()
                    {
                        viewModel.getObjectHolder()
                                .addOrRemove(viewModel.getMapView().convToMap(r));
                    }
                });
            }
            else
            {
                viewModel.getQueue().add(new Command()
                {
                    public void execute()
                    {
                        viewModel.getMapView().setControlMode(new ControlModeNormal());
                        viewModel.getObjectHolder().select(viewModel.getMapView().convToMap(r));
                    }
                });
            }
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @seerts.view.mapView.ControlModeAbstract#handleKeyEvent(rts.view.input.
     * KeyInputEvent)
     */
    @Override
    public void handleKeyEvent(final KeyInputEvent event)
    {
        super.handleKeyEvent(event);
        viewModel.getQueue().add(new Command()
        {
            public void execute()
            {
                if(event.wasPressed(KeyEvent.VK_ALT))
                {
                    viewModel.getMapView().setHpVisible(true);
                }
                else if(event.wasReleased(KeyEvent.VK_ALT))
                {
                    viewModel.getMapView().setHpVisible(false);
                }
            }
        });
    }
}