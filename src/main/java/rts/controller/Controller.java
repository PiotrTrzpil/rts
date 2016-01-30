package rts.controller;

import java.io.IOException;
import java.net.BindException;
import java.net.UnknownHostException;
import javax.swing.SwingUtilities;
import rts.controller.client.ClientController;
import rts.controller.connection.ClientSideConnectionOffline;
import rts.controller.connection.ClientSideConnectionOnline;
import rts.controller.netEvents.toServerEvents.ToServerUserJoined;
import rts.controller.server.ServerController;
import rts.model.map.MapSettings;
import rts.view.GameView;
import rts.view.screens.ScreenHost;

// TODO: Auto-generated Javadoc
/**
 * The Class Controller.
 */
public class Controller
{
    /** The game view. */
    private GameView gameView;
    /** Kolejka zdarzen klienta. */
    private final ClientQueue queue;

    /**
     * Instantiates a new controller.
     */
    public Controller()
    {
        queue = new ClientQueue();
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                gameView = new GameView(queue);
                gameView.createMainMenu(Controller.this);
                gameView.loadMainMenu();
            }
        });
    }
    /**
     * Zaklada nowa gre
     * 
     * @param mapSettings the map settings
     * @return the screen host
     * @throws BindException the bind exception
     * @throws Exception the exception
     */
    public ScreenHost hostGame(final MapSettings mapSettings) throws BindException, Exception
    {
        final ClientSideConnectionOffline connection = new ClientSideConnectionOffline();
        final ServerController serverController = new ServerController(gameView, mapSettings,
                connection);
        final ClientController clientController = new ClientController(gameView, queue, connection);
        final ScreenHost screenHost = new ScreenHost(serverController, clientController, gameView,
                mapSettings);
        clientController.initHost(screenHost, mapSettings);
        serverController.initialize();
        return screenHost;
    }
    /**
     * Dolacza do gry.
     * 
     * @param hostName ip hosta
     * @param playerName
     * @throws UnknownHostException the unknown host exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void joinGame(final String hostName, final String playerName)
        throws UnknownHostException, IOException
    {
        final ClientSideConnectionOnline connection = new ClientSideConnectionOnline();
        final ClientController clientController = new ClientController(gameView, queue, connection);
        connection.connect(hostName);
        clientController.send(new ToServerUserJoined(playerName));
    }
    /**
     * Run.
     */
    public void run()
    {
        while(true)
        {
            queue.takeEvent().execute();
        }
    }
}
