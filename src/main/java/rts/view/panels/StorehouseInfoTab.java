package rts.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.util.EnumMap;
import rts.controller.netEvents.toClientIngame.ToClientBuildingEvent;
import rts.controller.netEvents.toClientIngame.ToClientItemEvent;
import rts.model.Item;
import rts.model.serverBuildings.ItemSlot;
import rts.view.ViewModel;
import rts.view.clientModel.BuildingSprite;

// TODO: Auto-generated Javadoc
/**
 * Panel skladu
 */
public class StorehouseInfoTab extends BuildingInfoTab
{
    /** The resources. */
    private final EnumMap<Item, ItemSlot> resources;

    /**
     * Instantiates a new storehouse info tab.
     * 
     * @param mapEnviroment the map enviroment
     * @param parentBuilding the parent building
     */
    public StorehouseInfoTab(final ViewModel mapEnviroment, final BuildingSprite parentBuilding)
    {
        super(mapEnviroment, parentBuilding);
        resources = new EnumMap<Item, ItemSlot>(Item.class);
        for(final Item item : Item.values())
        {
            resources.put(item, new ItemSlot(item, Integer.MAX_VALUE));
        }
    }
    /* (non-Javadoc)
     * @see rts.view.panels.BuildingInfoTab#handleBuildingEvent(rts.controller.toClientEvents.ToClientBuildingEvent)
     */
    @Override
    public void handleBuildingEvent(final ToClientBuildingEvent buildingCommand)
    {
        if(buildingCommand instanceof ToClientItemEvent)
        {
            final ToClientItemEvent itemEvent = (ToClientItemEvent) buildingCommand;
            final ItemSlot itemSlot = resources.get(itemEvent.getItem());
            itemSlot.setQuantity(itemEvent.getNumber());
            repaint();
        }
    }
    /* (non-Javadoc)
     * @see rts.view.panels.BuildingInfoTab#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        int line = 0;
        g.setColor(Color.black);
        //     g.drawString(parentBuilding.getType().toString(), 0, (line += 20));
        //    g.drawString("Work Progress: " + prodQueue.completePercentage(), 0, (line += 20));
        int yPos;
        for(final ItemSlot slot : resources.values())
        {
            yPos = 50 + line * 20;
            final String name = slot.getItemType().toString();
            g.drawString(name + " " + slot.getQuantity(), 0, yPos);
            line++;
        }
    }
}