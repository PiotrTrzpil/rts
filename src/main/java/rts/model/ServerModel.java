package rts.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import rts.controller.Command;
import rts.controller.PlayerID;
import rts.controller.ServerGameQueue;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toClientIngame.ToClientGameEnd;
import rts.controller.server.ServerController;
import rts.controller.server.ServerPlayer;
import rts.misc.Coords;
import rts.model.map.BuildMap;
import rts.model.map.MapSettings;
import rts.model.map.PathGraph;
import rts.model.serverBuildings.Building;
import rts.model.serverBuildings.Storehouse;
import rts.model.timeline.TimeLine;
import rts.view.ImagesAndPatterns;

// TODO: Auto-generated Javadoc
/**
 * Model serwera. Tworzy wszystkie najwazniejsze moduly zarzadzajace gra.
 */
public class ServerModel
{
    /** The server controller. */
    private final ServerController serverController;
    /** Graf sciezek. */
    private final PathGraph pathGraph;
    /** Modul przechowujacy obrazki. */
    private final ImagesAndPatterns imageLoader;
    /** Modul tworzenia i przechowywania obiektow na mapie. */
    private final ServerObjectHolder objectHolder;
    /** Modul rozkazow dla jednostek. */
    private final CommandCenter commCenter;
    /** Lista graczy. */
    private final Map<PlayerID, ServerPlayer> players;
    /** Kolejka zdarzen serwera. */
    private final ServerGameQueue queue;
    /** The timeline. */
    private final TimeLine timeline;
    /** The game finished. */
    private boolean gameFinished;

    /**
     * Instantiates a new server model.
     * 
     * @param serverController the server controller
     * @param players the players
     * @param mapSettings the map settings
     */
    public ServerModel(final ServerController serverController,
        final Map<PlayerID, ServerPlayer> players, final MapSettings mapSettings)
    {
        this.serverController = serverController;
        queue = serverController.getQueue();
        imageLoader = serverController.getView().getImagesAndPatterns();
        // buildMap = new BuildMap(mapSettings);
        this.players = players;
        pathGraph = new PathGraph(new BuildMap(mapSettings));
        objectHolder = new ServerObjectHolder(this);
        timeline = new TimeLine(queue);
        commCenter = new CommandCenter(this);
    }
    /**
     * Wysyla do gracza zdarzenia poczatkowych zasobow.
     */
    public void sendPlayerResources()
    {
        // P.pr("RESS");
        for(final ServerPlayer player : players.values())
        {
            final Coords point = player.getStartPoint();
            final Storehouse storehouse = (Storehouse) objectHolder.createBuilding(
                BuildingType.STOREHOUSE, player, point, true);
            storehouse.addItem(Item.LOG, 5);
            storehouse.addItem(Item.PLANK, 5);
            objectHolder.createUnit(UnitType.WARRIOR, player, point.moveBy(-100, 0));
            objectHolder.createUnit(UnitType.WARRIOR, player, point.moveBy(-100, 30));
            objectHolder.createUnit(UnitType.ARCHER, player, point.moveBy(-120, 0));
            objectHolder.createUnit(UnitType.ARCHER, player, point.moveBy(-140, 30));
            objectHolder.createUnit(UnitType.CARRIER, player, point.moveBy(-120, 30));
            objectHolder.createUnit(UnitType.CARRIER, player, point.moveBy(-130, 30));
            objectHolder.createUnit(UnitType.CARRIER, player, point.moveBy(-130, 30));
            objectHolder.createUnit(UnitType.CARRIER, player, point.moveBy(-130, 30));
        }
    }
    /**
     * Usuwa gracza.
     * 
     * @param player the player
     */
    public void removePlayer(final ServerPlayer player)
    {
        player.setConnected(false);
        players.remove(player.getID());
        objectHolder.removePlayerObjects(player);
    }
    /**
     * Sprawdza czy instnieja warunki zakonczenia gry przez ktoregos z graczy.
     * 
     * @param serverPlayer gracz do sprawdzenia.
     */
    protected void checkForGameWinAndLose(final ServerPlayer serverPlayer)
    {
        if(!gameFinished)
        {
            final List<ServerPlayer> winnerList = new LinkedList<ServerPlayer>();
            final Set<Building> buildings = serverPlayer.getBuildings();
            if(buildings.isEmpty())
            {
                if(serverPlayer.isConnected())
                {
                    serverPlayer.send(new ToClientGameEnd(ToClientGameEnd.State.LOSE));
                }
            }
            for(final ServerPlayer player : players.values())
            {
                if(!player.getBuildings().isEmpty())
                {
                    winnerList.add(player);
                }
            }
            if(winnerList.size() == 1)
            {
                gameFinished = true;
                winnerList.get(0).send(new ToClientGameEnd(ToClientGameEnd.State.WIN));
            }
        }
    }
    /**
     * Gets the image loader.
     * 
     * @return the image loader
     */
    public ImagesAndPatterns getImageLoader()
    {
        return imageLoader;
    }
    /**
     * Gets the object holder.
     * 
     * @return the object holder
     */
    public ServerObjectHolder getObjectHolder()
    {
        return objectHolder;
    }
    /**
     * Gets the queue.
     * 
     * @return the queue
     */
    public ServerGameQueue getQueue()
    {
        return queue;
    }
    /**
     * Gets the comm center.
     * 
     * @return the comm center
     */
    public CommandCenter getCommCenter()
    {
        return commCenter;
    }
    /**
     * Gets the path graph.
     * 
     * @return the path graph
     */
    public PathGraph getPathGraph()
    {
        return pathGraph;
    }
    /**
     * Send to all.
     * 
     * @param message the message
     */
    public void sendToAll(final NetEvent message)
    {
        serverController.sendToAll(message);
    }
    /**
     * Gets the timeline.
     * 
     * @return the timeline
     */
    public TimeLine getTimeline()
    {
        return timeline;
    }

    /**
     * The Class Transport.
     */
    protected class Transport extends TimerTask
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
                    // final Collection<ServerPlayer> players =
                    // serverModel.getPlayers();
                    for(final ServerPlayer player : players.values())
                    {
                        final TransportTask task = player.getWorkerOvermind().carrierWork();
                        if(task != null)
                        {
                            commCenter.transport(task.getCarrier(), task.getSource(), task
                                    .getDestination(), task.getItem());
                        }
                    }
                }
            });
        }
    }
}
