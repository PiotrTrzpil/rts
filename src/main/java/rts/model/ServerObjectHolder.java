package rts.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;
import rts.controller.netEvents.toClientIngame.ToClientCreateBuilding;
import rts.controller.netEvents.toClientIngame.ToClientCreateUnit;
import rts.controller.netEvents.toClientIngame.ToClientRemoveObject;
import rts.controller.server.ServerPlayer;
import rts.misc.Coords;
import rts.misc.P;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.ingameEvents.ActivateUnitEvent;
import rts.model.ingameEvents.GameEvent;
import rts.model.map.BuildMap;
import rts.model.map.PathGraph;
import rts.model.objectPatterns.BuildingPattern;
import rts.model.serverBuildings.Building;
import rts.model.serverUnits.Unit;
import rts.model.timeline.EventSequence;
import rts.view.clientModel.BuildingImage;

// TODO: Auto-generated Javadoc
/**
 * Klasa zarzadzajaca istnieniem obiektow po stronie serwera.
 */
public class ServerObjectHolder
{
    /** Mapa obiektow. */
    private final Map<ObjectID, PlayerObject> objects;
    //    private final Map<PlayerID, PlayerObject> playerBuldings;
    /** The server model. */
    private final ServerModel serverModel;
    /** The production timer. */
    private final Timer productionTimer;
    private final PathGraph pathGraph;

    /**
     * Instantiates a new server object holder.
     * 
     * @param model model
     */
    public ServerObjectHolder(final ServerModel model)
    {
        serverModel = model;
        pathGraph = serverModel.getPathGraph();
        //        buildingCreator = buildingCreator2;
        objects = new TreeMap<ObjectID, PlayerObject>();
        productionTimer = new Timer();
        productionTimer.schedule(serverModel.new Transport(), 1000, 500);
    }
    /**
     * Zwraca obiekt z mapy.
     * 
     * @param id id obiektu
     * @return obiekt
     * @throws ObjectNotFoundException
     */
    public PlayerObject getObject(final ObjectID id) throws ObjectNotFoundException
    {
        final PlayerObject serverOwnableObject = objects.get(id);
        if(serverOwnableObject == null)
        {
            throw new ObjectNotFoundException(id.toString());
        }
        return serverOwnableObject;
    }
    public void removePlayerObjects(final ServerPlayer player)
    {
        final HashSet<Building> bClone = (HashSet<Building>) player.getBuildings().clone();
        final HashSet<Unit> uClone = (HashSet<Unit>) player.getUnits().clone();
        for(final Building building : bClone)
        {
            remove(building);
        }
        for(final Unit unit : uClone)
        {
            remove(unit);
        }
    }
    /**
     * Tworzy nowa jednostke.
     * 
     * @param type klasa okreslajaca rodzaj jednostki.
     * @param player gracz majacy stac sie posiadaczem jednostki.
     * @param position polozenie na mapie
     */
    public void createUnit(final UnitType type, final ServerPlayer player, final Coords position)
    {
        final EventSequence sequence = new EventSequence();
        final CreateUnitEvent createUnitEvent = new CreateUnitEvent(type, player, position);
        final Unit unit = createUnitEvent.getUnit();
        sequence.add(createUnitEvent);
        sequence.add(new ActivateUnitEvent(unit));
        serverModel.getTimeline().put(sequence);
    }

    /**
     * Zdarzenie stworzenia jednostki
     */
    public class CreateUnitEvent extends GameEvent
    {
        /** The unit. */
        private Unit unit;

        /**
         * Instantiates a new creates the unit event.
         * 
         * @param type klasa okreslajaca rodzaj jednostki.
         * @param player gracz majacy stac sie posiadaczem jednostki.
         * @param position polozenie na mapie
         */
        public CreateUnitEvent(final UnitType type, final ServerPlayer player, final Coords position)
        {
            super(0);
            try
            {
                unit = (Unit) type.getUnitClass().getConstructor().newInstance();
                unit.initParametrs(type, position, player, ObjectID.newID(), serverModel);
            }
            catch(final Exception e)
            {
                e.printStackTrace();
            }
        }
        /*
         * (non-Javadoc)
         *
         * @see rts.controller.ingameEvents.GameEvent#execute()
         */
        @Override
        public void execute()
        {
            objects.put(unit.getID(), unit);
            unit.getOwner().getUnits().add(unit);
            serverModel.sendToAll(new ToClientCreateUnit(unit));
        }
        /**
         * Gets the unit.
         * 
         * @return the unit
         */
        public Unit getUnit()
        {
            return unit;
        }
    }

    /**
     * Tworzy nowy budynek.
     * 
     * @param owner gracz majacy stac sie posiadaczem budynku.
     * @param completed czy budynek ma byc od razu ukonczony.
     * @param type the type
     * @param position the position
     * @return stworzony budynek.
     */
    public Building createBuilding(final BuildingType type, final ServerPlayer owner,
        final Coords position, final boolean completed)
    {
        final BuildingPattern pattern = serverModel.getImageLoader().getBuildingPattern(type);
        final BuildingImage buildingSprite = pattern.getSprite();
        Building building = null;
        if(pathGraph.canBeBuild(buildingSprite, position))
        {
            try
            {
                building = type.getBuildingClass().getDeclaredConstructor(BuildingPattern.class)
                        .newInstance(pattern);
            }
            catch(final Exception e)
            {
                P.er("Building creation error.");
                e.printStackTrace();
                return null;
            }
            final ObjectID newID = ObjectID.newID();
            building.initParametrs(BuildMap.roundToTile(position), buildingSprite, owner, newID,
                serverModel);
            objects.put(newID, building);
            owner.getBuildings().add(building);
            pathGraph.insertBuilding(building);
            // final Set<PathNode> nodes =
            // serverMapEnviroment.getPathGraph().getNodes();
            // final Set<PathNode> newNodes = nodes;//(TreeSet<PathNode>)
            // nodes.clone();
            // final NetPathNodes netPathNodes = new NetPathNodes(newNodes);
            // serverMapEnviroment.getGameServer().sendToAll(netPathNodes);
            // P.pr("C " + newID);
            serverModel.sendToAll(new ToClientCreateBuilding(building));
            building.activate();
        }
        return building;
    }
    /**
     * Usuwa obiekt.
     * 
     * @param object obiekt do usuniecia.
     */
    public void remove(final PlayerObject object)
    {
        if(object instanceof Building)
        {
            final Building building = (Building) object;
            building.getOwner().getBuildings().remove(building);
            pathGraph.removeBuilding(building);
            serverModel.checkForGameWinAndLose(object.getOwner());
        }
        else if(object instanceof Unit)
        {
            final Unit serverUnit = (Unit) object;
            serverUnit.getOwner().getUnits().remove(serverUnit);
        }
        object.deactivate();
        objects.remove(object.getID());
        serverModel.sendToAll(new ToClientRemoveObject(object.getID()));
    }
    /**
     * Gets the production timer.
     * 
     * @return the production timer
     */
    public Timer getProductionTimer()
    {
        return productionTimer;
    }
}
