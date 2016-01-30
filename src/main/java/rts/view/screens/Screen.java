package rts.view.screens;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JPanel;
import rts.view.GameView;
import rts.view.input.KeyInputEvent;

public abstract class Screen// extends JPanel
{
    // protected GameView gameView;
    private Screen parentScreen;
    private final ScreenPanel panel;

    // protected ImageLoader imageLoader;
    public Screen()
    {
        // super(null);
        // gameView = mainMenu;
        // imageLoader = mainMenu.getImageLoader();
        panel = new ScreenPanel();
        panel.setDoubleBuffered(true);
        panel.setLayout(null);
        panel.setBounds(0, 0, GameView.DIMX, GameView.DIMY);
    }
    // public void setParent(final Screen parentScreen)
    // {
    // this.parentScreen = parentScreen;
    // }
    // public void goToParent()
    // {
    // if(parentScreen != null)
    // {
    // gameView.load(parentScreen);
    // }
    // }
    public void handleKeyEvent(final KeyInputEvent event)
    {
    }
    public JPanel getPanel()
    {
        return panel;
    }
    public void add(final JComponent comp)
    {
        panel.add(comp);
    }
    public void paintComponent(final Graphics2D g)
    {
    }
    public Container getTopLevelAncestor()
    {
        return panel.getTopLevelAncestor();
    }

    public class ScreenPanel extends JPanel
    {
        @Override
        public void paintComponent(final Graphics g)
        {
            final Graphics2D g2d = (Graphics2D) g;
            Screen.this.paintComponent(g2d);
        }
    }
}
