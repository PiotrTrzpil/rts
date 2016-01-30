package rts.model.serverBuildings;

import java.util.EnumMap;
import rts.model.Item;
import rts.model.WorkerOvermind;
import rts.model.objectPatterns.BuildingPattern;

// TODO: Auto-generated Javadoc
/**
 * Budynek skladowy.
 */
public class Storehouse extends Building// Building implements
{
    /**
     * The Class StorehouseDemanderPart.
     */
    public class StorehouseDemanderPart extends ItemDemander
    {
        /**
         * Instantiates a new storehouse demander part.
         * 
         * @param resources the resources
         */
        public StorehouseDemanderPart(final EnumMap<Item, ItemSlot> resources)
        {
            super(resources);
        }
        /* (non-Javadoc)
         * @see rts.model.serverBuildings.Building.ItemModule#addToTransportSystem(rts.model.WorkerOvermind)
         */
        @Override
        public void addToTransportSystem(final WorkerOvermind workerOvermid)
        {
            workerOvermid.getTransportSystem().addStorehouse(this);
        }
        /* (non-Javadoc)
         * @see rts.model.serverBuildings.Building.ItemModule#removeFromTransportSystem(rts.model.WorkerOvermind)
         */
        @Override
        public void removeFromTransportSystem(final WorkerOvermind workerOvermid)
        {
            workerOvermid.getTransportSystem().removeStorehouse(this);
        }
    }

    /** The supplier part. */
    private final ItemSupplier supplierPart;
    /** The demander part. */
    private final StorehouseDemanderPart demanderPart;
    /** The resources. */
    private final EnumMap<Item, ItemSlot> resources;

    /**
     * Instantiates a new storehouse.
     * 
     * @param pattern the pattern
     */
    public Storehouse(final BuildingPattern pattern)
    {
        super(pattern);
        resources = new EnumMap<Item, ItemSlot>(Item.class);
        supplierPart = new ItemSupplier(resources);
        demanderPart = new StorehouseDemanderPart(resources);
        for(final Item item : Item.values())
        {
            resources.put(item, new ItemSlot(item, Integer.MAX_VALUE));
        }
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.Building#getDemanderPart()
     */
    @Override
    public ItemDemander getDemanderPart()
    {
        return demanderPart;
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.Building#getSupplierPart()
     */
    @Override
    public ItemSupplier getSupplierPart()
    {
        return supplierPart;
    }
    /**
     * Dodaje przedmioty
     * 
     * @param item the item
     * @param n the n
     */
    public void addItem(final Item item, final int n)
    {
        supplierPart.magicAddItem(item, n);
    }
}
