package rts.view.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import rts.view.GameView;
import rts.view.ViewModel;
import rts.view.input.KeyInputEvent;

public class Console extends Screen
{
    private final JTextArea jTextArea;
    private final JScrollPane jScrollPane;
    private final GameView gameView;
    private final ViewModel mapEnviroment;

    public Console(final ViewModel mapEnviroment)
    {
        this.mapEnviroment = mapEnviroment;
        gameView = mapEnviroment.getGameView();
        //        super(mainMenu);
        final JPanel jPanel = getPanel();
        jPanel.setLayout(new BorderLayout());
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        // jTextArea.setEnabled(false);
        jTextArea.setFocusable(false);
        jTextArea.setForeground(Color.BLACK);
        // final Font font = new Font("Serif", Font.ITALIC, 20);
        //  jTextArea.setFont(font);
        jTextArea.setForeground(Color.blue);
        jScrollPane = new JScrollPane(jTextArea);
        //  final jScrollPane.set
        //     jScrollPane.setLayout(new BorderLayout());
        //     jScrollPane.add(jTextArea);
        add(jScrollPane);
        jScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
        {
            public void adjustmentValueChanged(final AdjustmentEvent e)
            {
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });
        //        jScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
        //        {
        //            public void adjustmentValueChanged(final AdjustmentEvent e)
        //            {
        //                jTextArea.select(jTextArea.getHeight() + 1000, 0);
        //            }
        //        });
    }
    public void print(final String string)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                final String n = string + "\n";
                jTextArea.append(n);
                //        jTextArea.selectAll();
            }
        });
        //   jTextArea.setCaretPosition(jTextArea.getCaretPosition() + n.length());
    }
    @Override
    public void handleKeyEvent(final KeyInputEvent event)
    {
        if(event.wasPressed(KeyEvent.VK_C))
        {
            gameView.load(mapEnviroment);
            // goToParent();
        }
    }
}
