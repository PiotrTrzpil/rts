package rts.view.screens;

//import input.KeyLink;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import rts.controller.UserPlayer;
import rts.controller.client.ClientController;
import rts.model.map.MapSettings;
import rts.view.GameView;
import rts.view.ImagesAndPatterns;

// TODO: Auto-generated Javadoc
/**
 * The Class MainMenu.
 */
public class ScreenNewGame extends Screen implements ActionListener
{
    private final Image background;
    private final Image banner;
    protected final PlayersArea playerArea;
    protected final JButton cancelButton;
    private final ClientController clientController;

    public ScreenNewGame(final ClientController clientController, final GameView gameView,
        final MapSettings mapSettings)
    {
        this.clientController = clientController;
        final ImagesAndPatterns imageLoader = gameView.getImagesAndPatterns();
        background = imageLoader.getBasicImage("MainBackground");
        banner = imageLoader.getBasicImage("MainBanner");
        cancelButton = new JButton();
        imageLoader.createButton(cancelButton, "Cancel", 400, 390);
        cancelButton.addActionListener(this);
        add(cancelButton);
        playerArea = new PlayersArea();
        playerArea.setBounds(20, 20, 300, 300);
        add(playerArea);
        playerArea.createPanels(mapSettings);
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent e)
    {
        final Object source = e.getSource();
        if(source == cancelButton)
        {
            clientController.deactivate();
        }
    }
    public void playerJoined(final UserPlayer player)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                //                P.pr(player.getName());
                playerArea.insertPlayer(player);
            }
        });
    }
    public void playerLeft(final UserPlayer player)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                playerArea.removePlayer(player);
            }
        });
    }
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(final Graphics2D g2d)
    {
        g2d.drawImage(background, 0, 0, null);
        g2d.drawImage(banner, 370, 0, null);
    }

    protected class PlayersArea extends JPanel
    {
        private final LinkedList<PlayerPane> playerPanes;
        private final int distance = 40;

        PlayersArea()
        {
            super(null);
            playerPanes = new LinkedList<PlayerPane>();
        }
        public void insertPlayer(final UserPlayer player)
        {
            for(final PlayerPane pane : playerPanes)
            {
                if(pane.isOpen())
                {
                    //                    P.pr(playerPanes.indexOf(pane));
                    pane.insertPlayer(player);
                    return;
                }
            }
        }
        public void removePlayer(final UserPlayer player)
        {
            for(int i = 0; i < playerPanes.size(); i++)
            {
                final PlayerPane playerPane = playerPanes.get(i);
                if(playerPane.userPlayer.equals(player))
                {
                    // playerPanes.remove(i);
                    playerPane.removePlayer();
                    break;
                }
            }
        }
        public void createPanels(final MapSettings settings)
        {
            for(int i = 0; i < settings.getNumberOfPlayers(); i++)
            {
                final PlayerPane playerPane = new PlayerPane();
                playerPane.setEnabled(false);
                add(playerPane);
            }
        }
        @Override
        public void setEnabled(final boolean b)
        {
            super.setEnabled(b);
            for(final PlayerPane pane : playerPanes)
            {
                pane.setEnabled(b);
            }
        }
        public PlayerPane getOpenSlot()
        {
            for(final PlayerPane pane : playerPanes)
            {
                if(pane.isOpen())
                {
                    return pane;
                }
            }
            return null;
        }
        public Component add(final PlayerPane c)
        {
            super.add(c);
            c.setBounds(0, playerPanes.size() * distance, 300, 30);
            playerPanes.add(c);
            return c;
        }
    }

    protected class PlayerPane extends JComboBox
    {
        // protected PlayerNameSlot box1;
        protected UserPlayer userPlayer;
        private PlayerInSlot playerSlot;

        // @Override
        // public boolean equals(final Object obj)
        // {
        // return userPlayer.equals(obj);
        // }
        public PlayerPane()
        {
            super(new DefaultComboBoxModel());
            addItem(new PlayerInSlot(Slot.OPEN, "Open"));
            addItem(new PlayerInSlot(Slot.CLOSED, "Closed"));
            // box1 = new PlayerNameSlot(this);
            setBounds(0, 0, 120, 30);
        }
        public void insertPlayer(final UserPlayer player)
        {
            if(playerSlot == null)
            {
                userPlayer = player;
                playerSlot = new PlayerInSlot(Slot.PLAYER, player.getName());
                // P.pr("INSERT" + player.getName());
                // playerSlot.setName(userPlayer.getName());
                insertItemAt(playerSlot, 0);
                setSelectedIndex(0);
                addActionListener(ScreenNewGame.this);
            }
        }
        public void removePlayer()
        {
            if(playerSlot != null)
            {
                //                P.pr("FELT");
                // P.pr("REM" + box1.getItemCount());
                userPlayer = null;
                removeItem(playerSlot);
                playerSlot = null;
                removeActionListener(ScreenNewGame.this);
            }
        }
        public boolean isOpen()
        {
            final PlayerInSlot slot = (PlayerInSlot) getSelectedItem();
            return slot.type == Slot.OPEN;
        }
    }

    private class PlayerInSlot
    {
        private final Slot type;
        private final String name;

        public PlayerInSlot(final Slot type, final String name)
        {
            super();
            this.type = type;
            this.name = name;
        }
        @Override
        public String toString()
        {
            return name;
        }
        // @Override
        // public boolean equals(final Object obj)
        // {
        // return type.equals(obj);
        // }
    }
    // public class PlayerNameSlot extends JComboBox
    // {
    // PlayerPane parentPane;
    //
    // public PlayerNameSlot(final PlayerPane par)
    // {
    //
    // }
    // PlayerPane getParentPane()
    // {
    // return parentPane;
    // }
    // }
}
