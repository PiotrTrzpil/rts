package rts.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rts.controller.Controller;
import rts.controller.GameEventQueue;
import rts.controller.Timers;
import rts.view.input.KeyInputEvent;
import rts.view.input.MainInputListener;
import rts.view.screens.MainMenuMain;
import rts.view.screens.Screen;

/**
 * Klasa GameView odpowiada za stworzenie okna gry, wczytanie obrazkow,
 * stworzenie input listenera i wlaczenie menu glownego.
 */
// S
public class GameView
{
    /** The Constant DIMX. */
    public static final int DIMX = 640;
    /** The Constant DIMY. */
    public static final int DIMY = 480;
    /** The title. */
    private final String title = "Rts Game";
    /** The window. */
    private final JFrame window;
    /** The current. */
    private Screen currentScreen;
    /** The game panel. */
    private final MainGamePanel gamePanel;
    /** The input listener. */
    private final MainInputListener inputListener;
    private final ImagesAndPatterns imageLoader;
    private MainMenuMain mainMenu;

    // private final BuildingStats buildingStats;
    /**
     * Instantiates a new game view.
     * 
     * @param queue the queue
     */
    public GameView(final GameEventQueue queue)
    {
        window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle(title);
        window.setResizable(false);
        window.setLayout(new BorderLayout());
        Timers.init();
        gamePanel = new MainGamePanel();
        inputListener = new MainInputListener(gamePanel);
        window.setContentPane(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        imageLoader = new ImagesAndPatterns(getMainGamePanel());
        // imageLoader.loadGameWorldImages();
        // buildingStats = new BuildingStats(imageLoader);
        window.setVisible(true);
    }
    /**
     * Handle key event.
     * 
     * @param event the event
     */
    public void handleKeyEvent(final KeyInputEvent event)
    {
        if(event.wasPressed(KeyEvent.VK_ESCAPE))
        {
            // currentScreen.goToParent();
        }
        currentScreen.handleKeyEvent(event);
    }
    /**
     * Wczytuje nowy panel z zawartoscia ekranu.
     * 
     * @param screen the screen
     */
    public void load(final Screen screen)
    {
        if(currentScreen != null)
        {
            gamePanel.remove(currentScreen.getPanel());
        }
        gamePanel.add(screen.getPanel());
        currentScreen = screen;
        gamePanel.validate();
        gamePanel.repaint();
    }
    public void showDialog(final String info)
    {
        JOptionPane.showMessageDialog((JFrame) gamePanel.getTopLevelAncestor(), info);
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
    public ImagesAndPatterns getImagesAndPatterns()
    {
        return imageLoader;
    }
    public MainGamePanel getMainGamePanel()
    {
        return gamePanel;
    }

    // public BuildingStats getGameMechanics()
    // {
    // return buildingStats;
    // }
    public class MainGamePanel extends JPanel
    {
        private MainGamePanel()
        {
            // setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setLayout(null);
            setFocusable(true);
            setDoubleBuffered(true);
            setPreferredSize(new Dimension(GameView.DIMX, GameView.DIMY));
            setOpaque(true);
        }
        public void handleKeyEvent(final KeyInputEvent event)
        {
            GameView.this.handleKeyEvent(event);
        }
    }

    public void createMainMenu(final Controller controller)
    {
        mainMenu = new MainMenuMain(controller, this);
        load(mainMenu);
    }
    public void loadMainMenu()
    {
        load(mainMenu);
    }
}
