package rts.controller.client;

import javax.swing.SwingUtilities;
import rts.controller.UserPlayer;

// TODO: Auto-generated Javadoc
/**
 * Gracz dolaczyl lub opuscil gre
 */
public class ToClientPlayerChange implements ToClientEvent
{
    /** The player. */
    private final UserPlayer player;
    /** The state. */
    private final State state;

    /**
     * Instantiates a new to client player change.
     * 
     * @param player the player
     * @param state the state
     */
    public ToClientPlayerChange(final UserPlayer player, final State state)
    {
        this.player = player;
        this.state = state;
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * rts.controller.toClientInitial.ToClientInitialEvent#execute(rts.controller
     * .client.ClientController)
     */
    public void execute(final ClientController controller)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                if(state == State.JOINED)
                {
                    controller.getNewGameScreen().playerJoined(player);
                }
                else if(state == State.LEFT)
                {
                    controller.getNewGameScreen().playerLeft(player);
                }
                else
                {
                    controller.kickedFromGame();
                }
            }
        });
    }

    /**
     * The Enum State.
     */
    public static enum State
    {
        /** The LEFT. */
        LEFT,
        /** The JOINED. */
        JOINED,
        YOU_WERE_KICKED;
    }
}
