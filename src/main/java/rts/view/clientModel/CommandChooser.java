package rts.view.clientModel;

import rts.controller.netEvents.toServerEvents.ToServerUnitAttackEvent;
import rts.controller.netEvents.toServerEvents.ToServerUnitFollowEvent;
import rts.controller.netEvents.toServerEvents.ToServerUnitMoveEvent;
import rts.misc.Coords;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Obiekt majacy za zadanie wybranie i wyslanie rozkazu dla jednostki.
 */
public class CommandChooser
{
    /** The view model. */
    private final ViewModel viewModel;

    /**
     * Instantiates a new command chooser.
     * 
     * @param model the model
     */
    public CommandChooser(final ViewModel model)
    {
        viewModel = model;
    }
    /**
     * Move.
     * 
     * @param unit the unit
     * @param destination the destination
     */
    public void move(final ControllableUnit unit, final Coords destination)
    {
        viewModel.send(new ToServerUnitMoveEvent(unit.getID(), destination));
        // destination))
    }
    /**
     * Follow.
     * 
     * @param unit the unit
     * @param target the target
     */
    public void follow(final ControllableUnit unit, final UnitSprite target)
    {
        viewModel.send(new ToServerUnitFollowEvent(unit.getID(), target.getID()));
    }
    /**
     * Attack.
     * 
     * @param unit the unit
     * @param target the target
     */
    public void attack(final ControllableUnit unit, final PlayerObjectSprite target)
    {
        final ToServerUnitAttackEvent netUnitAttackEvent = new ToServerUnitAttackEvent(unit.getID(), target
                .getID());
        viewModel.send(netUnitAttackEvent);
    }
}
