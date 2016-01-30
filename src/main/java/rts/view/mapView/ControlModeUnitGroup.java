package rts.view.mapView;

import java.awt.Color;
import java.awt.event.KeyEvent;
import rts.controller.Command;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.Oval;
import rts.view.clientModel.UnitGroupController;
import rts.view.input.KeyInputEvent;
import rts.view.input.MouseButton;
import rts.view.input.MouseButtonEvent;
import rts.view.input.MouseMovedEvent;

/**
 * Tryb sterowania grupa jednostek
 */
public class ControlModeUnitGroup extends ControlModeNormal
{
    /** kontroler grup. */
    UnitGroupController groupController;

    /**
     * Instantiates a new control mode unit group.
     * 
     * @param unitGroupController kontroler grup.
     */
    public ControlModeUnitGroup(final UnitGroupController unitGroupController)
    {
        groupController = unitGroupController;
    }
    /*
     * (non-Javadoc)
     * 
     * @seerts.view.mapView.ControlModeNormal#handleMouseEvent(rts.view.input.
     * MouseButtonEvent)
     */
    @Override
    public void handleMouseEvent(final MouseButtonEvent event)
    {
        super.handleMouseEvent(event);
        if(event.wasPressed(MouseButton.RMB))
        {
            viewModel.getQueue().add(new Command()
            {
                @Override
                public void execute()
                {
                    final Coords pressLocation = event.getLocation();
                    groupController.moveOrAttack(viewModel.getMapView().convToMap(pressLocation));
                }
            });
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @seerts.view.mapView.ControlModeNormal#handleKeyEvent(rts.view.input.
     * KeyInputEvent)
     */
    @Override
    public void handleKeyEvent(final KeyInputEvent event)
    {
        super.handleKeyEvent(event);
        if(event.wasPressed(KeyEvent.VK_A))
        {
            viewModel.getQueue().add(new Command()
            {
                @Override
                public void execute()
                {
                    viewModel.getMapView().setControlMode(new ControlModeAttack());
                }
            });
        }
    }
    @Override
    public void deactivate()
    {
        // gameEnv.getObjectHolder().deselect();
    }

    /**
     * Dodatkowy tryb sterowania atakiem
     */
    public class ControlModeAttack extends ControlModeAbstract
    {
        private Coords location;

        @Override
        public Drawable getObjectsToDraw(final ConstRect visibleArea)
        {
            // final List<Drawable> list = new LinkedList<Drawable>();
            if(location != null)
            {
                final Oval oval = new Oval(location.moveBy(10, 18), 20, 20);// .moveBy(location);
                // constRect.setColor(Color.red);
                // constRect.setFilled(true);
                // list.add(constRect);
                return new Drawable()
                {
                    @Override
                    public void draw(final GameGraphics g)
                    {
                        g.setColor(Color.RED);
                        g.setFilled(true);
                        g.setToMapRelation(false);
                        g.drawOval(oval);
                        g.setToMapRelation(true);
                        // g.getGraphics().fillOval(constRect.getX(),
                        // constRect.getY(),
                        // constRect.getWidth(), constRect.getHeight());
                        // constRect.draw(g.getGraphics());
                    }
                };
            }
            return new DrawableNull();
            // return list;
        }
        @Override
        public void handleMouseEvent(final MouseMovedEvent event)
        {
            super.handleMouseEvent(event);
            location = event.getLocation();
        }
        /*
         * (non-Javadoc)
         * 
         * @see
         * rts.view.mapView.ControlModeAbstract#handleMouseEvent(rts.view.input
         * .MouseButtonEvent)
         */
        @Override
        public void handleMouseEvent(final MouseButtonEvent event)
        {
            super.handleMouseEvent(event);
            if(event.wasPressed(MouseButton.LMB))
            {
                viewModel.getQueue().add(new Command()
                {
                    @Override
                    public void execute()
                    {
                        final Coords clickPoint = event.getLocation();// .editable();
                        // view.convToMap(clickPoint);
                        groupController.agressiveMove(viewModel.getMapView().convToMap(clickPoint));
                        leaveControl();
                    }
                });
            }
            else if(event.wasPressed(MouseButton.RMB))
            {
                viewModel.getQueue().add(new Command()
                {
                    @Override
                    public void execute()
                    {
                        leaveControl();
                    }
                });
            }
        }
        /**
         * Leave control.
         */
        private void leaveControl()
        {
            viewModel.getMapView().setControlMode(ControlModeUnitGroup.this);
        }
    }
}