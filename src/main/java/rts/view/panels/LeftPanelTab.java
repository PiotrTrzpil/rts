package rts.view.panels;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * Klasa zakladki panelu po lewej stronie ekranu.
 */
public abstract class LeftPanelTab extends JPanel implements ActionListener
{
    /** The Constant tabBounds. */
    private static final Rectangle tabBounds = new Rectangle(10, 180, 110, 290);

    /**
     * Instantiates a new left panel tab.
     */
    public LeftPanelTab()
    {
        super(null);
        setBounds(tabBounds);
    }
    /**
     * Initialize.
     */
    public void initialize()
    {
    };
}
