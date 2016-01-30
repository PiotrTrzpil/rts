package rts.controller.server;

import java.net.BindException;
import java.util.Map;
import java.util.TreeMap;
import rts.controller.Command;
import rts.controller.PlayerID;
import rts.controller.ServerGameQueue;
import rts.controller.UserPlayer;
import rts.controller.client.ToClientGameSettings;
import rts.controller.client.ToClientGameStart;
import rts.controller.client.ToClientPlayerChange;
import rts.controller.client.ToClientGameSettings.NetResponse;
import rts.controller.connection.ClientSideConnectionOffline;
import rts.controller.connection.ServerSideConnection;
import rts.controller.connection.ServerSideConnectionOffline;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toClientIngame.ToClientMessage;
import rts.misc.P;
import rts.model.ServerModel;
import rts.model.map.MapSettings;
import rts.view.GameView;

// TODO: Auto-generated Javadoc
/**
 * Kontroler serwera.
 */
public class ServerController
{
    /** Model serwera. */
    private ServerModel serverModel;
    /**
     * Serwer obslugujacy zdarzenia sieciowe w fazie gry oczekiwania na
     * polaczenia.
     */
    private final GameServer gameServer;
    /** The view. */
    private final GameView gameView;
    /** Ustawienia mapy. */
    private final MapSettings mapSettings;
    /** The players map. */
    private final Map<PlayerID, ServerPlayer> playersMap;
    /** The user map. */
    private final Map<PlayerID, UserPlayer> userMap;
    /** Polaczenie lokalne hosta. */
    private final ServerSideConnectionOffline serverSideConnection;
    /** Polaczenie lokalne hosta. */
    private final ClientSideConnectionOffline clientSideConnection;
    /** Kolejka zdarzen serwera. */
    private final ServerGameQueue serverGameQueue;
    /** Bufor zdarzen przychodzacych przez siec. */
    private final ServerNetBuffer serverBuffer;
    /** The game state. */
    private State gameState;

    /**
     * Instantiates a new server controller.
     * 
     * @param view the view
     * @param mapSettings2 the map settings2
     * @param connection the connection
     * @throws BindException the bind exception
     * @throws Exception the exception
     */
    public ServerController(final GameView view, final MapSettings mapSettings2,
        final ClientSideConnectionOffline connection) throws BindException, Exception
    {
        gameState = new StateInitial();
        gameView = view;
        mapSettings = mapSettings2;
        clientSideConnection = connection;
        serverGameQueue = new ServerGameQueue();
        serverBuffer = new ServerNetBuffer();
        playersMap = new TreeMap<PlayerID, ServerPlayer>();
        userMap = new TreeMap<PlayerID, UserPlayer>();
        gameServer = new GameServer(this, serverBuffer);
        serverBuffer.setServer(gameServer);
        new Thread(gameServer).start();
        serverBuffer.start();
        serverGameQueue.start();
        serverSideConnection = new ServerSideConnectionOffline(serverBuffer, clientSideConnection);
        clientSideConnection.setServerSide(serverSideConnection);
        gameServer.addConnection(serverSideConnection);
    }
    /**
     * Inicjalizuje, tworzac gracza hosta.
     */
    public void initialize()
    {
        createPlayer("Host", serverSideConnection);
    }
    /**
     * Deactivate.
     */
    public void deactivate()
    {
        gameServer.stop();
        serverBuffer.stop();
        serverGameQueue.stop();
        clientSideConnection.setServerSide(null);
        gameView.loadMainMenu();
    }
    /**
     * (Tylko przez poczatkiem gry) Tworzy gracza i dodaje go do map.
     * 
     * @param name the name
     * @param connection the connection
     */
    private void createPlayer(final String name, final ServerSideConnection connection)
    {
        gameState.createPlayer(name, connection);
    }
    /**
     * (Tylko przez poczatkiem gry) Player joined.
     * 
     * @param playerName the player name
     * @param connection the connection
     */
    public void playerJoined(final String playerName, final ServerSideConnection connection)
    {
        gameState.playerJoined(playerName, connection);
    }
    /**
     * Gracz opuscil gre.
     * 
     * @param connection the connection
     */
    public void playerDisconnected(final ServerSideConnection connection)
    {
        gameState.playerDisconnected(connection);
    }
    /**
     * (Tylko przez poczatkiem gry) Gracz zostal usuniety z gry przemoca.
     * 
     * @param playerID the player id
     */
    public void playerKicked(final PlayerID playerID)
    {
        gameState.playerKicked(playerID);
    }
    /**
     * (Tylko przez poczatkiem gry) Start game.
     */
    public void startGame()
    {
        gameState.startGame();
    }
    /**
     * Send to all.
     * 
     * @param message the message
     */
    public void sendToAll(final NetEvent message)
    {
        gameServer.sendToAll(message);
    }
    /**
     * Gets the queue.
     * 
     * @return the queue
     */
    public ServerGameQueue getQueue()
    {
        return serverGameQueue;
    }
    /**
     * Gets the view.
     * 
     * @return the view
     */
    public GameView getView()
    {
        return gameView;
    }
    /**
     * Gets the model.
     * 
     * @return the model
     */
    public ServerModel getModel()
    {
        return serverModel;
    }

