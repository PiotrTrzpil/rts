package rts;

import rts.controller.Controller;

// TODO: Auto-generated Javadoc
/**
 * Punkt poczatkowy programu. Tworzony jest kontroler po czym uruchamiana jest
 * petla kolejki.
 */
public class RtsGame
{
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(final String[] args)
    {
        final Controller controller = new Controller();
        controller.run();
    }
}
