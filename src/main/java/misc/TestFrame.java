package misc;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class TestFrame.
 */
class SearchComboBox extends JComboBox
{
    private final List<String> stringList;

    public SearchComboBox()
    {
        super(new DefaultComboBoxModel());
        setEditable(true);
        stringList = new LinkedList<String>();
        // this.add
        //
        // addFocusListener(new FocusAdapter()
        // {
        // @Override
        // public void focusGained(final FocusEvent e)
        // {
        // // TODO Auto-generated method stub
        // super.focusGained(e);
        // System.out.println("Action");
        // }
        // });
        // addMouseListener(new MouseAdapter()
        // {
        //
        // @Override
        // public void mousePressed(final MouseEvent e)
        // {
        // System.out.println("Action");
        //
        // }
        //
        // });
        addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(final ItemEvent e)
            {
                // System.out.println("Item");
            }
        });
        addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                // System.out.println("Action");
            }
        });
    }
    public void addStrings(final String... strings)
    {
        for(final String s : strings)
        {
            stringList.add(s);
            addItem(s);
        }
        // setSelectedIndex(-1);
    }
    @Override
    public Object getSelectedItem()
    {
        if(!hasFocus())
        {
            return "Filter...";
        }
        else
        {
            return super.getSelectedItem();
            // final Object item = super.getSelectedItem();
            // if(item == null || item.toString().equals("<Please Choose..>"))
            // {
            // return null;
            // }
            // return item;
        }
    }
}

class SearchTextField extends JTextField
{
}

public class TestFrame extends JPanel
{
    private static int ii = 0;
    /** The window. */
    private final JFrame window;
    private final JTextField field;
    private final JButton button;
    /** The Constant w. */
    public static final int w = 500;
    /** The Constant h. */
    public static final int h = 500;
    {
        // System.out.println("SSSS");
    }

    public TestFrame()
    {
        // super(new GridLayout(1, 0));
        window = new JFrame("TEST");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        setFocusable(true);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(w, h));
        setOpaque(true);
        field = new JTextField();
        field.setText("DDDD");
        button = new JButton();
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent arg0)
            {
                // P.pr(field.getText());
            }
        });
        add(field);
        add(button);
        // final MidiPlayer player = new MidiPlayer();
        // final Sequence sequence = player.getSequence("C:/muz.mid");
        // player.play(sequence, true);
        // final Sequencer sequencer = player.getSequencer();
        // final SearchComboBox searchComboBox = new SearchComboBox();
        // searchComboBox.addStrings("Bida", "Baba", "Bebe");
        // add(searchComboBox);
        window.setContentPane(this);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    @Override
    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        // final ConstRect rect = new ConstRect(0, 0, 40, 72);
        // final ConstRect rect2 = new ConstRect(0, 0, 20, 63);
        // rect.draw(g);
        // rect2.draw(g);
        // g.setColor(Color.green);
        // rect.scaleToFitIn(rect2).centerIn(rect2).draw(g);
    }
    // @Override
    // protected void paintComponent(final Graphics g)
    // {
    // super.paintComponent(g);
    // final Graphics2D g2d = (Graphics2D) g;
    //
    // }
    // private Coords getAttackBuildingPosition(final Coords unitPosition,
    // final ConstRect buildingBounds)
    // {
    // final Line line = new Line(buildingBounds.getCenter(), unitPosition);
    // final List<Line> edgeLines = buildingBounds.getEdgeLines();
    // Coords point;
    // for(final Line edge : edgeLines)
    // {
    // point = line.intersection(edge);
    // if(point != null)
    // {
    // return point.moveBy(line.getVector(10));
    // }
    // }
    // }
    /**
     * The main method.
     * 
     * @param ar the arguments
     */
    public static final void main(final String[] ar)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new TestFrame();
            }
        });
    }
}
//
// import java.awt.Dimension;
// import java.awt.GridLayout;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
//
// public class TestFrame extends JPanel
// {
// private final boolean DEBUG = false;
//
// public TestFrame()
// {
// super(new GridLayout(1, 0));
//
// final String[] columnNames = { "First Name", "Last Name", "Sport",
// "# of Years",
// "Vegetarian" };
//
// final Object[][] data = {
// { "Mary", "Campione", "Snowboarding", new Integer(5), new Boolean(false) },
// { "Alison", "Huml", "Rowing", new Integer(3), new Boolean(true) },
// { "Kathy", "Walrath", "Knitting", new Integer(2), new Boolean(false) },
// { "Sharon", "Zakhour", "Speed reading", new Integer(20), new Boolean(true) },
// { "Philip", "Milne", "Pool", new Integer(10), new Boolean(false) } };
//
// final JTable table = new JTable(data, columnNames);
// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
// table.setFillsViewportHeight(true);
//
// //Create the scroll pane and add the table to it.
// final JScrollPane scrollPane = new JScrollPane(table);
//
// //Add the scroll pane to this panel.
// add(scrollPane);
// }
//
//
// private static void createAndShowGUI()
// {
// //Create and set up the window.
// final JFrame frame = new JFrame("SimpleTableDemo");
// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
// //Create and set up the content pane.
// final TestFrame newContentPane = new TestFrame();
// newContentPane.setOpaque(true); //content panes must be opaque
// frame.setContentPane(newContentPane);
//
// //Display the window.
// frame.pack();
// frame.setVisible(true);
// }
//
// public static void main(final String[] args)
// {
// //Schedule a job for the event-dispatching thread:
// //creating and showing this application's GUI.
// javax.swing.SwingUtilities.invokeLater(new Runnable()
// {
// public void run()
// {
// createAndShowGUI();
// }
// });
// }
// }
