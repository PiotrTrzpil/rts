package rts.model;

import java.io.Serializable;
import rts.model.serverUnits.Archer;
import rts.model.serverUnits.Builder;
import rts.model.serverUnits.Carrier;
import rts.model.serverUnits.Unit;
import rts.model.serverUnits.Warrior;

// TODO: Auto-generated Javadoc
/**
 * Typ jednostki
 */
public enum UnitType implements Serializable
{
    /** The WARRIOR. */
    WARRIOR("Warrior", 250, Warrior.class),
    /** The CARRIER. */
    CARRIER("Carrier", 100, Carrier.class),
    /** The BUILDER. */
    BUILDER("Builder", 100, Builder.class),
    /** The BUILDER. */
    ARCHER("Archer", 190, Archer.class);
    /** The name. */
    private String name;
    private final Class<? extends Unit> uClass;
    private final int maxHealthPoints;

    /**
     * Instantiates a new unit type.
     * 
     * @param n the n
     */
    UnitType(final String n, final int maxHealthPoints, final Class<? extends Unit> uClass)
    {
        name = n;
        this.maxHealthPoints = maxHealthPoints;
        this.uClass = uClass;
    }
    /**
     * Gets the name.
     * 
     * @return the name
     */
    @Override
    public String toString()
    {
        return name;
    }
    public Class<? extends Unit> getUnitClass()
    {
        // TODO Auto-generated method stub
        return uClass;
    }
    public int getMaxHealthPoints()
    {
        return maxHealthPoints;
    }
}
