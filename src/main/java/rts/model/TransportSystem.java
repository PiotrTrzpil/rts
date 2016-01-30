package rts.model;

//import buildings.BuildingWithItems;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import rts.model.serverBuildings.BuildingControl;
import rts.model.serverBuildings.Building.ItemDemander;
import rts.model.serverBuildings.Building.ItemModule;
import rts.model.serverBuildings.Building.ItemSupplier;
import rts.model.serverBuildings.Storehouse.StorehouseDemanderPart;

// TODO: Auto-generated Javadoc
/**
 * System transportu towarow miedzy budynkami. Budynek po stworzeniu musi
 * zarejestrowac sie pod kazdym rodzajem przedmiotu przetrzymywanym w danym
 * budynku. Zadaniem systemu jest znajdywanie zadan transportu zgodnie z
 * priorytetami przenoszenia przedmiotow oraz priorytetami otrzymywania
 * przedmiotow przez budynki.
 */
public class TransportSystem
{
    /** Lista z przedmiotami uszeregowanymi zgodnie z malejacymi priorytetami . */
    private final List<Item> itemPriority;
    /**
     * Mapa przyporzadkujaca przedmiotom liste rodzajow budynkow uporzadkowana
     * zgodnie z malejacymi priorytetami otrzymywania danego przedmiotu.
     */
    private final EnumMap<Item, List<BuildingType>> demanderPriority;
    /** Lista magazynow. */
    private final List<StorehouseDemanderPart> storehouses;
    /**
     * Mapa przyporzadkujaca przedmiotom liste zarejestrowanych budynkow
     * produkujacych ten przedmiot.
     */
    private final EnumMap<Item, List<ItemSupplier>> suppliers;
    /**
     * Mapa przyporzadkujaca przedmiotom liste zarejestrowanych budynkow
     * potrzebujacych tego przedmiotu.
     */
    private final EnumMap<Item, List<ItemDemander>> demanders;
    /** Zmienna losowa. */
    private final Random rand;

