package rts.controller.netEvents.toClientIngame;

import javax.swing.SwingUtilities;
import rts.controller.client.ClientController;
import rts.controller.client.ToClientEvent;
import rts.view.ViewModel;

public class ToClientGameEnd implements ToClientEvent
{
    private final State state;

    public ToClientGameEnd(final State state)
    {
        this.state = state;
    }

    public enum State
    {
        LOSE("You have lost."),
        WIN("You have won.");
        private final String message;

        State(final String message)
        {
            this.message = message;
        }
        public String getMessage()
        {
            return message;
        }
    }

    public State getState()
    {
        return state;
    }
    public void execute(final ClientController controller)
    {
        final ViewModel viewModel = controller.getViewModel();
        viewModel.deactivateAll();
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                viewModel.getGameView().showDialog(state.getMessage());
                viewModel.getGameView().loadMainMenu();
            }
        });
    }
}
