package rts.view.clientModel;

import rts.controller.UserPlayer;
import rts.misc.Coords;
import rts.model.ObjectID;
import rts.model.UnitType;
import rts.view.ViewModel;
import rts.view.animation.EffectsPool;
import rts.view.animation.StateAnimation;
import rts.view.mapView.Drawable;
import rts.view.mapView.DrawableImage;

// TODO: Auto-generated Javadoc
/**
 * Jednostka mogaca byc kontrolowana przez gracza
 */
public class ControllableUnit extends UnitSprite
{
    /** The selection. */
    StateAnimation selection;

    /**
     * Instantiates a new controllable unit.
     * 
     * @param mapEnv the map env
     * @param type the type
     * @param sprite the sprite
     * @param c the c
     * @param objectID the object id
     * @param owner the owner
     */
    public ControllableUnit(final ViewModel mapEnv, final UnitType type, final ObjectImage sprite,
        final Coords c, final ObjectID objectID, final UserPlayer owner)
    {
        super(type, sprite, c, objectID, owner);
        final EffectsPool effectsPool = mapEnv.getMapView().getEffectsPool();
        selection = (StateAnimation) effectsPool.getAnimation(EffectsPool.Effect.SELECTION);
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.UnitSprite#getSelection()
     */
    @Override
    public Drawable getSelection()
    {
        final DrawableImage drawableImage = selection.getDrawableImage(position.toCoords());
        return drawableImage;
    }
    /**
     * Gets the selection animation.
     * 
     * @return the selection animation
     */
    public StateAnimation getSelectionAnimation()
    {
        return selection;
    }
}
