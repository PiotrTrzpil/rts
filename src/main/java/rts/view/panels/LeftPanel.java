package rts.view.panels;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import rts.misc.ConstRect;
import rts.view.ImagesAndPatterns;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Klasa zarzadzajaca interfejsem po lewej stronie ekranu.
 */
public class LeftPanel
{
    /** The Constant TAB_BUTT_SPACE. */
    public static final int TAB_BUTT_SPACE = 30;
    /** The interf. */
    private final Image interf;
    /** The tab buttons. */
    private JPanel tabButtons;
    /** Polozenie i wymiary panelu interfejsu. */
    public static final ConstRect leftPanelBounds = new ConstRect(0, 0, 151, 480);
    /** Polozenie i wymiary przyciskow zmieniajacych aktywna zakladke. */
    public static final ConstRect tabButtonsBounds = new ConstRect(0, 135, 50, 30);
    /** Polozenie i wymiary minimapy. */
    public static final ConstRect minimapBounds = new ConstRect(15, 15, 100, 100);
    /** Aktualnie wybrana zakladka. */
    private LeftPanelTab currentTab;
    /** The buildings tab button. */
    private JButton buildingsTabButton;
    /** The info tab button. */
    private JButton infoTabButton;
    /** The default info. */
    private final InfoTab defaultInfo;
    /** The all tabs. */
    private final EnumMap<Tab, LeftPanelTab> allTabs;
    /** The minimap. */
    private final Minimap minimap;
    /** The image loader. */
    private final ImagesAndPatterns imageLoader;
    /** The panel. */
    private final Panel panel;

    /**
     * Instantiates a new left panel.
     * 
     * @param viewModel the map enviroment
     */
    public LeftPanel(final ViewModel viewModel)
    {
        imageLoader = viewModel.getImageLoader();
        interf = imageLoader.getBasicImage("LeftPanel");
        allTabs = new EnumMap<Tab, LeftPanelTab>(Tab.class);
        minimap = new Minimap(viewModel);
        minimap.setListener(viewModel.getInputListener());
        minimap.setBounds(minimapBounds.toRectangle());
        panel = new Panel();
        panel.add(minimap);
        defaultInfo = new InfoTab()
        {
            @Override
            protected void paintComponent(final Graphics g)
            {
                super.paintComponent(g);
                g.drawString("INFO", 0, 10);
            }
        };
        makeTabButtons();
        final LeftPanelTab build = new BuildingsTab(viewModel);
        allTabs.put(Tab.BUILD, build);
        allTabs.put(Tab.INFO, defaultInfo);
        currentTab = allTabs.get(Tab.INFO);
        panel.add(currentTab);
    }
    /**
     * Make tab buttons.
     */
    private void makeTabButtons()
    {
        tabButtons = new JPanel();
        tabButtons.setBounds(tabButtonsBounds.toRectangle());
        tabButtons.setOpaque(false);
        buildingsTabButton = new JButton();
        imageLoader.createButton(buildingsTabButton, "BuildingsTab");
        buildingsTabButton.setToolTipText("Buildings Tab");
        buildingsTabButton.addActionListener(panel);
        tabButtons.add(buildingsTabButton);
        infoTabButton = new JButton();
        imageLoader.createButton(infoTabButton, "InfoTab");
        infoTabButton.setToolTipText("Info Tab");
        infoTabButton.addActionListener(panel);
        tabButtons.add(infoTabButton);
        panel.add(tabButtons);
    }
    /**
     * Change tab to.
     * 
     * @param tab the tab
     */
    public void changeTabTo(final Tab tab)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                panel.remove(currentTab);
                currentTab = allTabs.get(tab);
                panel.add(currentTab);
                panel.validate();
                panel.repaint();
            }
        });
    }
    /**
     * Load new info tab.
     * 
     * @param info the info
     */
    public void loadNewInfoTab(final InfoTab info)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                if(currentTab == allTabs.get(Tab.INFO))
                {
                    panel.remove(currentTab);
                    panel.add(info);
                    currentTab = info;
                    panel.validate();
                    panel.repaint();
                }
                allTabs.put(Tab.INFO, info);
            }
        });
    }
    /**
     * Gets the info tab.
     * 
     * @return the info tab
     */
    public InfoTab getInfoTab()
    {
        return defaultInfo;
    }
    /**
     * Order repaint.
     * 
     * @param comp the comp
     */
    public static void orderRepaint(final JComponent comp)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                comp.repaint();
            }
        });
    }
    /**
     * Gets the minimap.
     * 
     * @return the minimap
     */
    public Minimap getMinimap()
    {
        return minimap;
    }
    /**
     * Gets the panel.
     * 
     * @return the panel
     */
    public Component getPanel()
    {
        return panel;
    }

    /**
     * The Class Panel.
     */
    private class Panel extends JPanel implements ActionListener
    {
        /**
         * Instantiates a new panel.
         */
        private Panel()
        {
            super(null);
            setBounds(leftPanelBounds.toRectangle());
        }
        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        protected void paintComponent(final Graphics g)
        {
            super.paintComponent(g);
            final Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(interf, 0, 0, null);
        }
        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        public void actionPerformed(final ActionEvent e)
        {
            final Object src = e.getSource();
            if(src == buildingsTabButton)
            {
                changeTabTo(Tab.BUILD);
            }
            else if(src == infoTabButton)
            {
                changeTabTo(Tab.INFO);
            }
        }
    }
}
