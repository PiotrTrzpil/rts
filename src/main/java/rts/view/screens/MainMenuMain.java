package rts.view.screens;

//import input.KeyLink;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import rts.controller.Controller;
import rts.model.map.MapSettings;
import rts.view.GameView;
import rts.view.ImagesAndPatterns;

// TODO: Auto-generated Javadoc
/**
 * The Class MainMenu.
 */
public class MainMenuMain extends Screen implements ActionListener
{
    /** The background. */
    private final Image background;
    /** The banner. */
    private final Image banner;
    /** The start button. */
    //    private final JButton startButton;
    private final JButton buutt;
    private final JButton join;
    private final JTextField hostName;
    private final MapSettings mapSettings;
    private final GameView gameView;
    private final ImagesAndPatterns imageLoader;
    private final Controller controller;
    private final JTextField playerName;

    public MainMenuMain(final Controller controller, final GameView gameView2)
    {
        this.controller = controller;
        gameView = gameView2;
        imageLoader = gameView.getImagesAndPatterns();
        background = imageLoader.getBasicImage("MainBackground");
        banner = imageLoader.getBasicImage("MainBanner");
        //        startButton = new JButton();
        //        imageLoader.createButton(startButton, "Start", 40, 290);
        //        startButton.setToolTipText("Start the game.");
        //        startButton.addActionListener(this);
        //        add(startButton);
        buutt = new JButton();
        imageLoader.createButton(buutt, "New Game", 200, 290);
        buutt.addActionListener(this);
        add(buutt);
        join = new JButton();
        imageLoader.createButton(join, "Join Game", 350, 290);
        join.addActionListener(this);
        add(join);
        playerName = new JTextField("Player Nickname");
        playerName.setBounds(300, 140, 200, 30);
        add(playerName);
        hostName = new JTextField("localhost");
        hostName.setBounds(300, 190, 200, 30);
        add(hostName);
        mapSettings = new MapSettings();
    }
    public void actionPerformed(final ActionEvent event)
    {
        final Object src = event.getSource();
        if(src == buutt)
        {
            try
            {
                final ScreenHost hostGame = controller.hostGame(mapSettings);
                gameView.load(hostGame);
            }
            catch(final BindException e1)
            {
                JOptionPane.showMessageDialog((JFrame) gameView.getMainGamePanel()
                        .getTopLevelAncestor(), "Cannot start server. Address in use.");
            }
            catch(final Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(src == join)
        {
            try
            {
                controller.joinGame(hostName.getText(), playerName.getText());
            }
            catch(final UnknownHostException e1)
            {
                JOptionPane.showMessageDialog((JFrame) gameView.getMainGamePanel()
                        .getTopLevelAncestor(), "Error. Host not found.");
            }
            catch(final IOException e1)
            {
                JOptionPane.showMessageDialog((JFrame) gameView.getMainGamePanel()
                        .getTopLevelAncestor(), "Error. No responce from server.");
            }
        }
    }
    @Override
    public void paintComponent(final Graphics2D g)
    {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background, 0, 0, null);
        g2d.drawImage(banner, 370, 0, null);
    }
}
