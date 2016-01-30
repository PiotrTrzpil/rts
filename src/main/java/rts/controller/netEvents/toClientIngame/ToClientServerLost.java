package rts.controller.netEvents.toClientIngame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.view.ViewModel;

public class ToClientServerLost implements ToClientEvent
{
    @Override
    public void execute(final ClientController controller)
    {
        final ViewModel viewModel = controller.getViewModel();
        viewModel.deactivateAll();
        JOptionPane.showMessageDialog((JFrame) viewModel.getTopLevelAncestor(),
            "Lost connection to server.");
        viewModel.getGameView().loadMainMenu();
    }
}
