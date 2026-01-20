package rts.model.serverBuildings;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import rts.controller.Command;
import rts.controller.ServerGameQueue;
import rts.controller.netEvents.toClientIngame.ToClientAddTraining;
import rts.controller.netEvents.toClientIngame.ToClientRemoveTraining;
import rts.controller.netEvents.toClientIngame.ToClientStartTraining;
import rts.controller.netEvents.toServerEvents.ToServerAddTraining;
import rts.controller.netEvents.toServerEvents.ToServerBuildingEvent;
import rts.controller.netEvents.toServerEvents.ToServerCancelTraining;
import rts.controller.server.ServerPlayer;
import rts.misc.Coords;
import rts.misc.exceptions.Exc;
import rts.model.Item;
import rts.model.ObjectID;
import rts.model.ServerModel;
import rts.model.ServerObjectHolder;
import rts.model.TrainID;
import rts.model.UnitType;
import rts.model.objectPatterns.BuildingPattern;
import rts.model.objectPatterns.UnitRecipe;
import rts.view.clientModel.BuildingImage;

// TODO: Auto-generated Javadoc
/**
 * Budynek tworzacy jednostki
 */
public class UnitTrainBuilding extends Building
{
    /** The demander part. */
    private final ItemDemander demanderPart;
    /** The unit recipes. */
    private final EnumMap<UnitType, UnitRecipe> unitRecipes;
    /** The production timer. */
    private Timer productionTimer;
    /** The game queue. */
    private ServerGameQueue gameQueue;
    /** The object holder. */
    private ServerObjectHolder objectHolder;
    /** The current. */
    private Training current;
    /** The unit queue. */
    private final List<Training> unitQueue;

    /**
     * Instantiates a new unit train building.
     * 
     * @param pattern the pattern
     */
    public UnitTrainBuilding(final BuildingPattern pattern)
    {
        super(pattern);
        unitRecipes = pattern.getUnitRecipes();
        unitQueue = new ArrayList<Training>();
        demanderPart = new ItemDemander(pattern.cloneIngredientSlots())
        {
            @Override
            public void putIngredient(final Item item)
            {
                super.putIngredient(item);
                tryStartNext();
            }
        };
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.Building#initParametrs(rts.misc.Coords, rts.view.clientModel.BuildingImage, rts.controller.server.ServerPlayer, rts.model.ObjectID, rts.model.ServerModel)
     */
    @Override
    public void initParametrs(final Coords position, final BuildingImage buildingSprite1,
        final ServerPlayer owner, final ObjectID newID, final ServerModel mapEnv1)
    {
        super.initParametrs(position, buildingSprite1, owner, newID, mapEnv1);
        gameQueue = mapEnv1.getQueue();
        objectHolder = mapEnv1.getObjectHolder();
        productionTimer = objectHolder.getProductionTimer();
    }
    /* (non-Javadoc)
     * @see rts.model.serverBuildings.Building#handleEvent(rts.controller.netEvents.NetBuildingEvent)
     */
    @Override
    public void handleEvent(final ToServerBuildingEvent buildingCommand)
    {
        if(buildingCommand instanceof ToServerAddTraining)
        {
            final ToServerAddTraining netNewTraining = (ToServerAddTraining) buildingCommand;
            addTrain(netNewTraining.getType());
        }
        else if(buildingCommand instanceof ToServerCancelTraining)
        {
            final ToServerCancelTraining netCancelTraining = (ToServerCancelTraining) buildingCommand;
            cancelTrain(netCancelTraining.getTrainID());
        }
    }
    /**
     * Dodaje trening
     * 
     * @param type the type
     */
    private void addTrain(final UnitType type)
    {
        final UnitRecipe unitRecipe = unitRecipes.get(type);
        if(unitRecipe == null)
        {
            throw new Exc("RECIPE NOT FOUND");
        }
        final TrainID newID = TrainID.newID();
        unitQueue.add(new Training(newID, unitRecipe));
        getOwner().send(new ToClientAddTraining(getID(), unitRecipe.getType(), newID));
        if(unitQueue.size() == 1)
        {
            tryStartNext();
        }
    }
    /**
     * Anuluje trening
     *
     * @param id the id
     */
    private void cancelTrain(final TrainID id)
    {
        getOwner().send(new ToClientRemoveTraining(getID(), id));
        if(current != null && current.id.equals(id))
        {
            current.cancel();
            current = null;
            tryStartNext();
            return;
        }
        final java.util.Iterator<Training> iterator = unitQueue.iterator();
        while(iterator.hasNext())
        {
            final Training train = iterator.next();
            if(train.id.equals(id))
            {
                iterator.remove();
                break;
            }
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
    /**
     * Try start next.
     */
    private void tryStartNext()
    {
        if(current == null && !unitQueue.isEmpty()
            && demanderPart.hasIngredients(unitQueue.get(0).recipe.getIngredients()))
        {
            current = unitQueue.remove(0);
            demanderPart.utilizeIngredients(current.recipe.getIngredients());
            getOwner().send(new ToClientStartTraining(getID(), current.id));
            productionTimer.schedule(current, current.recipe.getProductionTime());
        }
    }

    /**
     * Trening jednostki
     */
    private class Training extends TimerTask
    {
        /** The id. */
        private final TrainID id;
        /** The recipe. */
        private final UnitRecipe recipe;

        /**
         * Instantiates a new training.
         * 
         * @param id the id
         * @param recipe the recipe
         */
        public Training(final TrainID id, final UnitRecipe recipe)
        {
            super();
            this.id = id;
            this.recipe = recipe;
        }
        /* (non-Javadoc)
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run()
        {
            gameQueue.add(new Command()
            {
                @Override
                public void execute()
                {
                    cancelTrain(id);
                    mapEnv.getCommCenter().createUnitFromTraining(recipe.getType(),
                        UnitTrainBuilding.this);
                }
            });
        }
    }
}
