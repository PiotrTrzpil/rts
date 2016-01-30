package rts.model.serverBuildings;

import rts.model.Item;

/**
 * Przechowuje rodzaj przedmiotu, jego ilosc, maksymalna ilosc oraz nadchodzace
 * zmiany ilosci. Uzywane w budynkach.
 */
public class ItemSlot implements Comparable<ItemSlot>
{
    /** The item id. */
    protected final Item itemType;
    /** The max space. */
    protected final int maxSpace;
    /** The quantity. */
    protected int quantity;
    /** The pending puts. */
    private int pendingPuts;
    /** The pending takes. */
    private int pendingTakes;
    /** The locked space. */
    private int lockedSpace;

    /**
     * Instantiates a new item slot.
     * 
     * @param id the id
     */
    public ItemSlot(final Item id)
    {
        itemType = id;
        maxSpace = 8;
    }
    /**
     * Instantiates a new item slot.
     * 
     * @param id the id
     * @param max the max
     */
    public ItemSlot(final Item id, final int max)
    {
        itemType = id;
        maxSpace = max;
    }
    /**
     * Take.
     */
    public void take()
    {
        quantity--;
        pendingTakes--;
        if(quantity < 0)
        {
            throw new RuntimeException("Item exception");
        }
    }
    /**
     * Put.
     */
    public void put()
    {
        quantity++;
        pendingPuts--;
        if(quantity > getMaxSpace())
        {
            throw new RuntimeException("Item exception");
        }
    }
    /**
     * Use for building.
     */
    public void useForBuilding()
    {
        remove(1);
        lockedSpace++;
    }
    /**
     * Adds the.
     * 
     * @param n the n
     */
    public void add(final int n)
    {
        quantity += n;
        // if(quantity<0) throw new RuntimeException("WTF");
    }
    /**
     * Removes the.
     * 
     * @param n the n
     */
    public void remove(final int n)
    {
        quantity -= n;
    }
    /**
     * Can be taken.
     * 
     * @param n the n
     * @return true, if successful
     */
    public boolean canBeTaken(final int n)
    {
        return quantity - pendingTakes - n >= 0;
    }
    /**
     * Can be put.
     * 
     * @param n the n
     * @return true, if successful
     */
    public boolean canBePut(final int n)
    {
        // System.out.println("can be put:" + (quantity + pendingPuts + n +
        // lockedSpace) + " max:"
        // + maxSpace);
        return quantity + pendingPuts + n + lockedSpace <= getMaxSpace();
    }
    /**
     * Gets the quantity.
     * 
     * @return the quantity
     */
    public int getQuantity()
    {
        return quantity;
    }
    /**
     * Gets the incomming.
     * 
     * @return the incomming
     */
    public int getIncomming()
    {
        return pendingPuts;
    }
    /**
     * Gets the outgoing.
     * 
     * @return the outgoing
     */
    public int getOutgoing()
    {
        return pendingTakes;
    }
    /**
     * Pending take.
     */
    public void pendingTake()
    {
        pendingTakes++;
    }
    public void cancelPendingTake()
    {
        pendingTakes--;
    }
    public void cancelPendingPut()
    {
        // System.out.println("pending, qunat:" + quantity + " max:" +
        // maxSpace);
        pendingPuts--;
    }
    /**
     * Pending put.
     */
    public void pendingPut()
    {
        // System.out.println("pending, qunat:" + quantity + " max:" +
        // maxSpace);
        pendingPuts++;
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o)
    {
        return o instanceof ItemSlot && ((ItemSlot) o).itemType.equals(itemType);
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final ItemSlot o)
    {
        if(itemType.ordinal() > o.itemType.ordinal())
        {
            return 1;
        }
        if(itemType.ordinal() < o.itemType.ordinal())
        {
            return -1;
        }
        return 0;
    }
    public void setQuantity(final int number)
    {
        quantity = number;
    }
    public int getMaxSpace()
    {
        return maxSpace;
    }
    public Item getItemType()
    {
        return itemType;
    }
}
