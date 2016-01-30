package rts.model.serverUnits;

import rts.model.Item;
import rts.model.serverBuildings.Building.ItemDemander;
import rts.model.serverBuildings.Building.ItemSupplier;

// TODO: Auto-generated Javadoc
/**
 * Tragarz
 */
public class Carrier extends Unit
{
    /** Przenoszony przedmiot. */
    private Item carriedItem = Item.NULL_ITEM;

    /* (non-Javadoc)
     * @see rts.model.ServerOwnableObject#activate()
     */
    @Override
    public void activate()
    {
        getOwner().getWorkerOvermind().addFreeCarrier(this);
    }
    /**
     * Take item.
     * 
     * @param building the building
     * @param item the item
     */
    public void takeItem(final ItemSupplier building, final Item item)
    {
        building.takeProduct(item);
        carriedItem = item;
    }
    /**
     * Put item.
     * 
     * @param building the building
     */
    public void putItem(final ItemDemander building)
    {
        building.putIngredient(carriedItem);
        carriedItem = Item.NULL_ITEM;
    }
    /**
     * Checks if is carrying item.
     * 
     * @return true, if is carrying item
     */
    private boolean isCarryingItem()
    {
        return carriedItem != Item.NULL_ITEM;
    }
    /**
     * Checks if is carrying item.
     * 
     * @param item the item
     * @return true, if is carrying item
     */
    private boolean isCarryingItem(final Item item)
    {
        return carriedItem == item;
    }
    /**
     * Gets the item.
     * 
     * @return the item
     */
    public Item getItem()
    {
        return carriedItem;
    }
}
