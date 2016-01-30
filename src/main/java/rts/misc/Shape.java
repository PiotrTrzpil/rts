package rts.misc;

import java.io.Serializable;
import java.lang.reflect.Method;
import rts.view.mapView.Drawable;

// TODO: Auto-generated Javadoc
/**
 * Ksztalt.
 */
public abstract class Shape implements Serializable, Drawable
{
    /**
     * Move shape.
     * 
     * @param v the v
     * @return the shape
     */
    public final Shape moveShape(final GameVector v)
    {
        Method declaredMethod;
        try
        {
            declaredMethod = getClass().getDeclaredMethod("moveBy", GameVector.class);
            declaredMethod.invoke(this, v);
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }
}
