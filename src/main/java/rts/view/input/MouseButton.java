/*
 *
 */
package rts.view.input;

import java.awt.event.MouseEvent;

// TODO: Auto-generated Javadoc
/**
 * The Enum MouseButton.
 */
public enum MouseButton
{

    /** The NOBUTTON. */
    NOBUTTON,

    /** The LMB. */
    LMB,

    /** The CMB. */
    CMB,

    /** The RMB. */
    RMB;

    /**
     * From.
     * 
     * @param code the code
     * 
     * @return the mouse button
     */
    public static MouseButton from(final int code)
    {
        if(code == MouseEvent.NOBUTTON)
        {
            return NOBUTTON;
        }
        if(code == MouseEvent.BUTTON1)
        {
            return LMB;
        }
        if(code == MouseEvent.BUTTON2)
        {
            return CMB;
        }
        if(code == MouseEvent.BUTTON3)
        {
            return RMB;
        }
        throw new RuntimeException("Bad code");
    }

}