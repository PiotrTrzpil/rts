package rts.model.serverBuildings;

//import terrain.*;
import java.util.Collection;
import java.util.EnumMap;
import rts.controller.netEvents.toClientIngame.ToClientItemEvent;
import rts.controller.netEvents.toServerEvents.ToServerBuildingEvent;
import rts.controller.server.ServerPlayer;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.CoordsDouble;
import rts.misc.exceptions.ItemSlotNotFoundException;
import rts.model.BuildingType;
import rts.model.Item;
import rts.model.ObjectID;
import rts.model.PlayerObject;
import rts.model.MapObject;
import rts.model.ServerModel;
import rts.model.WorkerOvermind;
import rts.model.map.PathGraph;
import rts.model.objectPatterns.BuildingPattern;
import rts.model.objectPatterns.StaticItemSlot;
import rts.model.serverUnits.Unit;
import rts.view.clientModel.BuildingImage;

// TODO: Auto-generated Javadoc
/**
 * Interfejs reprezentujacy stan budynku - budowany lub juz zbudowany.
 */
interface BuildingShape
{
    // public InfoTab getInfoTabRedirecton();
    // public void update(final long elapsedTime);
    // public void initialize();
    // public Image getImage();
}

/**
 * Klasa reprezentujaca budynek.
 */
public abstract class Building extends PlayerObject implements BuildingControl, BuildingShape
{
    // /** The current shape. */
    // private BuildingShape currentShape;
    /** The pattern. */
    private final BuildingPattern pattern;
    /** The bounds. */
    private ConstRect bounds;
    /** The building sprite. */
    private BuildingImage buildingSprite;

