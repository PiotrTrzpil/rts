package rts.controller.client;

import rts.controller.netEvents.NetEvent;
import rts.model.map.MapSettings;

public class ToClientGameSettings implements ToClientEvent
{
    private final NetResponse status;
    private final MapSettings mapSettings;

    public ToClientGameSettings(final MapSettings mapSettings, final NetResponse status)
    {
        this.mapSettings = mapSettings;
        this.status = status;
    }
    public boolean isOk()
    {
        return status == NetResponse.OK;
    }
    public MapSettings getMapSettings()
    {
        return mapSettings;
    }
    @Override
    public void execute(final ClientController controller)
    {
        controller.joinGameResponse(this);
    }

    public enum NetResponse implements NetEvent
    {
        OK,
        NO;
    }
}
