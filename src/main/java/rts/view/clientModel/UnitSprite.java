package rts.view.clientModel;

import java.awt.Color;
import rts.controller.UserPlayer;
import rts.misc.Coords;
import rts.misc.Oval;
import rts.model.AbstractUnit;
import rts.model.Item;
import rts.model.ObjectID;
import rts.model.UnitType;
import rts.model.ingameEvents.ChangeMoveEvent;
import rts.model.serverUnits.Unit;
import rts.view.mapView.ColorShape;
import rts.view.mapView.Drawable;
import rts.view.mapView.DrawableSprite;
import rts.view.panels.InfoTab;

// TODO: Auto-generated Javadoc
/**
 * Reprezentacja jednostki po stronie klienta
 */
public class UnitSprite extends PlayerObjectSprite implements AbstractUnit
{
    /** The type. */
    private final UnitType type;
    /** The current move. */
    private ChangeMoveEvent currentMove;
    /** The item. */
    private Item item;

    /**
     * Instantiates a new unit sprite.
     * 
     * @param type the type
     * @param sprite the sprite
     * @param c the c
     * @param objectID the object id
     * @param owner the owner
     */
    public UnitSprite(final UnitType type, final ObjectImage sprite, final Coords c,
        final ObjectID objectID, final UserPlayer owner)
    {
        super(sprite, c, objectID, owner, type.getMaxHealthPoints());
        this.type = type;
        infoTab = new InfoTab()
        {
        };
        item = Item.NULL_ITEM;
    }
    /**
     * Update position.
     * 
     * @param currentTime the current time
     */
    public void updatePosition(final long currentTime)
    {
        if(currentMove != null)
        {
            final long elapsed = currentTime - currentMove.getExecutionTime();
            position = (currentMove.getPosition().moveBy(currentMove.getVector()
                    .multiplyBy(elapsed)));
        }
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.MapObjectSprite#getDrawableSprite()
     */
    @Override
    public DrawableSprite getDrawableSprite()
    {
        final DrawableSprite drawableSprite = super.getDrawableSprite();
        final Drawable drawable = item.getImage();
        if(drawable instanceof ColorShape)
        {
            final ColorShape shape = (ColorShape) drawable;
            final Coords drawPoint = drawableSprite.getDrawPoint();
            //            final GameVector gameVector = new GameVector(drawPoint.getX(), drawPoint.getY());
            drawableSprite.addDrawable(new ColorShape(shape.getColor(), shape.getShape().moveShape(
                drawPoint.vector())));
        }
        return drawableSprite;
    }
    /* (non-Javadoc)
     * @see rts.view.clientModel.misc.Selectable#getSelection()
     */
    @Override
    public Drawable getSelection()
    {
        return new ColorShape(Color.ORANGE, new Oval(getPosition(), 30, 25));
    }
    /* (non-Javadoc)
     * @see rts.model.AbstractUnit#getType()
     */
    public UnitType getType()
    {
        return type;
    }
    /* (non-Javadoc)
     * @see rts.model.AbstractUnit#getMaxVelocity()
     */
    public float getMaxVelocity()
    {
        return Unit.MAX_VEL;
    }
    /**
     * Sets the item.
     * 
     * @param item the new item
     */
    public void setItem(final Item item)
    {
        this.item = item;
    }
    /* (non-Javadoc)
     * @see rts.model.AbstractUnit#setCurrentMove(rts.model.ingameEvents.ChangeMoveEvent)
     */
    @Override
    public void setCurrentMove(final ChangeMoveEvent changeMoveEvent)
    {
        currentMove = changeMoveEvent;
    }
}
