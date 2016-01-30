package rts.view.input;

// TODO: Auto-generated Javadoc
/**
 * Klasa reprezentujaca zdarzenia przyciskow klawiatury
 */
public class KeyInputEvent extends GameInputEvent
{
    /**
     * Stan przycisku myszy
     */
    public enum State
    {
        /** The PRESSED. */
        PRESSED,
        /** The RELEASED. */
        RELEASED;
    }

    /**
     * Instantiates a new key input event.
     * 
     * @param state the state
     */
    public KeyInputEvent(final State state)
    {
        this.state = state;
    }

    /** The pressed button. */
    private int keyCode;
    /** The shift pressed. */
    private boolean shiftPressed;
    /** The ctrl pressed. */
    private boolean ctrlPressed;
    /** The alt pressed. */
    private boolean altPressed;
    /** The state. */
    private final State state;

    /**
     * Was pressed.
     * 
     * @param button the button
     * @return true, if successful
     */
    public boolean wasPressed(final int button)
    {
        return keyCode == button && state == State.PRESSED;
    }
    /**
     * Was released.
     * 
     * @param button the button
     * @return true, if successful
     */
    public boolean wasReleased(final int button)
    {
        return keyCode == button && state == State.RELEASED;
    }
    /**
     * Sets the button.
     * 
     * @param button the new button
     */
    protected void setButton(final int button)
    {
        keyCode = button;
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
