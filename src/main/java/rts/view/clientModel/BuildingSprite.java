package rts.view.clientModel;

import java.awt.Color;
import rts.controller.UserPlayer;
import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.model.BuildingType;
import rts.model.ObjectID;
import rts.model.objectPatterns.BuildingPattern;
import rts.view.mapView.ColorShape;
import rts.view.mapView.Drawable;

// TODO: Auto-generated Javadoc
/**
 * Reprezentacja budynku po stronie gracza.
 */
public class BuildingSprite extends PlayerObjectSprite
{
    /** The bounds. */
    private final ConstRect bounds;
    /** The pattern. */
    private final BuildingPattern pattern;

    /**
     * Instantiates a new building sprite.
     * 
     * @param sprite the sprite
     * @param pattern the pattern
     * @param c the c
     * @param objectID the object id
     * @param owner the owner
     */
    public BuildingSprite(final BuildingImage sprite, final BuildingPattern pattern,
        final Coords c, final ObjectID objectID, final UserPlayer owner)
    {
        super(sprite, c, objectID, owner, pattern.getType().getMaxHealthPoints());
        this.pattern = pattern;
        bounds = new ConstRect(getPosition(), sprite.getDimensions());
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.misc.Selectable#getSelection()
     */
    @Override
    public Drawable getSelection()
    {
        return new ColorShape(Color.MAGENTA, getBounds());
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.MapObject#getBounds()
     */
    @Override
    public ConstRect getBounds()
    {
        return bounds;
    }
    /**
     * Gets the type.
     * 
     * @return the type
     */
    public BuildingType getType()
    {
        return pattern.getType();
    }
    /**
     * Gets the pattern.
     * 
     * @return the pattern
     */
    public BuildingPattern getPattern()
    {
        return pattern;
    }
}
