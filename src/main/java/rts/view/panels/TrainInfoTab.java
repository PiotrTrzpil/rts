package rts.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import rts.controller.netEvents.toClientIngame.ToClientAddTraining;
import rts.controller.netEvents.toClientIngame.ToClientBuildingEvent;
import rts.controller.netEvents.toClientIngame.ToClientItemEvent;
import rts.controller.netEvents.toClientIngame.ToClientRemoveTraining;
import rts.controller.netEvents.toClientIngame.ToClientStartTraining;
import rts.controller.netEvents.toServerEvents.ToServerAddTraining;
import rts.controller.netEvents.toServerEvents.ToServerCancelTraining;
import rts.model.Item;
import rts.model.TrainID;
import rts.model.UnitType;
import rts.model.objectPatterns.UnitRecipe;
import rts.model.serverBuildings.ItemSlot;
import rts.view.ImagesAndPatterns;
import rts.view.ViewModel;
import rts.view.clientModel.BuildingSprite;

// TODO: Auto-generated Javadoc
/**
 * Klasa panelu informacyjnego budynku trenujacego jednostki
 */
public class TrainInfoTab extends BuildingInfoTab
{
    /** Maksymalna ilosc jednostek w kolejce treningu. */
    private static final int MAX_IN_QUEUE = 4;
    /** Odstep miedzy przyciskami treningu. */
    private static final int TRAINBUTTONS_SPACE = 20;
    /** Panel przyciskow treningu. */
    private final JPanel trainButtons;
    /** Panel z kolejka treningu. */
    private final UnitQueuePanel unitQueuePanel;
    /** Polozenie i wymiary panelu z kolejka treningu. */
    private static final Rectangle unitQueuePanelBounds = new Rectangle(0, 50, 120, 60);
    /** Polozenie i wymiary przycisku aktualnego treningu. */
    private static final Rectangle currentTrainBounds = new Rectangle(5, 5, 20, 20);
    /** Polozenie i wymiary kolejki treningu. */
    private static final Rectangle queueBounds = new Rectangle(0, 30, 120, 20);
    /** Polozenie i wymiary paska postepu. */
    private static final Rectangle progressBarBounds = new Rectangle(30, 0, 80, 20);
    /** Polozenie i wymiary przyciskow treningu. */
    private static final Rectangle trainButtonsBounds = new Rectangle(0, 240, 50, 50);
    /** The image loader. */
    private final ImagesAndPatterns imageLoader;
    // /** The event handler. */
    // private final GameNetEventHandler eventHandler;
    /** Recepty na jednostki. */
    private final EnumMap<UnitType, UnitRecipe> unitRecipes;
    /** Sloty na przedmioty. */
    private final EnumMap<Item, ItemSlot> ingredients;

