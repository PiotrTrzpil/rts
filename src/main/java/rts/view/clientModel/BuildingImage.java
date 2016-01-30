package rts.view.clientModel;

import java.awt.Image;
import rts.misc.Coords;
import rts.misc.GameVector;
import rts.misc.TileDimensions;

// TODO: Auto-generated Javadoc
/**
 * Sprajt budynku.
 */
public class BuildingImage extends ObjectImage
{
    /** Wzgledna wartosc wspolrzednej x pozycji wejscia do budynku. */
    private int doorPosX;

    /**
     * Instantiates a new building image.
     * 
     * @param image the image
     * @param tileDim the tile dim
     * @param drawShift the draw shift
     */
    public BuildingImage(final Image image, final TileDimensions tileDim, final GameVector drawShift)
    {
        super(image, tileDim.convertToSize(), drawShift);
    }
    /**
     * Sets the door x.
     * 
     * @param x the new door x
     */
    public void setDoorX(final int x)
    {
        doorPosX = x;
    }
    /**
     * Gets the door x.
     * 
     * @return the door x
     */
    public int getDoorX()
    {
        return doorPosX;
    }
    public Coords getDoorPosition()
    {
        return new Coords(getDoorX(), getDimensions().getDimY());
    }
}
