package rts.controller.client;

import java.util.Map;
import rts.controller.PlayerID;
import rts.controller.UserPlayer;
import rts.model.map.MapSettings;

public class ToClientGameStart implements ToClientEvent
{
    private final Map<PlayerID, UserPlayer> players;
    private final UserPlayer player;
    private final MapSettings setting;

    public ToClientGameStart(final Map<PlayerID, UserPlayer> playerMap, final UserPlayer player,
        final MapSettings setting)
    {
        super();
        players = playerMap;
        this.player = player;
        this.setting = setting;
    }
    public Map<PlayerID, UserPlayer> getPlayers()
    {
        return players;
    }
    public UserPlayer getThisPlayer()
    {
        return player;
    }
    public MapSettings getSetting()
    {
        return setting;
    }
    @Override
    public void execute(final ClientController controller)
    {
        controller.startGame(this);
    }
}
