package rts.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.TimerTask;
import javax.swing.JTextField;
import rts.controller.Command;
import rts.controller.Timers;
import rts.controller.netEvents.toServerEvents.ToServerChatMessage;
import rts.misc.Coords;
import rts.view.GameView.MainGamePanel;
import rts.view.mapView.Drawable;
import rts.view.mapView.GameGraphics;
import rts.view.mapView.MapView;

// TODO: Auto-generated Javadoc
/**
 * Interfejs czatu w grze.
 */
public class GameChat
{
    /** The map enviroment. */
    private final ViewModel viewModel;
    // / private final boolean active;
    /** The text field. */
    private final JTextField textField;
    /** The main game panel. */
    private final MainGamePanel mainGamePanel;
    /** The string list. */
    private final LinkedList<ChatMessage> stringList;
    /** The Constant lowerLeft. */
    private static final Coords lowerLeft = new Coords(10, MapView.dimY - 40);

    /**
     * The Class Messages.
     */
    /**
     * Instantiates a new game chat.
     * 
     * @param model the map enviroment
     */
    public GameChat(final ViewModel model)
    {
        viewModel = model;
        // active = false;
        mainGamePanel = model.getGameView().getMainGamePanel();
        stringList = new LinkedList<ChatMessage>();
        textField = new JTextField();
        textField.setForeground(Color.white);
        textField.setVisible(false);
        textField.setOpaque(false);
        textField.setBounds(10, lowerLeft.getY() + 10, MapView.dimX - 30, 20);
        model.getMapView().getViewPanel().add(textField);
        textField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(final KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    final String text = textField.getText();
                    if(textField.isVisible())
                    {
                        if(!text.equals(new String()))
                        {
                            viewModel.getQueue().add(new Command()
                            {
                                @Override
                                public void execute()
                                {
                                    viewModel.send(new ToServerChatMessage(text));
                                }
                            });
                        }
                        textField.setText(null);
                        textField.setVisible(false);
                    }
                    mainGamePanel.requestFocusInWindow();
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    textField.setText(null);
                    textField.setVisible(false);
                    mainGamePanel.requestFocusInWindow();
                }
            }
        });
    }
    /**
     * Activate.
     */
    public void activate()
    {
        textField.setVisible(true);
        textField.requestFocusInWindow();
    }
    // public JTextField getTextField()
    // {
    // return textField;
    // }
    /**
     * Dodanie nowej wiadomosci do listy wyswietlanej na ekranie
     * 
     * @param text wiadomosc
     */
    public void receiveText(final String text)
    {
        final ChatMessage chatMessage = new ChatMessage(text);
        stringList.addFirst(chatMessage);
        Timers.animTimer().schedule(new RemoveTask(chatMessage), 30000);
        if(stringList.size() > 10)
        {
            stringList.removeLast();
        }
    }
    /**
     * Gets the string list.
     * 
     * @return the string list
     */
    public Drawable getStringList()
    {
        final LinkedList<String> list = new LinkedList<String>();
        for(final ChatMessage mess : stringList)
        {
            list.add(mess.getMessage());
        }
        return new Messages(list);
    }

    private class Messages implements Drawable
    {
        /** The messages. */
        private final LinkedList<String> messages;

        /**
         * Instantiates a new messages.
         * 
         * @param messages the messages
         */
        private Messages(final LinkedList<String> messages)
        {
            this.messages = messages;
        }

        /** The Constant space. */
        private static final int space = 20;

        /*
         * (non-Javadoc)
         *
         * @see rts.view.mapView.Drawable#draw(rts.view.mapView.GameGraphics)
         */
        @Override
        public void draw(final GameGraphics g)
        {
            final Graphics2D graphics = g.getGraphics();
            int i = 0;
            g.setColor(Color.white);
            for(final String string : messages)
            {
                graphics.drawString(string, lowerLeft.getX(), lowerLeft.getY() - space * i);
                i++;
            }
        }
    }

    /**
     * Zadanie usuniecia wiadomosci z ekranu
     */
    private class RemoveTask extends TimerTask
    {
        /** The chat message. */
        private final ChatMessage chatMessage;

        /**
         * Instantiates a new removes the task.
         * 
         * @param chatMessage the chat message
         */
        public RemoveTask(final ChatMessage chatMessage)
        {
            this.chatMessage = chatMessage;
            // TODO Auto-generated constructor stub
        }
        /*
         * (non-Javadoc)
         *
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run()
        {
            viewModel.getQueue().add(new Command()
            {
                @Override
                public void execute()
                {
                    stringList.remove(chatMessage);
                }
            });
        }
    }
}
