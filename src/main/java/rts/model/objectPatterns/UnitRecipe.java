package rts.model.objectPatterns;

import rts.model.UnitType;

// TODO: Auto-generated Javadoc
/**
 * Recepta na jednostke.
 */
public class UnitRecipe extends Recipe
{
    /** Typ jednostki. */
    private final UnitType type;

    /**
     * Instantiates a new unit recipe.
     * 
     * @param type the type
     * @param productionTime the production time
     */
    public UnitRecipe(final UnitType type, final int productionTime)
    {
        super(productionTime);
        this.type = type;
    }
    /**
     * Gets the type.
     * 
     * @return the type
     */
    public UnitType getType()
    {
        return type;
    }
}