package rts.view.clientModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import rts.controller.Command;
import rts.controller.GameEventQueue;
import rts.controller.PlayerID;
import rts.controller.UserPlayer;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.Line;
import rts.misc.Velocity;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.AbstractUnit;
import rts.model.BuildingType;
import rts.model.ObjectID;
import rts.model.UnitType;
import rts.model.ingameEvents.ChangeMoveEvent;
import rts.model.map.PathNode;
import rts.model.objectPatterns.BuildingPattern;
import rts.model.timeline.EventSequence;
import rts.model.timeline.GameTime;
import rts.model.timeline.TimeLine;
import rts.view.ViewModel;
import rts.view.clientModel.misc.ObjectFlag;
import rts.view.clientModel.misc.RelationFlag;
import rts.view.clientModel.misc.Selectable;
import rts.view.mapView.ColorShape;
import rts.view.mapView.ControlModeUnitGroup;
import rts.view.mapView.Drawable;
import rts.view.mapView.DrawableList;
import rts.view.mapView.DrawableSprite;
import rts.view.mapView.GameGraphics;
import rts.view.panels.LeftPanel;
import rts.view.panels.Tab;

// TODO: Auto-generated Javadoc
/**
 * Zarzadca obiektow po stronie klienta.
 */
public class ObjectHolder
{
    /** Zaznaczone obiekt(y). */
    private Selectable selection;
    /** Mapa obiektow. */
    private final Map<ObjectID, PlayerObjectSprite> objects;
    /** The queue. */
    private final GameEventQueue queue;
    /** The user. */
    private final UserPlayer user;
    /** The unit group controller. */
    private final UnitGroupController unitGroupController;
    /** The left panel. */
    private final LeftPanel leftPanel;
    /** Model widoku. */
    private final ViewModel viewModel;
    /** The chooser. */
    private final CommandChooser commandChooser;
    /** The buildings. */
    private final List<BuildingSprite> buildings;
    /** The units. */
    private final List<UnitSprite> units;
    /** The nodes. */
    private Set<PathNode> nodes;
    /** The player map. */
    private final Map<PlayerID, UserPlayer> playerMap;
    /** The pointer position. */
    private Coords pointerPosition;
    /** The timeline. */
    private final TimeLine timeline;
    /** The timer. */
    private final Timer drawTimer;
    /** The draw task. */
    private DrawTask drawTask;

