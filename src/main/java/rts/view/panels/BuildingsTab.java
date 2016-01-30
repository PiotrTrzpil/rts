package rts.view.panels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import rts.controller.Command;
import rts.model.BuildingType;
import rts.view.ViewModel;
import rts.view.clientModel.BuildingImage;
import rts.view.mapView.ControlModeBuild;

// TODO: Auto-generated Javadoc
/**
 * Panel z przyciskami do tworzenia budynkow
 */
public class BuildingsTab extends LeftPanelTab
{
    /** The Constant ROWS. */
    public static final int ROWS = 3;
    /** The Constant COLUMNS. */
    public static final int COLUMNS = 3;
    /** The Constant SPACE_H. */
    public static final int SPACE_H = 10;
    /** The Constant SPACE_V. */
    public static final int SPACE_V = 10;
    /** The Constant packageName. */
    public static final String packageName = "objects.buildings.";
    /** The buttons. */
    private final ArrayList<JButton> buttons;
    private final ViewModel viewModel;

    public BuildingsTab(final ViewModel model)
    {
        viewModel = model;
        final GridLayout g = new GridLayout(ROWS, COLUMNS, SPACE_H, SPACE_V);
        setLayout(g);
        buttons = new ArrayList<JButton>();
        for(final BuildingType type : BuildingType.values())
        {
            buttons.add(makeButton(type, type.toString()));
        }
    }
    /**
     * Make button.
     * 
     * @param name the name
     * @param toolTip the tool tip
     * @return the j button
     */
    private JButton makeButton(final BuildingType type, final String toolTip)
    {
        final BuildButton jb = new BuildButton(type);
        viewModel.getImageLoader().createButton(jb, type.name());
        jb.setToolTipText(toolTip);
        jb.addActionListener(this);
        add(jb);
        return jb;
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent e)
    {
        final Object src = e.getSource();
        if(src instanceof BuildButton)
        {
            final BuildButton buildButton = (BuildButton) src;
            viewModel.getQueue().add(new Command()
            {
                @Override
                public void execute()
                {
                    final BuildingImage buildingSprite = viewModel.getImageLoader()
                            .getBuildingSprite(buildButton.type);
                    viewModel.getMapView().setControlMode(
                        new ControlModeBuild(buildButton.type, buildingSprite));
                    viewModel.getObjectHolder().deselect();
                }
            });
        }
    }

    private class BuildButton extends JButton
    {
        public BuildButton(final BuildingType type2)
        {
            type = type2;
        }

        private final BuildingType type;
    }
}
