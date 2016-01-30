package rts.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.util.EnumMap;
import rts.controller.netEvents.toClientIngame.ToClientBuildingEvent;
import rts.controller.netEvents.toClientIngame.ToClientItemEvent;
import rts.controller.netEvents.toClientIngame.ToClientProduction;
import rts.model.Item;
import rts.model.serverBuildings.ItemSlot;
import rts.view.ViewModel;
import rts.view.clientModel.BuildingSprite;

public class ProductionInfoTab extends BuildingInfoTab
{
    private final EnumMap<Item, ItemSlot> ingredients;
    private final EnumMap<Item, ItemSlot> products;
    private final ProgressBar progressBar;

    public ProductionInfoTab(final ViewModel mapEnviroment, final BuildingSprite parentBuilding)
    {
        super(mapEnviroment, parentBuilding);
        ingredients = parentBuilding.getPattern().cloneIngredientSlots();
        products = parentBuilding.getPattern().cloneProductSlots();
        progressBar = new ProgressBar(-1, -1);
        progressBar.setBounds(30, 50, 80, 20);
        add(progressBar);
    }
    @Override
    public void handleBuildingEvent(final ToClientBuildingEvent buildingCommand)
    {
        if(buildingCommand instanceof ToClientItemEvent)
        {
            final ToClientItemEvent itemEvent = (ToClientItemEvent) buildingCommand;
            ItemSlot itemSlot = ingredients.get(itemEvent.getItem());
            if(itemSlot == null)
            {
                itemSlot = products.get(itemEvent.getItem());
            }
            itemSlot.setQuantity(itemEvent.getNumber());
            repaint();
        }
        else if(buildingCommand instanceof ToClientProduction)
        {
            final ToClientProduction production = (ToClientProduction) buildingCommand;
            progressBar.reset(production.getProductionTime(), 20);
            progressBar.start();
        }
    }
    @Override
    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        final int line = 0;
        g.setColor(Color.black);
        int i = 0, yPos;
        for(final ItemSlot slot : ingredients.values())
        {
            yPos = 80 + i * 20;
            final String name = slot.getItemType().toString();
            g.drawString(name + " " + slot.getQuantity(), 0, yPos);
            i++;
        }
        for(final ItemSlot slot : products.values())
        {
            yPos = 80 + i * 20;
            final String name = slot.getItemType().toString();
            g.drawString(name + " " + slot.getQuantity(), 0, yPos);
            i++;
        }
    }
}