    /**
     * Instantiates a new object holder.
     * 
     * @param model model widoku
     * @param user gracz - wlasciciel
     * @param playerMap wszyscy gracze
     */
    public ObjectHolder(final ViewModel model, final UserPlayer user,
        final Map<PlayerID, UserPlayer> playerMap)
    {
        viewModel = model;
        queue = model.getQueue();
        this.user = user;
        this.playerMap = playerMap;
        leftPanel = model.getLeftPanel();
        objects = new TreeMap<ObjectID, PlayerObjectSprite>();
        drawTimer = new Timer();
        commandChooser = new CommandChooser(model);
        unitGroupController = new UnitGroupController(user, model.getMapView(), this,
                commandChooser);
        buildings = new ArrayList<BuildingSprite>();
        units = new ArrayList<UnitSprite>();
        timeline = new TimeLine(queue);
    }
    /**
     * Aktywuje rysowanie
     */
    public void activate()
    {
        drawTask = new DrawTask();
        drawTimer.schedule(drawTask, 0, 10);
    }
    /**
     * Dezaktywuje rysowanie
     */
    public void deactivate()
    {
        drawTask.cancel();
    }
    /**
     * Porusza jednostke
     * 
     * @param unitID the unit id
     * @param start the start
     * @param findPath the find path
     */
    public void move(final ObjectID unitID, final Coords start, final List<Coords> findPath)
    {
        try
        {
            UnitSprite unit;
            unit = (UnitSprite) getObject(unitID);
            timeline.clearEvents(unit);
            final EventSequence sequence = new EventSequence();
            createMoveEvents(unit, start, findPath, sequence);
            timeline.put(sequence);
        }
        catch(final ObjectNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Tworzy zdarzenia ruchu
     * 
     * @param unit the unit
     * @param start the start
     * @param path the path
     * @param sequence the sequence
     */
    private void createMoveEvents(final AbstractUnit unit, final Coords start,
        final List<Coords> path, final EventSequence sequence)
    {
        final float vel = unit.getMaxVelocity();
        ChangeMoveEvent change;
        Coords a = start;
        long time = 0;
        for(final Coords b : path)
        {
            final Line l = new Line(a, b);
            final Velocity velocity = new Velocity(l.getVector(vel));
            change = new ChangeMoveEvent(unit, a, time, velocity);
            sequence.add(change);
            time = GameTime.moveTime(a, b, vel);
            a = b;
        }
        change = new ChangeMoveEvent(unit, a, time, new Velocity(0, 0));
        sequence.add(change);
    }
    /**
     * Pobiera obiekty po czym z nich obrazki do narysowania.
     * 
     * @param visibleArea widoczny obszar mapy
     * @return the sprites to draw
     */
    public List<DrawableSprite> getSpritesToDraw(final ConstRect visibleArea)
    {
        final List<PlayerObjectSprite> visibleObjects = getVisibleObjects(visibleArea);
        final List<DrawableSprite> list = new LinkedList<DrawableSprite>();
        for(final PlayerObjectSprite object : visibleObjects)
        {
            list.add(object.getDrawableSprite());
        }
        return list;
    }
    /**
     * Pobiera obiekty zawarte w obszarze mapy.
     * 
     * @param visibleArea obszar mapy
     * @return the visible objects
     */
    public List<PlayerObjectSprite> getVisibleObjects(final ConstRect visibleArea)
    {
        final List<PlayerObjectSprite> list = new LinkedList<PlayerObjectSprite>();
        final long currentTime = System.currentTimeMillis();
        for(final UnitSprite unit : units)
        {
            unit.updatePosition(currentTime);
            if(unit.isVisible() && visibleArea.intersects(unit.getBounds()))
            {
                list.add(unit);
            }
        }
        for(final BuildingSprite building : buildings)
        {
            if(visibleArea.intersects(building.getBounds()))
            {
                list.add(building);
            }
        }
        return list;
    }
    /**
     * Pobiera paski zdrowia.
     * 
     * @param visibleArea obszar mapy
     * @return the health bars
     */
    public Drawable getHealthBars(final ConstRect visibleArea)
    {
        final DrawableList list = new DrawableList();
        for(final PlayerObjectSprite object : getVisibleObjects(visibleArea))
        {
            final ConstRect fullbar = new ConstRect(0, 0, 20, 3).centerIn(object.getBounds())
                    .moveBy(0, -10);
            final ConstRect hp = fullbar.setWidth(20 * object.getHP() / object.getMaxHP());
            list.add(new Drawable()
            {
                @Override
                public void draw(final GameGraphics g)
                {
                    g.setColor(Color.GREEN);
                    g.setFilled(false);
                    fullbar.draw(g);
                    g.setFilled(true);
                    hp.draw(g);
                }
            });
        }
        return list;
    }
    /**
     * Gets the object bounds.
     * 
     * @return the object bounds
     */
    public Drawable getObjectUnderCursorBounds()
    {
        final DrawableList list = new DrawableList();
        // final ObjectHolder holder = mapEnviroment.getObjectHolder();
        final PlayerObjectSprite objectUnderCursor = getObjectUnderCursor();
        if(objectUnderCursor != null)
        {
            list.add(new ColorShape(Color.blue, objectUnderCursor.getBounds()));
        }
        return list;
    }
    /**
     * Wydobywa obiekty pod danym prostokatem na mapie i/lub filtruje je wedlug
     * zadanych parametrow.
     * 
     * @param objectList lista obiektow do filtracji. Jesli jest null, to
     *            pobiera obiekty z mapy
     * @param relations zbior poszukiwanych rodzajow obiektow, jesli jest null,
     *            to dowolne obiekty.
     * @param flags zbior poszukiwanych relacji w stosunku do tego gracza, ktore
     *            maja spelniac obiekty. Jesli jest null, to dowolne obiekty.
     * @param rect prostokat na mapie, pobiera spod niego obiekty. Jesli jest
     *            null, to dziala na liscie.
     * @return przefiltrowana lista obiektow.
     */
    public List<? extends PlayerObjectSprite> extractObjects(List<PlayerObjectSprite> objectList,
        final RelationFlag.Set relations, final ObjectFlag.Set flags, final ConstRect rect)
    {
        if(objectList == null)
        {
            objectList = new LinkedList<PlayerObjectSprite>();
            objectList.addAll(units);
            objectList.addAll(buildings);
        }
        final List<PlayerObjectSprite> returnList = new LinkedList<PlayerObjectSprite>();
        for(final PlayerObjectSprite object : objectList)
        {
            if((relations == null || relations.equalsRelation(object, user))
                && (flags == null || flags.matchesObject(object))
                && (rect == null || rect.intersects(object.getBounds())))
            {
                returnList.add(object);
            }
        }
        objectList.removeAll(returnList);
        return returnList;
    }
    /**
     * Usuwa obiekt z mapy.
     * 
     * @param id identyfikator obiektu
     * @throws ObjectNotFoundException the object not found exception
     */
    public void remove(final ObjectID id) throws ObjectNotFoundException
    {
        final PlayerObjectSprite object = objects.remove(id);
        if(object == null)
        {
            throw new ObjectNotFoundException(id.toString());
            //            return;
        }
        if(object instanceof UnitSprite)
        {
            final UnitSprite unit = (UnitSprite) object;
            units.remove(unit);
            if(object instanceof ControllableUnit)
            {
                final ControllableUnit controllableUnit = (ControllableUnit) object;
                unitGroupController.unlinkFromAll(controllableUnit);
            }
        }
        else if(object instanceof BuildingSprite)
        {
            final BuildingSprite building = (BuildingSprite) object;
            buildings.remove(building);
            viewModel.getLeftPanel().getMinimap().removeObject(building.getBounds());
        }
        if(selection == object)
        {
            deselect();
        }
    }
    /**
     * Pobiera jednostki spod prostokata i wykonuje metode w
     * unitGroupController.
     * 
     * @param rect the rect
     */
    public void addOrRemove(final ConstRect rect)
    {
        if(!(selection instanceof UnitGroup))
        {
            select(rect);
        }
        else
        {
            final List<ControllableUnit> list = (List<ControllableUnit>) extractObjects(null,
                new RelationFlag.Set(EnumSet.of(RelationFlag.FRIENDLY)), new ObjectFlag.Set(EnumSet
                        .of(ObjectFlag.SOLDIER)), rect);
            if(list.isEmpty())
            {
                return;
            }
            unitGroupController.addOrRemove(list);
        }
    }
    /**
     * Zwraca obiekt pod kursorem lub null jesli takiego nie ma.
     * 
     * @return the object under cursor
     */
    private PlayerObjectSprite getObjectUnderCursor()
    {
        if(getPointerPosition() == null)
        {
            return null;
        }
        final List<? extends PlayerObjectSprite> obj = extractObjects(null, null, null,
            new ConstRect(getPointerPosition()));
        return obj.isEmpty() ? null : obj.get(0);
    }
    /**
     * Dezaktywuje aktualne zaznaczenie.
     */
    public void deselect()
    {
        unitGroupController.deselect();
        selection = null;
        leftPanel.loadNewInfoTab(leftPanel.getInfoTab());
    }
    /**
     * Zaznacza obiekty nalezace do gracza.
     * 
     * @param rect the rect
     * @return zaznaczony obiekt.
     */
    @SuppressWarnings("unchecked")
    public Selectable select(final ConstRect rect)
    {
        selection = null;
        // mapView.setControlMode(new ControlModeNormal());
        final List<? extends PlayerObjectSprite> obj = extractObjects(null, new RelationFlag.Set(
                EnumSet.of(RelationFlag.FRIENDLY)), new ObjectFlag.Set(EnumSet.of(ObjectFlag.ALL)),
            rect);
        selection = unitGroupSelected((List<PlayerObjectSprite>) obj);
        if(selection == null)
        {
            selection = unitSelected((List<PlayerObjectSprite>) obj);
        }
        if(selection == null)
        {
            selection = buildingSelected((List<PlayerObjectSprite>) obj);
        }
        if(selection != null)
        {
            leftPanel.loadNewInfoTab(selection.getInfoTab());
        }
        else
        {
            deselect();
        }
        leftPanel.changeTabTo(Tab.INFO);
        return selection;
    }
    /**
     * Probuje zaznaczyc grupe jednostek jesli jednostki sa z tym kompatybilne.
     * 
     * @param obj the obj
     * @return the selectable
     */
    @SuppressWarnings("unchecked")
    private Selectable unitGroupSelected(final List<PlayerObjectSprite> obj)
    {
        final List<ControllableUnit> list = (List<ControllableUnit>) extractObjects(obj, null,
            new ObjectFlag.Set(EnumSet.of(ObjectFlag.SOLDIER)), null);
        final UnitGroup group = unitGroupController.select(list);
        if(group != null)
        {
            viewModel.getMapView().setControlMode(new ControlModeUnitGroup(unitGroupController));
        }
        return group;
    }
    /**
     * Probuje zaznaczyc jedna jednostke ( nie-zolnienerza).
     * 
     * @param obj the obj
     * @return the selectable
     */
    @SuppressWarnings("unchecked")
    private Selectable unitSelected(final List<PlayerObjectSprite> obj)
    {
        final List<UnitSprite> list = (List<UnitSprite>) extractObjects(obj, null,
            new ObjectFlag.Set(EnumSet.of(ObjectFlag.CIVIL)), null);
        UnitSprite unit = null;
        if(!list.isEmpty())
        {
            unit = (UnitSprite) list.get(0);
        }
        return unit;
    }
    /**
     * Probuje zaznaczyc budynek.
     * 
     * @param objects the objects
     * @return the ownable object
     */
    @SuppressWarnings("unchecked")
    private PlayerObjectSprite buildingSelected(final List<PlayerObjectSprite> objects)
    {
        final List<BuildingSprite> list = (List<BuildingSprite>) extractObjects(objects, null,
            new ObjectFlag.Set(EnumSet.of(ObjectFlag.BUILDING)), null);
        BuildingSprite building = null;
        if(!list.isEmpty())
        {
            building = (BuildingSprite) list.get(0);
        }
        return building;
    }
    /**
     * Tworzy jednostke (reprezentacje od strony uzytkownika).
     * 
     * @param type the type
     * @param objectID the object id
     * @param playerID the player id
     * @param position the position
     */
    public void createUnit(final UnitType type, final ObjectID objectID, final PlayerID playerID,
        final Coords position)
    {
        final ObjectImage unitImage = viewModel.getImageLoader().getUnitSprite(type);
        final UserPlayer player = playerMap.get(playerID);
        final ControllableUnit sprite = new ControllableUnit(viewModel, type, unitImage, position,
                objectID, player);
        objects.put(objectID, sprite);
        units.add(sprite);
    }
    /**
     * Tworzy budynek (reprezentacje od strony uzytkownika).
     * 
     * @param type the type
     * @param objectID the object id
     * @param playerID the player id
     * @param position the position
     */
    public void createBuilding(final BuildingType type, final ObjectID objectID,
        final PlayerID playerID, final Coords position)
    {
        // viewModel.getImageLoader().getBuildingSprite(type);
        final BuildingPattern buildingPattern = viewModel.getImageLoader().getBuildingPattern(type);
        final BuildingImage buildingSprite = buildingPattern.getSprite();
        final UserPlayer player = playerMap.get(playerID);
        final BuildingSprite building = new BuildingSprite(buildingSprite, buildingPattern,
                position, objectID, player);
        objects.put(objectID, building);
        buildings.add(building);
        building.setInfoTab(type.createInfoTab(viewModel, building));
        viewModel.getLeftPanel().getMinimap().addObject(building.getBounds());
    }
    /**
     * Zwraca animacje zaznaczenia jednostek.
     * 
     * @return the selections
     */
    public Drawable getSelections()
    {
        final DrawableList list = new DrawableList();
        for(final UnitSprite unit : units)
        {
            list.add(unit.getSelection());
        }
        return list;
    }
    /**
     * Sets the path nodes.
     * 
     * @param nodes2 the new path nodes
     */
    public void setPathNodes(final Set<PathNode> nodes2)
    {
        nodes = nodes2;
    }
    /**
     * Gets the path nodes.
     * 
     * @return the path nodes
     */
    public Set<PathNode> getPathNodes()
    {
        return nodes;
    }
    /**
     * Zwraca obiekt z mapy.
     * 
     * @param id id obiektu
     * @return the object
     * @throws ObjectNotFoundException the object not found exception
     */
    public PlayerObjectSprite getObject(final ObjectID id) throws ObjectNotFoundException
    {
        final PlayerObjectSprite ownableObject = objects.get(id);
        if(ownableObject == null)
        {
            throw new ObjectNotFoundException(id.toString());
        }
        return ownableObject;
    }
    /**
     * Sets the pointer position.
     * 
     * @param pointerPosition the new pointer position
     */
    public void setPointerPosition(final Coords pointerPosition)
    {
        this.pointerPosition = pointerPosition;
    }
    /**
     * Gets the pointer position.
     * 
     * @return the pointer position
     */
    public Coords getPointerPosition()
    {
        return pointerPosition;
    }

    /**
     * Zadanie rysowania ekranu.
     */
    private class DrawTask extends TimerTask
    {
        /*
         * (non-Javadoc)
         *
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run()
        {
            queue.add(new Command()
            {
                @Override
                public void execute()
                {
                    viewModel.getMapView().drawAll();
                }
            });
        }
    }
}
