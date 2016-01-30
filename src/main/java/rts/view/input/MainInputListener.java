package rts.view.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import rts.misc.Coords;
import rts.view.GameView.MainGamePanel;

// TODO: Auto-generated Javadoc
/**
 * Glowna klasa odbierajaca zdarzenia klawiatury i myszy.
 */
public class MainInputListener implements KeyListener, MouseMotionListener, MouseListener
{
    /** The sender. */
    private final MainGamePanel sender;
    // /** The robot. */
    // private Robot robot;
    // /** The reposition point. */
    // private Coords repositionPoint;
    /** The mouse location. */
    private Coords mouseLocation;

    /**
     * Instantiates a new main input listener.
     * 
     * @param sender the sender
     */
    public MainInputListener(final MainGamePanel sender)
    {
        this.sender = sender;
        mouseLocation = new Coords();
        sender.addKeyListener(this);
        sender.setFocusTraversalKeysEnabled(false);
        // try
        // {
        // robot = new Robot();
        // }
        // catch(final AWTException ex)
        // {
        // robot = null;
        // }
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(final KeyEvent e)
    {
        final KeyInputEvent event = new KeyInputEvent(KeyInputEvent.State.PRESSED);
        event.setButton(e.getKeyCode());
        event.setModifierKeys(e.isShiftDown(), e.isControlDown(), e.isAltDown());
        sender.handleKeyEvent(event);
        e.consume();
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(final KeyEvent e)
    {
        final KeyInputEvent event = new KeyInputEvent(KeyInputEvent.State.RELEASED);
        event.setButton(e.getKeyCode());
        event.setModifierKeys(e.isShiftDown(), e.isControlDown(), e.isAltDown());
        sender.handleKeyEvent(event);
        e.consume();
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(final KeyEvent e)
    {
        e.consume();
    }
    // /**
    // * Reposition to.
    // *
    // * @param x the x
    // * @param y the y
    // * @param requester the requester
    // */
    // public void repositionTo(final int x, final int y, final Component
    // requester)
    // {
    // if(robot != null)
    // {
    // final Point dest = new Point(x, y);
    // SwingUtilities.convertPointToScreen(dest, requester);
    // repositionPoint = new Coords(x, y);
    // robot.mouseMove(dest.x, dest.y);
    // }
    // }
    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
     * )
     */
    public void mouseDragged(final MouseEvent e)
    {
        // mouseMoved(e);
        mouseMovedOrDragged(e, new MouseMovedEvent(MouseMovedEvent.State.DRAGGED));
        // e.consume();
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(final MouseEvent e)
    {
        mouseMovedOrDragged(e, new MouseMovedEvent(MouseMovedEvent.State.MOVED));
        e.consume();
    }
    /**
     * Mouse moved or dragged.
     * 
     * @param e the e
     * @param event the event
     */
    private void mouseMovedOrDragged(final MouseEvent e, final MouseMovedEvent event)
    {
        final int dx = e.getX() - mouseLocation.getX();
        final int dy = e.getY() - mouseLocation.getY();
        final Component source = (Component) e.getSource();
        event.setSource(source);
        event.setLocation(e.getX(), e.getY());
        event.setMoveDistance(dx, dy);
        mouseLocation = new Coords(e.getX(), e.getY());
        if(source instanceof MouseEventHandler)
        {
            final MouseEventHandler handler = (MouseEventHandler) source;
            handler.handleMouseEvent((MouseInputEvent) event);
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(final MouseEvent e)
    {
        final MouseButtonEvent event = new MouseButtonEvent(MouseButtonEvent.State.PRESSED);
        final Component source = (Component) e.getSource();
        event.setSource(source);
        event.setButton(MouseButton.from(e.getButton()));
        event.setLocation(e.getX(), e.getY());
        event.setModifierKeys(e.isShiftDown(), e.isControlDown(), e.isAltDown());
        if(source instanceof MouseEventHandler)
        {
            final MouseEventHandler handler = (MouseEventHandler) source;
            handler.handleMouseEvent((MouseInputEvent) event);
        }
        e.consume();
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(final MouseEvent e)
    {
        final MouseButtonEvent event = new MouseButtonEvent(MouseButtonEvent.State.RELEASED);
        final Component source = (Component) e.getSource();
        event.setSource(source);
        event.setButton(MouseButton.from(e.getButton()));
        event.setLocation(e.getX(), e.getY());
        event.setModifierKeys(e.isShiftDown(), e.isControlDown(), e.isAltDown());
        if(source instanceof MouseEventHandler)
        {
            final MouseEventHandler handler = (MouseEventHandler) source;
            handler.handleMouseEvent((MouseInputEvent) event);
        }
        e.consume();
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(final MouseEvent e)
    {
        e.consume();
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(final MouseEvent e)
    {
        e.consume();
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(final MouseEvent e)
    {
        e.consume();
    }
}
