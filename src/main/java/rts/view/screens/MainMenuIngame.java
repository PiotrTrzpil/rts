package rts.view.screens;

//import input.KeyLink;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import rts.controller.GameEventQueue;
import rts.view.GameView;
import rts.view.ImagesAndPatterns;

// TODO: Auto-generated Javadoc
/**
 * The Class MainMenu.
 */
public class MainMenuIngame extends Screen implements ActionListener
{
    /** The background. */
    private final Image background;
    /** The banner. */
    private final Image banner;
    private final JButton ret;
    private final GameEventQueue queue;
    private final ImagesAndPatterns imageLoader;

    public MainMenuIngame(final GameView gameView, final GameEventQueue queue)
    {
        //        super(gameView);
        this.queue = queue;
        imageLoader = gameView.getImagesAndPatterns();
        background = imageLoader.getBasicImage("MainBackground");
        banner = imageLoader.getBasicImage("MainBanner");
        ret = new JButton();
        imageLoader.createButton(ret, "Return", 40, 290);
        ret.addActionListener(this);
        add(ret);
        final JButton buutt = new JButton();
        imageLoader.createButton(buutt, "New Game", 200, 290);
        buutt.addActionListener(this);
        add(buutt);
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent e)
    {
        final Object src = e.getSource();
        if(src == ret)
        {
            //            goToParent();
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(final Graphics2D g)
    {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background, 0, 0, null);
        g2d.drawImage(banner, 370, 0, null);
    }
}