    /**
     * The Class State.
     */
    private abstract class State
    {
        /**
         * Player disconnected.
         * 
         * @param connection the connection
         */
        public abstract void playerDisconnected(final ServerSideConnection connection);
        /**
         * Player kicked.
         * 
         * @param playerID the player id
         */
        public void playerKicked(final PlayerID playerID)
        {
        }
        /**
         * Creates the player.
         * 
         * @param name the name
         * @param connection the connection
         */
        public void createPlayer(final String name, final ServerSideConnection connection)
        {
        }
        /**
         * Start game.
         */
        public void startGame()
        {
        }
        /**
         * Player joined.
         * 
         * @param playerName the player name
         * @param connection the connection
         */
        public void playerJoined(final String playerName, final ServerSideConnection connection)
        {
        }
    }

    /**
     * The Class StateIngame.
     */
    private class StateIngame extends State
    {
        /**
         * Gracz opuscil gre w jej trakcie.
         * 
         * @param connection the connection
         */
        @Override
        public void playerDisconnected(final ServerSideConnection connection)
        {
            gameServer.removeConnection(connection);
            final ServerPlayer player = connection.getPlayer();
            P.pr("Player " + player.getName() + " disconnected");
            gameServer.sendToAll(new ToClientMessage("Player " + player.getName()
                + " has left the game."));
            serverModel.removePlayer(player);
        }
    }

    /**
     * The Class StateInitial.
     */
    private class StateInitial extends State
    {
        /**
         * Gracz chce dolaczyc do gry. Wysylana jest odpowiedz negatywna gdy nie
         * ma miejsca, lub pozytywna w przeciwnym przypadku, razem z
         * ustawieniami mapy.
         * 
         * @param playerName nazwa gracza
         * @param connection polaczenie gracza
         */
        @Override
        public void playerJoined(final String playerName, final ServerSideConnection connection)
        {
            if(playersMap.size() < mapSettings.getNumberOfPlayers())
            {
                connection.send(new ToClientGameSettings(mapSettings, NetResponse.OK));
                for(final UserPlayer userPlayer : userMap.values())
                {
                    connection.send(new ToClientPlayerChange(userPlayer,
                            ToClientPlayerChange.State.JOINED));
                }
                createPlayer(playerName, connection);
            }
            else
            {
                final ToClientGameSettings gameSettings2 = new ToClientGameSettings(null,
                        NetResponse.NO);
                connection.send(gameSettings2);
                gameServer.removeConnection(connection);
            }
        }
        /**
         * Tworzy gracza i dodaje go do map.
         * 
         * @param name the name
         * @param connection the connection
         */
        @Override
        public void createPlayer(final String name, final ServerSideConnection connection)
        {
            final ServerPlayer serverPlayer = new ServerPlayer(PlayerID.newID(), name, connection,
                    mapSettings.getStartPlaces().get(playersMap.size()));
            // serverPlayer.setStartPlace();
            connection.setPlayer(serverPlayer);
            serverPlayer.setConnected(true);
            playersMap.put(serverPlayer.getID(), serverPlayer);
            final UserPlayer userPlayer = serverPlayer.createUserPlayer();
            userMap.put(serverPlayer.getID(), userPlayer);
            gameServer.sendToAll(new ToClientPlayerChange(userPlayer,
                    ToClientPlayerChange.State.JOINED));
        }
        /* (non-Javadoc)
         * @see rts.controller.server.ServerController.State#playerDisconnected(rts.controller.connection.ServerSideConnection)
         */
        @Override
        public void playerDisconnected(final ServerSideConnection connection)
        {
            gameServer.removeConnection(connection);
            final ServerPlayer serverPlayer = connection.getPlayer();
            if(serverPlayer != null)
            {
                serverPlayer.setConnected(false);
                playersMap.remove(serverPlayer.getID());
                final UserPlayer userPlayer = userMap.remove(serverPlayer.getID());
                gameServer.sendToAll(new ToClientPlayerChange(userPlayer,
                        ToClientPlayerChange.State.LEFT));
            }
        }
        /* (non-Javadoc)
         * @see rts.controller.server.ServerController.State#playerKicked(rts.controller.PlayerID)
         */
        @Override
        public void playerKicked(final PlayerID playerID)
        {
            serverGameQueue.add(new Command()
            {
                @Override
                public void execute()
                {
                    //                    P.pr("DDD");
                    final ServerPlayer serverPlayer = playersMap.get(playerID);
                    serverPlayer.send(new ToClientPlayerChange(null,
                            ToClientPlayerChange.State.YOU_WERE_KICKED));
                    gameServer.removeConnection(serverPlayer.getConnection());
                    // removePlayer(serverPlayer);
                }
            });
        }
        /**
         * Wylacza nasluchiwanie na polaczenia, zmienia serwer obslugujacy
         * zdarzenia, tworzy model gry i wysyla do graczy informacje o mapie i
         * graczach. Gre mozna uwazac za rozpoczata.
         */
        @Override
        public void startGame()
        {
            serverGameQueue.add(new Command()
            {
                @Override
                public void execute()
                {
                    gameState = new StateIngame();
                    gameServer.stopAcceptingConnections();
                    serverModel = new ServerModel(ServerController.this, playersMap, mapSettings);
                    for(final ServerPlayer player : playersMap.values())
                    {
                        player.send(new ToClientGameStart(userMap, userMap.get(player.getID()),
                                mapSettings));
                    }
                    serverModel.sendPlayerResources();
                }
            });
        }
    }
}
