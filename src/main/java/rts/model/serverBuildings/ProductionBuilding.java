package rts.model.serverBuildings;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import rts.controller.Command;
import rts.controller.ServerGameQueue;
import rts.controller.netEvents.toClientIngame.ToClientProduction;
import rts.controller.server.ServerPlayer;
import rts.misc.Coords;
import rts.model.Item;
import rts.model.ObjectID;
import rts.model.ServerModel;
import rts.model.objectPatterns.BuildingPattern;
import rts.model.objectPatterns.ItemRecipe;
import rts.model.objectPatterns.StaticItemSlot;
import rts.view.clientModel.BuildingImage;

public class ProductionBuilding extends Building// BuildingWithItems
{
    private static final Random rand = new Random();
    private final ArrayList<ItemRecipe> recipes;
    private ItemRecipe current;
    private final ItemDemander demanderPart;
    private final ItemSupplier supplierPart;
    private Timer productionTimer;
    private ServerGameQueue gameQueue;

    public ProductionBuilding(final BuildingPattern pattern)
    {
        super(pattern);
        recipes = pattern.getItemRecipes();
        demanderPart = new ItemDemander(pattern.cloneIngredientSlots())
        {
            @Override
            public void putIngredient(final Item item)
            {
                super.putIngredient(item);
                tryStartNext();
            }
        };
        supplierPart = new ItemSupplier(pattern.cloneProductSlots())
        {
            @Override
            public void takeProduct(final Item item)
            {
                super.takeProduct(item);
                tryStartNext();
            }
        };
    }
    @Override
    public void initParametrs(final Coords position, final BuildingImage buildingSprite1,
        final ServerPlayer owner, final ObjectID newID, final ServerModel mapEnv)
    {
        super.initParametrs(position, buildingSprite1, owner, newID, mapEnv);
        productionTimer = mapEnv.getObjectHolder().getProductionTimer();
        gameQueue = mapEnv.getQueue();
    }
    @Override
    public void activate()
    {
        super.activate();
        tryStartNext();
    }
    public void tryStartNext()
    {
        if(current != null)
        {
            return;
        }
        final ArrayList<ItemRecipe> readyList = new ArrayList<ItemRecipe>();
        for(final ItemRecipe rec : recipes)
        {
            if(canBeStarted(rec))
            {
                readyList.add(rec);
            }
        }
        if(!readyList.isEmpty())
        {
            current = readyList.get(rand.nextInt(readyList.size()));
            demanderPart.utilizeIngredients(current.getIngredients());
            final Production production = new Production(current);
            productionTimer.schedule(production, current.getProductionTime());
            getOwner().send(new ToClientProduction(getID(), current.getProductionTime()));
        }
    }
    private boolean canBeStarted(final ItemRecipe rec)
    {
        return supplierPart.hasPlaceFor(rec.getProducts())
            && demanderPart.hasIngredients(rec.getIngredients());
    }
    @Override
    public ItemDemander getDemanderPart()
    {
        return demanderPart;
    }
    @Override
    public ItemSupplier getSupplierPart()
    {
        return supplierPart;
    }

    private class Production extends TimerTask
    {
        private final ItemRecipe recipe;

        public Production(final ItemRecipe recipe)
        {
            this.recipe = recipe;
        }
        @Override
        public void run()
        {
            gameQueue.add(new Command()
            {
                @Override
                public void execute()
                {
                    for(final StaticItemSlot s : recipe.getProducts())
                    {
                        supplierPart.magicAddItem(s.getItemType(), s.getQuantity());
                    }
                    current = null;
                    tryStartNext();
                }
            });
        }
    }
}
