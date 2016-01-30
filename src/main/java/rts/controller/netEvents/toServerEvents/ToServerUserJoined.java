package rts.controller.netEvents.toServerEvents;

import rts.controller.connection.ServerSideConnection;
import rts.controller.server.ServerController;

// TODO: Auto-generated Javadoc
/**
 * Proba klienta dolaczenia do gry.
 */
public class ToServerUserJoined implements ToServerEvent
{
    /** The user name. */
    private final String userName;

    /**
     * Instantiates a new net login.
     * 
     * @param userName the user name
     */
    public ToServerUserJoined(final String userName)
    {
        this.userName = userName;
    }
    /**
     * Gets the user name.
     * 
     * @return the user name
     */
    public String getUserName()
    {
        return userName;
    }
    @Override
    public void execute(final ServerController controller, final ServerSideConnection source)
    {
        controller.playerJoined(userName, source);
    }
}
