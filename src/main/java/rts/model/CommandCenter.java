package rts.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import rts.controller.Command;
import rts.controller.ServerGameQueue;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toClientIngame.ToClientObjectDamaged;
import rts.controller.netEvents.toClientIngame.ToClientUnitMove;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.misc.GameVector;
import rts.misc.Line;
import rts.misc.Velocity;
import rts.misc.exceptions.Exc;
import rts.model.ServerObjectHolder.CreateUnitEvent;
import rts.model.ingameEvents.ActivateUnitEvent;
import rts.model.ingameEvents.AddFreeCarrierEvent;
import rts.model.ingameEvents.ChangeMoveEvent;
import rts.model.ingameEvents.GameEvent;
import rts.model.ingameEvents.NullEvent;
import rts.model.ingameEvents.PutItemEvent;
import rts.model.ingameEvents.TakeItemEvent;
import rts.model.map.PathGraph;
import rts.model.serverBuildings.Building;
import rts.model.serverBuildings.BuildingControl;
import rts.model.serverBuildings.Building.ItemDemander;
import rts.model.serverBuildings.Building.ItemSupplier;
import rts.model.serverUnits.Carrier;
import rts.model.serverUnits.Unit;
import rts.model.timeline.EventSequence;
import rts.model.timeline.GameTime;
import rts.model.timeline.TimeLine;

// TODO: Auto-generated Javadoc
/**
 * Modul generujacy zdarzenia zwiazane z rozkazami jednostek.
 */
public class CommandCenter
{
    /** The follow distance. */
    private final int followDistance = 10;
    /** The path graph. */
    private final PathGraph pathGraph;
    /** The timeline. */
    private final TimeLine timeline;
    /** The timer. */
    private final Timer timer;
    /** The unit tasks. */
    private final Map<Unit, LinkedList<TimerTask>> unitTasks;
    // /** The server. */
    // private final GameServer server;
    /** The game queue. */
    private final ServerGameQueue gameQueue;
    /** The object holder. */
    private final ServerObjectHolder objectHolder;
    private final ServerModel serverModel;