    /**
     * Instantiates a new transport system.
     */
    public TransportSystem()
    {
        suppliers = new EnumMap<Item, List<ItemSupplier>>(Item.class);
        demanders = new EnumMap<Item, List<ItemDemander>>(Item.class);
        itemPriority = new ArrayList<Item>();
        demanderPriority = new EnumMap<Item, List<BuildingType>>(Item.class);
        for(final Item item : Item.values())
        {
            suppliers.put(item, new ArrayList<ItemSupplier>());
            demanders.put(item, new ArrayList<ItemDemander>());
        }
        storehouses = new ArrayList<StorehouseDemanderPart>();
        setItemPriorities();
        setDemanderPriorities();
        rand = new Random(System.currentTimeMillis());
    }
    /**
     * Ustawia priorytety przenoszenia przedmiotow
     */
    private void setItemPriorities()
    {
        itemPriority.add(Item.LOG);
        itemPriority.add(Item.PLANK);
        itemPriority.add(Item.STONE);
        itemPriority.add(Item.BOW);
        itemPriority.add(Item.SWORD);
        itemPriority.add(Item.SHIELD);
        itemPriority.add(Item.PIEROGI);
    }
    /**
     * Ustawia priorytety budynkow zapisane w kazdym przedmiocie Item
     */
    private void setDemanderPriorities()
    {
        for(final Item item : Item.values())
        {
            demanderPriority.put(item, item.demanderPriority);
        }
        // Item.LOG.demanderPriority;
        // final ArrayList<BuildingType> forLogs = new
        // ArrayList<BuildingType>();
        // forLogs.add(BuildingType.LUMBER_MILL);
        // demanderPriority.put(Item.LOG, forLogs);
        // final ArrayList<BuildingType> forPlanks = new
        // ArrayList<BuildingType>();
        // forPlanks.add(BuildingType.BARRACKS);
        // demanderPriority.put(Item.PLANK, forPlanks);
    }
    /**
     * Rejestruje modul produkujacy lub otrzymujacy na dany przedmiot
     * 
     * @param module modul
     * @param item przedmiot
     */
    public void addModule(final ItemModule module, final Item item)
    {
        if(module instanceof ItemSupplier)
        {
            final ItemSupplier itemSupplier = (ItemSupplier) module;
            suppliers.get(item).add(itemSupplier);
        }
        else if(module instanceof ItemDemander)
        {
            final ItemDemander itemDemander = (ItemDemander) module;
            demanders.get(item).add(itemDemander);
        }
    }
    /**
     * Wyrejestrowuje modul produkujacy lub otrzymujacy na danym przedmiocie
     * 
     * @param module modul
     * @param item przedmiot
     */
    public void removeModule(final ItemModule module, final Item item)
    {
        if(module instanceof ItemSupplier)
        {
            final ItemSupplier itemSupplier = (ItemSupplier) module;
            suppliers.get(item).remove(itemSupplier);
        }
        else if(module instanceof ItemDemander)
        {
            final ItemDemander itemDemander = (ItemDemander) module;
            demanders.get(item).remove(itemDemander);
        }
    }
    /**
     * Rejestruje magazyn
     * 
     * @param storehouse magazyn
     */
    public void addStorehouse(final StorehouseDemanderPart storehouse)
    {
        storehouses.add(storehouse);
    }
    /**
     * Wyrejestrowuje magazyn
     * 
     * @param storehouse magazyn
     */
    public void removeStorehouse(final StorehouseDemanderPart storehouse)
    {
        storehouses.remove(storehouse);
    }
    /**
     * Probuje znalesc zadanie transportowe.
     * 
     * @return zadanie transportowe lub null jesli takiego nie znaleziono
     */
    public TransportTask findTransportTask()
    {
        final TransportTask normalTransport = normalTransport();
        return normalTransport != null ? normalTransport : storehouseTransport();
    }
    /**
     * Transport towaru miedzy normalnymi budynkami lub ze skladu do normalnego
     * budynku. W kolejnosci priorytetow przedmiotow znajduje przedmiot ktory
     * moze
     * 
     * @return zadanie transportowe lub null jesli takiego nie znaleziono
     */
    private TransportTask normalTransport()
    {
        ItemDemander receiver;
        ItemSupplier source;
        for(final Item item : itemPriority)
        {
            if(!suppliers.get(item).isEmpty() && !demanders.get(item).isEmpty())
            {
                // final List<ItemDemander> list =
                // getHighestPriorityDemanders(item);
                // if(list.isEmpty())
                // {
                // receiver = (ItemDemander) getRandom(demanders.get(item));
                // if(receiver == null || !receiver.canReceive(item, 1))
                // {
                // continue;
                // }
                // }
                // else
                // {
                // receiver = (ItemDemander) getRandom(findDemander(item));
                // }
                receiver = findDemander(item);
                if(receiver != null)
                {
                    source = getClosestSupplier(item, receiver);
                    if(source != null)
                    {
                        return new TransportTask(source, receiver, item);
                    }
                }
            }
        }
        return null;
    }
    /**
     * Transport towaru do skladu.
     * 
     * @return zadanie transportowe lub null jesli takiego nie znaleziono
     */
    private TransportTask storehouseTransport()
    {
        ItemSupplier source;
        for(final Item item : itemPriority)
        {
            final List<ItemSupplier> supplierList = suppliers.get(item);
            if(!supplierList.isEmpty() && !storehouses.isEmpty())
            {
                final List<ItemSupplier> readySuppliers = new LinkedList<ItemSupplier>();
                for(final ItemSupplier it : supplierList)
                {
                    if(!(it.getType() == BuildingType.STOREHOUSE) && it.canHandProduct(item, 1))
                    {
                        readySuppliers.add(it);
                    }
                }
                source = (ItemSupplier) getRandom(readySuppliers);
                if(source != null)
                {
                    final ItemDemander dem = (ItemDemander) closestBuilding(storehouses, source);
                    return new TransportTask(source, dem, item);
                }
            }
        }
        return null;
    }
    /**
     * Zwraca losowy modul z listy
     * 
     * @param list dana lista
     * 
     * @return losowy modul lub null gdy lista jest pusta
     */
    private BuildingControl getRandom(final List<? extends BuildingControl> list)
    {
        return list.isEmpty() ? null : list.get(rand.nextInt(list.size()));
    }
    /**
     * Znajduje odbiorce przedmiotu.
     * 
     * @param item dany przedmiot
     * 
     * @return znaleziony odbiorca o najwyzszym priorytecie oczekujacy na
     *         przedmiot lub null gdy nie znaleziono.
     */
    private ItemDemander findDemander(final Item item)
    {
        // if(demanderPriority.get(item) != null)
        // {
        final List<ItemDemander> ret = new ArrayList<ItemDemander>();
        for(final BuildingType bType : demanderPriority.get(item))
        {
            for(final ItemDemander demander : demanders.get(item))
            {
                if(demander.getType() == bType && demander.canReceive(item, 1))
                {
                    ret.add(demander);
                }
            }
            if(!ret.isEmpty())
            {
                break;
            }
        }
        return (ItemDemander) getRandom(ret);
        // }
        // return null;
    }
    /**
     * Znajduje dostawce z gotowym towarem znajdujacego sie najblizej danego
     * odbiorcy.
     * 
     * @param item dany towar
     * @param receiver dany odbiorca
     * 
     * @return najblizszy dostawca towaru lub null gdy nie znaleziono
     */
    private ItemSupplier getClosestSupplier(final Item item, final ItemDemander receiver)
    {
        final List<ItemSupplier> readySuppliers = new LinkedList<ItemSupplier>();
        for(final ItemSupplier supplier : suppliers.get(item))
        {
            if(supplier.getSuroundingBuilding() != receiver.getSuroundingBuilding()
                && supplier.canHandProduct(item, 1))
            {
                readySuppliers.add(supplier);
            }
        }
        return (ItemSupplier) closestBuilding(readySuppliers, receiver);
    }
    /**
     * Z listy wybiera budynek najblizszy danemu budynkowi.
     * 
     * @param list lista budynkow
     * @param b dany budynek
     * 
     * @return znaleziony najblizszy budynek
     */
    private BuildingControl closestBuilding(final List<? extends BuildingControl> list,
        final BuildingControl b)
    {
        BuildingControl ret = null;
        double min = Double.MAX_VALUE;
        double distAct;
        for(final BuildingControl it : list)
        {
            distAct = it.distance(b);
            if(distAct < min)
            {
                min = distAct;
                ret = it;
            }
        }
        return ret;
    }
}
