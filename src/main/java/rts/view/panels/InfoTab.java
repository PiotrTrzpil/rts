package rts.view.panels;

import java.awt.event.ActionEvent;

// TODO: Auto-generated Javadoc
/**
 * Panel informacyjny
 */
public abstract class InfoTab extends LeftPanelTab
{
    /** The pos x. */
    private final int posX = 30;
    /** The pos y. */
    private final int posY = 150;

    /* (non-Javadoc)
     * @see rts.view.panels.LeftPanelTab#initialize()
     */
    @Override
    public void initialize()
    {
    };
    /**
     * Gets the pos x.
     * 
     * @return the pos x
     */
    public int getPosX()
    {
        return posX;
    }
    /**
     * Gets the pos y.
     * 
     * @return the pos y
     */
    public int getPosY()
    {
        return posY;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent e)
    {
    }
}
