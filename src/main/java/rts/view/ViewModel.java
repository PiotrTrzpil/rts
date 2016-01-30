package rts.view;

import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import rts.controller.ClientQueue;
import rts.controller.PlayerID;
import rts.controller.UserPlayer;
import rts.controller.client.ClientController;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.misc.GameMidiPlayer;
import rts.model.map.MapSettings;
import rts.view.clientModel.ClientMap;
import rts.view.clientModel.ObjectHolder;
import rts.view.input.KeyInputEvent;
import rts.view.input.MainInputListener;
import rts.view.mapView.MapView;
import rts.view.panels.LeftPanel;
import rts.view.screens.Console;
import rts.view.screens.Screen;

/**
 * To jest kontekst swiata gry od strony klienta. Przechowuje wszystkie glowne
 * moduly zarzadzajace reprezentacja gry uzytkownikowi.
 */
public class ViewModel extends Screen
{
    /** The map view. */
    private final MapView mapView;
    /** The map. */
    private final ClientMap map;
    /** The object holder. */
    private ObjectHolder objectHolder;
    /** The left panel. */
    private final LeftPanel leftPanel;
    /** The input listener. */
    private final MainInputListener inputListener;
    /** The image loader. */
    private final ImagesAndPatterns imageLoader;
    /** The queue. */
    private final ClientQueue queue;
    private final GameChat gameChat;
    private final GameView gameView;
    public static Console console;
    private final ClientController clientController;

    public ViewModel(final ClientController clientController, final MapSettings mapSettings)
    {
        this.clientController = clientController;
        queue = clientController.getQueue();
        gameView = clientController.getView();
        inputListener = gameView.getInputListener();
        imageLoader = gameView.getImagesAndPatterns();
        console = new Console(this);
        map = new ClientMap(mapSettings, imageLoader);
        leftPanel = new LeftPanel(this);
        mapView = new MapView(this);
        gameChat = new GameChat(this);
        getPanel().add(leftPanel.getPanel());
        getPanel().add(mapView.getViewPanel());
        // playMusic();
    }
    public void initialize(final java.util.Map<PlayerID, UserPlayer> playerMap,
        final UserPlayer thisPlayer)
    {
        mapView.centerOn(thisPlayer.getStartPoint());
        objectHolder = new ObjectHolder(this, thisPlayer, playerMap);
        objectHolder.activate();
    }
    public void deactivateAll()
    {
        if(getObjectHolder() != null)
        {
            getObjectHolder().deactivate();
        }
    }
    private void playMusic()
    {
        final GameMidiPlayer player = new GameMidiPlayer();
    }
    /**
     * Gets the object holder.
     * 
     * @return the object holder
     */
    public ObjectHolder getObjectHolder()
    {
        return objectHolder;
    }
    /**
     * Gets the view.
     * 
     * @return the view
     */
    public MapView getMapView()
    {
        return mapView;
    }
    /**
     * Gets the input listener.
     * 
     * @return the input listener
     */
    public MainInputListener getInputListener()
    {
        return inputListener;
    }
    /**
     * Gets the map.
     * 
     * @return the map
     */
    public ClientMap getMap()
    {
        return map;
    }
    /**
     * Gets the image loader.
     * 
     * @return the image loader
     */
    public ImagesAndPatterns getImageLoader()
    {
        return imageLoader;
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
     * Gets the game pane.
     * 
     * @return the game pane
     */
    public JPanel getGamePane()
    {
        return getPanel();
    }
    /**
     * Gets the left panel.
     * 
     * @return the left panel
     */
    public LeftPanel getLeftPanel()
    {
        return leftPanel;
    }
    @Override
    public void handleKeyEvent(final KeyInputEvent event)
    {
        if(event.wasPressed(KeyEvent.VK_C))
        {
            gameView.load(console);
        }
        mapView.getControl().handleKeyEvent(event);
    }
    public void send(final ToServerEvent netEvent)
    {
        clientController.send(netEvent);
    }
    public GameChat getChat()
    {
        return gameChat;
    }
    public GameView getGameView()
    {
        return gameView;
    }
}
