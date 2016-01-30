package rts.controller.netEvents.toClientIngame;

import javax.swing.SwingUtilities;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.ObjectID;
import rts.view.clientModel.BuildingSprite;
import rts.view.panels.BuildingInfoTab;

public class ToClientBuildingEvent implements ToClientEvent
{
    private final ObjectID buildingID;

    public ToClientBuildingEvent(final ObjectID id)
    {
        buildingID = id;
    }
    public ObjectID getBuildingID()
    {
        return buildingID;
    }
    @Override
    public void execute(final ClientController controller)
    {
        BuildingSprite building;
        try
        {
            building = (BuildingSprite) controller.getViewModel().getObjectHolder().getObject(
                buildingID);
            final BuildingInfoTab infoTab = (BuildingInfoTab) building.getInfoTab();
            SwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    infoTab.handleBuildingEvent(ToClientBuildingEvent.this);
                }
            });
        }
        catch(final ObjectNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
