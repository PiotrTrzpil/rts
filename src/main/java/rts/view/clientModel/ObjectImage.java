package rts.view.clientModel;

import java.awt.Image;
import rts.misc.GameVector;
import rts.misc.Size;

// TODO: Auto-generated Javadoc
/**
 * Obrazek obiektu
 */
public class ObjectImage
{
    /** Obrazek. */
    private final Image image;
    /** Przesuniecie obrazka. */
    private final GameVector drawShift;
    /** Wymiary obrazka. */
    private final Size dimensions;

    /**
     * Instantiates a new object image.
     * 
     * @param image the image
     * @param dimensions the dimensions
     * @param drawShift the draw shift
     */
    public ObjectImage(final Image image, final Size dimensions, final GameVector drawShift)
    {
        this.image = image;
        this.drawShift = drawShift;
        this.dimensions = dimensions;
    }
    /**
     * Gets the draw shift.
     * 
     * @return the draw shift
     */
    public GameVector getDrawShift()
    {
        return drawShift;
    }
    /**
     * Gets the image.
     * 
     * @return the image
     */
    public Image getImage()
    {
        return image;
    }
    /**
     * Gets the dimensions.
     * 
     * @return the dimensions
     */
    public Size getDimensions()
    {
        return dimensions;
    }
}
