package rts.model.serverBuildings;

import rts.misc.ConstRect;
import rts.misc.Coords;
import rts.model.BuildingType;
import rts.model.ObjectID;

// TODO: Auto-generated Javadoc
/**
 * Interfejs BuildingControl implementuja klasy wewnetrzne w klasie Building
 * mogace utozsamiac sie z budynkiem w sensie polozenia i rozmiarow.
 */
public interface BuildingControl
{
    /**
     * Distance.
     * 
     * @param another the another
     * @return the double
     */
    public double distance(BuildingControl another);
    /**
     * Gets the bounds.
     * 
     * @return the bounds
     */
    public ConstRect getBounds();
    /**
     * Zwraca punkt do ktorego maja podazac jednostki wychodzace z budynku
     * 
     * @return punkt do ktorego maja podazac jednostki wychodzace z budynku
     */
    // public PathNode getDoorNode();
    public Coords getExitPosition();
    /**
     * Zwraca punkt do ktorego maja podazac jednostki wchodzace do budynku
     * 
     * @return punkt do ktorego maja podazac jednostki wchodzace do budynku
     */
    public Coords getEntrancePosition();
    /**
     * Gets the iD.
     * 
     * @return the iD
     */
    public ObjectID getID();
    /**
     * Gets the type.
     * 
     * @return the type
     */
    public BuildingType getType();
}