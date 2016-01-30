package rts.view.screens;

//import input.KeyLink;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rts.controller.PlayerID;
import rts.controller.client.ClientController;
import rts.controller.server.ServerController;
import rts.model.map.MapSettings;
import rts.view.GameView;

// TODO: Auto-generated Javadoc
/**
 * The Class MainMenu.
 */
public class ScreenHost extends ScreenNewGame
{
    private final ServerController serverController;
    protected final JButton startButton;

    public ScreenHost(final ServerController serverController,
        final ClientController clientController, final GameView gameView,
        final MapSettings mapSettings)
    {
        super(clientController, gameView, mapSettings);
        this.serverController = serverController;
        playerArea.setEnabled(true);
        final PlayerPane openPane = playerArea.getOpenSlot();
        openPane.setEnabled(false);
        startButton = new JButton();
        gameView.getImagesAndPatterns().createButton(startButton, "Start", 350, 290);
        startButton.setToolTipText("Start the game.");
        startButton.addActionListener(this);
        add(startButton);
    }
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        super.actionPerformed(e);
        final Object src = e.getSource();
        if(src == startButton)
        {
            serverController.startGame();
        }
        if(src == cancelButton)
        {
            serverController.deactivate();
        }
        else if(src instanceof PlayerPane)
        {
            final PlayerPane playerSlot = (PlayerPane) src;
            if(playerSlot.userPlayer != null)
            {
                final PlayerID player = playerSlot.userPlayer.getID();
                serverController.playerKicked(player);
            }
        }
    }
}
