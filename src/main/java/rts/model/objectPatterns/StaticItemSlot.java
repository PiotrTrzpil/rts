package rts.model.objectPatterns;

import rts.model.Item;
import rts.model.serverBuildings.ItemSlot;

// TODO: Auto-generated Javadoc
/**
 * Przechowuje rodzaj przedmiotu i jego ilosc. Uzywane w receptach.
 */
public class StaticItemSlot implements Comparable<StaticItemSlot>
{
    /** The item id. */
    private final Item itemID;
    /** The quant. */
    private final int quant;

    /**
     * Instantiates a new static item slot.
     * 
     * @param item the item
     * @param n the n
     */
    public StaticItemSlot(final Item item, final int n)
    {
        itemID = item;
        quant = n;
    }
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final StaticItemSlot o)
    {
        if(itemID.ordinal() > o.itemID.ordinal())
        {
            return 1;
        }
        if(itemID.ordinal() < o.itemID.ordinal())
        {
            return -1;
        }
        return 0;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o)
    {
        return o instanceof StaticItemSlot && ((ItemSlot) o).getItemType().equals(itemID);
    }
    public Item getItemType()
    {
        return itemID;
    }
    public int getQuantity()
    {
        return quant;
    }
}