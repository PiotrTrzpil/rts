package rts.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import rts.controller.netEvents.toClientIngame.ToClientBuildingEvent;
import rts.view.ViewModel;
import rts.view.clientModel.BuildingSprite;

// TODO: Auto-generated Javadoc
/**
 * Klasa panelu informacyjnego budynku
 */
public class BuildingInfoTab extends InfoTab
{
    /** Srodowisko. */
    protected final ViewModel mapEnviroment;
    /** Budynek ktorego tyczy sie ten panel. */
    protected final BuildingSprite parentBuilding;
    /** Polozenie i wymiary paska zdrowia. */
    private static final Rectangle healthBarBounds = new Rectangle(0, 15, 100, 15);

    /**
     * Instantiates a new building info tab.
     * 
     * @param mapEnviroment the map enviroment
     * @param parentBuilding the parent building
     */
    public BuildingInfoTab(final ViewModel mapEnviroment, final BuildingSprite parentBuilding)
    {
        this.mapEnviroment = mapEnviroment;
        this.parentBuilding = parentBuilding;
    }
    /**
     * Odebranie przekazania zdarzenia z serwera
     * 
     * @param buildingCommand the building command
     */
    public void handleBuildingEvent(final ToClientBuildingEvent buildingCommand)
    {
    }
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        g.drawString(parentBuilding.getType().toString(), 0, 10);
        g.setColor(Color.green);
        g.drawRect(healthBarBounds.x, healthBarBounds.y, healthBarBounds.width,
            healthBarBounds.height);
        //   g.drawRect(POS_X, POS_Y, HP_BAR_LENGTH, HP_BAR_HEIGHT);
        g.fillRect(healthBarBounds.x, healthBarBounds.y, healthBarBounds.width
            * parentBuilding.getHP() / parentBuilding.getMaxHP(), healthBarBounds.height);
    }
}