    /**
     * Instantiates a new train info tab.
     * 
     * @param mapEnviroment the map enviroment
     * @param parentBuilding the parent building
     */
    public TrainInfoTab(final ViewModel mapEnviroment, final BuildingSprite parentBuilding)
    {
        super(mapEnviroment, parentBuilding);
        imageLoader = mapEnviroment.getImageLoader();
        unitRecipes = parentBuilding.getPattern().getUnitRecipes();
        ingredients = parentBuilding.getPattern().cloneIngredientSlots();
        unitQueuePanel = new UnitQueuePanel();
        // unitQueue.setOpaque(false);
        trainButtons = new JPanel();
        trainButtons.setBounds(trainButtonsBounds);
        // unitButtons.setOpaque(false);
        final GridLayout g = new GridLayout(2, 2, 20, 20);
        trainButtons.setLayout(g);
        TrainButton unit;
        for(final UnitRecipe rec : unitRecipes.values())
        {
            unit = new TrainButton(rec);
            imageLoader.createButton(unit, rec.getType().name());
            unit.addActionListener(this);
            trainButtons.add(unit);
        }
        add(unitQueuePanel);
        add(trainButtons);
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * rts.view.panels.BuildingInfoTab#handleBuildingEvent(rts.controller.netEvents
     * .NetBuildingEvent)
     */
    @Override
    public void handleBuildingEvent(final ToClientBuildingEvent buildingCommand)
    {
        if(buildingCommand instanceof ToClientAddTraining)
        {
            final ToClientAddTraining toClientAddTraining = (ToClientAddTraining) buildingCommand;
            unitQueuePanel.addTraining(unitRecipes.get(toClientAddTraining.getUnitType()),
                toClientAddTraining.getTrainID());
        }
        else if(buildingCommand instanceof ToClientRemoveTraining)
        {
            final ToClientRemoveTraining toClientRemoveTraining = (ToClientRemoveTraining) buildingCommand;
            unitQueuePanel.removeTraining(toClientRemoveTraining.getTrainID());
        }
        else if(buildingCommand instanceof ToClientStartTraining)
        {
            final ToClientStartTraining toClientStartTraining = (ToClientStartTraining) buildingCommand;
            unitQueuePanel.startTraining(toClientStartTraining.getTrainID());
        }
        else if(buildingCommand instanceof ToClientItemEvent)
        {
            final ToClientItemEvent itemEvent = (ToClientItemEvent) buildingCommand;
            final ItemSlot itemSlot = ingredients.get(itemEvent.getItem());
            itemSlot.setQuantity(itemEvent.getNumber());
            repaint();
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see rts.view.panels.InfoTab#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        final Object source = e.getSource();
        if(source instanceof TabButton)
        {
            final TabButton tabButton = (TabButton) source;
            tabButton.action();
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see rts.view.panels.BuildingInfoTab#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.black);
        int i = 0, yPos;
        for(final ItemSlot slot : ingredients.values())
        {
            yPos = 130 + i * 20;
            final String name = slot.getItemType().toString();
            g.drawString(name + " " + slot.getQuantity(), 0, yPos);
            // g.drawString(name, 0, yPos);
            i++;
        }
    }

    /**
     * Klasa zarzadzajaca kolejka treningu. Metody modyfikujace kolejke sa
     * wynikiem nadejscia zdarzen z serwera.
     */
    private class UnitQueuePanel extends JPanel
    {
        /** Mapa identyfikatorow odpowiadajacych przyciskom w kolejce treningu. */
        private final Map<TrainID, QueueButton> trainings;
        /** przycisk aktualnego treningu. */
        private QueueButton currentTrain;
        /** panel z przyciskami treningu. */
        private final QueueButtonsPanel queuePanel;
        /** pasek postepu. */
        private final ProgressBar progressBar;

        /**
         * Instantiates a new unit queue.
         */
        private UnitQueuePanel()
        {
            super(null);
            setBounds(unitQueuePanelBounds);
            setBackground(Color.BLUE);
            trainings = new TreeMap<TrainID, QueueButton>();
            queuePanel = new QueueButtonsPanel();
            queuePanel.setBounds(queueBounds);
            add(queuePanel);
            progressBar = new ProgressBar(-1, -1);
            progressBar.setBounds(progressBarBounds);
            add(progressBar);
        }
        /**
         * Dodaje nowy trening do kolejki
         * 
         * @param unit the unit
         * @param trainID the train id
         */
        public void addTraining(final UnitRecipe unit, final TrainID trainID)
        {
            final QueueButton add = queuePanel.add(unit, trainID);
            trainings.put(trainID, add);
        }
        /**
         * usuwa trening w kolejki
         * 
         * @param trainID identyfikator treningu
         */
        public void removeTraining(final TrainID trainID)
        {
            final QueueButton queueButton = trainings.remove(trainID);
            if(queueButton == null)
            {
                return;
            }
            if(queueButton == currentTrain)
            {
                remove(currentTrain);
                currentTrain = null;
                progressBar.cancel();
            }
            else
            {
                queuePanel.remove(queueButton);
            }
            validate();
            repaint();
        }
        /**
         * Usuwa trening z kolejki i wstawia jako aktualny, startuje pasek
         * postepu
         * 
         * @param trainID identyfikator treningu
         */
        public void startTraining(final TrainID trainID)
        {
            currentTrain = trainings.get(trainID);
            queuePanel.remove(currentTrain);
            currentTrain.setBounds(currentTrainBounds);
            add(currentTrain);
            currentTrain.index = -1;
            final int productionTime = currentTrain.unitRecipe.getProductionTime();
            progressBar.reset(productionTime, 50);
            progressBar.start();
            validate();
            repaint();
        }
        /**
         * Dlugosc kolejki
         * 
         * @return dlugosc kolejki
         */
        public int length()
        {
            return queuePanel.queueButtons.size();
        }
    }

    /**
     * Klasa przechowujaca przyciski kolejki treningu
     */
    private class QueueButtonsPanel extends JPanel
    {
        /** Lista przyciskow kolejki. */
        protected ArrayList<QueueButton> queueButtons;

        /**
         * Instantiates a new queue buttons panel.
         */
        public QueueButtonsPanel()
        {
            super(null);
            queueButtons = new ArrayList<QueueButton>();
        }
        /**
         * Dodaje nowy przycisk do kolejki
         * 
         * @param unit recepta na jednostke
         * @param trainID identyfikator treningu
         * @return stworzony przycisk treningu
         */
        public QueueButton add(final UnitRecipe unit, final TrainID trainID)
        {
            final int index = queueButtons.size();
            final QueueButton newTrain = new QueueButton(index, trainID, unit);
            imageLoader.createButton(newTrain, unit.getType().name());
            queueButtons.add(newTrain);
            newTrain.addActionListener(TrainInfoTab.this);
            newTrain.setBounds(index * TRAINBUTTONS_SPACE, 0, 20, 20);
            super.add(newTrain);
            repaint();
            return newTrain;
        }
        /**
         * Usuwa przycisk z kolejki
         * 
         * @param queueButton przycisk treningu
         */
        public void remove(final QueueButton queueButton)
        {
            final int index = queueButton.index;
            final QueueButton button = queueButtons.remove(index);
            super.remove(button);
            final Rectangle rect = new Rectangle();
            for(int i = index; i < queueButtons.size(); i++)
            {
                queueButtons.get(i).getBounds(rect);
                rect.x -= TRAINBUTTONS_SPACE;
                queueButtons.get(i).setBounds(rect);
                queueButtons.get(i).index--;
            }
        }
    }

    /**
     * Klasa z ktorej wywodza sie przyciski tego panelu.
     */
    public abstract class TabButton extends JButton
    {
        /**
         * Wywoluje akcje przy nacisnieciu
         */
        public abstract void action();
    }

    /**
     * Klasa przycisku kolejki treningu
     */
    public class QueueButton extends TabButton
    {
        /** Indeks w kolejce. */
        private int index;
        /** identyfikator treningu */
        private final TrainID trainID;
        /** recepta na jednostke. */
        private final UnitRecipe unitRecipe;

        /**
         * Instantiates a new queue button.
         * 
         * @param index Indeks w kolejce.
         * @param trainID identyfikator treningu
         * @param unitRecipe recepta na jednostke
         */
        public QueueButton(final int index, final TrainID trainID, final UnitRecipe unitRecipe)
        {
            this.index = index;
            this.trainID = trainID;
            this.unitRecipe = unitRecipe;
        }
        /**
         * Wysyla do serwera rzadanie usuniecia treningu
         */
        @Override
        public void action()
        {
            mapEnviroment.send(new ToServerCancelTraining(parentBuilding.getID(), trainID));
        }
    }

    /**
     * Klasa przycisku inicjujacego nowy trening
     */
    public class TrainButton extends TabButton
    {
        /** recepta na jednostke. */
        UnitRecipe unit;

        /**
         * Instantiates a new train button.
         * 
         * @param unit recepta na jednostke
         */
        public TrainButton(final UnitRecipe unit)
        {
            this.unit = unit;
        }
        /**
         * Wysyla do serwera rzadanie dodania treningu
         */
        @Override
        public void action()
        {
            if(unitQueuePanel.length() < MAX_IN_QUEUE)
            {
                mapEnviroment.send(new ToServerAddTraining(parentBuilding.getID(), unit.getType()));
            }
        }
    }
}