    /**
     * Instantiates a new command center.
     * 
     * @param serverMapEnviroment the server map enviroment
     */
    public CommandCenter(final ServerModel serverMapEnviroment)
    {
        serverModel = serverMapEnviroment;
        objectHolder = serverMapEnviroment.getObjectHolder();
        // server = serverMapEnviroment.getGameServer();
        gameQueue = serverMapEnviroment.getQueue();
        pathGraph = serverMapEnviroment.getPathGraph();
        timeline = serverMapEnviroment.getTimeline();
        timer = new Timer();
        unitTasks = new HashMap<Unit, LinkedList<TimerTask>>();
    }
    /**
     * Adds the timer task.
     * 
     * @param unit the unit
     * @param task the task
     * @param delay the delay
     * @param period the period
     */
    private void addTimerTask(final Unit unit, final Task task, final long delay, final long period)
    {
        LinkedList<TimerTask> taskList = unitTasks.get(unit);
        if(taskList == null)
        {
            taskList = new LinkedList<TimerTask>();
            unitTasks.put(unit, taskList);
        }
        taskList.add(task);
        timer.schedule(task, delay, period);
    }
    /**
     * Wywolywana gdy jednostka ma byc stworzona w budynku.
     * 
     * @param type the type
     * @param building the building
     */
    public void createUnitFromTraining(final UnitType type, final Building building)
    {
        final EventSequence sequence = new EventSequence();
        final CreateUnitEvent createUnitEvent = objectHolder.new CreateUnitEvent(type, building
                .getOwner(), building.getEntrancePosition());
        sequence.add(createUnitEvent);
        final Unit unit = createUnitEvent.getUnit();
        sequence.add(exitBuilding(unit, building));
        sequence.add(new ActivateUnitEvent(unit));
        timeline.put(sequence);
    }
    /**
     * Enter building.
     * 
     * @param unit the unit
     * @param start the start
     * @param building the building
     * @return the event sequence
     */
    private EventSequence enterBuilding(final Unit unit, final Coords start,
        final BuildingControl building)
    {
        final Coords exit = building.getExitPosition();
        final Coords entrance = building.getEntrancePosition();
        final EventSequence move = move(unit, start, exit);
        if(move != null)
        {
            return move.add(straightMove(unit, exit, entrance));
        }
        return new EventSequence();
    }
    /**
     * Exit building.
     * 
     * @param unit the unit
     * @param building the building
     * @return the event sequence
     */
    private EventSequence exitBuilding(final Unit unit, final BuildingControl building)
    {
        return straightMove(unit, building.getEntrancePosition(), building.getExitPosition());
    }
    /**
     * Transport.
     * 
     * @param carrier the carrier
     * @param source the source
     * @param dest the dest
     * @param itemID the item id
     */
    public void transport(final Carrier carrier, final ItemSupplier source,
        final ItemDemander dest, final Item itemID)
    {
        carrier.getOwner().getWorkerOvermind().removeFreeCarrier(carrier);
        source.pendingTake(itemID);
        dest.pendingPut(itemID);
        final EventSequence sequence = new EventSequence();
        sequence.add(enterBuilding(carrier, carrier.getPosition(), source));
        sequence.add(new NullEvent(500));
        sequence.add(new TakeItemEvent(carrier, source, itemID));
        sequence.add(exitBuilding(carrier, source));
        sequence.add(enterBuilding(carrier, source.getExitPosition(), dest));
        sequence.add(new NullEvent(500));
        sequence.add(new PutItemEvent(carrier, dest));
        sequence.add(exitBuilding(carrier, dest));
        sequence.add(new AddFreeCarrierEvent(carrier));
        timeline.put(sequence);
    }
    /**
     * Update unit move.
     * 
     * @param unit the unit
     */
    private void updateUnitMove(final Unit unit)
    {
        final ChangeMoveEvent currentMove = unit.getCurrentMove();
        if(currentMove != null)
        {
            final long elapsed = System.currentTimeMillis() - currentMove.getExecutionTime();
            unit.setPosition(currentMove.getPosition().moveBy(
                currentMove.getVector().multiplyBy(elapsed)));
        }
    }
    /**
     * Obsluga rozkazu.
     * 
     * @param unit the unit
     * @param destination the destination
     */
    public void move(final Unit unit, final Coords destination)
    {
        updateUnitMove(unit);
        //
        final EventSequence sequence = move(unit, unit.getPosition(), destination);
        if(sequence != null)
        {
            timeline.put(sequence);
        }
    }
    /**
     * Move.
     * 
     * @param unit the unit
     * @param start the start
     * @param destination the destination
     * @return the event sequence
     */
    private EventSequence move(final Unit unit, final Coords start, final Coords destination)
    {
        // updateUnitMove(unit);
        final List<Coords> path = pathGraph.findPath(start, destination);
        if(path != null)
        {
            return createMoveEvents(unit, start, path);
        }
        return null;
    }
    /**
     * Straight move.
     * 
     * @param unit the unit
     * @param a the a
     * @param b the b
     * @return the event sequence
     */
    private EventSequence straightMove(final Unit unit, final Coords a, final Coords b)
    {
        final Coords start = a;
        final List<Coords> path = new LinkedList<Coords>();
        path.add(b);
        return createMoveEvents(unit, start, path);
    }
    /**
     * Creates the move events.
     * 
     * @param unit the unit
     * @param start the start
     * @param path the path
     * @return the event sequence
     */
    private EventSequence createMoveEvents(final AbstractUnit unit, final Coords start,
        final List<Coords> path)
    {
        final EventSequence sequence = new EventSequence();
        sequence.add(new SendEvent(new ToClientUnitMove(unit.getID(), start, path), 0));
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
            time += GameTime.moveTime(a, b, vel);
            a = b;
        }
        change = new ChangeMoveEvent(unit, a, time, new Velocity(0, 0));
        sequence.add(change);
        return sequence;
    }
    /**
     * Follow.
     * 
     * @param unit the unit
     * @param object the object
     */
    public void follow(final Unit unit, final Unit object)
    {
        // System.out.println("Follow");
        final FollowTask followTask = new FollowTask(unit, object, followDistance);
        addTimerTask(unit, followTask, 0, 600);
    }
    /**
     * Attack.
     * 
     * @param unit the unit
     * @param object the object
     */
    public void attack(final Unit unit, final PlayerObject object)
    {
        if(object instanceof Unit)
        {
            final Unit serverUnit = (Unit) object;
            final FollowTask followTask = new FollowTask(unit, serverUnit, unit.getAttackDistance());
            final AttackTask attackTask = new AttackTask(unit, serverUnit, followTask);
            addTimerTask(unit, followTask, 0, 600);
            addTimerTask(unit, attackTask, 400, 1000);
        }
        if(object instanceof Building)
        {
            final Building serverBuilding = (Building) object;
            updateUnitMove(unit);
            final Line line = new Line(serverBuilding.getBounds().getCenter(), unit.getPosition());
            move(unit, getBuildingEdgePoint(unit.getPosition(), serverBuilding.getBounds()).moveBy(
                line.getVector(10)));
            final AttackTask attackTask = new AttackTask(unit, serverBuilding, null);
            addTimerTask(unit, attackTask, 400, 1000);
        }
    }
    /**
     * Znajduje punkt na krawedzi budynku znajdujacy sie najblizej jednostki.
     * 
     * @param unitPosition polozenie jednostki
     * @param buildingBounds obramowanie budynku
     * @return punkt na krawedzi budynku
     */
    private Coords getBuildingEdgePoint(final Coords unitPosition, final ConstRect buildingBounds)
    {
        final Coords fromCenterIntersectionPoint = buildingBounds
                .getFromCenterIntersectionPoint(unitPosition);
        if(fromCenterIntersectionPoint == null)
        {
            throw new Exc("Blad znalezienia punktu.");
        }
        return fromCenterIntersectionPoint;
    }
    /**
     * Znajduje pozycje bedaca celem jednostki podazajacej za obiektem.
     * 
     * @param unit the unit
     * @param object the object
     * @return the follow position
     */
    private Coords getFollowPosition(final Unit unit, final PlayerObject object, final int distance)
    {
        final Coords objectPos = object.getPosition();
        final GameVector vect = new Line(objectPos, unit.getPosition()).getVector(distance);
        return objectPos.moveBy(vect);
    }
    /**
     * Usuwa zdarzenia i rozkazy zwiazane z jednostka.
     * 
     * @param unit dana jednostka
     */
    public void clearCommands(final Unit unit)
    {
        LinkedList<TimerTask> taskList;;
        if((taskList = unitTasks.get(unit)) != null)
        {
            for(final TimerTask task : taskList)
            {
                task.cancel();
            }
            taskList.clear();
        }
        timeline.clearEvents(unit);
    }
    /**
     * Cancel task.
     * 
     * @param task the task
     */
    private void cancelTask(final Task task)
    {
        task.cancel();
        final LinkedList<TimerTask> linkedList = unitTasks.get(task.unit);
        if(linkedList != null)
        {
            linkedList.remove(task);
            if(linkedList.isEmpty())
            {
                unitTasks.remove(task.unit);
            }
        }
    }

    /**
     * The Class SendEvent.
     */
    private class SendEvent extends GameEvent
    {
        /** The event. */
        private final NetEvent event;

        /**
         * Instantiates a new send event.
         * 
         * @param event the event
         * @param time the time
         */
        public SendEvent(final NetEvent event, final long time)
        {
            super(time);
            this.event = event;
        }
        /*
         * (non-Javadoc)
         *
         * @see rts.controller.ingameEvents.GameEvent#execute()
         */
        @Override
        public void execute()
        {
            serverModel.sendToAll(event);
        }
    }

    /**
     * The Class Task.
     */
    private abstract class Task extends TimerTask
    {
        /** The unit. */
        protected final Unit unit;

        /**
         * Instantiates a new task.
         * 
         * @param unit the unit
         */
        public Task(final Unit unit)
        {
            this.unit = unit;
        }
        /*
         * (non-Javadoc)
         *
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
                    Task.this.execute();
                }
            });
        }
        /**
         * Execute.
         */
        public abstract void execute();
    }

    /**
     * The Class FollowTask.
     */
    private class FollowTask extends Task
    {
        /** The target. */
        private final Unit target;
        /** The distance. */
        private final int distance;

        /**
         * Instantiates a new follow task.
         * 
         * @param unit the unit
         * @param target the target
         * @param distance the distance
         */
        public FollowTask(final Unit unit, final Unit target, final int distance)
        {
            super(unit);
            this.target = target;
            this.distance = distance;
        }
        /*
         * (non-Javadoc)
         *
         * @see rts.controller.server.CommandCenter.Task#execute()
         */
        @Override
        public void execute()
        {
            // P.pr("DD");
            // if(unit.distance(object) > distance)
            // {
            updateUnitMove(target);
            timeline.clearEvents(unit);
            final double distance2 = unit.distance(target);
            move(unit, getFollowPosition(unit, target, (int) Math.min(distance, distance2)));
            // }
        }
    }

    /**
     * The Class AttackTask.
     */
    private class AttackTask extends Task
    {
        /** The object. */
        private final PlayerObject object;
        /** The follow task. */
        private final FollowTask followTask;

        /**
         * Instantiates a new attack task.
         * 
         * @param unit the unit
         * @param object the object
         * @param followTask the follow task
         */
        public AttackTask(final Unit unit, final PlayerObject object, final FollowTask followTask)
        {
            super(unit);
            this.object = object;
            this.followTask = followTask;
        }
        /*
         * (non-Javadoc)
         *
         * @see rts.controller.server.CommandCenter.Task#execute()
         */
        @Override
        public void execute()
        {
            if(!object.isAlive())
            {
                cancelTask(this);
                if(followTask != null)
                {
                    cancelTask(followTask);
                    // followTask.cancel();
                }
                return;
            }
            if(object.distance(unit) < unit.getAttackDistance())
            {
                object.beHit(unit.getDamage());
                serverModel.sendToAll(new ToClientObjectDamaged(object.getID(), object.getHP()));
                if(!object.isAlive())
                {
                    objectHolder.remove(object);
                }
            }
        }
    }
}
