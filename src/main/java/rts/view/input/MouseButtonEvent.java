package rts.view.input;

import rts.misc.Coords;

// TODO: Auto-generated Javadoc
/**
 * Klasa reprezentujaca zdarzenia przyciskow myszy
 */
public class MouseButtonEvent extends MouseInputEvent
{
    /**
     * The Enum State.
     */
    public enum State
    {
        /** The PRESSED. */
        PRESSED,
        /** The RELEASED. */
        RELEASED;
    }

    /** The pressed button. */
    private MouseButton mouseButton;
    /** The location. */
    private Coords location;
    /** The shift pressed. */
    private boolean shiftPressed;
    /** The ctrl pressed. */
    private boolean ctrlPressed;
    /** The alt pressed. */
    private boolean altPressed;
    /** The state. */
    private final State state;

    /**
     * Instantiates a new mouse button event.
     * 
     * @param state the state
     */
    public MouseButtonEvent(final State state)
    {
        this.state = state;
    }
    /**
     * Gets the state.
     * 
     * @return the state
     */
    public State getState()
    {
        return state;
    }
    /**
     * Gets the pressed button.
     * 
     * @return the pressed button
     */
    public MouseButton getPressedButton()
    {
        return mouseButton;
    }
    /**
     * Was pressed.
     * 
     * @param button the button
     * @return true, if successful
     */
    public boolean wasPressed(final MouseButton button)
    {
        return mouseButton == button && state == State.PRESSED;
    }
    /**
     * Was released.
     * 
     * @param button the button
     * @return true, if successful
     */
    public boolean wasReleased(final MouseButton button)
    {
        return mouseButton == button && state == State.RELEASED;
    }
    /**
     * Sets the button.
     * 
     * @param button the new button
     */
    protected void setButton(final MouseButton button)
    {
        mouseButton = button;
    }
    /**
     * Gets the location.
     * 
     * @return the location
     */
    public Coords getLocation()
    {
        return location;
    }
    /**
     * Sets the location.
     * 
     * @param x the x
     * @param y the y
     */
    protected void setLocation(final int x, final int y)
    {
        location = new Coords(x, y);
    }
    /**
     * Sets the modifier keys.
     * 
     * @param shiftPressed the shift pressed
     * @param ctrlPressed the ctrl pressed
     * @param altPressed the alt pressed
     */
    protected void setModifierKeys(final boolean shiftPressed, final boolean ctrlPressed,
        final boolean altPressed)
    {
        this.shiftPressed = shiftPressed;
        this.ctrlPressed = ctrlPressed;
        this.altPressed = altPressed;
    }
    /**
     * Checks if is shift pressed.
     * 
     * @return true, if is shift pressed
     */
    public boolean isShiftPressed()
    {
        return shiftPressed;
    }
    /**
     * Checks if is ctrl pressed.
     * 
     * @return true, if is ctrl pressed
     */
    public boolean isCtrlPressed()
    {
        return ctrlPressed;
    }
    /**
     * Checks if is alt pressed.
     * 
     * @return true, if is alt pressed
     */
    public boolean isAltPressed()
    {
        return altPressed;
    }
}
