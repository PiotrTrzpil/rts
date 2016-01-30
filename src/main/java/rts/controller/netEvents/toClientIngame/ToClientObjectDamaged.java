package rts.controller.netEvents.toClientIngame;

import javax.swing.SwingUtilities;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.misc.exceptions.ObjectNotFoundException;
import rts.model.ObjectID;
import rts.view.clientModel.BuildingSprite;
import rts.view.clientModel.PlayerObjectSprite;
import rts.view.panels.BuildingInfoTab;

public class ToClientObjectDamaged implements ToClientEvent
{
    private final ObjectID objectID;
    private final int currentHealthPoints;

    public ToClientObjectDamaged(final ObjectID objectID, final int healthPoint)
    {
        this.objectID = objectID;
        currentHealthPoints = healthPoint;
    }
    public ObjectID getObjectID()
    {
        return objectID;
    }
    public int getHP()
    {
        return currentHealthPoints;
    }
    public void execute(final ClientController controller)
    {
        PlayerObjectSprite object;
        try
        {
            object = controller.getViewModel().getObjectHolder().getObject(objectID);
            object.setHP(currentHealthPoints);
            if(object instanceof BuildingSprite)
            {
                final BuildingSprite building = (BuildingSprite) object;
                final BuildingInfoTab infoTab = (BuildingInfoTab) building.getInfoTab();
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        infoTab.repaint();
                    }
                });
            }
        }
        catch(final ObjectNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
