package rts.controller.client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import rts.controller.ClientQueue;
import rts.controller.Command;
import rts.controller.GameThread;
import rts.controller.connection.ClientSideConnection;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.misc.P;
import rts.model.map.MapSettings;
import rts.view.GameView;
import rts.view.ViewModel;
import rts.view.screens.ScreenNewGame;

// TODO: Auto-generated Javadoc
/**
 * Kontroler klienta.
 */
public class ClientController extends GameThread// implements NetEventHandler
{
    /** The queue. */
    private final ClientQueue queue;
    // private final String hostName;
    /** The view. */
    private final GameView view;
    /** Polaczenie z serwerem. */
    private final ClientSideConnection connection;
    /** The map settings. */
    private MapSettings mapSettings;
    /** The view model. */
    private ViewModel viewModel;
    /** The screen new game. */
    private ScreenNewGame screenNewGame;

    /**
     * Instantiates a new client controller.
     * 
     * @param view the view
     * @param queue the queue
     * @param connection the connection
     */
    public ClientController(final GameView view, final ClientQueue queue,
        final ClientSideConnection connection)
    {
        this.view = view;
        this.queue = queue;
        this.connection = connection;
        //        connection.setHandler(this);
        start();
    }
    /**
     * Wywolywana, jesli ten kontroler zostal uruchomiony dla lokalnego hosta.
     * Tworzy model.
     * 
     * @param screen the screen
     * @param map the map
     */
    public void initHost(final ScreenNewGame screen, final MapSettings map)
    {
        mapSettings = map;
        screenNewGame = screen;
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                viewModel = new ViewModel(ClientController.this, map);
            }
        });
    }
    public void deactivate()
    {
        stop();
        connection.close();
        view.loadMainMenu();
    }
    /**
     * Reakcja na przyjscie odpowiedzi od serwera w sprawie dolaczenia do gry.
     * Jesli jest zgoda serwera, tworzony jest "model klienta" oraz ekran
     * oczekiwania na rozpoczecie gry.
     * 
     * @param gameSettings the game settings
     */
    protected void joinGameResponse(final ToClientGameSettings gameSettings)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                if(gameSettings.isOk())
                {
                    mapSettings = gameSettings.getMapSettings();
                    screenNewGame = new ScreenNewGame(ClientController.this, view, mapSettings);
                    viewModel = new ViewModel(ClientController.this, mapSettings);
                    view.load(screenNewGame);
                }
                else
                {
                    JOptionPane.showMessageDialog((JFrame) view.getMainGamePanel()
                            .getTopLevelAncestor(), "Cannot join game.");
                }
            }
        });
    }
    /**
     * Wywolywana gdy serwer wyslal zdarzenie rozpoczecia gry. Inicjalizuje
     * model, po czym wysyla informacje o gotowosci.
     * 
     * @param netGameStart the net game start
     */
    protected void startGame(final ToClientGameStart netGameStart)
    {
        viewModel.initialize(netGameStart.getPlayers(), netGameStart.getThisPlayer());
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                view.load(viewModel);
            }
        });
    }
    protected void kickedFromGame()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                view.loadMainMenu();
                view.showDialog("You were kicked from the game.");
            }
        });
    }
    @Override
    protected void job()
    {
        try
        {
            final NetEvent event = connection.take();
            //            if(event instanceof ToClientInitialEvent)
            //            {
            final ToClientEvent toClientInitialEvent = (ToClientEvent) event;
            queue.add(new Command()
            {
                @Override
                public void execute()
                {
                    toClientInitialEvent.execute(ClientController.this);
                }
            });
            //            }
            //            else
            //            {
            //                final ToClientIngameEvent toClientNetEvent = (ToClientIngameEvent) event;
            //                queue.add(new Command()
            //                {
            //                    @Override
            //                    public void execute()
            //                    {
            //                        toClientNetEvent.execute(viewModel);
            //                    }
            //                });
            //            }
        }
        catch(final NullPointerException e)
        {
            P.er("took null");
        }
        catch(final InterruptedException e)
        {
            P.er(this + " interrupted");
        }
    }
    /**
     * Send.
     * 
     * @param netEvent the net event
     */
    public void send(final ToServerEvent netEvent)
    {
        connection.send(netEvent);
    }
    //    /**
    //     * Gets the user name.
    //     *
    //     * @return the user name
    //     */
    //    public String getUserName()
    //    {
    //        return "KtosInny";
    //    }
    /**
     * Gets the view.
     * 
     * @return the view
     */
    public GameView getView()
    {
        return view;
    }
    /**
     * Gets the queue.
     * 
     * @return the queue
     */
    public ClientQueue getQueue()
    {
        return queue;
    }
    /**
     * Gets the new game screen.
     * 
     * @return the new game screen
     */
    protected ScreenNewGame getNewGameScreen()
    {
        return screenNewGame;
    }
    public ViewModel getViewModel()
    {
        return viewModel;
    }
}