    /**
     * Instantiates a new server building.
     * 
     * @param pattern the pattern
     */
    public Building(final BuildingPattern pattern)
    {
        this.pattern = pattern;
    }
    /**
     * Inits the parametrs.
     * 
     * @param position the position
     * @param buildingSprite1 the building sprite1
     * @param owner the owner
     * @param newID the new id
     * @param mapEnv the map env
     */
    public void initParametrs(final Coords position, final BuildingImage buildingSprite1,
        final ServerPlayer owner, final ObjectID newID, final ServerModel mapEnv)
    {
        super.initParametrs(mapEnv, position, owner, newID, pattern.getType().getMaxHealthPoints());;
        this.mapEnv = mapEnv;
        buildingSprite = buildingSprite1;
        bounds = new ConstRect(getPosition(), buildingSprite1.getDimensions());
    }
    /* (non-Javadoc)
     * @see rts.model.ServerMapObject#setPosition(rts.misc.CoordsDouble)
     */
    @Override
    public void setPosition(final CoordsDouble coordsDouble)
    {
        /** Nie mozna zmienic pozycji budynku. */
    }
    /**
     * Handle event.
     * 
     * @param buildingCommand the building command
     */
    public void handleEvent(final ToServerBuildingEvent buildingCommand)
    {
    }
    /* (non-Javadoc)
     * @see rts.model.ServerOwnableObject#activate()
     */
    @Override
    public void activate()
    {
        addToTransportSystem(getOwner().getWorkerOvermind());
    }
    @Override
    public void deactivate()
    {
        removeFromTransportSystem(getOwner().getWorkerOvermind());
    }
    /**
     * Adds the to transport system.
     * 
     * @param overmind the overmind
     */
    public void addToTransportSystem(final WorkerOvermind overmind)
    {
        final ItemDemander demanderPart = getDemanderPart();
        if(demanderPart != null)
        {
            demanderPart.addToTransportSystem(overmind);
        }
        final ItemSupplier supplierPart = getSupplierPart();
        if(supplierPart != null)
        {
            supplierPart.addToTransportSystem(overmind);
        }
    }
    /**
     * Removes the from transport system.
     * 
     * @param overmind the overmind
     */
    public void removeFromTransportSystem(final WorkerOvermind overmind)
    {
        final ItemDemander demanderPart = getDemanderPart();
        if(demanderPart != null)
        {
            demanderPart.removeFromTransportSystem(overmind);
        }
        final ItemSupplier supplierPart = getSupplierPart();
        if(supplierPart != null)
        {
            supplierPart.removeFromTransportSystem(overmind);
        }
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.BuildingControl#distance(rts.model.serverBuildings.BuildingControl)
     */
    public double distance(final BuildingControl another)
    {
        return getExitPosition().distance(another.getExitPosition());
    }
    /* (non-Javadoc)
     * @see rts.model.ServerMapObject#distance(rts.model.ServerMapObject)
     */
    @Override
    public double distance(final MapObject another)
    {
        if(another instanceof Unit)
        {
            final Coords unitPos = another.getPosition();
            final Coords fromCenterIntersectionPoint = getBounds().getFromCenterIntersectionPoint(
                unitPos);
            if(fromCenterIntersectionPoint == null)
            {
                return Double.MAX_VALUE;
            }
            return fromCenterIntersectionPoint.distance(unitPos);
        }
        else
        {
            return getPosition().distance(another.getPosition());
        }
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.BuildingControl#getBounds()
     */
    @Override
    public ConstRect getBounds()
    {
        return bounds;
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.BuildingControl#getType()
     */
    public BuildingType getType()
    {
        return pattern.getType();
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.BuildingControl#getExitPosition()
     */
    public Coords getExitPosition()
    {
        return new Coords(getEntrancePosition().getX(), getBounds().getLowerRight().getY()
            + PathGraph.NODE_SPACE);
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.BuildingControl#getEntrancePosition()
     */
    public Coords getEntrancePosition()
    {
        final int x = buildingSprite.getDoorX() + getPosition().getX();
        return new Coords(x, getBounds().getLowerRight().getY() - 20);
    }
    /**
     * Gets the demander part.
     * 
     * @return the demander part
     */
    public ItemDemander getDemanderPart()
    {
        return null;
    }
    /**
     * Gets the supplier part.
     * 
     * @return the supplier part
     */
    public ItemSupplier getSupplierPart()
    {
        return null;
    }

    /**
     * Klasa skracajaca kod. Implementuje metody BuildingControl.
     */
    public abstract class BuildingAdapter implements BuildingControl
    {
        public double distance(final BuildingControl another)
        {
            return Building.this.distance(another);
        }
        /*
         * (non-Javadoc)
         *
         * @see objects.buildings.BuildingControl#getBounds()
         */
        public ConstRect getBounds()
        {
            return Building.this.getBounds();
        }
        /**
         * Zwraca otaczajacy obiekt Building.
         * 
         * @return otaczajacy obiekt Building.
         */
        public BuildingControl getSuroundingBuilding()
        {
            return Building.this;
        }
        /* (non-Javadoc)
         * @see rts.model.serverBuildings.BuildingControl#getExitPosition()
         */
        public Coords getExitPosition()
        {
            return Building.this.getExitPosition();
        }
        /* (non-Javadoc)
         * @see rts.model.serverBuildings.BuildingControl#getEntrancePosition()
         */
        public Coords getEntrancePosition()
        {
            return Building.this.getEntrancePosition();
        }
        /* (non-Javadoc)
         * @see rts.model.serverBuildings.BuildingControl#getID()
         */
        public ObjectID getID()
        {
            return Building.this.getID();
        }
        /* (non-Javadoc)
         * @see rts.model.serverBuildings.BuildingControl#getType()
         */
        public BuildingType getType()
        {
            return Building.this.getType();
        }
    }

    /**
     * Modul przechowujacy przedmioty
     */
    public class ItemModule extends BuildingAdapter
    {
        /** The items. */
        protected final EnumMap<Item, ItemSlot> items;

        /**
         * Instantiates a new item module.
         * 
         * @param items the items
         */
        public ItemModule(final EnumMap<Item, ItemSlot> items)
        {
            this.items = items;
        }
        /**
         * Send item quantity.
         *
         * @param item the item
         */
        protected void sendItemQuantity(final Item item)
        {
            try
            {
                getOwner().send(new ToClientItemEvent(getID(), item, getSlotFor(item).getQuantity()));
            }
            catch(final ItemSlotNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        /**
         * Gets the slot for.
         * 
         * @param item the item
         * @return the slot for
         * @throws ItemSlotNotFoundException the item slot not found exception
         */
        protected ItemSlot getSlotFor(final Item item) throws ItemSlotNotFoundException
        {
            final ItemSlot itemSlot = items.get(item);
            if(itemSlot == null)
            {
                throw new ItemSlotNotFoundException(item.name());
            }
            return itemSlot;
        }
        /**
         * Adds the to transport system.
         * 
         * @param workerOvermid the worker overmid
         */
        public void addToTransportSystem(final WorkerOvermind workerOvermid)
        {
            for(final ItemSlot ingredient : items.values())
            {
                workerOvermid.getTransportSystem().addModule(this, ingredient.getItemType());
            }
        }
        /**
         * Removes the from transport system.
         * 
         * @param workerOvermid the worker overmid
         */
        public void removeFromTransportSystem(final WorkerOvermind workerOvermid)
        {
            for(final ItemSlot ingredient : items.values())
            {
                workerOvermid.getTransportSystem().removeModule(this, ingredient.getItemType());
            }
        }
    }

    /**
     * Klasa reprezentujaca ten budynek jako potencjalnego odbiorce towarow.
     */
    public class ItemDemander extends ItemModule
    {
        /**
         * Instantiates a new item demander.
         * 
         * @param ingr the ingr
         */
        public ItemDemander(final EnumMap<Item, ItemSlot> ingr)
        {
            super(ingr);
        }
        /**
         * Put ingredient.
         *
         * @param item the item
         */
        public void putIngredient(final Item item)
        {
            try
            {
                getSlotFor(item).put();
                sendItemQuantity(item);
            }
            catch(final ItemSlotNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        /**
         * Utilize ingredients.
         * 
         * @param slots the slots
         */
        public void utilizeIngredients(final Collection<StaticItemSlot> slots)
        {
            for(final StaticItemSlot s : slots)
            {
                try
                {
                    getSlotFor(s.getItemType()).remove(s.getQuantity());
                    sendItemQuantity(s.getItemType());
                }
                catch(final ItemSlotNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        /**
         * Checks for ingredients.
         * 
         * @param slots the slots
         * @return true, if successful
         */
        public boolean hasIngredients(final Collection<StaticItemSlot> slots)
        {
            for(final StaticItemSlot s : slots)
            {
                try
                {
                    if(getSlotFor(s.getItemType()).getQuantity() < s.getQuantity())
                    {
                        return false;
                    }
                }
                catch(final ItemSlotNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        /**
         * Pending put.
         * 
         * @param itemID the item id
         */
        public void pendingPut(final Item itemID)
        {
            try
            {
                getSlotFor(itemID).pendingPut();
            }
            catch(final ItemSlotNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /**
         * Cancel pending put.
         * 
         * @param itemID the item id
         */
        public void cancelPendingPut(final Item itemID)
        {
            try
            {
                getSlotFor(itemID).cancelPendingPut();
            }
            catch(final ItemSlotNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /**
         * Can receive.
         * 
         * @param item the item
         * @param n the n
         * @return true, if successful
         */
        public boolean canReceive(final Item item, final int n)
        {
            try
            {
                return getSlotFor(item).canBePut(n);
            }
            catch(final ItemSlotNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Klasa reprezentujaca ten budynek jako potencjalnego dostawce towarow.
     */
    public class ItemSupplier extends ItemModule
    {
        /**
         * Instantiates a new item supplier.
         * 
         * @param set the set
         */
        public ItemSupplier(final EnumMap<Item, ItemSlot> set)
        {
            super(set);
        }
        /**
         * Take product.
         *
         * @param item the item
         */
        public void takeProduct(final Item item)
        {
            try
            {
                getSlotFor(item).take();
                sendItemQuantity(item);
            }
            catch(final ItemSlotNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        /**
         * Checks for place for.
         * 
         * @param slots the slots
         * @return true, if successful
         */
        public boolean hasPlaceFor(final Collection<StaticItemSlot> slots)
        {
            for(final StaticItemSlot s : slots)
            {
                try
                {
                    if(!getSlotFor(s.getItemType()).canBePut(s.getQuantity()))
                    {
                        return false;
                    }
                }
                catch(final ItemSlotNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        /**
         * Magic add item.
         *
         * @param item the item
         * @param n the n
         */
        protected void magicAddItem(final Item item, final int n)
        {
            try
            {
                getSlotFor(item).add(n);
                sendItemQuantity(item);
            }
            catch(final ItemSlotNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        /**
         * Pending take.
         * 
         * @param itemID the item id
         */
        public void pendingTake(final Item itemID)
        {
            try
            {
                getSlotFor(itemID).pendingTake();
            }
            catch(final ItemSlotNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /**
         * Cancel pending take.
         * 
         * @param itemID the item id
         */
        public void cancelPendingTake(final Item itemID)
        {
            try
            {
                getSlotFor(itemID).cancelPendingTake();
            }
            catch(final ItemSlotNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /**
         * Can hand product.
         * 
         * @param item the item
         * @param n the n
         * @return true, if successful
         */
        public boolean canHandProduct(final Item item, final int n)
        {
            try
            {
                return getSlotFor(item).canBeTaken(n);
            }
            catch(final ItemSlotNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
    }
}